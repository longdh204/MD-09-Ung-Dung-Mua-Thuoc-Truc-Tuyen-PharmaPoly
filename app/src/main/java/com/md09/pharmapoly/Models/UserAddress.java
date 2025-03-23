package com.md09.pharmapoly.Models;

import android.util.Log;

public class UserAddress {
    private String _id;
    private String user_id;
    private int province_id;
    private int district_id;
    private String ward_id;
    private String street_address;
    private Province province;
    private District district;
    private Ward ward;
    public UserAddress() {
    }

    public UserAddress(String _id, String user_id, int province_id, int district_id, String ward_id, String street_address, Province province, District district, Ward ward) {
        this._id = _id;
        this.user_id = user_id;
        this.province_id = province_id;
        this.district_id = district_id;
        this.ward_id = ward_id;
        this.street_address = street_address;
        this.province = province;
        this.district = district;
        this.ward = ward;
    }

    @Override
    public String toString() {
        return "UserAddress{" +
                "province_id=" + province_id +
                ", district_id=" + district_id +
                ", ward_id='" + ward_id + '\'' +
                ", street_address='" + street_address + '\'' +
                ", province=" + province +
                ", district=" + district +
                ", ward=" + ward +
                '}';
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

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public String getWard_id() {
        return ward_id;
    }

    public void setWard_id(String ward_id) {
        this.ward_id = ward_id;
    }

    public String getStreet_address() {
        return street_address;
    }

    public void setStreet_address(String street_address) {
        this.street_address = street_address;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province_id = province.getProvinceID();
        this.province = province;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district_id = district.getDistrictID();
        this.district = district;
    }

    public Ward getWard() {
        return ward;
    }

    public void setWard(Ward ward) {
        this.ward_id = ward.getWardCode();
        this.ward = ward;
    }
}
