<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>物料出库</title>
    <style>
  .ui-button {
  	  display: inline-block;
	  padding: 2px 2px;
	  margin-bottom: 0;
	  font-size: 8px;
	  font-weight: normal;
	  line-height: 1.42857143;
	  text-align: center;
	  white-space: nowrap;
	  vertical-align: middle;
	  -ms-touch-action: manipulation;
      touch-action: manipulation;
	  cursor: pointer;
	  -webkit-user-select: none;
      -moz-user-select: none;
      -ms-user-select: none;
      user-select: none;
	  background-image: none;
	  border: 1px solid transparent;
	  border-radius: 4px;
  }
  </style>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  $(document).ready(function(){
	$('#tt').tabs({
	   onSelect:function(title){
	       $('#tt .panel-body').css('width','auto');
		}
	});
	$(".tabs-wrap").css('width','100%');
  });
 </script>
 </head>
 <body style="overflow-x: hidden;">
 <%--<c:if test="${tWzCkHeadPage.bpmStatus eq 0}">--%>
	 <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" tiptype="1" action="tWzCkHeadController.do?doUpdate" >
		 <input id="id" name="id" type="hidden" value="${tWzCkHeadPage.id }"/>
		 <table cellpadding="0" cellspacing="1" class="formtable">
			 <tr>
				 <td align="right">
					 <label class="Validform_label">领用部门:</label>
				 </td>
				 <td class="value">
					 <input id="orgCode" name="orgCode" type="text" style="width: 150px" class="inputxt"  ignore="ignore"  value='${tWzCkHeadPage.orgCode}'/>
					 <span class="Validform_checktip"></span>
					 <label class="Validform_label" style="display: none;">领用部门</label>
				 </td>
				 <td align="right">
					 <label class="Validform_label">领用部门名称:</label>
				 </td>
				 <td class="value">
					 <input id="orgName" name="orgName" type="text" style="width: 150px" class="inputxt"  ignore="ignore"  value='${tWzCkHeadPage.orgName}'/>
					 <span class="Validform_checktip"></span>
					 <label class="Validform_label" style="display: none;">领用部门名称</label>
				 </td>
			 </tr>
			 <tr>
				 <td align="right">
					 <label class="Validform_label">领用人:</label>
				 </td>
				 <td class="value">
					 <input id="ckUsername" name="ckUsername" type="text" style="width: 150px" class="inputxt"  ignore="ignore"  value='${tWzCkHeadPage.ckUsername}'/>
					 <span class="Validform_checktip"></span>
					 <label class="Validform_label" style="display: none;">领用人</label>
				 </td>
				 <td align="right">
					 <label class="Validform_label">领用人名字:</label>
				 </td>
				 <td class="value">
					 <input id="ckName" name="ckName" type="text" style="width: 150px" class="inputxt"  ignore="ignore"  value='${tWzCkHeadPage.ckName}'/>
					 <span class="Validform_checktip"></span>
					 <label class="Validform_label" style="display: none;">领用人名字</label>
				 </td>
			 </tr>
			 <tr>
				 <td align="right">
					 <label class="Validform_label">单据日期:</label>
				 </td>
				 <td class="value">
					 <input id="docDate" name="docDate" type="text" style="width: 150px"   ignore="ignore"  onClick="WdatePicker()"  value='${tWzCkHeadPage.docDate}' />
					 <span class="Validform_checktip"></span>
					 <label class="Validform_label" style="display: none;">单据日期</label>
				 </td>
				 <td align="right">
					 <label class="Validform_label">出库备注:</label>
				 </td>
				 <td class="value">
					 <input id="ckRemark" name="ckRemark" type="text" style="width: 150px" class="inputxt"  ignore="ignore"  value='${tWzCkHeadPage.ckRemark}'/>
					 <span class="Validform_checktip"></span>
					 <label class="Validform_label" style="display: none;">出库备注</label>
				 </td>
			 </tr>
			 <tr>
				 <td align="right">
					 <label class="Validform_label">手工审批:</label>
				 </td>
				 <td class="value">
					 <t:dictSelect field="ckSgsp" type="radio"   typeGroupCode="sf_yn"  defaultVal="${tWzCkHeadPage.ckSgsp}" hasLabel="false"  title="手工审批"></t:dictSelect>
					 <span class="Validform_checktip"></span>
				 </td>
					 <%--<td align="right">--%>
					 <%--<label class="Validform_label">附件:</label>--%>
					 <%--</td>--%>
					 <%--<td class="value">--%>
					 <%--<input id="ckFujian" name="ckFujian" type="text" style="width: 150px" class="inputxt"  ignore="ignore"  value='${tWzCkHeadPage.ckFujian}'/>--%>
					 <%--<span class="Validform_checktip"></span>--%>
					 <%--<label class="Validform_label" style="display: none;">附件</label>--%>
					 <%--</td>--%>

				 <td align="right">
					 <label class="Validform_label">出库类别:</label>
				 </td>
				 <td class="value">
					 <t:dictSelect field="by1" type="radio"   typeGroupCode="wz_cklb"    hasLabel="false"  title="出库类别"  defaultVal="${tWzCkHeadPage.by1}"></t:dictSelect>
					 <span class="Validform_checktip"></span>
				 </td>
			 </tr>
			 <tr>
				 <td align="right">
					 <label class="Validform_label">业务类型 :</label>
				 </td>
				 <td class="value">
					 <t:dictSelect field="by2" type="radio"   typeGroupCode="wz_ywlx"    hasLabel="false"  title="业务类型"   defaultVal="${tWzCkHeadPage.by2}" ></t:dictSelect>
					 <span class="Validform_checktip"></span>
				 </td>
				 <td align="right">
					 <label class="Validform_label">再利用程度 :</label>
				 </td>
				 <td>
				 <t:dictSelect field="by3" type="radio"   typeGroupCode="wz_zlycd"    hasLabel="false"  title="再利用程度"   defaultVal="${tWzCkHeadPage.by3}" ></t:dictSelect>
				 <span class="Validform_checktip"></span>
				 </td>
			 </tr>
		 </table>
		 <div style="width: auto;height: 200px;">
				 <%-- 增加一个div，用于调节页面大小，否则默认太小 --%>
			 <div style="width:800px;height:1px;"></div>
			 <t:tabs id="tt" iframe="false" tabPosition="top" fit="false">
				 <t:tab href="tWzCkHeadController.do?tWzCkItemList&id=${tWzCkHeadPage.id}" icon="icon-search" title="出库项目" id="tWzCkItem"></t:tab>
			 </t:tabs>
		 </div>
	 </t:formvalid>
	 <!-- 添加 附表明细 模版 -->
	 <table style="display:none">
		 <tbody id="add_tWzCkItem_table_template">
		 <tr>
			 <td align="center"><div style="width: 25px;" name="xh"></div></td>
			 <td align="center"><input style="width:20px;" type="checkbox" name="ck"/></td>
			 <td align="left">
				 <input name="tWzCkItemList[#index#].matCode" name="tWzCkItemList[#index#].matCode" type="text" style="width: 150px" class="searchbox-inputtext" 		datatype="*" ignore="ignore"    onclick="popupClick(this,'mat_code,mat_name,mat_unit,mat_location,mat_batch,mat_price','matCode,matName,matUnit,matLocation,matBatch,matPrice','wz_stock_pop')" value="${poVal.matCode }" />


				 <label class="Validform_label" style="display: none;">物料编码</label>
			 </td>
			 <td align="left">
				 <input name="tWzCkItemList[#index#].matName" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="ignore" />
				 <label class="Validform_label" style="display: none;">物料名称</label>
			 </td>
			 <td align="left">
				 <input name="tWzCkItemList[#index#].matQty" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="checked" />
				 <label class="Validform_label" style="display: none;">数量</label>
			 </td>
			 <td align="left">
				 <input name="tWzCkItemList[#index#].matUnit" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="ignore" />
				 <label class="Validform_label" style="display: none;">单位</label>
			 </td>
			 <td align="left">
				 <input name="tWzCkItemList[#index#].matLocation" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="ignore" />
				 <label class="Validform_label" style="display: none;">仓库</label>
			 </td>
			 <td align="left">
				 <input name="tWzCkItemList[#index#].matBatch" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
				 <label class="Validform_label" style="display: none;">批次</label>
			 </td>
			 <td align="left">
				 <input name="tWzCkItemList[#index#].matPrice" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
				 <label class="Validform_label" style="display: none;">单价</label>
			 </td>
			 <td align="left">
				 <input name="tWzCkItemList[#index#].itemRemark" maxlength="232" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
				 <label class="Validform_label" style="display: none;">备注</label>
			 </td>
			 <td align="left">
				 <input name="tWzCkItemList[#index#].matAmount" maxlength="232" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
				 <label class="Validform_label" style="display: none;">总价</label>
			 </td>
			 <td align="left">
				 <input name="tWzCkItemList[#index#].by2" maxlength="232" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
				 <label class="Validform_label" style="display: none;">备注3</label>
			 </td>
		 </tr>
		 </tbody>
	 </table>
 <%--</c:if>--%>
<%--<c:if test="${tWzCkHeadPage.bpmStatus eq 1}">--%>
	<%--<input id="id" name="id" type="hidden" value="${tWzCkHeadPage.id }"/>--%>
	<%--<table cellpadding="0" cellspacing="1" class="formtable">--%>
		<%--<tr>--%>
			<%--<td align="right">--%>
				<%--<label class="Validform_label">领用部门:</label>--%>
			<%--</td>--%>
			<%--<td class="value">--%>
				<%--<input  name="orgCode" type="text" style="width: 150px" readonly="readonly" class="inputxt"  ignore="ignore"  value='${tWzCkHeadPage.orgCode}'/>--%>
				<%--<span class="Validform_checktip"></span>--%>
				<%--<label class="Validform_label" style="display: none;">领用部门</label>--%>
			<%--</td>--%>
			<%--<td align="right">--%>
				<%--<label class="Validform_label">领用部门名称:</label>--%>
			<%--</td>--%>
			<%--<td class="value">--%>
				<%--<input  name="orgName" type="text" style="width: 150px" readonly="readonly" class="inputxt"  ignore="ignore"  value='${tWzCkHeadPage.orgName}'/>--%>
				<%--<span class="Validform_checktip"></span>--%>
				<%--<label class="Validform_label" style="display: none;">领用部门名称</label>--%>
			<%--</td>--%>
		<%--</tr>--%>
		<%--<tr>--%>
			<%--<td align="right">--%>
				<%--<label class="Validform_label">领用人:</label>--%>
			<%--</td>--%>
			<%--<td class="value">--%>
				<%--<input  name="ckUsername" type="text" style="width: 150px" class="inputxt" readonly="readonly" ignore="ignore"  value='${tWzCkHeadPage.ckUsername}'/>--%>
				<%--<span class="Validform_checktip"></span>--%>
				<%--<label class="Validform_label" style="display: none;">领用人</label>--%>
			<%--</td>--%>
			<%--<td align="right">--%>
				<%--<label class="Validform_label">领用人名字:</label>--%>
			<%--</td>--%>
			<%--<td class="value">--%>
				<%--<input name="ckName" type="text" style="width: 150px" class="inputxt"  ignore="ignore" readonly="readonly"  value='${tWzCkHeadPage.ckName}'/>--%>
				<%--<span class="Validform_checktip"></span>--%>
				<%--<label class="Validform_label" style="display: none;">领用人名字</label>--%>
			<%--</td>--%>
		<%--</tr>--%>
		<%--<tr>--%>
			<%--<td align="right">--%>
				<%--<label class="Validform_label">单据日期:</label>--%>
			<%--</td>--%>
			<%--<td class="value">--%>
				<%--<input  name="docDate" type="text" style="width: 150px"   readonly="readonly" ignore="ignore"    value='${tWzCkHeadPage.docDate}' />--%>
				<%--<span class="Validform_checktip"></span>--%>
				<%--<label class="Validform_label" style="display: none;">单据日期</label>--%>
			<%--</td>--%>
			<%--<td align="right">--%>
				<%--<label class="Validform_label">出库备注:</label>--%>
			<%--</td>--%>
			<%--<td class="value">--%>
				<%--<input name="ckRemark" type="text" style="width: 150px" class="inputxt" readonly="readonly" ignore="ignore"  value='${tWzCkHeadPage.ckRemark}'/>--%>
				<%--<span class="Validform_checktip"></span>--%>
				<%--<label class="Validform_label" style="display: none;">出库备注</label>--%>
			<%--</td>--%>
		<%--</tr>--%>
		<%--<tr>--%>
			<%--<td align="right">--%>
				<%--<label class="Validform_label">手工审批:</label>--%>
			<%--</td>--%>
			<%--<td class="value">--%>
				<%--<t:dictSelect field="ckSgsp" type="radio" readonly="readonly"  typeGroupCode="sf_yn"  defaultVal="${tWzCkHeadPage.ckSgsp}" hasLabel="false"  title="手工审批"></t:dictSelect>--%>
				<%--<span class="Validform_checktip"></span>--%>
				<%--<label class="Validform_label" style="display: none;">手工审批</label>--%>
			<%--</td>--%>
				<%--&lt;%&ndash;<td align="right">&ndash;%&gt;--%>
				<%--&lt;%&ndash;<label class="Validform_label">附件:</label>&ndash;%&gt;--%>
				<%--&lt;%&ndash;</td>&ndash;%&gt;--%>
				<%--&lt;%&ndash;<td class="value">&ndash;%&gt;--%>
				<%--&lt;%&ndash;<input id="ckFujian" name="ckFujian" type="text" style="width: 150px" class="inputxt"  ignore="ignore"  value='${tWzCkHeadPage.ckFujian}'/>&ndash;%&gt;--%>
				<%--&lt;%&ndash;<span class="Validform_checktip"></span>&ndash;%&gt;--%>
				<%--&lt;%&ndash;<label class="Validform_label" style="display: none;">附件</label>&ndash;%&gt;--%>
				<%--&lt;%&ndash;</td>&ndash;%&gt;--%>
		<%--</tr>--%>

	<%--</table>--%>
	<%--<div style="width: auto;height: 200px;">--%>
			<%--&lt;%&ndash; 增加一个div，用于调节页面大小，否则默认太小 &ndash;%&gt;--%>
		<%--<div style="width:800px;height:1px;"></div>--%>
		<%--<t:tabs id="tt" iframe="false" tabPosition="top" fit="false">--%>
			<%--<t:tab href="tWzCkHeadController.do?tWzCkItemListUnEdit&id=${tWzCkHeadPage.id}" icon="icon-search" title="出库商品" id="tWzCkItem"></t:tab>--%>
		<%--</t:tabs>--%>
	<%--</div>--%>
	<%--<table style="display:none">--%>
		<%--<tbody id="add_tWzCkItem_table_template2">--%>
		<%--<tr>--%>
			<%--<td align="center"><div style="width: 25px;" name="xh"></div></td>--%>
			<%--<td align="center"><input style="width:20px;" type="checkbox" name="ck"/></td>--%>
			<%--<td align="left">--%>
				<%--<input name="tWzCkItemList[#index#].matCode" readonly="readonly" name="tWzCkItemList[#index#].matCode" type="text" style="width: 150px" class="searchbox-inputtext" 		datatype="*" ignore="ignore"    onclick="popupClick(this,'mat_code,mat_name,mat_unit,mat_location,mat_batch,mat_price','matCode,matName,matUnit,matLocation,matBatch,matPrice','wz_stock_pop')" value="${poVal.matCode }" />--%>


				<%--<label class="Validform_label" style="display: none;">物料编码</label>--%>
			<%--</td>--%>
			<%--<td align="left">--%>
				<%--<input name="tWzCkItemList[#index#].matName" readonly="readonly" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="ignore" />--%>
				<%--<label class="Validform_label" style="display: none;">物料名称</label>--%>
			<%--</td>--%>
			<%--<td align="left">--%>
				<%--<input name="tWzCkItemList[#index#].matQty" readonly="readonly" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="checked" />--%>
				<%--<label class="Validform_label" style="display: none;">数量</label>--%>
			<%--</td>--%>
			<%--<td align="left">--%>
				<%--<input name="tWzCkItemList[#index#].matUnit" readonly="readonly" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="ignore" />--%>
				<%--<label class="Validform_label" style="display: none;">单位</label>--%>
			<%--</td>--%>
			<%--<td align="left">--%>
				<%--<input name="tWzCkItemList[#index#].matLocation" readonly="readonly" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="ignore" />--%>
				<%--<label class="Validform_label" style="display: none;">仓库</label>--%>
			<%--</td>--%>
			<%--<td align="left">--%>
				<%--<input name="tWzCkItemList[#index#].matBatch" readonly="readonly" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />--%>
				<%--<label class="Validform_label" style="display: none;">批次</label>--%>
			<%--</td>--%>
			<%--<td align="left">--%>
				<%--<input name="tWzCkItemList[#index#].matPrice" readonly="readonly" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />--%>
				<%--<label class="Validform_label" style="display: none;">单价</label>--%>
			<%--</td>--%>
			<%--<td align="left">--%>
				<%--<input name="tWzCkItemList[#index#].itemRemark" readonly="readonly" maxlength="232" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />--%>
				<%--<label class="Validform_label" style="display: none;">备注</label>--%>
			<%--</td>--%>
			<%--<td align="left">--%>
				<%--<input name="tWzCkItemList[#index#].by1" readonly="readonly" maxlength="232" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />--%>
				<%--<label class="Validform_label" style="display: none;">备注2</label>--%>
			<%--</td>--%>
			<%--<td align="left">--%>
				<%--<input name="tWzCkItemList[#index#].by2" readonly="readonly" maxlength="232" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />--%>
				<%--<label class="Validform_label" style="display: none;">备注3</label>--%>
			<%--</td>--%>
		<%--</tr>--%>
		<%--</tbody>--%>
	<%--</table>--%>
<%--</c:if>--%>
 </body>
 <script src = "webpage/com/zzjee/wzyw/tWzCkHead.js"></script>
