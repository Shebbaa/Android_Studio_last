package com.example.folomeev.adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.folomeev.R;
import com.example.folomeev.data.Task;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private final OnTaskStatusChangeListener listener;

    public interface OnTaskStatusChangeListener {
        void onTaskStatusChanged(Task task, boolean isDone);
    }

    public TaskAdapter(List<Task> tasks, OnTaskStatusChangeListener listener) {
        this.tasks = tasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        holder.tvTaskText.setText(task.text);

        holder.cbDone.setOnCheckedChangeListener(null);
        holder.cbDone.setChecked(task.isDone);

        toggleStrikeThrough(holder.tvTaskText, task.isDone);

        holder.cbDone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            toggleStrikeThrough(holder.tvTaskText, isChecked);
            listener.onTaskStatusChanged(task, isChecked);
        });
    }

    private void toggleStrikeThrough(TextView tv, boolean isDone) {
        if (isDone) {
            tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv.setAlpha(0.5f);
        } else {
            tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            tv.setAlpha(1.0f);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void updateList(List<Task> newTasks) {
        this.tasks = newTasks;
        notifyDataSetChanged();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbDone;
        TextView tvTaskText;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            cbDone = itemView.findViewById(R.id.cbDone);
            tvTaskText = itemView.findViewById(R.id.tvTaskText);
        }
    }
}