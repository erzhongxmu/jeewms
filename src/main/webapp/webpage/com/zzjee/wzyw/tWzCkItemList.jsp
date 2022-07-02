<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
	$('#addTWzCkItemBtn').linkbutton({
	    iconCls: 'icon-add'
	});
	$('#delTWzCkItemBtn').linkbutton({
	    iconCls: 'icon-remove'
	});
	$('#addTWzCkItemBtn').bind('click', function(){
 		 var tr =  $("#add_tWzCkItem_table_template tr").clone();
	 	 $("#add_tWzCkItem_table").append(tr);
	 	 resetTrNum('add_tWzCkItem_table');
	 	 return false;
    });
	$('#delTWzCkItemBtn').bind('click', function(){
      	$("#add_tWzCkItem_table").find("input:checked").parent().parent().remove();
        resetTrNum('add_tWzCkItem_table');
        return false;
    });
    $(document).ready(function(){
    	$(".datagrid-toolbar").parent().css("width","auto");
    	if(location.href.indexOf("load=detail")!=-1){
			$(":input").attr("disabled","true");
			$(".datagrid-toolbar").hide();
		}
    });

	function countby1() {
		var orQty = 0;
		var cpTiji = 0;
		var count = 0;

		$tbody = $("#add_tWzCkItem_table");
		console.log($tbody );
		$tbody.find('>tr').each(function(i) {
			console.log("i============"+i );

			orQty = 0;
			cpTiji = 0;
			count = 0;
			var $by1;
			$(':input, select,button,a', this).each(function () {
				var $this = $(this), validtype_str = $this.attr('validType'), name = $this.attr('name'),
						id = $this.attr('id'), onclick_str = $this.attr('onclick'), val = $this.val() ,value = $this.attr('value');
				value = 1*value;
				if (name.indexOf("matQty") >= 0 ) {
					if(value>0){
						orQty = 1* value;

					}
				}

				if (name.indexOf("matPrice") >= 0 ) {
					if(value>0){
						cpTiji = 1* value;
					}
				}

				if (name.indexOf("matAmount") >= 0 ) {
					$by1 = $this;
				}

			});
			count = orQty*cpTiji;
			count = count.toFixed(2)
			$by1.attr("value",count);


		});
	}
</script>
<div style="padding: 3px; height: 25px;width:auto;" class="datagrid-toolbar">
	<a id="addTWzCkItemBtn" href="#">添加</a> <a id="delTWzCkItemBtn" href="#">删除</a>
</div>
<table border="0" cellpadding="2" cellspacing="0" id="tWzCkItem_table">
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
						单价
				  </td>
				  <td align="left" bgcolor="#EEEEEE" style="width: 126px;">
						备注
				  </td>
				  <td align="left" bgcolor="#EEEEEE" style="width: 126px;">
					    总价
				  </td>
				  <td align="left" bgcolor="#EEEEEE" style="width: 126px;">
					    备注3
				  </td>
	</tr>
	<tbody id="add_tWzCkItem_table">
	<c:if test="${fn:length(tWzCkItemList)  <= 0 }">
			<tr>
				<td align="center"><div style="width: 25px;" name="xh">1</div></td>
				<td align="center"><input style="width:20px;"  type="checkbox" name="ck"/></td>
					<input name="tWzCkItemList[0].id" type="hidden"/>
					<input name="tWzCkItemList[0].createName" type="hidden"/>
					<input name="tWzCkItemList[0].createBy" type="hidden"/>
					<input name="tWzCkItemList[0].createDate" type="hidden"/>
					<input name="tWzCkItemList[0].updateName" type="hidden"/>
					<input name="tWzCkItemList[0].updateBy" type="hidden"/>
					<input name="tWzCkItemList[0].updateDate" type="hidden"/>
					<input name="tWzCkItemList[0].sysOrgCode" type="hidden"/>
					<input name="tWzCkItemList[0].sysCompanyCode" type="hidden"/>
					<input name="tWzCkItemList[0].bpmStatus" type="hidden"/>
					<input name="tWzCkItemList[0].wzckHid" type="hidden"/>
<%--					<input name="tWzCkItemList[0].matAmount" type="hidden"/>--%>
					<input name="tWzCkItemList[0].by1" type="hidden"/>
<%--					<input name="tWzCkItemList[0].by2" type="hidden"/>--%>
					<input name="tWzCkItemList[0].by3" type="hidden"/>
					<input name="tWzCkItemList[0].by4" type="hidden"/>
					<input name="tWzCkItemList[0].by5" type="hidden"/>
				  <td align="left">
							 <input  id="tWzCkItemList[0].matCode" name="tWzCkItemList[0].matCode" type="text" style="width: 150px" class="searchbox-inputtext"  value="${poVal.matCode }"  		datatype="*" ignore="ignore"   onclick="popupClick(this,'mat_code,mat_name,mat_unit,mat_location,mat_batch,mat_price','matCode,matName,matUnit,matLocation,matBatch,matPrice','wz_stock_pop')"/>
					  <label class="Validform_label" style="display: none;">物料编码</label>
					</td>
				  <td align="left">
					  	<input name="tWzCkItemList[0].matName" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="ignore" >
					  <label class="Validform_label" style="display: none;">物料名称</label>
					</td>
				  <td align="left">
					  	<input name="tWzCkItemList[0].matQty" oninput="countby1()" maxlength="32" type="text" class="inputxt"  style="width:120px;" 	  	datatype="*" ignore="checked" >
					  <label class="Validform_label" style="display: none;">数量</label>
					</td>
				  <td align="left">
					  	<input name="tWzCkItemList[0].matUnit" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="ignore" >
					  <label class="Validform_label" style="display: none;">单位</label>
					</td>
				  <td align="left">
					  	<input name="tWzCkItemList[0].matLocation" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="ignore" >
					  <label class="Validform_label" style="display: none;">仓库</label>
					</td>
				  <td align="left">
					  	<input name="tWzCkItemList[0].matBatch" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" >
					  <label class="Validform_label" style="display: none;">批次</label>
					</td>
				  <td align="left">
					  	<input name="tWzCkItemList[0].matPrice" oninput="countby1()" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" >
					  <label class="Validform_label" style="display: none;">单价</label>
					</td>
				  <td align="left">
					  	<input name="tWzCkItemList[0].itemRemark" maxlength="232" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" >
					  <label class="Validform_label" style="display: none;">备注</label>
				  </td>
				  <td align="left">
					 <input name="tWzCkItemList[0].matAmount" maxlength="232" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" >
					 <label class="Validform_label" style="display: none;">总价</label>
				  </td>
				  <td align="left">
					 <input name="tWzCkItemList[0].by2" maxlength="232" type="text" class="inputxt"  style="width:120px;"  ignore="ignore" >
					 <label class="Validform_label" style="display: none;">备注3</label>
				  </td>
   			</tr>
	</c:if>
	<c:if test="${fn:length(tWzCkItemList)  > 0 }">
		<c:forEach items="${tWzCkItemList}" var="poVal" varStatus="stuts">
			<tr>
				<td align="center"><div style="width: 25px;" name="xh">${stuts.index+1 }</div></td>
				<td align="center"><input style="width:20px;"  type="checkbox" name="ck" /></td>
						<input name="tWzCkItemList[${stuts.index }].id" type="hidden" value="${poVal.id }"/>
						<input name="tWzCkItemList[${stuts.index }].createName" type="hidden" value="${poVal.createName }"/>
						<input name="tWzCkItemList[${stuts.index }].createBy" type="hidden" value="${poVal.createBy }"/>
						<input name="tWzCkItemList[${stuts.index }].createDate" type="hidden" value="${poVal.createDate }"/>
						<input name="tWzCkItemList[${stuts.index }].updateName" type="hidden" value="${poVal.updateName }"/>
						<input name="tWzCkItemList[${stuts.index }].updateBy" type="hidden" value="${poVal.updateBy }"/>
						<input name="tWzCkItemList[${stuts.index }].updateDate" type="hidden" value="${poVal.updateDate }"/>
						<input name="tWzCkItemList[${stuts.index }].sysOrgCode" type="hidden" value="${poVal.sysOrgCode }"/>
						<input name="tWzCkItemList[${stuts.index }].sysCompanyCode" type="hidden" value="${poVal.sysCompanyCode }"/>
						<input name="tWzCkItemList[${stuts.index }].bpmStatus" type="hidden" value="${poVal.bpmStatus }"/>
						<input name="tWzCkItemList[${stuts.index }].wzckHid" type="hidden" value="${poVal.wzckHid }"/>
<%--						<input name="tWzCkItemList[${stuts.index }].matAmount" type="hidden" value="${poVal.matAmount }"/>--%>
<%--						<input name="tWzCkItemList[${stuts.index }].by1" type="hidden" value="${poVal.by1 }"/>--%>
						<input name="tWzCkItemList[${stuts.index }].by2" type="hidden" value="${poVal.by2 }"/>
						<input name="tWzCkItemList[${stuts.index }].by3" type="hidden" value="${poVal.by3 }"/>
						<input name="tWzCkItemList[${stuts.index }].by4" type="hidden" value="${poVal.by4 }"/>
						<input name="tWzCkItemList[${stuts.index }].by5" type="hidden" value="${poVal.by5 }"/>
				   <td align="left">
							 <input  id="tWzCkItemList[${stuts.index }].matCode" name="tWzCkItemList[${stuts.index }].matCode"  type="text" style="width: 150px" class="searchbox-inputtext"  		datatype="*" ignore="ignore"  onclick="popupClick(this,'mat_code,mat_name,mat_unit,mat_location,mat_batch,mat_price','matCode,matName,matUnit,matLocation,matBatch,matPrice','wz_stock_pop')"    value="${poVal.matCode }" />
					  <label class="Validform_label" style="display: none;">物料编码</label>
				   </td>
				   <td align="left">
					  	<input name="tWzCkItemList[${stuts.index }].matName" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="ignore"  value="${poVal.matName }"/>
					  <label class="Validform_label" style="display: none;">物料名称</label>
				   </td>
				   <td align="left">
					  	<input name="tWzCkItemList[${stuts.index }].matQty" oninput="countby1()" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="checked"  value="${poVal.matQty }"/>
					  <label class="Validform_label" style="display: none;">数量</label>
				   </td>
				   <td align="left">
					  	<input name="tWzCkItemList[${stuts.index }].matUnit" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="ignore"  value="${poVal.matUnit }"/>
					  <label class="Validform_label" style="display: none;">单位</label>
				   </td>
				   <td align="left">
					  	<input name="tWzCkItemList[${stuts.index }].matLocation" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="ignore"  value="${poVal.matLocation }"/>
					  <label class="Validform_label" style="display: none;">仓库</label>
				   </td>
				   <td align="left">
					  	<input name="tWzCkItemList[${stuts.index }].matBatch" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore"  value="${poVal.matBatch }"/>
					  <label class="Validform_label" style="display: none;">批次</label>
				   </td>
				   <td align="left">
					  	<input name="tWzCkItemList[${stuts.index }].matPrice" oninput="countby1()" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore"  value="${poVal.matPrice }"/>
					  <label class="Validform_label" style="display: none;">单价</label>
				   </td>
				   <td align="left">
					  	<input name="tWzCkItemList[${stuts.index }].itemRemark" maxlength="232" type="text" class="inputxt"  style="width:120px;"  ignore="ignore"  value="${poVal.itemRemark }"/>
					  <label class="Validform_label" style="display: none;">备注</label>
				   </td>
					<td align="left">
						<input name="tWzCkItemList[${stuts.index }].matAmount" maxlength="232" type="text" class="inputxt"  style="width:120px;"  ignore="ignore"  value="${poVal.matAmount }"/>
						<label class="Validform_label" style="display: none;">总价</label>
					</td>
					<td align="left">
						<input name="tWzCkItemList[${stuts.index }].by2" maxlength="232" type="text" class="inputxt"  style="width:120px;"  ignore="ignore"  value="${poVal.by2 }"/>
						<label class="Validform_label" style="display: none;">备注3</label>
					</td>
   			</tr>
		</c:forEach>
	</c:if>
	</tbody>
</table>
