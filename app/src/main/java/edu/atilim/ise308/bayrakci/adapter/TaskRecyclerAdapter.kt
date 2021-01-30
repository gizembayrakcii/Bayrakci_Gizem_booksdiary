package edu.atilim.ise308.bayrakci.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import edu.atilim.ise308.bayrakci.bayrakci_gizem_booksdiary.R
import edu.atilim.ise308.bayrakci.model.Books
import java.util.ArrayList

class TaskRecyclerAdapter(
        booksList: List<Books>, internal val context: Context, val onClick: (Int) -> Unit, val onDelete: (Int) -> Unit) :  RecyclerView.Adapter<TaskRecyclerAdapter.TaskViewHolder>() {

    private var booksList: List<Books> = ArrayList()
    init {
        this.booksList = booksList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_books, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val books = booksList[position]
        holder.name.text = books.name
        holder.desc.text = books.author
        holder.page_count.text = books.pageCount
        if (books.completed == "Y")
            holder.list_item.background = ContextCompat.getDrawable(context, R.color.colorSuccess)
        else
            holder.list_item.background = ContextCompat.getDrawable(context, R.color.colorUnSuccess)

        holder.btn_edit.setOnClickListener {
            onClick(books.id)
        }

        holder.btn_delete.setOnClickListener {
            onDelete(books.id)
        }

    }

    override fun getItemCount(): Int {
        return booksList.size
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var name: TextView = view.findViewById(R.id.tvName) as TextView
        var desc: TextView = view.findViewById(R.id.tvAuthor) as TextView
        var list_item: CardView = view.findViewById(R.id.list_item) as CardView
        var page_count: TextView = view.findViewById(R.id.tvPageCount) as TextView
        var btn_edit: ImageButton = view.findViewById(R.id.btnEdit) as ImageButton
        var btn_delete: ImageButton = view.findViewById(R.id.btnDelete) as ImageButton

        //var page_count: TextView = view.findViewById(R.id.tvPageCount) as TextView
    }
}
