package com.example.folomeev.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.folomeev.R;
import com.example.folomeev.data.DatabaseHelper;
import com.example.folomeev.data.Meeting;

public class AddMeetingActivity extends AppCompatActivity {

    EditText etTitle, etCategory, etDate;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meeting);

        etTitle = findViewById(R.id.etTitle);
        etCategory = findViewById(R.id.etCategory);
        etDate = findViewById(R.id.etDate);
        dbHelper = new DatabaseHelper(this);

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            String title = etTitle.getText().toString();
            String category = etCategory.getText().toString();
            String date = etDate.getText().toString();

            if (title.isEmpty()) {
                Toast.makeText(this, "Введите название!", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.addMeeting(new Meeting(title, category, date));
            finish();
        });
    }



}