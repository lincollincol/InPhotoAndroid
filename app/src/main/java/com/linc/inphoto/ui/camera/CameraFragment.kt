package com.linc.inphoto.ui.camera

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.linc.inphoto.R
import com.linc.inphoto.databinding.FragmentCameraBinding
import com.linc.inphoto.ui.base.fragment.BaseFragment
import com.linc.inphoto.ui.camera.model.CameraIntent
import com.linc.inphoto.ui.main.BottomBarViewModel
import com.linc.inphoto.utils.extensions.*
import com.linc.inphoto.utils.extensions.view.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class CameraFragment : BaseFragment(R.layout.fragment_camera) {

    companion object {
        private const val INTENT_ARG = "intent_key"

        @JvmStatic
        fun newInstance(intent: CameraIntent) = CameraFragment().apply {
            arguments = bundleOf(INTENT_ARG to intent)
        }
    }

    override val viewModel: CameraViewModel by viewModels()
    private val bottomBarViewModel: BottomBarViewModel by activityViewModels()
    private val binding by viewBinding(FragmentCameraBinding::bind)
    private var cameraExecutor: ExecutorService? = null
    private var lensFacing: Int? = null
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { allowed ->
        viewModel.updateCameraPermissions(allowed)
    }

    override suspend fun observeUiState() = with(binding) {
        viewModel.uiState.collect { state ->
            cameraControllersGroup.show(state.cameraPermissionsGranted)
            permissionsLayout.root.show(!state.cameraPermissionsGranted)
        }
    }

    override fun onStart() {
        super.onStart()
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize our background executor
        cameraExecutor = Executors.newSingleThreadExecutor()

        with(binding) {
            permissionsLayout.apply {
                permissionTextView.setText(R.string.permission_camera_description)
                allowButton.setOnClickListener {
                    val permission = Manifest.permission.CAMERA
                    if (permissionDisabled(permission)) {
                        viewModel.openSettings()
                        return@setOnClickListener
                    }
                    requestPermissionLauncher.launch(permission)
                }
            }
            flipCameraButton.setOnClickListener {
                flipCameraLens()
            }

            takePictureButton.setOnClickListener {
                imageCapture?.takePicture(
                    getOutputFileOptions(requireContext().contentResolver),
                    cameraExecutor!!,
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            viewModel.handleCapturedImage(
                                getArgument(INTENT_ARG),
                                outputFileResults.savedUri
                            )
                        }

                        override fun onError(error: ImageCaptureException) {
                            Timber.e(error)
                        }
                    })
            }
        }
        // TODO: 17.04.22 move ui state logic to view model
        setupCamera()
        bottomBarViewModel.hideBottomBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor?.shutdown()
        cameraProvider?.unbindAll()
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

    // TODO: 10.04.22 uiState
    private fun flipCameraLens() {
        lensFacing = when (lensFacing) {
            CameraSelector.LENS_FACING_BACK -> CameraSelector.LENS_FACING_FRONT
            else -> CameraSelector.LENS_FACING_BACK
        }
        updatePreview()
    }

}