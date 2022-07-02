<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="tWzRepairList" checkbox="false" pagination="true" fitColumns="false" title="交旧领新登记表" actionUrl="tWzRepairController.do?datagrid" idField="id" fit="true" queryMode="group">
    <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="创建人名称"  field="createName"    queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="创建人登录名称"  field="createBy"    queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="创建日期"  field="createDate" formatter="yyyy-MM-dd"   queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="更新人名称"  field="updateName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="更新人登录名称"  field="updateBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="更新日期"  field="updateDate" formatter="yyyy-MM-dd" hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="所属部门"  field="sysOrgCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="所属公司"  field="sysCompanyCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="流程状态"  field="bpmStatus"  hidden="true"  queryMode="single" dictionary="bpm_status" width="120"></t:dgCol>
      <t:dgCol title="操作" field="opt" width="100"></t:dgCol>

      <t:dgCol title="物料编码"  field="matCode"   query="true" queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="物料名称"  field="matName"   query="true" queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="库存地点"  field="matLocation"   query="true" queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="数量"  field="matQty"    queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="规格"  field="matGuige"    queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="单位"  field="matUnit"    queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="原价值"  field="matOriamount"    queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="现价值"  field="matNowamount"    queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="修复时间"  field="repairDate"    queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="修复人"  field="repairUser"    queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="验收人"  field="qmUser"    queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="修复备注"  field="repairRemark"    queryMode="single"  width="120"></t:dgCol>
    <t:dgCol title="出库单号"  field="ckId"    queryMode="single"  width="120"></t:dgCol>
    <%--<t:dgCol title="出库项目"  field="ckItmeId"    queryMode="single"  width="120"></t:dgCol>--%>
   <t:dgDelOpt title="删除" url="tWzRepairController.do?doDel&id={id}" urlclass="ace_button"  urlfont="fa-trash-o"/>
   <t:dgToolBar title="手工新增" icon="icon-add" url="tWzRepairController.do?goAdd" funname="add"></t:dgToolBar>
   <t:dgToolBar title="修复" icon="icon-edit" url="tWzRepairController.do?goUpdate" funname="update"></t:dgToolBar>
   <%--<t:dgToolBar title="批量删除"  icon="icon-remove" url="tWzRepairController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>--%>
   <t:dgToolBar title="查看" icon="icon-search" url="tWzRepairController.do?goUpdate" funname="detail"></t:dgToolBar>
   <%--<t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>--%>
   <%--<t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>--%>
   <%--<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>--%>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/com/zzjee/wzrepair/tWzRepairList.js"></script>
 <script type="text/javascript">
 $(document).ready(function(){
 });



//导入
function ImportXls() {
	openwindow('Excel导入', 'tWzRepairController.do?upload', "tWzRepairList");
}

//导出
function ExportXls() {
	JeecgExcelExport("tWzRepairController.do?exportXls","tWzRepairList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("tWzRepairController.do?exportXlsByT","tWzRepairList");
}

 </script>
