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
import org.jeecgframework.core.util.oConvertUtils;
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

import com.zzjee.md.entity.MvGoodsEntity;
import com.zzjee.md.service.MvGoodsServiceI;

/**
 * @Title: Controller
 * @Description: ????????????
 * @author erzhongxmu
 * @date 2017-11-02 23:59:31
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/mvGoodsController")
public class MvGoodsController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MvGoodsController.class);

	@Autowired
	private MvGoodsServiceI mvGoodsService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	public String goodsCode ;
	public String coscode ;

	/**
	 * ?????????????????? ????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(MvGoodsEntity mvGoods,HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("com/zzjee/md/mvGoodsList");
		goodsCode =  oConvertUtils.getString(request.getParameter("ids")) ;
//		coscode =  oConvertUtils.getString(request.getParameter("coscode"));
//		mv.addObject("coscode", oConvertUtils.getString(request.getParameter("coscode")));
//		mv.addObject("goodsCode", mvGoods.getGoodsCode());
		return mv;

	}
	/**
	 * ??????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "setvalue")
	@ResponseBody
	public AjaxJson setvalue(String cusCode, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		System.out.print("ResourceUtil.getSessionUserName().setBrowser(cusCode)*******"+		ResourceUtil.getSessionUserName().getBrowser());
		ResourceUtil.getSessionUserName().setBrowser(cusCode);
		System.out.print("ResourceUtil.getSessionUserName().setBrowser(cusCode)########"+		ResourceUtil.getSessionUserName().getBrowser());

		coscode = cusCode;
		return j;
	}
	/**
	 * easyui AJAX????????????
	 *
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(MvGoodsEntity mvGoods,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(MvGoodsEntity.class, dataGrid);
		//?????????????????????

		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, mvGoods, request.getParameterMap());
		try{
		//???????????????????????????
			System.out.print("ResourceUtil.getSessionUserName().setBrowser(cusCode)data"+		ResourceUtil.getSessionUserName().getBrowser());

		//	String cusCode = ResourceUtil.getSessionUserName().getBrowser();
			if(!StringUtil.isEmpty(ResourceUtil.getSessionUserName().getBrowser())){
				cq.eq("cusCode", coscode);
			}
			cq.add();
			if(!StringUtil.isEmpty(goodsCode)){
				cq.like("goodsName", "%"+goodsCode+"%");
			}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}

//		TSUser user = ResourceUtil.getSessionUserName();
//		String roles = "";
		System.out.println("datagrid"+goodsCode);


//		if (user != null) {
//			List<TSRoleUser> rUsers = systemService.findByProperty(TSRoleUser.class, "TSUser.id", user.getId());
//			for (TSRoleUser ru : rUsers) {
//				TSRole role = ru.getTSRole();
//				roles += role.getRoleCode() + ",";
//			}
//			if (roles.length() > 0) {
//				roles = roles.substring(0, roles.length() - 1);
//			}
//			if(roles.equals("CUS")){
//				cq.eq("cusCode", user.getUserName());
//
//			}
//		}
		cq.add();
		this.mvGoodsService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * ??????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(MvGoodsEntity mvGoods, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		mvGoods = systemService.getEntity(MvGoodsEntity.class, mvGoods.getId());
		message = "????????????????????????";
		try{
			mvGoodsService.delete(mvGoods);
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
				MvGoodsEntity mvGoods = systemService.getEntity(MvGoodsEntity.class,
				id
				);
				mvGoodsService.delete(mvGoods);
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
	public AjaxJson doAdd(MvGoodsEntity mvGoods, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		try{
			mvGoodsService.save(mvGoods);
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
	public AjaxJson doUpdate(MvGoodsEntity mvGoods, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		MvGoodsEntity t = mvGoodsService.get(MvGoodsEntity.class, mvGoods.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(mvGoods, t);
			mvGoodsService.saveOrUpdate(t);
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
	public ModelAndView goAdd(MvGoodsEntity mvGoods, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(mvGoods.getId())) {
			mvGoods = mvGoodsService.getEntity(MvGoodsEntity.class, mvGoods.getId());
			req.setAttribute("mvGoodsPage", mvGoods);
		}
		return new ModelAndView("com/zzjee/md/mvGoods-add");
	}
	/**
	 * ??????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(MvGoodsEntity mvGoods, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(mvGoods.getId())) {
			mvGoods = mvGoodsService.getEntity(MvGoodsEntity.class, mvGoods.getId());
			req.setAttribute("mvGoodsPage", mvGoods);
		}
		return new ModelAndView("com/zzjee/md/mvGoods-update");
	}

	/**
	 * ??????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","mvGoodsController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * ??????excel
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(MvGoodsEntity mvGoods,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(MvGoodsEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, mvGoods, request.getParameterMap());
		List<MvGoodsEntity> mvGoodss = this.mvGoodsService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"????????????");
		modelMap.put(NormalExcelConstants.CLASS,MvGoodsEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("??????????????????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
			"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST,mvGoodss);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * ??????excel ?????????
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(MvGoodsEntity mvGoods,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"????????????");
    	modelMap.put(NormalExcelConstants.CLASS,MvGoodsEntity.class);
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
				List<MvGoodsEntity> listMvGoodsEntitys = ExcelImportUtil.importExcel(file.getInputStream(),MvGoodsEntity.class,params);
				for (MvGoodsEntity mvGoods : listMvGoodsEntitys) {
					mvGoodsService.save(mvGoods);
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
	public List<MvGoodsEntity> list() {
		List<MvGoodsEntity> listMvGoodss=mvGoodsService.getList(MvGoodsEntity.class);
		return listMvGoodss;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		MvGoodsEntity task = mvGoodsService.get(MvGoodsEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody MvGoodsEntity mvGoods, UriComponentsBuilder uriBuilder) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<MvGoodsEntity>> failures = validator.validate(mvGoods);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//??????
		try{
			mvGoodsService.save(mvGoods);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//??????Restful???????????????????????????????????????url, ?????????????????????id?????????.
		String id = mvGoods.getId();
		URI uri = uriBuilder.path("/rest/mvGoodsController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody MvGoodsEntity mvGoods) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<MvGoodsEntity>> failures = validator.validate(mvGoods);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//??????
		try{
			mvGoodsService.saveOrUpdate(mvGoods);
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
		mvGoodsService.deleteEntityById(MvGoodsEntity.class, id);
	}
}
