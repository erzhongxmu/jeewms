<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
 <head>
  <title>物料</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <script type="text/javascript">
  //编写自定义JS代码
  </script>
 </head>
 <body>
		<t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" action="tWzMaterialController.do?doUpdate" >
					<input id="id" name="id" type="hidden" value="${tWzMaterialPage.id }"/>
			<table style="width: 800px;" cellpadding="0" cellspacing="1" class="formtable">

			<tr    >
<tr>
			<td align="right">
				<label class="Validform_label">
					产品大类:
				</label>
			</td>
			<td class="value">
				<t:dictSelect field="by2" type="list" extendJson="{class:'form-control';datatype:'*'}"
							  dictTable="ba_deg_type" dictField="deg_type_code" dictText="deg_type_name" defaultVal="${tWzMaterialPage.by2}" hasLabel="false"  title="产品大类"></t:dictSelect>

			</td>

			<td align="right">
				<label class="Validform_label">
					产品小类:
				</label>
			</td>
			<td class="value">
				<t:dictSelect field="by3" type="list" extendJson="{class:'form-control';datatype:'*'}"
							  dictTable="ba_goods_type" dictField="goods_type_code" dictText="goods_type_name" defaultVal="${tWzMaterialPage.by3}" hasLabel="false"  title="产品属性"></t:dictSelect>

			</td>
		</tr>
				<tr>



					<td align="right">
						<label class="Validform_label">
							存储位置:
						</label>
					</td>
					<td class="value">
						<input id="by4" name="by4" type="text" style="width: 150px" class="inputxt" 	value='${tWzMaterialPage.by4}'	datatype="*" ignore="checked" />

					</td>

					<td align="right">
						<label class="Validform_label">
							所属设备:
						</label>
					</td>
					<td class="value">
						<input id="by5" name="by5" type="text" style="width: 150px" class="inputxt" 	value='${tWzMaterialPage.by5}'	datatype="*" ignore="checked" />

					</td>



				</tr>
				<tr>



					<td align="right">
						<label class="Validform_label">
							库位:
						</label>
					</td>
					<td class="value">
						<input id="by7" name="by7" type="text" style="width: 150px" class="inputxt" 	value='${tWzMaterialPage.by7}'	datatype="*" ignore="checked" />

					</td>

					<td align="right">
						<label class="Validform_label">
							库区:
						</label>
					</td>
					<td class="value">
						<input id="by8" name="by8" type="text" style="width: 150px" class="inputxt" 	value='${tWzMaterialPage.by8}'	datatype="*" ignore="checked" />

					</td>



				</tr>

			<tr>
						<td align="right">
							<label class="Validform_label">
								物料编码:
							</label>
						</td>
						<td class="value">
						    <input id="matCode" name="matCode" type="text" style="width: 150px" class="inputxt" 		validType="t_wz_material,mat_code,id" datatype="*" ignore="checked"  value='${tWzMaterialPage.matCode}'/>

						</td>

				<td align="right">
					<label class="Validform_label">
						物料条码:
					</label>
				</td>
				<td class="value">
					<input id="by1" name="by1" type="text" style="width: 150px" class="inputxt" 		     value='${tWzMaterialPage.by1}'/>

				</td>
			</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								物料名称:
							</label>
						</td>
						<td class="value">
						    <input id="matName" name="matName" type="text" style="width: 150px" class="inputxt" 		datatype="*" ignore="checked"  value='${tWzMaterialPage.matName}'/>

						</td>

						<td align="right">
							<label class="Validform_label">
								单位:
							</label>
						</td>
 						<td class="value">
									<t:dictSelect field="matUnit" type="list" 		datatype="*" dictTable="ba_unit" dictField="UNIT_CODE" dictText="UNIT_ZH_NAME"   defaultVal="${tWzMaterialPage.matUnit}" hasLabel="false"  title="单位" ></t:dictSelect>

						</td>
					</tr>
					<tr>
						<td align="right">
							<label class="Validform_label">
								规格:
							</label>
						</td>
						<td class="value">
						    <input id="matGuige" name="matGuige" type="text" style="width: 150px" class="inputxt"  ignore="ignore"  value='${tWzMaterialPage.matGuige}'/>

						</td>

						<td align="right">
							<label class="Validform_label">
								等级:
							</label>
						</td>
						<td class="value">
						    <input id="matClass" name="matClass" type="text" style="width: 150px" class="inputxt"  ignore="ignore"  value='${tWzMaterialPage.matClass}'/>
							<span class="Validform_checktip"></span>
							<label class="Validform_label" style="display: none;">等级</label>
						</td>
					</tr>
					<tr>
<%--						<td align="right">--%>
<%--							<label class="Validform_label">--%>
<%--								标准价元:--%>
<%--							</label>--%>
<%--						</td>--%>
<%--						<td class="value">--%>
<%--						    <input id="matPrice" name="matPrice" type="text" style="width: 150px" class="inputxt" 		datatype="*" ignore="checked"  value='${tWzMaterialPage.matPrice}'/>--%>

<%--						</td>--%>

						<td align="right">
							<label class="Validform_label">
								安全库存:
							</label>
						</td>
						<td class="value">
						    <input id="matAqkc" name="matAqkc" type="text" style="width: 150px" class="inputxt" 		datatype="*" ignore="checked"  value='${tWzMaterialPage.matAqkc}'/>

						</td>

						<td align="right">
							<label class="Validform_label">
								库存地点:
							</label>
						</td>
						<td class="value">
									<t:dictSelect field="matLocation" type="list" 		datatype="*" dictTable="t_wz_location" dictField="mat_location" dictText="mat_location"   defaultVal="${tWzMaterialPage.matLocation}" hasLabel="false"  title="库存地点" ></t:dictSelect>

						</td>
					</tr>
				<tr>
					<td align="right">
						<label class="Validform_label">
							附件:
						</label>
					</td>
					<td class="value">
						<t:webUploader auto="true"  pathValues="${tWzMaterialPage.by6}"  name="by6" duplicate="true" fileNumLimit="1"></t:webUploader>

					</td>
				</tr>
			</table>
		</t:formvalid>
 </body>
  <script src = "webpage/com/zzjee/wz/tWzMaterial.js"></script>
