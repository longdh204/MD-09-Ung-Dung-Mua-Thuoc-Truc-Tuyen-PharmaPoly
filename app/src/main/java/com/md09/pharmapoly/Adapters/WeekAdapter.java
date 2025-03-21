package com.md09.pharmapoly.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.WeekViewHolder> {
    private Context context;
    private List<Calendar> weekDays;
    private Calendar selectedDate;
    private OnDateClickListener listener;

    public interface OnDateClickListener {
        void onDateClick(Calendar date);
    }

    public WeekAdapter(Context context, List<Calendar> weekDays, Calendar selectedDate, OnDateClickListener listener) {
        this.context = context;
        this.weekDays = weekDays;
        this.selectedDate = selectedDate;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WeekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_day, parent, false);
        return new WeekViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeekViewHolder holder, int position) {
        Calendar day = weekDays.get(position);
        SimpleDateFormat dayFormat = new SimpleDateFormat("E", new Locale("vi")); // Lấy tên thứ (T2, T3...)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());

        holder.txtDayName.setText(dayFormat.format(day.getTime())); // Gán thứ
        holder.tvDay.setText(dateFormat.format(day.getTime())); // Gán ngày

        // Đánh dấu ngày hiện tại
        if (day.get(Calendar.DAY_OF_YEAR) == selectedDate.get(Calendar.DAY_OF_YEAR)) {
            holder.tvDay.setTextColor(Color.WHITE);
            holder.tvDay.setBackgroundResource(R.drawable.bg_date_selected);
        } else {
            holder.tvDay.setTextColor(Color.BLACK);
            holder.tvDay.setBackgroundResource(R.drawable.bg_date_unselected);
        }

        holder.itemView.setOnClickListener(v -> listener.onDateClick(day));
    }


    @Override
    public int getItemCount() {
        return weekDays.size();
    }

    public static class WeekViewHolder extends RecyclerView.ViewHolder {
        TextView txtDayName, tvDay;

        public WeekViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDayName = itemView.findViewById(R.id.txtDayName);
            tvDay = itemView.findViewById(R.id.tvDay);
        }
    }

}

