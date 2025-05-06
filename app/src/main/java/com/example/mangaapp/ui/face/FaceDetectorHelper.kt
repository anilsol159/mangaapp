package com.example.mangaapp.ui.face

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facedetector.FaceDetector
import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult
import com.google.mediapipe.framework.image.BitmapImageBuilder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FaceDetectorHelper @Inject constructor(
    private val context: Context
) {
    private var faceDetector: FaceDetector? = null
    private var lastFaceDetectionResult: FaceDetectorResult? = null
    private var isDetecting = false

    var onFaceDetectionResultListener: ((FaceDetectorResult?) -> Unit)? = null

    fun setupFaceDetector(threshold: Float = 0.5f) {
        try {
            // The model file name. Make sure this file exists in your assets folder
            val modelName = "blaze_face_short_range.tflite"

            val baseOptionsBuilder = BaseOptions.builder()
                .setModelAssetPath(modelName)

            val optionsBuilder = FaceDetector.FaceDetectorOptions.builder()
                .setBaseOptions(baseOptionsBuilder.build())
                .setMinDetectionConfidence(threshold)
                .setRunningMode(RunningMode.IMAGE)

            val options = optionsBuilder.build()

            faceDetector = FaceDetector.createFromOptions(context, options)

            Log.d(TAG, "Face detector setup complete")
        } catch (e: Exception) {
            Log.e(TAG, "Error setting up face detector: ${e.message}")
            e.printStackTrace()
        }
    }

    fun detectAsync(imageProxy: ImageProxy) {
        if (isDetecting || faceDetector == null) {
            imageProxy.close()
            return
        }

        isDetecting = true

        try {
            val bitmap = imageProxy.toBitmap()
            if (bitmap != null) {
                val mpImage = BitmapImageBuilder(bitmap).build()
                val detectionResult = faceDetector?.detect(mpImage)
                onFaceDetectionResultListener?.invoke(detectionResult)
                lastFaceDetectionResult = detectionResult

                if (detectionResult != null) {
                    Log.d(TAG, "Detected ${detectionResult.detections().size} faces")
                }

                // Log rectangle coordinates for debugging
                detectionResult?.detections()?.forEach { detection ->
                    val box = detection.boundingBox()
                    Log.d(TAG, "Face bounding box: ${box.left}, ${box.top}, ${box.right}, ${box.bottom}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error detecting faces: ${e.message}")
            e.printStackTrace()
            onFaceDetectionResultListener?.invoke(null)
        } finally {
            isDetecting = false
            imageProxy.close()
        }
    }

    fun getFaceInBoundsStatus(referenceRect: RectF): Boolean {
        // Get the latest detection result
        val detections = lastFaceDetectionResult?.detections() ?: return false

        // If no faces detected, return false
        if (detections.isEmpty()) return false

        // Take the first face detected (assuming we're just focusing on one face)
        val detection = detections[0]

        // Get bounding box
        val boundingBox = detection.boundingBox()

        // Log both rectangles for debugging
        Log.d(TAG, "Reference rectangle: ${referenceRect.left}, ${referenceRect.top}, ${referenceRect.right}, ${referenceRect.bottom}")
        Log.d(TAG, "Face bounding box: ${boundingBox.left}, ${boundingBox.top}, ${boundingBox.right}, ${boundingBox.bottom}")

        // Calculate face center
        val centerX = boundingBox.centerX()
        val centerY = boundingBox.centerY()

        // Check if face center is within reference rectangle
        val isInBounds = referenceRect.contains(centerX, centerY)
        Log.d(TAG, "Face in bounds: $isInBounds")

        return isInBounds
    }

    fun release() {
        faceDetector?.close()
        faceDetector = null
        lastFaceDetectionResult = null
    }

    companion object {
        private const val TAG = "FaceDetectorHelper"
    }
}

// Extension function to convert ImageProxy to Bitmap
fun ImageProxy.toBitmap(): Bitmap? {
    val buffer = planes[0].buffer
    val pixelStride = planes[0].pixelStride
    val rowStride = planes[0].rowStride
    val rowPadding = rowStride - pixelStride * width

    // Create bitmap
    val bitmap = Bitmap.createBitmap(
        width + rowPadding / pixelStride,
        height,
        Bitmap.Config.ARGB_8888
    )

    buffer.rewind()
    bitmap.copyPixelsFromBuffer(buffer)

    // Crop if needed due to padding
    return if (rowPadding > 0) {
        Bitmap.createBitmap(bitmap, 0, 0, width, height)
    } else {
        bitmap
    }
}