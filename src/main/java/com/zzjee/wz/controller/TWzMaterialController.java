package com.zzjee.wz.controller;

import com.alibaba.fastjson.JSONArray;
import com.zzjee.md.entity.MdGoodsEntity;
import com.zzjee.md.service.MdGoodsServiceI;
import com.zzjee.wz.entity.TWzMaterialEntity;
import com.zzjee.wz.service.TWzMaterialServiceI;
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
 * @Description: 物料
 * @author onlineGenerator
 * @date 2018-05-20 21:40:03
 * @version V1.0
 *
 */
@Api(value="TWzMaterial",description="物料",tags="tWzMaterialController")
@Controller
@RequestMapping("/tWzMaterialController")
public class TWzMaterialController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TWzMaterialController.class);

	@Autowired
	private TWzMaterialServiceI tWzMaterialService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;

	@Autowired
	private MdGoodsServiceI mdGoodsService;

	/**
	 * 物料列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/wz/tWzMaterialList");
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
	public void datagrid(TWzMaterialEntity tWzMaterial,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TWzMaterialEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tWzMaterial, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.tWzMaterialService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除物料
	 *
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(TWzMaterialEntity tWzMaterial, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		tWzMaterial = systemService.getEntity(TWzMaterialEntity.class, tWzMaterial.getId());
		message = "物料删除成功";
		try{
			tWzMaterialService.delete(tWzMaterial);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "物料删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除物料
	 *
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "物料删除成功";
		try{
			for(String id:ids.split(",")){
				TWzMaterialEntity tWzMaterial = systemService.getEntity(TWzMaterialEntity.class,
				Integer.parseInt(id)
				);
				tWzMaterialService.delete(tWzMaterial);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "物料删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加物料
	 *
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(TWzMaterialEntity tWzMaterial, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "物料添加成功";
		try{
			tWzMaterialService.save(tWzMaterial);

			MdGoodsEntity mdn = mdGoodsService.findUniqueByProperty(MdGoodsEntity.class,"shpBianMa",tWzMaterial.getMatCode());
			if(mdn==null){
				mdn = new MdGoodsEntity();
				mdn.setShpBianMa(tWzMaterial.getMatCode());
				mdn.setSuoShuKeHu(ResourceUtil.getConfigByName("default.cuscode"));
				mdn.setChlShl("1");
				mdn.setShpMingCheng(tWzMaterial.getMatName());
				mdn.setBzhiQi("999");
				mdn.setChpShuXing(tWzMaterial.getBy3());
				mdn.setJshDanWei(tWzMaterial.getMatUnit());
				mdn.setShlDanWei(tWzMaterial.getMatUnit());
				mdn.setShpTiaoMa(tWzMaterial.getBy1());
				mdn.setCfWenCeng(tWzMaterial.getBy2());
				mdn.setJiZhunwendu("1");
				mdn.setTiJiCm("1");
				mdn.setZhlKg("1");
				mdn.setChlKongZhi("N");
				mdn.setJfShpLei("10");
				mdn.setMpCengGao("99");
				mdn.setMpDanCeng("99");

				mdGoodsService.save(mdn);
			}

			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "物料添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新物料
	 *
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(TWzMaterialEntity tWzMaterial, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "物料更新成功";
		TWzMaterialEntity t = tWzMaterialService.get(TWzMaterialEntity.class, tWzMaterial.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(tWzMaterial, t);
			tWzMaterialService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "物料更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 物料新增页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(TWzMaterialEntity tWzMaterial, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tWzMaterial.getId())) {
			tWzMaterial = tWzMaterialService.getEntity(TWzMaterialEntity.class, tWzMaterial.getId());
			req.setAttribute("tWzMaterialPage", tWzMaterial);
		}
		return new ModelAndView("com/zzjee/wz/tWzMaterial-add");
	}
	/**
	 * 物料编辑页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(TWzMaterialEntity tWzMaterial, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tWzMaterial.getId())) {
			tWzMaterial = tWzMaterialService.getEntity(TWzMaterialEntity.class, tWzMaterial.getId());
			req.setAttribute("tWzMaterialPage", tWzMaterial);
		}
		return new ModelAndView("com/zzjee/wz/tWzMaterial-update");
	}

	/**
	 * 导入功能跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","tWzMaterialController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * 导出excel
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(TWzMaterialEntity tWzMaterial, HttpServletRequest request, HttpServletResponse response
			, DataGrid dataGrid, ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(TWzMaterialEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tWzMaterial, request.getParameterMap());
		List<TWzMaterialEntity> tWzMaterials = this.tWzMaterialService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"物料");
		modelMap.put(NormalExcelConstants.CLASS,TWzMaterialEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("物料列表", "导出人:"+ ResourceUtil.getSessionUser().getRealName(),
			"导出信息"));
		modelMap.put(NormalExcelConstants.DATA_LIST,tWzMaterials);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模板
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(TWzMaterialEntity tWzMaterial, HttpServletRequest request, HttpServletResponse response
			, DataGrid dataGrid, ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"物料");
    	modelMap.put(NormalExcelConstants.CLASS,TWzMaterialEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("物料列表", "导出人:"+ ResourceUtil.getSessionUser().getRealName(),
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
				List<TWzMaterialEntity> listTWzMaterialEntitys = ExcelImportUtil.importExcel(file.getInputStream(),TWzMaterialEntity.class,params);
				for (TWzMaterialEntity tWzMaterial : listTWzMaterialEntitys) {
					TWzMaterialEntity t = systemService.findUniqueByProperty(TWzMaterialEntity.class,"matCode",tWzMaterial.getMatClass());
					if(t!=null){
						MyBeanUtils.copyBeanNotNull2Bean(tWzMaterial, t);
						tWzMaterialService.updateEntitie(t);
					}else{
						tWzMaterialService.save(tWzMaterial);

					}
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
	@ApiOperation(value="物料列表信息",produces="application/json",httpMethod="GET")
	public ResponseMessage<List<TWzMaterialEntity>> list() {
		List<TWzMaterialEntity> listTWzMaterials=tWzMaterialService.getList(TWzMaterialEntity.class);
		return Result.success(listTWzMaterials);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="根据ID获取物料信息",notes="根据ID获取物料信息",httpMethod="GET",produces="application/json")
	public ResponseMessage<?> get(@ApiParam(required=true,name="id",value="ID")@PathVariable("id") String id) {
		TWzMaterialEntity task = tWzMaterialService.get(TWzMaterialEntity.class, id);
		if (task == null) {
			return Result.error("根据ID获取物料信息为空");
		}
		return Result.success(task);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="创建物料")
	public ResponseMessage<?> create(@ApiParam(name="物料对象")@RequestBody TWzMaterialEntity tWzMaterial, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<TWzMaterialEntity>> failures = validator.validate(tWzMaterial);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//保存
		try{
			tWzMaterialService.save(tWzMaterial);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("物料信息保存失败");
		}
		return Result.success(tWzMaterial);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="更新物料",notes="更新物料")
	public ResponseMessage<?> update(@ApiParam(name="物料对象")@RequestBody TWzMaterialEntity tWzMaterial) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<TWzMaterialEntity>> failures = validator.validate(tWzMaterial);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//保存
		try{
			tWzMaterialService.saveOrUpdate(tWzMaterial);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("更新物料信息失败");
		}

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return Result.success("更新物料信息成功");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value="删除物料")
	public ResponseMessage<?> delete(@ApiParam(name="id",value="ID",required=true)@PathVariable("id") String id) {
		logger.info("delete[{}]" + id);
		// 验证
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID不能为空");
		}
		try {
			tWzMaterialService.deleteEntityById(TWzMaterialEntity.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("物料删除失败");
		}

		return Result.success();
	}
}
