package com.zzjee.md.controller;
import com.zzjee.md.entity.TMdBomHeadEntity;
import com.zzjee.md.service.TMdBomHeadServiceI;
import com.zzjee.md.page.TMdBomHeadPage;
import com.zzjee.md.entity.TMdBomItemEntity;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

 import org.jeecgframework.jwt.util.ResponseMessage;
import org.jeecgframework.jwt.util.Result;
import org.apache.commons.lang3.StringUtils;
import com.alibaba.fastjson.JSONArray;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.jwt.util.ResponseMessage;
import org.jeecgframework.jwt.util.Result;
import com.alibaba.fastjson.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**   
 * @Title: Controller
 * @Description: BOM??????
 * @author onlineGenerator
 * @date 2018-05-05 12:56:55
 * @version V1.0   
 *
 */
@Api(value="TMdBomHead",description="BOM??????",tags="tMdBomHeadController")
@Controller
@RequestMapping("/tMdBomHeadController")
public class TMdBomHeadController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TMdBomHeadController.class);

	@Autowired
	private TMdBomHeadServiceI tMdBomHeadService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;

	/**
	 * BOM???????????? ????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/md/tMdBomHeadList");
	}

	/**
	 * easyui AJAX????????????
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(TMdBomHeadEntity tMdBomHead,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TMdBomHeadEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tMdBomHead);
		try{
		//???????????????????????????
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.tMdBomHeadService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * ??????BOM??????
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(TMdBomHeadEntity tMdBomHead, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		tMdBomHead = systemService.getEntity(TMdBomHeadEntity.class, tMdBomHead.getId());
		String message = "BOM??????????????????";
		try{
			tMdBomHeadService.delMain(tMdBomHead);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "BOM??????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * ????????????BOM??????
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		String message = "BOM??????????????????";
		try{
			for(String id:ids.split(",")){
				TMdBomHeadEntity tMdBomHead = systemService.getEntity(TMdBomHeadEntity.class,
				id
				);
				tMdBomHeadService.delMain(tMdBomHead);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "BOM??????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * ??????BOM??????
	 * 
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(TMdBomHeadEntity tMdBomHead,TMdBomHeadPage tMdBomHeadPage, HttpServletRequest request) {
		List<TMdBomItemEntity> tMdBomItemList =  tMdBomHeadPage.getTMdBomItemList();
		AjaxJson j = new AjaxJson();
		String message = "????????????";
		try{
			tMdBomHeadService.addMain(tMdBomHead, tMdBomItemList);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "BOM??????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	/**
	 * ??????BOM??????
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(TMdBomHeadEntity tMdBomHead,TMdBomHeadPage tMdBomHeadPage, HttpServletRequest request) {
		List<TMdBomItemEntity> tMdBomItemList =  tMdBomHeadPage.getTMdBomItemList();
		AjaxJson j = new AjaxJson();
		String message = "????????????";
		try{
			tMdBomHeadService.updateMain(tMdBomHead, tMdBomItemList);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "??????BOM????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * BOM????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(TMdBomHeadEntity tMdBomHead, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tMdBomHead.getId())) {
			tMdBomHead = tMdBomHeadService.getEntity(TMdBomHeadEntity.class, tMdBomHead.getId());
			req.setAttribute("tMdBomHeadPage", tMdBomHead);
		}
		return new ModelAndView("com/zzjee/md/tMdBomHead-add");
	}
	
	/**
	 * BOM????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(TMdBomHeadEntity tMdBomHead, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tMdBomHead.getId())) {
			tMdBomHead = tMdBomHeadService.getEntity(TMdBomHeadEntity.class, tMdBomHead.getId());
			req.setAttribute("tMdBomHeadPage", tMdBomHead);
		}
		return new ModelAndView("com/zzjee/md/tMdBomHead-update");
	}
	
	
	/**
	 * ??????????????????[BOM??????]
	 * 
	 * @return
	 */
	@RequestMapping(params = "tMdBomItemList")
	public ModelAndView tMdBomItemList(TMdBomHeadEntity tMdBomHead, HttpServletRequest req) {
	
		//===================================================================================
		//????????????
		Object id0 = tMdBomHead.getId();
		//===================================================================================
		//??????-BOM??????
	    String hql0 = "from TMdBomItemEntity where 1 = 1 AND bOMID = ? ";
	    try{
	    	List<TMdBomItemEntity> tMdBomItemEntityList = systemService.findHql(hql0,id0);
			req.setAttribute("tMdBomItemList", tMdBomItemEntityList);
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		return new ModelAndView("com/zzjee/md/tMdBomItemList");
	}

    /**
    * ??????excel
    *
    * @param request
    * @param response
    */
    @RequestMapping(params = "exportXls")
    public String exportXls(TMdBomHeadEntity tMdBomHead,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid,ModelMap map) {
    	CriteriaQuery cq = new CriteriaQuery(TMdBomHeadEntity.class, dataGrid);
    	//?????????????????????
    	org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tMdBomHead);
    	try{
    	//???????????????????????????
    	}catch (Exception e) {
    		throw new BusinessException(e.getMessage());
    	}
    	cq.add();
    	List<TMdBomHeadEntity> list=this.tMdBomHeadService.getListByCriteriaQuery(cq, false);
    	List<TMdBomHeadPage> pageList=new ArrayList<TMdBomHeadPage>();
        if(list!=null&&list.size()>0){
        	for(TMdBomHeadEntity entity:list){
        		try{
        		TMdBomHeadPage page=new TMdBomHeadPage();
        		   MyBeanUtils.copyBeanNotNull2Bean(entity,page);
            	    Object id0 = entity.getId();
				    String hql0 = "from TMdBomItemEntity where 1 = 1 AND bOMID = ? ";
        	        List<TMdBomItemEntity> tMdBomItemEntityList = systemService.findHql(hql0,id0);
            		page.setTMdBomItemList(tMdBomItemEntityList);
            		pageList.add(page);
            	}catch(Exception e){
            		logger.info(e.getMessage());
            	}
            }
        }
        map.put(NormalExcelConstants.FILE_NAME,"BOM??????");
        map.put(NormalExcelConstants.CLASS,TMdBomHeadPage.class);
        map.put(NormalExcelConstants.PARAMS,new ExportParams("BOM????????????", "?????????:Jeecg",
            "????????????"));
        map.put(NormalExcelConstants.DATA_LIST,pageList);
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

    /**
	 * ??????excel????????????
	 * @param request
	 * @param
	 * @return
	 */
	@RequestMapping(params = "importExcel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson importExcel(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// ????????????????????????
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(2);
			params.setNeedSave(true);
			try {
				List<TMdBomHeadPage> list =  ExcelImportUtil.importExcel(file.getInputStream(), TMdBomHeadPage.class, params);
				TMdBomHeadEntity entity1=null;
				for (TMdBomHeadPage page : list) {
					entity1=new TMdBomHeadEntity();
					MyBeanUtils.copyBeanNotNull2Bean(page,entity1);
		            tMdBomHeadService.addMain(entity1, page.getTMdBomItemList());
				}
				j.setMsg("?????????????????????");
			} catch (Exception e) {
				j.setMsg("?????????????????????");
				logger.error(ExceptionUtil.getExceptionMessage(e));
			}finally{
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			}
			return j;
	}
	/**
	* ??????excel ?????????
	*/
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(ModelMap map) {
		map.put(NormalExcelConstants.FILE_NAME,"BOM??????");
		map.put(NormalExcelConstants.CLASS,TMdBomHeadPage.class);
		map.put(NormalExcelConstants.PARAMS,new ExportParams("BOM????????????", "?????????:"+ ResourceUtil.getSessionUser().getRealName(),
		"????????????"));
		map.put(NormalExcelConstants.DATA_LIST,new ArrayList());
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	* ??????????????????
	*
	* @return
	*/
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name", "tMdBomHeadController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

 	
 	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="BOM??????????????????",produces="application/json",httpMethod="GET")
	public ResponseMessage<List<TMdBomHeadPage>> list() {
		List<TMdBomHeadEntity> list= tMdBomHeadService.getList(TMdBomHeadEntity.class);
    	List<TMdBomHeadPage> pageList=new ArrayList<TMdBomHeadPage>();
        if(list!=null&&list.size()>0){
        	for(TMdBomHeadEntity entity:list){
        		try{
        			TMdBomHeadPage page=new TMdBomHeadPage();
        		   MyBeanUtils.copyBeanNotNull2Bean(entity,page);
					Object id0 = entity.getId();
				     String hql0 = "from TMdBomItemEntity where 1 = 1 AND bOMID = ? ";
	    			List<TMdBomItemEntity> tMdBomItemOldList = this.tMdBomHeadService.findHql(hql0,id0);
            		page.setTMdBomItemList(tMdBomItemOldList);
            		pageList.add(page);
            	}catch(Exception e){
            		logger.info(e.getMessage());
            	}
            }
        }
		return Result.success(pageList);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="??????ID??????BOM????????????",notes="??????ID??????BOM????????????",httpMethod="GET",produces="application/json")
	public ResponseMessage<?> get(@ApiParam(required=true,name="id",value="ID")@PathVariable("id") String id) {
		TMdBomHeadEntity task = tMdBomHeadService.get(TMdBomHeadEntity.class, id);
		if (task == null) {
			return Result.error("??????ID??????BOM??????????????????");
		}
		TMdBomHeadPage page = new TMdBomHeadPage();
		try {
			MyBeanUtils.copyBeanNotNull2Bean(task, page);
				Object id0 = task.getId();
		    String hql0 = "from TMdBomItemEntity where 1 = 1 AND bOMID = ? ";
			List<TMdBomItemEntity> tMdBomItemOldList = this.tMdBomHeadService.findHql(hql0,id0);
    		page.setTMdBomItemList(tMdBomItemOldList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.success(page);
	}
 	
 	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="??????BOM??????")
	public ResponseMessage<?> create(@ApiParam(name="BOM????????????")@RequestBody TMdBomHeadPage tMdBomHeadPage, UriComponentsBuilder uriBuilder) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<TMdBomHeadPage>> failures = validator.validate(tMdBomHeadPage);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//??????
		List<TMdBomItemEntity> tMdBomItemList =  tMdBomHeadPage.getTMdBomItemList();
		
		TMdBomHeadEntity tMdBomHead = new TMdBomHeadEntity();
		try{
			MyBeanUtils.copyBeanNotNull2Bean(tMdBomHeadPage,tMdBomHead);
		}catch(Exception e){
            logger.info(e.getMessage());
            return Result.error("??????BOM????????????");
        }
		tMdBomHeadService.addMain(tMdBomHead, tMdBomItemList);

		return Result.success(tMdBomHead);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="??????BOM??????",notes="??????BOM??????")
	public ResponseMessage<?> update(@RequestBody TMdBomHeadPage tMdBomHeadPage) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<TMdBomHeadPage>> failures = validator.validate(tMdBomHeadPage);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//??????
		List<TMdBomItemEntity> tMdBomItemList =  tMdBomHeadPage.getTMdBomItemList();
		
		TMdBomHeadEntity tMdBomHead = new TMdBomHeadEntity();
		try{
			MyBeanUtils.copyBeanNotNull2Bean(tMdBomHeadPage,tMdBomHead);
		}catch(Exception e){
            logger.info(e.getMessage());
            return Result.error("BOM??????????????????");
        }
		tMdBomHeadService.updateMain(tMdBomHead, tMdBomItemList);

		//???Restful???????????????204?????????, ?????????. ???????????????200?????????.
		return Result.success();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value="??????BOM??????")
	public ResponseMessage<?> delete(@ApiParam(name="id",value="ID",required=true)@PathVariable("id") String id) {
		logger.info("delete[{}]" + id);
		// ??????
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID????????????");
		}
		try {
			TMdBomHeadEntity tMdBomHead = tMdBomHeadService.get(TMdBomHeadEntity.class, id);
			tMdBomHeadService.delMain(tMdBomHead);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("BOM??????????????????");
		}

		return Result.success();
	}
}
