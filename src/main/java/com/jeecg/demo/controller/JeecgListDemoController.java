package com.jeecg.demo.controller;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
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
import org.jeecgframework.p3.core.util.oConvertUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSLog;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Pie;
import com.jeecg.demo.dao.JeecgMinidaoDao;
import com.jeecg.demo.entity.JeecgDemoEntity;
import com.jeecg.demo.entity.JeecgDemoPage;
import com.jeecg.demo.entity.JeecgLogReport;
import com.jeecg.demo.service.JeecgDemoServiceI;

/**   
 * @Title: Controller  
 * @Description: jeecg_demo
 * @author onlineGenerator
 * @date 2017-03-22 20:11:23
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/jeecgListDemoController")
public class JeecgListDemoController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(JeecgListDemoController.class);

	@Autowired
	private JeecgDemoServiceI jeecgDemoService;
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private JeecgMinidaoDao jeecgMinidaoDao;


	
	/**
	 * ??????minidao????????????
	 * @param request
	 * @return
	 */
	//JeecgListDemoController.do?minidaoListDemo
	@RequestMapping(params = "minidaoListDemo")
	public ModelAndView minidaoListDemo(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/demo/taglist_minidao");
	}
	
	/**
	 * ???????????????
	 */
	//JeecgListDemoController.do?rowListDemo
	@RequestMapping(params = "rowListDemo")
	public ModelAndView rowListDemo(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/demo/list_rowedtior");
	}
	
	/**
	 * jeecg_demo?????? ????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/demo/jeecgDemoList");
	}
	
	/**
	 * ?????????????????????
	 */
	//JeecgListDemoController.do?mysearchListDemo
	@RequestMapping(params = "mysearchListDemo")
	public ModelAndView mysearchListDemo(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/demo/taglist_mysearch");
	}
	
	
	@RequestMapping(params = "minidaoDatagrid")
	public void minidaoDatagrid(JeecgDemoEntity jeecgDemo,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		/**
		 * ?????????minidao?????????springjdbc???????????????????????????????????????????????????????????????????????????
		 * ???????????????????????????{USER_NAME}
		 * ???????????????????????????{userName}
		 */
		List<JeecgDemoEntity> list = jeecgMinidaoDao.getAllEntities(jeecgDemo, dataGrid.getPage(), dataGrid.getRows());
		Integer count = jeecgMinidaoDao.getCount();
		dataGrid.setTotal(count);
		dataGrid.setResults(list);
		String total_salary = String.valueOf(jeecgMinidaoDao.getSumSalary());
		/*
		 * ?????????????????? ?????????:???(????????????????????????????????????????????????) ???????????? ??? , ??????
		 */
		dataGrid.setFooter("salary:"+(total_salary.equalsIgnoreCase("null")?"0.0":total_salary)+",age,email:??????");
		TagUtil.datagrid(response, dataGrid);
	}
	

	/**
	 * easyui AJAX????????????
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(JeecgDemoEntity jeecgDemo,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(JeecgDemoEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jeecgDemo, request.getParameterMap());
		try{
		//???????????????????????????
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.jeecgDemoService.getDataGridReturn(cq, true);
		//String total_salary = String.valueOf(jeecgMinidaoDao.getSumSalary());
		/*
		 * ?????????????????? ?????????:???(????????????????????????????????????????????????) ???????????? ??? , ??????
		 */
		//dataGrid.setFooter("salary:"+(total_salary.equalsIgnoreCase("null")?"0.0":total_salary)+",age,email:??????");
		List<JeecgDemoEntity> list=dataGrid.getResults();
		Map<String,Map<String,Object>> extMap = new HashMap<String, Map<String,Object>>();
		for(JeecgDemoEntity temp:list){
		        //???????????????????????????????????????????????????
		        Map m = new HashMap();
		        m.put("extField",this.jeecgMinidaoDao.getOrgCode(temp.getDepId()));
		        extMap.put(temp.getId(), m);
		}
		//dataGrid.setFooter("extField,salary,age,name:??????");
		TagUtil.datagrid(response, dataGrid,extMap);
		dataGrid.setFooter("salary,age,name:??????");
		TagUtil.datagrid(response, dataGrid);
	}
	
	@RequestMapping(params = "addTab")
	public ModelAndView addTab(HttpServletRequest request) {
		//String type = oConvertUtils.getString(request.getParameter("type"));
		return new ModelAndView("com/jeecg/demo/demoTab");
		
	}
	
	@RequestMapping(params = "goCheck")
	public ModelAndView goCheck( HttpServletRequest request) {
		logger.info("----??????-----");
		String id=request.getParameter("id");
		if (StringUtil.isNotEmpty(id)) {
			JeecgDemoEntity jeecgDemo = jeecgDemoService.getEntity(JeecgDemoEntity.class, id);
			request.setAttribute("jeecgDemoPage", jeecgDemo);
		}
		return new ModelAndView("com/jeecg/demo/jeecgDemo-check");
		
	}
	
	@RequestMapping(params = "doCheck")
	@ResponseBody
	public AjaxJson doCheck(String content,String id,String status) {
		logger.info("-------????????????:"+content);//demo???????????????,???????????????????????????
		String message = null;
		AjaxJson j = new AjaxJson();
		JeecgDemoEntity jeecgDemo = systemService.getEntity(JeecgDemoEntity.class, id);
		message = "????????????";
		try{
			jeecgDemo.setStatus(status);
			this.jeecgDemoService.updateEntitie(jeecgDemo);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	@RequestMapping(params = "addWithbtn")
	public ModelAndView addWithbtn(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/demo/jeecgDemo-add-btn");
		
	}
	
	/**
	 * JeecgDemo ??????????????????
	 * @param jeecgDemo
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "print")
	public ModelAndView print(JeecgDemoEntity jeecgDemo, HttpServletRequest req) {
		// ??????????????????
		List<TSDepart> departList = systemService.getList(TSDepart.class);
		req.setAttribute("departList", departList);

		if (StringUtil.isNotEmpty(jeecgDemo.getId())) {
			jeecgDemo = jeecgDemoService.getEntity(JeecgDemoEntity.class, jeecgDemo.getId());
			req.setAttribute("jgDemo", jeecgDemo);
			if ("0".equals(jeecgDemo.getSex())) {
                req.setAttribute("sex", "???");
            }
			if ("1".equals(jeecgDemo.getSex())) {
                req.setAttribute("sex", "???");
            }
		}
		return new ModelAndView("com/jeecg/demo/jeecgDemo-print");
	}
	
	/**
	 * ??????jeecg_demo
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(JeecgDemoEntity jeecgDemo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		jeecgDemo = systemService.getEntity(JeecgDemoEntity.class, jeecgDemo.getId());
		message = "????????????";
		try{
			jeecgDemoService.delete(jeecgDemo);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * ????????????jeecg_demo
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????";
		try{
			for(String id:ids.split(",")){
				JeecgDemoEntity jeecgDemo = systemService.getEntity(JeecgDemoEntity.class, 
				id
				);
				jeecgDemoService.delete(jeecgDemo);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * ??????jeecg_demo
	 * 
	 * @param jeecgDemo
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(JeecgDemoEntity jeecgDemo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????";
		try{
			jeecgDemoService.save(jeecgDemo);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * ??????jeecg_demo
	 * 
	 * @param jeecgDemo
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(JeecgDemoEntity jeecgDemo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????";
		JeecgDemoEntity t = jeecgDemoService.get(JeecgDemoEntity.class, jeecgDemo.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(jeecgDemo, t);
			jeecgDemoService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * jeecg_demo??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(JeecgDemoEntity jeecgDemo, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jeecgDemo.getId())) {
			jeecgDemo = jeecgDemoService.getEntity(JeecgDemoEntity.class, jeecgDemo.getId());
			req.setAttribute("jeecgDemoPage", jeecgDemo);
		}
		return new ModelAndView("com/jeecg/demo/jeecgDemo-add");
	}
	/**
	 * jeecg_demo??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(JeecgDemoEntity jeecgDemo, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jeecgDemo.getId())) {
			jeecgDemo = jeecgDemoService.getEntity(JeecgDemoEntity.class, jeecgDemo.getId());
			req.setAttribute("jeecgDemoPage", jeecgDemo);
		}
		return new ModelAndView("com/jeecg/demo/jeecgDemo-update");
	}
	
	/**
	 * ??????????????????
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","JeecgListDemoController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * ??????excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(JeecgDemoEntity jeecgDemo,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(JeecgDemoEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jeecgDemo, request.getParameterMap());
		List<JeecgDemoEntity> jeecgDemos = this.jeecgDemoService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"jeecg_demo");
		modelMap.put(NormalExcelConstants.CLASS,JeecgDemoEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("jeecg_demo??????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST,jeecgDemos);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * ??????excel ?????????
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(JeecgDemoEntity jeecgDemo,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"jeecg_demo");
    	modelMap.put(NormalExcelConstants.CLASS,JeecgDemoEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("jeecg_demo??????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
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
				List<JeecgDemoEntity> listJeecgDemoEntitys = ExcelImportUtil.importExcel(file.getInputStream(),JeecgDemoEntity.class,params);
				for (JeecgDemoEntity jeecgDemo : listJeecgDemoEntitys) {
					jeecgDemoService.save(jeecgDemo);
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
	public List<JeecgDemoEntity> list() {
		List<JeecgDemoEntity> listJeecgDemos=jeecgDemoService.getList(JeecgDemoEntity.class);
		return listJeecgDemos;
	}
	
	/**
	 * ????????????/??????????????????
	 * @param page
	 * @return
	 */
	@RequestMapping(params = "saveRows")
	@ResponseBody
	public AjaxJson saveRows(JeecgDemoPage page){
		String message = null;
		List<JeecgDemoEntity> demos=page.getDemos();
		AjaxJson j = new AjaxJson();
		if(CollectionUtils.isNotEmpty(demos)){
			for(JeecgDemoEntity jeecgDemo:demos){
				if (StringUtil.isNotEmpty(jeecgDemo.getId())) {
					JeecgDemoEntity t =jeecgDemoService.get(JeecgDemoEntity.class, jeecgDemo.getId());
					try {
						message = "JeecgDemo??????: " + jeecgDemo.getName() + "???????????????";
						MyBeanUtils.copyBeanNotNull2Bean(jeecgDemo, t);
						jeecgDemoService.saveOrUpdate(t);
						systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						message = "JeecgDemo??????: " + jeecgDemo.getName() + "???????????????";
						//jeecgDemo.setStatus("0");
						jeecgDemoService.save(jeecgDemo);
						systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
		}
		return j;
	}
	
	//jeecgListDemoController.do?log
	@RequestMapping(params = "log")
	public ModelAndView log() {
		return new ModelAndView("com/jeecg/demo/logList");
	}
	
	//jeecgListDemoController.do?logDatagrid
	@RequestMapping(params = "logDatagrid")
	public void logDatagrid(HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TSLog.class, dataGrid);
		
		//????????????????????????
		String loglevel = request.getParameter("loglevel");
		if (loglevel != null && !"0".equals(loglevel)) {
			cq.eq("loglevel", org.jeecgframework.core.util.oConvertUtils.getShort(loglevel));
			cq.add();
		}
		//????????????????????????
        String operatetime_begin = request.getParameter("operatetime_begin");
        String operatetime_end = request.getParameter("operatetime_end");
        if(oConvertUtils.isNotEmpty(operatetime_begin)){
        	try {
				cq.ge("operatetime", DateUtils.parseDate(operatetime_begin, "yyyy-MM-dd hh:mm:ss"));
			} catch (ParseException e) {
				logger.error(e);
			}
        	cq.add();
        }
        if(oConvertUtils.isNotEmpty(operatetime_end)){
        	try {
				cq.le("operatetime", DateUtils.parseDate(operatetime_end, "yyyy-MM-dd hh:mm:ss"));
			} catch (ParseException e) {
				logger.error(e);
			}
        	cq.add();
        }
        this.systemService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	@RequestMapping(params = "goOnlyData")
	public ModelAndView goOnlyData(HttpServletRequest req,JeecgLogReport log) {
		return new ModelAndView("com/jeecg/demo/logrp-onlyData");
	}
	
	@RequestMapping(params = "logrpDatagrid")
	public void logrpDatagrid(HttpServletResponse response,JeecgLogReport log, DataGrid dataGrid) {
		//CriteriaQuery cq = new CriteriaQuery(JeecgLogReport.class, dataGrid);
		List<JeecgLogReport> list=this.jeecgMinidaoDao.getLogReportData(log);
		dataGrid.setResults(list);
		TagUtil.datagrid(response, dataGrid);
	}
	
	@RequestMapping(params = "goChart")
	public ModelAndView goChart(HttpServletRequest req,JeecgLogReport log) {
		List<Map<String, Object>> list=this.jeecgMinidaoDao.getLogChartData(log);
		net.sf.json.JSONArray arr=net.sf.json.JSONArray.fromObject(list);
		req.setAttribute("logs",arr);
		return new ModelAndView("com/jeecg/demo/logrp-chart");
	}
	@RequestMapping(params = "goEchart")
	public ModelAndView goEchart(HttpServletRequest req,JeecgLogReport log) {
		return new ModelAndView("com/jeecg/demo/echart");
	}
	@RequestMapping(params = "doEchart",method ={RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public AjaxJson doEchart(HttpServletRequest req,JeecgLogReport log) {
		//??????Option  
	    Option option = new Option();  
	    option.title("??????").tooltip(Trigger.axis).legend("????????????");  
	    //???????????????  
	    option.xAxis(new ValueAxis().boundaryGap(0d, 0.01));  
	    //???????????????  
	    CategoryAxis category = new CategoryAxis();  
	    //????????????  
	    Bar bar = new Bar("????????????");  
	    //????????????  
	    Pie pie = new Pie("????????????");  
	    //????????????  
		List<Map<String, Object>> list=this.jeecgMinidaoDao.getLogChartData(log);

	    for (Map<String, Object> objectMap : list) {  
	        //????????????  
	        category.data(objectMap.get("name"));  
	        //????????????????????????  
	        bar.data(objectMap.get("value"));  
	        //????????????  
			System.out.println("value************"+objectMap.get("value"));
	        pie.data(new PieData(objectMap.get("name").toString(), objectMap.get("value")));  
	    }  
	    //???????????????  
	    option.yAxis(category);  
	    //????????????????????????  
	    pie.center(900,380).radius(100);  
	    //????????????  
	    option.series(bar, pie);  


		AjaxJson j = new AjaxJson();
		j.setObj(option);
		System.out.println(j.toString());
		return j;
	}
}
