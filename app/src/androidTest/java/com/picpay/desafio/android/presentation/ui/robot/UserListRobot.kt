package com.picpay.desafio.android.presentation.ui.robot

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import com.picpay.desafio.android.R
import com.picpay.desafio.android.presentation.ui.UserListFragment
import com.picpay.desafio.android.presentation.ui.UserViewState

fun userListRobot(func: UserListRobot.() -> Unit) = UserListRobot().apply {
    UserListRobot().apply { func() }
}

class UserListRobot : ScreenRobot<UserListRobot>() {
    companion object {
        private const val SWIPE_REFRESH_LAYOUT_ID = R.id.swipeRefreshLayout
        private const val LIST_PROGRESSBAR_ID = R.id.listProgressBar
        private const val TITLE_ID = R.id.title
        private const val RECYCLER_VIEW_ID = R.id.recyclerView
        private const val PICTURE_ID = R.id.picture
        private const val USERNAME_ID = R.id.username
        private const val NAME_ID = R.id.name
    }

    private val scenario: FragmentScenario<UserListFragment> by lazy {
        launchFragmentInContainer(themeResId = R.style.AppTheme)
    }

    init {
        scenario.moveToState(newState = Lifecycle.State.STARTED)
    }

    fun setUiState(state: UserViewState) {
        scenario.onFragment { fragment -> fragment.buildViewState(state) }
    }

    fun checkInitialState() {
        checkIsDisplayed(SWIPE_REFRESH_LAYOUT_ID)
        checkIsHidden(LIST_PROGRESSBAR_ID, TITLE_ID, RECYCLER_VIEW_ID)
    }

    fun checkLoadingState() {
        checkIsDisplayed(SWIPE_REFRESH_LAYOUT_ID, LIST_PROGRESSBAR_ID, TITLE_ID)
        checkIsHidden(RECYCLER_VIEW_ID)
    }

    fun checkSuccessState() {
        checkIsDisplayed(SWIPE_REFRESH_LAYOUT_ID, TITLE_ID, RECYCLER_VIEW_ID)
        checkIsHidden(LIST_PROGRESSBAR_ID)
    }

    fun checkErrorState() {
        checkIsDisplayed(SWIPE_REFRESH_LAYOUT_ID, TITLE_ID)
        checkIsHidden(LIST_PROGRESSBAR_ID, RECYCLER_VIEW_ID)
    }

    fun checkTitle(title: String) {
        checkViewHasText(TITLE_ID, title)
    }

    fun checkPictureInRv(contentDescription: String) {
        checkIsDisplayed(PICTURE_ID)
        checkViewHasContentDescription(PICTURE_ID, contentDescription)
    }

    fun checkUsernameInRv(username: String) {
        checkIsDisplayed(USERNAME_ID)
        checkViewHasText(USERNAME_ID, username)
    }

    fun checkNameInRv(username: String) {
        checkIsDisplayed(NAME_ID)
        checkViewHasText(NAME_ID, username)
    }
}