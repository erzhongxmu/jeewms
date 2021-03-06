package com.zzjee.wz.controller;

import com.alibaba.fastjson.JSONArray;
import com.zzjee.wz.entity.TWzSpConfEntity;
import com.zzjee.wz.service.TWzSpConfServiceI;
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
import org.jeecgframework.web.system.pojo.base.TSUser;
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
 * @Description: ????????????
 * @author onlineGenerator
 * @date 2018-05-20 21:40:51
 * @version V1.0
 *
 */
@Api(value="TWzSpConf",description="????????????",tags="tWzSpConfController")
@Controller
@RequestMapping("/tWzSpConfController")
public class TWzSpConfController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TWzSpConfController.class);

	@Autowired
	private TWzSpConfServiceI tWzSpConfService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;



	/**
	 * ?????????????????? ????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/wz/tWzSpConfList");
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
	public void datagrid(TWzSpConfEntity tWzSpConf,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TWzSpConfEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tWzSpConf, request.getParameterMap());
		try{
		//???????????????????????????
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.tWzSpConfService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * ??????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(TWzSpConfEntity tWzSpConf, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		tWzSpConf = systemService.getEntity(TWzSpConfEntity.class, tWzSpConf.getId());
		message = "????????????????????????";
		try{
			tWzSpConfService.delete(tWzSpConf);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "????????????????????????";
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
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		try{
			for(String id:ids.split(",")){
				TWzSpConfEntity tWzSpConf = systemService.getEntity(TWzSpConfEntity.class,
				Integer.parseInt(id)
				);
				tWzSpConfService.delete(tWzSpConf);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * ??????????????????
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(TWzSpConfEntity tWzSpConf, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		try{
			try{
				String[] strs = tWzSpConf.getSpUsername().split(",");
				//??????????????????????????????????????????????????????????????????
				String userid = strs[0];
				TSUser user = systemService.get(TSUser.class,userid);
				tWzSpConf.setSpUsername(user.getUserName());
			}catch (Exception e){

			}
			tWzSpConfService.save(tWzSpConf);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * ??????????????????
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(TWzSpConfEntity tWzSpConf, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		TWzSpConfEntity t = tWzSpConfService.get(TWzSpConfEntity.class, tWzSpConf.getId());
		try {
			try{
				String[] strs = tWzSpConf.getSpUsername().split(",");
				//??????????????????????????????????????????????????????????????????
				String userid = strs[0];
				TSUser user = systemService.get(TSUser.class,userid);
				tWzSpConf.setSpUsername(user.getUserName());
			}catch (Exception e){

			}

			MyBeanUtils.copyBeanNotNull2Bean(tWzSpConf, t);
			tWzSpConfService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * ??????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(TWzSpConfEntity tWzSpConf, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tWzSpConf.getId())) {
			tWzSpConf = tWzSpConfService.getEntity(TWzSpConfEntity.class, tWzSpConf.getId());
			req.setAttribute("tWzSpConfPage", tWzSpConf);
		}
		return new ModelAndView("com/zzjee/wz/tWzSpConf-add");
	}
	/**
	 * ??????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(TWzSpConfEntity tWzSpConf, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tWzSpConf.getId())) {
			tWzSpConf = tWzSpConfService.getEntity(TWzSpConfEntity.class, tWzSpConf.getId());
			req.setAttribute("tWzSpConfPage", tWzSpConf);
		}
		return new ModelAndView("com/zzjee/wz/tWzSpConf-update");
	}

	/**
	 * ??????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","tWzSpConfController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * ??????excel
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(TWzSpConfEntity tWzSpConf, HttpServletRequest request, HttpServletResponse response
			, DataGrid dataGrid, ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(TWzSpConfEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tWzSpConf, request.getParameterMap());
		List<TWzSpConfEntity> tWzSpConfs = this.tWzSpConfService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"????????????");
		modelMap.put(NormalExcelConstants.CLASS,TWzSpConfEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("??????????????????", "?????????:"+ ResourceUtil.getSessionUser().getRealName(),
			"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST,tWzSpConfs);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * ??????excel ?????????
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(TWzSpConfEntity tWzSpConf, HttpServletRequest request, HttpServletResponse response
			, DataGrid dataGrid, ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"????????????");
    	modelMap.put(NormalExcelConstants.CLASS,TWzSpConfEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("??????????????????", "?????????:"+ ResourceUtil.getSessionUser().getRealName(),
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
				List<TWzSpConfEntity> listTWzSpConfEntitys = ExcelImportUtil.importExcel(file.getInputStream(),TWzSpConfEntity.class,params);
				for (TWzSpConfEntity tWzSpConf : listTWzSpConfEntitys) {
					tWzSpConfService.save(tWzSpConf);
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
	@ApiOperation(value="????????????????????????",produces="application/json",httpMethod="GET")
	public ResponseMessage<List<TWzSpConfEntity>> list() {
		List<TWzSpConfEntity> listTWzSpConfs=tWzSpConfService.getList(TWzSpConfEntity.class);
		return Result.success(listTWzSpConfs);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="??????ID????????????????????????",notes="??????ID????????????????????????",httpMethod="GET",produces="application/json")
	public ResponseMessage<?> get(@ApiParam(required=true,name="id",value="ID")@PathVariable("id") String id) {
		TWzSpConfEntity task = tWzSpConfService.get(TWzSpConfEntity.class, id);
		if (task == null) {
			return Result.error("??????ID??????????????????????????????");
		}
		return Result.success(task);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="??????????????????")
	public ResponseMessage<?> create(@ApiParam(name="??????????????????")@RequestBody TWzSpConfEntity tWzSpConf, UriComponentsBuilder uriBuilder) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<TWzSpConfEntity>> failures = validator.validate(tWzSpConf);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//??????
		try{
			tWzSpConfService.save(tWzSpConf);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("??????????????????????????????");
		}
		return Result.success(tWzSpConf);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="??????????????????",notes="??????????????????")
	public ResponseMessage<?> update(@ApiParam(name="??????????????????")@RequestBody TWzSpConfEntity tWzSpConf) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<TWzSpConfEntity>> failures = validator.validate(tWzSpConf);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//??????
		try{
			tWzSpConfService.saveOrUpdate(tWzSpConf);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("??????????????????????????????");
		}

		//???Restful???????????????204?????????, ?????????. ???????????????200?????????.
		return Result.success("??????????????????????????????");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value="??????????????????")
	public ResponseMessage<?> delete(@ApiParam(name="id",value="ID",required=true)@PathVariable("id") String id) {
		logger.info("delete[{}]" + id);
		// ??????
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID????????????");
		}
		try {
			tWzSpConfService.deleteEntityById(TWzSpConfEntity.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("????????????????????????");
		}

		return Result.success();
	}
}
