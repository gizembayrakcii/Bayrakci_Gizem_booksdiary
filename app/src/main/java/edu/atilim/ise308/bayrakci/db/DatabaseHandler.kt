package edu.atilim.ise308.bayrakci.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import edu.atilim.ise308.bayrakci.model.Books


class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE =
                "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY,$NAME TEXT,$AUTHOR TEXT,$COMPLETED TEXT,$PAGE TEXT, $ISBN TEXT);"
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun addBook(books: Books): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, books.name)
        values.put(AUTHOR, books.author)
        values.put(COMPLETED, books.completed)
        values.put(ISBN, books.isbnNo)
        values.put(PAGE, books.pageCount)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        Log.v("InsertedId", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }

    fun getBook(_id: Int): Books {
        val books = Books()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()
        books.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
        books.name = cursor.getString(cursor.getColumnIndex(NAME))
        books.author = cursor.getString(cursor.getColumnIndex(AUTHOR))
        books.isbnNo = cursor.getString(cursor.getColumnIndex(ISBN))
        books.pageCount = cursor.getString(cursor.getColumnIndex(PAGE))
        books.completed = cursor.getString(cursor.getColumnIndex(COMPLETED))
        cursor.close()
        return books
    }

    fun book(): List<Books> {
        val bookList = mutableListOf<Books>()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val books = Books()
                    books.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    books.name = cursor.getString(cursor.getColumnIndex(NAME))
                    books.author = cursor.getString(cursor.getColumnIndex(AUTHOR))
                    books.isbnNo = cursor.getString(cursor.getColumnIndex(ISBN))
                    books.pageCount = cursor.getString(cursor.getColumnIndex(PAGE))
                    books.completed = cursor.getString(cursor.getColumnIndex(COMPLETED))
                    bookList.add(books)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return bookList
    }

    fun updateBook(books: Books): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, books.name)
        values.put(AUTHOR, books.author)
        values.put(COMPLETED, books.completed)
        values.put(ISBN, books.isbnNo)
        values.put(PAGE, books.pageCount)
        val _success =
                db.update(TABLE_NAME, values, ID + "=?", arrayOf(books.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteBook(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteAllBooks(): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, null, null).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    companion object {
        private const val DB_VERSION = 1
        private const val DB_NAME = "MyBook"
        private const val TABLE_NAME = "Book"
        private const val ID = "id"
        private const val NAME = "name"
        private const val AUTHOR = "author"
        private const val COMPLETED = "completed"
        private const val ISBN = "isbn"
        private const val PAGE = "page_count"
    }

}