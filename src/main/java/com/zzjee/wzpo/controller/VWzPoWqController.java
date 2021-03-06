package com.zzjee.wzpo.controller;

import com.alibaba.fastjson.JSONArray;
import com.zzjee.wmutil.erpUtil;
import com.zzjee.wzpo.entity.PowzPage;
import com.zzjee.wzpo.entity.VWzPoWqEntity;
import com.zzjee.wzpo.service.VWzPoWqServiceI;
import com.zzjee.wzyw.entity.TWzRkHeadEntity;
import com.zzjee.wzyw.entity.TWzRkItemEntity;
import com.zzjee.wzyw.service.TWzRkHeadServiceI;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import org.jeecgframework.core.common.controller.BaseController;
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
import java.io.IOException;
import java.util.*;

/**
 * @Title: Controller
 * @Description: v_wz_po_wq
 * @author onlineGenerator
 * @date 2018-05-21 15:57:04
 * @version V1.0
 *
 */
@Api(value="VWzPoWq",description="v_wz_po_wq",tags="vWzPoWqController")
@Controller
@RequestMapping("/vWzPoWqController")
public class VWzPoWqController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(VWzPoWqController.class);

	@Autowired
	private VWzPoWqServiceI vWzPoWqService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;

	@Autowired
	private TWzRkHeadServiceI tWzRkHeadService;

	/**
	 * v_wz_po_wq?????? ????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/wzpo/vWzPoWqList");
	}

	/**
	 * easyui AJAX????????????
	 *
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(VWzPoWqEntity vWzPoWq, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(VWzPoWqEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, vWzPoWq, request.getParameterMap());
		try{
		//???????????????????????????
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.vWzPoWqService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}



	/**
	 * ????????????/??????????????????
	 * @param page
	 * @return
	 */
	@RequestMapping(params = "saveRows")
	@ResponseBody
	public AjaxJson saveRows(PowzPage page){

		String message = null;
		List<VWzPoWqEntity> demos=page.getDemos();
		AjaxJson j = new AjaxJson();
		if(CollectionUtils.isNotEmpty(demos)){
			TWzRkHeadEntity tWzRkHead = new TWzRkHeadEntity();
			List<TWzRkItemEntity> tWzRkItemList = new ArrayList<TWzRkItemEntity>() ;

          Map<String,String> mapvendor = new HashMap<>();

            for(VWzPoWqEntity jeecgDemo:demos) {
                if (StringUtil.isNotEmpty(jeecgDemo.getId())) {
                    VWzPoWqEntity t = vWzPoWqService.get(VWzPoWqEntity.class, jeecgDemo.getId());
                    try {
                        mapvendor.put(t.getVendorCode(),t.getVendorName());
                    } catch (Exception e) {
                     }
                }
            }
            if(mapvendor.size()>1){
                j.setMsg("????????????????????????????????????????????????");
                j.setSuccess(false);
                return j;

            }
			for(VWzPoWqEntity jeecgDemo:demos){
				if (StringUtil.isNotEmpty(jeecgDemo.getId())) {
					VWzPoWqEntity t =vWzPoWqService.get(VWzPoWqEntity.class, jeecgDemo.getId());
					try {
						MyBeanUtils.copyBeanNotNull2Bean(demos, t);
						tWzRkHead.setDocDate(DateUtils.getDate());
						tWzRkHead.setRkRemark("?????????????????????"+t.getPoRemark());
						tWzRkHead.setVendorCode(t.getVendorCode());
						tWzRkHead.setVendorName(t.getVendorName());
					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						TWzRkItemEntity pogri = new TWzRkItemEntity();
						pogri.setBy1(jeecgDemo.getId().toString());
						pogri.setMatCode(jeecgDemo.getMatCode());
						pogri.setMatName(jeecgDemo.getMatName());
						pogri.setMatLocation(jeecgDemo.getMatLocation());
						pogri.setMatPrice(jeecgDemo.getMatPrice());
						pogri.setMatQty(jeecgDemo.getMatQtyWq().toString());
						pogri.setMatBatch(jeecgDemo.getMatBatch());
						pogri.setMatUnit(jeecgDemo.getMatUnit());
						String res = erpUtil.geterpys(pogri.getBy1(),pogri.getMatCode(),pogri.getMatQty());
						if(StringUtil.isNotEmpty(res)){
						    j.setMsg(res);
						    j.setSuccess(false);
                            return j;

                        }

						tWzRkItemList.add(pogri);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			try {
				tWzRkHeadService.addMain(tWzRkHead, tWzRkItemList);
			}catch (Exception e) {

			}
		}
		return j;
	}




	/**
	 * ??????v_wz_po_wq
	 *
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(VWzPoWqEntity vWzPoWq, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		vWzPoWq = systemService.getEntity(VWzPoWqEntity.class, vWzPoWq.getId());
		message = "v_wz_po_wq????????????";
		try{
			vWzPoWqService.delete(vWzPoWq);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "v_wz_po_wq????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * ????????????v_wz_po_wq
	 *
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "v_wz_po_wq????????????";
		try{
			for(String id:ids.split(",")){
				VWzPoWqEntity vWzPoWq = systemService.getEntity(VWzPoWqEntity.class,
				id
				);
				vWzPoWqService.delete(vWzPoWq);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "v_wz_po_wq????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * ??????v_wz_po_wq
	 *
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(VWzPoWqEntity vWzPoWq, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "v_wz_po_wq????????????";
		try{
			vWzPoWqService.save(vWzPoWq);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "v_wz_po_wq????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * ??????v_wz_po_wq
	 *
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(VWzPoWqEntity vWzPoWq, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "v_wz_po_wq????????????";
		VWzPoWqEntity t = vWzPoWqService.get(VWzPoWqEntity.class, vWzPoWq.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(vWzPoWq, t);
			vWzPoWqService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			message = "v_wz_po_wq????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * v_wz_po_wq??????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(VWzPoWqEntity vWzPoWq, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(vWzPoWq.getId())) {
			vWzPoWq = vWzPoWqService.getEntity(VWzPoWqEntity.class, vWzPoWq.getId());
			req.setAttribute("vWzPoWqPage", vWzPoWq);
		}
		return new ModelAndView("com/zzjee/wzpo/vWzPoWq-add");
	}
	/**
	 * v_wz_po_wq??????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(VWzPoWqEntity vWzPoWq, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(vWzPoWq.getId())) {
			vWzPoWq = vWzPoWqService.getEntity(VWzPoWqEntity.class, vWzPoWq.getId());
			req.setAttribute("vWzPoWqPage", vWzPoWq);
		}
		return new ModelAndView("com/zzjee/wzpo/vWzPoWq-update");
	}

	/**
	 * ??????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","vWzPoWqController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * ??????excel
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(VWzPoWqEntity vWzPoWq, HttpServletRequest request, HttpServletResponse response
			, DataGrid dataGrid, ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(VWzPoWqEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, vWzPoWq, request.getParameterMap());
		List<VWzPoWqEntity> vWzPoWqs = this.vWzPoWqService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"v_wz_po_wq");
		modelMap.put(NormalExcelConstants.CLASS, VWzPoWqEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("v_wz_po_wq??????", "?????????:"+ ResourceUtil.getSessionUser().getRealName(),
			"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST,vWzPoWqs);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * ??????excel ?????????
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(VWzPoWqEntity vWzPoWq, HttpServletRequest request, HttpServletResponse response
			, DataGrid dataGrid, ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"v_wz_po_wq");
    	modelMap.put(NormalExcelConstants.CLASS, VWzPoWqEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("v_wz_po_wq??????", "?????????:"+ ResourceUtil.getSessionUser().getRealName(),
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
				List<VWzPoWqEntity> listVWzPoWqEntitys = ExcelImportUtil.importExcel(file.getInputStream(), VWzPoWqEntity.class,params);
				for (VWzPoWqEntity vWzPoWq : listVWzPoWqEntitys) {
					vWzPoWqService.save(vWzPoWq);
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
	@ApiOperation(value="v_wz_po_wq????????????",produces="application/json",httpMethod="GET")
	public ResponseMessage<List<VWzPoWqEntity>> list() {
		List<VWzPoWqEntity> listVWzPoWqs=vWzPoWqService.getList(VWzPoWqEntity.class);
		return Result.success(listVWzPoWqs);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="??????ID??????v_wz_po_wq??????",notes="??????ID??????v_wz_po_wq??????",httpMethod="GET",produces="application/json")
	public ResponseMessage<?> get(@ApiParam(required=true,name="id",value="ID")@PathVariable("id") String id) {
		VWzPoWqEntity task = vWzPoWqService.get(VWzPoWqEntity.class, id);
		if (task == null) {
			return Result.error("??????ID??????v_wz_po_wq????????????");
		}
		return Result.success(task);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="??????v_wz_po_wq")
	public ResponseMessage<?> create(@ApiParam(name="v_wz_po_wq??????")@RequestBody VWzPoWqEntity vWzPoWq, UriComponentsBuilder uriBuilder) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<VWzPoWqEntity>> failures = validator.validate(vWzPoWq);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//??????
		try{
			vWzPoWqService.save(vWzPoWq);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("v_wz_po_wq??????????????????");
		}
		return Result.success(vWzPoWq);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="??????v_wz_po_wq",notes="??????v_wz_po_wq")
	public ResponseMessage<?> update(@ApiParam(name="v_wz_po_wq??????")@RequestBody VWzPoWqEntity vWzPoWq) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<VWzPoWqEntity>> failures = validator.validate(vWzPoWq);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//??????
		try{
			vWzPoWqService.saveOrUpdate(vWzPoWq);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("??????v_wz_po_wq????????????");
		}

		//???Restful???????????????204?????????, ?????????. ???????????????200?????????.
		return Result.success("??????v_wz_po_wq????????????");
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value="??????v_wz_po_wq")
	public ResponseMessage<?> delete(@ApiParam(name="id",value="ID",required=true)@PathVariable("id") String id) {
		logger.info("delete[{}]" + id);
		// ??????
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID????????????");
		}
		try {
			vWzPoWqService.deleteEntityById(VWzPoWqEntity.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("v_wz_po_wq????????????");
		}

		return Result.success();
	}
}
