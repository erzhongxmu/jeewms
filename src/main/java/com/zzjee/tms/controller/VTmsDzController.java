package com.zzjee.tms.controller;

import com.alibaba.fastjson.JSONArray;
import com.zzjee.tms.entity.VTmsDzEntity;
import com.zzjee.tms.service.VTmsDzServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.jwt.util.ResponseMessage;
import org.jeecgframework.jwt.util.Result;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**   
 * @Title: Controller  
 * @Description: v_tms_dz
 * @author onlineGenerator
 * @date 2018-08-08 01:31:13
 * @version V1.0   
 *
 */
@Api(value="VTmsDz",description="v_tms_dz",tags="vTmsDzController")
@Controller
@RequestMapping("/vTmsDzController")
public class VTmsDzController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(VTmsDzController.class);

	@Autowired
	private VTmsDzServiceI vTmsDzService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	


	/**
	 * v_tms_dz?????? ????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/tms/vTmsDzList");
	}

	/**
	 * easyui AJAX????????????
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(VTmsDzEntity vTmsDz, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(VTmsDzEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, vTmsDz, request.getParameterMap());
		try{
		//???????????????????????????
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.vTmsDzService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * ??????v_tms_dz
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(VTmsDzEntity vTmsDz, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		vTmsDz = systemService.getEntity(VTmsDzEntity.class, vTmsDz.getId());
		message = "v_tms_dz????????????";
		try{
			vTmsDzService.delete(vTmsDz);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "v_tms_dz????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * ????????????v_tms_dz
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "v_tms_dz????????????";
		try{
			for(String id:ids.split(",")){
				VTmsDzEntity vTmsDz = systemService.getEntity(VTmsDzEntity.class,
				id
				);
				vTmsDzService.delete(vTmsDz);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "v_tms_dz????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * ??????v_tms_dz
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(VTmsDzEntity vTmsDz, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "v_tms_dz????????????";
		try{
			vTmsDzService.save(vTmsDz);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "v_tms_dz????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * ??????v_tms_dz
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(VTmsDzEntity vTmsDz, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "v_tms_dz????????????";
		VTmsDzEntity t = vTmsDzService.get(VTmsDzEntity.class, vTmsDz.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(vTmsDz, t);
			vTmsDzService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "v_tms_dz????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * v_tms_dz??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(VTmsDzEntity vTmsDz, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(vTmsDz.getId())) {
			vTmsDz = vTmsDzService.getEntity(VTmsDzEntity.class, vTmsDz.getId());
			req.setAttribute("vTmsDzPage", vTmsDz);
		}
		return new ModelAndView("com/zzjee/tms/vTmsDz-add");
	}
	/**
	 * v_tms_dz??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(VTmsDzEntity vTmsDz, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(vTmsDz.getId())) {
			vTmsDz = vTmsDzService.getEntity(VTmsDzEntity.class, vTmsDz.getId());
			req.setAttribute("vTmsDzPage", vTmsDz);
		}
		return new ModelAndView("com/zzjee/tms/vTmsDz-update");
	}
	
	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","vTmsDzController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * ??????excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(VTmsDzEntity vTmsDz, HttpServletRequest request, HttpServletResponse response
			, DataGrid dataGrid, ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(VTmsDzEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, vTmsDz, request.getParameterMap());
		List<VTmsDzEntity> vTmsDzs = this.vTmsDzService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"v_tms_dz");
		modelMap.put(NormalExcelConstants.CLASS,VTmsDzEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("v_tms_dz??????", "?????????:"+ResourceUtil.getSessionUser().getRealName(),
			"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST,vTmsDzs);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * ??????excel ?????????
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(VTmsDzEntity vTmsDz, HttpServletRequest request, HttpServletResponse response
			, DataGrid dataGrid, ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"v_tms_dz");
    	modelMap.put(NormalExcelConstants.CLASS,VTmsDzEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("v_tms_dz??????", "?????????:"+ResourceUtil.getSessionUser().getRealName(),
    	"????????????"));
    	modelMap.put(NormalExcelConstants.DATA_LIST,new ArrayList());
    	return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	
	@SuppressWarnings("unchecked")
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
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<VTmsDzEntity> listVTmsDzEntitys = ExcelImportUtil.importExcel(file.getInputStream(),VTmsDzEntity.class,params);
				for (VTmsDzEntity vTmsDz : listVTmsDzEntitys) {
					vTmsDzService.save(vTmsDz);
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
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="v_tms_dz????????????",produces="application/json",httpMethod="GET")
	public ResponseMessage<List<VTmsDzEntity>> list() {
		List<VTmsDzEntity> listVTmsDzs=vTmsDzService.getList(VTmsDzEntity.class);
		return Result.success(listVTmsDzs);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="??????ID??????v_tms_dz??????",notes="??????ID??????v_tms_dz??????",httpMethod="GET",produces="application/json")
	public ResponseMessage<?> get(@ApiParam(required=true,name="id",value="ID")@PathVariable("id") String id) {
		VTmsDzEntity task = vTmsDzService.get(VTmsDzEntity.class, id);
		if (task == null) {
			return Result.error("??????ID??????v_tms_dz????????????");
		}
		return Result.success(task);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="??????v_tms_dz")
	public ResponseMessage<?> create(@ApiParam(name="v_tms_dz??????")@RequestBody VTmsDzEntity vTmsDz, UriComponentsBuilder uriBuilder) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<VTmsDzEntity>> failures = validator.validate(vTmsDz);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//??????
		try{
			vTmsDzService.save(vTmsDz);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("v_tms_dz??????????????????");
		}
		return Result.success(vTmsDz);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="??????v_tms_dz",notes="??????v_tms_dz")
	public ResponseMessage<?> update(@ApiParam(name="v_tms_dz??????")@RequestBody VTmsDzEntity vTmsDz) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<VTmsDzEntity>> failures = validator.validate(vTmsDz);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//??????
		try{
			vTmsDzService.saveOrUpdate(vTmsDz);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("??????v_tms_dz????????????");
		}

		//???Restful???????????????204?????????, ?????????. ???????????????200?????????.
		return Result.success("??????v_tms_dz????????????");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value="??????v_tms_dz")
	public ResponseMessage<?> delete(@ApiParam(name="id",value="ID",required=true)@PathVariable("id") String id) {
		logger.info("delete[{}]" + id);
		// ??????
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID????????????");
		}
		try {
			vTmsDzService.deleteEntityById(VTmsDzEntity.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("v_tms_dz????????????");
		}

		return Result.success();
	}
}
