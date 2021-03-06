package com.zzjee.report.controller;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.zzjee.report.entity.RpWmUpAndDownEntity;
import com.zzjee.report.service.RpWmUpAndDownServiceI;

/**   
 * @Title: Controller  
 * @Description: rp_wm_up_and_down
 * @author erzhongxmu
 * @date 2018-09-11 07:47:13
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/rpWmUpAndDownController")
public class RpWmUpAndDownController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RpWmUpAndDownController.class);

	@Autowired
	private RpWmUpAndDownServiceI rpWmUpAndDownService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	


	/**
	 * rp_wm_up_and_down?????? ????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/report/rpWmUpAndDownList");
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
	public void datagrid(RpWmUpAndDownEntity rpWmUpAndDown,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(RpWmUpAndDownEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, rpWmUpAndDown, request.getParameterMap());
		try{
		//???????????????????????????
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.rpWmUpAndDownService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * ??????rp_wm_up_and_down
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(RpWmUpAndDownEntity rpWmUpAndDown, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		rpWmUpAndDown = systemService.getEntity(RpWmUpAndDownEntity.class, rpWmUpAndDown.getId());
		message = "rp_wm_up_and_down????????????";
		try{
			rpWmUpAndDownService.delete(rpWmUpAndDown);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "rp_wm_up_and_down????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * ????????????rp_wm_up_and_down
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "rp_wm_up_and_down????????????";
		try{
			for(String id:ids.split(",")){
				RpWmUpAndDownEntity rpWmUpAndDown = systemService.getEntity(RpWmUpAndDownEntity.class, 
				id
				);
				rpWmUpAndDownService.delete(rpWmUpAndDown);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "rp_wm_up_and_down????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * ??????rp_wm_up_and_down
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(RpWmUpAndDownEntity rpWmUpAndDown, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "rp_wm_up_and_down????????????";
		try{
			rpWmUpAndDownService.save(rpWmUpAndDown);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "rp_wm_up_and_down????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * ??????rp_wm_up_and_down
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(RpWmUpAndDownEntity rpWmUpAndDown, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "rp_wm_up_and_down????????????";
		RpWmUpAndDownEntity t = rpWmUpAndDownService.get(RpWmUpAndDownEntity.class, rpWmUpAndDown.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(rpWmUpAndDown, t);
			rpWmUpAndDownService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "rp_wm_up_and_down????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * rp_wm_up_and_down??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(RpWmUpAndDownEntity rpWmUpAndDown, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(rpWmUpAndDown.getId())) {
			rpWmUpAndDown = rpWmUpAndDownService.getEntity(RpWmUpAndDownEntity.class, rpWmUpAndDown.getId());
			req.setAttribute("rpWmUpAndDownPage", rpWmUpAndDown);
		}
		return new ModelAndView("com/zzjee/report/rpWmUpAndDown-add");
	}
	/**
	 * rp_wm_up_and_down??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(RpWmUpAndDownEntity rpWmUpAndDown, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(rpWmUpAndDown.getId())) {
			rpWmUpAndDown = rpWmUpAndDownService.getEntity(RpWmUpAndDownEntity.class, rpWmUpAndDown.getId());
			req.setAttribute("rpWmUpAndDownPage", rpWmUpAndDown);
		}
		return new ModelAndView("com/zzjee/report/rpWmUpAndDown-update");
	}
	
	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","rpWmUpAndDownController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * ??????excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(RpWmUpAndDownEntity rpWmUpAndDown,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(RpWmUpAndDownEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, rpWmUpAndDown, request.getParameterMap());
		List<RpWmUpAndDownEntity> rpWmUpAndDowns = this.rpWmUpAndDownService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"rp_wm_up_and_down");
		modelMap.put(NormalExcelConstants.CLASS,RpWmUpAndDownEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("rp_wm_up_and_down??????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
			"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST,rpWmUpAndDowns);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * ??????excel ?????????
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(RpWmUpAndDownEntity rpWmUpAndDown,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"rp_wm_up_and_down");
    	modelMap.put(NormalExcelConstants.CLASS,RpWmUpAndDownEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("rp_wm_up_and_down??????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
    	"????????????"));
    	modelMap.put(NormalExcelConstants.DATA_LIST,new ArrayList());
    	return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	
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
				List<RpWmUpAndDownEntity> listRpWmUpAndDownEntitys = ExcelImportUtil.importExcel(file.getInputStream(),RpWmUpAndDownEntity.class,params);
				for (RpWmUpAndDownEntity rpWmUpAndDown : listRpWmUpAndDownEntitys) {
					rpWmUpAndDownService.save(rpWmUpAndDown);
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
	public List<RpWmUpAndDownEntity> list() {
		List<RpWmUpAndDownEntity> listRpWmUpAndDowns=rpWmUpAndDownService.getList(RpWmUpAndDownEntity.class);
		return listRpWmUpAndDowns;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		RpWmUpAndDownEntity task = rpWmUpAndDownService.get(RpWmUpAndDownEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody RpWmUpAndDownEntity rpWmUpAndDown, UriComponentsBuilder uriBuilder) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<RpWmUpAndDownEntity>> failures = validator.validate(rpWmUpAndDown);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//??????
		try{
			rpWmUpAndDownService.save(rpWmUpAndDown);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//??????Restful???????????????????????????????????????url, ?????????????????????id?????????.
		String id = rpWmUpAndDown.getId();
		URI uri = uriBuilder.path("/rest/rpWmUpAndDownController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody RpWmUpAndDownEntity rpWmUpAndDown) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<RpWmUpAndDownEntity>> failures = validator.validate(rpWmUpAndDown);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//??????
		try{
			rpWmUpAndDownService.saveOrUpdate(rpWmUpAndDown);
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
		rpWmUpAndDownService.deleteEntityById(RpWmUpAndDownEntity.class, id);
	}
}
