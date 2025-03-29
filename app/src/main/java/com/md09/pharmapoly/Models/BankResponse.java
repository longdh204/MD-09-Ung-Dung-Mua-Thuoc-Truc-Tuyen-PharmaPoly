package com.md09.pharmapoly.Models;

import java.util.List;

public class BankResponse {
    private String code;
    private String desc;
    private List<Bank> data;

    public List<Bank> getData() {
        return data;
    }
}
