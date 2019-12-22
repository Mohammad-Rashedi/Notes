package com.example.notes.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.notes.R;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_TITLE = "com.example.notes.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.notes.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.example.notes.EXTRA_PRIORITY";
    public static final String EXTRA_ID = "com.example.notes.EXTRA_ID";

    EditText editTextTitle;
    EditText editTextDescription;
    NumberPicker numberPickerPriority;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        editTextTitle = findViewById(R.id.edit_text_title_add);
        editTextDescription = findViewById(R.id.edit_text_description_add);
        numberPickerPriority = findViewById(R.id.number_picker_priority_add);

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        ActionBar bar = getSupportActionBar();
        bar.setHomeAsUpIndicator(R.drawable.ic_close);
        bar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));

        }else{
            setTitle("Add Note");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu_item:
                saveNote();
            case android.R.id.home:
                finish();

                Toast.makeText(this, "home pressed", Toast.LENGTH_SHORT).show();
                default:
                    super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();
        if(title.trim().isEmpty()|description.trim().isEmpty()){
            Toast.makeText(this, "pls enter a value for title and description",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Intent dataIntent = new Intent();
        dataIntent.putExtra(EXTRA_TITLE, title);
        dataIntent.putExtra(EXTRA_DESCRIPTION, description);
        dataIntent.putExtra(EXTRA_PRIORITY, priority);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            dataIntent.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK,dataIntent);
        finish();
    }
}
