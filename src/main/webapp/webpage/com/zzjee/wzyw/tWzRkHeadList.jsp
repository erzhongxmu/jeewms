<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="tWzRkHeadList"  checkbox="true" sortOrder="desc" sortName="id" fitColumns="true" title="入库抬头" actionUrl="tWzRkHeadController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"    queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人名称"  field="createName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人登录名称"  field="createBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建日期"  field="createDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人名称"  field="updateName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人登录名称"  field="updateBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新日期"  field="updateDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属部门"  field="sysOrgCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属公司"  field="sysCompanyCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <%--<t:dgCol title="流程状态"  field="bpmStatus"  hidden="true"  queryMode="single"  dictionary="bpm_status"  width="120"></t:dgCol>--%>
   <t:dgCol title="供应商编码"  field="vendorCode"  query="true"  queryMode="single"  dictionary="wz_pop_vendor,vendorCode,vendorName,vendor_code,vendor_name"  popup="true"  width="120"></t:dgCol>
   <t:dgCol title="供应商名称"  field="vendorName"  query="true"  queryMode="single"  width="120"></t:dgCol>

   <t:dgCol title="入库日期"  field="docDate"  formatter="yyyy-MM-dd"  query="true"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="入库备注"  field="rkRemark"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <%--<t:dgCol title="附件"  field="rkFujian"  queryMode="single"  width="120"></t:dgCol>--%>
   <t:dgCol title="备用1"  field="by1"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="备用2"  field="by2"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="备用3"  field="by3"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="备用4"  field="by4"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="备用5"  field="by5"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>
   <t:dgFunOpt title="打印"   funname="doprint(id)"  urlclass="ace_button" />

   <%--<t:dgFunOpt title="审批" funname="doUpdatesp(id)" exp="bpmStatus#eq#0"  urlclass="ace_button" operationCode="updatesp"/>--%>
   <%--<t:dgCol title="操作" field="opt" width="100"></t:dgCol>--%>
   <%--<t:dgDelOpt title="删除" url="tWzRkHeadController.do?doDel&id={id}"  urlclass="ace_button" urlfont="fa-trash-o"/>--%>
   <t:dgToolBar title="入库" icon="icon-add" url="tWzRkHeadController.do?goAdd" funname="add" width="100%" height="100%"></t:dgToolBar>
   <t:dgToolBar title="编辑" icon="icon-edit" url="tWzRkHeadController.do?goUpdate" funname="update" width="100%" height="100%"></t:dgToolBar>
   <%--<t:dgToolBar title="批量删除"  icon="icon-remove" url="tWzRkHeadController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>--%>
   <t:dgToolBar title="查看" icon="icon-search" url="tWzRkHeadController.do?goUpdate" funname="detail" width="100%" height="100%"></t:dgToolBar>
   <%--<t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>--%>
   <%--<t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>--%>
   <%--<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>--%>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/com/zzjee/wzyw/tWzRkHeadList.js"></script>
 <script type="text/javascript">
     function doUpdatesp(id) {
         var url = "tWzRkHeadController.do?doUpdatesp&id="+id;

         $.ajax({
             async : false,
             cache : false,
             type : 'POST',
             url : url,// 请求的action路径
             error : function() {// 请求失败处理函数
             },
             success : function(data) {
                 var d = $.parseJSON(data);
                 if (d.success) {
                     tip("成功");
                     $('#tWzRkHeadList').datagrid('reload',{});
                 }
             }
         });
     }
     function doprint(id){
         var url = "tWzRkHeadController.do?doPrint&id="+id;

         window.open(url);
     }
//导入
function ImportXls() {
 openwindow('Excel导入', 'tWzRkHeadController.do?upload', "tWzRkHeadList");
}

//导出
function ExportXls() {
	JeecgExcelExport("tWzRkHeadController.do?exportXls","tWzRkHeadList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("tWzRkHeadController.do?exportXlsByT","tWzRkHeadList");
}
 </script>
