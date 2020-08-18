package me.ycdev.android.demo.customviews.ui.events

import timber.log.Timber

object CallRecordsCollector {
    val callRecords = arrayListOf<CallRecord>()

    fun addCallEvent(tag: String, event: CallEvent) {
        val record = CallRecord.create(tag, event)
        callRecords.add(record)
        Timber.tag(tag).d("$record")
    }

    fun addCallEvent(tag: String, event: CallEvent, action: Int) {
        val record = CallRecord.create(tag, event, action)
        callRecords.add(record)
        Timber.tag(tag).d("$record")
    }
}