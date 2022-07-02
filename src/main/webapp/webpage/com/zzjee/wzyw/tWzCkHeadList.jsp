<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="tWzCkHeadList" checkbox="true" sortOrder="desc" sortName="id"  fitColumns="true" title="物料出库" actionUrl="tWzCkHeadController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="主键"  field="id"   queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人名称"  field="createName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建人登录名称"  field="createBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="创建日期"  field="createDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人名称"  field="updateName"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新人登录名称"  field="updateBy"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="更新日期"  field="updateDate"  formatter="yyyy-MM-dd"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属部门"  field="sysOrgCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="所属公司"  field="sysCompanyCode"  hidden="true"  queryMode="single"  width="120"></t:dgCol>


   <t:dgCol title="流程状态"  field="bpmStatus" hidden="true"  queryMode="single"  dictionary="bpm_status"  width="120"></t:dgCol>
   <t:dgCol title="领用部门"  field="orgCode"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="领用部门名称"  field="orgName"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="领用人"  field="ckUsername"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="领用人名字"  field="ckName"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="单据日期"  field="docDate"  formatter="yyyy-MM-dd"  query="true"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="整单价值"  field="ckValue"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="出库备注"  field="ckRemark"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="手工审批"  field="ckSgsp"  queryMode="single"  dictionary="sf_yn"  width="120"></t:dgCol>
   <%--<t:dgCol title="附件"  field="ckFujian"  queryMode="single"  width="120"></t:dgCol>--%>
   <t:dgCol title="备用1"  field="by1"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="备用2"  field="by2"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="备用3"  field="by3"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="备用4"  field="by4"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="备用5"  field="by5"  hidden="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="操作" field="opt" width="100"></t:dgCol>

   <t:dgFunOpt title="打印" funname="doprint(id)"  exp="bpmStatus#eq#1"  urlclass="ace_button"/>
   <%--<t:dgFunOpt title="审批" funname="doUpdatesp(id)" exp="bpmStatus#eq#0"  urlclass="ace_button" operationCode="wzcksp"/>--%>

   <%--<t:dgDelOpt title="删除" url="tWzCkHeadController.do?doDel&id={id}"  urlclass="ace_button" urlfont="fa-trash-o"/>--%>
   <t:dgToolBar title="出库" icon="icon-add" url="tWzCkHeadController.do?goAdd" funname="add" width="100%" height="100%"></t:dgToolBar>
   <%--<t:dgToolBar title="编辑" icon="icon-edit" url="tWzCkHeadController.do?goUpdate" funname="update" width="100%" height="100%"></t:dgToolBar>--%>
   <%--<t:dgToolBar title="批量删除"  icon="icon-remove" url="tWzCkHeadController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>--%>
   <t:dgToolBar title="查看" icon="icon-search" url="tWzCkHeadController.do?goUpdate" funname="detail" width="100%" height="100%"></t:dgToolBar>
   <%--<t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>--%>
   <%--<t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>--%>
   <%--<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>--%>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/com/zzjee/wzyw/tWzCkHeadList.js"></script>
 <script type="text/javascript">

     function doUpdatesp(id) {
         var url = "tWzCkHeadController.do?doUpdatesp&id="+id;

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
                     $('#tWzCkHeadList').datagrid('reload',{});
                 }
             }
         });
     }

     function doprint(id){
         var url = "tWzCkHeadController.do?doPrint&id="+id;

         window.open(url);
     }
//导入
function ImportXls() {
 openwindow('Excel导入', 'tWzCkHeadController.do?upload', "tWzCkHeadList");
}

//导出
function ExportXls() {
	JeecgExcelExport("tWzCkHeadController.do?exportXls","tWzCkHeadList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("tWzCkHeadController.do?exportXlsByT","tWzCkHeadList");
}
 </script>
