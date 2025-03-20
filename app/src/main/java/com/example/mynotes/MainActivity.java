package com.example.mynotes;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private AppCompatButton btnCreate;
    private FloatingActionButton btnFloat;
    private RecyclerView recycleView;
    private LinearLayout llView;
    private DatabaseHelper databaseHelper;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        showNotes();

        btnFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_layout);

                AppCompatButton btnAdd = dialog.findViewById(R.id.btnAdd);
                EditText edtTitle = dialog.findViewById(R.id.edtTitle);
                EditText edtContent = dialog.findViewById(R.id.edtContent);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = edtTitle.getText().toString();
                        String content = edtContent.getText().toString();

                        if (!title.isEmpty() && !content.isEmpty()) {
                            executorService.execute(new Runnable() {
                                @Override
                                public void run() {
                                    databaseHelper.noteDao().addNote(new Notes(title, content));
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            showNotes();
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this, "Title and Content can't be empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFloat.performClick();
            }
        });
    }

    public void showNotes() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                List<Notes> noteList = databaseHelper.noteDao().getNotes();
                ArrayList<Notes> arrayList = new ArrayList<>(noteList);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!arrayList.isEmpty()) {
                            recycleView.setVisibility(View.VISIBLE);
                            llView.setVisibility(View.GONE);
                            recycleView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            recycleView.setAdapter(new RecyclerNoteAdapter(MainActivity.this, arrayList, databaseHelper));
                        } else {
                            llView.setVisibility(View.VISIBLE);
                            recycleView.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    private void initView() {
        btnCreate = findViewById(R.id.btnCreate);
        btnFloat = findViewById(R.id.addFloat);
        recycleView = findViewById(R.id.recycleList);
        llView = findViewById(R.id.llView);
        databaseHelper = DatabaseHelper.getInstance(getApplicationContext());
    }
}
