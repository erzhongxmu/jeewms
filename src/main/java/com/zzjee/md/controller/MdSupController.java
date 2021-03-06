package com.zzjee.md.controller;

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

import com.zzjee.md.entity.MdSupEntity;
import com.zzjee.md.service.MdSupServiceI;
import com.zzjee.wmutil.wmIntUtil;

/**
 * @Title: Controller
 * @Description: ?????????
 * @author erzhongxmu
 * @date 2017-08-15 23:16:45
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/mdSupController")
public class MdSupController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(MdSupController.class);

	@Autowired
	private MdSupServiceI mdSupService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;

	/**
	 * ??????????????? ????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/md/mdSupList");
	}
	@RequestMapping(params = "listsel")
	public ModelAndView listsel(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/md/mdSupselList");
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
	public void datagrid(MdSupEntity mdSup, HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(MdSupEntity.class, dataGrid);
		// ?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq,
				mdSup, request.getParameterMap());
		try {
			// ???????????????????????????
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.mdSupService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * ???????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(MdSupEntity mdSup, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		mdSup = systemService.getEntity(MdSupEntity.class, mdSup.getId());
		message = "?????????????????????";
		try {
			mdSupService.delete(mdSup);
			systemService.addLog(message, Globals.Log_Type_DEL,
					Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "?????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * ?????????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "?????????????????????";
		try {
			for (String id : ids.split(",")) {
				MdSupEntity mdSup = systemService.getEntity(MdSupEntity.class,
						id);
				mdSupService.delete(mdSup);
				systemService.addLog(message, Globals.Log_Type_DEL,
						Globals.Log_Leavel_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "?????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * ???????????????
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(MdSupEntity mdSup, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "?????????????????????";
		try {
			MdSupEntity mdSup1 = systemService.findUniqueByProperty(
					MdSupEntity.class, "gysBianMa", mdSup.getGysBianMa());
			if (mdSup1 == null) {
				mdSupService.save(mdSup);
				systemService.addLog(message, Globals.Log_Type_INSERT,
						Globals.Log_Leavel_INFO);
			} else {
				j.setSuccess(false);
				message = "???????????????????????????";
			}

		} catch (Exception e) {
			e.printStackTrace();
			message = "?????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	@RequestMapping(params = "doGet")
	@ResponseBody
	public AjaxJson dogetfromother(String formDate, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";

		try {
			if(StringUtil.isEmpty(formDate)){
				formDate = "2011-01-01";
			}
			wmIntUtil.getSup(formDate);
			systemService.addLog(message, Globals.Log_Type_UPDATE,
					Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	/**
	 * ???????????????
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(MdSupEntity mdSup, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "?????????????????????";
		MdSupEntity t = mdSupService.get(MdSupEntity.class, mdSup.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(mdSup, t);
			mdSupService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE,
					Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "?????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * ???????????????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(MdSupEntity mdSup, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(mdSup.getId())) {
			mdSup = mdSupService.getEntity(MdSupEntity.class, mdSup.getId());
			req.setAttribute("mdSupPage", mdSup);
		}
		return new ModelAndView("com/zzjee/md/mdSup-add");
	}

	/**
	 * ???????????????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(MdSupEntity mdSup, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(mdSup.getId())) {
			mdSup = mdSupService.getEntity(MdSupEntity.class, mdSup.getId());
			req.setAttribute("mdSupPage", mdSup);
		}
		return new ModelAndView("com/zzjee/md/mdSup-update");
	}

	/**
	 * ??????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name", "mdSupController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * ??????excel
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(MdSupEntity mdSup, HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(MdSupEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq,
				mdSup, request.getParameterMap());
		List<MdSupEntity> mdSups = this.mdSupService.getListByCriteriaQuery(cq,
				false);
		modelMap.put(NormalExcelConstants.FILE_NAME, "?????????");
		modelMap.put(NormalExcelConstants.CLASS, MdSupEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("???????????????",
				"?????????:" + ResourceUtil.getSessionUserName().getRealName(),
				"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST, mdSups);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	/**
	 * ??????excel ?????????
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(MdSupEntity mdSup, HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid, ModelMap modelMap) {
		modelMap.put(NormalExcelConstants.FILE_NAME, "?????????");
		modelMap.put(NormalExcelConstants.CLASS, MdSupEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("???????????????",
				"?????????:" + ResourceUtil.getSessionUserName().getRealName(),
				"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST, new ArrayList());
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(params = "importExcel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson importExcel(HttpServletRequest request,
			HttpServletResponse response) {
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
				List<MdSupEntity> listMdSupEntitys = ExcelImportUtil
						.importExcel(file.getInputStream(), MdSupEntity.class,
								params);
				for (MdSupEntity mdSup : listMdSupEntitys) {
					mdSupService.save(mdSup);
				}
				j.setMsg("?????????????????????");
			} catch (Exception e) {
				j.setMsg("?????????????????????");
				logger.error(ExceptionUtil.getExceptionMessage(e));
			} finally {
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
	public List<MdSupEntity> list() {
		List<MdSupEntity> listMdSups = mdSupService.getList(MdSupEntity.class);
		return listMdSups;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		MdSupEntity task = mdSupService.get(MdSupEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody MdSupEntity mdSup,
			UriComponentsBuilder uriBuilder) {
		// ??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<MdSupEntity>> failures = validator
				.validate(mdSup);
		if (!failures.isEmpty()) {
			return new ResponseEntity(
					BeanValidators.extractPropertyAndMessage(failures),
					HttpStatus.BAD_REQUEST);
		}

		// ??????
		try {
			mdSupService.save(mdSup);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		// ??????Restful???????????????????????????????????????url, ?????????????????????id?????????.
		String id = mdSup.getId();
		URI uri = uriBuilder.path("/rest/mdSupController/" + id).build()
				.toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody MdSupEntity mdSup) {
		// ??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<MdSupEntity>> failures = validator
				.validate(mdSup);
		if (!failures.isEmpty()) {
			return new ResponseEntity(
					BeanValidators.extractPropertyAndMessage(failures),
					HttpStatus.BAD_REQUEST);
		}

		// ??????
		try {
			mdSupService.saveOrUpdate(mdSup);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		// ???Restful???????????????204?????????, ?????????. ???????????????200?????????.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		mdSupService.deleteEntityById(MdSupEntity.class, id);
	}
}
