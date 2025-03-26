package com.md09.pharmapoly.Models;

import java.util.Date;
import java.util.List;

public class Order {
    private String _id;
    private String user_id;
    private String order_code;
    private String to_name;
    private String to_phone;
    private String to_address;
    private String to_district_id;
    private String to_ward_code;
    private String payment_method;
    private int shipping_fee;
    private int total_price;
    private String status;
    private Date delivered_at;
    private boolean cancel_request;
    private boolean return_request;
    private Date created_at;
    private Date updated_at;
    private List<OrderItem> items;
    private District district;
    private Province province;
    private Ward ward;
    public Order() {
    }

    public Order(String _id, String user_id, String order_code, String to_name, String to_phone, String to_address, String to_district_id, String to_ward_code, String payment_method, int shipping_fee, int total_price, String status, Date delivered_at, boolean cancel_request, boolean return_request, Date created_at, Date updated_at, List<OrderItem> items, District district, Province province, Ward ward) {
        this._id = _id;
        this.user_id = user_id;
        this.order_code = order_code;
        this.to_name = to_name;
        this.to_phone = to_phone;
        this.to_address = to_address;
        this.to_district_id = to_district_id;
        this.to_ward_code = to_ward_code;
        this.payment_method = payment_method;
        this.shipping_fee = shipping_fee;
        this.total_price = total_price;
        this.status = status;
        this.delivered_at = delivered_at;
        this.cancel_request = cancel_request;
        this.return_request = return_request;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.items = items;
        this.district = district;
        this.province = province;
        this.ward = ward;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getTo_name() {
        return to_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }

    public String getTo_phone() {
        return to_phone;
    }

    public void setTo_phone(String to_phone) {
        this.to_phone = to_phone;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String getTo_district_id() {
        return to_district_id;
    }

    public void setTo_district_id(String to_district_id) {
        this.to_district_id = to_district_id;
    }

    public String getTo_ward_code() {
        return to_ward_code;
    }

    public void setTo_ward_code(String to_ward_code) {
        this.to_ward_code = to_ward_code;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public int getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(int shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDelivered_at() {
        return delivered_at;
    }

    public void setDelivered_at(Date delivered_at) {
        this.delivered_at = delivered_at;
    }

    public boolean isCancel_request() {
        return cancel_request;
    }

    public void setCancel_request(boolean cancel_request) {
        this.cancel_request = cancel_request;
    }

    public boolean isReturn_request() {
        return return_request;
    }

    public void setReturn_request(boolean return_request) {
        this.return_request = return_request;
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

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }
}
