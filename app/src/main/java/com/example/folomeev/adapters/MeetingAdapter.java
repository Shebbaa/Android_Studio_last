package com.example.folomeev.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.folomeev.R;
import com.example.folomeev.data.Meeting;
import java.util.List;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> {

    private List<Meeting> meetings;
    private final OnMeetingClickListener onClickListener; // 1. Переменная для клика

    // 2. Интерфейс для кликов
    public interface OnMeetingClickListener {
        void onMeetingClick(Meeting meeting, int position);
    }

    // 3. Обновили конструктор: теперь он просит еще и слушатель кликов
    public MeetingAdapter(List<Meeting> meetings, OnMeetingClickListener onClickListener) {
        this.meetings = meetings;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meeting, parent, false);
        return new MeetingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingViewHolder holder, int position) {
        Meeting meeting = meetings.get(position);
        holder.tvTitle.setText(meeting.getTitle());
        holder.tvCategory.setText(meeting.getCategory());
        holder.tvDate.setText(meeting.getDate());

        // 4. ВЕШАЕМ ОБРАБОТЧИК НАЖАТИЯ на весь элемент списка
        holder.itemView.setOnClickListener(v -> {
            if (onClickListener != null) {
                onClickListener.onMeetingClick(meeting, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    // 5. Исправили обновление списка
    public void updateList(List<Meeting> newMeetings) {
        this.meetings = newMeetings;
        notifyDataSetChanged(); // <--- ВОТ ЭТОЙ СТРОЧКИ НЕ ХВАТАЛО для обновления!
    }

    public static class MeetingViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvDate;

        public MeetingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}