package com.example.folomeev.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.folomeev.R;
import com.example.folomeev.adapters.MeetingAdapter;
import com.example.folomeev.data.DatabaseHelper;
import com.example.folomeev.data.Meeting;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    MeetingAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewMeetings);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        adapter = new MeetingAdapter(dbHelper.getAllMeetings(), new MeetingAdapter.OnMeetingClickListener() {
            @Override
            public void onMeetingClick(Meeting meeting) {
                Intent intent = new Intent(HomeActivity.this, TaskListActivity.class);
                intent.putExtra("meeting_id", meeting.getId());
                intent.putExtra("meeting_title", meeting.getTitle());
                startActivity(intent);
            }
            @Override
            public void onMoreClick(Meeting meeting) {
                Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                intent.putExtra("id", meeting.getId());
                intent.putExtra("title", meeting.getTitle());
                intent.putExtra("category", meeting.getCategory());
                intent.putExtra("date", meeting.getDate());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);

        findViewById(R.id.fabAddMeeting).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddMeetingActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(adapter != null) {
            adapter.updateList(dbHelper.getAllMeetings());
        }
    }
}