<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>v_wz_po_wq</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="vWzPoWqController.do?doAdd" >
					<input id="id" name="id" type="hidden" value="${vWzPoWqPage.id }"/>
		<table style="width: 600px;" cellpadding="0" cellspacing="1" class="formtable">
				<tr>
					<td align="right">
						<label class="Validform_label">
							物料编码:
						</label>
					</td>
					<td class="value">
					     	 <input id="matCode" name="matCode" type="text" style="width: 150px" class="inputxt"  ignore="ignore" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">物料编码</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							物料名称:
						</label>
					</td>
					<td class="value">
					     	 <input id="matName" name="matName" type="text" style="width: 150px" class="inputxt"  ignore="ignore" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">物料名称</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							单位:
						</label>
					</td>
					<td class="value">
					     	 <input id="matUnit" name="matUnit" type="text" style="width: 150px" class="inputxt"  ignore="ignore" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">单位</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							仓库:
						</label>
					</td>
					<td class="value">
					     	 <input id="matLocation" name="matLocation" type="text" style="width: 150px" class="inputxt"  ignore="ignore" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">仓库</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							批次:
						</label>
					</td>
					<td class="value">
					     	 <input id="matBatch" name="matBatch" type="text" style="width: 150px" class="inputxt"  ignore="ignore" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">批次</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							标准价:
						</label>
					</td>
					<td class="value">
					     	 <input id="matPrice" name="matPrice" type="text" style="width: 150px" class="inputxt"  ignore="ignore" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">标准价</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							数量:
						</label>
					</td>
					<td class="value">
					     	 <input id="matQty" name="matQty" type="text" style="width: 150px" class="inputxt"  ignore="ignore" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">数量</label>
						</td>
				</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							matQtyWq:
						</label>
					</td>
					<td class="value">
					     	 <input id="matQtyWq" name="matQtyWq" type="text" style="width: 150px" class="inputxt"  datatype="/^(-?\d+)(\.\d+)?$/"  ignore="ignore" />
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">matQtyWq</label>
						</td>
				</tr>
				
				
			</table>
		</t:formvalid>
 </body>
  <script src = "webpage/com/zzjee/wzpo/vWzPoWq.js"></script>
