package com.example.mynotes

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var btnCreate: AppCompatButton
    private lateinit var btnFloat: FloatingActionButton
    private lateinit var recycleView: RecyclerView
    private lateinit var llView: LinearLayout
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        showNotes()

        btnFloat.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.add_layout)

            val btnAdd: AppCompatButton = dialog.findViewById(R.id.btnAdd)
            val edtTitle: EditText = dialog.findViewById(R.id.edtTitle)
            val edtContent: EditText = dialog.findViewById(R.id.edtContent)

            btnAdd.setOnClickListener {
                val title = edtTitle.text.toString()
                val content = edtContent.text.toString()

                if (title.isNotEmpty() && content.isNotEmpty()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        databaseHelper.noteDao().addNote(Notes(title, content))
                        withContext(Dispatchers.Main) {
                            showNotes()
                            dialog.dismiss()
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Title and Content can't be empty", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.show()
        }

        btnCreate.setOnClickListener {
            btnFloat.performClick()
        }
    }

    private fun showNotes() {
        CoroutineScope(Dispatchers.IO).launch {
            val noteList = databaseHelper.noteDao().getNotes()
            val arrayList = ArrayList(noteList)

            withContext(Dispatchers.Main) {
                if (arrayList.isNotEmpty()) {
                    recycleView.visibility = View.VISIBLE
                    llView.visibility = View.GONE
                    recycleView.layoutManager = LinearLayoutManager(this@MainActivity)
                    recycleView.adapter = RecyclerNoteAdapter(this@MainActivity, arrayList)
                } else {
                    llView.visibility = View.VISIBLE
                    recycleView.visibility = View.GONE
                }
            }
        }
    }

    private fun initView() {
        btnCreate = findViewById(R.id.btnCreate)
        btnFloat = findViewById(R.id.addFloat)
        recycleView = findViewById(R.id.recycleList)
        llView = findViewById(R.id.llView)
        databaseHelper = DatabaseHelper.getInstance(applicationContext)
    }
}
