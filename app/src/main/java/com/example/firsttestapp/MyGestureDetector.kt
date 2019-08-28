package com.example.firsttestapp

import android.util.Log
import android.view.MotionEvent
import android.view.GestureDetector

// In the SimpleOnGestureListener subclass you should override
// onDown and any other gesture that you want to detect.
internal class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

    override fun onDown(event: MotionEvent): Boolean {

        Log.d("whatever","on down")

        // don't return false here or else none of the other
        // gestures will work
        return true
    }

    override fun onSingleTapConfirmed(e: MotionEvent): Boolean {

        return true
    }

    override fun onLongPress(e: MotionEvent) {

    }

    override fun onDoubleTap(e: MotionEvent): Boolean {

        return true
    }

    override fun onScroll(
        e1: MotionEvent, e2: MotionEvent,
        distanceX: Float, distanceY: Float
    ): Boolean {

        return true
    }

    override fun onFling(
        event1: MotionEvent, event2: MotionEvent,
        velocityX: Float, velocityY: Float
    ): Boolean {
        Log.d("whatever","on fling\t Veloxity x \t\t"+velocityX+"\t\tveloxity y\t\t"+velocityY)
        return true
    }
}