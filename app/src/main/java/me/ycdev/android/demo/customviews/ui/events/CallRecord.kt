package me.ycdev.android.demo.customviews.ui.events

import android.view.MotionEvent

data class CallRecord(val tag: String, val event: CallEvent, val actionStr: String) {
    companion object {
        fun create(tag: String, event: CallEvent): CallRecord {
            return CallRecord(tag, event, "")
        }

        fun create(tag: String, event: CallEvent, action: Int): CallRecord {
            return CallRecord(tag, event, MotionEvent.actionToString(action))
        }
    }
}