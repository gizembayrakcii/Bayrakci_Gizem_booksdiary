package edu.atilim.ise308.bayrakci.bayrakci_gizem_booksdiary

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import edu.atilim.ise308.bayrakci.adapter.TaskRecyclerAdapter
import edu.atilim.ise308.bayrakci.db.DatabaseHandler
import edu.atilim.ise308.bayrakci.model.Books

class MainActivity : AppCompatActivity() {

    var taskRecyclerAdapter: TaskRecyclerAdapter? = null;
    var fab: FloatingActionButton? = null
    var recyclerView: RecyclerView? = null
    var dbHandler: DatabaseHandler? = null
    var listTasks: List<Books> = ArrayList<Books>()
    var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initOperations()
        //initDB()
    }

    fun initDB() {
        dbHandler = DatabaseHandler(this)
        listTasks = (dbHandler as DatabaseHandler).book()
        taskRecyclerAdapter = TaskRecyclerAdapter(booksList = listTasks, context = applicationContext, onDelete = {
           deleteBook(bookID = it)
        }, onClick = {
            AddOrEditFragment(it, "E") {
                if (it) initDB()
            }.show(this.supportFragmentManager, "add_book")
        })
        (recyclerView as RecyclerView).adapter = taskRecyclerAdapter
    }

    fun initViews() {
        fab = findViewById<FloatingActionButton>(R.id.fab)
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        taskRecyclerAdapter = TaskRecyclerAdapter(booksList = listTasks, context = applicationContext, onDelete = {
            deleteBook(bookID = it)
        }, onClick = {
            AddOrEditFragment(it, "E") {
                if (it) initDB()
            }.show(this.supportFragmentManager, "add_book")
        })
        linearLayoutManager = LinearLayoutManager(applicationContext)
        (recyclerView as RecyclerView).layoutManager = linearLayoutManager
    }

    fun initOperations() {
        fab?.setOnClickListener {
            AddOrEditFragment(0, "A") {
                if (it) initDB()
            }.show(this.supportFragmentManager, "add_book")
        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_delete) {
            val dialog = AlertDialog.Builder(this).setTitle("Info").setMessage("Click 'YES' Delete All Tasks")
                    .setPositiveButton("YES", { dialog, i ->
                        dbHandler!!.deleteAllTasks()
                        initDB()
                        dialog.dismiss()
                    })
                    .setNegativeButton("NO", { dialog, i ->
                        dialog.dismiss()
                    })
            dialog.show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }*/

    override fun onResume() {
        super.onResume()
        initDB()
    }

    private fun deleteBook(bookID : Int)
    {
        val dialog = AlertDialog.Builder(this).setTitle("Info").setMessage("Click 'YES' to delete the book.")
                .setPositiveButton("YES") { dialog, _ ->
                    val success = dbHandler?.deleteBook(bookID) as Boolean
                    if (success)
                    initDB()
                    dialog.dismiss()
                }
                .setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
        dialog.show()
    }
}
