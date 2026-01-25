package com.example.folomeev.ui;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.folomeev.R;

public class TaskListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        String title = getIntent().getStringExtra("meeting_title");
        TextView tvHeader = findViewById(R.id.tvTaskListHeader);
        if (title != null) tvHeader.setText("Задачи: " + title);
    }
}