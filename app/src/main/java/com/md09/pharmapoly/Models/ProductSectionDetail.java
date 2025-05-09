package com.md09.pharmapoly.Models;

import java.io.Serializable;

public class ProductSectionDetail implements Serializable {
    private String _id;

    private String product_section_id;

    private String title;
    private String content;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
//
    public String getProduct_section_id() {
        return product_section_id;
    }

    public void setProduct_section_id(String product_section_id) {
        this.product_section_id = product_section_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ProductSectionDetail{" +
                "_id='" + _id + '\'' +
                ", product_section_id='" + product_section_id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
