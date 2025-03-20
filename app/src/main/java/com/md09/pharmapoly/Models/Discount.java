package com.md09.pharmapoly.Models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Discount {
    private String _id;
    private String code;
    private String applies_to;
    private ArrayList<String> target_ids;
    private String type;
    private int value;
    private int min_order_value;
    private int max_discount;
    private Date start_date;
    private Date end_date;
    private int usage_limit;
    private int used_count;
    private Boolean stackable;
    private int status;
    private Date created_at;
    private Date updated_at;

    public Discount() {
    }

    public Discount(String _id, String code, String applies_to, ArrayList<String> target_ids, String type, int value, int minOrderValue, int maxDiscount, Date startDate, Date endDate, int usageLimit, int usedCount, Boolean stackable, int status, Date created_at, Date updated_at) {
        this._id = _id;
        this.code = code;
        this.applies_to = applies_to;
        this.target_ids = target_ids;
        this.type = type;
        this.value = value;
        this.min_order_value = minOrderValue;
        this.max_discount = maxDiscount;
        this.start_date = startDate;
        this.end_date = endDate;
        this.usage_limit = usageLimit;
        this.used_count = usedCount;
        this.stackable = stackable;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getFormattedValue() {
        if ("percent".equalsIgnoreCase(type)) {
            return value + "%";
        } else if ("fixed".equalsIgnoreCase(type)) {
            return value + "đ";
        }
        return String.valueOf(value);
    }
    public String getFormattedEndDate() {
        if (end_date == null) return "N/A";

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
        return sdf.format(end_date);
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getApplies_to() {
        return applies_to;
    }

    public void setApplies_to(String applies_to) {
        this.applies_to = applies_to;
    }

    public ArrayList<String> getTarget_ids() {
        return target_ids;
    }

    public void setTarget_ids(ArrayList<String> target_ids) {
        this.target_ids = target_ids;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMin_order_value() {
        return min_order_value;
    }

    public void setMin_order_value(int min_order_value) {
        this.min_order_value = min_order_value;
    }

    public int getMax_discount() {
        return max_discount;
    }

    public void setMax_discount(int max_discount) {
        this.max_discount = max_discount;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public int getUsage_limit() {
        return usage_limit;
    }

    public void setUsage_limit(int usage_limit) {
        this.usage_limit = usage_limit;
    }

    public int getUsed_count() {
        return used_count;
    }

    public void setUsed_count(int used_count) {
        this.used_count = used_count;
    }

    public Boolean getStackable() {
        return stackable;
    }

    public void setStackable(Boolean stackable) {
        this.stackable = stackable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "_id='" + _id + '\'' +
                ", code='" + code + '\'' +
                ", applies_to='" + applies_to + '\'' +
                ", target_ids=" + target_ids +
                ", type='" + type + '\'' +
                ", value=" + value +
                ", minOrderValue=" + min_order_value +
                ", maxDiscount=" + max_discount +
                ", startDate=" + start_date +
                ", endDate=" + end_date +
                ", usageLimit=" + usage_limit +
                ", usedCount=" + used_count +
                ", stackable=" + stackable +
                ", status=" + status +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
