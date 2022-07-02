package com.zzjee.wzpo.entity;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: v_wz_po_wq
 * @author onlineGenerator
 * @date 2018-05-21 15:57:04
 * @version V1.0
 *
 */
@Entity
@Table(name = "v_wz_po_wq", schema = "")
@SuppressWarnings("serial")
public class VWzPoWqEntity implements java.io.Serializable {
	/**id*/
	private String id;
	/**供应商编码*/
	@Excel(name="供应商编码",width=15)
	private String vendorCode;
	/**供应商名称*/
	@Excel(name="供应商名称",width=15)
	private String vendorName;
	/**采购订单日期*/
	@Excel(name="采购订单日期",width=15,format = "yyyy-MM-dd")
	private Date docDate;
	/**采购订单备注*/
	@Excel(name="采购订单备注",width=15)
	private String poRemark;
	/**物料编码*/
	@Excel(name="物料编码",width=15)
	private String matCode;
	/**物料名称*/
	@Excel(name="物料名称",width=15)
	private String matName;
	/**单位*/
	@Excel(name="单位",width=15)
	private String matUnit;
	/**仓库*/
	@Excel(name="仓库",width=15)
	private String matLocation;
	/**批次*/
	@Excel(name="批次",width=15)
	private String matBatch;
	/**标准价*/
	@Excel(name="标准价",width=15)
	private String matPrice;
	/**数量*/
	@Excel(name="数量",width=15)
	private String matQty;
	/**matQtyWq*/
//	@Excel(name="matQtyWq",width=15)
	private Double matQtyWq;
	@Excel(name="未清数量",width=15)
	private Double matQtyWq1;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  id
	 */
	@Id
	@GeneratedValue(generator = "paymentableGenerator")
	@GenericGenerator(name = "paymentableGenerator", strategy = "uuid")

	@Column(name ="ID",nullable=false,length=36)
	public String getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  id
	 */
	public void setId(String id){
		this.id = id;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  物料编码
	 */

	@Column(name ="MAT_CODE",nullable=true,length=32)
	public String getMatCode(){
		return this.matCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  物料编码
	 */
	public void setMatCode(String matCode){
		this.matCode = matCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  物料名称
	 */

	@Column(name ="MAT_NAME",nullable=true,length=32)
	public String getMatName(){
		return this.matName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  物料名称
	 */
	public void setMatName(String matName){
		this.matName = matName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位
	 */

	@Column(name ="MAT_UNIT",nullable=true,length=32)
	public String getMatUnit(){
		return this.matUnit;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位
	 */
	public void setMatUnit(String matUnit){
		this.matUnit = matUnit;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  仓库
	 */

	@Column(name ="MAT_LOCATION",nullable=true,length=32)
	public String getMatLocation(){
		return this.matLocation;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  仓库
	 */
	public void setMatLocation(String matLocation){
		this.matLocation = matLocation;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  批次
	 */

	@Column(name ="MAT_BATCH",nullable=true,length=32)
	public String getMatBatch(){
		return this.matBatch;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  批次
	 */
	public void setMatBatch(String matBatch){
		this.matBatch = matBatch;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  标准价
	 */

	@Column(name ="MAT_PRICE",nullable=true,length=32)
	public String getMatPrice(){
		return this.matPrice;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  标准价
	 */
	public void setMatPrice(String matPrice){
		this.matPrice = matPrice;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  数量
	 */

	@Column(name ="MAT_QTY",nullable=true,length=32)
	public String getMatQty(){
		return this.matQty;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  数量
	 */
	public void setMatQty(String matQty){
		this.matQty = matQty;
	}
	/**
	 *方法: 取得java.lang.Double
	 *@return: java.lang.Double  matQtyWq
	 */

	@Column(name ="MAT_QTY_WQ",nullable=true,length=23)
	public Double getMatQtyWq(){
		return this.matQtyWq;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  matQtyWq
	 */
	public void setMatQtyWq(Double matQtyWq){
		this.matQtyWq = matQtyWq;
	}

	@Column(name ="MAT_QTY_WQ1",nullable=true,length=23)
	public Double getMatQtyWq1(){
		return this.matQtyWq1;
	}

	/**
	 *方法: 设置java.lang.Double
	 *@param: java.lang.Double  matQtyWq
	 */
	public void setMatQtyWq1(Double matQtyWq1){
		this.matQtyWq1 = matQtyWq1;
	}


	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  供应商编码
	 */

	@Column(name ="VENDOR_CODE",nullable=true,length=32)
	public String getVendorCode(){
		return this.vendorCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  供应商编码
	 */
	public void setVendorCode(String vendorCode){
		this.vendorCode = vendorCode;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  供应商名称
	 */

	@Column(name ="VENDOR_NAME",nullable=true,length=32)
	public String getVendorName(){
		return this.vendorName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  供应商名称
	 */
	public void setVendorName(String vendorName){
		this.vendorName = vendorName;
	}

	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  采购订单日期
	 */

	@Column(name ="DOC_DATE",nullable=true,length=32)
	public Date getDocDate(){
		return this.docDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  采购订单日期
	 */
	public void setDocDate(Date docDate){
		this.docDate = docDate;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  采购订单备注
	 */

	@Column(name ="PO_REMARK",nullable=true,length=232)
	public String getPoRemark(){
		return this.poRemark;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  采购订单备注
	 */
	public void setPoRemark(String poRemark){
		this.poRemark = poRemark;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备用1
	 */
}
