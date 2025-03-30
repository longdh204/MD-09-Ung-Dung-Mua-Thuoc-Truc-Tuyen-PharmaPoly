package com.md09.pharmapoly.Adapters;

import static com.md09.pharmapoly.utils.Constants.findObjectById;
import static com.md09.pharmapoly.utils.Constants.formatCurrency;
import static com.md09.pharmapoly.utils.Constants.getDisplayStatus;
import static com.md09.pharmapoly.utils.Constants.getStatusColor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.md09.pharmapoly.Models.Order;
import com.md09.pharmapoly.Models.OrderItem;
import com.md09.pharmapoly.R;
import com.md09.pharmapoly.ui.view.activity.OrderInfoActivity;
import com.md09.pharmapoly.utils.Constants;
import com.md09.pharmapoly.utils.DialogHelper;
import com.md09.pharmapoly.utils.PaymentMethod;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PurchasedOrdersAdapter extends RecyclerView.Adapter<PurchasedOrdersAdapter.ViewHolder> {

    private Context context;
    private List<Order> orders;
    private Constants.OrderStatusGroup orderStatusGroup;
    private OrderActionListener listener;

    public interface OrderActionListener {
        void onCancelOrder(Order order);

        //void onReturnOrExchangeOrder(Order order);
    }

    public PurchasedOrdersAdapter(Context context, List<Order> orders, Constants.OrderStatusGroup orderStatusGroup, OrderActionListener listener) {
        this.context = context;
        this.orders = orders;
        this.orderStatusGroup = orderStatusGroup;
        this.listener = listener;
    }

    public void Update(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }
    public void UpdateItem(Order updatedOrder) {
        for (int i = 0; i < orders.size(); i++) {
            if (orders.get(i).get_id().equals(updatedOrder.get_id())) {
                orders.set(i, updatedOrder);
                notifyItemChanged(i);
                break;
            }
        }
    }

    @NonNull
    @Override
    public PurchasedOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PurchasedOrdersAdapter.ViewHolder holder, int position) {
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.slide_in_left);
        holder.itemView.startAnimation(animation);

        Order order = orders.get(position);

        if (order.getPayment_method().equals(PaymentMethod.ONLINE.getValue())) {
            holder.tv_payment_status.setVisibility(View.VISIBLE);
            updatePaymentStatus(holder.tv_payment_status, order.getPayment_status());
        } else {
            holder.tv_payment_status.setVisibility(View.GONE);
        }

        holder.layout_order_item.removeAllViews();
        SetOrderStatus(holder, order);

        List<OrderItem> items = order.getItems();
        int itemCount = items.size();
        int productCount = 0;


        for (OrderItem item : items) {
            productCount += item.getQuantity();
            View view = LayoutInflater.from(context).inflate(R.layout.checkout_item, null, false);

            TextView tv_quantity = view.findViewById(R.id.tv_quantity);
            TextView tv_product_name = view.findViewById(R.id.tv_product_name);
            TextView tv_original_price = view.findViewById(R.id.tv_original_price);
            ImageView img_product = view.findViewById(R.id.img_product);

            tv_quantity.setText("x" + item.getQuantity());
            tv_product_name.setText(item.getProduct().getName());
            tv_original_price.setText(formatCurrency(item.getPrice(), "đ"));
            Picasso.get().load(item.getProduct().getImageUrl()).into(img_product);

            holder.layout_order_item.addView(view);
        }

        if (itemCount > 1) {
            holder.layout_order_item.post(() -> {
                int itemHeight = holder.layout_order_item.getChildAt(0).getHeight();
                ViewGroup.LayoutParams params = holder.layout_order_item.getLayoutParams();
                params.height = itemHeight;
                holder.layout_order_item.setLayoutParams(params);
            });

            holder.btn_show_more.setVisibility(View.VISIBLE);
        } else {
            holder.btn_show_more.setVisibility(View.GONE);
        }
        holder.tv_total_price.setText(
                context.getString(R.string.total_amount) + " ( " +
                        productCount + " " +
                        context.getString(R.string.product).toLowerCase() + " ): " +
                        formatCurrency(order.getTotal_price(), "đ")
        );

        holder.btn_show_more.setOnClickListener(view -> {
            holder.btn_show_more.setVisibility(View.GONE);

            holder.layout_order_item.post(() -> {
                int itemHeight = holder.layout_order_item.getChildAt(0).getHeight();
                int totalHeight = itemHeight * itemCount;

                ViewGroup.LayoutParams params = holder.layout_order_item.getLayoutParams();
                params.height = totalHeight;
                holder.layout_order_item.setLayoutParams(params);
            });
        });
        if (order.isCancel_request() ||
                //order.isReturn_request() ||
                order.getStatus().equals(Constants.OrderStatusGroup.DELIVERED.toString().toLowerCase())) {
            holder.btn_order_item.setVisibility(View.GONE);
        } else {
            handleOrderActionButton(holder, order, orderStatusGroup);
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderInfoActivity.class);
            intent.putExtra("order_id", order.get_id());
            context.startActivity(intent);
        });
    }

    private void handleOrderActionButton(ViewHolder holder, Order order, Constants.OrderStatusGroup orderStatusGroup) {
        if (orderStatusGroup == Constants.OrderStatusGroup.PROCESSING) {
            holder.btn_order_item.setCardBackgroundColor(context.getResources().getColor(R.color.red_757));
            holder.tv_btn_order.setText(R.string.cancel_order);
            holder.btn_order_item.setOnClickListener(v -> {
                DialogHelper.ShowConfirmationDialog(
                        context,
                        context.getString(R.string.cancel_order_title),
                        context.getString(R.string.cancel_order_message),
                        "OK",
                        "Cancel",
                        () -> listener.onCancelOrder(order)
                );
            });
        }
//        else if (order.getStatus().equals("delivering") || order.getStatus().equals("money_collect_delivering")) {
//            holder.btn_order_item.setCardBackgroundColor(context.getResources().getColor(R.color.green_500));
//            holder.tv_btn_order.setText(R.string.confirm_received);
//            holder.btn_order_item.setOnClickListener(v -> {
//                DialogHelper.ShowConfirmationDialog(
//                        context,
//                        context.getString(R.string.confirm_received_title),
//                        context.getString(R.string.confirm_received_message),
//                        "OK",
//                        "Cancel",
//                        () -> listener.onConfirmReceived(order)
//                );
//            });
//
//        }
//        else if (orderStatusGroup == Constants.OrderStatusGroup.DELIVERED) {
//            if (canReturnOrder(order)) {
//                holder.btn_order_item.setCardBackgroundColor(context.getResources().getColor(R.color.green_500));
//                holder.tv_btn_order.setText(R.string.return_order);
//                holder.btn_order_item.setOnClickListener(v -> {
//                    DialogHelper.ShowConfirmationDialog(
//                            context,
//                            context.getString(R.string.return_order_title),
//                            context.getString(R.string.return_order_message),
//                            "OK",
//                            "Cancel",
//                            () -> listener.onReturnOrExchangeOrder(order)
//                    );
//                });
//            } else {
//                holder.btn_order_item.setVisibility(View.GONE);
//            }
//        }
        else if (orderStatusGroup == Constants.OrderStatusGroup.CANCELED) {
            holder.btn_order_item.setVisibility(View.GONE);
        } else {
            holder.btn_order_item.setCardBackgroundColor(context.getResources().getColor(R.color.blue_CE4));
            holder.tv_btn_order.setText(R.string.details);
        }
    }



    private void updatePaymentStatus(TextView tv_payment_status, String paymentStatus) {
        int color;
        String text;

        switch (paymentStatus) {
            case "paid":
                color = ContextCompat.getColor(context, R.color.green_500);
                text = context.getString(R.string.payment_paid);
                break;
            case "pending":
                color = ContextCompat.getColor(context, R.color.orange_500);
                text = context.getString(R.string.payment_pending);
                break;
            case "failed":
                color = ContextCompat.getColor(context, R.color.red_757);
                text = context.getString(R.string.payment_failed);
                break;
            case "refunded":
            default:
                color = ContextCompat.getColor(context, R.color.green_500);
                text = context.getString(R.string.payment_refunded);
                break;
        }

        String statusTitle = context.getString(R.string.payment_status) + ": ";
        SpannableString spannable = new SpannableString(statusTitle + text);
        spannable.setSpan(new ForegroundColorSpan(Color.BLACK), 0, statusTitle.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(color), statusTitle.length(), spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_payment_status.setText(spannable);
    }

    private boolean canReturnOrder(Order order) {
        if (order.getDelivered_at() == null) return false;
        Date now = new Date();
        long diff = now.getTime() - order.getDelivered_at().getTime();
        long daysDiff = TimeUnit.MILLISECONDS.toDays(diff);
        return daysDiff <= 7;
    }

    private void SetOrderStatus(ViewHolder holder, Order order) {
        //String prefix = context.getString(R.string.order_status_title) + ": ";
        String prefix = "";
        String displayStatus = getDisplayStatus(context, order.getStatus());

        SpannableStringBuilder spannable = new SpannableStringBuilder();

        spannable.append(prefix);
        spannable.setSpan(context.getColor(R.color.black_333), 0, prefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        int statusColor = getStatusColor(context, order.getStatus());
        int start = spannable.length();
        spannable.append(displayStatus);
        spannable.setSpan(new ForegroundColorSpan(statusColor), start, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (order.getStatus().equals("ready_to_pick")) {
            String confirmedRequestText = getDisplayStatus(context, "confirmed") + " - ";
            spannable.insert(0, confirmedRequestText);
            spannable.setSpan(
                    new ForegroundColorSpan(getStatusColor(context, "confirmed")),
                    0,
                    confirmedRequestText.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }
        if (!order.getStatus().equals("canceled") && order.isCancel_request()) {
            String cancelRequestText = " - " + context.getString(R.string.cancel_request_sent);
            spannable.append(cancelRequestText);
            spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.red_757)), spannable.length() - cancelRequestText.length(), spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
//        if (!order.getStatus().equals("canceled") && order.isReturn_request()) {
//            String returnRequestText = " - " + context.getString(R.string.return_request_sent);
//            int returnStart = spannable.length();
//            spannable.append(returnRequestText);
//            spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.blue_CE4)), returnStart, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
        holder.tv_order_status.setText(spannable);
    }


    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout
                layout_order_item,
                btn_show_more;
        private TextView tv_total_price, tv_order_status, tv_payment_status, tv_delivery_date;
        private CardView btn_order_item;
        private TextView tv_btn_order;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btn_order_item = itemView.findViewById(R.id.btn_order_item);
            tv_btn_order = itemView.findViewById(R.id.tv_btn_order);
            layout_order_item = itemView.findViewById(R.id.layout_order_item);
            tv_total_price = itemView.findViewById(R.id.tv_total_price);
            tv_delivery_date = itemView.findViewById(R.id.tv_delivery_date);
            btn_show_more = itemView.findViewById(R.id.btn_show_more);
            tv_order_status = itemView.findViewById(R.id.tv_order_status);
            tv_payment_status = itemView.findViewById(R.id.tv_payment_status);
        }
    }
}
