package com.zzjee.wzyw.entity;

import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 入库抬头
 * @author onlineGenerator
 * @date 2018-05-20 21:43:08
 * @version V1.0
 *
 */
@Entity
@Table(name = "t_wz_rk_head", schema = "")
@SuppressWarnings("serial")
public class TWzRkHeadEntity implements java.io.Serializable {
	/**主键*/
	private Integer id;
	/**创建人名称*/
	private String createName;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人名称*/
	private String updateName;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**所属部门*/
	private String sysOrgCode;
	/**所属公司*/
	private String sysCompanyCode;
	/**流程状态*/
	private String bpmStatus;
	/**供应商编码*/
    @Excel(name="供应商编码",width=15)
	private String vendorCode;
	/**供应商名称*/
    @Excel(name="供应商名称",width=15)
	private String vendorName;
	/**入库日期*/
    @Excel(name="入库日期",width=15,format = "yyyy-MM-dd")
	private Date docDate;
	/**入库备注*/
    @Excel(name="入库备注",width=15)
	private String rkRemark;
	/**附件*/
    @Excel(name="附件",width=15)
	private String rkFujian;
	/**备用1*/
	private String by1;
	/**备用2*/
	private String by2;
	/**备用3*/
	private String by3;
	/**备用4*/
	private String by4;
	/**备用5*/
	private String by5;

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	@Column(name ="ID",nullable=false,length=20)
	public Integer getId(){
		return this.id;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  主键
	 */
	public void setId(Integer id){
		this.id = id;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人名称
	 */

	@Column(name ="CREATE_NAME",nullable=true,length=50)
	public String getCreateName(){
		return this.createName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人名称
	 */
	public void setCreateName(String createName){
		this.createName = createName;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */

	@Column(name ="CREATE_BY",nullable=true,length=50)
	public String getCreateBy(){
		return this.createBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}

	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */

	@Column(name ="CREATE_DATE",nullable=true,length=20)
	public Date getCreateDate(){
		return this.createDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人名称
	 */

	@Column(name ="UPDATE_NAME",nullable=true,length=50)
	public String getUpdateName(){
		return this.updateName;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人名称
	 */
	public void setUpdateName(String updateName){
		this.updateName = updateName;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */

	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public String getUpdateBy(){
		return this.updateBy;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}

	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */

	@Column(name ="UPDATE_DATE",nullable=true,length=20)
	public Date getUpdateDate(){
		return this.updateDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(Date updateDate){
		this.updateDate = updateDate;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属部门
	 */

	@Column(name ="SYS_ORG_CODE",nullable=true,length=50)
	public String getSysOrgCode(){
		return this.sysOrgCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属部门
	 */
	public void setSysOrgCode(String sysOrgCode){
		this.sysOrgCode = sysOrgCode;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属公司
	 */

	@Column(name ="SYS_COMPANY_CODE",nullable=true,length=50)
	public String getSysCompanyCode(){
		return this.sysCompanyCode;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属公司
	 */
	public void setSysCompanyCode(String sysCompanyCode){
		this.sysCompanyCode = sysCompanyCode;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  流程状态
	 */

	@Column(name ="BPM_STATUS",nullable=true,length=32)
	public String getBpmStatus(){
		return this.bpmStatus;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  流程状态
	 */
	public void setBpmStatus(String bpmStatus){
		this.bpmStatus = bpmStatus;
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
	 *@return: java.util.Date  入库日期
	 */

	@Column(name ="DOC_DATE",nullable=true,length=32)
	public Date getDocDate(){
		return this.docDate;
	}

	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  入库日期
	 */
	public void setDocDate(Date docDate){
		this.docDate = docDate;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  入库备注
	 */

	@Column(name ="RK_REMARK",nullable=true,length=32)
	public String getRkRemark(){
		return this.rkRemark;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  入库备注
	 */
	public void setRkRemark(String rkRemark){
		this.rkRemark = rkRemark;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  附件
	 */

	@Column(name ="RK_FUJIAN",nullable=true,length=32)
	public String getRkFujian(){
		return this.rkFujian;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  附件
	 */
	public void setRkFujian(String rkFujian){
		this.rkFujian = rkFujian;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备用1
	 */

	@Column(name ="BY1",nullable=true,length=32)
	public String getBy1(){
		return this.by1;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备用1
	 */
	public void setBy1(String by1){
		this.by1 = by1;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备用2
	 */

	@Column(name ="BY2",nullable=true,length=32)
	public String getBy2(){
		return this.by2;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备用2
	 */
	public void setBy2(String by2){
		this.by2 = by2;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备用3
	 */

	@Column(name ="BY3",nullable=true,length=32)
	public String getBy3(){
		return this.by3;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备用3
	 */
	public void setBy3(String by3){
		this.by3 = by3;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备用4
	 */

	@Column(name ="BY4",nullable=true,length=32)
	public String getBy4(){
		return this.by4;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备用4
	 */
	public void setBy4(String by4){
		this.by4 = by4;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备用5
	 */

	@Column(name ="BY5",nullable=true,length=32)
	public String getBy5(){
		return this.by5;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备用5
	 */
	public void setBy5(String by5){
		this.by5 = by5;
	}

}
