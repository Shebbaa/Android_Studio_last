package com.example.folomeev.data;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.folomeev.R;

import com.example.folomeev.adapters.TaskAdapter;
import com.example.folomeev.data.Task;

import java.util.ArrayList;
import java.util.List;
import android.database.Cursor;

public class TaskListActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    int meetingId;
    EditText etNewTask;
    RecyclerView rvTasks;

    TaskAdapter adapter;
    List<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        dbHelper = new DatabaseHelper(this);
        meetingId = getIntent().getIntExtra("meeting_id", -1);
        String title = getIntent().getStringExtra("meeting_title");

        findViewById(R.id.tvTaskListHeader).setOnClickListener(v -> finish());
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        ((TextView)findViewById(R.id.tvTaskListHeader)).setText(title);

        etNewTask = findViewById(R.id.etNewTask);
        rvTasks = findViewById(R.id.rvTasks);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));

        refreshTasks();

        findViewById(R.id.btnAddTodo).setOnClickListener(v -> {
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

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String text = cursor.getString(2);
                int isDoneInt = cursor.getInt(3);

                taskList.add(new Task(id, text, isDoneInt));
            } while (cursor.moveToNext());
        }
        cursor.close();

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