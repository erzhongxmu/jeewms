package com.zzjee.oms.controller;
import com.zzjee.oms.entity.OmsOederDetailEntity;
import com.zzjee.oms.service.OmsOederDetailServiceI;
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
 * @Description: ????????????
 * @author onlineGenerator
 * @date 2019-07-07 15:44:44
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/omsOederDetailController")
public class OmsOederDetailController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(OmsOederDetailController.class);

	@Autowired
	private OmsOederDetailServiceI omsOederDetailService;
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
		return new ModelAndView("com/zzjee/oms/omsOederDetailList");
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
	public void datagrid(OmsOederDetailEntity omsOederDetail,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(OmsOederDetailEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, omsOederDetail, request.getParameterMap());
		try{
		//???????????????????????????
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.omsOederDetailService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(OmsOederDetailEntity omsOederDetail, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		omsOederDetail = systemService.getEntity(OmsOederDetailEntity.class, omsOederDetail.getId());
		message = "????????????????????????";
		try{
			omsOederDetailService.delete(omsOederDetail);
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
				OmsOederDetailEntity omsOederDetail = systemService.getEntity(OmsOederDetailEntity.class, 
				id
				);
				omsOederDetailService.delete(omsOederDetail);
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
	public AjaxJson doAdd(OmsOederDetailEntity omsOederDetail, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		try{
			omsOederDetailService.save(omsOederDetail);
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
	public AjaxJson doUpdate(OmsOederDetailEntity omsOederDetail, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		OmsOederDetailEntity t = omsOederDetailService.get(OmsOederDetailEntity.class, omsOederDetail.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(omsOederDetail, t);
			omsOederDetailService.saveOrUpdate(t);
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
	public ModelAndView goAdd(OmsOederDetailEntity omsOederDetail, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(omsOederDetail.getId())) {
			omsOederDetail = omsOederDetailService.getEntity(OmsOederDetailEntity.class, omsOederDetail.getId());
			req.setAttribute("omsOederDetailPage", omsOederDetail);
		}
		return new ModelAndView("com/zzjee/oms/omsOederDetail-add");
	}
	/**
	 * ??????????????????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(OmsOederDetailEntity omsOederDetail, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(omsOederDetail.getId())) {
			omsOederDetail = omsOederDetailService.getEntity(OmsOederDetailEntity.class, omsOederDetail.getId());
			req.setAttribute("omsOederDetailPage", omsOederDetail);
		}
		return new ModelAndView("com/zzjee/oms/omsOederDetail-update");
	}
	
	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","omsOederDetailController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * ??????excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(OmsOederDetailEntity omsOederDetail,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(OmsOederDetailEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, omsOederDetail, request.getParameterMap());
		List<OmsOederDetailEntity> omsOederDetails = this.omsOederDetailService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"????????????");
		modelMap.put(NormalExcelConstants.CLASS,OmsOederDetailEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("??????????????????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
			"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST,omsOederDetails);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * ??????excel ?????????
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(OmsOederDetailEntity omsOederDetail,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"????????????");
    	modelMap.put(NormalExcelConstants.CLASS,OmsOederDetailEntity.class);
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
				List<OmsOederDetailEntity> listOmsOederDetailEntitys = ExcelImportUtil.importExcel(file.getInputStream(),OmsOederDetailEntity.class,params);
				for (OmsOederDetailEntity omsOederDetail : listOmsOederDetailEntitys) {
					omsOederDetailService.save(omsOederDetail);
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
	public List<OmsOederDetailEntity> list() {
		List<OmsOederDetailEntity> listOmsOederDetails=omsOederDetailService.getList(OmsOederDetailEntity.class);
		return listOmsOederDetails;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		OmsOederDetailEntity task = omsOederDetailService.get(OmsOederDetailEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody OmsOederDetailEntity omsOederDetail, UriComponentsBuilder uriBuilder) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<OmsOederDetailEntity>> failures = validator.validate(omsOederDetail);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//??????
		try{
			omsOederDetailService.save(omsOederDetail);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//??????Restful???????????????????????????????????????url, ?????????????????????id?????????.
		String id = omsOederDetail.getId();
		URI uri = uriBuilder.path("/rest/omsOederDetailController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody OmsOederDetailEntity omsOederDetail) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<OmsOederDetailEntity>> failures = validator.validate(omsOederDetail);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//??????
		try{
			omsOederDetailService.saveOrUpdate(omsOederDetail);
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
		omsOederDetailService.deleteEntityById(OmsOederDetailEntity.class, id);
	}
}
