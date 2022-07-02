<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>入库抬头</title>
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
      $('#tt2').tabs({
          onSelect:function(title){
              $('#tt2 .panel-body').css('width','auto');
          }
      });
	$(".tabs-wrap").css('width','100%');
  });
 </script>
 </head>
 <body style="overflow-x: hidden;">
  <c:if test="${tWzRkHeadPage.bpmStatus eq 0}">
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" tiptype="1" action="tWzRkHeadController.do?doUpdate" >
					<input id="id" name="id" type="hidden" value="${tWzRkHeadPage.id }"/>
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td align="right">
				<label class="Validform_label">供应商编码:</label>
			</td>
			<td class="value">
					<input id="vendorCode" name="vendorCode" type="text" style="width: 150px" class="searchbox-inputtext" 		datatype="*" ignore="checked"    value='${tWzRkHeadPage.vendorCode}'/>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">供应商编码</label>
			</td>
			<td align="right">
				<label class="Validform_label">供应商名称:</label>
			</td>
			<td class="value">
		     	 <input id="vendorName" name="vendorName" type="text" style="width: 150px" class="inputxt" 		datatype="*" ignore="checked"  value='${tWzRkHeadPage.vendorName}'/>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">供应商名称</label>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="Validform_label">入库日期:</label>
			</td>
			<td class="value">
					  <input id="docDate" name="docDate" type="text" style="width: 150px"   ignore="ignore"  onClick="WdatePicker()" value='${tWzRkHeadPage.docDate}'/>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">入库日期</label>
			</td>
			<td align="right">
				<label class="Validform_label">入库备注:</label>
			</td>
			<td class="value">
		     	 <input id="rkRemark" name="rkRemark" type="text" style="width: 150px" class="inputxt"  ignore="ignore"  value='${tWzRkHeadPage.rkRemark}'/>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">入库备注</label>
			</td>
		</tr>
		<%--<tr>--%>
			<%--<td align="right">--%>
				<%--<label class="Validform_label">附件:</label>--%>
			<%--</td>--%>
			<%--<td class="value">--%>
		     	 <%--<input id="rkFujian" name="rkFujian" type="text" style="width: 150px" class="inputxt"  ignore="ignore"  value='${tWzRkHeadPage.rkFujian}'/>--%>
				<%--<span class="Validform_checktip"></span>--%>
				<%--<label class="Validform_label" style="display: none;">附件</label>--%>
			<%--</td>--%>
		<%--</tr>--%>

			</table>
			<div style="width: auto;height: 200px;">
				<%-- 增加一个div，用于调节页面大小，否则默认太小 --%>
				<div style="width:800px;height:1px;"></div>
				<t:tabs id="tt" iframe="false" tabPosition="top" fit="false">
				 <t:tab href="tWzRkHeadController.do?tWzRkItemList&id=${tWzRkHeadPage.id}" icon="icon-search" title="入库商品" id="tWzRkItem"></t:tab>
				</t:tabs>
			</div>
			</t:formvalid>
			<!-- 添加 附表明细 模版 -->
		<table style="display:none">
		<tbody id="add_tWzRkItem_table_template">
			<tr>
			 <td align="center"><div style="width: 25px;" name="xh"></div></td>
			 <td align="center"><input style="width:20px;" type="checkbox" name="ck"/></td>
				  <td align="left">
					  				<input name="tWzRkItemList[#index#].matCode" name="tWzRkItemList[#index#].matCode" type="text" style="width: 150px" class="searchbox-inputtext" 		datatype="*" ignore="checked"    onclick="popupClick(this,'mat_code,mat_name,mat_unit,mat_location,mat_price','matCode,matName,matUnit,matLocation,matPrice','wz_material_pop')" value="${poVal.matCode }" />


					  <label class="Validform_label" style="display: none;">物料编码</label>
				  </td>
				  <td align="left">
					  		<input name="tWzRkItemList[#index#].matName" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="checked" />
					  <label class="Validform_label" style="display: none;">物料名称</label>
				  </td>
				  <td align="left">
					  		<input name="tWzRkItemList[#index#].matQty" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="checked" />
					  <label class="Validform_label" style="display: none;">入库数量</label>
				  </td>
				  <td align="left">
					  		<input name="tWzRkItemList[#index#].matUnit" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="checked" />
					  <label class="Validform_label" style="display: none;">物料单位</label>
				  </td>
				  <td align="left">
					  		<input name="tWzRkItemList[#index#].matLocation" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="checked" />
					  <label class="Validform_label" style="display: none;">仓库</label>
				  </td>
				  <td align="left">
					  		<input name="tWzRkItemList[#index#].matBatch" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
					  <label class="Validform_label" style="display: none;">批次</label>
				  </td>
				  <td align="left">
					  		<input name="tWzRkItemList[#index#].matPrice" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
					  <label class="Validform_label" style="display: none;">标准价</label>
				  </td>
				  <td align="left">
					  		<input name="tWzRkItemList[#index#].itemRemark" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
					  <label class="Validform_label" style="display: none;">备注</label>
				  </td>
				<td align="left">
					<input name="tWzRkItemList[#index#].by1" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
					<label class="Validform_label" style="display: none;">备注2</label>
				</td>
				<td align="left">
					<input name="tWzRkItemList[#index#].by2" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
					<label class="Validform_label" style="display: none;">备注3</label>
				</td>
			</tr>
		 </tbody>
		</table>
  </c:if>
  <c:if test="${tWzRkHeadPage.bpmStatus eq 1}">
	  <table cellpadding="0" cellspacing="1" class="formtable">
		  <tr>
			  <td align="right">
				  <label class="Validform_label">供应商编码:</label>
			  </td>
			  <td class="value">
				  <input name="vendorCode2" type="text" style="width: 150px" class="searchbox-inputtext" datatype="*" ignore="checked" readonly="readonly"  value='${tWzRkHeadPage.vendorCode}'/>
				  <span class="Validform_checktip"></span>
				  <label class="Validform_label" style="display: none;">供应商编码</label>
			  </td>
			  <td align="right">
				  <label class="Validform_label">供应商名称:</label>
			  </td>
			  <td class="value">
				  <input name="vendorName2" type="text" style="width: 150px" class="inputxt" 	readonly="readonly"	datatype="*" ignore="checked"  value='${tWzRkHeadPage.vendorName}'/>
				  <span class="Validform_checktip"></span>
				  <label class="Validform_label" style="display: none;">供应商名称</label>
			  </td>
		  </tr>
		  <tr>
			  <td align="right">
				  <label class="Validform_label">入库日期:</label>
			  </td>
			  <td class="value">
				  <input  name="docDate2" type="text" style="width: 150px"   ignore="ignore"  readonly="readonly" value='${tWzRkHeadPage.docDate}'/>
				  <span class="Validform_checktip"></span>
				  <label class="Validform_label" style="display: none;">入库日期</label>
			  </td>
			  <td align="right">
				  <label class="Validform_label">入库备注:</label>
			  </td>
			  <td class="value">
				  <input  name="rkRemark2" type="text" style="width: 150px" class="inputxt"  ignore="ignore"  readonly="readonly" value='${tWzRkHeadPage.rkRemark}'/>
				  <span class="Validform_checktip"></span>
				  <label class="Validform_label" style="display: none;">入库备注</label>
			  </td>
		  </tr>
			  <%--<tr>--%>
			  <%--<td align="right">--%>
			  <%--<label class="Validform_label">附件:</label>--%>
			  <%--</td>--%>
			  <%--<td class="value">--%>
			  <%--<input id="rkFujian" name="rkFujian" type="text" style="width: 150px" class="inputxt"  ignore="ignore"  value='${tWzRkHeadPage.rkFujian}'/>--%>
			  <%--<span class="Validform_checktip"></span>--%>
			  <%--<label class="Validform_label" style="display: none;">附件</label>--%>
			  <%--</td>--%>
			  <%--</tr>--%>

	  </table>
	  <div style="width: auto;height: 200px;">
			  <%-- 增加一个div，用于调节页面大小，否则默认太小 --%>
		  <div style="width:800px;height:1px;"></div>
		  <t:tabs id="tt" iframe="false" tabPosition="top" fit="false">
			  <t:tab href="tWzRkHeadController.do?tWzRkItemListUnEdit&id=${tWzRkHeadPage.id}" title="入库商品" id="tWzRkItem"></t:tab>
		  </t:tabs>
	  </div>
	  <table style="display:none">
		  <tbody>
		  <tr>
			  <td align="center"><div style="width: 25px;" name="xh"></div></td>
			  <td align="center"><input style="width:20px;" type="checkbox" name="ck"/></td>
			  <td align="left">
				  <input  name="tWzRkItemList[#index#].matCode" value="${poVal.matCode }" type="text" style="width: 150px" datatype="*" ignore="checked"  readonly="readonly"/>

				  <label class="Validform_label" style="display: none;">物料编码</label>
			  </td>
			  <td align="left">
				  <input name="tWzRkItemList[#index#].matName" maxlength="32" type="text" class="inputxt"  style="width:120px;" 	readonly="readonly" value="${poVal.matName}" 	datatype="*" ignore="checked" />
				  <label class="Validform_label" style="display: none;">物料名称</label>
			  </td>
			  <td align="left">
				  <input name="tWzRkItemList[#index#].matQty"  value="${poVal.matQty}" maxlength="32" type="text" class="inputxt"  style="width:120px;" 	readonly="readonly"	datatype="*" ignore="checked" />
				  <label class="Validform_label" style="display: none;">入库数量</label>
			  </td>
			  <td align="left">
				  <input name="tWzRkItemList[#index#].matUnit" value="${poVal.matUnit}" maxlength="32" type="text" class="inputxt"  style="width:120px;" 	readonly="readonly"	datatype="*" ignore="checked" />
				  <label class="Validform_label" style="display: none;">物料单位</label>
			  </td>
			  <td align="left">
				  <input name="tWzRkItemList[#index#].matLocation" value="${poVal.matLocation}" maxlength="32" type="text" class="inputxt"  readonly="readonly" style="width:120px;" 		datatype="*" ignore="checked" />
				  <label class="Validform_label" style="display: none;">仓库</label>
			  </td>
			  <td align="left">
				  <input name="tWzRkItemList[#index#].matBatch" value="${poVal.matBatch}" maxlength="32" type="text" class="inputxt" readonly="readonly"   style="width:120px;"  ignore="ignore" />
				  <label class="Validform_label" style="display: none;">批次</label>
			  </td>
			  <td align="left">
				  <input name="tWzRkItemList[#index#].matPrice"  value="${poVal.matPrice}" maxlength="32" type="text" readonly="readonly" class="inputxt"  style="width:120px;"  ignore="ignore" />
				  <label class="Validform_label" style="display: none;">标准价</label>
			  </td>
			  <td align="left">
				  <input name="tWzRkItemList[#index#].itemRemark" value="${poVal.itemRemark}" maxlength="32" readonly="readonly" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
				  <label class="Validform_label" style="display: none;">备注</label>
			  </td>
			  <td align="left">
				  <input name="tWzRkItemList[#index#].by1"  value="${poVal.by1}" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
				  <label class="Validform_label" style="display: none;">备注2</label>
			  </td>
			  <td align="left">
				  <input name="tWzRkItemList[#index#].by2"  value="${poVal.by2}" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
				  <label class="Validform_label" style="display: none;">备注3</label>
			  </td>
		  </tr>
		  </tbody>
	  </table>
  </c:if>
 </body>
 <script src = "webpage/com/zzjee/wzyw/tWzRkHead.js"></script>
