package com.example.mangaapp.ui.face

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import android.graphics.RectF
import androidx.compose.foundation.background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaceDetectionScreen(
    viewModel: FaceDetectionViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val density = LocalDensity.current

    // State to track screen dimensions
    var screenWidth by remember { mutableStateOf(0f) }
    var screenHeight by remember { mutableStateOf(0f) }

    // Request camera permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.updateCameraPermission(isGranted)
    }

    // Check if permission is already granted
    LaunchedEffect(Unit) {
        val permissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        viewModel.updateCameraPermission(permissionGranted)
    }

    // Handle lifecycle events
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> viewModel.setupFaceDetector()
                Lifecycle.Event.ON_PAUSE -> viewModel.releaseFaceDetector()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Face Detection") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Get the screen dimensions
            screenWidth = with(density) { maxWidth.toPx() }
            screenHeight = with(density) { maxHeight.toPx() }

            if (!state.hasCameraPermission) {
                // Show permission request
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Camera permission is required for face detection",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(
                        onClick = {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    ) {
                        Text("Grant Permission")
                    }
                }
            } else {
                // Show camera preview with face detection
                Box(modifier = Modifier.fillMaxSize()) {
                    CameraPreview(
                        modifier = Modifier.fillMaxSize(),
                        onFaceDetection = { imageProxy ->
                            // Calculate reference rectangle based on the image size
                            val imageWidth = imageProxy.width.toFloat()
                            val imageHeight = imageProxy.height.toFloat()

                            // Calculate reference rectangle (25% from edges)
                            val referenceRect = RectF(
                                imageWidth * 0.25f,
                                imageHeight * 0.25f,
                                imageWidth * 0.75f,
                                imageHeight * 0.75f
                            )

                            viewModel.processFaceDetection(imageProxy, referenceRect)
                        }
                    )

                    // Draw reference rectangle and face bounding boxes
                    FaceDetectionOverlay(
                        isFrameInBounds = state.isFrameInBounds,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Add a text overlay to display the status
                    Text(
                        text = if (state.isFrameInBounds) "Face in bounds!" else "Position your face in the rectangle",
                        color = if (state.isFrameInBounds) Color.Green else Color.Red,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 16.dp)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    // Add debug info
                    Text(
                        text = "Face detected: ${state.faceDetected}",
                        color = Color.White,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(bottom = 16.dp, start = 16.dp)
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FaceDetectionOverlay(
    isFrameInBounds: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Define reference rectangle (center of the screen)
        val referenceLeft = canvasWidth * 0.25f
        val referenceTop = canvasHeight * 0.25f
        val referenceWidth = canvasWidth * 0.5f
        val referenceHeight = canvasHeight * 0.5f

        // Draw reference rectangle with color based on face detection status
        val rectangleColor = if (isFrameInBounds) Color.Green else Color.Red

        drawRect(
            color = rectangleColor,
            topLeft = Offset(referenceLeft, referenceTop),
            size = Size(referenceWidth, referenceHeight),
            style = Stroke(width = 5.dp.toPx())
        )
    }
}