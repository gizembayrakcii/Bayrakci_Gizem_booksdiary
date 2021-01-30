package edu.atilim.ise308.bayrakci.bayrakci_gizem_booksdiary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import edu.atilim.ise308.bayrakci.db.DatabaseHandler
import edu.atilim.ise308.bayrakci.model.Books

class AddOrEditFragment(
        private val bookID: Int,
        private val mode: String,
        private val isUpdated: (Boolean) -> Unit
) : DialogFragment() {
    lateinit var btn_delete: Button
    lateinit var btn_save: Button
    lateinit var input_name: EditText
    lateinit var input_desc: EditText
    lateinit var page_count: EditText
    lateinit var isbn_no: EditText
    lateinit var swt_completed: CheckBox
    var dbHandler: DatabaseHandler? = null
    var isEditMode = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_add_edit, container, false)
        setupButtons(root)
        initDB()
        initOperations()
        return root
    }

    private fun setupButtons(root: View) {
        btn_delete = root.findViewById(R.id.btn_delete)
        btn_save = root.findViewById(R.id.btn_save)
        input_name = root.findViewById(R.id.book_name)
        input_desc = root.findViewById(R.id.author_name)
        swt_completed = root.findViewById(R.id.cb_completed)
        page_count = root.findViewById(R.id.page_count)
        isbn_no = root.findViewById(R.id.isbn_no)
    }

    private fun initDB() {
        dbHandler = DatabaseHandler(this.requireContext())
        btn_delete.visibility = View.INVISIBLE
        if (mode == "E") {
            isEditMode = true
            val books: Books = dbHandler!!.getBook(bookID)
            input_name.setText(books.name)
            input_desc.setText(books.author)
            isbn_no.setText(books.isbnNo)
            page_count.setText(books.pageCount)
            swt_completed.isChecked = books.completed == "Y"
            btn_delete.visibility = View.VISIBLE
        }
    }

    private fun initOperations() {
        btn_save.setOnClickListener {
            var success: Boolean = false
            if (!isEditMode) {
                val books: Books = Books()
                books.name = input_name.text.toString()
                books.author = input_desc.text.toString()
                books.isbnNo = isbn_no.text.toString()
                books.pageCount = (page_count.text.toString())
                if (swt_completed.isChecked)
                    books.completed = "Y"
                else
                    books.completed = "N"
                success = dbHandler?.addBook(books) as Boolean
                isUpdated(success)
            } else {
                val books: Books = Books()
                books.id = bookID
                books.name = input_name.text.toString()
                books.author = input_desc.text.toString()
                books.isbnNo = isbn_no.text.toString()
                books.pageCount = (page_count.text.toString())
                if (swt_completed.isChecked)
                    books.completed = "Y"
                else
                    books.completed = "N"
                success = dbHandler?.updateBook(books) as Boolean
                isUpdated(success)
            }

            if (success)
                dismiss().also { isUpdated(true) }
        }

        btn_delete.setOnClickListener {
            val dialog = AlertDialog.Builder(this.requireContext()).setTitle("Info").setMessage("Click 'YES' to delete the book.")
                    .setPositiveButton("YES") { dialog, _ ->
                        val success = dbHandler?.deleteBook(bookID) as Boolean
                        if (success)
                            dismiss()
                        isUpdated(success)
                        dialog.dismiss()
                    }
                    .setNegativeButton("NO") { dialog, _ ->
                        dialog.dismiss()
                    }
            dialog.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            dismiss()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}