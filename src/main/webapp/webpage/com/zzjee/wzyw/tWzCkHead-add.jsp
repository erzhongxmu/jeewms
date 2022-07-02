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
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" tiptype="1" beforeSubmit="doAddcheck()" action="tWzCkHeadController.do?doAdd" >
					<input id="id" name="id" type="hidden" value="${tWzCkHeadPage.id }"/>
	<table cellpadding="0" cellspacing="1" class="formtable">
		<tr>
			<td align="right">
				<label class="Validform_label">出库类别:</label>
			</td>
			<td class="value">
				<t:dictSelect field="by1" type="radio"  defaultVal="领料出库" typeGroupCode="wz_cklb"    hasLabel="false"  title="出库类别" ></t:dictSelect>
				<span class="Validform_checktip"></span>
			</td>
			<td align="right">
				<label class="Validform_label">业务类型 :</label>
			</td>
			<td class="value">
				<t:dictSelect field="by2" type="radio"  defaultVal="出库"  typeGroupCode="wz_ywlx"    hasLabel="false"  title="业务类型" ></t:dictSelect>
				<span class="Validform_checktip"></span>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="Validform_label">再利用程度 :</label>
			</td>
			<td>
			<t:dictSelect field="by3" type="radio"  defaultVal="正常出库"  typeGroupCode="wz_zlycd"    hasLabel="false"  title="再利用程度" ></t:dictSelect>
				<span class="Validform_checktip"></span>
			</td>

            <td align="right">
                <label class="Validform_label">手工审批:</label>
            </td>
            <td class="value">
                <t:dictSelect field="ckSgsp" type="radio"   typeGroupCode="sf_yn"  defaultVal="N" hasLabel="false"  title="手工审批" ></t:dictSelect>
                <span class="Validform_checktip"></span>
            </td>
			<%--<td align="right">--%>
				<%--<label class="Validform_label">领用人:</label>--%>
			<%--</td>--%>
			<%--<td class="value">--%>
		     	 <%--&lt;%&ndash;<input id="ckUsername" name="ckUsername" type="text" style="width: 150px" class="inputxt"  ignore="ignore" />&ndash;%&gt;--%>
				<%--<span class="Validform_checktip"></span>--%>
				<%--<label class="Validform_label" style="display: none;">领用人</label>--%>
			<%--</td>--%>
		</tr>
		<tr>
			<td align="right">
				<label class="Validform_label">领用人(${usernametext}):</label>
			</td>
			<td class="value">
				<%--<t:userSelect  userNamesDefalutVal='${username}'  title="用户名称"   selectedNamesInputId="ckName" selectedIdsInputId="ckUsername" readonly="readonly"  windowWidth="1000px"  windowHeight="600px"></t:userSelect>--%>

				<input id="ckUsername" name="ckUsername" type="text" value='${username}' readonly="readonly" style="width: 150px" class="inputxt"  ignore="ignore" />不可更改
				<span class="Validform_checktip"></span>
			</td>

			<td align="right">
				<label class="Validform_label">单据日期:</label>
			</td>
			<td class="value">
					  <input id="docDate" name="docDate" type="text" style="width: 150px"  class="Wdate" onClick="WdatePicker()"  ignore="ignore"  />
				<span class="Validform_checktip"></span>
			</td>
		</tr>
		<tr>
			<td align="right">
				<label class="Validform_label">出库备注:</label>
			</td>
			<td class="value" colspan="3">
		     	 <input id="ckRemark" name="ckRemark" type="text" style="width: 150px" class="inputxt"  ignore="ignore" />
				<span class="Validform_checktip"></span>
			</td>
		</tr>
		<tr>

			<%--<td align="right">--%>
				<%--<label class="Validform_label">附件:</label>--%>
			<%--</td>--%>
			<%--<td class="value">--%>
		     	 <%--<input id="ckFujian" name="ckFujian" type="text" style="width: 150px" class="inputxt"  ignore="ignore" />--%>
				<%--<span class="Validform_checktip"></span>--%>
				<%--<label class="Validform_label" style="display: none;">附件</label>--%>
			<%--</td>--%>
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
					  		<input name="tWzCkItemList[#index#].matCode" name="tWzCkItemList[#index#].matCode" type="text" style="width: 150px" class="searchbox-inputtext" 		datatype="*" ignore="ignore"  onclick="popupClick(this,'mat_code,mat_name,mat_unit,mat_location,mat_batch,mat_price','matCode,matName,matUnit,matLocation,matBatch,matPrice','wz_stock_pop')"/>
					  <label class="Validform_label" style="display: none;">物料编码</label>
				  </td>
				  <td align="left">
					  	<input name="tWzCkItemList[#index#].matName" maxlength="32" type="text" class="inputxt"  style="width:120px;"   		datatype="*" ignore="ignore" />
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
 </body>
 <script src = "webpage/com/zzjee/wzyw/tWzCkHead.js"></script>
