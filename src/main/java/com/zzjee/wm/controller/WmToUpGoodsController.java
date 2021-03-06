package com.zzjee.wm.controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.zzjee.wmutil.wmUtil;
import org.apache.commons.collections.CollectionUtils;
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
import org.jeecgframework.core.util.JSONHelper;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSBaseUser;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.zzjee.api.ResultDO;
import com.zzjee.md.entity.MvGoodsEntity;
import com.zzjee.wm.entity.WmInQmIEntity;
import com.zzjee.wm.entity.WmToUpGoodsEntity;
import com.zzjee.wm.page.wmtoupgoodspage;
import com.zzjee.wm.service.WmToUpGoodsServiceI;

/**
 * @Title: Controller
 * @Description: ????????????
 * @author erzhongxmu
 * @date 2017-09-11 15:08:46
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/wmToUpGoodsController")
public class WmToUpGoodsController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(WmToUpGoodsController.class);

	@Autowired
	private WmToUpGoodsServiceI wmToUpGoodsService;
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
		return new ModelAndView("com/zzjee/wm/wmToUpGoodsList");
	}

	/**
	 * easyui AJAX????????????
	 *
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(WmToUpGoodsEntity wmToUpGoods,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(WmToUpGoodsEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wmToUpGoods, request.getParameterMap());
		try{
			//???????????????????????????
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("createDate", "desc");
		cq.setOrder(map1);
		cq.add();
		this.wmToUpGoodsService.getDataGridReturn(cq, true);

		List<WmToUpGoodsEntity> resultold = dataGrid.getResults();
		List<WmToUpGoodsEntity> resultnew = new ArrayList<>();
		for(WmToUpGoodsEntity t:resultold){
			if (StringUtil.isEmpty(t.getGoodsName())){
				try{
					MvGoodsEntity goods = systemService.findUniqueByProperty(MvGoodsEntity.class, "goodsCode", t.getGoodsId());
					if(goods!=null){
						t.setGoodsName(goods.getGoodsName());
					}
				}catch (Exception e){

				}

			}

			resultnew.add(t);
		}
		dataGrid.setResults(resultnew);

		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * ??????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(WmToUpGoodsEntity wmToUpGoods, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		wmToUpGoods = systemService.getEntity(WmToUpGoodsEntity.class, wmToUpGoods.getId());
		message = "????????????????????????";
		try{
			wmToUpGoodsService.delete(wmToUpGoods);
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
				WmToUpGoodsEntity wmToUpGoods = systemService.getEntity(WmToUpGoodsEntity.class,
						id
				);
				wmToUpGoodsService.delete(wmToUpGoods);
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


	@RequestMapping(params = "doGettextzy",method ={RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public AjaxJson doGettextzy(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		MvGoodsEntity mvgoods = systemService.findUniqueByProperty(MvGoodsEntity.class, "goodsCode", oConvertUtils.getString(request.getParameter("goodsid"))) ;
		if(mvgoods==null){
			j.setSuccess(false);
			j.setMsg("??????????????????");
		}
		j.setObj(mvgoods);
		return j;
	}


	/**
	 * ??????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(WmToUpGoodsEntity wmToUpGoods, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		try{
			wmToUpGoods.setOrderId("ZY");
			//MvGoodsEntity mvgoods = new MvGoodsEntity();
			MvGoodsEntity mvgoods = systemService.findUniqueByProperty(
					MvGoodsEntity.class, "goodsCode",
					wmToUpGoods.getGoodsId());
			if(mvgoods!=null){
				wmToUpGoods.setGoodsName(mvgoods.getGoodsName());
				wmToUpGoods.setBaseUnit(mvgoods.getBaseunit());
				wmToUpGoods.setGoodsUnit(mvgoods.getShlDanWei());
				if (!mvgoods.getBaseunit().equals(mvgoods.getShlDanWei())) {
					try {
						wmToUpGoods.setBaseGoodscount(String.valueOf(Double
								.parseDouble(mvgoods.getChlShl())
								* Double.parseDouble(wmToUpGoods.getGoodsQua())));
					} catch (Exception e) {
						// TODO: handle exception
					}

				} else {
					wmToUpGoods.setBaseGoodscount(wmToUpGoods
							.getGoodsQua());
				}
				wmToUpGoodsService.save(wmToUpGoods);
				systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
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
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(WmToUpGoodsEntity wmToUpGoods, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		WmToUpGoodsEntity t = wmToUpGoodsService.get(WmToUpGoodsEntity.class, wmToUpGoods.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(wmToUpGoods, t);
			wmToUpGoodsService.saveOrUpdate(t);
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
	 * gengxin
	 * @param page
	 * @return
	 */
	@RequestMapping(params = "updateRows")
	@ResponseBody
	public AjaxJson updateRows(wmtoupgoodspage page){
//		String message = null;
		List<WmToUpGoodsEntity> demos=page.getUprows();
		AjaxJson j = new AjaxJson();
		if(CollectionUtils.isNotEmpty(demos)){
			for(WmToUpGoodsEntity jeecgDemo:demos){
				if (StringUtil.isNotEmpty(jeecgDemo.getId())) {
					WmToUpGoodsEntity t =systemService.get(WmToUpGoodsEntity.class, jeecgDemo.getId());
					try {


						MyBeanUtils.copyBeanNotNull2Bean(jeecgDemo, t);

						systemService.saveOrUpdate(t);


//						systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return j;
	}

	/**
	 * ??????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(WmToUpGoodsEntity wmToUpGoods, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(wmToUpGoods.getId())) {
			wmToUpGoods = wmToUpGoodsService.getEntity(WmToUpGoodsEntity.class, wmToUpGoods.getId());
			req.setAttribute("wmToUpGoodsPage", wmToUpGoods);
		}
		return new ModelAndView("com/zzjee/wm/wmToUpGoods-add");
	}
	/**
	 * ??????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(WmToUpGoodsEntity wmToUpGoods, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(wmToUpGoods.getId())) {
			wmToUpGoods = wmToUpGoodsService.getEntity(WmToUpGoodsEntity.class, wmToUpGoods.getId());
			req.setAttribute("wmToUpGoodsPage", wmToUpGoods);
		}
		return new ModelAndView("com/zzjee/wm/wmToUpGoods-update");
	}

	/**
	 * ??????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","wmToUpGoodsController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * ??????excel
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(WmToUpGoodsEntity wmToUpGoods,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(WmToUpGoodsEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wmToUpGoods, request.getParameterMap());
		List<WmToUpGoodsEntity> wmToUpGoodss = this.wmToUpGoodsService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"????????????");
		modelMap.put(NormalExcelConstants.CLASS,WmToUpGoodsEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("??????????????????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
				"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST,wmToUpGoodss);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * ??????excel ?????????
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(WmToUpGoodsEntity wmToUpGoods,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		modelMap.put(NormalExcelConstants.FILE_NAME,"????????????");
		modelMap.put(NormalExcelConstants.CLASS,WmToUpGoodsEntity.class);
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
				List<WmToUpGoodsEntity> listWmToUpGoodsEntitys = ExcelImportUtil.importExcel(file.getInputStream(),WmToUpGoodsEntity.class,params);
				for (WmToUpGoodsEntity wmToUpGoods : listWmToUpGoodsEntitys) {
					wmToUpGoodsService.save(wmToUpGoods);
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
	public ResponseEntity<?>  list(@RequestParam String username, @RequestParam String searchstr) {


		ResultDO D0 = new  ResultDO();
		List<WmToUpGoodsEntity> listWmToUpGoodss=wmToUpGoodsService.getList(WmToUpGoodsEntity.class);
		D0.setOK(true);
		D0.setObj(listWmToUpGoodss);
		return new ResponseEntity(D0, HttpStatus.OK);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		WmToUpGoodsEntity task = wmToUpGoodsService.get(WmToUpGoodsEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestParam String wmToUpGoodsstr, UriComponentsBuilder uriBuilder) {
		ResultDO D0 = new  ResultDO();
		WmToUpGoodsEntity wmToUpGoods = (WmToUpGoodsEntity) JSONHelper.json2Object(wmToUpGoodsstr,WmToUpGoodsEntity.class);
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<WmToUpGoodsEntity>> failures = validator.validate(wmToUpGoods);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		if(StringUtil.isEmpty(wmToUpGoods.getKuWeiBianMa())){
			D0.setOK(false);
			D0.setErrorMsg("??????????????????");
			return new ResponseEntity(D0, HttpStatus.OK);
		}else{
			if (!wmUtil.checkbin(wmToUpGoods.getKuWeiBianMa())) {
				D0.setOK(false);
				D0.setErrorMsg("???????????????");
				return new ResponseEntity(D0, HttpStatus.OK);            }
		}


		//??????
		try{
			D0.setOK(true);
			WmInQmIEntity wmInQmIEntity = systemService.get(WmInQmIEntity.class,wmToUpGoods.getWmToUpId());
			if(wmInQmIEntity!=null){
				if("Y".equals(wmInQmIEntity.getBinSta())){
					D0.setOK(false);
					D0.setErrorMsg("?????????????????????????????????");
					return new ResponseEntity(D0, HttpStatus.OK);
				}
				wmInQmIEntity.setBinSta("Y");
				systemService.updateEntitie(wmInQmIEntity);
			}else{
				D0.setOK(false);
				D0.setErrorMsg("???????????????????????????????????????");

				return new ResponseEntity(D0, HttpStatus.OK);
			}
			if(StringUtil.isNotEmpty(wmToUpGoods.getOrderIdI())){
				List<WmToUpGoodsEntity> wmToUpGoodsEntity = systemService.findByProperty(WmToUpGoodsEntity.class,"orderIdI",wmToUpGoods.getWmToUpId());
				if(wmToUpGoodsEntity!=null&&wmToUpGoodsEntity.size()>0){
					D0.setOK(false);
					D0.setErrorMsg("?????????????????????????????????");
					return new ResponseEntity(D0, HttpStatus.OK);
				}
			}else{
				D0.setOK(false);
				D0.setErrorMsg("?????????????????????????????????");

				return new ResponseEntity(D0, HttpStatus.OK);
			}

			wmToUpGoods.setGoodsName(wmInQmIEntity.getGoodsName());
			wmToUpGoods.setCreateDate(DateUtils.getDate());

			TSBaseUser user = systemService.findUniqueByProperty(TSBaseUser.class,"userName",wmToUpGoods.getCreateBy());
			if (user != null ) {
				wmToUpGoods.setCreateName(user.getRealName());
			}

			wmToUpGoodsService.save(wmToUpGoods);
		} catch (Exception e) {
			e.printStackTrace();
			D0.setOK(false);
		}

		return new ResponseEntity(D0, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody WmToUpGoodsEntity wmToUpGoods) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
//		Set<ConstraintViolation<WmToUpGoodsEntity>> failures = validator.validate(wmToUpGoods);
//		if (!failures.isEmpty()) {
//			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
//		}
		ResultDO D0 = new  ResultDO();
		//??????
		try{
			WmToUpGoodsEntity t = wmToUpGoodsService.get(WmToUpGoodsEntity.class,wmToUpGoods.getId());
			MyBeanUtils.copyBeanNotNull2Bean(wmToUpGoods, t);
			wmToUpGoodsService.saveOrUpdate(t);
			D0.setOK(true);

		} catch (Exception e) {
			e.printStackTrace();
			D0.setOK(false);
//			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity(D0, HttpStatus.OK);
		//???Restful???????????????204?????????, ?????????. ???????????????200?????????.
//		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		wmToUpGoodsService.deleteEntityById(WmToUpGoodsEntity.class, id);
	}
}
