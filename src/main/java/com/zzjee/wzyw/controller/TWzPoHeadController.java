package com.zzjee.wzyw.controller;

import com.alibaba.fastjson.JSONArray;
import com.zzjee.md.entity.MdSupEntity;
import com.zzjee.md.entity.MvGoodsEntity;
import com.zzjee.wm.entity.WmImNoticeHEntity;
import com.zzjee.wm.entity.WmImNoticeIEntity;
import com.zzjee.wm.service.WmImNoticeHServiceI;
import com.zzjee.wmutil.wmUtil;
import com.zzjee.wz.entity.TWzMaterialEntity;
import com.zzjee.wzyw.entity.TWzPoHeadEntity;
import com.zzjee.wzyw.entity.TWzPoItemEntity;
import com.zzjee.wzyw.page.TWzPoHeadPage;
import com.zzjee.wzyw.service.TWzPoHeadServiceI;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author onlineGenerator
 * @version V1.0
 * @Title: Controller
 * @Description: ??????????????????
 * @date 2018-05-21 13:23:13
 */
@Api(value = "TWzPoHead", description = "??????????????????", tags = "tWzPoHeadController")
@Controller
@RequestMapping("/tWzPoHeadController")
public class TWzPoHeadController extends BaseController {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(TWzPoHeadController.class);

    @Autowired
    private TWzPoHeadServiceI tWzPoHeadService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private Validator validator;
    @Autowired
    private WmImNoticeHServiceI wmImNoticeHService;

    /**
     * ???????????????????????? ????????????
     *
     * @return
     */
    @RequestMapping(params = "list")
    public ModelAndView list(HttpServletRequest request) {
        return new ModelAndView("com/zzjee/wzyw/tWzPoHeadList");
    }

    @RequestMapping(params = "listsp")
    public ModelAndView listsp(HttpServletRequest request) {
        return new ModelAndView("com/zzjee/wzyw/tWzPoHeadspList");
    }

    /**
     * easyui AJAX????????????
     *
     * @param request
     * @param response
     * @param dataGrid
     */

    @RequestMapping(params = "datagrid")
    public void datagrid(TWzPoHeadEntity tWzPoHead, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(TWzPoHeadEntity.class, dataGrid);
        //?????????????????????
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tWzPoHead);
        try {
            //???????????????????????????
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        cq.add();
        this.tWzPoHeadService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    @RequestMapping(params = "datagridsp")
    public void datagridsp(TWzPoHeadEntity tWzPoHead, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
        CriteriaQuery cq = new CriteriaQuery(TWzPoHeadEntity.class, dataGrid);
        //?????????????????????
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tWzPoHead);
        try {
            //???????????????????????????
            cq.eq("bpmStatus", "1");
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        cq.add();
        this.tWzPoHeadService.getDataGridReturn(cq, true);
        TagUtil.datagrid(response, dataGrid);
    }

    /**
     * ????????????????????????
     *
     * @return
     */
    @RequestMapping(params = "doDel")
    @ResponseBody
    public AjaxJson doDel(TWzPoHeadEntity tWzPoHead, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        tWzPoHead = systemService.getEntity(TWzPoHeadEntity.class, tWzPoHead.getId());
        String message = "??????????????????????????????";
        try {
            tWzPoHead.setBpmStatus("9");
            systemService.saveOrUpdate(tWzPoHead);
//            tWzPoHeadService.delMain(tWzPoHead);
            systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "??????????????????????????????";
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
    @RequestMapping(params = "doBatchDel")
    @ResponseBody
    public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String message = "??????????????????????????????";
        try {
            for (String id : ids.split(",")) {
                TWzPoHeadEntity tWzPoHead = systemService.getEntity(TWzPoHeadEntity.class,
                        Integer.parseInt(id)
                );
                tWzPoHeadService.delMain(tWzPoHead);
                systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
            }
        } catch (Exception e) {
            e.printStackTrace();
            message = "??????????????????????????????";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }


    @RequestMapping(params = "doPrint")
    @ResponseBody
    public void downReceiveExcel(String id,
                                 HttpServletRequest request, HttpServletResponse response) {
        OutputStream fileOut = null;
        BufferedImage bufferImg = null;
        String codedFileName = null;
        TWzPoHeadEntity tWzPoHead = systemService.getEntity(TWzPoHeadEntity.class,
                Integer.parseInt(id));// ????????????
        String hql0 = "from TWzPoItemEntity where 1 = 1 AND pO_ID = ? ";
        List<TWzPoItemEntity> tWzPoItemEntityList = systemService.findHql(hql0, id);
        try {
            StringBuffer sber = new StringBuffer();

            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

            // ??????????????????????????????????????????
            codedFileName = java.net.URLEncoder.encode("??????", "UTF-8");
            response.setHeader("content-disposition", "attachment;filename="
                    + id + ".xls");

            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("?????????");
            sheet.setMargin(HSSFSheet.TopMargin, 0.2);// ??????????????????
            sheet.setMargin(HSSFSheet.BottomMargin, 0.1);// ??????????????????
            sheet.setMargin(HSSFSheet.LeftMargin, 0.8);// ??????????????????
            sheet.setMargin(HSSFSheet.RightMargin, 0.0);// ???????????????
            sheet.setColumnWidth(0, 10 * 256);
            sheet.setColumnWidth(1, 35 * 256);
            sheet.setColumnWidth(2, 35 * 200);
            sheet.setColumnWidth(3, 35 * 256);
            sheet.setColumnWidth(4, 10 * 256);
            sheet.setColumnWidth(5, 10 * 256);
            // sheet.setColumnWidth(6, 8 * 256);
            // sheet.setColumnWidth(7, 8 * 256);
            // sheet.setColumnWidth(8, 8 * 256);
            // ???????????????
            Row row = sheet.createRow((short) 0); // ???????????????

            // ???????????????????????????
            CellStyle cs = wb.createCellStyle();
            CellStyle cs2 = wb.createCellStyle();
            CellStyle cs3 = wb.createCellStyle();
            CellStyle cs4 = wb.createCellStyle();
            CellStyle cs5 = wb.createCellStyle();
            // ??????????????????
            Font f = wb.createFont();
            Font f2 = wb.createFont();

            // ?????????????????????????????????????????????
            f.setFontHeightInPoints((short) 22);
            f.setColor(IndexedColors.BLACK.getIndex());
            f.setBoldweight(Font.BOLDWEIGHT_BOLD);

            // ??????????????????????????????????????????
            f2.setFontHeightInPoints((short) 16);
            f2.setColor(IndexedColors.BLACK.getIndex());

            // Font f3=wb.createFont();
            // f3.setFontHeightInPoints((short) 10);
            // f3.setColor(IndexedColors.RED.getIndex());

            // ???????????????????????????????????????????????????
            cs.setFont(f);
            cs.setBorderLeft(CellStyle.BORDER_NONE);
            cs.setBorderRight(CellStyle.BORDER_NONE);
            cs.setBorderTop(CellStyle.BORDER_NONE);
            cs.setBorderBottom(CellStyle.BORDER_NONE);
            cs.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            // ????????????????????????????????????????????????
            cs2.setFont(f2);
            cs2.setBorderLeft(CellStyle.BORDER_NONE);
            cs2.setBorderRight(CellStyle.BORDER_NONE);
            cs2.setBorderTop(CellStyle.BORDER_NONE);
            cs2.setBorderBottom(CellStyle.BORDER_NONE);
            cs2.setWrapText(true);

            // cs2.setAlignment(CellStyle.BORDER_NONE);

            cs3.setFont(f2);
            cs3.setBorderLeft(CellStyle.BORDER_MEDIUM);
            cs3.setBorderRight(CellStyle.BORDER_MEDIUM);
            cs3.setBorderTop(CellStyle.BORDER_MEDIUM);
            cs3.setBorderBottom(CellStyle.BORDER_MEDIUM);
            // cs3.setAlignment(CellStyle.BORDER_HAIR);
            cs4.setFont(f2);
            cs4.setBorderTop(CellStyle.BORDER_MEDIUM);
            cs4.setBorderBottom(CellStyle.BORDER_MEDIUM);

            cs5.setFont(f2);
            cs5.setBorderLeft(CellStyle.BORDER_NONE);
            cs5.setBorderRight(CellStyle.BORDER_NONE);
            cs5.setBorderTop(CellStyle.BORDER_NONE);
            cs5.setBorderBottom(CellStyle.BORDER_NONE);
//			cs5.setWrapText(true);

            Row row1 = sheet.createRow((short) 1); // ???????????????
            row1.setHeight((short) 700);
            Cell cellTitle = row1.createCell(0);
            cellTitle.setCellValue(ResourceUtil.getConfigByName("comname") + "?????????");
            cellTitle.setCellStyle(cs);
            Row rowHead1 = sheet.createRow((short) 2); // ???????????????
            rowHead1.setHeight((short) 800);
            Cell cellHead11 = rowHead1.createCell(0);
            cellHead11.setCellValue("???????????????" + id );
            cellHead11.setCellStyle(cs2);
            Cell cellHead12 = rowHead1.createCell(2);

            cellHead12.setCellValue("???????????????" + DateUtils.date2Str(tWzPoHead.getCreateDate(), DateUtils.date_sdf));
            cellHead12.setCellStyle(cs2);

            Row rowHead2 = sheet.createRow((short) 3); // ???????????????
            rowHead2.setHeight((short) 500);
            Cell cellHead21 = rowHead2.createCell(0);
            cellHead21.setCellValue("??????????????????" + tWzPoHead.getVendorCode());
            cellHead21.setCellStyle(cs2);

            Cell cellHead23 = rowHead2.createCell(2);
            cellHead23.setCellValue("??????????????????" + tWzPoHead.getVendorName());
            cellHead23.setCellStyle(cs2);

            Row rowHead3 = sheet.createRow((short) 4); // ???????????????
            rowHead3.setHeight((short) 500);
            Cell cellHead31 = rowHead3.createCell(0);
            cellHead31.setCellValue("?????????" + tWzPoHead.getPoRemark()
            );
            cellHead31.setCellStyle(cs2);

            Cell cellHead35 = rowHead3.createCell(2);
            cellHead35.setCellValue("????????????" + tWzPoHead.getCreateName());
            cellHead35.setCellStyle(cs2);
            Row rowHead4 = sheet.createRow((short) 5); // ???????????????
            Cell cellHead41 = rowHead4.createCell(0);
            cellHead41.setCellValue("?????????" + tWzPoHead.getPoBy1());
            cellHead41.setCellStyle(cs2);
            // ???????????????
            CellRangeAddress c = new CellRangeAddress(0, 0, 0, 5); // ???????????????
            CellRangeAddress c0 = new CellRangeAddress(1, 1, 0, 5);// ???????????????

            CellRangeAddress c1 = new CellRangeAddress(2, 2, 0, 1);// ?????????????????????
            CellRangeAddress c11 = new CellRangeAddress(2, 2, 2, 5);// ?????????????????????
            CellRangeAddress c2 = new CellRangeAddress(3, 3, 0, 1);// ???????????????
            CellRangeAddress c3 = new CellRangeAddress(3, 3, 2, 5);// ???????????????????????????
            CellRangeAddress c4 = new CellRangeAddress(4, 4, 0, 1);// ???????????????
            CellRangeAddress c5 = new CellRangeAddress(4, 4, 2, 5);// ???????????????????????????
            CellRangeAddress c6 = new CellRangeAddress(5, 5, 0, 5); // ???????????????

            // CellRangeAddress c4 = new CellRangeAddress(4, 4, 0, 1);
            // CellRangeAddress c5 = new CellRangeAddress(4, 4, 2, 3);
            // CellRangeAddress c6 = new CellRangeAddress(4, 4, 4, 5);
            // CellRangeAddress c7 = new CellRangeAddress(4, 4, 6, 6);

            sheet.addMergedRegion(c);
            sheet.addMergedRegion(c0);
            sheet.addMergedRegion(c1);
            sheet.addMergedRegion(c2);
            sheet.addMergedRegion(c3);
            sheet.addMergedRegion(c4);
            sheet.addMergedRegion(c5);
            sheet.addMergedRegion(c11);
             sheet.addMergedRegion(c6);

            Row rowColumnName = sheet.createRow((short) 7); // ??????
            rowColumnName.setHeight((short) 500);
            String[] columnNames = {"??????", "????????????", "????????????", "??????", "??????",
                    "??????"};

            for (int i = 0; i < columnNames.length; i++) {
                Cell cell = rowColumnName.createCell(i);
                cell.setCellValue(columnNames[i]);
                cell.setCellStyle(cs3);
            }
            int cellsNum = 7;
            int cerconNo = 1;
            for (int i = 0; i < tWzPoItemEntityList.size(); i++) {
                TWzPoItemEntity entity = tWzPoItemEntityList.get(i);
                cellsNum++;
                Row rowColumnValue = sheet.createRow((short) cellsNum); // ??????
                rowColumnValue.setHeight((short) 500);
                Cell cell1 = rowColumnValue.createCell(0);
                cell1.setCellValue(cerconNo);
                cell1.setCellStyle(cs3);
                Cell cell2 = rowColumnValue.createCell(1);


                try {
                    cell2.setCellStyle(cs3);
                    cell2.setCellValue(entity.getMatCode());
                } catch (Exception e) {

                }
                try {
                    Cell cell3 = rowColumnValue.createCell(2);
                    cell3.setCellValue(entity.getMatName());
                    cell3.setCellStyle(cs3);
                } catch (Exception e) {

                }
                try {
                    Cell cell4 = rowColumnValue.createCell(3);
                    cell4.setCellValue(StringUtil.moneyToString(entity.getMatQty(),"#.0000"));
                    cell4.setCellStyle(cs3);
                } catch (Exception e) {

                }
                try {
                    Cell cell5 = rowColumnValue.createCell(4);
                    cell5.setCellValue(entity.getMatUnit());
                    cell5.setCellStyle(cs3);
                } catch (Exception e) {

                }
                ;
                try {


                    Cell cell6 = rowColumnValue.createCell(5);
                    cell6.setCellValue(entity.getItemRemark());
                    cell6.setCellStyle(cs3);
                } catch (Exception e) {

                }
                cerconNo++;
            }
            Row rowColumnInfo = sheet.createRow((short) 2 + cellsNum); // ??????
            Cell cellrow = rowColumnInfo.createCell(0);
            cellrow.setCellValue("????????????              ????????????            ?????????");
            cellrow.setCellStyle(cs5);
//			rowColumnInfo.createCell(0).setCellValue(
//					"????????????                       ?????????                     ?????????                      ");
//			CellRangeAddress c15 = new CellRangeAddress(10 + cellsNum,
//					10 + cellsNum, 0, 5);
//			sheet.addMergedRegion(c15);
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


    /**
     * ????????????????????????
     *
     * @return
     */
    @RequestMapping(params = "doAdd")
    @ResponseBody
    public AjaxJson doAdd(TWzPoHeadEntity tWzPoHead, TWzPoHeadPage tWzPoHeadPage, HttpServletRequest request) {
        List<TWzPoItemEntity> tWzPoItemList = tWzPoHeadPage.getTWzPoItemList();
        AjaxJson j = new AjaxJson();
        String message = "????????????";
        try {
            tWzPoHead.setBpmStatus("1");
            tWzPoHeadService.addMain(tWzPoHead, tWzPoItemList);

            String poid = tWzPoHead.getId().toString();
            String cusCode = "";
            List<WmImNoticeHEntity> wmimh = systemService.findByProperty(WmImNoticeHEntity.class, "imCusCode", poid);
            if (wmimh != null && wmimh.size() > 0) {
                message = "??????????????????????????????????????????????????????";
                j.setMsg(message);
                return j;
            }
            List<WmImNoticeIEntity> wmImNoticeIListnew = new ArrayList<WmImNoticeIEntity>();
            for (TWzPoItemEntity page : tWzPoItemList) {
                WmImNoticeIEntity wmi = new WmImNoticeIEntity();
                wmi.setGoodsCode(page.getMatCode());
                MvGoodsEntity mvgoods = systemService.findUniqueByProperty(
                        MvGoodsEntity.class, "goodsCode", wmi.getGoodsCode());
                if (mvgoods != null) {
                    cusCode = mvgoods.getCusCode();
                    wmi.setGoodsName(mvgoods.getGoodsName());
                    wmi.setGoodsUnit(mvgoods.getShlDanWei());
                }
                try {
                    wmi.setGoodsCount(page.getMatQty());
                    String[] args = page.getMatQty().split("\\.");
                    wmi.setGoodsCount(args[0]);
                } catch (Exception e) {

                }

                wmi.setOtherId(page.getId());
                wmImNoticeIListnew.add(wmi);

            }

            WmImNoticeHEntity wmImNoticeH = new WmImNoticeHEntity();
            wmImNoticeH.setOrderTypeCode("01");
            String noticeid = wmUtil.getNextNoticeid(wmImNoticeH.getOrderTypeCode());
            wmImNoticeH.setCusCode(cusCode);
            wmImNoticeH.setNoticeId(noticeid);
            wmImNoticeH.setImData(tWzPoHead.getDocDate());
//				wmImNoticeH.setPlatformCode(pageheader.getCusCode());
            wmImNoticeH.setImBeizhu(tWzPoHead.getPoRemark());
            wmImNoticeH.setSupCode(tWzPoHead.getVendorCode());
            MdSupEntity mdsup = systemService.findUniqueByProperty(MdSupEntity.class, "gysBianMa", wmImNoticeH.getSupCode());
            if (mdsup != null) {
                wmImNoticeH.setSupName(mdsup.getZhongWenQch());
            }
            wmImNoticeH.setImCusCode(poid);

            wmImNoticeHService.addMain(wmImNoticeH, wmImNoticeIListnew);


            systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "??????????????????????????????";
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
    @RequestMapping(params = "doUpdatesp")
    @ResponseBody
    public AjaxJson doUpdatesp(String id, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String message = "????????????";
        try {
            TWzPoHeadEntity tWzPoHead = systemService.get(TWzPoHeadEntity.class, Integer.parseInt(id));
            tWzPoHead.setBpmStatus("2");
            //===================================================================================
            //??????-?????????????????????
            String hql0 = "from TWzPoItemEntity where 1 = 1 AND pO_ID = ? ";
            try {
                List<TWzPoItemEntity> tWzPoItemEntityList = systemService.findHql(hql0, id.toString());
                for (TWzPoItemEntity page : tWzPoItemEntityList) {
                    TWzMaterialEntity wz = systemService.findUniqueByProperty(TWzMaterialEntity.class, "matCode", page.getMatCode());
                    try {
                        if (!StringUtil.isEmpty(page.getMatPrice())) {
                            wz.setMatPrice(new BigDecimal(page.getMatPrice()));
                            systemService.updateEntitie(wz);
                        }
                    } catch (Exception e) {
                    }
                }
            } catch (Exception e) {
            }
            systemService.saveOrUpdate(tWzPoHead);
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
     * ??????????????????
     *
     * @return
     */
    @RequestMapping(params = "doUpdatecom")
    @ResponseBody
    public AjaxJson doUpdatecom(String id, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String message = "????????????";
        try {
            TWzPoHeadEntity tWzPoHead = systemService.get(TWzPoHeadEntity.class, Integer.parseInt(id));
            tWzPoHead.setBpmStatus("3");
            systemService.saveOrUpdate(tWzPoHead);
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
     * ????????????????????????
     *
     * @return
     */
    @RequestMapping(params = "doUpdate")
    @ResponseBody
    public AjaxJson doUpdate(TWzPoHeadEntity tWzPoHead, TWzPoHeadPage tWzPoHeadPage, HttpServletRequest request) {
        List<TWzPoItemEntity> tWzPoItemList = tWzPoHeadPage.getTWzPoItemList();
        AjaxJson j = new AjaxJson();
        String message = "????????????";
        try {
            tWzPoHeadService.updateMain(tWzPoHead, tWzPoItemList);
            systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
        } catch (Exception e) {
            e.printStackTrace();
            message = "??????????????????????????????";
            throw new BusinessException(e.getMessage());
        }
        j.setMsg(message);
        return j;
    }

    /**
     * ????????????????????????????????????
     *
     * @return
     */
    @RequestMapping(params = "goAdd")
    public ModelAndView goAdd(TWzPoHeadEntity tWzPoHead, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(tWzPoHead.getId())) {
            tWzPoHead = tWzPoHeadService.getEntity(TWzPoHeadEntity.class, tWzPoHead.getId());
            req.setAttribute("tWzPoHeadPage", tWzPoHead);
        }
        return new ModelAndView("com/zzjee/wzyw/tWzPoHead-add");
    }

    /**
     * ????????????????????????????????????
     *
     * @return
     */
    @RequestMapping(params = "goUpdate")
    public ModelAndView goUpdate(TWzPoHeadEntity tWzPoHead, HttpServletRequest req) {
        if (StringUtil.isNotEmpty(tWzPoHead.getId())) {
            tWzPoHead = tWzPoHeadService.getEntity(TWzPoHeadEntity.class, tWzPoHead.getId());
            req.setAttribute("tWzPoHeadPage", tWzPoHead);
        }
        return new ModelAndView("com/zzjee/wzyw/tWzPoHead-update");
    }


    /**
     * ??????????????????[?????????????????????]
     *
     * @return
     */
    @RequestMapping(params = "tWzPoItemList")
    public ModelAndView tWzPoItemList(TWzPoHeadEntity tWzPoHead, HttpServletRequest req) {

        //===================================================================================
        //????????????
        Object id0 = tWzPoHead.getId();
        //===================================================================================
        //??????-?????????????????????
        String hql0 = "from TWzPoItemEntity where 1 = 1 AND pO_ID = ? ";
        try {
            List<TWzPoItemEntity> tWzPoItemEntityList = systemService.findHql(hql0, id0);
            req.setAttribute("tWzPoItemList", tWzPoItemEntityList);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return new ModelAndView("com/zzjee/wzyw/tWzPoItemList");
    }

    /**
     * ??????excel
     *
     * @param request
     * @param response
     */
    @RequestMapping(params = "exportXls")
    public String exportXls(TWzPoHeadEntity tWzPoHead, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap map) {
        CriteriaQuery cq = new CriteriaQuery(TWzPoHeadEntity.class, dataGrid);
        //?????????????????????
        org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tWzPoHead);
        try {
            //???????????????????????????
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        cq.add();
        List<TWzPoHeadEntity> list = this.tWzPoHeadService.getListByCriteriaQuery(cq, false);
        List<TWzPoHeadPage> pageList = new ArrayList<TWzPoHeadPage>();
        if (list != null && list.size() > 0) {
            for (TWzPoHeadEntity entity : list) {
                try {
                    TWzPoHeadPage page = new TWzPoHeadPage();
                    MyBeanUtils.copyBeanNotNull2Bean(entity, page);
                    Object id0 = entity.getId();
                    String hql0 = "from TWzPoItemEntity where 1 = 1 AND pO_ID = ? ";
                    List<TWzPoItemEntity> tWzPoItemEntityList = systemService.findHql(hql0, id0.toString());
                    page.setTWzPoItemList(tWzPoItemEntityList);
                    pageList.add(page);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
            }
        }
        map.put(NormalExcelConstants.FILE_NAME, "????????????");
        map.put(NormalExcelConstants.CLASS, TWzPoHeadPage.class);
        map.put(NormalExcelConstants.PARAMS, new ExportParams("??????????????????", "?????????:" + ResourceUtil.getSessionUser().getRealName(),
                "????????????"));
        map.put(NormalExcelConstants.DATA_LIST, pageList);
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
    }

    /**
     * ??????excel????????????
     *
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
            MultipartFile file = entity.getValue();// ????????????????????????
            ImportParams params = new ImportParams();
            params.setTitleRows(2);
            params.setHeadRows(2);
            params.setNeedSave(true);
            try {
                List<TWzPoHeadPage> list = ExcelImportUtil.importExcel(file.getInputStream(), TWzPoHeadPage.class, params);
                TWzPoHeadEntity entity1 = null;
                for (TWzPoHeadPage page : list) {
                    entity1 = new TWzPoHeadEntity();
                    MyBeanUtils.copyBeanNotNull2Bean(page, entity1);
                    tWzPoHeadService.addMain(entity1, page.getTWzPoItemList());
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

    /**
     * ??????excel ?????????
     */
    @RequestMapping(params = "exportXlsByT")
    public String exportXlsByT(ModelMap map) {
        map.put(NormalExcelConstants.FILE_NAME, "??????????????????");
        map.put(NormalExcelConstants.CLASS, TWzPoHeadPage.class);
        map.put(NormalExcelConstants.PARAMS, new ExportParams("????????????????????????", "?????????:" + ResourceUtil.getSessionUser().getRealName(),
                "????????????"));
        map.put(NormalExcelConstants.DATA_LIST, new ArrayList());
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
    }

    /**
     * ??????????????????
     *
     * @return
     */
    @RequestMapping(params = "upload")
    public ModelAndView upload(HttpServletRequest req) {
        req.setAttribute("controller_name", "tWzPoHeadController");
        return new ModelAndView("common/upload/pub_excel_upload");
    }


    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "??????????????????????????????", produces = "application/json", httpMethod = "GET")
    public ResponseMessage<List<TWzPoHeadPage>> list() {
        List<TWzPoHeadEntity> list = tWzPoHeadService.getList(TWzPoHeadEntity.class);
        List<TWzPoHeadPage> pageList = new ArrayList<TWzPoHeadPage>();
        if (list != null && list.size() > 0) {
            for (TWzPoHeadEntity entity : list) {
                try {
                    TWzPoHeadPage page = new TWzPoHeadPage();
                    MyBeanUtils.copyBeanNotNull2Bean(entity, page);
                    Object id0 = entity.getId();
                    String hql0 = "from TWzPoItemEntity where 1 = 1 AND pO_ID = ? ";
                    List<TWzPoItemEntity> tWzPoItemOldList = this.tWzPoHeadService.findHql(hql0, id0);
                    page.setTWzPoItemList(tWzPoItemOldList);
                    pageList.add(page);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
            }
        }
        return Result.success(pageList);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "??????ID??????????????????????????????", notes = "??????ID??????????????????????????????", httpMethod = "GET", produces = "application/json")
    public ResponseMessage<?> get(@ApiParam(required = true, name = "id", value = "ID") @PathVariable("id") String id) {
        TWzPoHeadEntity task = tWzPoHeadService.get(TWzPoHeadEntity.class, id);
        if (task == null) {
            return Result.error("??????ID????????????????????????????????????");
        }
        TWzPoHeadPage page = new TWzPoHeadPage();
        try {
            MyBeanUtils.copyBeanNotNull2Bean(task, page);
            Object id0 = task.getId();
            String hql0 = "from TWzPoItemEntity where 1 = 1 AND pO_ID = ? ";
            List<TWzPoItemEntity> tWzPoItemOldList = this.tWzPoHeadService.findHql(hql0, id0);
            page.setTWzPoItemList(tWzPoItemOldList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success(page);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "????????????????????????")
    public ResponseMessage<?> create(@ApiParam(name = "????????????????????????") @RequestBody TWzPoHeadPage tWzPoHeadPage, UriComponentsBuilder uriBuilder) {
        //??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
        Set<ConstraintViolation<TWzPoHeadPage>> failures = validator.validate(tWzPoHeadPage);
        if (!failures.isEmpty()) {
            return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
        }

        //??????
        List<TWzPoItemEntity> tWzPoItemList = tWzPoHeadPage.getTWzPoItemList();

        TWzPoHeadEntity tWzPoHead = new TWzPoHeadEntity();
        try {
            MyBeanUtils.copyBeanNotNull2Bean(tWzPoHeadPage, tWzPoHead);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return Result.error("??????????????????????????????");
        }
        tWzPoHeadService.addMain(tWzPoHead, tWzPoItemList);

        return Result.success(tWzPoHead);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "????????????????????????", notes = "????????????????????????")
    public ResponseMessage<?> update(@RequestBody TWzPoHeadPage tWzPoHeadPage) {
        //??????JSR303 Bean Validator????????????????????????????????????400????????????json?????????????????????.
        Set<ConstraintViolation<TWzPoHeadPage>> failures = validator.validate(tWzPoHeadPage);
        if (!failures.isEmpty()) {
            return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
        }

        //??????
        List<TWzPoItemEntity> tWzPoItemList = tWzPoHeadPage.getTWzPoItemList();

        TWzPoHeadEntity tWzPoHead = new TWzPoHeadEntity();
        try {
            MyBeanUtils.copyBeanNotNull2Bean(tWzPoHeadPage, tWzPoHead);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return Result.error("??????????????????????????????");
        }
        tWzPoHeadService.updateMain(tWzPoHead, tWzPoItemList);

        //???Restful???????????????204?????????, ?????????. ???????????????200?????????.
        return Result.success();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "????????????????????????")
    public ResponseMessage<?> delete(@ApiParam(name = "id", value = "ID", required = true) @PathVariable("id") String id) {
        logger.info("delete[{}]" + id);
        // ??????
        if (StringUtils.isEmpty(id)) {
            return Result.error("ID????????????");
        }
        try {
            TWzPoHeadEntity tWzPoHead = tWzPoHeadService.get(TWzPoHeadEntity.class, id);
            tWzPoHeadService.delMain(tWzPoHead);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("??????????????????????????????");
        }

        return Result.success();
    }
}
