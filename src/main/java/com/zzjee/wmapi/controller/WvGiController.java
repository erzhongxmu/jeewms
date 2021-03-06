package com.zzjee.wmapi.controller;

import com.zzjee.api.ResultDO;
import com.zzjee.wmapi.entity.WvGiEntity;
import com.zzjee.wmapi.entity.WvGiNoticeEntity;
import com.zzjee.wmapi.service.WvGiServiceI;

import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zzjee.wmutil.wmUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.common.TreeChildCount;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.core.util.MyBeanUtils;

import java.io.OutputStream;

import org.jeecgframework.core.util.BrowserUtils;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.TemplateExportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.vo.TemplateExcelConstants;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jeecgframework.core.util.ResourceUtil;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Map;
import java.util.HashMap;

import org.jeecgframework.core.util.ExceptionUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.jeecgframework.core.beanvalidator.BeanValidators;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author erzhongxmu
 * @version V1.0
 * @Title: Controller
 * @Description: wv_gi
 * @date 2018-05-30 20:21:50
 */
@Controller
@RequestMapping("/wvGiController")
public class WvGiController extends BaseController {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(WvGiController.class);

    @Autowired
    private WvGiServiceI wvGiService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private Validator validator;


    /**
     * wv_gi?????? ????????????
     *
     * @return
     */
    @RequestMapping(params = "list")
    public ModelAndView list(HttpServletRequest request) {
        return new ModelAndView("com/zzjee/wmapi/wvGiList");
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
    public void datagrid(WvGiEntity wvGi, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(WvGiEntity.class, dataGrid);
        //?????????????????????
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wvGi, request.getParameterMap());
        try {
            //???????????????????????????
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        cq.add();
        this.wvGiService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * ??????wv_gi
     *
     * @return
     */
    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(WvGiEntity wvGi, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        wvGi = systemService.getEntity(WvGiEntity.class, wvGi.getId());
        message = "wv_gi????????????";
        try {
            wvGiService.delete(wvGi);
            systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "wv_gi????????????";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * ????????????wv_gi
     *
     * @return
     */
    @RequestMapping(params = "doBatchDel")
    @ResponseBody
    public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "wv_gi????????????";
        try {
            for (String id : ids.split(",")) {
                WvGiEntity wvGi = systemService.getEntity(WvGiEntity.class,
                        id
                );
                wvGiService.delete(wvGi);
                systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "wv_gi????????????";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }


    /**
     * ??????wv_gi
     *
     * @param ids
     * @return
     */
    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(WvGiEntity wvGi, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "wv_gi????????????";
        try {
            wvGiService.save(wvGi);
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "wv_gi????????????";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * ??????wv_gi
     *
     * @param ids
     * @return
     */
    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(WvGiEntity wvGi, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "wv_gi????????????";
        WvGiEntity t = wvGiService.get(WvGiEntity.class, wvGi.getId());
        try {
            MyBeanUtils.copyBeanNotNull2Bean(wvGi, t);
            wvGiService.saveOrUpdate(t);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "wv_gi????????????";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }


    /**
     * wv_gi??????????????????
     *
     * @return
     */
    @RequestMapping(params = "goAdd")
    public ModelAndView goAdd(WvGiEntity wvGi, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(wvGi.getId())) {
            wvGi = wvGiService.getEntity(WvGiEntity.class, wvGi.getId());
            req.setAttribute("wvGiPage", wvGi);
        }
        return new ModelAndView("com/zzjee/wmapi/wvGi-add");
    }

    /**
     * wv_gi??????????????????
     *
     * @return
     */
    @RequestMapping(params = "goUpdate")
    public ModelAndView goUpdate(WvGiEntity wvGi, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(wvGi.getId())) {
            wvGi = wvGiService.getEntity(WvGiEntity.class, wvGi.getId());
            req.setAttribute("wvGiPage", wvGi);
        }
        return new ModelAndView("com/zzjee/wmapi/wvGi-update");
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @RequestMapping(params = "upload")
    public ModelAndView upload(HttpServletRequest req) {
        req.setAttribute("controller_name", "wvGiController");
        return new ModelAndView("common/upload/pub_excel_upload");
    }

    /**
     * ??????excel
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXls")
    public String exportXls(WvGiEntity wvGi, HttpServletRequest request, HttpServletResponse response
            , DataGrid dataGrid, ModelMap modelMap) {
        CriteriaQuery cq = new CriteriaQuery(WvGiEntity.class, dataGrid);
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wvGi, request.getParameterMap());
        List<WvGiEntity> wvGis = this.wvGiService.getListByCriteriaQuery(cq, false);
        modelMap.put(NormalExcelConstants.FILE_NAME, "wv_gi");
        modelMap.put(NormalExcelConstants.CLASS, WvGiEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("wv_gi??????", "?????????:" + ResourceUtil.getSessionUserName().getRealName(),
                "????????????"));
        modelMap.put(NormalExcelConstants.DATA_LIST, wvGis);
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
    }

    /**
     * ??????excel ?????????
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXlsByT")
    public String exportXlsByT(WvGiEntity wvGi, HttpServletRequest request, HttpServletResponse response
            , DataGrid dataGrid, ModelMap modelMap) {
        modelMap.put(NormalExcelConstants.FILE_NAME, "wv_gi");
        modelMap.put(NormalExcelConstants.CLASS, WvGiEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("wv_gi??????", "?????????:" + ResourceUtil.getSessionUserName().getRealName(),
                "????????????"));
        modelMap.put(NormalExcelConstants.DATA_LIST, new ArrayList());
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
                List<WvGiEntity> listWvGiEntitys = ExcelImportUtil.importExcel(file.getInputStream(), WvGiEntity.class, params);
                for (WvGiEntity wvGi : listWvGiEntitys) {
                    wvGiService.save(wvGi);
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
    public ResponseEntity<?> list(@RequestParam(value = "username", required = false) String username,
                                  @RequestParam(value = "searchstr", required = false) String searchstr,
                                  @RequestParam(value = "searchstr2", required = false) String searchstr2) {
//		return listWvGis;


        ResultDO D0 = new ResultDO();
        String hql = " from WvGiEntity where downSta is null ";
        D0.setOK(true);
        if (!StringUtil.isEmpty(searchstr)) {
            hql = hql + "  and orderId = '" + searchstr + "'";
        }
        if (!StringUtil.isEmpty(searchstr2)) {
            try {
                String shpbianma = wmUtil.getmdgoodsbytiaoma(searchstr2);
                if (StringUtil.isNotEmpty(shpbianma)) {
                    searchstr2 = shpbianma;
                }
            } catch (Exception e) {

            }

//			hql=hql+"  and goodsId = '" + searchstr2 + "'";
            String[] ss = searchstr2.split(",");
            if (ss.length == 1) {
                hql = hql + "  and goodsId = '" + searchstr2 + "'";

            } else {
                String insearch = "";
                for (String s : ss) {
                    if (StringUtil.isNotEmpty(insearch)) {
                        insearch = insearch + " or  goodsId = '" + s + "'";
                    } else {
                        insearch = "goodsId = '" + s + "'";
                    }

                }
                hql = hql + "  and  (" + insearch + ")";


            }
        }

        List<WvGiEntity> listWvGis = wvGiService.findHql(hql);

//		List<WvGiEntity> listWvGis=wvGiService.getList(WvGiEntity.class);
        D0.setOK(true);
        List<WvGiEntity> result = new ArrayList<WvGiEntity>();
        int i = 0;
        for (WvGiEntity t : listWvGis) {

            i++;
            if (i > 100) {
                break;
            }
            result.add(t);
        }

        D0.setObj(result);
        return new ResponseEntity(D0, HttpStatus.OK);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> get(@PathVariable("id") String id) {
        WvGiEntity task = wvGiService.get(WvGiEntity.class, id);
        if (task == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(task, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody WvGiEntity wvGi, UriComponentsBuilder uriBuilder) {
        //??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
        Set<ConstraintViolation<WvGiEntity>> failures = validator.validate(wvGi);
        if (!failures.isEmpty()) {
            return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
        }

        //??????
        try {
            wvGiService.save(wvGi);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        //??????Restful???????????????????????????????????????url, ?????????????????????id?????????.
        String id = wvGi.getId();
        URI uri = uriBuilder.path("/rest/wvGiController/" + id).build().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri);

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody WvGiEntity wvGi) {
        //??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
        Set<ConstraintViolation<WvGiEntity>> failures = validator.validate(wvGi);
        if (!failures.isEmpty()) {
            return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
        }

        //??????
        try {
            wvGiService.saveOrUpdate(wvGi);
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
        wvGiService.deleteEntityById(WvGiEntity.class, id);
    }
}
