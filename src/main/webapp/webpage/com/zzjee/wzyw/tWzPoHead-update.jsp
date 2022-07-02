<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>物料采购订单</title>
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
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" tiptype="1" action="tWzPoHeadController.do?doUpdate" >
					<input id="id" name="id" type="hidden" value="${tWzPoHeadPage.id }"/>
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td align="right">
				<label class="Validform_label">供应商编码:</label>
			</td>
			<td class="value">
					<input id="vendorCode" name="vendorCode" type="text" style="width: 150px" class="searchbox-inputtext"  ignore="ignore"   onclick="popupClick(this,'vendorCode,vendorName','wz_pop_vendor')"  value='${tWzPoHeadPage.vendorCode}'/>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">供应商编码</label>
			</td>
			<td align="right">
				<label class="Validform_label">供应商名称:</label>
			</td>
			<td class="value">
		     	 <input id="vendorName" name="vendorName" type="text" style="width: 150px" class="inputxt"  ignore="ignore"  value='${tWzPoHeadPage.vendorName}'/>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">供应商名称</label>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="Validform_label">采购订单日期:</label>
			</td>
			<td class="value">
					  <input id="docDate" name="docDate" type="text" style="width: 150px"   ignore="ignore"  value='<fmt:formatDate value='${tWzPoHeadPage.docDate}' type="date" pattern="yyyy-MM-dd"/>'/>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">采购订单日期</label>
			</td>
			<td align="right">
				<label class="Validform_label">采购订单备注:</label>
			</td>
			<td class="value">
		     	 <input id="poRemark" name="poRemark" type="text" style="width: 150px" class="inputxt"  ignore="ignore"  value='${tWzPoHeadPage.poRemark}'/>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">采购订单备注</label>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="Validform_label">采购类别:</label>
			</td>
			<td class="value">
				<t:dictSelect field="poBy1" type="radio"   typeGroupCode="wz_cglb"    hasLabel="false" defaultVal='${tWzPoHeadPage.poBy1}' title="采购类别" ></t:dictSelect>

			</td>

		</tr>
			</table>
			<div style="width: auto;height: 200px;">
				<%-- 增加一个div，用于调节页面大小，否则默认太小 --%>
				<div style="width:800px;height:1px;"></div>
				<t:tabs id="tt" iframe="false" tabPosition="top" fit="false">
				 <t:tab href="tWzPoHeadController.do?tWzPoItemList&id=${tWzPoHeadPage.id}" icon="icon-search" title="采购订单行项目" id="tWzPoItem"></t:tab>
				</t:tabs>
			</div>
			</t:formvalid>
			<!-- 添加 附表明细 模版 -->
		<table style="display:none">
		<tbody id="add_tWzPoItem_table_template">
			<tr>
			 <td align="center"><div style="width: 25px;" name="xh"></div></td>
			 <td align="center"><input style="width:20px;" type="checkbox" name="ck"/></td>
				  <td align="left">
					  				<input name="tWzPoItemList[#index#].matCode" name="tWzPoItemList[#index#].matCode" type="text" style="width: 150px" class="searchbox-inputtext"  ignore="ignore"    onclick="popupClick(this,'mat_code,mat_name,mat_unit,mat_location,mat_price','matCode,matName,matUnit,matLocation,matPrice','wz_material_pop')" value="${poVal.matCode }" />


					  <label class="Validform_label" style="display: none;">物料编码</label>
				  </td>
				  <td align="left">
					  		<input name="tWzPoItemList[#index#].matName" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
					  <label class="Validform_label" style="display: none;">物料名称</label>
				  </td>
				  <td align="left">
					  		<input name="tWzPoItemList[#index#].matQty" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
					  <label class="Validform_label" style="display: none;">数量</label>
				  </td>
				  <td align="left">
					  		<input name="tWzPoItemList[#index#].matUnit" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
					  <label class="Validform_label" style="display: none;">单位</label>
				  </td>
				  <td align="left">
					  		<input name="tWzPoItemList[#index#].matLocation" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
					  <label class="Validform_label" style="display: none;">仓库</label>
				  </td>
				  <td align="left">
					  		<input name="tWzPoItemList[#index#].matBatch" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
					  <label class="Validform_label" style="display: none;">批次</label>
				  </td>
				  <td align="left">
					  		<input name="tWzPoItemList[#index#].matPrice" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
					  <label class="Validform_label" style="display: none;">标准价</label>
				  </td>
				  <td align="left">
					  		<input name="tWzPoItemList[#index#].itemRemark" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" />
					  <label class="Validform_label" style="display: none;">备注</label>
				  </td>
			</tr>
		 </tbody>
		</table>
 </body>
 <script src = "webpage/com/zzjee/wzyw/tWzPoHead.js"></script>
