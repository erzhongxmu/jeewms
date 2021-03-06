package com.zzjee.rfid.controller;
import com.zzjee.rfid.entity.RfidBuseEntity;
import com.zzjee.rfid.service.RfidBuseServiceI;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.common.TreeChildCount;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import java.io.OutputStream;
import org.jeecgframework.core.util.BrowserUtils;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.vo.TemplateExcelConstants;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jeecgframework.core.util.ResourceUtil;
import java.io.IOException;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import java.util.Map;
import java.util.HashMap;
import org.jeecgframework.core.util.ExceptionUtil;

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

/**
 * @Title: Controller
 * @Description: RFID???
 * @author onlineGenerator
 * @date 2020-06-19 06:54:21
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/rfidBuseController")
public class RfidBuseController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(RfidBuseController.class);

	@Autowired
	private RfidBuseServiceI rfidBuseService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;



	/**
	 * RFID????????? ????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/rfid/rfidBuseList");
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
	public void datagrid(RfidBuseEntity rfidBuse,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(RfidBuseEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, rfidBuse, request.getParameterMap());
		try{
		//???????????????????????????
		String query_createDate_begin = request.getParameter("createDate_begin");
		String query_createDate_end = request.getParameter("createDate_end");
		if(StringUtil.isNotEmpty(query_createDate_begin)){
			cq.ge("createDate", new SimpleDateFormat("yyyy-MM-dd").parse(query_createDate_begin));
		}
		if(StringUtil.isNotEmpty(query_createDate_end)){
			cq.le("createDate", new SimpleDateFormat("yyyy-MM-dd").parse(query_createDate_end));
		}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.rfidBuseService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * ??????RFID???
	 *
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(RfidBuseEntity rfidBuse, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		rfidBuse = systemService.getEntity(RfidBuseEntity.class, rfidBuse.getId());
		message = "RFID???????????????";
		try{
			rfidBuse.setBpmStatus("del");
			rfidBuseService.updateEntitie(rfidBuse);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "RFID???????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * ????????????RFID???
	 *
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "RFID???????????????";
		try{
			for(String id:ids.split(",")){
				RfidBuseEntity rfidBuse = systemService.getEntity(RfidBuseEntity.class,
				id
				);
 				rfidBuse.setBpmStatus("del");
				rfidBuseService.updateEntitie(rfidBuse);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "RFID???????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * ??????RFID???
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(RfidBuseEntity rfidBuse, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "RFID???????????????";
		try{
			rfidBuseService.save(rfidBuse);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "RFID???????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * ??????RFID???
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(RfidBuseEntity rfidBuse, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "RFID???????????????";
		RfidBuseEntity t = rfidBuseService.get(RfidBuseEntity.class, rfidBuse.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(rfidBuse, t);
			rfidBuseService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "RFID???????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * RFID?????????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(RfidBuseEntity rfidBuse, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(rfidBuse.getId())) {
			rfidBuse = rfidBuseService.getEntity(RfidBuseEntity.class, rfidBuse.getId());
			req.setAttribute("rfidBusePage", rfidBuse);
		}
		return new ModelAndView("com/zzjee/rfid/rfidBuse-add");
	}
	/**
	 * RFID?????????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(RfidBuseEntity rfidBuse, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(rfidBuse.getId())) {
			rfidBuse = rfidBuseService.getEntity(RfidBuseEntity.class, rfidBuse.getId());
			req.setAttribute("rfidBusePage", rfidBuse);
		}
		return new ModelAndView("com/zzjee/rfid/rfidBuse-update");
	}

	/**
	 * ??????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","rfidBuseController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * ??????excel
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(RfidBuseEntity rfidBuse,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(RfidBuseEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, rfidBuse, request.getParameterMap());
		List<RfidBuseEntity> rfidBuses = this.rfidBuseService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"RFID???");
		modelMap.put(NormalExcelConstants.CLASS,RfidBuseEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("RFID?????????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
			"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST,rfidBuses);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * ??????excel ?????????
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(RfidBuseEntity rfidBuse,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"RFID???");
    	modelMap.put(NormalExcelConstants.CLASS,RfidBuseEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("RFID?????????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
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
				List<RfidBuseEntity> listRfidBuseEntitys = ExcelImportUtil.importExcel(file.getInputStream(),RfidBuseEntity.class,params);
				for (RfidBuseEntity rfidBuse : listRfidBuseEntitys) {
					rfidBuseService.save(rfidBuse);
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
	public List<RfidBuseEntity> list() {
		List<RfidBuseEntity> listRfidBuses=rfidBuseService.getList(RfidBuseEntity.class);
		return listRfidBuses;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		RfidBuseEntity task = rfidBuseService.get(RfidBuseEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody RfidBuseEntity rfidBuse, UriComponentsBuilder uriBuilder) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<RfidBuseEntity>> failures = validator.validate(rfidBuse);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//??????
		try{
			rfidBuseService.save(rfidBuse);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//??????Restful???????????????????????????????????????url, ?????????????????????id?????????.
		String id = rfidBuse.getId();
		URI uri = uriBuilder.path("/rest/rfidBuseController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody RfidBuseEntity rfidBuse) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<RfidBuseEntity>> failures = validator.validate(rfidBuse);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//??????
		try{
			rfidBuseService.saveOrUpdate(rfidBuse);
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
		rfidBuseService.deleteEntityById(RfidBuseEntity.class, id);
	}
}
