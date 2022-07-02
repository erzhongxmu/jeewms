package com.zzjee.wzyw.entity;

import org.jeecgframework.poi.excel.annotation.Excel;

public class TWzRkItemDto {

    /**主键*/
    private Integer id;
    /**物料名称*/
    @Excel(name="编码/商品名称",width=15)
    private String matName;
    /**物料编码*/
    @Excel(name="规格",width=15)
    private String spec;
    /**物料单位*/
    @Excel(name="单位",width=15)
    private String matUnit;
    /**仓库*/
    @Excel(name="仓库",width=15)
    private String matLocation;
    /**入库数量*/
    @Excel(name="入库数量",width=15)
    private String matQty;
    /**单价*/
    @Excel(name="单价",width=15)
    private String matPrice;
    /**总价*/
    private String matAmount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMatName() {
        return matName;
    }

    public void setMatName(String matName) {
        this.matName = matName;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getMatUnit() {
        return matUnit;
    }

    public void setMatUnit(String matUnit) {
        this.matUnit = matUnit;
    }

    public String getMatLocation() {
        return matLocation;
    }

    public void setMatLocation(String matLocation) {
        this.matLocation = matLocation;
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

    public String getMatAmount() {
        return matAmount;
    }

    public void setMatAmount(String matAmount) {
        this.matAmount = matAmount;
    }
}
