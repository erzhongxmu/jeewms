
package com.zzjee.wzyw.page;

import com.zzjee.wzyw.entity.TWzPoItemEntity;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Title: Entity
 * @Description: 物料采购订单
 * @author onlineGenerator
 * @date 2018-05-21 13:23:13
 * @version V1.0
 *
 */
public class TWzPoHeadPage implements java.io.Serializable {
	/**主键*/
	@Excel(name="采购单号")
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
    @Excel(name="供应商编码")
	private String vendorCode;
	/**供应商名称*/
    @Excel(name="供应商名称")
	private String vendorName;
	/**采购订单日期*/
    @Excel(name="采购订单日期",format = "yyyy-MM-dd")
	private Date docDate;
	/**采购订单备注*/
    @Excel(name="采购订单备注")
	private String poRemark;
	/**备用1*/
	private String poBy1;
	/**备用2*/
	private String poBy2;
	/**备用3*/
	private String poBy3;
	/**备用4*/
	private String poBy4;

	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  主键
	 */
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
	public String getPoBy1(){
		return this.poBy1;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备用1
	 */
	public void setPoBy1(String poBy1){
		this.poBy1 = poBy1;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备用2
	 */
	public String getPoBy2(){
		return this.poBy2;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备用2
	 */
	public void setPoBy2(String poBy2){
		this.poBy2 = poBy2;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备用3
	 */
	public String getPoBy3(){
		return this.poBy3;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备用3
	 */
	public void setPoBy3(String poBy3){
		this.poBy3 = poBy3;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备用4
	 */
	public String getPoBy4(){
		return this.poBy4;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备用4
	 */
	public void setPoBy4(String poBy4){
		this.poBy4 = poBy4;
	}

	/**保存-采购订单行项目*/
    @ExcelCollection(name="采购订单行项目")
	private List<TWzPoItemEntity> tWzPoItemList = new ArrayList<TWzPoItemEntity>();
		public List<TWzPoItemEntity> getTWzPoItemList() {
		return tWzPoItemList;
		}
		public void setTWzPoItemList(List<TWzPoItemEntity> tWzPoItemList) {
		this.tWzPoItemList = tWzPoItemList;
		}
}
