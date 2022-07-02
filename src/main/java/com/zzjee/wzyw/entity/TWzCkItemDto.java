package com.zzjee.wzyw.entity;

import org.jeecgframework.poi.excel.annotation.Excel;

public class TWzCkItemDto {

    /**物料名称*/
    @Excel(name="商品编号",width=15)
    private String matCode;
    /**物料名称*/
    @Excel(name="商品名称",width=15)
    private String matName;
    /**仓库*/
    @Excel(name="仓库",width=15)
    private String matLocation;
    /**领用人*/
    @Excel(name="领用人",width=15)
    private String ckName;
    /**出库数量*/
    @Excel(name="出库数量",width=15)
    private String matQty;
    /**单价*/
    @Excel(name="单价",width=15)
    private String matPrice;
    /**物料单位*/
    @Excel(name="单位",width=15)
    private String matUnit;
    /**总价*/
    @Excel(name="总价",width=15)
    private String matAmount;

    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }

    public String getMatName() {
        return matName;
    }

    public void setMatName(String matName) {
        this.matName = matName;
    }

    public String getMatLocation() {
        return matLocation;
    }

    public void setMatLocation(String matLocation) {
        this.matLocation = matLocation;
    }

    public String getCkName() {
        return ckName;
    }

    public void setCkName(String ckName) {
        this.ckName = ckName;
    }

    public String getMatQty() {
        return matQty;
    }

    public void setMatQty(String matQty) {
        this.matQty = matQty;
    }

    public String getMatPrice() {
        return matPrice;
    }

    public void setMatPrice(String matPrice) {
        this.matPrice = matPrice;
    }

    public String getMatUnit() {
        return matUnit;
    }

    public void setMatUnit(String matUnit) {
        this.matUnit = matUnit;
    }

    public String getMatAmount() {
        return matAmount;
    }

    public void setMatAmount(String matAmount) {
        this.matAmount = matAmount;
    }
}
