package com.zzjee.wmapi.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.zzjee.api.ResultDO;
import com.zzjee.wmapi.entity.WvNoticeEntity;
import com.zzjee.wmapi.service.WvNoticeServiceI;
import com.zzjee.wmutil.wmUtil;

/**
 * @author erzhongxmu
 * @version V1.0
 * @Title: Controller
 * @Description: wv_notice
 * @date 2018-05-30 20:21:05
 */
@Controller
@RequestMapping("/wvNoticeController")
public class WvNoticeController extends BaseController {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(WvNoticeController.class);

    @Autowired
    private WvNoticeServiceI wvNoticeService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private Validator validator;


    /**
     * wv_notice?????? ????????????
     *
     * @return
     */
    @RequestMapping(params = "list")
    public ModelAndView list(HttpServletRequest request) {
        return new ModelAndView("com/zzjee/wmapi/wvNoticeList");
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
    public void datagrid(WvNoticeEntity wvNotice, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(WvNoticeEntity.class, dataGrid);
        //?????????????????????
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wvNotice, request.getParameterMap());
        try {
            //???????????????????????????
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        cq.add();
        this.wvNoticeService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * ??????wv_notice
     *
     * @return
     */
    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(WvNoticeEntity wvNotice, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        wvNotice = systemService.getEntity(WvNoticeEntity.class, wvNotice.getId());
        message = "wv_notice????????????";
        try {
            wvNoticeService.delete(wvNotice);
            systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "wv_notice????????????";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * ????????????wv_notice
     *
     * @return
     */
    @RequestMapping(params = "doBatchDel")
    @ResponseBody
    public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "wv_notice????????????";
        try {
            for (String id : ids.split(",")) {
                WvNoticeEntity wvNotice = systemService.getEntity(WvNoticeEntity.class,
                        id
                );
                wvNoticeService.delete(wvNotice);
                systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "wv_notice????????????";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }


    /**
     * ??????wv_notice
     *
     * @param ids
     * @return
     */
    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(WvNoticeEntity wvNotice, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "wv_notice????????????";
        try {
            wvNoticeService.save(wvNotice);
            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "wv_notice????????????";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * ??????wv_notice
     *
     * @param ids
     * @return
     */
    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(WvNoticeEntity wvNotice, HttpServletRequest request) {
        String message = null;
        AjaxJson j = new AjaxJson();
        message = "wv_notice????????????";
        WvNoticeEntity t = wvNoticeService.get(WvNoticeEntity.class, wvNotice.getId());
        try {
            MyBeanUtils.copyBeanNotNull2Bean(wvNotice, t);
            wvNoticeService.saveOrUpdate(t);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "wv_notice????????????";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }


    /**
     * wv_notice??????????????????
     *
     * @return
     */
    @RequestMapping(params = "goAdd")
    public ModelAndView goAdd(WvNoticeEntity wvNotice, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(wvNotice.getId())) {
            wvNotice = wvNoticeService.getEntity(WvNoticeEntity.class, wvNotice.getId());
            req.setAttribute("wvNoticePage", wvNotice);
        }
        return new ModelAndView("com/zzjee/wmapi/wvNotice-add");
    }

    /**
     * wv_notice??????????????????
     *
     * @return
     */
    @RequestMapping(params = "goUpdate")
    public ModelAndView goUpdate(WvNoticeEntity wvNotice, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(wvNotice.getId())) {
            wvNotice = wvNoticeService.getEntity(WvNoticeEntity.class, wvNotice.getId());
            req.setAttribute("wvNoticePage", wvNotice);
        }
        return new ModelAndView("com/zzjee/wmapi/wvNotice-update");
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @RequestMapping(params = "upload")
    public ModelAndView upload(HttpServletRequest req) {
        req.setAttribute("controller_name", "wvNoticeController");
        return new ModelAndView("common/upload/pub_excel_upload");
    }

    /**
     * ??????excel
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXls")
    public String exportXls(WvNoticeEntity wvNotice, HttpServletRequest request, HttpServletResponse response
            , DataGrid dataGrid, ModelMap modelMap) {
        CriteriaQuery cq = new CriteriaQuery(WvNoticeEntity.class, dataGrid);
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, wvNotice, request.getParameterMap());
        List<WvNoticeEntity> wvNotices = this.wvNoticeService.getListByCriteriaQuery(cq, false);
        modelMap.put(NormalExcelConstants.FILE_NAME, "wv_notice");
        modelMap.put(NormalExcelConstants.CLASS, WvNoticeEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("wv_notice??????", "?????????:" + ResourceUtil.getSessionUserName().getRealName(),
                "????????????"));
        modelMap.put(NormalExcelConstants.DATA_LIST, wvNotices);
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
    }

    /**
     * ??????excel ?????????
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXlsByT")
    public String exportXlsByT(WvNoticeEntity wvNotice, HttpServletRequest request, HttpServletResponse response
            , DataGrid dataGrid, ModelMap modelMap) {
        modelMap.put(NormalExcelConstants.FILE_NAME, "wv_notice");
        modelMap.put(NormalExcelConstants.CLASS, WvNoticeEntity.class);
        modelMap.put(NormalExcelConstants.PARAMS, new ExportParams("wv_notice??????", "?????????:" + ResourceUtil.getSessionUserName().getRealName(),
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
                List<WvNoticeEntity> listWvNoticeEntitys = ExcelImportUtil.importExcel(file.getInputStream(), WvNoticeEntity.class, params);
                for (WvNoticeEntity wvNotice : listWvNoticeEntitys) {
                    wvNoticeService.save(wvNotice);
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

    //PDA??????
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list(@RequestParam(value = "username", required = false) String username,
                                  @RequestParam(value = "searchstr", required = false) String searchstr,
                                  @RequestParam(value = "searchstr2", required = false) String searchstr2) {
        ResultDO D0 = new ResultDO();
        String hql = " from WvNoticeEntity where 1 = 1 ";
        D0.setOK(true);
        if (!StringUtil.isEmpty(searchstr)) {
            hql = hql + "  and noticeId like '%" + searchstr + "%'" + "  or imCusCode like '%" + searchstr + "%'";
        }
        if (!StringUtil.isEmpty(searchstr2)) {
            try {
                String shpbianma = wmUtil.getmdgoodsbytiaoma(searchstr2);
                if (StringUtil.isNotEmpty(shpbianma)) {
                    searchstr2 = shpbianma;
                }
            } catch (Exception e) {

            }
            String[] ss = searchstr2.split(",");
            if (ss.length == 1) {
                hql = hql + "  and (goodsCode like '%" + searchstr2 + "%'" + "or shp_ming_cheng like '%" + searchstr2 + "%')";

            } else {
                String insearch = "";
                for (String s : ss) {
                    if (StringUtil.isNotEmpty(insearch)) {
                        insearch = insearch + " or  goodsCode = '" + s + "'";
                    } else {
                        insearch = "goodsCode = '" + s + "'";
                    }
                }
                hql = hql + "  and  (" + insearch + ")";

            }
        }
        List<WvNoticeEntity> listWvNotices = wvNoticeService.findHql(hql);
        D0.setOK(true);
        List<WvNoticeEntity> result = new ArrayList<WvNoticeEntity>();
        int i = 0;
        for (WvNoticeEntity t : listWvNotices) {
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
        WvNoticeEntity task = wvNoticeService.get(WvNoticeEntity.class, id);
        if (task == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(task, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> create(@RequestBody WvNoticeEntity wvNotice, UriComponentsBuilder uriBuilder) {
        //??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
        Set<ConstraintViolation<WvNoticeEntity>> failures = validator.validate(wvNotice);
        if (!failures.isEmpty()) {
            return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
        }
        //??????
        try {
            wvNoticeService.save(wvNotice);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        //??????Restful???????????????????????????????????????url, ?????????????????????id?????????.
        String id = wvNotice.getId();
        URI uri = uriBuilder.path("/rest/wvNoticeController/" + id).build().toUri();
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri);
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@RequestBody WvNoticeEntity wvNotice) {
        //??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
        Set<ConstraintViolation<WvNoticeEntity>> failures = validator.validate(wvNotice);
        if (!failures.isEmpty()) {
            return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
        }
        //??????
        try {
            wvNoticeService.saveOrUpdate(wvNotice);
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
        wvNoticeService.deleteEntityById(WvNoticeEntity.class, id);
    }
}
