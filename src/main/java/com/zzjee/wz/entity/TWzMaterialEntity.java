package com.zzjee.wz.entity;

import org.jeecgframework.poi.excel.annotation.Excel;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 物料
 * @author onlineGenerator
 * @date 2018-05-20 21:40:03
 * @version V1.0
 *
 */
@Entity
@Table(name = "t_wz_material", schema = "")
@SuppressWarnings("serial")
public class TWzMaterialEntity implements java.io.Serializable {
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
	/**物料编码*/
	@Excel(name="物料编码",width=15)
	private String matCode;
	/**物料名称*/
	@Excel(name="物料名称",width=15)
	private String matName;
	/**单位*/
	@Excel(name="单位",width=15,dictTable ="ba_unit",dicCode ="UNIT_CODE",dicText ="UNIT_ZH_NAME")
	private String matUnit;
	/**规格*/
	@Excel(name="规格",width=15)
	private String matGuige;
	/**等级*/
	@Excel(name="等级",width=15)
	private String matClass;
	/**标准价元*/
	@Excel(name="标准价元",width=15)
	private BigDecimal matPrice;
	/**安全库存*/
	@Excel(name="安全库存",width=15)
	private Integer matAqkc;
	/**仓库*/
	@Excel(name="仓库",width=15,dictTable ="t_wz_location",dicCode ="mat_location",dicText ="mat_location")
	private String matLocation;
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
	private String by6;
	private String by7;
	private String by8;
	private String by9;

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

	@Column(name ="MAT_NAME",nullable=true,length=132)
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
	 *@return: java.lang.String  等级
	 */

	@Column(name ="MAT_CLASS",nullable=true,length=32)
	public String getMatClass(){
		return this.matClass;
	}

	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  等级
	 */
	public void setMatClass(String matClass){
		this.matClass = matClass;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  标准价元
	 */

	@Column(name ="MAT_PRICE",nullable=true,length=32)
	public BigDecimal getMatPrice(){
		return this.matPrice;
	}

	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  标准价元
	 */
	public void setMatPrice(BigDecimal matPrice){
		this.matPrice = matPrice;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  安全库存
	 */

	@Column(name ="MAT_AQKC",nullable=true,length=32)
	public Integer getMatAqkc(){
		return this.matAqkc;
	}

	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  安全库存
	 */
	public void setMatAqkc(Integer matAqkc){
		this.matAqkc = matAqkc;
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

	@Column(name ="BY6",nullable=true,length=32)
	public String getBy6() {
		return by6;
	}

	public void setBy6(String by6) {
		this.by6 = by6;
	}

	@Column(name ="BY7",nullable=true,length=32)
	public String getBy7() {
		return by7;
	}

	public void setBy7(String by7) {
		this.by7 = by7;
	}

	@Column(name ="BY8",nullable=true,length=32)
	public String getBy8() {
		return by8;
	}

	public void setBy8(String by8) {
		this.by8 = by8;
	}

	@Column(name ="BY9",nullable=true,length=32)
	public String getBy9() {
		return by9;
	}

	public void setBy9(String by9) {
		this.by9 = by9;
	}
}
