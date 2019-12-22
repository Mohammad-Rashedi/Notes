package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.notes.Room.Note;
import com.example.notes.View.AddEditNoteActivity;
import com.example.notes.View.NoteAdapter;
import com.example.notes.ViewModel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

//LifeCycle library has ViewModel class and LiveData and ...
public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    NoteViewModel noteViewModel;
    AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialogBuilder = new AlertDialog.Builder(this);
        //add button
        FloatingActionButton buttonAddNote = findViewById(R.id.add_note_button);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentAddNote = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intentAddNote, ADD_NOTE_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NoteAdapter noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);
        recyclerView.setHasFixedSize(true);
        noteViewModel = ViewModelProviders.of(this)
                .get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                noteAdapter.setNotes(notes);
            }
        });
        //swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            //showing delete dialog
                dialogBuilder.setTitle("DELETE")
                        .setMessage("Do you wanr to delete note?");
                dialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        noteViewModel.deleteNote(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                        Toast.makeText(MainActivity.this, "note deleted", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
                dialogBuilder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                noteAdapter.notifyDataSetChanged();
                    }
                });
                dialogBuilder.create();
                dialogBuilder.show();
            }
        }).attachToRecyclerView(recyclerView);

        noteAdapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intentEditNote = new Intent(MainActivity.this,
                        AddEditNoteActivity.class);
                intentEditNote.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                intentEditNote.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                intentEditNote.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());
                intentEditNote.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
                startActivityForResult(intentEditNote,EDIT_NOTE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);
            Note note = new Note(title, description, priority);
            noteViewModel.insertNote(note);
            Toast.makeText(this, "note inserted", Toast.LENGTH_SHORT).show();
            } else if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "note not updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);
            Note note = new Note(title, description, priority);
            note.setId(id);
            noteViewModel.updateNote(note);
            Toast.makeText(this, "note updated", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_menu_main_activity:
                noteViewModel.deleteAllNote();
                Toast.makeText(this, "all notes deleted", Toast.LENGTH_SHORT).show();
                return true;
                default:return super.onOptionsItemSelected(item);
        }

    }
}
