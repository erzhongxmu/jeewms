package com.zzjee.wzyw.controller;

import com.alibaba.fastjson.JSONArray;
import com.zzjee.util.ReportUtils;
import com.zzjee.wz.entity.TWzMaterialEntity;
import com.zzjee.wzyw.entity.TWzRkHeadEntity;
import com.zzjee.wzyw.entity.TWzRkItemDto;
import com.zzjee.wzyw.entity.TWzRkItemEntity;
import com.zzjee.wzyw.page.TWzRkHeadPage;
import com.zzjee.wzyw.service.TWzRkHeadServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.dao.jdbc.JdbcDao;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.*;
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
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Title: Controller
 * @Description: 入库抬头
 * @author onlineGenerator
 * @date 2018-05-20 21:43:08
 * @version V1.0
 *
 */
@Api(value="TWzRkHead",description="入库抬头",tags="tWzRkHeadController")
@Controller
@RequestMapping("/tWzRkHeadController")
public class TWzRkHeadController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TWzRkHeadController.class);

	@Autowired
	private TWzRkHeadServiceI tWzRkHeadService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;

	@Autowired
    JdbcDao jdbcDao;

	/**
	 * 入库抬头列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/wzyw/tWzRkHeadList");
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
	public void datagrid(TWzRkHeadEntity tWzRkHead,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TWzRkHeadEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tWzRkHead);
		try{
		//自定义追加查询条件
		String query_docDate_begin = request.getParameter("docDate_begin");
		String query_docDate_end = request.getParameter("docDate_end");
		String status = request.getParameter("status");
		if(StringUtil.isNotEmpty(status)){
			cq.ge("bpmStatus",status);
		}
		if(StringUtil.isNotEmpty(query_docDate_begin)){
			cq.ge("docDate", new SimpleDateFormat("yyyy-MM-dd").parse(query_docDate_begin));
		}
		if(StringUtil.isNotEmpty(query_docDate_end)){
			cq.le("docDate", new SimpleDateFormat("yyyy-MM-dd").parse(query_docDate_end));
		}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.tWzRkHeadService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除入库抬头
	 *
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(TWzRkHeadEntity tWzRkHead, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		tWzRkHead = systemService.getEntity(TWzRkHeadEntity.class, tWzRkHead.getId());
		String message = "入库抬头删除成功";
		try{
			tWzRkHeadService.delMain(tWzRkHead);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "入库抬头删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除入库抬头
	 *
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		String message = "入库抬头删除成功";
		try{
			for(String id:ids.split(",")){
				TWzRkHeadEntity tWzRkHead = systemService.getEntity(TWzRkHeadEntity.class,
				Integer.parseInt(id)
				);
				tWzRkHeadService.delMain(tWzRkHead);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "入库抬头删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加入库抬头
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(TWzRkHeadEntity tWzRkHead, TWzRkHeadPage tWzRkHeadPage, HttpServletRequest request) {
		List<TWzRkItemEntity> tWzRkItemList =  tWzRkHeadPage.getTWzRkItemList();
		AjaxJson j = new AjaxJson();
		String message = "添加成功";
		try{
//			try{
//
//				for (TWzRkItemEntity page : tWzRkItemList) {
//					TWzMaterialEntity wz  = systemService.findUniqueByProperty(TWzMaterialEntity.class,"matCode",page.getMatCode());
//					try{
//						if(!StringUtil.isEmpty(page.getMatPrice())){
//							wz.setMatPrice(new BigDecimal(page.getMatPrice()));
//							systemService.updateEntitie(wz);
//						}
//					}catch (Exception e){
//					}
//				}
//			}catch (Exception e){
//
//			}


			tWzRkHeadService.addMain(tWzRkHead, tWzRkItemList);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "入库抬头添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	/**
	 * 更新入库抬头
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(TWzRkHeadEntity tWzRkHead, TWzRkHeadPage tWzRkHeadPage, HttpServletRequest request) {
		List<TWzRkItemEntity> tWzRkItemList =  tWzRkHeadPage.getTWzRkItemList();
		AjaxJson j = new AjaxJson();
		String message = "更新成功";
		try{
			tWzRkHeadService.updateMain(tWzRkHead, tWzRkItemList);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "更新入库抬头失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 入库抬头新增页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(TWzRkHeadEntity tWzRkHead, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tWzRkHead.getId())) {
			tWzRkHead = tWzRkHeadService.getEntity(TWzRkHeadEntity.class, tWzRkHead.getId());
			req.setAttribute("tWzRkHeadPage", tWzRkHead);
		}
		return new ModelAndView("com/zzjee/wzyw/tWzRkHead-add");
	}

	/**
	 * 入库抬头编辑页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(TWzRkHeadEntity tWzRkHead, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tWzRkHead.getId())) {
			tWzRkHead = tWzRkHeadService.getEntity(TWzRkHeadEntity.class, tWzRkHead.getId());
			req.setAttribute("tWzRkHeadPage", tWzRkHead);
		}
		return new ModelAndView("com/zzjee/wzyw/tWzRkHead-update");
	}


	/**
	 * 加载明细列表[入库商品]
	 *
	 * @return
	 */
	@RequestMapping(params = "tWzRkItemList")
	public ModelAndView tWzRkItemList(TWzRkHeadEntity tWzRkHead, HttpServletRequest req) {

		//===================================================================================
		//获取参数
		Object id0 = tWzRkHead.getId();
		//===================================================================================
		//查询-入库商品
	    String hql0 = "from TWzRkItemEntity where 1 = 1 AND wZRK_HID = ? ";
	    try{
	    	List<TWzRkItemEntity> tWzRkItemEntityList = systemService.findHql(hql0,id0);
			req.setAttribute("tWzRkItemList", tWzRkItemEntityList);
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		return new ModelAndView("com/zzjee/wzyw/tWzRkItemList");
	}

	/**
	 * 加载明细列表[入库商品]不可编辑
	 *
	 * @return
	 */
	@RequestMapping(params = "tWzRkItemListUnEdit")
	public ModelAndView tWzRkItemListUnEdit(TWzRkHeadEntity tWzRkHead, HttpServletRequest req) {

		//===================================================================================
		//获取参数
		Object id0 = tWzRkHead.getId();
		//===================================================================================
		//查询-入库商品
		String hql0 = "from TWzRkItemEntity where 1 = 1 AND wZRK_HID = ? ";
		try{
			List<TWzRkItemEntity> tWzRkItemEntityList = systemService.findHql(hql0,id0);
			req.setAttribute("tWzRkItemList", tWzRkItemEntityList);
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		return new ModelAndView("com/zzjee/wzyw/tWzRkItemListUnEdit");
	}
    /**
    * 导出excel
    *
    * @param request
    * @param response
    */
    @RequestMapping(params = "exportXls")
    public String exportXls(TWzRkHeadEntity tWzRkHead, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap map) {
    	CriteriaQuery cq = new CriteriaQuery(TWzRkHeadEntity.class, dataGrid);
    	//查询条件组装器
    	org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tWzRkHead);
    	try{
    	//自定义追加查询条件
    	}catch (Exception e) {
    		throw new BusinessException(e.getMessage());
    	}
    	cq.add();
    	List<TWzRkHeadEntity> list=this.tWzRkHeadService.getListByCriteriaQuery(cq, false);
    	List<TWzRkHeadPage> pageList=new ArrayList<TWzRkHeadPage>();
        if(list!=null&&list.size()>0){
        	for(TWzRkHeadEntity entity:list){
        		try{
        		TWzRkHeadPage page=new TWzRkHeadPage();
        		   MyBeanUtils.copyBeanNotNull2Bean(entity,page);
            	    Object id0 = entity.getId();
				    String hql0 = "from TWzRkItemEntity where 1 = 1 AND wZRK_HID = ? ";
        	        List<TWzRkItemEntity> tWzRkItemEntityList = systemService.findHql(hql0,id0.toString());
            		page.setTWzRkItemList(tWzRkItemEntityList);
            		pageList.add(page);
            	}catch(Exception e){
            		logger.info(e.getMessage());
            	}
            }
        }
        map.put(NormalExcelConstants.FILE_NAME,"入库抬头");
        map.put(NormalExcelConstants.CLASS, TWzRkHeadPage.class);
        map.put(NormalExcelConstants.PARAMS,new ExportParams("入库抬头列表", "导出人:"+ ResourceUtil.getSessionUser().getRealName(),
            "导出信息"));
        map.put(NormalExcelConstants.DATA_LIST,pageList);
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

    /**
	 * 通过excel导入数据
	 * @param request
	 * @param
	 * @return
	 */
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
			params.setHeadRows(2);
			params.setNeedSave(true);
			try {
				List<TWzRkHeadPage> list =  ExcelImportUtil.importExcel(file.getInputStream(), TWzRkHeadPage.class, params);
				TWzRkHeadEntity entity1=null;
				for (TWzRkHeadPage page : list) {
					entity1=new TWzRkHeadEntity();
					MyBeanUtils.copyBeanNotNull2Bean(page,entity1);
		            tWzRkHeadService.addMain(entity1, page.getTWzRkItemList());
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
	/**
	* 导出excel 使模板
	*/
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(ModelMap map) {
		map.put(NormalExcelConstants.FILE_NAME,"入库抬头");
		map.put(NormalExcelConstants.CLASS, TWzRkHeadPage.class);
		map.put(NormalExcelConstants.PARAMS,new ExportParams("入库抬头列表", "导出人:"+ ResourceUtil.getSessionUser().getRealName(),
		"导出信息"));
		map.put(NormalExcelConstants.DATA_LIST,new ArrayList());
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	* 导入功能跳转
	*
	* @return
	*/
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name", "tWzRkHeadController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}


 	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="入库抬头列表信息",produces="application/json",httpMethod="GET")
	public ResponseMessage<List<TWzRkHeadPage>> list() {
		List<TWzRkHeadEntity> list= tWzRkHeadService.getList(TWzRkHeadEntity.class);
    	List<TWzRkHeadPage> pageList=new ArrayList<TWzRkHeadPage>();
        if(list!=null&&list.size()>0){
        	for(TWzRkHeadEntity entity:list){
        		try{
        			TWzRkHeadPage page=new TWzRkHeadPage();
        		   MyBeanUtils.copyBeanNotNull2Bean(entity,page);
					Object id0 = entity.getId();
				     String hql0 = "from TWzRkItemEntity where 1 = 1 AND wZRK_HID = ? ";
	    			List<TWzRkItemEntity> tWzRkItemOldList = this.tWzRkHeadService.findHql(hql0,id0);
            		page.setTWzRkItemList(tWzRkItemOldList);
            		pageList.add(page);
            	}catch(Exception e){
            		logger.info(e.getMessage());
            	}
            }
        }
		return Result.success(pageList);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="根据ID获取入库抬头信息",notes="根据ID获取入库抬头信息",httpMethod="GET",produces="application/json")
	public ResponseMessage<?> get(@ApiParam(required=true,name="id",value="ID")@PathVariable("id") String id) {
		TWzRkHeadEntity task = tWzRkHeadService.get(TWzRkHeadEntity.class, id);
		if (task == null) {
			return Result.error("根据ID获取入库抬头信息为空");
		}
		TWzRkHeadPage page = new TWzRkHeadPage();
		try {
			MyBeanUtils.copyBeanNotNull2Bean(task, page);
				Object id0 = task.getId();
		    String hql0 = "from TWzRkItemEntity where 1 = 1 AND wZRK_HID = ? ";
			List<TWzRkItemEntity> tWzRkItemOldList = this.tWzRkHeadService.findHql(hql0,id0);
    		page.setTWzRkItemList(tWzRkItemOldList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.success(page);
	}

 	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="创建入库抬头")
	public ResponseMessage<?> create(@ApiParam(name="入库抬头对象")@RequestBody TWzRkHeadPage tWzRkHeadPage, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<TWzRkHeadPage>> failures = validator.validate(tWzRkHeadPage);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//保存
		List<TWzRkItemEntity> tWzRkItemList =  tWzRkHeadPage.getTWzRkItemList();

		TWzRkHeadEntity tWzRkHead = new TWzRkHeadEntity();
		try{
			MyBeanUtils.copyBeanNotNull2Bean(tWzRkHeadPage,tWzRkHead);
		}catch(Exception e){
            logger.info(e.getMessage());
            return Result.error("保存入库抬头失败");
        }
		tWzRkHeadService.addMain(tWzRkHead, tWzRkItemList);

		return Result.success(tWzRkHead);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="更新入库抬头",notes="更新入库抬头")
	public ResponseMessage<?> update(@RequestBody TWzRkHeadPage tWzRkHeadPage) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<TWzRkHeadPage>> failures = validator.validate(tWzRkHeadPage);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//保存
		List<TWzRkItemEntity> tWzRkItemList =  tWzRkHeadPage.getTWzRkItemList();

		TWzRkHeadEntity tWzRkHead = new TWzRkHeadEntity();
		try{
			MyBeanUtils.copyBeanNotNull2Bean(tWzRkHeadPage,tWzRkHead);
		}catch(Exception e){
            logger.info(e.getMessage());
            return Result.error("入库抬头更新失败");
        }
		tWzRkHeadService.updateMain(tWzRkHead, tWzRkItemList);

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return Result.success();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value="删除入库抬头")
	public ResponseMessage<?> delete(@ApiParam(name="id",value="ID",required=true)@PathVariable("id") String id) {
		logger.info("delete[{}]" + id);
		// 验证
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID不能为空");
		}
		try {
			TWzRkHeadEntity tWzRkHead = tWzRkHeadService.get(TWzRkHeadEntity.class, id);
			tWzRkHeadService.delMain(tWzRkHead);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("入库抬头删除失败");
		}

		return Result.success();
	}


	@RequestMapping(params = "doPrint")
	@ResponseBody
	public void downReceiveExcel(String id,
								 HttpServletRequest request, HttpServletResponse response){
		OutputStream fileOut = null;
		BufferedImage bufferImg = null;
		String codedFileName = null;
		TWzRkHeadEntity tWzRkHead = systemService.getEntity(TWzRkHeadEntity.class,
				Integer.parseInt(id));// 获取抬头
		String sql = "select r.id,CONCAT(r.mat_code,'/',r.mat_name) AS mat_name,(SELECT m.mat_guige FROM t_wz_material m WHERE m.mat_location = r.mat_location AND m.mat_code = r.mat_code) spec," +
				"r.mat_unit,r.mat_location,r.mat_qty,r.mat_price,(r.mat_price * r.mat_qty) AS mat_amount from t_wz_rk_item r WHERE r.wzrk_hid = " + id;
		List<TWzRkItemDto> tWzRkItemOldList =  jdbcDao.find(sql,TWzRkItemDto.class,null);
		try {
			StringBuffer sber = new StringBuffer();

			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

			// 进行转码，使其支持中文文件名
			codedFileName = java.net.URLEncoder.encode("中文", "UTF-8");
			response.setHeader("content-disposition", "attachment;filename="
					+ id + ".xls");

			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("入库单");
			sheet.setMargin(HSSFSheet.TopMargin,0.5);// 页边距（上）
			sheet.setMargin(HSSFSheet.BottomMargin,0.5);// 页边距（下）
			sheet.setMargin(HSSFSheet.LeftMargin,0.8);// 页边距（左）
			sheet.setMargin(HSSFSheet.RightMargin,0.5);// 页边距（右
			sheet.setColumnWidth(0, 5 * 256);
			sheet.setColumnWidth(1, 15 * 256);
			sheet.setColumnWidth(2, 10 * 200);
			sheet.setColumnWidth(3, 5 * 256);
			sheet.setColumnWidth(4, 10 * 256);
			sheet.setColumnWidth(5, 10 * 256);
			sheet.setColumnWidth(6, 10 * 256);
			sheet.setColumnWidth(7, 10 * 256);
			sheet.setColumnWidth(8, 15 * 256);
            // { "序号", "编码/商品名称", "规格", "单位", "仓库","数量","单价","金额","备注" }
			// sheet.setColumnWidth(6, 8 * 256);
			// sheet.setColumnWidth(7, 8 * 256);
			// sheet.setColumnWidth(8, 8 * 256);
			// 创建第一行
			Row row = sheet.createRow((short) 0); // 第一行空白

			// 创建两种单元格格式
			CellStyle cs = wb.createCellStyle();
			CellStyle cs2 = wb.createCellStyle();
			CellStyle cs3 = wb.createCellStyle();
			CellStyle cs4 = wb.createCellStyle();
			CellStyle cs5 = wb.createCellStyle();
			// 创建两种字体
			Font f = wb.createFont();
			Font f2 = wb.createFont();

			// 创建第一种字体样式（用于列名）
			f.setFontHeightInPoints((short) 12);
			f.setColor(IndexedColors.BLACK.getIndex());
			f.setBoldweight(Font.BOLDWEIGHT_BOLD);

			// 创建第二种字体样式（用于值）
			f2.setFontHeightInPoints((short) 8);
			f2.setColor(IndexedColors.BLACK.getIndex());



			// 设置第一种单元格的样式（用于列名）
			cs.setFont(f);
			cs.setBorderLeft(CellStyle.BORDER_NONE);
			cs.setBorderRight(CellStyle.BORDER_NONE);
			cs.setBorderTop(CellStyle.BORDER_NONE);
			cs.setBorderBottom(CellStyle.BORDER_NONE);
			cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);

			// 设置第二种单元格的样式（用于值）
			cs2.setFont(f2);
			cs2.setBorderLeft(CellStyle.BORDER_NONE);
			cs2.setBorderRight(CellStyle.BORDER_NONE);
			cs2.setBorderTop(CellStyle.BORDER_NONE);
			cs2.setBorderBottom(CellStyle.BORDER_NONE);
			cs2.setWrapText(true);

            // 设置第三种单元格的样式
			cs3.setFont(f2);
			cs3.setBorderLeft(CellStyle.BORDER_THIN);
			cs3.setBorderRight(CellStyle.BORDER_THIN);
			cs3.setBorderTop(CellStyle.BORDER_THIN);
			cs3.setBorderBottom(CellStyle.BORDER_THIN);
			cs3.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中


			// 设置第四种单元格的样式
			cs4.setFont(f2);
			cs4.setBorderLeft(CellStyle.BORDER_THIN);
			cs4.setBorderRight(CellStyle.BORDER_THIN);
			cs4.setBorderTop(CellStyle.BORDER_THIN);
			cs4.setBorderBottom(CellStyle.BORDER_THIN);


			// 设置第五种单元格的样式
			cs5.setFont(f2);
			cs5.setBorderLeft(CellStyle.BORDER_NONE);
			cs5.setBorderRight(CellStyle.BORDER_NONE);
			cs5.setBorderTop(CellStyle.BORDER_NONE);
			cs5.setBorderBottom(CellStyle.BORDER_NONE);


			Row row1 = sheet.createRow((short) 1); // 第二行标题
			row1.setHeight((short) 700);
			Cell cellTitle = row1.createCell(0);
			cellTitle.setCellValue(ResourceUtil.getConfigByName("comname")+"采购入库单");
			cellTitle.setCellStyle(cs);
			Row row2 = sheet.createRow((short) 2); // 第三行
			row2.setHeight((short) 500);
			Cell cell21 = row2.createCell(0);
			cell21.setCellValue("供应商：" + tWzRkHead.getVendorName());
			cell21.setCellStyle(cs2);
			Cell cell22 = row2.createCell(3);

			cell22.setCellValue("单据日期：" + DateUtils.date2Str(tWzRkHead.getDocDate(), DateUtils.date_sdf));
			cell22.setCellStyle(cs2);
			Cell cell23 = row2.createCell(6);

			cell23.setCellValue("入库单号：" + tWzRkHead.getId());
			cell23.setCellStyle(cs2);

			// 合并单元格
			CellRangeAddress c = new CellRangeAddress(0, 0, 0, 8); // 第一行空白
			CellRangeAddress c0 = new CellRangeAddress(1, 1, 0, 8);// 第二行标题
			CellRangeAddress c11 = new CellRangeAddress(2, 2, 0, 2);// 第三行供应商
			CellRangeAddress c12 = new CellRangeAddress(2, 2, 3, 5);// 第三行单据日期
			CellRangeAddress c13 = new CellRangeAddress(2, 2, 6, 8);// 第三行单据编号

			sheet.addMergedRegion(c);
			sheet.addMergedRegion(c0);
			sheet.addMergedRegion(c11);
			sheet.addMergedRegion(c12);
			sheet.addMergedRegion(c13);


			Row rowColumnName = sheet.createRow((short) 3); // 列名
			rowColumnName.setHeight((short) 500);
			String[] columnNames = { "序号", "编码/商品名称", "规格", "单位", "仓库","数量","单价",
					"金额","备注" };
            Double totalAmount = 0.00;
			Double totalNumber = 0.00;
			for (int i = 0; i < columnNames.length; i++) {
				Cell cell = rowColumnName.createCell(i);
				cell.setCellValue(columnNames[i]);
				cell.setCellStyle(cs3);
			}
			int cellsNum = 3;
			int cerconNo = 1;
			for (int i = 0; i < tWzRkItemOldList.size(); i++) {
				TWzRkItemDto entity = tWzRkItemOldList.get(i);
				cellsNum++;
				Row rowColumnValue = sheet.createRow((short) cellsNum); // 列名
				rowColumnValue.setHeight((short) 500);
				Cell cell1 = rowColumnValue.createCell(0);
				cell1.setCellValue(cerconNo);
				cell1.setCellStyle(cs3);
				Cell cell2 = rowColumnValue.createCell(1);


				try {

					cell2.setCellStyle(cs3);
					cell2.setCellValue(entity.getMatName());
				}catch (Exception e){

				}
				try {

					Cell cell3 = rowColumnValue.createCell(2);
					cell3.setCellValue(entity.getSpec());
					cell3.setCellStyle(cs3);
				}catch (Exception e){

				}
				try {

					Cell cell4 = rowColumnValue.createCell(3);
					cell4.setCellValue(entity.getMatUnit());
					cell4.setCellStyle(cs3);
				}catch (Exception e){

				}
				try {

					Cell cell5 = rowColumnValue.createCell(4);
					cell5.setCellValue(entity.getMatLocation());
					cell5.setCellStyle(cs3);
				}catch (Exception e){

				}
				try {

					Cell cell6 = rowColumnValue.createCell(5);
//					cell6.setCellValue(entity.getMatQty());
					cell6.setCellValue(StringUtil.moneyToString(entity.getMatQty(),"#.0000"));
					cell6.setCellStyle(cs3);
					totalNumber = totalNumber + Double.parseDouble(entity.getMatQty());
				}catch (Exception e){

				}
				try {

					Cell cell7 = rowColumnValue.createCell(6);
//					cell7.setCellValue(entity.getMatPrice());
					cell7.setCellValue(StringUtil.moneyToString(entity.getMatPrice(),"#.00"));

					cell7.setCellStyle(cs3);
				}catch (Exception e){

				}
				try {

					Cell cell8 = rowColumnValue.createCell(7);
//					cell8.setCellValue(entity.getMatAmount());
					cell8.setCellValue(StringUtil.moneyToString(entity.getMatAmount(),"#.00"));

					cell8.setCellStyle(cs3);
					totalAmount = totalAmount + Double.parseDouble(entity.getMatAmount());
				}catch (Exception e){

				}
				try {

					Cell cell9 = rowColumnValue.createCell(8);
					cell9.setCellValue("");
					cell9.setCellStyle(cs3);
				}catch (Exception e){

				}
				cerconNo++;
			}

			//底部合计金额与数量
			Row rowColumnInfo = sheet.createRow((short) 1 + cellsNum); // 列名
			Cell cellrow = rowColumnInfo.createCell(0);
			cellrow.setCellValue("合计");
			cellrow.setCellStyle(cs4);
			Cell cellTotatl2 = rowColumnInfo.createCell(5);
			cellTotatl2.setCellValue(StringUtil.moneyToString(totalNumber,"#.0000"));
			cellTotatl2.setCellStyle(cs3);
			Cell cellTotatl3 = rowColumnInfo.createCell(6);
			cellTotatl3.setCellValue("");
			cellTotatl3.setCellStyle(cs3);
			Cell cellTotatl4 = rowColumnInfo.createCell(7);
			cellTotatl4.setCellValue(StringUtil.moneyToString(totalAmount,"#.00"));
			cellTotatl4.setCellStyle(cs3);
			Cell cellTotatl5 = rowColumnInfo.createCell(8);
			cellTotatl5.setCellValue("");
			cellTotatl5.setCellStyle(cs3);
			//合并单元格
			CellRangeAddress cellTotal = new CellRangeAddress(1 + cellsNum, 1 + cellsNum, 0, 4);
            sheet.addMergedRegion(cellTotal);
			ReportUtils.setBorderStyle(HSSFCellStyle.BORDER_THIN, cellTotal, sheet, wb);

            //底部合计金额大写转为汉字
			Row rowColumnInfo2 = sheet.createRow((short) 2 + cellsNum); // 列名
			Cell cellrow2 = rowColumnInfo2.createCell(0);
			cellrow2.setCellValue("合计金额大写：" + ReportUtils.number2CNMontrayUnit(new BigDecimal(StringUtil.moneyToString(totalAmount,"#.00"))));
			cellrow2.setCellStyle(cs4);
			//合并单元格
			CellRangeAddress cellAmount = new CellRangeAddress(2 + cellsNum, 2 + cellsNum, 0, 8);
			sheet.addMergedRegion(cellAmount);
			ReportUtils.setBorderStyle(HSSFCellStyle.BORDER_THIN, cellAmount, sheet, wb);

			//底部签字
			Row rowColumnInfo3 = sheet.createRow((short) 5 + cellsNum); // 列名
			Cell cellrow3 = rowColumnInfo3.createCell(0);
			cellrow3.setCellValue("制单人：                              审核人：                                收货人：");
			cellrow3.setCellStyle(cs5);

			//输出文件
			fileOut = response.getOutputStream();
			HSSFPrintSetup printSetup = sheet.getPrintSetup();
			printSetup.setPaperSize(HSSFPrintSetup.A5_PAPERSIZE);
			printSetup.setLandscape(true);
			wb.write(fileOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	@RequestMapping(params = "doUpdatesp")
	@ResponseBody
	public AjaxJson doUpdatesp(String id, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String message = "审批成功";
		try{
			TWzRkHeadEntity tWzRkHead = systemService.get(TWzRkHeadEntity.class,Integer.parseInt(id));
			tWzRkHead.setBpmStatus("1");
			String hql0 = "from TWzRkItemEntity where 1 = 1 AND wzrk_hid = ? ";
			List<TWzRkItemEntity> tWzRkItemList = systemService.findHql(hql0,id);
			//===================================================================================
			//查询-采购订单行项目
             try{

				for (TWzRkItemEntity page : tWzRkItemList) {
					page.setBpmStatus("1");
					systemService.saveOrUpdate(page);
					TWzMaterialEntity wz  = systemService.findUniqueByProperty(TWzMaterialEntity.class,"matCode",page.getMatCode());
					try{
						if(!StringUtil.isEmpty(page.getMatPrice())){
							wz.setMatPrice(new BigDecimal(page.getMatPrice()));
							systemService.updateEntitie(wz);
						}
					}catch (Exception e){
					}
				}
			}catch (Exception e){

			}
			systemService.saveOrUpdate(tWzRkHead);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "审批失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
}
