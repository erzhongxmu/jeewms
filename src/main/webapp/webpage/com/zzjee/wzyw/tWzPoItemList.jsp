<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	$('#addTWzPoItemBtn').linkbutton({   
	    iconCls: 'icon-add'  
	});  
	$('#delTWzPoItemBtn').linkbutton({   
	    iconCls: 'icon-remove'  
	}); 
	$('#addTWzPoItemBtn').bind('click', function(){   
 		 var tr =  $("#add_tWzPoItem_table_template tr").clone();
	 	 $("#add_tWzPoItem_table").append(tr);
	 	 resetTrNum('add_tWzPoItem_table');
	 	 return false;
    });  
	$('#delTWzPoItemBtn').bind('click', function(){   
      	$("#add_tWzPoItem_table").find("input:checked").parent().parent().remove();   
        resetTrNum('add_tWzPoItem_table'); 
        return false;
    }); 
    $(document).ready(function(){
    	$(".datagrid-toolbar").parent().css("width","auto");
    	if(location.href.indexOf("load=detail")!=-1){
			$(":input").attr("disabled","true");
			$(".datagrid-toolbar").hide();
		}
    });
</script>
<div style="padding: 3px; height: 25px;width:auto;" class="datagrid-toolbar">
	<a id="addTWzPoItemBtn" href="#">添加</a> <a id="delTWzPoItemBtn" href="#">删除</a> 
</div>
<table border="0" cellpadding="2" cellspacing="0" id="tWzPoItem_table">
	<tr bgcolor="#E6E6E6">
		<td align="center" bgcolor="#EEEEEE" style="width: 25px;">序号</td>
		<td align="center" bgcolor="#EEEEEE" style="width: 25px;">操作</td>
				  <td align="left" bgcolor="#EEEEEE" style="width: 126px;">
						物料编码
				  </td>
				  <td align="left" bgcolor="#EEEEEE" style="width: 126px;">
						物料名称
				  </td>
				  <td align="left" bgcolor="#EEEEEE" style="width: 126px;">
						数量
				  </td>
				  <td align="left" bgcolor="#EEEEEE" style="width: 126px;">
						单位
				  </td>
				  <td align="left" bgcolor="#EEEEEE" style="width: 126px;">
						仓库
				  </td>
				  <td align="left" bgcolor="#EEEEEE" style="width: 126px;">
						批次
				  </td>
				  <td align="left" bgcolor="#EEEEEE" style="width: 126px;">
						标准价
				  </td>
				  <td align="left" bgcolor="#EEEEEE" style="width: 126px;">
						备注
				  </td>
	</tr>
	<tbody id="add_tWzPoItem_table">
	<c:if test="${fn:length(tWzPoItemList)  <= 0 }">
			<tr>
				<td align="center"><div style="width: 25px;" name="xh">1</div></td>
				<td align="center"><input style="width:20px;"  type="checkbox" name="ck"/></td>
					<input name="tWzPoItemList[0].id" type="hidden"/>
					<input name="tWzPoItemList[0].createName" type="hidden"/>
					<input name="tWzPoItemList[0].createBy" type="hidden"/>
					<input name="tWzPoItemList[0].createDate" type="hidden"/>
					<input name="tWzPoItemList[0].updateName" type="hidden"/>
					<input name="tWzPoItemList[0].updateBy" type="hidden"/>
					<input name="tWzPoItemList[0].updateDate" type="hidden"/>
					<input name="tWzPoItemList[0].sysOrgCode" type="hidden"/>
					<input name="tWzPoItemList[0].sysCompanyCode" type="hidden"/>
					<input name="tWzPoItemList[0].bpmStatus" type="hidden"/>
					<input name="tWzPoItemList[0].poId" type="hidden"/>
					<input name="tWzPoItemList[0].matAmount" type="hidden"/>
					<input name="tWzPoItemList[0].by1" type="hidden"/>
					<input name="tWzPoItemList[0].by2" type="hidden"/>
					<input name="tWzPoItemList[0].by3" type="hidden"/>
					<input name="tWzPoItemList[0].matQtyWq" type="hidden"/>
				  <td align="left">
							 <input  id="tWzPoItemList[0].matCode" name="tWzPoItemList[0].matCode" type="text" style="width: 150px" class="searchbox-inputtext"  value="${poVal.matCode }"   ignore="ignore"   onclick="popupClick(this,'mat_code,mat_name,mat_unit,mat_location,mat_price','matCode,matName,matUnit,matLocation,matPrice','wz_material_pop')"/> 			 
					  <label class="Validform_label" style="display: none;">物料编码</label>
					</td>
				  <td align="left">
					  	<input name="tWzPoItemList[0].matName" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" >
					  <label class="Validform_label" style="display: none;">物料名称</label>
					</td>
				  <td align="left">
					  	<input name="tWzPoItemList[0].matQty" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" >
					  <label class="Validform_label" style="display: none;">数量</label>
					</td>
				  <td align="left">
					  	<input name="tWzPoItemList[0].matUnit" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" >
					  <label class="Validform_label" style="display: none;">单位</label>
					</td>
				  <td align="left">
					  	<input name="tWzPoItemList[0].matLocation" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" >
					  <label class="Validform_label" style="display: none;">仓库</label>
					</td>
				  <td align="left">
					  	<input name="tWzPoItemList[0].matBatch" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" >
					  <label class="Validform_label" style="display: none;">批次</label>
					</td>
				  <td align="left">
					  	<input name="tWzPoItemList[0].matPrice" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" >
					  <label class="Validform_label" style="display: none;">标准价</label>
					</td>
				  <td align="left">
					  	<input name="tWzPoItemList[0].itemRemark" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" >
					  <label class="Validform_label" style="display: none;">备注</label>
					</td>
   			</tr>
	</c:if>
	<c:if test="${fn:length(tWzPoItemList)  > 0 }">
		<c:forEach items="${tWzPoItemList}" var="poVal" varStatus="stuts">
			<tr>
				<td align="center"><div style="width: 25px;" name="xh">${stuts.index+1 }</div></td>
				<td align="center"><input style="width:20px;"  type="checkbox" name="ck" /></td>
						<input name="tWzPoItemList[${stuts.index }].id" type="hidden" value="${poVal.id }"/>
						<input name="tWzPoItemList[${stuts.index }].createName" type="hidden" value="${poVal.createName }"/>
						<input name="tWzPoItemList[${stuts.index }].createBy" type="hidden" value="${poVal.createBy }"/>
						<input name="tWzPoItemList[${stuts.index }].createDate" type="hidden" value="${poVal.createDate }"/>
						<input name="tWzPoItemList[${stuts.index }].updateName" type="hidden" value="${poVal.updateName }"/>
						<input name="tWzPoItemList[${stuts.index }].updateBy" type="hidden" value="${poVal.updateBy }"/>
						<input name="tWzPoItemList[${stuts.index }].updateDate" type="hidden" value="${poVal.updateDate }"/>
						<input name="tWzPoItemList[${stuts.index }].sysOrgCode" type="hidden" value="${poVal.sysOrgCode }"/>
						<input name="tWzPoItemList[${stuts.index }].sysCompanyCode" type="hidden" value="${poVal.sysCompanyCode }"/>
						<input name="tWzPoItemList[${stuts.index }].bpmStatus" type="hidden" value="${poVal.bpmStatus }"/>
						<input name="tWzPoItemList[${stuts.index }].poId" type="hidden" value="${poVal.poId }"/>
						<input name="tWzPoItemList[${stuts.index }].matAmount" type="hidden" value="${poVal.matAmount }"/>
						<input name="tWzPoItemList[${stuts.index }].by1" type="hidden" value="${poVal.by1 }"/>
						<input name="tWzPoItemList[${stuts.index }].by2" type="hidden" value="${poVal.by2 }"/>
						<input name="tWzPoItemList[${stuts.index }].by3" type="hidden" value="${poVal.by3 }"/>
						<input name="tWzPoItemList[${stuts.index }].matQtyWq" type="hidden" value="${poVal.matQtyWq }"/>
				   <td align="left">
							 <input  id="tWzPoItemList[${stuts.index }].matCode" name="tWzPoItemList[${stuts.index }].matCode"  type="text" style="width: 150px" class="searchbox-inputtext"   ignore="ignore"  onclick="popupClick(this,'mat_code,mat_name,mat_unit,mat_location,mat_price','matCode,matName,matUnit,matLocation,matPrice','wz_material_pop')"    value="${poVal.matCode }" /> 			 
					  <label class="Validform_label" style="display: none;">物料编码</label>
				   </td>
				   <td align="left">
					  	<input name="tWzPoItemList[${stuts.index }].matName" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore"  value="${poVal.matName }"/>
					  <label class="Validform_label" style="display: none;">物料名称</label>
				   </td>
				   <td align="left">
					  	<input name="tWzPoItemList[${stuts.index }].matQty" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore"  value="${poVal.matQty }"/>
					  <label class="Validform_label" style="display: none;">数量</label>
				   </td>
				   <td align="left">
					  	<input name="tWzPoItemList[${stuts.index }].matUnit" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore"  value="${poVal.matUnit }"/>
					  <label class="Validform_label" style="display: none;">单位</label>
				   </td>
				   <td align="left">
					  	<input name="tWzPoItemList[${stuts.index }].matLocation" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore"  value="${poVal.matLocation }"/>
					  <label class="Validform_label" style="display: none;">仓库</label>
				   </td>
				   <td align="left">
					  	<input name="tWzPoItemList[${stuts.index }].matBatch" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore"  value="${poVal.matBatch }"/>
					  <label class="Validform_label" style="display: none;">批次</label>
				   </td>
				   <td align="left">
					  	<input name="tWzPoItemList[${stuts.index }].matPrice" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore"  value="${poVal.matPrice }"/>
					  <label class="Validform_label" style="display: none;">标准价</label>
				   </td>
				   <td align="left">
					  	<input name="tWzPoItemList[${stuts.index }].itemRemark" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore"  value="${poVal.itemRemark }"/>
					  <label class="Validform_label" style="display: none;">备注</label>
				   </td>
   			</tr>
		</c:forEach>
	</c:if>	
	</tbody>
</table>
