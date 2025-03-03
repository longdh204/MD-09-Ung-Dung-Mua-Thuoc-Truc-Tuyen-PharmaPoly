package com.md09.pharmapoly.Models;

import java.util.List;

public class ProductSection {
    private String _id;
    private String product_id;
    private String section_id;
    private Section section;
    private List<ProductSectionDetail> details;

    public ProductSection() {
    }

    public ProductSection(String _id, String product_id, String section_id, Section section, List<ProductSectionDetail> details) {
        this._id = _id;
        this.product_id = product_id;
        this.section_id = section_id;
        this.section = section;
        this.details = details;
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

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public List<ProductSectionDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ProductSectionDetail> details) {
        this.details = details;
    }
}
//