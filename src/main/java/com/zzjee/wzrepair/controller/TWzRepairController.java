package com.zzjee.wzrepair.controller;

import com.zzjee.wzrepair.entity.TWzRepairEntity;
import com.zzjee.wzrepair.service.TWzRepairServiceI;
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
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Title: Controller
 * @Description: ?????????????????????
 * @author onlineGenerator
 * @date 2020-02-24 11:36:29
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/tWzRepairController")
public class TWzRepairController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(com.zzjee.wzrepair.controller.TWzRepairController.class);

	@Autowired
	private TWzRepairServiceI tWzRepairService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;



	/**
	 * ??????????????????????????? ????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/wzrepair/tWzRepairList");
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
	public void datagrid(TWzRepairEntity tWzRepair,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TWzRepairEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tWzRepair, request.getParameterMap());
		try{
		//???????????????????????????
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.tWzRepairService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * ???????????????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(TWzRepairEntity tWzRepair, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		tWzRepair = systemService.getEntity(TWzRepairEntity.class, tWzRepair.getId());
		message = "?????????????????????????????????";
		try{
			tWzRepairService.delete(tWzRepair);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "?????????????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * ?????????????????????????????????
	 *
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "?????????????????????????????????";
		try{
			for(String id:ids.split(",")){
				TWzRepairEntity tWzRepair = systemService.getEntity(TWzRepairEntity.class,
				id
				);
				tWzRepairService.delete(tWzRepair);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "?????????????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * ???????????????????????????
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(TWzRepairEntity tWzRepair, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "?????????????????????????????????";
		try{
			tWzRepairService.save(tWzRepair);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "?????????????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * ???????????????????????????
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(TWzRepairEntity tWzRepair, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "?????????????????????????????????";
		TWzRepairEntity t = tWzRepairService.get(TWzRepairEntity.class, tWzRepair.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(tWzRepair, t);
			tWzRepairService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "?????????????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * ???????????????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(TWzRepairEntity tWzRepair, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tWzRepair.getId())) {
			tWzRepair = tWzRepairService.getEntity(TWzRepairEntity.class, tWzRepair.getId());
			req.setAttribute("tWzRepairPage", tWzRepair);
		}
		return new ModelAndView("com/zzjee/wzrepair/tWzRepair-add");
	}
	/**
	 * ???????????????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(TWzRepairEntity tWzRepair, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tWzRepair.getId())) {
			tWzRepair = tWzRepairService.getEntity(TWzRepairEntity.class, tWzRepair.getId());
			req.setAttribute("tWzRepairPage", tWzRepair);
		}
		return new ModelAndView("com/zzjee/wzrepair/tWzRepair-update");
	}

	/**
	 * ??????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","tWzRepairController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * ??????excel
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(TWzRepairEntity tWzRepair, HttpServletRequest request, HttpServletResponse response
			, DataGrid dataGrid, ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(TWzRepairEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tWzRepair, request.getParameterMap());
		List<TWzRepairEntity> tWzRepairs = this.tWzRepairService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"?????????????????????");
		modelMap.put(NormalExcelConstants.CLASS,TWzRepairEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("???????????????????????????", "?????????:"+ ResourceUtil.getSessionUserName().getRealName(),
			"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST,tWzRepairs);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * ??????excel ?????????
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(TWzRepairEntity tWzRepair, HttpServletRequest request, HttpServletResponse response
			, DataGrid dataGrid, ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"?????????????????????");
    	modelMap.put(NormalExcelConstants.CLASS,TWzRepairEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("???????????????????????????", "?????????:"+ ResourceUtil.getSessionUserName().getRealName(),
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
				List<TWzRepairEntity> listTWzRepairEntitys = ExcelImportUtil.importExcel(file.getInputStream(),TWzRepairEntity.class,params);
				for (TWzRepairEntity tWzRepair : listTWzRepairEntitys) {
					tWzRepairService.save(tWzRepair);
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
	public List<TWzRepairEntity> list() {
		List<TWzRepairEntity> listTWzRepairs=tWzRepairService.getList(TWzRepairEntity.class);
		return listTWzRepairs;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		TWzRepairEntity task = tWzRepairService.get(TWzRepairEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody TWzRepairEntity tWzRepair, UriComponentsBuilder uriBuilder) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<TWzRepairEntity>> failures = validator.validate(tWzRepair);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//??????
		try{
			tWzRepairService.save(tWzRepair);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//??????Restful???????????????????????????????????????url, ?????????????????????id?????????.
		String id = tWzRepair.getId();
		URI uri = uriBuilder.path("/rest/tWzRepairController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody TWzRepairEntity tWzRepair) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<TWzRepairEntity>> failures = validator.validate(tWzRepair);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//??????
		try{
			tWzRepairService.saveOrUpdate(tWzRepair);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		//???Restful???????????????204?????????, ?????????. ???????????????200?????????.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		tWzRepairService.deleteEntityById(TWzRepairEntity.class, id);
	}
}
