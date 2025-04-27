package com.majorproject.roomify.feature.ARViewer.presetation

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Plane
import com.google.ar.core.TrackingFailureReason
import com.jraska.falcon.Falcon
import com.majorproject.roomify.R
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.getDescription
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ArViewerActivity : AppCompatActivity(R.layout.activity_ar_viewer) {

    lateinit var sceneView: ARSceneView
    lateinit var loadingView: View
    lateinit var instructionText: TextView
    lateinit var cameraButton: ImageButton

    private var productId: String? = null
    private var productName: String? = null
    private var modelUrl: String? = null
    private lateinit var mediaProjectionManager: android.media.projection.MediaProjectionManager
    private var screenCapturePermissionResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            // Permission granted, start capturing
            startScreenCapture(it.data!!)
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    var isLoading = false
        set(value) {
            field = value
            loadingView.isGone = !value
        }

    var anchorNode: AnchorNode? = null
        set(value) {
            if (field != value) {
                field = value
                updateInstructions()
            }
        }

    var trackingFailureReason: TrackingFailureReason? = null
        set(value) {
            if (field != value) {
                field = value
                updateInstructions()
            }
        }

    fun updateInstructions() {
        instructionText.text = trackingFailureReason?.let {
            it.getDescription(this)
        } ?: if (anchorNode == null) {
            "Point your phone at a flat surface"
        } else {
            "Tap and drag to move, pinch to scale"
        }
    }
    var isSceneValid = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mediaProjectionManager = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as android.media.projection.MediaProjectionManager
        isSceneValid = true

        // Get data from intent
        productId = intent.getStringExtra("productId")
        productName = intent.getStringExtra("productName")
        modelUrl = intent.getStringExtra("modelUrl")

        if (modelUrl == null) {
            Toast.makeText(this, "Error: No model provided", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        instructionText = findViewById(R.id.instructionText)
        loadingView = findViewById(R.id.loadingView)
        cameraButton = findViewById(R.id.cameraButton)

        // Initialize camera button
        cameraButton.setOnClickListener {
            requestScreenCapturePermission()
        }

        sceneView = findViewById<ARSceneView?>(R.id.sceneView).apply {
            lifecycle = this@ArViewerActivity.lifecycle
            planeRenderer.isEnabled = true
            configureSession { session, config ->
                config.depthMode = when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
                    true -> Config.DepthMode.AUTOMATIC
                    else -> Config.DepthMode.DISABLED
                }
                config.instantPlacementMode = Config.InstantPlacementMode.DISABLED
                config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
            }
            onSessionUpdated = { _, frame ->
                if (anchorNode == null) {
                    frame.getUpdatedPlanes()
                        .firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
                        ?.let { plane ->
                            addAnchorNode(plane.createAnchor(plane.centerPose))
                        }
                }
            }
            onTrackingFailureChanged = { reason ->
                this@ArViewerActivity.trackingFailureReason = reason
            }
        }
    }

    private fun requestScreenCapturePermission() {
        val captureIntent = mediaProjectionManager.createScreenCaptureIntent()
        screenCapturePermissionResult.launch(captureIntent)
    }

    // Start screen capture after permission is granted
    private fun startScreenCapture(data: Intent) {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val displayName = "AR_${productName ?: "Roomify"}_$timestamp.jpg"

        // Trigger built-in Android screenshot mechanism
        val screenshotIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        screenshotIntent.putExtra(MediaStore.EXTRA_OUTPUT, getScreenshotUri(displayName))
        startActivity(screenshotIntent)

        Toast.makeText(this, "Screenshot captured", Toast.LENGTH_SHORT).show()
    }

    // Helper function to get the screenshot URI to store the image
    private fun getScreenshotUri(displayName: String): Uri {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        return uri ?: throw IOException("Failed to create URI for screenshot")
    }




    fun addAnchorNode(anchor: Anchor) {
        sceneView.addChildNode(
            AnchorNode(sceneView.engine, anchor)
                .apply {
                    isEditable = true
                    lifecycleScope.launch {
                        isLoading = true
                        buildModelNode()?.let { addChildNode(it) }
                        isLoading = false
                    }
                    anchorNode = this
                }
        )
    }

    suspend fun buildModelNode(): ModelNode? {
        // Use the model URL passed from ProductDetailActivity
        modelUrl?.let { url ->
            sceneView.modelLoader.loadModelInstance(url)?.let { modelInstance ->
                return ModelNode(
                    modelInstance = modelInstance,
                    scaleToUnits = 0.5f,
                    centerOrigin = Position(y = -0.5f)
                ).apply {
                    isEditable = true

                    // Override the default rotation handler to only rotate on Y axis
                    onRotate = { detector, _, _ ->
                        // Extract the current rotation
                        val currentRotation = this.rotation

                        // Apply only the y-axis rotation from the detector
                        val deltaRadians = detector.currentAngle - detector.lastAngle
                        val deltaDegrees = -deltaRadians * (180f / Math.PI.toFloat())

                        // Update only the y-component of rotation, keeping x and z unchanged
                        this.rotation = Rotation(
                            x = currentRotation.x,
                        )

                        true
                    }

                    // Apply appropriate transformations
                    onTransformChanged()
                }
            }
        }
        return null
    }


}