<#setting number_format="0.#####################">
<!DOCTYPE html>
<html lang="zh-CN">
<head>
	<base href="${basePath}/"/>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>jeecg</title>
	<meta name="description" content="">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="${basePath}/online/template/ledefault/css/vendor.css">
	<link rel="stylesheet" href="${basePath}/online/template/ledefault/css/bootstrap-theme.css">
	<link rel="stylesheet" href="${basePath}/online/template/ledefault/css/bootstrap.css">
	<link rel="stylesheet" href="${basePath}/online/template/ledefault/css/app.css">
	  
	<link rel="stylesheet" href="${basePath}/plug-in/Validform/css/metrole/style.css" type="text/css"/>
	<link rel="stylesheet" href="${basePath}/plug-in/Validform/css/metrole/tablefrom.css" type="text/css"/>
	<script type="text/javascript" src="${basePath}/plug-in/jquery/jquery-1.8.3.js"></script>
	<script type="text/javascript" src="${basePath}/plug-in/tools/dataformat.js"></script>
	<script type="text/javascript" src="${basePath}/plug-in/easyui/jquery.easyui.min.1.3.2.js"></script>
	<script type="text/javascript" src="${basePath}/plug-in/easyui/locale/zh-cn.js"></script>
	<script type="text/javascript" src="${basePath}/plug-in/tools/syUtil.js"></script>
	<script type="text/javascript" src="${basePath}/plug-in/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${basePath}/plug-in/lhgDialog/lhgdialog.min.js"></script>
	<script type="text/javascript" src="${basePath}/plug-in/tools/curdtools_zh-cn.js"></script>
	<script type="text/javascript" src="${basePath}/plug-in/tools/easyuiextend.js"></script>
	<script type="text/javascript" src="${basePath}/plug-in/Validform/js/Validform_v5.3.1_min_zh-cn.js"></script>
	<script type="text/javascript" src="${basePath}/plug-in/Validform/js/Validform_Datatype_zh-cn.js"></script>
	<script type="text/javascript" src="${basePath}/plug-in/Validform/js/datatype_zh-cn.js"></script>
	<script type="text/javascript" src="${basePath}/plug-in/Validform/plugin/passwordStrength/passwordStrength-min.js"></script>
	<script type="text/javascript"  charset="utf-8" src="${basePath}/plug-in/ueditor/ueditor.config.js"></script>
  	<script type="text/javascript"  charset="utf-8" src="${basePath}/plug-in/ueditor/ueditor.all.min.js"></script>
	
</head>


<script type="text/javascript">
	$(document).ready(function(){
		//$('#tt').tabs({
		//   onSelect:function(title){
		//       $('#tt .panel-body').css('width','auto');
		//	}
		//});
		//$(".tabs-wrap").css('width','100%');
		$("#jform_tab .con-wrapper").hide(); //Hide all tab content  
		$("#jform_tab li:first").addClass("active").show(); //Activate first tab  
		$("#jform_tab .con-wrapper:first").show(); //Show first tab content
	 
	 
		//On Click Event  
		$("#jform_tab li").click(function() {  
	        $("#jform_tab li").removeClass("active"); //Remove any "active" class  
	        $(this).addClass("active"); //Add "active" class to selected tab  
	        var activeTab = $(this).find("a").attr("href"); //Find the rel attribute value to identify the active tab + content
	        $("#jform_tab .con-wrapper").hide(); //Hide all tab content  
	        $(activeTab).fadeIn(); //Fade in the active content
	        //$(""+activeTab).show();   
	        return false;  
		});  
	});
	
	//???????????????
	function resetTrNum(tableId) {
		$tbody = $("#"+tableId+"");
		$tbody.find('>tr').each(function(i){
			$(':input, select', this).each(function(){
				var $this = $(this), name = $this.attr('name'), val = $this.val();
				if(name!=null){
					if (name.indexOf("#index#") >= 0){
						$this.attr("name",name.replace('#index#',i));
					}else{
						var s = name.indexOf("[");
						var e = name.indexOf("]");
						var new_name = name.substring(s+1,e);
						$this.attr("name",name.replace(new_name,i));
					}
				}
			});
			$(this).find('div[name=\'xh\']').html(i+1);
		});
	}
</script>
<body>
	<form id="formobj" action="${basePath}/cgFormBuildController.do?saveOrUpdateMore" name="formobj" method="post">
	<input type="hidden" id="btn_sub" class="btn_sub"/>
	
	<script type="text/javascript">
		$(function() {
		    //?????????????????????,???????????????????????????????????????
		    if(location.href.indexOf("goDetail.do")!=-1){
				$(".jeecgDetail").hide();
			}
			
			if(location.href.indexOf("goDetail.do")!=-1){
				//????????????????????????
				$("#formobj").find(":input").attr("disabled","disabled");
			}
			if(location.href.indexOf("goAddButton.do")!=-1||location.href.indexOf("goUpdateButton.do")!=-1){
				//??????????????????????????????
				$("#sub_tr").show();
			}
		});
		function upload() {
		<#list columns as po>
	  		<#if po.show_type=='file'>
	  		$('#${po.field_name}').uploadify('upload', '*');		
	  		</#if>
	  	</#list>
		}
		var neibuClickFlag = false;
		function neibuClick() {
		    neibuClickFlag = true;
		    $('#btn_sub').trigger('click');
		}
		function cancel() {
		<#list columns as po>
	  		<#if po.show_type=='file'>
			$('#${po.field_name}').uploadify('cancel', '*');
	 	 	</#if>
	  	</#list>
		}
		function uploadFile(data) {
		    if (!$("input[name='id']").val()) {
		        if (data.obj != null && data.obj != 'undefined') {
		            $("input[name='id']").val(data.obj.id);
		        }
		    }
		    if ($(".uploadify-queue-item").length > 0) {
		        upload();
		    } else {
		        if (neibuClickFlag) {
		            alert(data.msg);
		            neibuClickFlag = false;
		        } else {
		            var win = frameElement.api.opener;
		            win.reloadTable();
		            win.tip(data.msg);
		            frameElement.api.close();
		        }
		    }
		}
		$.dialog.setting.zIndex = 1990;
		function del(url, obj) {
		    $.dialog.confirm("?????????????????????????",
		    function() {
		        $.ajax({
		            async: false,
		            cache: false,
		            type: 'POST',
		            url: url,
		            // ?????????action??????
		            error: function() { // ????????????????????????
		            },
		            success: function(data) {
		                var d = $.parseJSON(data);
		                if (d.success) {
		                    var msg = d.msg;
		                    tip(msg);
		                    $(obj).closest("tr").hide("slow");
		                }
		            }
		        });
		    },
		    function() {});
		}
	</script>

	<div id="jform_tab" class="tab-wrapper">
		<!-- tab -->
    	<ul class="nav nav-tabs">
    		<li role="presentation">
				<a href="#con-wrapper0">??????????????????</a>
			</li>
			<#assign subTableStr>${head.subTableStr?if_exists?html}</#assign>
			<#assign subtablelist=subTableStr?split(",")>
			<#list subtablelist as sub >
				<#if field['${sub}']?exists >
					<li role="presentation">
						<a href="#con-wrapper${sub_index + 1}">${field['${sub}'].head.content?if_exists?html}</a>
					</li>
				</#if>
			</#list>
		</ul>
    	
    	<#include "online/template/o2mTableStyle/html/jformhead.ftl">
		<#assign subTableStr>${head.subTableStr?if_exists?html}</#assign>
		<#assign subtablelist=subTableStr?split(",")>
		<#list subtablelist as sub >
			<#if field['${sub}']?exists >
				<#if field['${sub}'].head.relationType==1 >
					<#include "online/template/o2mTableStyle/html/jformonetoone.ftl">
				<#else>
					<#include "online/template/o2mTableStyle/html/jformonetomany.ftl">
				</#if>
			</#if>
		</#list>
	</div>
	<div style="width: auto;height: 200px;">
		<div style="width:690px;height:1px;"></div>
		<div id="tt" tabPosition="top" border=flase style="margin:0px;padding:0px;overflow:hidden;width:auto;" class="easyui-tabs" fit="false"></div>
	</div>
	<div align="center"  id = "sub_tr" style="display: none;" >
		<input type="button" value="??????" onclick="$('#btn_sub').trigger('click');" class="ui_state_highlight">
	</div>
	<script type="text/javascript">
		$(function() {
		    $("#formobj").Validform({
		        tiptype: 1,
		        btnSubmit: "#btn_sub",
		        btnReset: "#btn_reset",
		        ajaxPost: true,
		        usePlugin: {
		            passwordstrength: {
		                minLen: 6,
		                maxLen: 18,
		                trigger: function(obj, error) {
		                    if (error) {
		                        obj.parent().next().find(".Validform_checktip").show();
		                        obj.find(".passwordStrength").hide();
		                    } else {
		                        $(".passwordStrength").show();
		                        obj.parent().next().find(".Validform_checktip").hide();
		                    }
		                }
		            }
		        },
		        callback: function(data) {
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
		    });
		});
	</script>
</form>
	<!-- ?????? ???????????? ?????? -->
	<table style="display:none">
		<#assign subTableStr>${head.subTableStr?if_exists?html}</#assign>
		<#assign subtablelist=subTableStr?split(",")>
		<#list subtablelist as sub >
		    <#if field['${sub}']?exists >
				<#if field['${sub}'].head.relationType!=1 >
				    <#include "online/template/o2mTableStyle/html/jformonetomanytpl.ftl">
			    </#if>
			</#if>
		</#list>
	</table>
	<script type="text/javascript">${js_plug_in?if_exists}</script>	
</body>
</html>