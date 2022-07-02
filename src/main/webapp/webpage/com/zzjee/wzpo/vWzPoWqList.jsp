<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<t:base type="jquery,easyui,tools,DatePicker"></t:base>
<div class="easyui-layout" fit="true">
  <div region="center" style="padding:0px;border:0px">
  <t:datagrid name="vWzPoWqList" checkbox="true" pagination="true" fitColumns="true" title="批量采购入库" actionUrl="vWzPoWqController.do?datagrid" idField="id" fit="true" queryMode="group">
   <t:dgCol title="id"  field="id"  hidden="true"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="供应商编码"  field="vendorCode"  query="true"  queryMode="single"  dictionary="wz_pop_vendor,vendorCode,vendorName,vendor_code,vendor_name"  popup="true"  width="120"></t:dgCol>
   <t:dgCol title="供应商名称"  field="vendorName"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="采购订单日期"  field="docDate"  formatter="yyyy-MM-dd"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="采购订单备注"  field="poRemark"  query="true"  queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="物料编码"  field="matCode" query="true" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="物料名称"  field="matName"  query="true" queryMode="single"  width="120"></t:dgCol>
   <t:dgCol title="单位"  field="matUnit"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="仓库"  field="matLocation"   extendParams="editor:'text'" queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="批次"  field="matBatch"  extendParams="editor:'text'"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="入库数量"  field="matQtyWq"  extendParams="editor:'text'"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="未清数量"  field="matQtyWq1"     queryMode="group"  width="120"></t:dgCol>

   <t:dgCol title="标准价"  field="matPrice"  queryMode="group"  width="120"></t:dgCol>
   <t:dgCol title="订单数量"  field="matQty"  queryMode="group"  width="120"></t:dgCol>
   <%--<t:dgCol title="操作" field="opt" width="100"></t:dgCol>--%>

   <t:dgToolBar   title="开始入库" icon="icon-edit"  funname="editRow"></t:dgToolBar>
   <t:dgToolBar   title="入库保存" icon="icon-save" url="vWzPoWqController.do?saveRows" funname="saveData"></t:dgToolBar>
   <t:dgToolBar   title="取消编辑" icon="icon-undo" funname="reject"></t:dgToolBar>

   <%--<t:dgDelOpt title="删除" url="vWzPoWqController.do?doDel&id={id}" urlclass="ace_button"  urlfont="fa-trash-o"/>--%>
   <%--<t:dgToolBar title="录入" icon="icon-add" url="vWzPoWqController.do?goAdd" funname="add"></t:dgToolBar>--%>
   <%--<t:dgToolBar title="编辑" icon="icon-edit" url="vWzPoWqController.do?goUpdate" funname="update"></t:dgToolBar>--%>
   <%--<t:dgToolBar title="批量删除"  icon="icon-remove" url="vWzPoWqController.do?doBatchDel" funname="deleteALLSelect"></t:dgToolBar>--%>
   <%--<t:dgToolBar title="查看" icon="icon-search" url="vWzPoWqController.do?goUpdate" funname="detail"></t:dgToolBar>--%>
   <%--<t:dgToolBar title="导入" icon="icon-put" funname="ImportXls"></t:dgToolBar>--%>
   <%--<t:dgToolBar title="导出" icon="icon-putout" funname="ExportXls"></t:dgToolBar>--%>
   <%--<t:dgToolBar title="模板下载" icon="icon-putout" funname="ExportXlsByT"></t:dgToolBar>--%>
  </t:datagrid>
  </div>
 </div>
 <script src = "webpage/com/zzjee/wzpo/vWzPoWqList.js"></script>
 <script type="text/javascript">
 $(document).ready(function(){
 });

 //添加行
 function addRow(title,addurl,gname){
     $('#'+gname).datagrid('appendRow',{});
     var editIndex = $('#'+gname).datagrid('getRows').length-1;
     $('#'+gname).datagrid('selectRow', editIndex)
         .datagrid('beginEdit', editIndex);
 }
 //保存数据
 function saveData(title,addurl,gname){
     if(!endEdit(gname))
         return false;
     var rows=$('#'+gname).datagrid("getChanges","inserted");
     var uprows=$('#'+gname).datagrid("getChanges","updated");
     rows=rows.concat(uprows);
     if(rows.length<=0){
         tip("没有需要保存的数据！")
         return false;
     }
     var result={};
     for(var i=0;i<rows.length;i++){
         for(var d in rows[i]){
             result["demos["+i+"]."+d]=rows[i][d];
         }
     }
     $.ajax({
         url:"<%=basePath%>/"+addurl,
         type:"post",
         data:result,
         dataType:"json",
         success:function(data){
             tip(data.msg);
             if(data.success){
                 $('#vWzPoWqList').datagrid('reload',{});
             }
         }
     })
 }
 //结束编辑
 function endEdit(gname){
     var  editIndex = $('#'+gname).datagrid('getRows').length-1;
     for(var i=0;i<=editIndex;i++){
         if($('#'+gname).datagrid('validateRow', i)){
             $('#'+gname).datagrid('endEdit', i);
         }else{

             tip("请选择必填项(带有红色三角形状的字段)!");

             return false;
         }
     }
     return true;
 }
 //编辑行
 function editRow(title,addurl,gname){
     var rows=$('#'+gname).datagrid("getChecked");
     if(rows.length==0){
         tip("请选择条目");
         return false;
     }
     for(var i=0;i<rows.length;i++){
         var index= $('#'+gname).datagrid('getRowIndex', rows[i]);
         $('#'+gname).datagrid('beginEdit', index);
     }
 }

 //取消编辑
 function reject(title,addurl,gname){
     $('#'+gname).datagrid('clearChecked');
     $('#'+gname).datagrid('rejectChanges');


 }

//导入
function ImportXls() {
 openwindow('Excel导入', 'vWzPoWqController.do?upload', "vWzPoWqList");
}

//导出
function ExportXls() {
	JeecgExcelExport("vWzPoWqController.do?exportXls","vWzPoWqList");
}

//模板下载
function ExportXlsByT() {
	JeecgExcelExport("vWzPoWqController.do?exportXlsByT","vWzPoWqList");
}

 </script>
