package com.raithavarta.app.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.raithavarta.app.data.db.DiagnosisEntity
import kotlinx.coroutines.tasks.await

/**
 * FirebaseManager
 * ───────────────
 * Wraps Firebase Auth (anonymous sign-in) and Firestore read/write.
 *
 * Auth strategy:
 *   • The app signs in anonymously so every device has a stable UID
 *     without requiring the farmer to enter a phone/email.
 *   • User profile (name, district, crops, PIN hash) is stored in
 *     Firestore under  users/{uid}
 *   • Diagnosis history is mirrored from Room → Firestore under
 *     users/{uid}/diagnoses/{docId}
 *
 * All suspend functions must be called from a coroutine scope.
 */
object FirebaseManager {

    // ── Firebase instances ───────────────────────────────────────
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    /** Currently signed-in user, or null if not yet authenticated. */
    val currentUser: FirebaseUser? get() = auth.currentUser

    /** Firestore path for the current user's profile document. */
    private fun userDoc() = db.collection("users").document(currentUser!!.uid)

    /** Firestore sub-collection for the current user's diagnoses. */
    private fun diagnosisCol() = userDoc().collection("diagnoses")

    // ── Auth ─────────────────────────────────────────────────────

    /**
     * Sign in anonymously if not already authenticated.
     * Safe to call multiple times — is a no-op when a session already exists.
     */
    suspend fun ensureSignedIn() {
        if (currentUser == null) {
            auth.signInAnonymously().await()
        }
    }

    /** Returns true if a Firebase session exists. */
    fun isSignedIn(): Boolean = currentUser != null

    // ── User Profile ─────────────────────────────────────────────

    /**
     * Save / merge user profile data to Firestore.
     * Uses SetOptions.merge() so partial updates don't overwrite other fields.
     */
    suspend fun saveUserProfile(
        name: String,
        district: String,
        crops: String,
        pinHash: String = ""
    ) {
        ensureSignedIn()
        val data = hashMapOf(
            "name"     to name,
            "district" to district,
            "crops"    to crops,
            "pinHash"  to pinHash,
            "updatedAt" to com.google.firebase.Timestamp.now()
        )
        userDoc().set(data, SetOptions.merge()).await()
    }

    /**
     * Fetch the user profile from Firestore.
     * Returns a map of field → value, or null if the document doesn't exist yet.
     */
    suspend fun getUserProfile(): Map<String, Any?>? {
        ensureSignedIn()
        val snap = userDoc().get().await()
        return if (snap.exists()) snap.data else null
    }

    /**
     * Update a single field in the user profile (e.g. "district").
     */
    suspend fun updateUserField(field: String, value: Any) {
        ensureSignedIn()
        userDoc().update(field, value).await()
    }

    // ── Diagnosis History ────────────────────────────────────────

    /**
     * Mirror a Room DiagnosisEntity to Firestore.
     * Uses the local Room id as the Firestore document ID for idempotency.
     */
    suspend fun saveDiagnosis(entity: DiagnosisEntity) {
        ensureSignedIn()
        val data = hashMapOf(
            "cropName"  to entity.cropName,
            "cropEmoji" to entity.cropEmoji,
            "resultText" to entity.resultText,
            "lang"      to entity.lang,
            "timestamp" to entity.timestamp,
            "savedAt"   to com.google.firebase.Timestamp.now()
        )
        // Use timestamp as doc ID so it's naturally unique per diagnosis
        diagnosisCol().document(entity.timestamp.toString()).set(data).await()
    }

    /**
     * Fetch the 10 most recent diagnoses from Firestore.
     * Falls back gracefully — if offline the local Room DB is the source of truth.
     */
    suspend fun getRecentDiagnoses(): List<DiagnosisEntity> {
        ensureSignedIn()
        return try {
            diagnosisCol()
                .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .await()
                .documents
                .mapNotNull { doc ->
                    DiagnosisEntity(
                        id         = 0,
                        cropName   = doc.getString("cropName")  ?: "",
                        cropEmoji  = doc.getString("cropEmoji") ?: "",
                        resultText = doc.getString("resultText") ?: "",
                        lang       = doc.getString("lang")      ?: "en",
                        timestamp  = doc.getLong("timestamp")   ?: 0L
                    )
                }
        } catch (e: Exception) {
            // Return empty list; UI will fall back to Room LiveData
            emptyList()
        }
    }

    /**
     * Clear all Firestore diagnoses for the current user (mirrors Room clearAll).
     */
    suspend fun clearAllDiagnoses() {
        ensureSignedIn()
        val docs = diagnosisCol().get().await().documents
        val batch = db.batch()
        docs.forEach { batch.delete(it.reference) }
        batch.commit().await()
    }
}
