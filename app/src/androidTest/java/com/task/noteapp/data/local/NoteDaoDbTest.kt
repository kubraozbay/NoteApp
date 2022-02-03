package com.task.noteapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.task.noteapp.data.db.NoteDao
import com.task.noteapp.data.db.NoteDatabase
import com.task.noteapp.data.model.Note
import com.task.noteapp.getOrAwaitValue
import com.task.noteapp.util.getCurrentDateTime
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class NoteDaoDbTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: NoteDatabase
    private lateinit var dao: NoteDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.getNoteDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertShoppingItem() = runBlockingTest {

        val note = Note(
            id = 2,
            title = "title2",
            content = "content2",
            isUpdated = false,
            date = getCurrentDateTime(),
            imageUrl = null
        )
        dao.insert(note)

        val allNotes = dao.getNotes().getOrAwaitValue()

        assertThat(allNotes).contains(note)
    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val note = Note(
            id = 1,
            title = "title",
            content = "content",
            isUpdated = false,
            date = getCurrentDateTime(),
            imageUrl = null
        )
        dao.insert(note)
        dao.delete(note)

        val allNotes = dao.getNotes().getOrAwaitValue()

        assertThat(allNotes).doesNotContain(note)
    }
}