package me.ycdev.android.demo.customviews.ui.events

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatButton
import timber.log.Timber

class MyButton(context: Context, attrs: AttributeSet) : AppCompatButton(context, attrs) {
    var disallowIntercept: Boolean = false

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        CallRecordsCollector.addCallEvent(TAG, CallEvent.DISPATCH_TOUCH_EVENT, event.action)
        val handled = super.dispatchTouchEvent(event)
        Timber.tag(TAG).d("super.dispatchTouchEvent: $handled")
        return handled
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        CallRecordsCollector.addCallEvent(TAG, CallEvent.ON_TOUCH_EVENT, event.action)
        if (disallowIntercept && event.action == MotionEvent.ACTION_DOWN) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        val handled = if (isEnabled) super.onTouchEvent(event) else false
        Timber.tag(TAG).d("super.onTouchEvent: $handled")
        return handled
    }

    companion object {
        const val TAG = "MyButton"
    }
}