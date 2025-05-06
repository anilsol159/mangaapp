package com.example.mangaapp.ui.face

import android.graphics.RectF
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class FaceDetectionState(
    val isFrameInBounds: Boolean = false,
    val hasCameraPermission: Boolean = false,
    val faceDetected: Boolean = false
)

@HiltViewModel
class FaceDetectionViewModel @Inject constructor(
    private val faceDetectorHelper: FaceDetectorHelper
) : ViewModel() {

    private val _state = MutableStateFlow(FaceDetectionState())
    val state: StateFlow<FaceDetectionState> = _state.asStateFlow()

    init {
        faceDetectorHelper.onFaceDetectionResultListener = { result ->
            // Update face detected state
            val faceDetected = result != null && result.detections().isNotEmpty()
            _state.value = _state.value.copy(faceDetected = faceDetected)
        }
    }

    fun updateCameraPermission(granted: Boolean) {
        _state.value = _state.value.copy(hasCameraPermission = granted)
        if (granted) {
            setupFaceDetector()
        }
    }

    fun setupFaceDetector() {
        faceDetectorHelper.setupFaceDetector()
    }

    fun processFaceDetection(imageProxy: ImageProxy, referenceRect: RectF) {
        faceDetectorHelper.detectAsync(imageProxy)

        // Check if face is within reference rectangle
        val isInBounds = faceDetectorHelper.getFaceInBoundsStatus(referenceRect)

        // Update state
        _state.value = _state.value.copy(
            isFrameInBounds = isInBounds
        )
    }

    fun releaseFaceDetector() {
        faceDetectorHelper.release()
    }

    override fun onCleared() {
        super.onCleared()
        faceDetectorHelper.release()
    }
}