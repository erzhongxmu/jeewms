package com.zzjee.wm.controller;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.jeecgframework.core.util.DateUtils;
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

import com.zzjee.wm.entity.WmPlatIoEntity;
import com.zzjee.wm.service.WmPlatIoServiceI;

/**   
 * @Title: Controller  
 * @Description: ????????????
 * @author erzhongxmu
 * @date 2017-08-15 23:20:14
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/wmPlatIoController")
public class WmPlatIoController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(WmPlatIoController.class);

	@Autowired
	private WmPlatIoServiceI wmPlatIoService;
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
		return new ModelAndView("com/zzjee/wm/wmPlatIoList");
	}
	@RequestMapping(params = "listplan")
	public ModelAndView listplan(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/wm/wmPlatIoplanList");
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
	public void datagrid(WmPlatIoEntity wmPlatIo,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(WmPlatIoEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wmPlatIo, request.getParameterMap());
		try{
		//???????????????????????????

		String query_planIndata_begin = DateUtils.date2Str(new Date(),DateUtils.date_sdf);
		Date today = new Date();
		Calendar c = Calendar.getInstance();  
        c.setTime(today);  
        c.add(Calendar.DAY_OF_MONTH, 1);// ??????+1???  
		String query_planIndata_end = DateUtils.date2Str(c.getTime(),DateUtils.date_sdf);;
		if(StringUtil.isNotEmpty(query_planIndata_begin)){
			cq.ge("planIndata", new SimpleDateFormat("yyyy-MM-dd").parse(query_planIndata_begin));
		}
		if(StringUtil.isNotEmpty(query_planIndata_end)){
			cq.le("planIndata", new SimpleDateFormat("yyyy-MM-dd").parse(query_planIndata_end));
		}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.notEq("platSta", "??????");
		cq.add();
		this.wmPlatIoService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	@RequestMapping(params = "datagridplan")
	public void datagridplan(WmPlatIoEntity wmPlatIo,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(WmPlatIoEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wmPlatIo, request.getParameterMap());
		try{
		//???????????????????????????

		String query_planIndata_begin = request.getParameter("planIndata_begin1"); ;

		String query_planIndata_end = request.getParameter("planIndata_end2"); ;
		if(StringUtil.isNotEmpty(query_planIndata_begin)){
			cq.ge("planIndata", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(query_planIndata_begin));
		}
		if(StringUtil.isNotEmpty(query_planIndata_end)){
			cq.le("planIndata", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(query_planIndata_end));
		}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.wmPlatIoService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(WmPlatIoEntity wmPlatIo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		wmPlatIo = systemService.getEntity(WmPlatIoEntity.class, wmPlatIo.getId());
		message = "????????????????????????";
		try{
			wmPlatIoService.delete(wmPlatIo);
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
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		try{
			for(String id:ids.split(",")){
				WmPlatIoEntity wmPlatIo = systemService.getEntity(WmPlatIoEntity.class, 
				id
				);
				wmPlatIoService.delete(wmPlatIo);
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
	public AjaxJson doAdd(WmPlatIoEntity wmPlatIo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		try{
			wmPlatIo.setPlatSta("??????");
			wmPlatIoService.save(wmPlatIo);
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
	public AjaxJson doUpdate(WmPlatIoEntity wmPlatIo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		WmPlatIoEntity t = wmPlatIoService.get(WmPlatIoEntity.class, wmPlatIo.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(wmPlatIo, t);
			wmPlatIoService.saveOrUpdate(t);
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
	 * ??????????????????
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doOutplat")
	@ResponseBody
	public AjaxJson doOutplat(WmPlatIoEntity wmPlatIo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "??????????????????";
		WmPlatIoEntity t = wmPlatIoService.get(WmPlatIoEntity.class, wmPlatIo.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(wmPlatIo, t);
			t.setOutData(new Date());
			t.setPlatSta("??????");
			wmPlatIoService.saveOrUpdate(t);
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
	 * ??????????????????
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doOnplat")
	@ResponseBody
	public AjaxJson doOnplat(WmPlatIoEntity wmPlatIo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "??????????????????";
		WmPlatIoEntity t = wmPlatIoService.get(WmPlatIoEntity.class, wmPlatIo.getId());
		String sql = "select count(*) as count from wm_plat_io  wp where  wp.plat_sta = '??????' and wp.plat_id  = '"+t.getPlatId()+"' and    TO_DAYS(wp.in_data) = TO_DAYS(NOW());";
		Map<String, Object> countMap = systemService
				.findOneForJdbc(sql);
		if(countMap!=null&&((Long) countMap.get("count")).intValue() > 0){
			j.setSuccess(false);
			message = "?????????????????????";
			j.setMsg(message);
			return j;
		}

		try {
			MyBeanUtils.copyBeanNotNull2Bean(wmPlatIo, t);
		    t.setInData(new Date());
		    t.setPlatSta("??????");
			wmPlatIoService.saveOrUpdate(t);
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
	 * ??????????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(WmPlatIoEntity wmPlatIo, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(wmPlatIo.getId())) {
			wmPlatIo = wmPlatIoService.getEntity(WmPlatIoEntity.class, wmPlatIo.getId());
			req.setAttribute("wmPlatIoPage", wmPlatIo);
		}
		return new ModelAndView("com/zzjee/wm/wmPlatIo-add");
	}
	/**
	 * ??????????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(WmPlatIoEntity wmPlatIo, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(wmPlatIo.getId())) {
			wmPlatIo = wmPlatIoService.getEntity(WmPlatIoEntity.class, wmPlatIo.getId());
			req.setAttribute("wmPlatIoPage", wmPlatIo);
		}
		return new ModelAndView("com/zzjee/wm/wmPlatIo-update");
	}
	
	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","wmPlatIoController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * ??????excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(WmPlatIoEntity wmPlatIo,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(WmPlatIoEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wmPlatIo, request.getParameterMap());
		List<WmPlatIoEntity> wmPlatIos = this.wmPlatIoService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"????????????");
		modelMap.put(NormalExcelConstants.CLASS,WmPlatIoEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("??????????????????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
			"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST,wmPlatIos);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * ??????excel ?????????
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(WmPlatIoEntity wmPlatIo,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"????????????");
    	modelMap.put(NormalExcelConstants.CLASS,WmPlatIoEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("??????????????????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
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
				List<WmPlatIoEntity> listWmPlatIoEntitys = ExcelImportUtil.importExcel(file.getInputStream(),WmPlatIoEntity.class,params);
				for (WmPlatIoEntity wmPlatIo : listWmPlatIoEntitys) {
					wmPlatIoService.save(wmPlatIo);
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
	public List<WmPlatIoEntity> list() {
		List<WmPlatIoEntity> listWmPlatIos=wmPlatIoService.getList(WmPlatIoEntity.class);
		return listWmPlatIos;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		WmPlatIoEntity task = wmPlatIoService.get(WmPlatIoEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody WmPlatIoEntity wmPlatIo, UriComponentsBuilder uriBuilder) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<WmPlatIoEntity>> failures = validator.validate(wmPlatIo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//??????
		try{
			wmPlatIoService.save(wmPlatIo);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//??????Restful???????????????????????????????????????url, ?????????????????????id?????????.
		String id = wmPlatIo.getId();
		URI uri = uriBuilder.path("/rest/wmPlatIoController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody WmPlatIoEntity wmPlatIo) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<WmPlatIoEntity>> failures = validator.validate(wmPlatIo);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//??????
		try{
			wmPlatIoService.saveOrUpdate(wmPlatIo);
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
		wmPlatIoService.deleteEntityById(WmPlatIoEntity.class, id);
	}
}
