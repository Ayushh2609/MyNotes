package com.example.mynotes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerNoteAdapter extends RecyclerView.Adapter<RecyclerNoteAdapter.ViewHolder> {

    private final Context context;
    private final List<Notes> notesList;
    DatabaseHelper databaseHelper;

    public RecyclerNoteAdapter(Context context, List<Notes> notesList, DatabaseHelper databaseHelper) {
        this.context = context;
        this.notesList = notesList;

        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notes note = notesList.get(position);
        holder.txtTitle.setText(note.title);
        holder.txtContent.setText(note.content);

        holder.llNote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                deleteItem(position);

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtContent;
        LinearLayout llNote;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtLayoutTitle);
            txtContent = itemView.findViewById(R.id.txtLayoutContent);

            llNote = itemView.findViewById(R.id.llNote);
        }
    }

    public void deleteItem(int pos) {

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Delete")
                .setMessage("Pakka delete kardu na?, sochle...")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                databaseHelper.noteDao().deleteNote(new Notes(notesList.get(pos).getId(),
                                        notesList.get(pos).getTitle(), notesList.get(pos).getContent()));

                                // Remove from list and notify on main thread
                                ((MainActivity) context).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        notesList.remove(pos);
                                        notifyItemRemoved(pos);
                                        ((MainActivity) context).showNotes(); // refresh
                                    }
                                });
                            }
                        }).start();
                    }
                })
                .setNegativeButton("No", null)
                .show();

    }
}
