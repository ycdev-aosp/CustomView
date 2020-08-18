package me.ycdev.android.demo.customviews.ui.events

import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.google.common.truth.Truth.assertThat
import me.ycdev.android.demo.customviews.MainActivity
import me.ycdev.android.demo.customviews.R
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test

class EventsFragmentTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun handled() {
        val buttonIds = arrayOf(R.id.one_btn_has_listener, R.id.one_btn_no_listener)
        val expectedRecords = arrayListOf(
            CallRecord.create(MyLinearLayout.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.ON_INTERCEPT_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyButton.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyButton.TAG, CallEvent.ON_TOUCH_EVENT, ACTION_DOWN),

            CallRecord.create(MyLinearLayout.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_UP),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.ON_INTERCEPT_TOUCH_EVENT, ACTION_UP),
            CallRecord.create(MyButton.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_UP),
            CallRecord.create(MyButton.TAG, CallEvent.ON_TOUCH_EVENT, ACTION_UP)
        )

        for (btnId in buttonIds) {
            CallRecordsCollector.callRecords.clear()
            onView(withId(btnId)).perform(click())
            assertThat(CallRecordsCollector.callRecords).isEqualTo(expectedRecords)
        }
    }

    @Test
    fun notHandled() {
        val buttonIds = arrayOf(R.id.one_btn_has_listener, R.id.one_btn_no_listener)
        // MyLinearLayout doesn't handle the event ACTION_DOWN,
        // so it cannot receive the next ACTION_MOVE and ACTION UP events.
        val expectedRecords = arrayListOf(
            CallRecord.create(MyLinearLayout.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.ON_INTERCEPT_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyButton.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyButton.TAG, CallEvent.ON_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.ON_TOUCH_EVENT, ACTION_DOWN)
        )
        onView(withId(R.id.one_btn_disable)).perform(click())

        for (btnId in buttonIds) {
            CallRecordsCollector.callRecords.clear()
            onView(withId(btnId)).check(matches(not(isEnabled()))).perform(click())
            assertThat(CallRecordsCollector.callRecords).isEqualTo(expectedRecords)
        }
    }

    @Test
    fun intercept_notHandled() {
        val buttonIds = arrayOf(R.id.one_btn_has_listener, R.id.one_btn_no_listener)
        // MyLinearLayout intercepts the event ACTION_DOWN and doesn't handle it,
        // so MyButton cannot receive the event ACTION_DOWN
        // and MyLinearLayout cannot receive the next ACTION_MOVE and ACTION_UP events.
        val expectedRecords = arrayListOf(
            CallRecord.create(MyLinearLayout.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.ON_INTERCEPT_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.ON_TOUCH_EVENT, ACTION_DOWN)
        )

        onView(withId(R.id.one_intercept_down)).perform(click()).check(matches(isChecked()))
        onView(withId(R.id.one_handled)).check(matches(not(isChecked())))
        for (btnId in buttonIds) {
            CallRecordsCollector.callRecords.clear()
            onView(withId(btnId)).perform(click())
            assertThat(CallRecordsCollector.callRecords).isEqualTo(expectedRecords)
        }
    }

    @Test
    fun intercept_handled() {
        val buttonIds = arrayOf(R.id.one_btn_has_listener, R.id.one_btn_no_listener)
        // MyLinearLayout intercepts the event ACTION_DOWN and handles it,
        // so MyButton cannot receive the event ACTION_DOWN
        // and MyLinearLayout can receive the next ACTION_MOVE and ACTION_UP events.
        val expectedRecords = arrayListOf(
            CallRecord.create(MyLinearLayout.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.ON_INTERCEPT_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.ON_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_UP),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.ON_TOUCH_EVENT, ACTION_UP)
        )

        onView(withId(R.id.one_handled)).perform(click()).check(matches(isChecked()))
        onView(withId(R.id.one_intercept_down)).perform(click()).check(matches(isChecked()))
        for (btnId in buttonIds) {
            CallRecordsCollector.callRecords.clear()
            onView(withId(btnId)).perform(click())
            assertThat(CallRecordsCollector.callRecords).isEqualTo(expectedRecords)
        }
    }

    @Test
    fun interceptEverything() {
        val buttonIds = arrayOf(R.id.one_btn_has_listener, R.id.one_btn_no_listener)
        // MyLinearLayout intercepts the event ACTION_DOWN,
        // so MyButton have NO chance to disallow intercept!
        val expectedRecords = arrayListOf(
            CallRecord.create(MyLinearLayout.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.ON_INTERCEPT_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.ON_TOUCH_EVENT, ACTION_DOWN)
        )

        onView(withId(R.id.one_disallow_intercept)).perform(click()).check(matches(isChecked()))  // will not work
        onView(withId(R.id.one_intercept_down)).perform(click()).check(matches(isChecked()))
        for (btnId in buttonIds) {
            CallRecordsCollector.callRecords.clear()
            onView(withId(btnId)).perform(click())
            assertThat(CallRecordsCollector.callRecords).isEqualTo(expectedRecords)
        }
    }

    @Test
    fun interceptOtherEvents() {
        val buttonIds = arrayOf(R.id.one_btn_has_listener, R.id.one_btn_no_listener)
        // MyLinearLayout intercepts the other events,
        // so MyButton will receive ACTION_CANCEL!
        val expectedRecords = arrayListOf(
            CallRecord.create(MyLinearLayout.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.ON_INTERCEPT_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyButton.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyButton.TAG, CallEvent.ON_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_UP),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.ON_INTERCEPT_TOUCH_EVENT, ACTION_UP),
            CallRecord.create(MyButton.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_CANCEL),
            CallRecord.create(MyButton.TAG, CallEvent.ON_TOUCH_EVENT, ACTION_CANCEL)
            // MyLinearLayout cannot receive ACTION_UP in it's #onTouchEvent()!
        )

        onView(withId(R.id.one_intercept_others)).perform(click()).check(matches(isChecked()))
        for (btnId in buttonIds) {
            CallRecordsCollector.callRecords.clear()
            onView(withId(btnId)).perform(click())
            assertThat(CallRecordsCollector.callRecords).isEqualTo(expectedRecords)
        }
    }

    @Test
    fun disallowIntercept() {
        val buttonIds = arrayOf(R.id.one_btn_has_listener, R.id.one_btn_no_listener)
        // MyLinearLayout doesn't intercept the event ACTION_DOWN,
        // and MyButton requests to disallow intercept,
        // so MyLinearLayout cannot intercept other events.
        val expectedRecords = arrayListOf(
            CallRecord.create(MyLinearLayout.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.ON_INTERCEPT_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyButton.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyButton.TAG, CallEvent.ON_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.REQUEST_DISALLOW_INTERCEPT_TOUCH_EVENT),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_UP),
            CallRecord.create(MyButton.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_UP),
            CallRecord.create(MyButton.TAG, CallEvent.ON_TOUCH_EVENT, ACTION_UP)
        )

        onView(withId(R.id.one_disallow_intercept)).perform(click()).check(matches(isChecked()))
        onView(withId(R.id.one_intercept_others)).perform(click()).check(matches(isChecked())) // will not work
        for (btnId in buttonIds) {
            CallRecordsCollector.callRecords.clear()
            onView(withId(btnId)).perform(click())
            assertThat(CallRecordsCollector.callRecords).isEqualTo(expectedRecords)
        }
    }

    @Test
    fun actionDownClearStates() {
        val buttonIds = arrayOf(R.id.one_btn_has_listener, R.id.one_btn_no_listener)
        // MyLinearLayout doesn't intercept the event ACTION_DOWN,
        // and MyButton requests to disallow intercept,
        // so MyLinearLayout cannot intercept other events.
        val expectedRecords = arrayListOf(
            CallRecord.create(MyLinearLayout.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.ON_INTERCEPT_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyButton.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyButton.TAG, CallEvent.ON_TOUCH_EVENT, ACTION_DOWN),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_UP),
            CallRecord.create(MyLinearLayout.TAG, CallEvent.ON_INTERCEPT_TOUCH_EVENT, ACTION_UP),
            CallRecord.create(MyButton.TAG, CallEvent.DISPATCH_TOUCH_EVENT, ACTION_CANCEL),
            CallRecord.create(MyButton.TAG, CallEvent.ON_TOUCH_EVENT, ACTION_CANCEL)
        )

        // The following request will not work, because the state will be cleared
        // when MyLinearLayout receives ACTION_UP or ACTION_DOWN.
        onView(withId(R.id.one_btn_disallow_intercept)).perform(click())

        onView(withId(R.id.one_intercept_others)).perform(click()).check(matches(isChecked()))
        for (btnId in buttonIds) {
            CallRecordsCollector.callRecords.clear()
            onView(withId(btnId)).perform(click())
            assertThat(CallRecordsCollector.callRecords).isEqualTo(expectedRecords)
        }
    }
}
