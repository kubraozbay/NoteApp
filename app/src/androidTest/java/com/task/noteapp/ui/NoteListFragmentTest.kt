package com.task.noteapp.ui

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.task.noteapp.R
import com.task.noteapp.launchFragmentInHiltContainer
import com.task.noteapp.ui.noteList.NoteListFragment
import com.task.noteapp.ui.noteList.NoteListFragmentDirections
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class NoteListFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun clickAddNoteButton_navigateToNoteAddFragment() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<NoteListFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.buttonAddNote)).perform(click())

        verify(navController).navigate(
            NoteListFragmentDirections.actionNoteListFragmentToNoteAddFragment()
        )
    }
}












