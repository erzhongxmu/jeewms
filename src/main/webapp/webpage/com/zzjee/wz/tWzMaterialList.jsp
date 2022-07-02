<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="tWzMaterialList" checkbox="true" pagination="true" fitColumns="true" title="物料" actionUrl="tWzMaterialController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人名称"  field="createName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人登录名称"  field="createBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建日期"  field="createDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人名称"  field="updateName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人登录名称"  field="updateBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新日期"  field="updateDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属部门"  field="sysOrgCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属公司"  field="sysCompanyCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="流程状态"  field="bpmStatus"  hidden="true"  queryMode="single"  dictionary="bpm_status"  width="120"></t:dgCol>
     <t:dgCol title="物料编码"  field="matCode"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="物料名称"  field="matName"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="单位"  field="matUnit"  queryMode="single"  dictionary="ba_unit,UNIT_CODE,UNIT_ZH_NAME"  width="120"></t:dgCol>
   <t:dgCol title="规格"  field="matGuige"  queryMode="single"  width="80"></t:dgCol>
   <t:dgCol title="等级"  field="matClass"  queryMode="single"  width="80"></t:dgCol>
<%--   <t:dgCol title="标准价元"  field="matPrice"  queryMode="single"  width="80"></t:dgCol>--%>
   <t:dgCol title="安全库存"  field="matAqkc"  queryMode="single"  width="80"></t:dgCol>
   <t:dgCol title="库存地点"  field="matLocation"  queryMode="single"  dictionary="t_wz_location,mat_location,mat_location"  width="120"></t:dgCol>
   <t:dgCol title="物料条码"  field="by1"    queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="产品大类"  field="by2"     queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="产品小类"  field="by3"     queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="存储位置"  field="by4"    queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属设备"  field="by5"     queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="物资图片"  field="by6"    queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="库位"  field="by7"    queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="库区"  field="by8"    queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgDelOpt title="删除" url="tWzMaterialController.do?doDel&id={id}" urlclass="ace_button"  urlfont="fa-trash-o"/>
   <t:dgToolBar title="录入" icon="icon-add" url="tWzMaterialController.do?goAdd" funname="add"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="tWzMaterialController.do?goUpdate" funname="update"></t:dgToolBar>
   <%--<t:dgToolBar title="批量删除"  icon="icon-remove" url="tWzMaterialController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>--%>
   <t:dgToolBar title="查看" icon="icon-search" url="tWzMaterialController.do?goUpdate" funname="detail"></t:dgToolBar>
   <t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>
   <t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>
   <%--<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>--%>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/com/zzjee/wz/tWzMaterialList.js"></script>
 <script type="text/javascript">
 $(document).ready(function(){
 });



//导入
function ImportXls() {
	openwindow('Excel导入', 'tWzMaterialController.do?upload', "tWzMaterialList");
}

//导出
function ExportXls() {
	JeecgExcelExport("tWzMaterialController.do?exportXls","tWzMaterialList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("tWzMaterialController.do?exportXlsByT","tWzMaterialList");
}

 </script>
