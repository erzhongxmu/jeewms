package com.zzjee.wzrepair.entity;

import org.hibernate.annotations.GenericGenerator;
import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 交旧领新登记表
 * @author onlineGenerator
 * @date 2020-02-24 11:36:29
 * @version V1.0
 *
 */
@Entity
@Table(name = "t_wz_repair", schema = "")
@SuppressWarnings("serial")
public class TWzRepairEntity implements java.io.Serializable {
	/**主键*/
	private String id;
	/**创建人名称*/
	@Excel(name="创建人名称")
	private String createName;
	/**创建人登录名称*/
	@Excel(name="创建人登录名称")
	private String createBy;
	/**创建日期*/
	@Excel(name="创建日期",format = "yyyy-MM-dd")
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
	/**物料编码*/
	@Excel(name="物料编码")
	private String matCode;
	/**物料名称*/
	@Excel(name="物料名称")
	private String matName;
	/**库存地点*/
	@Excel(name="库存地点")
	private String matLocation;
	/**数量*/
	@Excel(name="数量")
	private String matQty;
	/**规格*/
	@Excel(name="规格")
	private String matGuige;
	/**单位*/
	@Excel(name="单位")
	private String matUnit;
	/**原价值*/
	@Excel(name="原价值")
	private String matOriamount;
	/**现价值*/
	@Excel(name="现价值")
	private String matNowamount;
	/**修复时间*/
	@Excel(name="修复时间")
	private String repairDate;
	/**修复人*/
	@Excel(name="修复人")
	private String repairUser;
	/**验收人*/
	@Excel(name="验收人")
	private String qmUser;
	/**修复备注*/
	@Excel(name="修复备注")
	private String repairRemark;
	/**出库单号*/
	@Excel(name="出库单号")
	private String ckId;
	/**出库项目*/
	@Excel(name="出库项目")
	private String ckItmeId;

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键
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
	 *@param: java.lang.String  主键
	 */
	public void setId(String id){
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
	@Column(name ="MAT_NAME",nullable=true,length=64)
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
	 *@return: java.lang.String  库存地点
	 */
	@Column(name ="MAT_LOCATION",nullable=true,length=32)
	public String getMatLocation(){
		return this.matLocation;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  库存地点
	 */
	public void setMatLocation(String matLocation){
		this.matLocation = matLocation;
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  规格
	 */
	@Column(name ="MAT_GUIGE",nullable=true,length=32)
	public String getMatGuige(){
		return this.matGuige;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  规格
	 */
	public void setMatGuige(String matGuige){
		this.matGuige = matGuige;
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
	 *@return: java.lang.String  原价值
	 */
	@Column(name ="MAT_ORIAMOUNT",nullable=true,length=32)
	public String getMatOriamount(){
		return this.matOriamount;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原价值
	 */
	public void setMatOriamount(String matOriamount){
		this.matOriamount = matOriamount;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  现价值
	 */
	@Column(name ="MAT_NOWAMOUNT",nullable=true,length=32)
	public String getMatNowamount(){
		return this.matNowamount;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  现价值
	 */
	public void setMatNowamount(String matNowamount){
		this.matNowamount = matNowamount;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  修复时间
	 */
	@Column(name ="REPAIR_DATE",nullable=true,length=32)
	public String getRepairDate(){
		return this.repairDate;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  修复时间
	 */
	public void setRepairDate(String repairDate){
		this.repairDate = repairDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  修复人
	 */
	@Column(name ="REPAIR_USER",nullable=true,length=32)
	public String getRepairUser(){
		return this.repairUser;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  修复人
	 */
	public void setRepairUser(String repairUser){
		this.repairUser = repairUser;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  验收人
	 */
	@Column(name ="QM_USER",nullable=true,length=32)
	public String getQmUser(){
		return this.qmUser;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  验收人
	 */
	public void setQmUser(String qmUser){
		this.qmUser = qmUser;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  修复备注
	 */
	@Column(name ="REPAIR_REMARK",nullable=true,length=320)
	public String getRepairRemark(){
		return this.repairRemark;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  修复备注
	 */
	public void setRepairRemark(String repairRemark){
		this.repairRemark = repairRemark;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  出库单号
	 */
	@Column(name ="CK_ID",nullable=true,length=32)
	public String getCkId(){
		return this.ckId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  出库单号
	 */
	public void setCkId(String ckId){
		this.ckId = ckId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  出库项目
	 */
	@Column(name ="CK_ITME_ID",nullable=true,length=32)
	public String getCkItmeId(){
		return this.ckItmeId;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  出库项目
	 */
	public void setCkItmeId(String ckItmeId){
		this.ckItmeId = ckItmeId;
	}
}
