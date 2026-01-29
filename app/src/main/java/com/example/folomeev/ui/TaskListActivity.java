package com.example.folomeev.ui; // Важно: пакет ui

import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button; // Не забудь добавить этот импорт

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.folomeev.R;
import com.example.folomeev.adapters.TaskAdapter;
import com.example.folomeev.data.DatabaseHelper; // Импорт БД из пакета data
import com.example.folomeev.data.Task; // Импорт модели Task из пакета data

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

        // Инициализация БД
        dbHelper = new DatabaseHelper(this);

        // Получаем данные из Intent
        meetingId = getIntent().getIntExtra("meeting_id", -1);
        String title = getIntent().getStringExtra("meeting_title");

        // Находим Views
        tvHeader = findViewById(R.id.tvTaskListHeader);
        etNewTask = findViewById(R.id.etNewTask);
        rvTasks = findViewById(R.id.rvTasks);
        Button btnAddTodo = findViewById(R.id.btnAddTodo);

        // Настройка UI
        if (title != null) {
            tvHeader.setText(title);
        }

        // Кнопка "Назад" (нажатие на заголовок)
        tvHeader.setOnClickListener(v -> finish());

        // Настройка RecyclerView
        rvTasks.setLayoutManager(new LinearLayoutManager(this));

        // Загружаем задачи при старте
        refreshTasks();

        // Логика кнопки добавления
        btnAddTodo.setOnClickListener(v -> {
            String text = etNewTask.getText().toString();
            if (!text.isEmpty()) {
                // 1. Добавляем в БД
                dbHelper.addTask(meetingId, text);
                // 2. Очищаем поле ввода
                etNewTask.setText("");
                // 3. Обновляем список на экране
                refreshTasks();
            }
        });
    }

    private void refreshTasks() {
        taskList.clear();
        Cursor cursor = dbHelper.getTasksByMeeting(meetingId);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Индексы колонок лучше брать по имени, чтобы избежать ошибок, если структура БД изменится
                int idIndex = cursor.getColumnIndex("id"); // или DatabaseHelper.COLUMN_TASK_ID
                int textIndex = cursor.getColumnIndex("task_text");
                int doneIndex = cursor.getColumnIndex("is_done");

                // Проверка, что колонки найдены (на случай изменений БД)
                if (idIndex != -1 && textIndex != -1 && doneIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String text = cursor.getString(textIndex);
                    int isDoneInt = cursor.getInt(doneIndex);
                    taskList.add(new Task(id, text, isDoneInt));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Если адаптер еще не создан — создаем, если есть — обновляем данные
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