package com.md09.pharmapoly.Models;

public class Bank {
    private int id;
    private String name;
    private String code;
    private String bin;
    private String shortName;
    private String logo;
    private int transferSupported;
    private int lookupSupported;
    private String short_name;
    private int support;
    private int isTransfer;
    private String swiftCode;

    public Bank() {
    }

    public Bank(int id, String name, String code, String bin, String shortName, String logo, int transferSupported, int lookupSupported, String short_name, int support, int isTransfer, String swiftCode) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.bin = bin;
        this.shortName = shortName;
        this.logo = logo;
        this.transferSupported = transferSupported;
        this.lookupSupported = lookupSupported;
        this.short_name = short_name;
        this.support = support;
        this.isTransfer = isTransfer;
        this.swiftCode = swiftCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getTransferSupported() {
        return transferSupported;
    }

    public void setTransferSupported(int transferSupported) {
        this.transferSupported = transferSupported;
    }

    public int getLookupSupported() {
        return lookupSupported;
    }

    public void setLookupSupported(int lookupSupported) {
        this.lookupSupported = lookupSupported;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public int getSupport() {
        return support;
    }

    public void setSupport(int support) {
        this.support = support;
    }

    public int getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(int isTransfer) {
        this.isTransfer = isTransfer;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }
}
