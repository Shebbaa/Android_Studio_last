package com.example.folomeev.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.folomeev.R;
import com.example.folomeev.data.DatabaseHelper;
import com.example.folomeev.data.Meeting;

public class DetailActivity extends AppCompatActivity {

    EditText etTitle, etCategory, etDate;
    Button btnSave, btnDelete;
    DatabaseHelper dbHelper;
    int meetingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dbHelper = new DatabaseHelper(this);

        etTitle = findViewById(R.id.etTitleDetail);
        etCategory = findViewById(R.id.etCategoryDetail);
        etDate = findViewById(R.id.etDateDetail);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        if (getIntent().hasExtra("id")) {
            meetingId = getIntent().getIntExtra("id", -1);
            etTitle.setText(getIntent().getStringExtra("title"));
            etCategory.setText(getIntent().getStringExtra("category"));
            etDate.setText(getIntent().getStringExtra("date"));
        }

        btnSave.setOnClickListener(v -> {
            String newTitle = etTitle.getText().toString();
            String newCategory = etCategory.getText().toString();
            String newDate = etDate.getText().toString();

            if (!newTitle.isEmpty()) {
                Meeting updatedMeeting = new Meeting(meetingId, newTitle, newCategory, newDate);
                dbHelper.updateMeeting(updatedMeeting);
                finish();
            } else {
                Toast.makeText(this, "Заполните название", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            dbHelper.deleteMeeting(meetingId);
            finish();
        });
    }
}