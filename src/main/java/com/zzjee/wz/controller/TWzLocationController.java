package com.zzjee.wz.controller;

import com.alibaba.fastjson.JSONArray;
import com.zzjee.wz.entity.TWzLocationEntity;
import com.zzjee.wz.service.TWzLocationServiceI;
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
 * @Description: ??????
 * @author onlineGenerator
 * @date 2018-05-20 21:41:04
 * @version V1.0
 *
 */
@Api(value="TWzLocation",description="????????????",tags="tWzLocationController")
@Controller
@RequestMapping("/tWzLocationController")
public class TWzLocationController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TWzLocationController.class);

	@Autowired
	private TWzLocationServiceI tWzLocationService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;



	/**
	 * ???????????? ????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/wz/tWzLocationList");
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
	public void datagrid(TWzLocationEntity tWzLocation,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TWzLocationEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tWzLocation, request.getParameterMap());
		try{
		//???????????????????????????
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.tWzLocationService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * ????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(TWzLocationEntity tWzLocation, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		tWzLocation = systemService.getEntity(TWzLocationEntity.class, tWzLocation.getId());
		message = "??????????????????";
		try{
			tWzLocationService.delete(tWzLocation);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "??????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * ??????????????????
	 *
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "??????????????????";
		try{
			for(String id:ids.split(",")){
				TWzLocationEntity tWzLocation = systemService.getEntity(TWzLocationEntity.class,
				Integer.parseInt(id)
				);
				tWzLocationService.delete(tWzLocation);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "??????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * ????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(TWzLocationEntity tWzLocation, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "??????????????????";
		try{
			tWzLocationService.save(tWzLocation);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "??????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * ????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(TWzLocationEntity tWzLocation, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "??????????????????";
		TWzLocationEntity t = tWzLocationService.get(TWzLocationEntity.class, tWzLocation.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(tWzLocation, t);
			tWzLocationService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "??????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * ????????????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(TWzLocationEntity tWzLocation, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tWzLocation.getId())) {
			tWzLocation = tWzLocationService.getEntity(TWzLocationEntity.class, tWzLocation.getId());
			req.setAttribute("tWzLocationPage", tWzLocation);
		}
		return new ModelAndView("com/zzjee/wz/tWzLocation-add");
	}
	/**
	 * ????????????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(TWzLocationEntity tWzLocation, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tWzLocation.getId())) {
			tWzLocation = tWzLocationService.getEntity(TWzLocationEntity.class, tWzLocation.getId());
			req.setAttribute("tWzLocationPage", tWzLocation);
		}
		return new ModelAndView("com/zzjee/wz/tWzLocation-update");
	}

	/**
	 * ??????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","tWzLocationController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * ??????excel
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(TWzLocationEntity tWzLocation, HttpServletRequest request, HttpServletResponse response
			, DataGrid dataGrid, ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(TWzLocationEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tWzLocation, request.getParameterMap());
		List<TWzLocationEntity> tWzLocations = this.tWzLocationService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"??????");
		modelMap.put(NormalExcelConstants.CLASS,TWzLocationEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("????????????", "?????????:"+ ResourceUtil.getSessionUser().getRealName(),
			"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST,tWzLocations);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * ??????excel ?????????
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(TWzLocationEntity tWzLocation, HttpServletRequest request, HttpServletResponse response
			, DataGrid dataGrid, ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"??????");
    	modelMap.put(NormalExcelConstants.CLASS,TWzLocationEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("????????????", "?????????:"+ ResourceUtil.getSessionUser().getRealName(),
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
				List<TWzLocationEntity> listTWzLocationEntitys = ExcelImportUtil.importExcel(file.getInputStream(),TWzLocationEntity.class,params);
				for (TWzLocationEntity tWzLocation : listTWzLocationEntitys) {
					tWzLocationService.save(tWzLocation);
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
	@ApiOperation(value="??????????????????",produces="application/json",httpMethod="GET")
	public ResponseMessage<List<TWzLocationEntity>> list() {
		List<TWzLocationEntity> listTWzLocations=tWzLocationService.getList(TWzLocationEntity.class);
		return Result.success(listTWzLocations);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="??????ID??????????????????",notes="??????ID??????????????????",httpMethod="GET",produces="application/json")
	public ResponseMessage<?> get(@ApiParam(required=true,name="id",value="ID")@PathVariable("id") String id) {
		TWzLocationEntity task = tWzLocationService.get(TWzLocationEntity.class, id);
		if (task == null) {
			return Result.error("??????ID????????????????????????");
		}
		return Result.success(task);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="????????????")
	public ResponseMessage<?> create(@ApiParam(name="????????????")@RequestBody TWzLocationEntity tWzLocation, UriComponentsBuilder uriBuilder) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<TWzLocationEntity>> failures = validator.validate(tWzLocation);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//??????
		try{
			tWzLocationService.save(tWzLocation);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("????????????????????????");
		}
		return Result.success(tWzLocation);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="????????????",notes="????????????")
	public ResponseMessage<?> update(@ApiParam(name="????????????")@RequestBody TWzLocationEntity tWzLocation) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<TWzLocationEntity>> failures = validator.validate(tWzLocation);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//??????
		try{
			tWzLocationService.saveOrUpdate(tWzLocation);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("????????????????????????");
		}

		//???Restful???????????????204?????????, ?????????. ???????????????200?????????.
		return Result.success("????????????????????????");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value="????????????")
	public ResponseMessage<?> delete(@ApiParam(name="id",value="ID",required=true)@PathVariable("id") String id) {
		logger.info("delete[{}]" + id);
		// ??????
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID????????????");
		}
		try {
			tWzLocationService.deleteEntityById(TWzLocationEntity.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("??????????????????");
		}

		return Result.success();
	}
}
