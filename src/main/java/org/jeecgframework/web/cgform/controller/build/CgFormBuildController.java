package org.jeecgframework.web.cgform.controller.build;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.enums.SysThemesEnum;
import org.jeecgframework.core.online.util.FreemarkerHelper;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.SysThemesUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.web.cgform.common.CgAutoListConstant;
import org.jeecgframework.web.cgform.common.CommUtils;
import org.jeecgframework.web.cgform.engine.TempletContext;
import org.jeecgframework.web.cgform.entity.config.CgFormHeadEntity;
import org.jeecgframework.web.cgform.entity.template.CgformTemplateEntity;
import org.jeecgframework.web.cgform.entity.upload.CgUploadEntity;
import org.jeecgframework.web.cgform.exception.BusinessException;
import org.jeecgframework.web.cgform.service.build.DataBaseService;
import org.jeecgframework.web.cgform.service.config.CgFormFieldServiceI;
import org.jeecgframework.web.cgform.service.template.CgformTemplateServiceI;
import org.jeecgframework.web.cgform.util.PublicUtil;
import org.jeecgframework.web.cgform.util.TemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @ClassName: formBuildController
 * @Description: ???????????????????????????????????????????????????-???????????????????????????????????????
 * @author ?????????
 */
//@Scope("prototype")
@Controller
@RequestMapping("/cgFormBuildController")
public class CgFormBuildController extends BaseController {
	private static final Logger logger = Logger.getLogger(CgFormBuildController.class);
	@Autowired
	private TempletContext templetContext;
	@Autowired
	private DataBaseService dataBaseService;
	@Autowired
	private CgformTemplateServiceI cgformTemplateService;
	@Autowired
	private CgFormFieldServiceI cgFormFieldService;

	@RequestMapping(value = "ftlForm/{tableName}/goAdd")
	public void goAdd(@PathVariable("tableName") String tableName,HttpServletRequest request,HttpServletResponse response) {
		 ftlForm(tableName,"",request,response);
	}
	@RequestMapping(value = "ftlForm/{tableName}/goAddButton")
	public void goAddButton(@PathVariable("tableName") String tableName,HttpServletRequest request,HttpServletResponse response) {
		 ftlForm(tableName,"onbutton",request,response);
	}
	@RequestMapping(value = "ftlForm/{tableName}/goUpdate")
	public void goUpdate(@PathVariable("tableName") String tableName,HttpServletRequest request,HttpServletResponse response) {
		 ftlForm(tableName,"",request,response);
	}
	@RequestMapping(value = "ftlForm/{tableName}/goUpdateButton")
	public void goUpdateButton(@PathVariable("tableName") String tableName,HttpServletRequest request,HttpServletResponse response) {
		 ftlForm(tableName,"onbutton",request,response);
	}
	@RequestMapping(value = "ftlForm/{tableName}/goDetail")
	public void goDatilFtlForm(@PathVariable("tableName") String tableName,HttpServletRequest request,HttpServletResponse response) {
		 ftlForm(tableName,"read",request,response);
	}

	/**
	 * Online??????????????????????????????
	 */
	@RequestMapping(params = "mobileForm")
	public void mobileForm(HttpServletRequest request,HttpServletResponse response) {
		String tableName =request.getParameter("tableName");
		String sql = "select form_template_mobile from cgform_head where table_name = '"+tableName+"'";
		Map<String, Object> mp = cgFormFieldService.findOneForJdbc(sql);
		if(mp.containsKey("form_template_mobile") && oConvertUtils.isNotEmpty(mp.get("form_template_mobile"))){
			String urlTemplateName=request.getParameter("olstylecode");
			if(oConvertUtils.isEmpty(urlTemplateName)){
				request.setAttribute("olstylecode", mp.get("form_template_mobile").toString().trim());
			}
		}
		ftlForm(tableName,"",request,response);
		
	}

	/**
	 * form??????????????????
	 */
//	@SuppressWarnings("unchecked")
//	@RequestMapping(params = "ftlForm")
	private void ftlForm(String tableName,String mode,HttpServletRequest request,HttpServletResponse response) {
		try {
			long start = System.currentTimeMillis();
//			String tableName =request.getParameter("tableName");
	        Map<String, Object> data = new HashMap<String, Object>();
	        String id = request.getParameter("id");

//			String mode=request.getParameter("mode");

			String tablename = PublicUtil.replaceTableName(tableName);
			String templateName=tablename+"_";
			//String templateName=tableName+"_";

			TemplateUtil.TemplateType templateType=TemplateUtil.TemplateType.LIST;
			if(StringUtils.isBlank(id)){
				templateName+=TemplateUtil.TemplateType.ADD.getName();
				templateType=TemplateUtil.TemplateType.ADD;
			}else if("read".equals(mode)){
				templateName+=TemplateUtil.TemplateType.DETAIL.getName();
				templateType=TemplateUtil.TemplateType.DETAIL;
			}else{
				templateName+=TemplateUtil.TemplateType.UPDATE.getName();
				templateType=TemplateUtil.TemplateType.UPDATE;
			}
			//???????????????
	        String version = cgFormFieldService.getCgFormVersionByTableName(tableName);
	        //??????????????????
	    	Map configData = cgFormFieldService.getFtlFormConfig(tableName,version);
	    	data = new HashMap(configData);
	    	//??????????????????????????????????????????
	    	CgFormHeadEntity head = (CgFormHeadEntity)data.get("head");
	        Map<String, Object> dataForm = new HashMap<String, Object>();
	        if(StringUtils.isNotEmpty(id)){

	        	dataForm = dataBaseService.findOneForJdbc(tablename, id);
	        	//dataForm = dataBaseService.findOneForJdbc(tableName, id);

	        }
	        Iterator it=dataForm.entrySet().iterator();
		    while(it.hasNext()){
		    	Map.Entry entry=(Map.Entry)it.next();
		        String ok=(String)entry.getKey();
		        Object ov=entry.getValue();
		        data.put(ok, ov);
		    }
	        Map<String, Object> tableData  = new HashMap<String, Object>();
	        //?????????????????????????????????

	        tableData.put(tablename, dataForm);
	        //tableData.put(tableName, dataForm);

	        //???????????????????????????
	    	if(StringUtils.isNotEmpty(id)){
		    	if(head.getJformType()==CgAutoListConstant.JFORM_TYPE_MAIN_TALBE){
			    	String subTableStr = head.getSubTableStr();
			    	if(StringUtils.isNotEmpty(subTableStr)){
			    		 String [] subTables = subTableStr.split(",");
			    		 List<Map<String,Object>> subTableData = new ArrayList<Map<String,Object>>();
			    		 for(String subTable:subTables){
				    			subTableData = cgFormFieldService.getSubTableData(tableName,subTable,id);
				    			tableData.put(subTable, subTableData);
			    		 }
			    	}
		    	}
	    	}
	    	//????????????/(???????????????)????????????
	    	data.put("data", tableData);
	    	data.put("id", id);
	    	data.put("head", head);
	    	
	    	//????????????js??????
	    	data.put(CgAutoListConstant.CONFIG_IFRAME, getHtmlHead(request));
	    	//????????????????????????
	    	pushFiles(data, id);
	    	pushImages(data, id);
	    	
	    	//??????basePath
	    	String basePath = request.getContextPath();
	    	data.put(CgAutoListConstant.BASEPATH, basePath);
	    	
			String content =null;
			response.setContentType("text/html;charset=utf-8");

			String urlTemplateName = request.getParameter("olstylecode");

			if(oConvertUtils.isEmpty(urlTemplateName)){
				urlTemplateName = (String) request.getAttribute("olstylecode");
			}

			
			if(StringUtils.isNotBlank(urlTemplateName)){
				data.put("this_olstylecode",urlTemplateName);
				LogUtil.debug("-------------urlTemplateName-----------"+urlTemplateName);
				content=getUrlTemplate(urlTemplateName,templateType,data);
			}else{
				data.put("this_olstylecode",head.getFormTemplate());
				LogUtil.debug("-------------formTemplate-----------"+head.getFormTemplate());
				content=getTableTemplate(templateName,request,data);
			}

			response.getWriter().print(content);
			response.getWriter().flush();
			long end = System.currentTimeMillis();
			logger.debug("??????????????????????????????"+(end-start)+" ms");
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				response.getWriter().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * ??????url????????????
	 * @param templateName
	 * @param templateType
	 * @param dataMap
	 * @return
	 */
	private String getUrlTemplate(String templateName,TemplateUtil.TemplateType templateType,Map dataMap){
		String content=null;
		CgformTemplateEntity entity=cgformTemplateService.findByCode(templateName);
		if(entity!=null){
			FreemarkerHelper viewEngine = new FreemarkerHelper();

			dataMap.put("DictData", ApplicationContextUtil.getContext().getBean("dictDataTag"));

			content = viewEngine.parseTemplate(TemplateUtil.getTempletPath(entity,0, templateType), dataMap);
		}
		return content;
	}

	/**
	 * ???????????????????????????????????????
	 * @param templateName
	 * @param request
	 * @param data
	 * @return
	 */
	private String getTableTemplate(String templateName,HttpServletRequest request,Map data){
		StringWriter stringWriter = new StringWriter();
		BufferedWriter writer = new BufferedWriter(stringWriter);

		String ftlVersion =request.getParameter("ftlVersion");
//		String ftlVersion = oConvertUtils.getString(data.get("version"));

		Template template = templetContext.getTemplate(templateName, ftlVersion);
		try {

			template.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");  
			template.setDateFormat("yyyy-MM-dd");  
			template.setTimeFormat("HH:mm:ss");

			template.process(data, writer);
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringWriter.toString();
	}

	private String getHtmlHead(HttpServletRequest request){
		HttpSession session = ContextHolderUtils.getSession();
		String lang = (String)session.getAttribute("lang");
		StringBuilder sb= new StringBuilder("");
		SysThemesEnum sysThemesEnum = SysThemesUtil.getSysTheme(request);
		String basePath = request.getContextPath();
		sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/jquery/jquery-1.8.3.js\"></script>");
		sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/tools/dataformat.js\"></script>");
		sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\""+basePath+"/plug-in/accordion/css/accordion.css\">");
		sb.append(SysThemesUtil.getEasyUiTheme(sysThemesEnum,basePath));

		sb.append(SysThemesUtil.getEasyUiIconTheme(sysThemesEnum));
		//sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\""+basePath+"/plug-in/accordion/css/icons.css\">");

		sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/easyui/jquery.easyui.min.1.3.2.js\"></script>");
		sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/easyui/locale/zh-cn.js\"></script>");
		sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/tools/syUtil.js\"></script>");
		sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/My97DatePicker/WdatePicker.js\"></script>");
//		sb.append("<link rel=\"stylesheet\" href=\"plug-in/tools/css/common.css\" type=\"text/css\"></link>");
		//common.css
		sb.append(SysThemesUtil.getCommonTheme(sysThemesEnum,basePath));
//		sb.append("<script type=\"text/javascript\" src=\"plug-in/lhgDialog/lhgdialog.min.js\"></script>");
		sb.append(SysThemesUtil.getLhgdialogTheme(sysThemesEnum,basePath));

		sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/layer/layer.js\"></script>");

		sb.append(StringUtil.replace("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/tools/curdtools_{0}.js\"></script>", "{0}", lang));
		sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/tools/easyuiextend.js\"></script>");
		sb.append(SysThemesUtil.getEasyUiMainTheme(sysThemesEnum,basePath));
		sb.append("<link rel=\"stylesheet\" href=\""+basePath+"/plug-in/uploadify/css/uploadify.css\" type=\"text/css\"></link>");
		sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/uploadify/jquery.uploadify-3.1.js\"></script>");
		sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/tools/Map.js\"></script>");
		sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/Validform/js/Validform_v5.3.1_min_zh-cn.js\"></script>");
		sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/Validform/js/Validform_Datatype_zh-cn.js\"></script>");
		sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/Validform/js/datatype_zh-cn.js\"></script>");
		sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/Validform/plugin/passwordStrength/passwordStrength-min.js\"></script>");
		//style.css
		sb.append(SysThemesUtil.getValidformStyleTheme(sysThemesEnum,basePath));
		//tablefrom.css
		sb.append(SysThemesUtil.getValidformTablefrom(sysThemesEnum,basePath));

		//uedit
		sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/ueditor/ueditor.config.js\"></script>");
		sb.append("<script type=\"text/javascript\" src=\""+basePath+"/plug-in/ueditor/ueditor.all.js\"></script>");

		
		return sb.toString();
	}


	
	/**
	 * ?????????????????????????????????????????????????????????
	 * @param data ???????????????????????????
	 * @param id ???????????????????????????????????????
	 */
	private void pushFiles(Map<String, Object> data, String id) {
		List<CgUploadEntity> uploadBeans = cgFormFieldService.findByProperty(CgUploadEntity.class, "cgformId", id);
		List<Map<String,Object>> files = new ArrayList<Map<String,Object>>(0);
		for(CgUploadEntity b:uploadBeans){
			String title = b.getAttachmenttitle();//?????????
			String fileKey = b.getId();//????????????
			String path = b.getRealpath();//????????????
			String field = b.getCgformField();//????????????????????????????????????
			Map<String, Object> file = new HashMap<String, Object>();
			file.put("title", title);
			file.put("fileKey", fileKey);
			file.put("path", path);
			file.put("field", field==null?"":field);
			files.add(file);
		}
		data.put("filesList", files);
	}

	/**
	 * ?????????????????? ??????(??????),??????????????????????????????
	 * @param data ???????????????????????????
	 * @param id ????????????,????????????????????????
	 */
	private void pushImages(Map<String, Object> data, String id) {
		List<CgUploadEntity> uploadBeans = cgFormFieldService.findByProperty(CgUploadEntity.class, "cgformId", id);
		List<Map<String,Object>> images = new ArrayList<Map<String,Object>>(0);
		for(CgUploadEntity b:uploadBeans){
			String title = b.getAttachmenttitle();//?????????
			String fileKey = b.getId();//????????????
			String path = b.getRealpath();//????????????
			String field = b.getCgformField();//????????????????????????????????????
			Map<String, Object> image = new HashMap<String, Object>();
			image.put("title", title);
			image.put("fileKey", fileKey);
			image.put("path", path);
			image.put("field", field==null?"":field);
			images.add(image);
		}
		data.put("imageList", images);
	}

	/**
	 * ???????????????
	 * 
	 * @param jeecgDemo
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "saveOrUpdate")
	@ResponseBody
	public AjaxJson saveOrUpdate(HttpServletRequest request) throws Exception{
		String message = null;
		AjaxJson j = new AjaxJson();
		Map data = request.getParameterMap();
		if(data!=null){
			data = CommUtils.mapConvert(data);
			String tableName = (String)data.get("tableName");
			String id = (String)data.get("id");
			//????????????
		    Iterator it=data.entrySet().iterator();
		    while(it.hasNext()){
		    	Map.Entry entry=(Map.Entry)it.next();
		        Object ok=entry.getKey();
		        Object ov=entry.getValue()==null?"":entry.getValue();
		        logger.debug("name:"+ok.toString()+";value:"+ov.toString());
		    }
		    if(StringUtils.isEmpty(id)){
			    //????????????????????????
			    String [] filterName = {"tableName","saveOrUpdate"};
			    data = CommUtils.attributeMapFilter(data,filterName);
			    //???????????????
			    try {
			    	Object pkValue = null;
			    	pkValue = dataBaseService.getPkValue(tableName);
			    	data.put("id", pkValue);
			    	//--author???luobaoli---------date:20150615--------for: ??????service??????????????????
			    	try {
						dataBaseService.insertTable(tableName, data);
						j.setSuccess(true);
						message = "??????????????????";
			    	}catch (Exception e) {
			    		j.setSuccess(false);
						message = "??????????????????";
			    	}
			    	//--author???luobaoli---------date:20150615--------for: ??????service??????????????????
				} catch (Exception e) {
					e.printStackTrace();
					j.setSuccess(false);
					message = e.getMessage();
				}
			}else{
				//????????????????????????
			    String [] filterName = {"tableName","saveOrUpdate","id"};
			    data = CommUtils.attributeMapFilter(data,filterName);
			    //???????????????
			    try {
					int num = dataBaseService.updateTable(tableName, id, data);
					if (num>0) {
						j.setSuccess(true);
						message = "??????????????????";
					}else {
						j.setSuccess(false);
						message = "??????????????????";
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					j.setSuccess(false);
					message = e.getMessage();
				}
			}
		}
		j.setMsg(message);
		j.setObj(data);
		return j;
	}
	
	
	
	/**
	 * ???????????????
	 * 
	 * @param jeecgDemo
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "saveOrUpdateMore")
	@ResponseBody
	public AjaxJson saveOrUpdateMore(HttpServletRequest request) throws Exception{
		String message = null;
		AjaxJson j = new AjaxJson();
		Map data = request.getParameterMap();
		if(data!=null){
			data = CommUtils.mapConvert(data);
			String tableName = (String)data.get("tableName");
			String id = (String)data.get("id");
			//????????????
		    Iterator it=data.entrySet().iterator();
		    while(it.hasNext()){
		    	Map.Entry entry=(Map.Entry)it.next();
		        Object ok=entry.getKey();
		        Object ov=entry.getValue()==null?"":entry.getValue();
		        logger.debug("name:"+ok.toString()+";value:"+ov.toString());
		    }
		    Map<String,List<Map<String,Object>>> mapMore =CommUtils.mapConvertMore(data, tableName);
		    if(StringUtils.isEmpty(id)){
		    	logger.info("???????????????!!!!!");
		    	try {
		    		Map<String, Object> result = dataBaseService.insertTableMore(mapMore, tableName);
		    		data.put("id", result.get("id"));
		    		j.setSuccess(true);
					message = "????????????";
				} catch (BusinessException e) {
					e.printStackTrace();
					j.setSuccess(false);
					message = e.getMessage();
				}
		    	
			}else{
				logger.info("???????????????!!!!!");
				try {
					dataBaseService.updateTableMore(mapMore, tableName);
					j.setSuccess(true);
					message = "????????????";
				} catch (BusinessException e) {
					e.printStackTrace();
					j.setSuccess(false);
					message = e.getMessage();
				}
			}
		}
		j.setMsg(message);
		j.setObj(data);
		return j;
	}
	
	
	/**
	 * ????????????????????????????????????????????????
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "doButton")
	@ResponseBody
	public AjaxJson doButton(HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		try {
			String formId = request.getParameter("formId");
			String buttonCode = request.getParameter("buttonCode");
			String tableName = request.getParameter("tableName");
			String id = request.getParameter("id");
			Map<String,Object> data  = dataBaseService.findOneForJdbc(tableName, id);
			if(data!=null){
				//????????????
			    Iterator it=data.entrySet().iterator();
			    while(it.hasNext()){
			    	Map.Entry entry=(Map.Entry)it.next();
			        Object ok=entry.getKey();
			        Object ov=entry.getValue()==null?"":entry.getValue();
			        logger.debug("name:"+ok.toString()+";value:"+ov.toString());
			    }
				data = CommUtils.mapConvert(data);
				dataBaseService.executeSqlExtend(formId, buttonCode, data);

				dataBaseService.executeJavaExtend(formId, buttonCode, data);

			}
			j.setSuccess(true);
			message = "????????????";
		} catch (Exception e) {
			e.printStackTrace();
			message = "????????????";
		}
		j.setMsg(message);
		return j;
	}
	
}
