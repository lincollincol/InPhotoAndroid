package com.linc.inphoto.ui.camera

import android.os.Bundle
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentCameraBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.utils.extensions.aspectRatio
import com.linc.inphoto.utils.extensions.getFirstAvailableCameraLens
import com.linc.inphoto.utils.extensions.getOutputFileOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class CameraFragment : BaseFragment(R.layout.fragment_camera) {

    companion object {
        @JvmStatic
        fun newInstance() = CameraFragment()
    }

    override val viewModel: CameraViewModel by viewModels()
    private val binding by viewBinding(FragmentCameraBinding::bind)

    private var cameraExecutor: ExecutorService? = null
    private var lensFacing: Int? = null
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var cameraProvider: ProcessCameraProvider? = null

    override suspend fun observeUiState() {
        viewModel.uiState.collect { state ->
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor?.shutdown()
        cameraProvider?.unbindAll()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        with(binding) {
            flipCameraButton.setOnClickListener {
                flipCameraLens()
            }

            takePictureButton.setOnClickListener {
                imageCapture?.takePicture(
                    getOutputFileOptions(requireContext().contentResolver),
                    cameraExecutor!!,
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            viewModel.handleCapturedImage(outputFileResults.savedUri)
                        }

                        override fun onError(error: ImageCaptureException) {
                            Timber.e(error)
                        }
                    })
            }
        }

        setupCamera()
    }

    private fun setupCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener(Runnable {
            cameraProvider = cameraProviderFuture.get()
            // Select lensFacing depending on the available cameras
            lensFacing = cameraProvider.getFirstAvailableCameraLens()
            updatePreview()
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun updatePreview() {
        preview = Preview.Builder()
            // We request aspect ratio but no resolution
            .setTargetAspectRatio(aspectRatio())
            .build()

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(lensFacing!!)
            .build()

        imageCapture = ImageCapture.Builder()
            .setTargetRotation(requireView().display.rotation)
            .build()

        cameraProvider?.unbindAll()

        cameraProvider?.bindToLifecycle(
            this,
            cameraSelector,
            imageCapture,
            preview
        )

        // Attach the viewfinder's surface provider to preview use case
        preview?.setSurfaceProvider(binding.cameraPreviewView.surfaceProvider)
    }

    private fun flipCameraLens() {
        lensFacing = when (lensFacing) {
            CameraSelector.LENS_FACING_BACK -> CameraSelector.LENS_FACING_FRONT
            else -> CameraSelector.LENS_FACING_BACK
        }
        updatePreview()
    }

}