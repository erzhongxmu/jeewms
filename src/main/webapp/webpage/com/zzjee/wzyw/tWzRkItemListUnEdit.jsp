<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<script type="text/javascript">
    $('#addTWzRkItemBtn').linkbutton({
        iconCls: 'icon-add'
    });
    $('#delTWzRkItemBtn').linkbutton({
        iconCls: 'icon-remove'
    });
    $('#addTWzRkItemBtn').bind('click', function(){
        var tr =  $("#add_tWzRkItem_table_template tr").clone();
        $("#add_tWzRkItem_table").append(tr);
        resetTrNum('add_tWzRkItem_table');
        return false;
    });
    $('#delTWzRkItemBtn').bind('click', function(){
        $("#add_tWzRkItem_table").find("input:checked").parent().parent().remove();
        resetTrNum('add_tWzRkItem_table');
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
    <a id="addTWzRkItemBtn" href="#">添加</a> <a id="delTWzRkItemBtn" href="#">删除</a>
</div>
<table border="0" cellpadding="2" cellspacing="0" id="tWzRkItem_table">
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
            入库数量
        </td>
        <td align="left" bgcolor="#EEEEEE" style="width: 126px;">
            物料单位
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
        <td align="left" bgcolor="#EEEEEE" style="width: 126px;">
            备注2
        </td>
        <td align="left" bgcolor="#EEEEEE" style="width: 126px;">
            备注3
        </td>
    </tr>
    <tbody id="add_tWzRkItem_table">
    <c:if test="${fn:length(tWzRkItemList)  <= 0 }">
        <tr>
            <td align="center"><div style="width: 25px;" name="xh">1</div></td>
            <td align="center"><input style="width:20px;"  type="checkbox" name="ck"/></td>
            <input name="tWzRkItemList[0].id" type="hidden"/>
            <input name="tWzRkItemList[0].createName" type="hidden"/>
            <input name="tWzRkItemList[0].createBy" type="hidden"/>
            <input name="tWzRkItemList[0].createDate" type="hidden"/>
            <input name="tWzRkItemList[0].updateName" type="hidden"/>
            <input name="tWzRkItemList[0].updateBy" type="hidden"/>
            <input name="tWzRkItemList[0].updateDate" type="hidden"/>
            <input name="tWzRkItemList[0].sysOrgCode" type="hidden"/>
            <input name="tWzRkItemList[0].sysCompanyCode" type="hidden"/>
            <input name="tWzRkItemList[0].bpmStatus" type="hidden"/>
            <input name="tWzRkItemList[0].wzrkHid" type="hidden"/>
            <input name="tWzRkItemList[0].matAmount" type="hidden"/>
            <input name="tWzRkItemList[0].by1" type="hidden"/>
            <input name="tWzRkItemList[0].by2" type="hidden"/>
            <input name="tWzRkItemList[0].by3" type="hidden"/>
            <input name="tWzRkItemList[0].by4" type="hidden"/>
            <input name="tWzRkItemList[0].by5" type="hidden"/>
            <input name="tWzRkItemList[0].by6" type="hidden"/>
            <td align="left">
                <input  id="tWzRkItemList[0].matCode" name="tWzRkItemList[0].matCode" type="text" style="width: 150px" class="searchbox-inputtext"  value="${poVal.matCode }"  	readonly="readonly"	datatype="*" ignore="checked"/>
                <label class="Validform_label" style="display: none;">物料编码</label>
            </td>
            <td align="left">
                <input name="tWzRkItemList[0].matName" maxlength="32" type="text" class="inputxt" readonly="readonly" style="width:120px;" 		datatype="*" ignore="checked" >
                <label class="Validform_label" style="display: none;">物料名称</label>
            </td>
            <td align="left">
                <input name="tWzRkItemList[0].matQty" maxlength="32" type="text" class="inputxt"  style="width:120px;" 	readonly="readonly"	datatype="*" ignore="checked" >
                <label class="Validform_label" style="display: none;">入库数量</label>
            </td>
            <td align="left">
                <input name="tWzRkItemList[0].matUnit" maxlength="32" type="text" class="inputxt"  style="width:120px;" 	readonly="readonly"	datatype="*" ignore="checked" >
                <label class="Validform_label" style="display: none;">物料单位</label>
            </td>
            <td align="left">
                <input name="tWzRkItemList[0].matLocation" maxlength="32" type="text" class="inputxt"  style="width:120px;" 	readonly="readonly"	datatype="*" ignore="checked" >
                <label class="Validform_label" style="display: none;">仓库</label>
            </td>
            <td align="left">
                <input name="tWzRkItemList[0].matBatch" maxlength="32" type="text" class="inputxt"  style="width:120px;"  readonly="readonly" ignore="ignore" >
                <label class="Validform_label" style="display: none;">批次</label>
            </td>
            <td align="left">
                <input name="tWzRkItemList[0].matPrice" maxlength="32" type="text" class="inputxt"  style="width:120px;" readonly="readonly" ignore="ignore" >
                <label class="Validform_label" style="display: none;">标准价</label>
            </td>
            <td align="left">
                <input name="tWzRkItemList[0].itemRemark" maxlength="32" type="text" class="inputxt"  style="width:120px;"  readonly="readonly" ignore="ignore" >
                <label class="Validform_label" style="display: none;">备注</label>
            </td>
            <td align="left">
                <input name="tWzRkItemList[0].by1" maxlength="32" type="text" class="inputxt"  style="width:120px;"  readonly="readonly" ignore="ignore" >
                <label class="Validform_label" style="display: none;">备注2</label>
            </td>
            <td align="left">
                <input name="tWzRkItemList[0].by2" maxlength="32" type="text" class="inputxt"  style="width:120px;"  readonly="readonly" ignore="ignore" >
                <label class="Validform_label" style="display: none;">备注3</label>
            </td>
        </tr>
    </c:if>
    <c:if test="${fn:length(tWzRkItemList)  > 0 }">
        <c:forEach items="${tWzRkItemList}" var="poVal" varStatus="stuts">
            <tr>
                <td align="center"><div style="width: 25px;" name="xh">${stuts.index+1 }</div></td>
                <td align="center"><input style="width:20px;"  type="checkbox" name="ck" /></td>
                <input name="tWzRkItemList[${stuts.index }].id" type="hidden" value="${poVal.id }"/>
                <input name="tWzRkItemList[${stuts.index }].createName" type="hidden" value="${poVal.createName }"/>
                <input name="tWzRkItemList[${stuts.index }].createBy" type="hidden" value="${poVal.createBy }"/>
                <input name="tWzRkItemList[${stuts.index }].createDate" type="hidden" value="${poVal.createDate }"/>
                <input name="tWzRkItemList[${stuts.index }].updateName" type="hidden" value="${poVal.updateName }"/>
                <input name="tWzRkItemList[${stuts.index }].updateBy" type="hidden" value="${poVal.updateBy }"/>
                <input name="tWzRkItemList[${stuts.index }].updateDate" type="hidden" value="${poVal.updateDate }"/>
                <input name="tWzRkItemList[${stuts.index }].sysOrgCode" type="hidden" value="${poVal.sysOrgCode }"/>
                <input name="tWzRkItemList[${stuts.index }].sysCompanyCode" type="hidden" value="${poVal.sysCompanyCode }"/>
                <input name="tWzRkItemList[${stuts.index }].bpmStatus" type="hidden" value="${poVal.bpmStatus }"/>
                <input name="tWzRkItemList[${stuts.index }].wzrkHid" type="hidden" value="${poVal.wzrkHid }"/>
                <input name="tWzRkItemList[${stuts.index }].matAmount" type="hidden" value="${poVal.matAmount }"/>
                <input name="tWzRkItemList[${stuts.index }].by1" type="hidden" value="${poVal.by1 }"/>
                <input name="tWzRkItemList[${stuts.index }].by2" type="hidden" value="${poVal.by2 }"/>
                <input name="tWzRkItemList[${stuts.index }].by3" type="hidden" value="${poVal.by3 }"/>
                <input name="tWzRkItemList[${stuts.index }].by4" type="hidden" value="${poVal.by4 }"/>
                <input name="tWzRkItemList[${stuts.index }].by5" type="hidden" value="${poVal.by5 }"/>
                <input name="tWzRkItemList[${stuts.index }].by6" type="hidden" value="${poVal.by6 }"/>
                <td align="left">
                    <input  id="tWzRkItemList[${stuts.index }].matCode" readonly="readonly" name="tWzRkItemList[${stuts.index }].matCode"  type="text" style="width: 150px"  class="inputxt"   		datatype="*" ignore="checked"  onclick="popupClick(this,'mat_code,mat_name,mat_unit,mat_location,mat_price','matCode,matName,matUnit,matLocation,matPrice','wz_material_pop')"    value="${poVal.matCode }" />
                    <label class="Validform_label" style="display: none;">物料编码</label>
                </td>
                <td align="left">
                    <input name="tWzRkItemList[${stuts.index }].matName"  readonly="readonly" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="checked"  value="${poVal.matName }"/>
                    <label class="Validform_label" style="display: none;">物料名称</label>
                </td>
                <td align="left">
                    <input name="tWzRkItemList[${stuts.index }].matQty" readonly="readonly" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="checked"  value="${poVal.matQty }"/>
                    <label class="Validform_label" style="display: none;">入库数量</label>
                </td>
                <td align="left">
                    <input name="tWzRkItemList[${stuts.index }].matUnit" readonly="readonly" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="checked"  value="${poVal.matUnit }"/>
                    <label class="Validform_label" style="display: none;">物料单位</label>
                </td>
                <td align="left">
                    <input name="tWzRkItemList[${stuts.index }].matLocation" readonly="readonly" maxlength="32" type="text" class="inputxt"  style="width:120px;" 		datatype="*" ignore="checked"  value="${poVal.matLocation }"/>
                    <label class="Validform_label" style="display: none;">仓库</label>
                </td>
                <td align="left">
                    <input name="tWzRkItemList[${stuts.index }].matBatch" readonly="readonly" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore"  value="${poVal.matBatch }"/>
                    <label class="Validform_label" style="display: none;">批次</label>
                </td>
                <td align="left">
                    <input name="tWzRkItemList[${stuts.index }].matPrice" readonly="readonly" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore"  value="${poVal.matPrice }"/>
                    <label class="Validform_label" style="display: none;">标准价</label>
                </td>
                <td align="left">
                    <input name="tWzRkItemList[${stuts.index }].itemRemark" readonly="readonly" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore"  value="${poVal.itemRemark }"/>
                    <label class="Validform_label" style="display: none;">备注</label>
                </td>
                <td align="left">
                    <input name="tWzRkItemList[${stuts.index }].by1" readonly="readonly" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore"  value="${poVal.by1 }"/>
                    <label class="Validform_label" style="display: none;">备注2</label>
                </td>
                <td align="left">
                    <input name="tWzRkItemList[${stuts.index }].by2" readonly="readonly" maxlength="32" type="text" class="inputxt"  style="width:120px;"  ignore="ignore"  value="${poVal.by2 }"/>
                    <label class="Validform_label" style="display: none;">备注3</label>
                </td>
            </tr>
        </c:forEach>
    </c:if>
    </tbody>
</table>