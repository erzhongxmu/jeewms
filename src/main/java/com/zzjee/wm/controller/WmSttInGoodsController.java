package com.zzjee.wm.controller;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;

import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.JSONHelper;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.sms.util.Constants;
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
import com.zzjee.wm.entity.WmSttInGoodsEntity;
import com.zzjee.wm.entity.WmToDownGoodsEntity;
import com.zzjee.wm.entity.WmToUpGoodsEntity;
import com.zzjee.wm.service.WmSttInGoodsServiceI;
import com.zzjee.wmutil.wmUtil;

/**
 * @Title: Controller
 * @Description: ????????????
 * @author erzhongxmu
 * @date 2017-09-08 21:03:33
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/wmSttInGoodsController")
public class WmSttInGoodsController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(WmSttInGoodsController.class);

	@Autowired
	private WmSttInGoodsServiceI wmSttInGoodsService;
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
		return new ModelAndView("com/zzjee/wm/wmSttInGoodsList");
	}
	@RequestMapping(params = "listfp")
	public ModelAndView listfp(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/wm/wmSttInGoodsfpList");
	}
	@RequestMapping(params = "listcygz")
	public ModelAndView listcygz(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/wm/wmSttInGoodscygzList");
	}
	/**
	 * easyui AJAX????????????
	 *
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(WmSttInGoodsEntity wmSttInGoods,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(WmSttInGoodsEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wmSttInGoods, request.getParameterMap());
		try{
		//???????????????????????????


			// ???????????????????????????
			String query_createDate_begin = request.getParameter("createDate_begin1");
			String query_createDate_end = request.getParameter("createDate_end2");

			if (StringUtil.isNotEmpty(query_createDate_begin)) {
				cq.ge("createDate", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
						.parse(query_createDate_begin));
			}
			if (StringUtil.isNotEmpty(query_createDate_end)) {
				cq.le("createDate", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
						.parse(query_createDate_end));
			}


		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("createDate", "desc");
		cq.setOrder(map1);
		cq.notEq("sttSta", "?????????");
		cq.add();
		this.wmSttInGoodsService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}


	@RequestMapping(params = "datagridcygz")
	public void datagridcygz(WmSttInGoodsEntity wmSttInGoods,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(WmSttInGoodsEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wmSttInGoods, request.getParameterMap());
		try{
		//???????????????????????????
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("createDate", "desc");
		cq.setOrder(map1);
		cq.eq("sttSta", "?????????");
		cq.add();
		this.wmSttInGoodsService.getDataGridReturn(cq, true);
		List<WmSttInGoodsEntity> resultnew = new ArrayList<WmSttInGoodsEntity>();
		List<WmSttInGoodsEntity> resultold = dataGrid.getResults();
		for (WmSttInGoodsEntity wmSttInGoodsold : resultold) {
			if(!wmSttInGoodsold.getGoodsQua().equals(wmSttInGoodsold.getSttQua())){
				resultnew.add(wmSttInGoodsold);
			}
		}
		dataGrid.setResults(resultnew);
		dataGrid.setTotal(resultnew.size());
		TagUtil.datagrid(response, dataGrid);

	}




	@RequestMapping(params = "datagridfp")
	public void datagridfp(WmSttInGoodsEntity wmSttInGoods,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(WmSttInGoodsEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wmSttInGoods, request.getParameterMap());
		try{
		//???????????????????????????
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("createDate", "desc");
		cq.setOrder(map1);
		cq.eq("sttSta", "?????????");
		cq.add();
		this.wmSttInGoodsService.getDataGridReturn(cq, true);
		List<WmSttInGoodsEntity> resultnew = new ArrayList<WmSttInGoodsEntity>();
		List<WmSttInGoodsEntity> resultold = dataGrid.getResults();
		for (WmSttInGoodsEntity wmSttInGoodsold : resultold) {
			if(!wmSttInGoodsold.getGoodsQua().equals(wmSttInGoodsold.getSttQua())){
				resultnew.add(wmSttInGoodsold);
			}
		}
		dataGrid.setResults(resultnew);
		dataGrid.setTotal(resultnew.size());
		TagUtil.datagrid(response, dataGrid);

	}

	/**
	 * ??????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(WmSttInGoodsEntity wmSttInGoods, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		wmSttInGoods = systemService.getEntity(WmSttInGoodsEntity.class, wmSttInGoods.getId());
		message = "????????????????????????";
		try{
			wmSttInGoods.setSttSta("?????????");
			wmSttInGoodsService.saveOrUpdate(wmSttInGoods);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}



	@RequestMapping(params = "dorun")
	@ResponseBody
	public AjaxJson dorun(WmSttInGoodsEntity wmSttInGoods, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		wmSttInGoods = systemService.getEntity(WmSttInGoodsEntity.class, request
				.getParameter("id").toString());
		message = "??????????????????????????????";
		if(wmSttInGoods.getSttSta().equals("?????????")){

		if(!wmSttInGoods.getGoodsQua().equals(wmSttInGoods.getSttQua())){
			try{
//				wmSttInGoods.setSttQua("0");
				if(Double.parseDouble(wmSttInGoods.getGoodsQua()) > Double.parseDouble(wmSttInGoods.getSttQua())){
					//??????
					String goodsqua = Double.toString((Double.parseDouble(wmSttInGoods.getGoodsQua()) - Double.parseDouble(wmSttInGoods.getSttQua())));
					WmToDownGoodsEntity wmToDownGoods = new WmToDownGoodsEntity();

					wmToDownGoods.setOrderId("ZY");
					wmToDownGoods.setBinIdTo("PK");
					wmToDownGoods.setDownSta("?????????");
					wmToDownGoods.setGoodsQua(goodsqua);
					wmToDownGoods.setGoodsQuaok(goodsqua);
					MvGoodsEntity mvgoods = new MvGoodsEntity();
					mvgoods = systemService.findUniqueByProperty(
							MvGoodsEntity.class, "goodsCode",
							wmSttInGoods.getGoodsId());
					if(mvgoods!=null){
						wmToDownGoods.setGoodsId(wmSttInGoods.getGoodsId());
						wmToDownGoods.setBinIdFrom(wmSttInGoods.getTinId());
						wmToDownGoods.setKuWeiBianMa(wmSttInGoods.getBinId());
						wmToDownGoods.setGoodsProData(wmSttInGoods.getGoodsProData());
					wmToDownGoods.setBaseUnit(mvgoods.getBaseunit());
					wmToDownGoods.setGoodsUnit(mvgoods.getBaseunit());
					wmToDownGoods.setBaseGoodscount(wmToDownGoods
								.getGoodsQuaok());
					wmToDownGoods.setCusCode(wmSttInGoods.getCusCode());

					if(!wmUtil.checkstcok(wmToDownGoods.getKuWeiBianMa(),wmToDownGoods.getBinIdFrom(),wmToDownGoods.getGoodsId(),wmToDownGoods.getGoodsProData(),wmToDownGoods.getGoodsQua())){
						message = "????????????";
						j.setMsg(message);
						return j;

					};
					systemService.save(wmToDownGoods);
					wmSttInGoods.setSttSta("?????????");
					wmSttInGoodsService.saveOrUpdate(wmSttInGoods);
					systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
					}
				}else{
					//??????
					String goodsqua = Double.toString(( Double.parseDouble(wmSttInGoods.getSttQua()) - Double.parseDouble(wmSttInGoods.getGoodsQua())));

					WmToUpGoodsEntity wmToUpGoods = new WmToUpGoodsEntity();
					wmToUpGoods.setOrderId("ZY");
					MvGoodsEntity mvgoods = new MvGoodsEntity();
					mvgoods = systemService.findUniqueByProperty(
							MvGoodsEntity.class, "goodsCode",
							wmSttInGoods.getGoodsId());
					if(mvgoods!=null){
						wmToUpGoods.setBaseUnit(mvgoods.getBaseunit());
					wmToUpGoods.setGoodsUnit(mvgoods.getBaseunit());
					wmToUpGoods.setBaseGoodscount(goodsqua);
					wmToUpGoods.setGoodsQua(goodsqua);
					wmToUpGoods.setGoodsId(wmSttInGoods.getGoodsId());
					wmToUpGoods.setBinId(wmSttInGoods.getTinId());
					wmToUpGoods.setKuWeiBianMa(wmSttInGoods.getBinId());
					wmToUpGoods.setCusCode(wmSttInGoods.getCusCode());
					wmToUpGoods.setGoodsProData(wmSttInGoods.getGoodsProData());
					systemService.save(wmToUpGoods);
					wmSttInGoods.setSttSta("?????????");
					wmSttInGoodsService.saveOrUpdate(wmSttInGoods);
					systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
					}
				}

			}catch(Exception e){
				e.printStackTrace();
				message = "??????????????????????????????";
				throw new BusinessException(e.getMessage());
			}
		}else{
			message = "??????????????????????????????";
		}
		}
		j.setMsg(message);
		return j;
	}


	@RequestMapping(params = "dostt")
	@ResponseBody
	public AjaxJson dostt(WmSttInGoodsEntity wmSttInGoods, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		wmSttInGoods = systemService.getEntity(WmSttInGoodsEntity.class, request
				.getParameter("id").toString());
		message = "????????????????????????";
		try{
			wmSttInGoods.setSttQua("0");
			wmSttInGoods.setSttSta("?????????");
			wmSttInGoodsService.save(wmSttInGoods);
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
				WmSttInGoodsEntity wmSttInGoods = systemService.getEntity(WmSttInGoodsEntity.class,
				id
				);
				wmSttInGoodsService.delete(wmSttInGoods);
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
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(WmSttInGoodsEntity wmSttInGoods, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		try{
			wmSttInGoods.setSttSta("?????????");
			wmSttInGoodsService.save(wmSttInGoods);
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
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(WmSttInGoodsEntity wmSttInGoods, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		WmSttInGoodsEntity t = wmSttInGoodsService.get(WmSttInGoodsEntity.class, wmSttInGoods.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(wmSttInGoods, t);
			wmSttInGoodsService.saveOrUpdate(t);
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
	public ModelAndView goAdd(WmSttInGoodsEntity wmSttInGoods, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(wmSttInGoods.getId())) {
			wmSttInGoods = wmSttInGoodsService.getEntity(WmSttInGoodsEntity.class, wmSttInGoods.getId());
			req.setAttribute("wmSttInGoodsPage", wmSttInGoods);
		}
		return new ModelAndView("com/zzjee/wm/wmSttInGoods-add");
	}
	/**
	 * ??????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(WmSttInGoodsEntity wmSttInGoods, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(wmSttInGoods.getId())) {
			wmSttInGoods = wmSttInGoodsService.getEntity(WmSttInGoodsEntity.class, wmSttInGoods.getId());
			req.setAttribute("wmSttInGoodsPage", wmSttInGoods);
		}
		return new ModelAndView("com/zzjee/wm/wmSttInGoods-update");
	}

	/**
	 * ??????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","wmSttInGoodsController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * ??????excel
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(WmSttInGoodsEntity wmSttInGoods,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(WmSttInGoodsEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wmSttInGoods, request.getParameterMap());
		List<WmSttInGoodsEntity> wmSttInGoodss = this.wmSttInGoodsService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"????????????");
		modelMap.put(NormalExcelConstants.CLASS,WmSttInGoodsEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("??????????????????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
			"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST,wmSttInGoodss);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * ??????excel ?????????
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(WmSttInGoodsEntity wmSttInGoods,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"????????????");
    	modelMap.put(NormalExcelConstants.CLASS,WmSttInGoodsEntity.class);
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
				List<WmSttInGoodsEntity> listWmSttInGoodsEntitys = ExcelImportUtil.importExcel(file.getInputStream(),WmSttInGoodsEntity.class,params);
				for (WmSttInGoodsEntity wmSttInGoods : listWmSttInGoodsEntitys) {
					wmSttInGoodsService.save(wmSttInGoods);
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
	//PDA??????
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?>  list( @RequestParam(value="username", required=false) String username,
									@RequestParam(value="searchstr", required=false)String searchstr,
									@RequestParam(value="searchstr2", required=false)String searchstr2) {
//		return listWvGis;


		ResultDO D0 = new  ResultDO();
		String hql = " from WmSttInGoodsEntity where 1 = 1 and sttSta = '?????????' ";
		D0.setOK(true);
		if(!StringUtil.isEmpty(searchstr)) {
			hql=hql+"  and binId like '%" + searchstr + "%'";
		}
		if(!StringUtil.isEmpty(searchstr2)) {
            try{
                String shpbianma = wmUtil.getmdgoodsbytiaoma(searchstr2);
                if(StringUtil.isNotEmpty(shpbianma)){
                    searchstr2=shpbianma;
                }
            }catch (Exception e){

            }
			hql=hql+"  and goodsId like '%" + searchstr2 + "%'";
		}

		List<WmSttInGoodsEntity> listWmSttInGoodss=wmSttInGoodsService.findHql(hql);
		D0.setOK(true);
		List<WmSttInGoodsEntity> result = new ArrayList<WmSttInGoodsEntity>();
		int i = 0;
		for (WmSttInGoodsEntity t :listWmSttInGoodss){

			i++;
			if(i>100){
				break;
			}
			result.add(t);
		}
		if(result.size()<=0){
			D0.setOK(false);
		}
		D0.setObj(result);
		return new ResponseEntity(D0, HttpStatus.OK);

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		WmSttInGoodsEntity task = wmSttInGoodsService.get(WmSttInGoodsEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody String wmSttInGoodsstr, UriComponentsBuilder uriBuilder) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		ResultDO D0 = new  ResultDO();
		WmSttInGoodsEntity wmSttInGoods  = (WmSttInGoodsEntity)JSONHelper.json2Object(wmSttInGoodsstr,WmToDownGoodsEntity.class);
		//??????
		try{
			wmSttInGoodsService.save(wmSttInGoods);
			D0.setOK(true);
		} catch (Exception e) {
			e.printStackTrace();
			D0.setOK(false);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}


		return new ResponseEntity(D0, HttpStatus.OK);
	}

	@RequestMapping(value = "/change", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> update(@RequestParam String wmSttInGoodsstr,
									UriComponentsBuilder uriBuilder) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		ResultDO D0 = new  ResultDO();
		WmSttInGoodsEntity wmSttInGoods  = (WmSttInGoodsEntity)JSONHelper.json2Object(wmSttInGoodsstr,WmSttInGoodsEntity.class);
		//??????
		try{
			WmSttInGoodsEntity t = systemService.get(WmSttInGoodsEntity.class,wmSttInGoods.getId());

			MyBeanUtils.copyBeanNotNull2Bean(wmSttInGoods,t);
			t.setSttSta(Constants.wm_sta4);
			t.setUpdateDate(new Date());
			wmSttInGoodsService.saveOrUpdate(t);
			D0.setOK(true);
		} catch (Exception e) {
			e.printStackTrace();
			D0.setOK(false);
		}

		//???Restful???????????????204?????????, ?????????. ???????????????200?????????.

		return new ResponseEntity(D0, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		wmSttInGoodsService.deleteEntityById(WmSttInGoodsEntity.class, id);
	}
}
