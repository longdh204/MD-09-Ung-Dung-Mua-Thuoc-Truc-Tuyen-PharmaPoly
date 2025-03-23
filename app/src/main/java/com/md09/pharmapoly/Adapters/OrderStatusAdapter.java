package com.md09.pharmapoly.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.R;

import java.util.List;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.ViewHolder> {
    private final List<String> orderStatuses;
    private int selectedPosition = 0;
    private final OnStatusClickListener listener;

    public interface OnStatusClickListener {
        void onStatusClick(View itemView, int position);
    }

    public OrderStatusAdapter(List<String> orderStatuses, OnStatusClickListener listener) {
        this.orderStatuses = orderStatuses;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_status, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String status = orderStatuses.get(position);
        holder.tvOrderStatus.setText(status);
        boolean isSelected = position == selectedPosition;
        int textColor = isSelected
                ? ContextCompat.getColor(holder.itemView.getContext(), R.color.black_333)
                : ContextCompat.getColor(holder.itemView.getContext(), R.color.gray_E9E);
        holder.tvOrderStatus.setTextColor(textColor);


//        holder.tvOrderStatus.setOnClickListener(v -> {
//            selectedPosition = position;
//            notifyDataSetChanged();
//            listener.onStatusClick(position);
//        });
        holder.itemView.setOnClickListener(v -> {
            int prevPosition = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(prevPosition);
            notifyItemChanged(selectedPosition);
            listener.onStatusClick(holder.itemView, position);
        });
    }

    @Override
    public int getItemCount() {
        return orderStatuses.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderStatus;

        ViewHolder(View itemView) {
            super(itemView);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
        }
    }
}
