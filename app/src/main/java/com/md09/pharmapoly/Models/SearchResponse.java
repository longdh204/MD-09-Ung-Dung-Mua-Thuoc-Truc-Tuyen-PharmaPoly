package com.md09.pharmapoly.Models;

import java.util.List;

public class SearchResponse {

    private List<Product> products;
    private List<Category> categories;
    private List<Brand> brands;
    private int totalItems;  // Tổng số sản phẩm
    private int totalPages;  // Tổng số trang

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }


    public SearchResponse() {}

    public SearchResponse(List<Product> products, List<Category> categories, List<Brand> brands, int totalPages, int totalItems) {
        this.products = products;
        this.categories = categories;
        this.brands = brands;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Brand> getBrands() {
        return brands;
    }

    public void setBrands(List<Brand> brands) {
        this.brands = brands;
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "products=" + products +
                ", categories=" + categories +
                ", brands=" + brands +
                '}';
    }
}
