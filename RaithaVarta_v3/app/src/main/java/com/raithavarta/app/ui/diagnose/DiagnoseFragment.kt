package com.raithavarta.app.ui.diagnose

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.raithavarta.app.R
import com.raithavarta.app.data.CropOption
import com.raithavarta.app.data.DataRepository
import com.raithavarta.app.data.LanguageManager
import com.raithavarta.app.databinding.FragmentDiagnoseBinding
import com.raithavarta.app.ui.adapters.CropOptionAdapter
import com.raithavarta.app.ui.adapters.DiagnosisHistoryAdapter

class DiagnoseFragment : Fragment() {

    private var _binding: FragmentDiagnoseBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DiagnoseViewModel by viewModels()

    private var selectedCrop: CropOption? = null
    private var imageUri: Uri? = null
    private lateinit var lang: String

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                imageUri = uri
                binding.previewImg.setImageURI(uri)
                binding.previewImg.visibility = View.VISIBLE
                refreshAnalyzeEnabled()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiagnoseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lang = LanguageManager.getCurrentLang(requireContext())

        // Crop grid
        binding.cropOptions.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.cropOptions.adapter = CropOptionAdapter(DataRepository.cropOptions, lang) { crop ->
            selectedCrop = crop
            refreshAnalyzeEnabled()
        }

        // History list
        val historyAdapter = DiagnosisHistoryAdapter()
        binding.historyRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecycler.adapter = historyAdapter

        // Upload
        binding.uploadArea.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
            pickImage.launch(intent)
        }

        // Analyze button
        binding.analyzeBtn.setOnClickListener {
            val crop = selectedCrop ?: return@setOnClickListener
            val uri  = imageUri  ?: return@setOnClickListener
            viewModel.analyze(uri, crop, lang)
        }

        // Observe ViewModel state
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DiagnoseViewModel.AnalyzeState.Idle -> {
                    binding.analyzeBtn.isEnabled = (selectedCrop != null && imageUri != null)
                    binding.analyzeBtn.text = getString(R.string.analyze_btn)
                    binding.aiResult.visibility = View.GONE
                }
                is DiagnoseViewModel.AnalyzeState.Loading -> {
                    binding.analyzeBtn.isEnabled = false
                    binding.analyzeBtn.text = getString(R.string.analyzing_btn)
                    binding.aiResult.visibility = View.VISIBLE
                    binding.aiResultText.text =
                        if (lang == "kn") "ವಿಶ್ಲೇಷಿಸಲಾಗುತ್ತಿದೆ…" else "Analyzing…"
                }
                is DiagnoseViewModel.AnalyzeState.Success -> {
                    binding.analyzeBtn.isEnabled = true
                    binding.analyzeBtn.text = getString(R.string.analyze_btn)
                    binding.aiResult.visibility = View.VISIBLE
                    binding.aiResultText.text = state.text
                }
                is DiagnoseViewModel.AnalyzeState.Error -> {
                    binding.analyzeBtn.isEnabled = true
                    binding.analyzeBtn.text = getString(R.string.analyze_btn)
                    binding.aiResult.visibility = View.VISIBLE
                    val prefix = if (lang == "kn") "⚠️ ದೋಷ: " else "⚠️ Error: "
                    binding.aiResultText.text = prefix + state.message
                }
            }
        }

        // Observe Room DB — history updates automatically
        viewModel.historyList.observe(viewLifecycleOwner) { list ->
            if (list.isNullOrEmpty()) {
                binding.historySection.visibility = View.GONE
            } else {
                binding.historySection.visibility = View.VISIBLE
                historyAdapter.submitList(list)
            }
        }

        refreshAnalyzeEnabled()
    }

    private fun refreshAnalyzeEnabled() {
        val idle = viewModel.state.value is DiagnoseViewModel.AnalyzeState.Idle ||
                   viewModel.state.value is DiagnoseViewModel.AnalyzeState.Success ||
                   viewModel.state.value is DiagnoseViewModel.AnalyzeState.Error
        binding.analyzeBtn.isEnabled = idle && selectedCrop != null && imageUri != null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
