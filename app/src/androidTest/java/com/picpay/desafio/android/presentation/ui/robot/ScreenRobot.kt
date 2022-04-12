@file:Suppress("UNCHECKED_CAST")

package com.picpay.desafio.android.presentation.ui.robot

import android.app.Activity
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import org.hamcrest.Matchers.not

@SuppressWarnings("unchecked")
abstract class ScreenRobot<out T> {
    // Only required for some calls
    private var activityContext: Activity? = null

    fun checkIsDisplayed(@IdRes vararg viewIds: Int): T {
        for (viewId in viewIds) {
            onView(withId(viewId)).check(matches(isDisplayed()))
        }
        return this as T
    }

    fun checkIsHidden(@IdRes vararg viewIds: Int): T {
        for (viewId in viewIds) {
            onView(withId(viewId)).check(matches(not(isDisplayed())))
        }
        return this as T
    }

    fun checkViewHasText(@IdRes viewId: Int, expected: String?): T {
        onView(withId(viewId)).check(matches(withText(expected)))
        return this as T
    }

    fun checkViewHasText(@IdRes viewId: Int, @StringRes messageResId: Int): T {
        onView(withId(viewId)).check(matches(withText(messageResId)))
        return this as T
    }

    fun checkViewHasHint(@IdRes viewId: Int, @StringRes messageResId: Int): T {
        onView(withId(viewId)).check(matches(withHint(messageResId)))
        return this as T
    }

    fun checkViewHasContentDescription(@IdRes viewId: Int, expected: String?): T {
        onView(withId(viewId)).check(matches(withContentDescription(expected)))
        return this as T
    }

    fun checkViewHasContentDescription(@IdRes viewId: Int, @StringRes messageResId: Int): T {
        onView(withId(viewId)).check(matches(withContentDescription(messageResId)))
        return this as T
    }

    fun clickOkOnView(@IdRes viewId: Int): T {
        onView(withId(viewId)).perform(click())
        return this as T
    }

    fun enterTextIntoView(@IdRes viewId: Int, text: String?): T {
        onView(withId(viewId)).perform(typeText(text))
        return this as T
    }

    fun provideActivityContext(activityContext: Activity?): T {
        this.activityContext = activityContext
        return this as T
    }

    fun checkDialogWithTextIsDisplayed(@StringRes messageResId: Int): T {
        onView(withText(messageResId))
            .inRoot(withDecorView(not(activityContext?.window?.decorView)))
            .check(matches(isDisplayed()))
        return this as T
    }

    fun swipeLeftOnView(@IdRes viewId: Int): T {
        onView(withId(viewId)).perform(swipeLeft())
        return this as T
    }
}