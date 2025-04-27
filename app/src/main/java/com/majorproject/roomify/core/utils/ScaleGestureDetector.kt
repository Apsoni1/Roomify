package com.majorproject.roomify.core.utils

import android.content.Context
import android.view.MotionEvent
import io.github.sceneview.math.Position
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt

class ScaleGestureDetector(
    context: Context,
    private val listener: OnScaleGestureListener
) {
    private var scaleFactor = 1f
    private var focusX = 0f
    private var focusY = 0f
    private var inProgress = false

    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount == 2) {
                    focusX = (event.getX(0) + event.getX(1)) / 2
                    focusY = (event.getY(0) + event.getY(1)) / 2
                    inProgress = true
                    return listener.onScaleBegin(this)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.pointerCount == 2 && inProgress) {
                    val x0 = event.getX(0)
                    val y0 = event.getY(0)
                    val x1 = event.getX(1)
                    val y1 = event.getY(1)

                    val dx = x0 - x1
                    val dy = y0 - y1
                    val newDist = sqrt(dx * dx + dy * dy)

                    val prevDx = focusX * 2 - x0 - x1
                    val prevDy = focusY * 2 - y0 - y1
                    val prevDist = sqrt(prevDx * prevDx + prevDy * prevDy)

                    if (prevDist > 0) {
                        scaleFactor = newDist / prevDist
                        listener.onScale(this)
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                if (inProgress) {
                    inProgress = false
                    listener.onScaleEnd(this)
                }
            }
        }
        return true
    }

    interface OnScaleGestureListener {
        fun onScale(detector: ScaleGestureDetector): Boolean
        fun onScaleBegin(detector: ScaleGestureDetector): Boolean
        fun onScaleEnd(detector: ScaleGestureDetector)
    }

    abstract class SimpleOnScaleGestureListener : OnScaleGestureListener {
        override fun onScale(detector: ScaleGestureDetector) = true
        override fun onScaleBegin(detector: ScaleGestureDetector) = true
        override fun onScaleEnd(detector: ScaleGestureDetector) {}
    }
}

class RotationGestureDetector(
    context: Context,
    private val listener: OnRotationGestureListener
) {
    private var rotationDegrees = 0f
    private var focusX = 0f
    private var focusY = 0f
    private var inProgress = false

    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount == 2) {
                    focusX = (event.getX(0) + event.getX(1)) / 2
                    focusY = (event.getY(0) + event.getY(1)) / 2
                    inProgress = true
                    return listener.onRotationBegin(this)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.pointerCount == 2 && inProgress) {
                    val x0 = event.getX(0)
                    val y0 = event.getY(0)
                    val x1 = event.getX(1)
                    val y1 = event.getY(1)

                    val angle = atan2(y1 - y0, x1 - x0) * 180 / PI.toFloat()
                    val prevAngle = atan2(focusY * 2 - y0 - y1, focusX * 2 - x0 - x1) * 180 / PI.toFloat()

                    rotationDegrees = angle - prevAngle
                    listener.onRotate(this)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                if (inProgress) {
                    inProgress = false
                    listener.onRotationEnd(this)
                }
            }
        }
        return true
    }

    interface OnRotationGestureListener {
        fun onRotate(detector: RotationGestureDetector): Boolean
        fun onRotationBegin(detector: RotationGestureDetector): Boolean
        fun onRotationEnd(detector: RotationGestureDetector)
    }

    abstract class SimpleOnRotationGestureListener : OnRotationGestureListener {
        override fun onRotate(detector: RotationGestureDetector) = true
        override fun onRotationBegin(detector: RotationGestureDetector) = true
        override fun onRotationEnd(detector: RotationGestureDetector) {}
    }
}

class MoveGestureDetector(
    context: Context,
    private val listener: OnMoveGestureListener
) {
    private var focusX = 0f
    private var focusY = 0f
    private var inProgress = false

    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                focusX = event.x
                focusY = event.y
                inProgress = true
                return listener.onMoveBegin(this)
            }
            MotionEvent.ACTION_MOVE -> {
                if (inProgress) {
                    val dx = event.x - focusX
                    val dy = event.y - focusY
                    focusX = event.x
                    focusY = event.y
                    listener.onMove(this, dx, dy)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (inProgress) {
                    inProgress = false
                    listener.onMoveEnd(this)
                }
            }
        }
        return true
    }

    val focusDelta: Position
        get() = Position(focusX, focusY, 0f)

    interface OnMoveGestureListener {
        fun onMove(detector: MoveGestureDetector, dx: Float, dy: Float): Boolean
        fun onMoveBegin(detector: MoveGestureDetector): Boolean
        fun onMoveEnd(detector: MoveGestureDetector)
    }

    abstract class SimpleOnMoveGestureListener : OnMoveGestureListener {
        override fun onMove(detector: MoveGestureDetector, dx: Float, dy: Float) = true
        override fun onMoveBegin(detector: MoveGestureDetector) = true
        override fun onMoveEnd(detector: MoveGestureDetector) {}
    }
}