package com.raithavarta.app.ui.diagnose

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raithavarta.app.data.CropOption
import com.raithavarta.app.data.FirebaseManager
import com.raithavarta.app.data.GroqClient
import com.raithavarta.app.data.db.AppDatabase
import com.raithavarta.app.data.db.DiagnosisEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for the Diagnose screen.
 * Keeps AI call logic and DB operations out of the Fragment.
 *
 * Firebase integration:
 *   • After every successful diagnosis the result is saved to Room (local)
 *     AND mirrored to Firestore under users/{uid}/diagnoses/{timestamp}.
 *   • Firestore sync is best-effort — an offline device still works via Room.
 */
class DiagnoseViewModel(application: Application) : AndroidViewModel(application) {

    private val db  = AppDatabase.getInstance(application)
    private val dao = db.diagnosisDao()

    /** Live list of recent diagnoses — Room updates this automatically. */
    val historyList: LiveData<List<DiagnosisEntity>> = dao.getRecentDiagnoses()

    private val _state = MutableLiveData<AnalyzeState>(AnalyzeState.Idle)
    val state: LiveData<AnalyzeState> = _state

    sealed class AnalyzeState {
        object Idle    : AnalyzeState()
        object Loading : AnalyzeState()
        data class Success(val text: String) : AnalyzeState()
        data class Error(val message: String) : AnalyzeState()
    }

    /**
     * Run leaf analysis on a background thread, then save result to Room + Firestore.
     */
    fun analyze(imageUri: Uri, crop: CropOption, lang: String) {
        _state.value = AnalyzeState.Loading

        viewModelScope.launch {
            val cropName = if (lang == "kn") crop.nameKn else crop.nameEn
            val prompt   = buildPrompt(lang, cropName)

            val result = withContext(Dispatchers.IO) {
                GroqClient.analyzeLeaf(getApplication(), imageUri, prompt)
            }

            when (result) {
                is GroqClient.Result.Success -> {
                    val entity = DiagnosisEntity(
                        cropName   = cropName,
                        cropEmoji  = crop.emoji,
                        resultText = result.text,
                        lang       = lang,
                        timestamp  = System.currentTimeMillis()
                    )

                    withContext(Dispatchers.IO) {
                        // 1. Save locally — always works (offline-safe)
                        dao.insert(entity)

                        // 2. Mirror to Firestore (best-effort)
                        try {
                            FirebaseManager.saveDiagnosis(entity)
                        } catch (_: Exception) {
                            // Silently ignore — Room is the source of truth
                        }
                    }
                    _state.value = AnalyzeState.Success(result.text)
                }
                is GroqClient.Result.Error -> {
                    _state.value = AnalyzeState.Error(result.message)
                }
            }
        }
    }

    fun resetState() {
        _state.value = AnalyzeState.Idle
    }

    private fun buildPrompt(lang: String, cropName: String): String =
        if (lang == "kn") {
            """
            ನೀವು ಕೃಷಿ ತಜ್ಞ. ಈ $cropName ಎಲೆಯ ಫೋಟೋವನ್ನು ವಿಶ್ಲೇಷಿಸಿ.

            ಈ ಸ್ವರೂಪದಲ್ಲಿ ಕನ್ನಡದಲ್ಲಿ ಉತ್ತರಿಸಿ:
            🔬 ಗುರುತಿಸಲಾಗಿದೆ: <ರೋಗ/ಸಮಸ್ಯೆಯ ಹೆಸರು>

            🌱 ಲಕ್ಷಣಗಳು:
            • <ಲಕ್ಷಣ 1>
            • <ಲಕ್ಷಣ 2>

            💊 ಶಿಫಾರಸುಗಳು:
            • <ಚಿಕಿತ್ಸೆ 1 — ಪ್ರಮಾಣ ಸಹಿತ>
            • <ಚಿಕಿತ್ಸೆ 2>
            • <ತಡೆಗಟ್ಟುವಿಕೆ ಸಲಹೆ>

            📞 ಹೆಚ್ಚಿನ ಸಹಾಯಕ್ಕಾಗಿ ಸಮೀಪದ KVK ಗೆ ಸಂಪರ್ಕಿಸಿ.

            ಸಂಕ್ಷಿಪ್ತ, ಪ್ರಾಯೋಗಿಕ ಮತ್ತು ಸಣ್ಣ ರೈತರಿಗೆ ಕೈಗೆಟಕುವಂತಿರಲಿ.
            """.trimIndent()
        } else {
            """
            You are an agricultural expert. Analyze this $cropName leaf photo.

            Reply in English using exactly this format:
            🔬 Detected: <disease / problem name>

            🌱 Symptoms:
            • <symptom 1>
            • <symptom 2>

            💊 Recommendations:
            • <treatment 1 with dosage>
            • <treatment 2>
            • <prevention tip>

            📞 Contact your nearest Krishi Vigyana Kendra (KVK) for further help.

            Keep it concise, practical and affordable for small farmers.
            """.trimIndent()
        }
}
