package com.example.folomeev.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.folomeev.R;
import com.example.folomeev.adapters.TaskAdapter;
import com.example.folomeev.data.DatabaseHelper;
import com.example.folomeev.data.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    int meetingId;
    EditText etNewTask;
    RecyclerView rvTasks;
    TextView tvHeader;

    TaskAdapter adapter;
    List<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        dbHelper = new DatabaseHelper(this);

        meetingId = getIntent().getIntExtra("meeting_id", -1);
        String title = getIntent().getStringExtra("meeting_title");

        tvHeader = findViewById(R.id.tvTaskListHeader);
        etNewTask = findViewById(R.id.etNewTask);
        rvTasks = findViewById(R.id.rvTasks);
        Button btnAddTodo = findViewById(R.id.btnAddTodo);

        if (title != null) {
            tvHeader.setText(title);
        }

        tvHeader.setOnClickListener(v -> finish());

        rvTasks.setLayoutManager(new LinearLayoutManager(this));

        refreshTasks();

        btnAddTodo.setOnClickListener(v -> {
            String text = etNewTask.getText().toString();
            if (!text.isEmpty()) {
                dbHelper.addTask(meetingId, text);
                etNewTask.setText("");
                refreshTasks();
            }
        });
    }

    private void refreshTasks() {
        taskList.clear();
        Cursor cursor = dbHelper.getTasksByMeeting(meetingId);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int textIndex = cursor.getColumnIndex("task_text");
                int doneIndex = cursor.getColumnIndex("is_done");

                if (idIndex != -1 && textIndex != -1 && doneIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String text = cursor.getString(textIndex);
                    int isDoneInt = cursor.getInt(doneIndex);
                    taskList.add(new Task(id, text, isDoneInt));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        if (adapter == null) {
            adapter = new TaskAdapter(taskList, new TaskAdapter.OnTaskStatusChangeListener() {
                @Override
                public void onTaskStatusChanged(Task task, boolean isDone) {
                    dbHelper.updateTaskStatus(task.id, isDone);
                }
            });
            rvTasks.setAdapter(adapter);
        } else {
            adapter.updateList(taskList);
        }
    }
}