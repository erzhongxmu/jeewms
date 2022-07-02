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
 * @Description: 审批配置
 * @author onlineGenerator
 * @date 2018-05-20 21:40:51
 * @version V1.0
 *
 */
@Api(value="TWzSpConf",description="审批配置",tags="tWzSpConfController")
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
	 * 审批配置列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/wz/tWzSpConfList");
	}

	/**
	 * easyui AJAX请求数据
	 *
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(TWzSpConfEntity tWzSpConf,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TWzSpConfEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tWzSpConf, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.tWzSpConfService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除审批配置
	 *
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(TWzSpConfEntity tWzSpConf, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		tWzSpConf = systemService.getEntity(TWzSpConfEntity.class, tWzSpConf.getId());
		message = "审批配置删除成功";
		try{
			tWzSpConfService.delete(tWzSpConf);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "审批配置删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除审批配置
	 *
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "审批配置删除成功";
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
			message = "审批配置删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加审批配置
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(TWzSpConfEntity tWzSpConf, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "审批配置添加成功";
		try{
			try{
				String[] strs = tWzSpConf.getSpUsername().split(",");
				//第一个分号之前的字符串自然就是数组里的第一个
				String userid = strs[0];
				TSUser user = systemService.get(TSUser.class,userid);
				tWzSpConf.setSpUsername(user.getUserName());
			}catch (Exception e){

			}
			tWzSpConfService.save(tWzSpConf);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "审批配置添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新审批配置
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(TWzSpConfEntity tWzSpConf, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "审批配置更新成功";
		TWzSpConfEntity t = tWzSpConfService.get(TWzSpConfEntity.class, tWzSpConf.getId());
		try {
			try{
				String[] strs = tWzSpConf.getSpUsername().split(",");
				//第一个分号之前的字符串自然就是数组里的第一个
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
			message = "审批配置更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 审批配置新增页面跳转
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
	 * 审批配置编辑页面跳转
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
	 * 导入功能跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","tWzSpConfController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * 导出excel
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
		modelMap.put(NormalExcelConstants.FILE_NAME,"审批配置");
		modelMap.put(NormalExcelConstants.CLASS,TWzSpConfEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("审批配置列表", "导出人:"+ ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,tWzSpConfs);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(TWzSpConfEntity tWzSpConf, HttpServletRequest request, HttpServletResponse response
			, DataGrid dataGrid, ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"审批配置");
    	modelMap.put(NormalExcelConstants.CLASS,TWzSpConfEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("审批配置列表", "导出人:"+ ResourceUtil.getSessionUser().getRealName(),
    	"导出信息"));
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
			MultipartFile file = entity.getValue();// 获取上传文件对象
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<TWzSpConfEntity> listTWzSpConfEntitys = ExcelImportUtil.importExcel(file.getInputStream(),TWzSpConfEntity.class,params);
				for (TWzSpConfEntity tWzSpConf : listTWzSpConfEntitys) {
					tWzSpConfService.save(tWzSpConf);
				}
				j.setMsg("文件导入成功！");
			} catch (Exception e) {
				j.setMsg("文件导入失败！");
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
	@ApiOperation(value="审批配置列表信息",produces="application/json",httpMethod="GET")
	public ResponseMessage<List<TWzSpConfEntity>> list() {
		List<TWzSpConfEntity> listTWzSpConfs=tWzSpConfService.getList(TWzSpConfEntity.class);
		return Result.success(listTWzSpConfs);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="根据ID获取审批配置信息",notes="根据ID获取审批配置信息",httpMethod="GET",produces="application/json")
	public ResponseMessage<?> get(@ApiParam(required=true,name="id",value="ID")@PathVariable("id") String id) {
		TWzSpConfEntity task = tWzSpConfService.get(TWzSpConfEntity.class, id);
		if (task == null) {
			return Result.error("根据ID获取审批配置信息为空");
		}
		return Result.success(task);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="创建审批配置")
	public ResponseMessage<?> create(@ApiParam(name="审批配置对象")@RequestBody TWzSpConfEntity tWzSpConf, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<TWzSpConfEntity>> failures = validator.validate(tWzSpConf);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//保存
		try{
			tWzSpConfService.save(tWzSpConf);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("审批配置信息保存失败");
		}
		return Result.success(tWzSpConf);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="更新审批配置",notes="更新审批配置")
	public ResponseMessage<?> update(@ApiParam(name="审批配置对象")@RequestBody TWzSpConfEntity tWzSpConf) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<TWzSpConfEntity>> failures = validator.validate(tWzSpConf);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//保存
		try{
			tWzSpConfService.saveOrUpdate(tWzSpConf);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("更新审批配置信息失败");
		}

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return Result.success("更新审批配置信息成功");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value="删除审批配置")
	public ResponseMessage<?> delete(@ApiParam(name="id",value="ID",required=true)@PathVariable("id") String id) {
		logger.info("delete[{}]" + id);
		// 验证
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID不能为空");
		}
		try {
			tWzSpConfService.deleteEntityById(TWzSpConfEntity.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("审批配置删除失败");
		}

		return Result.success();
	}
}
