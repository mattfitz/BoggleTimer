package com.example.firsttestapp

import android.graphics.Color
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import android.os.CountDownTimer
import android.view.*
import androidx.core.view.GestureDetectorCompat


class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private var timerMasterLengthMillis:Long = 90000
    private var timerLengthMillisPlayer1:Long = 90000
    private var timerLengthMillisPlayer2:Long = 90000
    private var resumeFromMillisPlayer1:Long = timerLengthMillisPlayer1
    private var resumeFromMillisPlayer2:Long = timerLengthMillisPlayer2
    private var timer1 = timerPlayer1(timerLengthMillisPlayer1, 1000)
    private var timer2 = timerPlayer2(timerLengthMillisPlayer2, 1000)
    private var player1Running = false;
    private var player2Running = false;
    private var player1Complete = false;
    private var player2Complete = false;

    private var mVelocityTracker: VelocityTracker? = null

    lateinit var myGestureDetector: GestureDetector

    var gDetector: GestureDetectorCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textCenter.text = "Tap a timer to start the other player's timer.\nSwipe to increase or decrease time.\nDouble tap to reset."
        
        //initial display value
        textPlayer1Timer.setText(stringifyMilliseconds(timerMasterLengthMillis))
        textPlayer2Timer.setText(stringifyMilliseconds(timerMasterLengthMillis))

        //myGestureDetector = GestureDetector(this, MyGestureListener())


        //var touchListener = View.OnTouchListener{ v,event->
        // myGestureDetector.onTouchEvent(event)
        //}

        //textCenter.setOnTouchListener(touchListener)

        this.gDetector = GestureDetectorCompat(this, this)

        textPlayer2Timer.setOnClickListener { view ->
            if (!player1Running && !player1Complete) {
                player1Running = true;
                player2Running = false;
                textPlayer1Timer.setBackgroundColor(Color.parseColor("#00ff00"))
                textPlayer2Timer.setBackgroundColor(Color.parseColor("#ffff00"))
                timerLengthMillisPlayer2 = resumeFromMillisPlayer2
                timer2.cancel()
                timer1 = timerPlayer1(timerLengthMillisPlayer1, 1000)
                timer1.start()
            }
        }

        textPlayer1Timer.setOnClickListener { view ->
            if (!player2Running && !player2Complete) {
                player2Running = true;
                player1Running = false;
                textPlayer2Timer.setBackgroundColor(Color.parseColor("#00ff00"))
                textPlayer1Timer.setBackgroundColor(Color.parseColor("#ffff00"))
                timerLengthMillisPlayer1 = resumeFromMillisPlayer1
                timer1.cancel()
                timer2 = timerPlayer2(timerLengthMillisPlayer2, 1000)
                timer2.start()
            }
        }

    }

    private fun timerPlayer1(millisInFuture:Long,countDownInterval:Long): CountDownTimer{

        return object: CountDownTimer(millisInFuture,countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                textPlayer1Timer.setText(stringifyMilliseconds(millisUntilFinished))
                resumeFromMillisPlayer1 = millisUntilFinished
            }

            override fun onFinish() {
                player1Running = false;
                player1Complete = true;
                textPlayer1Timer.setBackgroundColor(Color.parseColor("#ff0000"))
                if (resumeFromMillisPlayer2 > 1000)
                {
                    timer2 = timerPlayer2(timerLengthMillisPlayer2, 1000)
                    timer2.start()
                }
            }
        }
    }
    private fun timerPlayer2(millisInFuture:Long,countDownInterval:Long): CountDownTimer{

        return object: CountDownTimer(millisInFuture,countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                textPlayer2Timer.setText(stringifyMilliseconds(millisUntilFinished))
                resumeFromMillisPlayer2 = millisUntilFinished
            }

            override fun onFinish() {
                player2Running = false;
                player2Complete = true;
                textPlayer2Timer.setBackgroundColor(Color.parseColor("#ff0000"))
                if (resumeFromMillisPlayer1 > 1000)
                {
                    timer1 = timerPlayer1(timerLengthMillisPlayer1, 1000)
                    timer1.start()
                }
            }
        }
    }
    private fun stringifyMilliseconds(millis: Long): String{
        val seconds = java.util.concurrent.TimeUnit.MILLISECONDS.toSeconds(millis)
        val minutes = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(millis)
        return String.format("%02d:%02d",
            minutes,
            seconds - java.util.concurrent.TimeUnit.MINUTES.toSeconds(minutes))
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }



    override fun onTouchEvent(event: MotionEvent): Boolean {
        this.gDetector?.onTouchEvent(event)
        //// Be sure to call the superclass implementation
        //return super.onTouchEvent(event)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // Reset the velocity tracker back to its initial state.
                mVelocityTracker?.clear()
                // If necessary retrieve a new VelocityTracker object to watch the
                // velocity of a motion.
                mVelocityTracker = mVelocityTracker ?: VelocityTracker.obtain()
                // Add a user's movement to the tracker.
                mVelocityTracker?.addMovement(event)
                //textCenter.text = ""
            }
            MotionEvent.ACTION_MOVE -> {
                mVelocityTracker?.apply {
                    val pointerId: Int = event.getPointerId(event.actionIndex)
                    addMovement(event)
                    // When you want to determine the velocity, call
                    // computeCurrentVelocity(). Then call getXVelocity()
                    // and getYVelocity() to retrieve the velocity for each pointer ID.
                    computeCurrentVelocity(1000)
                    // Log velocity of pixels per second
                    // Best practice to use VelocityTrackerCompat where possible.
                    //textCenter.append("X velocity: ${getXVelocity(pointerId)}:")
                    //textCenter.append("Y velocity: ${getYVelocity(pointerId)}/n")
                    if (!player1Running && !player2Running) {
                        val xVelo = Math.floor((getXVelocity(pointerId)).toDouble())
                        if (xVelo > 0 || xVelo < 0) {
                            timerMasterLengthMillis += xVelo.toLong()
                            if (timerMasterLengthMillis <= 0)
                            {
                                timerMasterLengthMillis = 1000
                            }
                            timerLengthMillisPlayer1 = timerMasterLengthMillis
                            timerLengthMillisPlayer2 = timerMasterLengthMillis
                            resumeFromMillisPlayer1 = timerMasterLengthMillis
                            resumeFromMillisPlayer2 = timerMasterLengthMillis
                            textPlayer1Timer.setText(stringifyMilliseconds(timerMasterLengthMillis))
                            textPlayer2Timer.setText(stringifyMilliseconds(timerMasterLengthMillis))
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker?.recycle()
                mVelocityTracker = null
            }
        }
        return true
    }

    override fun onDown(event: MotionEvent): Boolean {
        //textCenter.text = String.format("onDown x:${event.x} y:${event.y}")
        return true
    }

    override fun onFling(event1: MotionEvent, event2: MotionEvent,
                         velocityX: Float, velocityY: Float): Boolean {
        //textCenter.append(String.format("\nonFling x1:${event1.x} x2:${event2.x}"))
        return true
    }

    override fun onLongPress(event: MotionEvent) {
        //textCenter.append(String.format("\nonLongPress x:${event.x} y:${event.y}"))
    }

    override fun onScroll(e1: MotionEvent, e2: MotionEvent,
                          distanceX: Float, distanceY: Float): Boolean {
        //textCenter.append(String.format("\nonScroll e1x:${e1.x} e2x:${e2.x} "))
        return true
    }

    override fun onShowPress(event: MotionEvent) {
        //textCenter.append(String.format("\nonShowPress x:${event.x} y:${event.y}"))
    }

    override fun onSingleTapUp(event: MotionEvent): Boolean {
        //textCenter.append(String.format("\nonSingleTapUp x:${event.x} y:${event.y}"))
        return true
    }
    override fun onDoubleTap(event: MotionEvent): Boolean {
        //textCenter.text = "onDoubleTap: $event"
        return true
    }

    override fun onDoubleTapEvent(event: MotionEvent): Boolean {
        //textCenter.text = "onDoubleTapEvent: $event"
        timer1.cancel()
        timer2.cancel()
        player1Running = false;
        player2Running = false;
        player1Complete = false;
        player2Complete = false;
        timerLengthMillisPlayer1 = timerMasterLengthMillis
        timerLengthMillisPlayer2 = timerMasterLengthMillis
        resumeFromMillisPlayer1 = timerMasterLengthMillis
        resumeFromMillisPlayer2 = timerMasterLengthMillis
        textPlayer1Timer.setText(stringifyMilliseconds(timerMasterLengthMillis))
        textPlayer2Timer.setText(stringifyMilliseconds(timerMasterLengthMillis))
        textPlayer1Timer.setBackgroundColor(Color.parseColor("#ffff00"))
        textPlayer2Timer.setBackgroundColor(Color.parseColor("#ffff00"))
        return true
    }

    override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
        //textCenter.text = "onSingleTapConfirmedEvent: $event"
        return true
    }
}
