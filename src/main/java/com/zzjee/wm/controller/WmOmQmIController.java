package com.zzjee.wm.controller;

import com.alibaba.fastjson.JSON;
import com.zzjee.wm.entity.*;
import com.zzjee.wm.page.Delrowpage;
import com.zzjee.wm.page.omqmpage;
import com.zzjee.wm.service.WmOmQmIServiceI;

import java.util.ArrayList;
import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.util.*;
import org.jeecgframework.web.system.pojo.base.TSRole;
import org.jeecgframework.web.system.sms.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.tag.core.easyui.TagUtil;

import org.jeecgframework.web.system.service.SystemService;
import com.zzjee.wmutil.wmUtil;


import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;

import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;


import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Map;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.jeecgframework.core.beanvalidator.BeanValidators;

import java.util.Random;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @Title: Controller
 * @Description: ????????????
 * @author erzhongxmu
 * @date 2017-09-11 14:57:43
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/wmOmQmIController")
public class WmOmQmIController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(WmOmQmIController.class);

	@Autowired
	private WmOmQmIServiceI wmOmQmIService;
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
		return new ModelAndView("com/zzjee/wm/wmOmQmIList");
	}
	@RequestMapping(params = "listd")
	public ModelAndView listd(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/wm/wmOmQmIdList");
	}
	@RequestMapping(params = "assignlist")
	public ModelAndView assignlist(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/wm/wmOmQmIassignList");
	}
	/**
	 * easyui AJAX????????????
	 *
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(WmOmQmIEntity wmOmQmI,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(WmOmQmIEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wmOmQmI, request.getParameterMap());
		try{
		//???????????????????????????
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("createDate", "desc");
		cq.setOrder(map1);
		if(wmOmQmI.getBinSta()==null){
			cq.eq("binSta", "N");
		}
		if(StringUtil.isNotEmpty(wmUtil.getCusCode())){
			cq.eq("cusCode", wmUtil.getCusCode());
		}
		cq.add();
		this.wmOmQmIService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	@RequestMapping(params = "datagridassign")
	public void datagridassign(WmOmQmIEntity wmOmQmI,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(WmOmQmIEntity.class, dataGrid);
		//?????????????????????
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wmOmQmI, request.getParameterMap());
		try{
		//???????????????????????????
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		Map<String,Object> map1 = new HashMap<String,Object>();
		map1.put("createDate", "desc");
		cq.setOrder(map1);
		cq.eq("binSta", "I");
		if(StringUtil.isNotEmpty(wmUtil.getCusCode())){
			cq.eq("cusCode", wmUtil.getCusCode());
		}
		cq.add();
		this.wmOmQmIService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}


	@RequestMapping(params = "dogetbin", method = { RequestMethod.GET,
			RequestMethod.POST })
	@ResponseBody
	public AjaxJson dogetbin(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		WmOmQmIEntity wmOmQmI = new WmOmQmIEntity();
		String goods = request.getParameter("goodsid");
		String tinid = request.getParameter("tinid");
		String tsql = "select  ws.ku_wei_bian_ma,  ws.goods_pro_data"
				+ "  from wv_stock ws, md_bin mb  where "
				+ "   ws.ku_wei_bian_ma = mb.ku_wei_bian_ma and mb.ting_yong <> 'Y' and (ws.kuctype = '??????' )"
				+ "   and ws.bin_id =  ? "
				+ "   and ws.goods_id =  ? "
				+ "   group by ws.ku_wei_bian_ma,ws.bin_id,ws.goods_id,mb.qu_huo_ci_xu, ws.goods_pro_data order by ws.goods_pro_data , ws.goods_qua ,mb.qu_huo_ci_xu,ws.create_date desc limit 1";

		List<Map<String, Object>> result = systemService.findForJdbc(tsql,tinid, goods);
		if (result.size() > 0) {
			wmOmQmI.setBinId(result.get(0).get("ku_wei_bian_ma").toString());
			wmOmQmI.setProData(result.get(0).get("goods_pro_data").toString());
		}
		j.setObj(wmOmQmI);
		return j;
	}


	@RequestMapping(params = "doassign")
	@ResponseBody
	public AjaxJson doassign(WmOmQmIEntity wmOmQmI, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "?????????????????????????????????";
		WmOmQmIEntity t = wmOmQmIService.get(WmOmQmIEntity.class, request
				.getParameter("id").toString());
		//?????????????????????
		List<String> usernameList = systemService.findListbySql("SELECT u.username  FROM t_s_role_user ru LEFT JOIN t_s_role r ON ru.roleid=r.id LEFT JOIN t_s_base_user u ON ru.userid=u.id LEFT JOIN (\n" +
				"SELECT assign_to,count(1) num FROM wm_om_qm_i WHERE bin_sta='N' GROUP BY assign_to) i ON u.username=i.assign_to WHERE r.rolecode='jhy' ORDER BY num");
		if (usernameList != null && usernameList.size() > 0) {
			//????????????????????????????????????
			System.out.println(JSON.toJSONString(usernameList));
			wmOmQmI.setAssignTo(usernameList.get(0));
		}

		if(!wmUtil.checkstcoka( t.getBinId(),t.getTinId(),t.getGoodsId(),t.getProData(),t.getBaseGoodscount())){
			message = "????????????";
			j.setMsg(message);
			return j;
		}else{
			try {
				t.setBinSta("N");
				systemService.updateEntitie(t);;
				systemService.addLog(message, Globals.Log_Type_UPDATE,
						Globals.Log_Leavel_INFO);
			} catch (Exception e) {
				e.printStackTrace();
				message = "?????????????????????????????????";
				throw new BusinessException(e.getMessage());
			}
		}

		j.setMsg(message);
		return j;
	}

	@RequestMapping(params = "doassignwave")
	@ResponseBody
	public AjaxJson dowavebatch(String ids, String waveid,HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "??????????????????";
		String waveId = "";
		String firstrongqi = request.getParameter("firstrongqi");
		waveId = DateUtils.date2Str(DateUtils.yyyymmddhhmmss);
		try {
			for (String id : ids.split(",")) {
				WmOmQmIEntity t = wmOmQmIService.get(WmOmQmIEntity.class, id);
				try {
					t.setWaveId("BC"+waveId);
				    t.setBinSta("N");//??????????????????????????????
					t.setFirstRq("");

					if(StringUtil.isNotEmpty(firstrongqi)){
						t.setFirstRq(firstrongqi);
					}


					String recarno = "";
					try{
						WmOmNoticeHEntity wmOmNoticeHEntity = systemService.findUniqueByProperty(WmOmNoticeHEntity.class,"omNoticeId",t.getOmNoticeId());
						recarno = wmOmNoticeHEntity.getReCarno();
					}catch (Exception e){

					}
					t.setSecondRq(recarno);
					systemService.updateEntitie(t);
					systemService.addLog(message, Globals.Log_Type_UPDATE,
							Globals.Log_Leavel_INFO);
				} catch (Exception e) {
					e.printStackTrace();
					message = "??????????????????";
					throw new BusinessException(e.getMessage());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "?????????????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;

	}

    @RequestMapping(params = "dodelwave")
    @ResponseBody
    public AjaxJson dodelwavebatch(String ids, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "??????????????????";
        String waveId = "";
        waveId = DateUtils.date2Str(DateUtils.yyyymmddhhmmss);
        try {
            for (String id : ids.split(",")) {
                WmOmQmIEntity t = wmOmQmIService.get(WmOmQmIEntity.class, id);
                try {
                    if("N".equals(t.getBinSta())){
                        t.setWaveId(null);
                        systemService.updateEntitie(t);
                        systemService.addLog(message, Globals.Log_Type_UPDATE,
                                Globals.Log_Leavel_INFO);
                    }
//                    t.setWaveId("BC"+waveId);
//                    t.setBinSta("N");//??????????????????????????????

                } catch (Exception e) {
                    e.printStackTrace();
                    message = "??????????????????";
                    throw new BusinessException(e.getMessage());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "??????????????????";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;

    }


	@RequestMapping(params = "doassignbatch")
	@ResponseBody
	public AjaxJson doassignbatch(String ids, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "?????????????????????????????????";
		try {
			for (String id : ids.split(",")) {
				WmOmQmIEntity t = wmOmQmIService.get(WmOmQmIEntity.class, id);
				try {
					if(!wmUtil.checkstcoka( t.getBinId(),t.getTinId(),t.getGoodsId(),t.getProData(),t.getBaseGoodscount())){
						message = "????????????";
						j.setMsg(message);
						return j;
					}else{
						t.setBinSta("N");
						systemService.updateEntitie(t);
						systemService.addLog(message, Globals.Log_Type_UPDATE,
								Globals.Log_Leavel_INFO);
					}

				} catch (Exception e) {
					e.printStackTrace();
					message = "?????????????????????????????????";
					throw new BusinessException(e.getMessage());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "?????????????????????????????????";
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
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(WmOmQmIEntity wmOmQmI, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		wmOmQmI = systemService.getEntity(WmOmQmIEntity.class, wmOmQmI.getId());
		message = "????????????????????????";
		try{
			wmOmQmIService.delete(wmOmQmI);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "????????????????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	@RequestMapping(params = "dotowavedown")
	@ResponseBody
	public synchronized AjaxJson dotowavedown(HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????";
		try {
			WmOmQmIEntity wmOmQmI = systemService.getEntity(
					WmOmQmIEntity.class, request.getParameter("id").toString());
			if (wmOmQmI != null&&wmOmQmI.getBinSta().equals("N")) {
				WmToDownGoodsEntity wmToDownGoods = new WmToDownGoodsEntity();
				wmToDownGoods.setBinIdFrom(wmOmQmI.getTinId());//????????????
				wmToDownGoods.setKuWeiBianMa(wmOmQmI.getBinId());//??????
				wmToDownGoods.setBinIdTo(wmOmQmI.getOmNoticeId());//?????????
				wmToDownGoods.setCusCode(wmOmQmI.getCusCode());//??????
				wmToDownGoods.setGoodsId(wmOmQmI.getGoodsId());//
				wmToDownGoods.setGoodsProData(wmOmQmI.getProData());//????????????
				wmToDownGoods.setOrderId(wmOmQmI.getOmNoticeId());//???????????????
				wmToDownGoods.setOrderIdI(wmOmQmI.getId());//??????????????????
				wmToDownGoods.setBaseUnit(wmOmQmI.getBaseUnit());//????????????
				wmToDownGoods.setBaseGoodscount(wmOmQmI.getBaseGoodscount());//??????????????????
				wmToDownGoods.setGoodsUnit(wmOmQmI.getGoodsUnit());//????????????
				wmToDownGoods.setGoodsQua(wmOmQmI.getQmOkQuat());//????????????
				wmToDownGoods.setGoodsQuaok(wmOmQmI.getQmOkQuat());//????????????
				wmToDownGoods.setGoodsName(wmOmQmI.getGoodsName());//????????????
				wmToDownGoods.setOmBeizhu(wmOmQmI.getOmBeizhu());//??????
				wmToDownGoods.setImCusCode(wmOmQmI.getImCusCode());//????????????
				wmToDownGoods.setOrderType("01");//?????????01
				systemService.save(wmToDownGoods);
				wmOmQmI.setBinSta("H");
				systemService.saveOrUpdate(wmOmQmI);
				try{
					String orderId = wmOmQmI.getOmNoticeId();
					String type = "jh";
					String username = ResourceUtil.getSessionUserName().getRealName();
					updateUser(orderId,type,username);
				}catch (Exception e){
				}
				systemService.addLog(message, Globals.Log_Type_DEL,
						Globals.Log_Leavel_INFO);
			} else {
				j.setSuccess(false);

				message = "?????????????????????";
			}

		} catch (Exception e) {
			j.setSuccess(false);
			e.printStackTrace();
			message = "????????????";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}




	@RequestMapping(params = "dotodown")
	@ResponseBody
	public synchronized AjaxJson dotodown(HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????";
	  	String id = request.getParameter("id").toString();
        boolean isok = todown(id);
        if (!isok){
        	j.setSuccess(isok);
        	message = "????????????";
		}
		j.setMsg(message);
		return j;
	}


	public boolean todown(String id){
		try {
			WmOmQmIEntity wmOmQmI = systemService.getEntity(
					WmOmQmIEntity.class, id);
			if (wmOmQmI != null&&wmOmQmI.getBinSta().equals("N")) {
				WmToDownGoodsEntity wmToDownGoods = new WmToDownGoodsEntity();
				wmToDownGoods.setBinIdFrom(wmOmQmI.getTinId());//????????????
				wmToDownGoods.setKuWeiBianMa(wmOmQmI.getBinId());//??????
				wmToDownGoods.setBinIdTo(wmOmQmI.getOmNoticeId());//?????????
				wmToDownGoods.setCusCode(wmOmQmI.getCusCode());//??????
				wmToDownGoods.setGoodsId(wmOmQmI.getGoodsId());//
				wmToDownGoods.setGoodsProData(wmOmQmI.getProData());//????????????
				wmToDownGoods.setOrderId(wmOmQmI.getOmNoticeId());//???????????????
				wmToDownGoods.setOrderIdI(wmOmQmI.getId());//??????????????????
				wmToDownGoods.setBaseUnit(wmOmQmI.getBaseUnit());//????????????
				wmToDownGoods.setBaseGoodscount(wmOmQmI.getBaseGoodscount());//??????????????????
				wmToDownGoods.setGoodsUnit(wmOmQmI.getGoodsUnit());//????????????
				wmToDownGoods.setGoodsQua(wmOmQmI.getQmOkQuat());//????????????
				wmToDownGoods.setGoodsQuaok(wmOmQmI.getQmOkQuat());//????????????
				wmToDownGoods.setGoodsName(wmOmQmI.getGoodsName());//????????????
				wmToDownGoods.setOmBeizhu(wmOmQmI.getOmBeizhu());//??????
				wmToDownGoods.setImCusCode(wmOmQmI.getImCusCode());//????????????
				wmToDownGoods.setOrderType("01");//?????????01
				systemService.save(wmToDownGoods);
				wmOmQmI.setBinSta("Y");
				systemService.saveOrUpdate(wmOmQmI);
				try{
					String orderId = wmOmQmI.getOmNoticeId();
					String type = "jh";
					String username = ResourceUtil.getSessionUserName().getRealName();
					updateUser(orderId,type,username);
				}catch (Exception e){
				}
				 return true;
			} else {
				 return false;
			}
		} catch (Exception e) {
		 return false;
		}
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
				WmOmQmIEntity wmOmQmI = systemService.getEntity(WmOmQmIEntity.class,
				id
				);
				wmOmQmIService.delete(wmOmQmI);
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
	public AjaxJson doAdd(WmOmQmIEntity wmOmQmI, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		try{
			wmOmQmIService.save(wmOmQmI);
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
	 *
	 * @param page
	 * @return
	 */
	@RequestMapping(params = "saveRows")
	@ResponseBody
	public AjaxJson saveRows(omqmpage page){
		String message = null;
		List<WmOmQmIEntity> demos=page.getOmqmrows();
		AjaxJson j = new AjaxJson();
		if(CollectionUtils.isNotEmpty(demos)){
			for(WmOmQmIEntity jeecgDemo:demos){
				if (StringUtil.isNotEmpty(jeecgDemo.getId())) {
					WmOmQmIEntity t =systemService.get(WmOmQmIEntity.class, jeecgDemo.getId());
					try {
						if(!wmUtil.checkstcok( jeecgDemo.getBinId(),jeecgDemo.getTinId(),jeecgDemo.getGoodsId(),jeecgDemo.getProData(),jeecgDemo.getBaseGoodscount())) {
						}else{
						message = "??????????????????";
						MyBeanUtils.copyBeanNotNull2Bean(jeecgDemo, t);
						systemService.saveOrUpdate(t);
						systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return j;
	}



	/**
	 * ??????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(WmOmQmIEntity wmOmQmI, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		WmOmQmIEntity t = wmOmQmIService.get(WmOmQmIEntity.class, wmOmQmI.getId());
		try {
			if(!wmUtil.checkstcok( wmOmQmI.getBinId(),wmOmQmI.getTinId(),wmOmQmI.getGoodsId(),wmOmQmI.getProData(),wmOmQmI.getBaseGoodscount())){
				message = "????????????";
				j.setMsg(message);
				return j;
			}else{
				double goods = Double.parseDouble(wmOmQmI.getBaseGoodscount());
					wmOmQmI.setQmOkQuat(wmOmQmI.getBaseGoodscount());
					wmOmQmI.setOmQuat(wmOmQmI.getBaseGoodscount());
					MyBeanUtils.copyBeanNotNull2Bean(wmOmQmI, t);
				wmOmQmIService.saveOrUpdate(t);
				systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
			}

		} catch (Exception e) {
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
	@RequestMapping(params = "dobatchassgnto")
	@ResponseBody
	public AjaxJson dobatchassgnto(String id,String assgnTo, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "????????????????????????";
		WmOmQmIEntity t = wmOmQmIService.get(WmOmQmIEntity.class, id);
		try {
				t.setAssignTo(assgnTo);
				wmOmQmIService.saveOrUpdate(t);
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
	public ModelAndView goAdd(WmOmQmIEntity wmOmQmI, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(wmOmQmI.getId())) {
			wmOmQmI = wmOmQmIService.getEntity(WmOmQmIEntity.class, wmOmQmI.getId());
			req.setAttribute("wmOmQmIPage", wmOmQmI);
		}
		return new ModelAndView("com/zzjee/wm/wmOmQmI-add");
	}
	/**
	 * ??????????????????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(WmOmQmIEntity wmOmQmI, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(wmOmQmI.getId())) {
			wmOmQmI = wmOmQmIService.getEntity(WmOmQmIEntity.class, wmOmQmI.getId());
			req.setAttribute("wmOmQmIPage", wmOmQmI);
		}
		return new ModelAndView("com/zzjee/wm/wmOmQmI-update");
	}

	/**
	 * ??????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","wmOmQmIController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

	/**
	 * ??????excel
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(WmOmQmIEntity wmOmQmI,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(WmOmQmIEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wmOmQmI, request.getParameterMap());
		List<WmOmQmIEntity> wmOmQmIs = this.wmOmQmIService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"????????????");
		modelMap.put(NormalExcelConstants.CLASS,WmOmQmIEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("??????????????????", "?????????:"+ResourceUtil.getSessionUserName().getRealName(),
			"????????????"));
		modelMap.put(NormalExcelConstants.DATA_LIST,wmOmQmIs);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * ??????excel ?????????
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(WmOmQmIEntity wmOmQmI,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"????????????");
    	modelMap.put(NormalExcelConstants.CLASS,WmOmQmIEntity.class);
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
				List<WmOmQmIEntity> listWmOmQmIEntitys = ExcelImportUtil.importExcel(file.getInputStream(),WmOmQmIEntity.class,params);
				for (WmOmQmIEntity wmOmQmI : listWmOmQmIEntitys) {
					wmOmQmIService.save(wmOmQmI);
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
	public List<WmOmQmIEntity> list() {
		List<WmOmQmIEntity> listWmOmQmIs=wmOmQmIService.getList(WmOmQmIEntity.class);
		return listWmOmQmIs;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		WmOmQmIEntity task = wmOmQmIService.get(WmOmQmIEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity(task, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> create(@RequestBody WmOmQmIEntity wmOmQmI, UriComponentsBuilder uriBuilder) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<WmOmQmIEntity>> failures = validator.validate(wmOmQmI);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//??????
		try{
			wmOmQmIService.save(wmOmQmI);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}
		//??????Restful???????????????????????????????????????url, ?????????????????????id?????????.
		String id = wmOmQmI.getId();
		URI uri = uriBuilder.path("/rest/wmOmQmIController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> update(@RequestBody WmOmQmIEntity wmOmQmI) {
		//??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
		Set<ConstraintViolation<WmOmQmIEntity>> failures = validator.validate(wmOmQmI);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//??????
		try{
			wmOmQmIService.saveOrUpdate(wmOmQmI);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		//???Restful???????????????204?????????, ?????????. ???????????????200?????????.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}


	void  updateUser(String orderId,String type,String userName){
		try{
			WmOmNoticeHEntity wmOmNoticeHEntity = systemService.findUniqueByProperty(WmOmNoticeHEntity.class,"omNoticeId",orderId);
			if ("jh".equals(type)){
				wmOmNoticeHEntity.setJhUser(userName);
			}
			if ("fh".equals(type)){
				wmOmNoticeHEntity.setFhUser(userName);
			}
			systemService.updateEntitie(wmOmNoticeHEntity);
		}catch (Exception e){

		}

	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") String id) {
		wmOmQmIService.deleteEntityById(WmOmQmIEntity.class, id);
	}
}
