<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<#assign callbackFlag = false />
<#assign fileName = "" />
<#list pageColumns as callBackTestPo>
	<#if callBackTestPo.showType=='file'>
		<#assign callbackFlag = true />
	</#if>
</#list>
<html>
 <head>
  <title>${ftl_description}</title>
  <t:base type="jquery,easyui,tools,DatePicker"></t:base>
  <#if callbackFlag == true>
		<link rel="stylesheet" href="plug-in/uploadify/css/uploadify.css" type="text/css" />
		<script type="text/javascript" src="plug-in/uploadify/jquery.uploadify-3.1.js"></script>
  </#if>
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
  <t:formvalid formid="formobj" dialog="true" usePlugin="password" layout="table" tiptype="1" action="${entityName?uncap_first}Controller.do?doUpdate" ${callbackFlag?string("callback=\"jeecgFormFileCallBack@Override\"", "")}>
			<#list columns as po>
				<#if po.isShow == 'N'>
					<input id="${po.fieldName}" name="${po.fieldName}" type="hidden" value="${'$'}{${entityName?uncap_first}Page.${po.fieldName} }">
				</#if>
			</#list>
	<table cellpadding="0" cellspacing="1" class="formtable">
		<#list pageColumns as po>
		<#if po_index%2==0>
		<tr>
		</#if>
			<td align="right">
				<label class="Validform_label">${po.content}:</label>
			</td>
			<td class="value">
			<#if po.showType=='text'>
		     	 <input id="${po.fieldName}" name="${po.fieldName}" type="text" style="width: 150px" class="inputxt" <#if po.fieldValidType?if_exists?html != ''> datatype="${po.fieldValidType?if_exists?html}" <#else><#if po.type == 'int'>datatype="n" <#elseif po.type=='double'>datatype="/^(-?\d+)(\.\d+)?$/" <#else><#if po.isNull != 'Y'>datatype="*" </#if></#if></#if>
		     	 <#-- update--begin--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
				<#if po.fieldMustInput??><#if po.fieldMustInput == 'Y' || po.isNull != 'Y'>ignore="checked"<#else>ignore="ignore"</#if></#if>
				<#-- update--end--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
		     	 value='${'$'}{${entityName?uncap_first}Page.${po.fieldName}}'>
			<#elseif po.showType=='popup'>
			<input id="${po.fieldName}" name="${po.fieldName}" type="text" style="width: 150px" class="searchbox-inputtext"<#if po.fieldValidType?if_exists?html != ''> datatype="${po.fieldValidType?if_exists?html}" <#else><#if po.type == 'int'>datatype="n" <#elseif po.type=='double'>datatype="/^(-?\d+)(\.\d+)?$/" <#else><#if po.isNull != 'Y'>datatype="*" </#if></#if></#if>
			<#-- update--begin--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
			<#if po.fieldMustInput??><#if po.fieldMustInput == 'Y' || po.isNull != 'Y'>ignore="checked"<#else>ignore="ignore"</#if></#if>
			<#-- update--end--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
			<#if po.dictTable?if_exists?html!=""> onclick="inputClick(this,'${po.dictField}','${po.dictTable}')"</#if> value='${'$'}{${entityName?uncap_first}Page.${po.fieldName}}'>			    
		     <#elseif po.showType=='textarea'>
				 <textarea id="${po.fieldName}" style="width:600px;" class="inputxt" rows="6" name="${po.fieldName}"
				 <#-- update--begin--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
				<#if po.fieldMustInput??><#if po.fieldMustInput == 'Y' || po.isNull != 'Y'>ignore="checked"<#else>ignore="ignore"</#if></#if>
				<#-- update--end--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
				 >${'$'}{${entityName?uncap_first}Page.${po.fieldName}}</textarea>
		      <#elseif po.showType=='password'>
		      	<input id="${po.fieldName}" name="${po.fieldName}" type="password" style="width: 150px" class="inputxt"  
				      	<#-- update--begin--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
						<#if po.fieldMustInput??><#if po.fieldMustInput == 'Y' || po.isNull != 'Y'>ignore="checked"<#else>ignore="ignore"</#if></#if>
						<#-- update--end--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
		      						<#if po.fieldValidType?if_exists?html != ''> datatype="${po.fieldValidType?if_exists?html}"<#else><#if po.type == 'int'> datatype="n"<#elseif po.type=='double'> datatype="/^(-?\d+)(\.\d+)?$/"<#else><#if po.isNull != 'Y'>datatype="*"</#if></#if></#if> value='${'$'}{${entityName?uncap_first}Page.${po.fieldName}}'>
				<#elseif po.showType=='radio' || po.showType=='select' || po.showType=='checkbox' || po.showType=='list'>	 
					<t:dictSelect field="${po.fieldName}" type="${po.showType?if_exists?html}" 
						 <#if po.isNull != 'Y'>datatype="*"</#if>
						<#if po.dictTable?if_exists?html != ''>dictTable="${po.dictTable?if_exists?html}" dictField="${po.dictField?if_exists?html}" dictText="${po.dictText?if_exists?html}"<#else>typeGroupCode="${po.dictField}"</#if> defaultVal="${'$'}{${entityName?uncap_first}Page.${po.fieldName}}" hasLabel="false"  title="${po.content}"></t:dictSelect>     
				<#elseif po.showType=='date'>
					  <input id="${po.fieldName}" name="${po.fieldName}" type="text" style="width: 150px" 
					  <#-- update--begin--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
									<#if po.fieldMustInput??><#if po.fieldMustInput == 'Y' || po.isNull != 'Y'>ignore="checked"<#else>ignore="ignore"</#if></#if>
									<#-- update--end--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
		      						class="Wdate" onClick="WdatePicker()" <#if po.fieldValidType?if_exists?html != ''> datatype="${po.fieldValidType?if_exists?html}"<#else><#if po.isNull != 'Y'>datatype="*"</#if></#if> value='<fmt:formatDate value='${'$'}{${entityName?uncap_first}Page.${po.fieldName}}' type="date" pattern="yyyy-MM-dd"/>'>
		      	<#elseif po.showType=='datetime'>
					  <input id="${po.fieldName}" name="${po.fieldName}" type="text" style="width: 150px" 
					  <#-- update--begin--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
									<#if po.fieldMustInput??><#if po.fieldMustInput == 'Y' || po.isNull != 'Y'>ignore="checked"<#else>ignore="ignore"</#if></#if>
									<#-- update--end--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
		      						 class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" <#if po.fieldValidType?if_exists?html != ''> datatype="${po.fieldValidType?if_exists?html}"<#else><#if po.isNull != 'Y'>datatype="*"</#if></#if> value='<fmt:formatDate value='${'$'}{${entityName?uncap_first}Page.${po.fieldName}}' type="date" pattern="yyyy-MM-dd hh:mm:ss"/>'>
				<#elseif po.showType=='file'>
					<table id="fileTable"></table>
					<#if !(po.operationCodesReadOnly ??)>
						<#assign fileName = "${po.fieldName}" />
						<table></table>
						<script type="text/javascript">
							var serverMsg="";
							$(function(){
								$('#${po.fieldName}').uploadify({
									buttonText:'????????????',
									auto:false,
									progressData:'speed',
									multi:true,
									height:25,
									overrideEvents:['onDialogClose'],
									fileTypeDesc:'????????????:',
									queueID:'filediv_file',
									<#-- fileTypeExts:'*.rar;*.zip;*.doc;*.docx;*.txt;*.ppt;*.xls;*.xlsx;*.html;*.htm;*.pdf;*.jpg;*.gif;*.png',   ???????????????????????? 20170317 scott -->
									fileSizeLimit:'15MB',
									swf:'plug-in/uploadify/uploadify.swf',	
									uploader:'cgUploadController.do?saveFiles&jsessionid='+$("#sessionUID").val()+'',
									onUploadStart : function(file) { 
										var cgFormId=$("input[name='id']").val();
										$('#${po.fieldName}').uploadify("settings", "formData", {
											'cgFormId':cgFormId,
											'cgFormName':'${tableName}',
											'cgFormField':'${fieldMeta[po.fieldName]}'
										});
									} ,
									onQueueComplete : function(queueData) {
										 var win = frameElement.api.opener;
										 win.reloadTable();
										 win.tip(serverMsg);
										 frameElement.api.close();
									},
									onUploadSuccess : function(file, data, response) {
										var d=$.parseJSON(data);
										if(d.success){
											var win = frameElement.api.opener;
											serverMsg = d.msg;
										}
									},
									onFallback: function() {
					                    tip("????????????FLASH???????????????????????????????????????FLASH???????????????")
					                },
					                onSelectError: function(file, errorCode, errorMsg) {
					                    switch (errorCode) {
					                    case - 100 : tip("????????????????????????????????????????????????" + $('#file').uploadify('settings', 'queueSizeLimit') + "????????????");
					                        break;
					                    case - 110 : tip("?????? [" + file.name + "] ???????????????????????????" + $('#file').uploadify('settings', 'fileSizeLimit') + "?????????");
					                        break;
					                    case - 120 : tip("?????? [" + file.name + "] ???????????????");
					                        break;
					                    case - 130 : tip("?????? [" + file.name + "] ??????????????????");
					                        break;
					                    }
					                },
					                onUploadProgress: function(file, bytesUploaded, bytesTotal, totalBytesUploaded, totalBytesTotal) {}
								});
							});
						</script>
						<span id="file_uploadspan"><input type="file" name="${po.fieldName}" id="${po.fieldName}" /></span> 
						<div class="form" id="filediv_file"></div>
					</#if>
				<#--update-start--Author: jg_huangxg  Date:20160421 for???TASK #1027 ???online?????????????????????????????????UE????????? -->
				<#elseif po.showType='umeditor'>
					<script type="text/javascript"  charset="utf-8" src="plug-in/ueditor/ueditor.config.js"></script>
					<script type="text/javascript"  charset="utf-8" src="plug-in/ueditor/ueditor.all.min.js"></script>
			    	<textarea name="${po.fieldName}" id="${po.fieldName}" style="width: 650px;height:300px"></textarea>
				    <script type="text/javascript">
				        var editor = UE.getEditor('${po.fieldName}');
				    </script>
				<#--update-end--Author: jg_huangxg  Date:20160421 for???TASK #1027 ???online?????????????????????????????????UE????????? -->
		      	<#else>
		      		<input id="${po.fieldName}" name="${po.fieldName}" type="text" style="width: 150px" class="inputxt"  
		      						<#if po.fieldValidType?if_exists?html != ''> datatype="${po.fieldValidType?if_exists?html}"<#else><#if po.type == 'int'> datatype="n"<#elseif po.type=='double'> datatype="/^(-?\d+)(\.\d+)?$/"<#else><#if po.isNull != 'Y'>datatype="*"</#if></#if></#if> value='${'$'}{${entityName?uncap_first}Page.${po.fieldName}}'>
				</#if>
				<span class="Validform_checktip"></span>
				<label class="Validform_label" style="display: none;">${po.content?if_exists?html}</label>
			</td>
		<#if (po_index+1)%2==0>
		</tr>
		<#else>
		<#if !po_has_next>
		</tr>
		</#if>
		</#if>
	</#list>
			</table>
			<div style="width: auto;height: 200px;">
				<%-- ????????????div???????????????????????????????????????????????? --%>
				<div style="width:800px;height:1px;"></div>
				<t:tabs id="tt" iframe="false" tabPosition="top" fit="false">
				<#list subTab as sub>
				 <t:tab href="${entityName?uncap_first}Controller.do?${sub.entityName?uncap_first}List<#list sub.foreignKeys as key><#if key?lower_case?index_of("${jeecg_table_id}")!=-1>&${jeecg_table_id}=${"$"}{${entityName?uncap_first}Page.${jeecg_table_id}}<#else>&${key?uncap_first}=${"$"}{${entityName?uncap_first}Page.${key?uncap_first}}</#if></#list>" icon="icon-search" title="${sub.ftlDescription}" id="${sub.entityName?uncap_first}"></t:tab>
				</#list>
				</t:tabs>
			</div>
			</t:formvalid>
			<!-- ?????? ???????????? ?????? -->
		<table style="display:none">
		<#list subTab as sub>
		<tbody id="add_${sub.entityName?uncap_first}_table_template">
			<tr>
			 <td align="center"><div style="width: 25px;" name="xh"></div></td>
			 <td align="center"><input style="width:20px;" type="checkbox" name="ck"/></td>
			 <#list subPageColumnsMap[sub.tableName] as po>
				 <#assign check = 0 >
				  <#list sub.foreignKeys as key>
				  <#if subFieldMeta[po.fieldName]==key?uncap_first>
				  <#assign check = 1 >
				  <#break>
				  </#if>
				  </#list>
				  <#if check==0>
				  <td align="left">
					  <#if po.showType == "text">
					  	<input name="${sub.entityName?uncap_first}List[#index#].${po.fieldName}" maxlength="${po.length?c}" 
					  		type="text" class="inputxt"  style="width:120px;"
					  		<#-- update--begin--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
									<#if po.fieldMustInput??><#if po.fieldMustInput == 'Y' || po.isNull != 'Y'>ignore="checked"<#else>ignore="ignore"</#if></#if>
									<#-- update--end--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
					  		<#if po.fieldValidType?if_exists?html != ''> datatype="${po.fieldValidType?if_exists?html}"<#else><#if po.type == 'int'> datatype="n"<#elseif po.type=='double'> datatype="/^(-?\d+)(\.\d+)?$/"<#else><#if po.isNull != 'Y'>datatype="*"</#if></#if></#if>>
						<#elseif po.showType=='password'>
							<input name="${sub.entityName?uncap_first}List[#index#].${po.fieldName}" maxlength="${po.length?c}" 
					  		type="password" class="inputxt"  style="width:120px;"
					  		<#-- update--begin--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
									<#if po.fieldMustInput??><#if po.fieldMustInput == 'Y' || po.isNull != 'Y'>ignore="checked"<#else>ignore="ignore"</#if></#if>
									<#-- update--end--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
					  		<#if po.fieldValidType?if_exists?html != ''> datatype="${po.fieldValidType?if_exists?html}"<#else><#if po.type == 'int'> datatype="n"<#elseif po.type=='double'> datatype="/^(-?\d+)(\.\d+)?$/"<#else> <#if po.isNull != 'Y'>datatype="*"</#if></#if></#if>>
						<#elseif po.showType=='radio' || po.showType=='select' || po.showType=='checkbox' || po.showType=='list'>
							<t:dictSelect field="${sub.entityName?uncap_first}List[#index#].${po.fieldName}" type="${po.showType?if_exists?html}" <#if po.isNull != 'Y'>datatype="*"</#if>
										<#if po.dictTable?if_exists?html != ''>dictTable="${po.dictTable?if_exists?html}" dictField="${po.dictField?if_exists?html}" dictText="${po.dictText?if_exists?html}"<#else>typeGroupCode="${po.dictField}"</#if> defaultVal="" hasLabel="false"  title="${po.content}"></t:dictSelect>     
						<#elseif po.showType=='date'>
							<input name="${sub.entityName?uncap_first}List[#index#].${po.fieldName}" maxlength="${po.length?c}" 
					  		type="text" class="Wdate" onClick="WdatePicker()"  style="width:120px;"
					  		<#-- update--begin--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
									<#if po.fieldMustInput??><#if po.fieldMustInput == 'Y' || po.isNull != 'Y'>ignore="checked"<#else>ignore="ignore"</#if></#if>
									<#-- update--end--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
					  		<#if po.fieldValidType?if_exists?html != ''> datatype="${po.fieldValidType?if_exists?html}"<#else> <#if po.isNull != 'Y'>datatype="*"</#if></#if>>
					      <#elseif po.showType=='datetime'>
					      	<input name="${sub.entityName?uncap_first}List[#index#].${po.fieldName}" maxlength="${po.length?c}" 
						  		<#-- update--begin--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
									<#if po.fieldMustInput??><#if po.fieldMustInput == 'Y' || po.isNull != 'Y'>ignore="checked"<#else>ignore="ignore"</#if></#if>
									<#-- update--end--author:zhangjiaqiang Date:20170414 for:????????????????????? -->
						  		type="text"  class="Wdate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"  style="width:120px;" <#if po.fieldValidType?if_exists?html != ''> datatype="${po.fieldValidType?if_exists?html}"<#else> <#if po.isNull != 'Y'>datatype="*"</#if></#if>>
					        <#elseif po.showType=='file'>
										<input type="hidden" id="${sub.entityName?uncap_first}List[#index#].${po.fieldName}" name="${sub.entityName?uncap_first}List[#index#].${po.fieldName}" />
										<a  target="_blank" id="${sub.entityName?uncap_first}List[#index#].${po.fieldName}_href">?????????</a>
									   <br>
									   <input class="ui-button" type="button" value="????????????"
													onclick="commonUpload(commonUploadDefaultCallBack,'${sub.entityName?uncap_first}List\\[#index#\\]\\.${po.fieldName}')"/>
					       <#else>
					       	<input name="${sub.entityName?uncap_first}List[#index#].${po.fieldName}" maxlength="${po.length?c}" 
						  		type="text" class="inputxt"  style="width:120px;" <#if po.fieldValidType?if_exists?html != ''> datatype="${po.fieldValidType?if_exists?html}"<#else><#if po.type == 'int'> datatype="n"<#elseif po.type=='double'> datatype="/^(-?\d+)(\.\d+)?$/"<#else> <#if po.isNull != 'Y'>datatype="*"</#if></#if></#if>>
					  </#if>
					  <label class="Validform_label" style="display: none;">${po.content?if_exists?html}</label>
				  </td>
				  </#if>
              </#list>
			</tr>
		 </tbody>
		 </#list>
		</table>
 </body>
 <script src = "webpage/${bussiPackage?replace('.','/')}/${entityPackage}/${entityName?uncap_first}.js"></script>	
 <#if callbackFlag == true>
  	<script type="text/javascript">
	  	//?????? ???????????? ??????
	  	$(function(){
	  		var table = $("#fileTable");
	  		var cgFormId=$("input[name='id']").val();
	  		$.ajax({
	  		   type: "post",
	  		   url: "${entityName?uncap_first}Controller.do?getFiles&id=" +  cgFormId,
	  		   success: function(data){
	  			 var arrayFileObj = jQuery.parseJSON(data).obj;
	  			 
	  			$.each(arrayFileObj,function(n,file){
	  				var tr = $("<tr style=\"height:34px;\"></tr>");
	  				var td_title = $("<td>" + file.title + "</td>")
	  		  		var td_download = $("<td><a href=\"commonController.do?viewFile&fileid=" + file.fileKey + "&subclassname=org.jeecgframework.web.cgform.entity.upload.CgUploadEntity\" title=\"??????\">??????</a></td>")
	  		  		var td_view = $("<td><a href=\"javascript:void(0);\" onclick=\"openwindow('??????','commonController.do?openViewFile&fileid=" + file.fileKey + "&subclassname=org.jeecgframework.web.cgform.entity.upload.CgUploadEntity','fList',700,500)\">??????</a></td>");
	  		  		var td_del = $("<td><a href=\"javascript:void(0)\" class=\"jeecgDetail\" onclick=\"del('cgUploadController.do?delFile&id=" + file.fileKey + "',this)\">??????</a></td>");
	  		  		
	  		  		tr.appendTo(table);
	  		  		td_title.appendTo(tr);
	  		  		td_download.appendTo(tr);
	  		  		td_view.appendTo(tr);
	  		  		td_del.appendTo(tr);
	  			 });
	  		   }
	  		});
	  	})
  		function jeecgFormFileCallBack(data){
  			if (data.success == true) {
				uploadFile(data);
			} else {
				if (data.responseText == '' || data.responseText == undefined) {
					$.messager.alert('??????', data.msg);
					$.Hidemsg();
				} else {
					try {
						var emsg = data.responseText.substring(data.responseText.indexOf('????????????'), data.responseText.indexOf('????????????'));
						$.messager.alert('??????', emsg);
						$.Hidemsg();
					} catch(ex) {
						$.messager.alert('??????', data.responseText + '');
					}
				}
				return false;
			}
			if (!neibuClickFlag) {
				var win = frameElement.api.opener;
				win.reloadTable();
			}
  		}
  		function upload() {
			$('#${fileName}').uploadify('upload', '*');		
		}
		
		var neibuClickFlag = false;
		function neibuClick() {
			neibuClickFlag = true; 
			$('#btn_sub').trigger('click');
		}
		function cancel() {
			$('#${fileName}').uploadify('cancel', '*');
		}
		function uploadFile(data){
			if(!$("input[name='id']").val()){
				if(data.obj!=null && data.obj!='undefined'){
					$("input[name='id']").val(data.obj.id);
				}
			}
			if($(".uploadify-queue-item").length>0){
				upload();
			}else{
				if (neibuClickFlag){
					alert(data.msg);
					neibuClickFlag = false;
				}else {
					var win = frameElement.api.opener;
					win.reloadTable();
					win.tip(data.msg);
					frameElement.api.close();
				}
			}
		}
  	</script>
</#if>