package org.jeecgframework.web.cgform.controller.generate;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jeecgframework.codegenerate.database.JeecgReadTable;
import org.jeecgframework.codegenerate.generate.CgformCodeGenerate;
import org.jeecgframework.codegenerate.generate.onetomany.CgformCodeGenerateOneToMany;
import org.jeecgframework.codegenerate.pojo.CreateFileProperty;
import org.jeecgframework.codegenerate.pojo.onetomany.CodeParamEntity;
import org.jeecgframework.codegenerate.pojo.onetomany.SubTableEntity;
import org.jeecgframework.codegenerate.util.CodeResourceUtil;
import org.jeecgframework.codegenerate.util.CodeStringUtils;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.enums.OnlineGenerateEnum;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.entity.button.CgformButtonEntity;
import org.jeecgframework.web.cgform.entity.button.CgformButtonSqlEntity;
import org.jeecgframework.web.cgform.entity.config.CgFormFieldEntity;
import org.jeecgframework.web.cgform.entity.config.CgFormHeadEntity;
import org.jeecgframework.web.cgform.entity.enhance.CgformEnhanceJavaEntity;
import org.jeecgframework.web.cgform.entity.enhance.CgformEnhanceJsEntity;
import org.jeecgframework.web.cgform.entity.generate.GenerateEntity;
import org.jeecgframework.web.cgform.entity.generate.GenerateSubListEntity;
import org.jeecgframework.web.cgform.service.build.DataBaseService;
import org.jeecgframework.web.cgform.service.button.CgformButtonServiceI;
import org.jeecgframework.web.cgform.service.button.CgformButtonSqlServiceI;
import org.jeecgframework.web.cgform.service.config.CgFormFieldServiceI;
import org.jeecgframework.web.cgform.service.enhance.CgformEnhanceJsServiceI;
import org.jeecgframework.web.cgform.service.impl.generate.TempletContextWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @Title:CgGenerateController
 * @description:???????????????????????????[????????????????????????+??????????????????->????????????]
 * @author ?????????
 * @date Sep 7, 2013 12:19:32 PM
 * @version V1.0
 */
@Controller
@RequestMapping("/generateController")
public class GenerateController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GenerateController.class);
	
	@Autowired
	private CgFormFieldServiceI cgFormFieldService;
	@Autowired
	private CgformButtonServiceI cgformButtonService;
	@Autowired
	private CgformButtonSqlServiceI cgformButtonSqlService;
	@Autowired
	private CgformEnhanceJsServiceI cgformEnhanceJsService;
	@Autowired
	private TempletContextWord templetContextWord;
	@Autowired
	private DataBaseService dataBaseService;
	/**
	 * ????????????????????????
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "gogenerate")
	public ModelAndView gogenerate( CgFormHeadEntity cgFormHead,HttpServletRequest request) {
		if (StringUtil.isNotEmpty(cgFormHead.getId())) {
			cgFormHead = cgFormFieldService.getEntity(
					CgFormHeadEntity.class, cgFormHead.getId());
		}else{
			throw new RuntimeException("?????????????????????");
		}
		String returnModelAndView = null;
		Map<String,String> entityNameMap = new HashMap<String,String>(0);
		if(cgFormHead.getJformType()==1 || cgFormHead.getJformType()==3){
			//???????????????????????????????????????????????????
			request.setAttribute("jspModeList", getOnlineGenerateEnum("single"));// ????????????
			returnModelAndView = "jeecg/cgform/generate/single";
		}else{
			//??????????????????????????????????????????
			List<CgFormHeadEntity> subTableList = new ArrayList<CgFormHeadEntity>();
			if(StringUtil.isNotEmpty(cgFormHead.getSubTableStr())){
				String[] subTables = cgFormHead.getSubTableStr().split(",");
				for(String subTable :subTables){
					CgFormHeadEntity subHead = cgFormFieldService.getCgFormHeadByTableName(subTable);
					subTableList.add(subHead);
					entityNameMap.put(subHead.getTableName(), JeecgReadTable.formatFieldCapital(subHead.getTableName()));
				}
			}
			request.setAttribute("jspModeList", getOnlineGenerateEnum("onetomany"));// ????????????
			request.setAttribute("subTableList", subTableList);
			returnModelAndView = "jeecg/cgform/generate/one2many";
		}
		String projectPath = CodeResourceUtil.getProject_path();
		try{
		    Cookie[] cookies=request.getCookies();
		    if(cookies!=null){
		    for(int i=0;i<cookies.length;i++){
		        if(cookies[i].getName().equals("cookie_projectPath")){
		        String value =  cookies[i].getValue();
		        if(value!=null&&!"".equals(value)){
		        	projectPath=cookies[i].getValue();
		        	projectPath= URLDecoder.decode(projectPath, "UTF-8"); 
		            }
		         }
		        request.setAttribute("projectPath",projectPath);
		    }
		    }
		}catch(Exception e){
		    e.printStackTrace();
		}
		String entityName = JeecgReadTable.formatFieldCapital(cgFormHead.getTableName());
		entityNameMap.put(cgFormHead.getTableName(), entityName);
		request.setAttribute("cgFormHeadPage", cgFormHead);
		request.setAttribute("entityNames",entityNameMap );
		return new ModelAndView(returnModelAndView);
	}
	
	private List<OnlineGenerateEnum> getOnlineGenerateEnum(String type){
		List<OnlineGenerateEnum> list = new ArrayList<OnlineGenerateEnum>();
		for(OnlineGenerateEnum item : OnlineGenerateEnum.values()) {
			if(item.getFormType().equals(type)) {
				list.add(item);
			}
		}
		return list;
	}
	/**
	 * ??????????????????-??????
	 * @param generateEntity
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(params = "dogenerate")
	public void dogenerate(CgFormHeadEntity cgFormHead,GenerateEntity generateEntity,CreateFileProperty createFileProperty,
			HttpServletRequest request,HttpServletResponse response) throws Exception {
		//step.1 ??????????????????????????????
		if (StringUtil.isNotEmpty(cgFormHead.getId())) {
			cgFormHead = cgFormFieldService.getEntity(
					CgFormHeadEntity.class, cgFormHead.getId());
			getCgformConfig(cgFormHead, generateEntity);
		}else{
			throw new RuntimeException("?????????????????????");
		}
		AjaxJson j =  new AjaxJson();
		String tableName = generateEntity.getTableName();
		String ftlDescription = generateEntity.getFtlDescription();
		try {
			//step.2 ?????????????????????
			boolean tableexist = new JeecgReadTable().checkTableExist(tableName);
			if(tableexist){

				OnlineGenerateEnum modeEnum = OnlineGenerateEnum.toEnum(createFileProperty.getJspMode());
				if(modeEnum!=null){
					if("system".equals(modeEnum.getVersion())){

						//step.3 ???????????????????????????????????????
						CgformCodeGenerate generate = new CgformCodeGenerate(createFileProperty,generateEntity);
						if(createFileProperty.getJspMode().equals("04")){
							String formhtml = templetContextWord.autoFormGenerateHtml(tableName, null, null);
							generate.setCgformJspHtml(formhtml);
						}

						//step.4 ?????????????????????
						generate.generateToFile();
					}else if("ext".equals(modeEnum.getVersion())){
						CgformCodeGenerate generate = new CgformCodeGenerate(createFileProperty,generateEntity);
						generate.generateToFileUserDefined();
					}
					j.setMsg(ftlDescription+"?????????????????????????????????????????????????????????????????????"+CodeStringUtils.getInitialSmall(generateEntity.getEntityName())+"Controller.do?list");
				}else{
					j.setMsg("???????????????????????????????????????");
				}

			}else{
				j.setMsg("???["+tableName+"] ???????????????????????????");
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
			j.setMsg(e1.getMessage());
			throw new RuntimeException(e1.getMessage());
		}
		try {
			String projectPath = URLEncoder.encode(generateEntity.getProjectPath(), "UTF-8");
			Cookie cookie = new Cookie("cookie_projectPath",projectPath );				
			cookie.setMaxAge(60*60*24*30); //cookie ??????30???
			response.addCookie(cookie);
			response.getWriter().print(j.getJsonStr());
			response.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				response.getWriter().close();
			} catch (Exception e2) {
			}
		}
	}
	
	
	
	
	/**
	 * ??????????????????-?????????
	 * @param generateEntity
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "dogenerateOne2Many")
	@ResponseBody
	public void dogenerateOne2Many(CodeParamEntity codeParamEntityIn,GenerateSubListEntity subTableListEntity,String jspMode,HttpServletRequest request,HttpServletResponse response){
		AjaxJson j =  new AjaxJson();
		//step.1 ????????????
		//??????????????????codeParamEntityIn
		//step.2 ??????????????????
		//??????????????????subTabParamIn,?????????????????????
		try{
			//step.3 ???????????????????????????????????????
			String mainTable = codeParamEntityIn.getTableName();
			//???????????????????????????
			GenerateEntity mainG = new GenerateEntity();
			mainG.setProjectPath(subTableListEntity.getProjectPath());
			mainG.setPackageStyle(subTableListEntity.getPackageStyle());
			CgFormHeadEntity mCgFormHead = cgFormFieldService.getCgFormHeadByTableName(mainTable);
			getCgformConfig(mCgFormHead, mainG);
			//step.4 ???????????????????????????????????????
			Map<String,GenerateEntity> subsG = new HashMap<String,GenerateEntity>();
			List<SubTableEntity>  subTabParamIn = subTableListEntity.getSubTabParamIn();
			for(SubTableEntity po:subTabParamIn){
				String sTableName = po.getTableName();
				CgFormHeadEntity cgSubHead = cgFormFieldService.getCgFormHeadByTableName(sTableName);
				List<CgFormFieldEntity> colums = cgSubHead.getColumns();
				String[] foreignKeys =getForeignkeys(colums);
				po.setForeignKeys(foreignKeys);
				GenerateEntity subG = new GenerateEntity();
				getCgformConfig(cgSubHead, subG);
				subG.setEntityName(po.getEntityName());
				subG.setEntityPackage(po.getEntityPackage());
				subG.setFieldRowNum(1);
				subG.setFtlDescription(po.getFtlDescription());
				subG.setForeignKeys(foreignKeys);
				subG.setTableName(po.getTableName());
				subG.setProjectPath(subTableListEntity.getProjectPath());
				subG.setPackageStyle(subTableListEntity.getPackageStyle());
				subsG.put(sTableName, subG);
			}
			codeParamEntityIn.setSubTabParam(subTabParamIn);

			OnlineGenerateEnum modeEnum = OnlineGenerateEnum.toEnum(jspMode);
			if(modeEnum!=null){
				if("system".equals(modeEnum.getVersion())){
					//step.5 ?????????(?????????)????????????,????????????

					if("06".equals(jspMode)){
						CgformCodeGenerateOneToMany.oneToManyCreateBootstap(subTabParamIn, codeParamEntityIn,mainG,subsG);
					}else{
						CgformCodeGenerateOneToMany.oneToManyCreate(subTabParamIn, codeParamEntityIn,mainG,subsG);
					}

					//j.setMsg("????????????????????????->?????????"+codeParamEntityIn.getFtlDescription());
				}else if("ext".equals(modeEnum.getVersion())){
					CgformCodeGenerateOneToMany.oneToManyCreateUserDefined(jspMode,subTabParamIn, codeParamEntityIn,mainG,subsG);
				}
				j.setMsg(codeParamEntityIn.getFtlDescription()+"?????????????????????????????????????????????????????????????????????"+CodeStringUtils.getInitialSmall(codeParamEntityIn.getEntityName())+"Controller.do?list");
			}else{
				j.setMsg("???????????????????????????????????????");
			}

		}catch (Exception e) {
			e.printStackTrace();
			j.setMsg(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		try {
			String projectPath = URLEncoder.encode(subTableListEntity.getProjectPath(), "UTF-8");
			Cookie cookie = new Cookie("cookie_projectPath",projectPath );						
			cookie.setMaxAge(60*60*24*30); //cookie ??????30???
			response.addCookie(cookie);
			response.getWriter().print(j.getJsonStr());
			response.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				response.getWriter().close();
			} catch (Exception e2) {
			}
		}
	}
	/**
	 * ????????????????????????????????????????????????????????????????????????????????????
	 * @param colums
	 * @return
	 */
	private String[] getForeignkeys(List<CgFormFieldEntity> colums) {
		List<String> fs = new ArrayList<String>(0);
		for(CgFormFieldEntity c : colums){
			if(StringUtil.isNotEmpty(c.getMainTable()) && StringUtil.isNotEmpty(c.getMainField())){
				fs.add(c.getFieldName().toUpperCase());
			}
		}
		String[] foreignkeys = (String[]) fs.toArray(new String[fs.size()]);
		return foreignkeys;
	}
	/**
	 * ?????????????????????????????????
	 * @param cgFormHead
	 * @param generateEntity
	 * @throws Exception 
	 */
	private void getCgformConfig(CgFormHeadEntity cgFormHead,
			GenerateEntity generateEntity) throws Exception {
		int filedNums = cgFormHead.getColumns().size();
		List<CgformButtonEntity> buttons = null;
		Map<String, String[]> buttonSqlMap = new LinkedHashMap<String, String[]>();
		//????????????
		cgFormHead = cgFormFieldService.getEntity(
				CgFormHeadEntity.class, cgFormHead.getId());
		//????????????
		buttons = cgformButtonService.getCgformButtonListByFormId(cgFormHead.getId());
		//??????SQL??????
		for(CgformButtonEntity cb:buttons){
			CgformButtonSqlEntity cbs = cgformButtonSqlService.getCgformButtonSqlByCodeFormId(cb.getButtonCode(), cgFormHead.getId());
			buttonSqlMap.put(cb.getButtonCode(), cbs==null?new String[]{}:cbs.getCgbSqlStr().replaceAll("(\r\n|\r|\n|\n\r)", "").split(";"));
		}
		CgformButtonSqlEntity cbsAdd = cgformButtonSqlService.getCgformButtonSqlByCodeFormId("add", cgFormHead.getId());
		buttonSqlMap.put("add", cbsAdd==null?new String[]{}:cbsAdd.getCgbSqlStr().replaceAll("(\r\n|\r|\n|\n\r)", "").split(";"));
		CgformButtonSqlEntity cbsUpdate = cgformButtonSqlService.getCgformButtonSqlByCodeFormId("update", cgFormHead.getId());
		buttonSqlMap.put("update", cbsUpdate==null?new String[]{}:cbsUpdate.getCgbSqlStr().replaceAll("(\r\n|\r|\n|\n\r)", "").split(";"));
		CgformButtonSqlEntity cbsDelete = cgformButtonSqlService.getCgformButtonSqlByCodeFormId("delete", cgFormHead.getId());
		buttonSqlMap.put("delete", cbsDelete==null?new String[]{}:cbsDelete.getCgbSqlStr().replaceAll("(\r\n|\r|\n|\n\r)", "").split(";"));
		//??????java??????
		Map<String, CgformEnhanceJavaEntity> buttonJavaMap = new LinkedHashMap<String, CgformEnhanceJavaEntity>();
		List<CgformEnhanceJavaEntity> javaList = dataBaseService.getCgformEnhanceJavaEntityByFormId(cgFormHead.getId());
		if(javaList!=null&&javaList.size()>0){
			for(CgformEnhanceJavaEntity e:javaList){
				if(StringUtil.isNotEmpty(e.getCgJavaValue())){
					buttonJavaMap.put(e.getButtonCode(), e);
				}
			}
		}
		
		//JS??????-??????
		CgformEnhanceJsEntity listJs = 	cgformEnhanceJsService.getCgformEnhanceJsByTypeFormId("list", cgFormHead.getId());
		CgformEnhanceJsEntity listJsCopy = null;
		try{
			listJsCopy = listJs.deepCopy();
		}catch (Exception e) {
			logger.debug(e.getMessage());
		}
		//JS??????-??????
		CgformEnhanceJsEntity formJs = 	cgformEnhanceJsService.getCgformEnhanceJsByTypeFormId("form", cgFormHead.getId());
		CgformEnhanceJsEntity formJsCopy = null;
		try{
			formJsCopy = formJs.deepCopy();
		}catch (Exception e) {
			logger.debug(e.getMessage());
		}
		//???js?????????online???????????? ?????????java??????
		for(CgFormFieldEntity field : cgFormHead.getColumns()){
			String fieldName = field.getFieldName();
			if(listJsCopy!=null){
				listJsCopy.setCgJsStr(listJsCopy.getCgJsStr().replace(fieldName, JeecgReadTable.formatField(fieldName)));
			}
			if(formJsCopy!=null){
				formJsCopy.setCgJsStr(formJsCopy.getCgJsStr().replace(fieldName, JeecgReadTable.formatField(fieldName)));
			}
		}
		generateEntity.setButtons(buttons);
		generateEntity.setButtonSqlMap(buttonSqlMap);
		generateEntity.setButtonJavaMap(buttonJavaMap);
		generateEntity.setCgFormHead(cgFormHead);
		generateEntity.setListJs(listJsCopy);
		generateEntity.setFormJs(formJsCopy);
	}
	/**
	 * ???????????????????????????
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "goFileTree")
	public ModelAndView goFileTree(HttpServletRequest request) {
		return new ModelAndView("jeecg/cgform/generate/fileTree");
	}
	/**
	 * ???????????????json
	 * @param parentNode
	 * @return
	 */
	@RequestMapping(params = "doExpandFileTree")
	@ResponseBody
	public Object doExpandFileTree(String parentNode){
		JSONArray fjson = new JSONArray();
		try{
			if(StringUtil.isEmpty(parentNode)){
				//??????????????????????????????
				File[] roots = File.listRoots();
				for(File r:roots){
					JSONObject item = new JSONObject();
					item.put("id", r.getAbsolutePath());
					item.put("text", r.getPath());
					item.put("iconCls", "icon-folder");
					if(hasDirs(r)){
						item.put("state", "closed");
					}else{
						item.put("state", "open");
					}
					fjson.add(item);
				}
			}else{
				try {
					parentNode =  new String(parentNode.getBytes("ISO-8859-1"), "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				//??????????????????
				File parent = new File(parentNode);
				File[] chs = parent.listFiles();
				for(File r:chs){
					JSONObject item = new JSONObject();
					if(r.isDirectory()){
						item.put("id", r.getAbsolutePath());
						item.put("text", r.getPath());
						if(hasDirs(r)){
							item.put("state", "closed");
						}else{
							item.put("state", "open");
						}
						fjson.add(item);
					}else{
						
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("????????????????????????");
		}
		return fjson;
	}
	private boolean hasDirs(File dir){
		try{
			if(dir.listFiles().length==0){
	//			item.put("state", "open");
				return false;
			}else{
	//			item.put("state", "closed");
				return true;
			}
		}catch (Exception e) {
			logger.info(e.getMessage());
			return false;
		}
	}
}
