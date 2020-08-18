package me.ycdev.android.demo.customviews.ui.events

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.actionToString
import android.widget.LinearLayout
import timber.log.Timber

class MyLinearLayout(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    var interceptDown: Boolean = false
    var interceptOthers: Boolean = false
    var handled: Boolean = false

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        CallRecordsCollector.addCallEvent(TAG, CallEvent.DISPATCH_TOUCH_EVENT, event.action)
        val handled = super.dispatchTouchEvent(event)
        Timber.tag(TAG).d("super.dispatchTouchEvent: $handled")
        return handled
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        CallRecordsCollector.addCallEvent(TAG, CallEvent.ON_TOUCH_EVENT, event.action)
        val handled = this.handled || super.onTouchEvent(event)
        Timber.tag(TAG).d("super.onTouchEvent: $handled")
        return handled
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        CallRecordsCollector.addCallEvent(TAG, CallEvent.ON_INTERCEPT_TOUCH_EVENT, event.action)
        if (interceptDown && event.action == ACTION_DOWN ||
                interceptOthers && event.action != ACTION_DOWN) {
            Timber.tag(TAG).d("intercepted: %s", actionToString(event.action))
            return true
        }
        return super.onInterceptTouchEvent(event)
    }

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        CallRecordsCollector.addCallEvent(TAG, CallEvent.REQUEST_DISALLOW_INTERCEPT_TOUCH_EVENT)
        super.requestDisallowInterceptTouchEvent(disallowIntercept)
    }

    companion object {
        const val TAG = "MyLinearLayout"
    }
}