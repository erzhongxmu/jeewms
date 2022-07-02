<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>审批配置</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="tWzSpConfController.do?doAdd" >
					<input id="id" name="id" type="hidden" value="${tWzSpConfPage.id }"/>
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							审批类型:
						</label>
					</td>
					<td class="value">
					     	 <input id="spType" name="spType" type="text" style="width: 150px" class="inputxt"  ignore="ignore" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">审批类型</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							最低金额:
						</label>
					</td>
					<td class="value">
					     	 <input id="spJine" name="spJine" type="text" style="width: 150px" class="inputxt"  ignore="ignore" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">最低金额</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							审批人:
						</label>
					</td>
					<td class="value">
					     	 <%--<input id="spUsername" name="spUsername" type="text" style="width: 150px" class="inputxt"  ignore="ignore" />--%>
						<t:userSelect title="用户名称" selectedNamesInputId="userNames" selectedIdsInputId="spUsername" windowWidth="1000px" windowHeight="600px"></t:userSelect>
						<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">审批人</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							备注:
						</label>
					</td>
					<td class="value">
					     	 <input id="spRemark" name="spRemark" type="text" style="width: 150px" class="inputxt"  ignore="ignore" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">备注</label>
						</td>
				</tr>
				
				
			</table>
		</t:formvalid>
 </body>
  <script src = "webpage/com/zzjee/wz/tWzSpConf.js"></script>
