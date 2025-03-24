package com.md09.pharmapoly.Models;

public class PageDataClone<T> {
    private int currentPage;
    private int totalPages;
    private int totalProducts;
    private boolean hasNextPage;
    private boolean hasPrevPage;
    private T products;

    public T getData() {
        return products;
    }

    public void setData(T products) {
        this.products = products;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public boolean isHasPrevPage() {
        return hasPrevPage;
    }

    public void setHasPrevPage(boolean hasPrevPage) {
        this.hasPrevPage = hasPrevPage;
    }
}