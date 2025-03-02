package com.md09.pharmapoly.Models;

import java.util.List;

public class Sections {
    private String _id;
    private String product_id;
    private String section_id;
    private String createdAt;
    private String updatedAt;
    private Section section;
    private List<SectionDetail> details;

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<SectionDetail> getDetails() {
        return details;
    }

    public void setDetails(List<SectionDetail> details) {
        this.details = details;
    }
}
