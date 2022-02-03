package com.task.noteapp.ui.noteAdd

import android.animation.Animator
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.task.noteapp.R
import com.task.noteapp.data.model.Note
import com.task.noteapp.databinding.FragmentNoteAddBinding
import com.task.noteapp.ui.MainActivity
import com.task.noteapp.ui.noteList.NoteListFragment.Companion.BUNDLE_NOTE
import com.task.noteapp.util.getBitmapFromByteArray
import com.task.noteapp.util.getByteArrayFromBitmap
import com.task.noteapp.util.getCurrentDateTime
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*


@AndroidEntryPoint
class NoteAddFragment : Fragment(R.layout.fragment_note_add) {

    private val viewModel: NoteAddViewModel by viewModels()
    private lateinit var binding: FragmentNoteAddBinding

    private var bitmap: Bitmap? = null
    private var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        arguments?.let {
            note = it.getParcelable<Note>(BUNDLE_NOTE)
        }
        (requireActivity() as? MainActivity)?.supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = getString(R.string.add_note)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNoteAddBinding.bind(view)
        binding.apply {
            note?.let {
                editTextTitle.setText(it.title)
                editTextContent.setText(it.content)
                it.imageUrl?.let { imageUrl ->
                    imageView.setImageBitmap(imageUrl.getBitmapFromByteArray())
                    imageView.visibility = View.VISIBLE
                }
            }
            imageButtonAdd.setOnClickListener {
                selectImage()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_add_note, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemAddNote -> {
                if (isValidate()) {
                    if (addNoteToRoom()) {
                        binding.lottieAnimationView.apply {
                            visibility = View.VISIBLE
                            addAnimatorListener(object : Animator.AnimatorListener {
                                override fun onAnimationRepeat(animation: Animator?) {
                                }

                                override fun onAnimationEnd(animation: Animator?) {
                                    findNavController().navigate(R.id.action_noteAddFragment_to_noteListFragment)
                                }

                                override fun onAnimationCancel(animation: Animator?) {
                                }

                                override fun onAnimationStart(animation: Animator?) {
                                }

                            })
                            playAnimation()
                        }
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isValidate(): Boolean {
        var isValidate = true
        binding.apply {
            editTextTitle.apply {
                if (this.text.isEmpty()) {
                    error = getString(
                        R.string.error_empty_input,
                        hint.toString().toLowerCase(Locale.ROOT)
                    )
                    isValidate = false
                }
            }
            editTextContent.apply {
                if (this.text.isEmpty()) {
                    error = getString(
                        R.string.error_empty_input,
                        hint.toString().toLowerCase(Locale.ROOT)
                    )
                    isValidate = false
                }
            }
        }
        return isValidate
    }

    private fun addNoteToRoom(): Boolean {
        binding.apply {
            note?.let {
                it.isUpdated = isUpdated(it)
                if (it.isUpdated == false) {
                    findNavController().navigate(R.id.action_noteAddFragment_to_noteListFragment)
                } else {
                    it.title = editTextTitle.text.toString().trim()
                    it.content = editTextContent.text.toString().trim()
                    it.imageUrl = bitmap?.getByteArrayFromBitmap() ?: it.imageUrl
                    viewModel.saveOrUpdateNote(it)
                    return true
                }
            } ?: kotlin.run {
                viewModel.saveOrUpdateNote(
                    Note(
                        title = editTextTitle.text.toString().trim(),
                        content = editTextContent.text.toString().trim(),
                        imageUrl = bitmap?.getByteArrayFromBitmap(),
                        date = getCurrentDateTime()
                    )
                )
                return true
            }
        }
        return false
    }

    private fun isUpdated(note: Note): Boolean {
        return note.title != binding.editTextTitle.text.toString().trim() ||
                note.content != binding.editTextContent.text.toString().trim() ||
                !note.imageUrl.contentEquals(bitmap.getByteArrayFromBitmap())
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        //   startActivityForResult(intent, IMAGE_REQ)

        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val path: Uri? = result.data?.data
                try {
                    path?.let {
                        var storedBitmap = getCapturedImage(it)
                        storedBitmap = Bitmap.createScaledBitmap(
                            storedBitmap,
                            IMAGE_WIDTH_HEIGHT,
                            IMAGE_WIDTH_HEIGHT,
                            true
                        )
                        bitmap = storedBitmap
                        binding.imageView.apply {
                            setImageBitmap(storedBitmap)
                            visibility = View.VISIBLE
                        }
                    } ?: kotlin.run {
                        Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                    }

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    private fun getCapturedImage(selectedPhotoUri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source =
                ImageDecoder.createSource(requireActivity().contentResolver, selectedPhotoUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(
                requireContext().contentResolver,
                selectedPhotoUri
            )
        }
    }

    companion object {
        const val IMAGE_WIDTH_HEIGHT = 200
    }
}