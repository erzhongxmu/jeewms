<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="tWzPoHeadList" checkbox="true"  sortOrder="desc" sortName="id"  fitColumns="true" title="物料采购订单" actionUrl="tWzPoHeadController.do?datagridsp" idField="id" fit="true" queryMode="group">
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>

   <t:dgCol title="主键"  field="id"    queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人名称"  field="createName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人登录名称"  field="createBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建日期"  field="createDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人名称"  field="updateName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人登录名称"  field="updateBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新日期"  field="updateDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属部门"  field="sysOrgCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属公司"  field="sysCompanyCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <%--<t:dgCol title="流程状态"  field="bpmStatus" hidden="true"    queryMode="single"  dictionary="bpm_status"  width="120"></t:dgCol>--%>
   <t:dgCol title="供应商编码"  field="vendorCode"  query="true"    width="120"></t:dgCol>
   <t:dgCol title="供应商名称"  field="vendorName"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="采购订单日期"  field="docDate"  formatter="yyyy-MM-dd"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="采购订单备注"  field="poRemark"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="备用1"  field="poBy1"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="备用2"  field="poBy2"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="备用3"  field="poBy3"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="备用4"  field="poBy4"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgFunOpt title="审批" funname="dosp(id)"  urlclass="ace_button"      />
   <%--<t:dgToolBar title="下单" icon="icon-add" url="tWzPoHeadController.do?goAdd" funname="add" width="100%" height="100%"></t:dgToolBar>--%>
   <%--<t:dgToolBar title="编辑" icon="icon-edit" url="tWzPoHeadController.do?goUpdate" funname="update" width="100%" height="100%"></t:dgToolBar>--%>
   <%--<t:dgToolBar title="批量删除"  icon="icon-remove" url="tWzPoHeadController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>--%>
   <t:dgToolBar title="查看" icon="icon-search" url="tWzPoHeadController.do?goUpdate" funname="detail" width="100%" height="100%"></t:dgToolBar>
   <%--<t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>--%>
   <%--<t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>--%>
   <%--<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>--%>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/com/zzjee/wzyw/tWzPoHeadList.js"></script>
 <script type="text/javascript">
     function dosp(id) {
         var url = "tWzPoHeadController.do?doUpdatesp&id="+id;

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
                     $('#tWzPoHeadList').datagrid('reload',{});
                 }
             }
         });
     }
//导入
function ImportXls() {
 openwindow('Excel导入', 'tWzPoHeadController.do?upload', "tWzPoHeadList");
}

//导出
function ExportXls() {
	JeecgExcelExport("tWzPoHeadController.do?exportXls","tWzPoHeadList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("tWzPoHeadController.do?exportXlsByT","tWzPoHeadList");
}
 </script>
