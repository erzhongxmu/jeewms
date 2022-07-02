package com.zzjee.wzyw.controller;

import com.alibaba.fastjson.JSONArray;
import com.zzjee.md.entity.MvGoodsEntity;
import com.zzjee.util.ReportUtils;
import com.zzjee.wm.entity.WmOmNoticeHEntity;
import com.zzjee.wm.entity.WmOmNoticeIEntity;
import com.zzjee.wm.service.WmOmNoticeHServiceI;
import com.zzjee.wmutil.wmUtil;
import com.zzjee.wz.entity.TWzMaterialEntity;
import com.zzjee.wzrepair.entity.TWzRepairEntity;
import com.zzjee.wzrepair.service.TWzRepairServiceI;
import com.zzjee.wzyw.entity.TWzCkHeadEntity;
import com.zzjee.wzyw.entity.TWzCkItemDto;
import com.zzjee.wzyw.entity.TWzCkItemEntity;
import com.zzjee.wzyw.page.TWzCkHeadPage;
import com.zzjee.wzyw.service.TWzCkHeadServiceI;
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
import org.jeecgframework.web.system.pojo.base.TSUser;
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
 * @Title: Controller
 * @Description: 物料出库
 * @author onlineGenerator
 * @date 2018-05-20 21:43:29
 * @version V1.0
 *
 */
@Api(value="TWzCkHead",description="物料出库",tags="tWzCkHeadController")
@Controller
@RequestMapping("/tWzCkHeadController")
public class TWzCkHeadController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(TWzCkHeadController.class);

	@Autowired
	private TWzCkHeadServiceI tWzCkHeadService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;

	@Autowired
    JdbcDao jdbcDao;
	@Autowired
	private WmOmNoticeHServiceI wmOmNoticeHService;
	@Autowired
	private TWzRepairServiceI tWzRepairService;
	/**
	 * 物料出库列表 页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/wzyw/tWzCkHeadList");
	}
	@RequestMapping(params = "listsp")
	public ModelAndView listsp(HttpServletRequest request) {
		return new ModelAndView("com/zzjee/wzyw/tWzCkHeadspList");
	}
	/**
	 * easyui AJAX请求数据
	 *
	 * @param request
	 * @param response
	 * @param dataGrid
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(TWzCkHeadEntity tWzCkHead,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TWzCkHeadEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tWzCkHead);
		try{
		//自定义追加查询条件
		String query_docDate_begin = request.getParameter("docDate_begin");
		String query_docDate_end = request.getParameter("docDate_end");
		if(StringUtil.isNotEmpty(query_docDate_begin)){
			cq.ge("docDate", Integer.parseInt(query_docDate_begin));
		}
		if(StringUtil.isNotEmpty(query_docDate_end)){
			cq.le("docDate", Integer.parseInt(query_docDate_end));
		}
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.tWzCkHeadService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	@RequestMapping(params = "datagridsp")
	public void datagridsp(TWzCkHeadEntity tWzCkHead,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TWzCkHeadEntity.class, dataGrid);
		//查询条件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tWzCkHead);
		try{
			//自定义追加查询条件
			String query_docDate_begin = request.getParameter("docDate_begin");
			String query_docDate_end = request.getParameter("docDate_end");
			if(StringUtil.isNotEmpty(query_docDate_begin)){
				cq.ge("docDate", Integer.parseInt(query_docDate_begin));
			}
			if(StringUtil.isNotEmpty(query_docDate_end)){
				cq.le("docDate", Integer.parseInt(query_docDate_end));
			}
			String[] ina = new String[2];
			ina[0]="2";
			ina[1]="3";
			cq.in("bpmStatus",ina);
//			cq.eq("updateBy",ResourceUtil.getSessionUser().getUserName());
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.tWzCkHeadService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}



	/**
	 * 删除物料出库
	 *
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(TWzCkHeadEntity tWzCkHead, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		tWzCkHead = systemService.getEntity(TWzCkHeadEntity.class, tWzCkHead.getId());
		String message = "物料出库删除成功";
		try{
			tWzCkHeadService.delMain(tWzCkHead);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "物料出库删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除物料出库
	 *
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		String message = "物料出库删除成功";
		try{
			for(String id:ids.split(",")){
				TWzCkHeadEntity tWzCkHead = systemService.getEntity(TWzCkHeadEntity.class,
				Integer.parseInt(id)
				);
//				tWzCkHead.setBpmStatus("已删除");
//				systemService.updateEntitie(tWzCkHead);
				tWzCkHeadService.delMain(tWzCkHead);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "物料出库删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加物料出库
	 *
	 * @return
	 */
	@RequestMapping(params = "doAddcheck")
	@ResponseBody
	public AjaxJson doAddcheck(String mat_code , String  mat_location, String mat_qty, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String message = "库存充足";
		try{
			String sql = "select mat_qty as qua from v_wz_stock_a  t where  t.mat_code =  '"
					+ mat_code + "'" +  " and t.mat_location =  '"
					+ mat_location + "'" ;
			Map<String, Object> binMap	 = systemService.findOneForJdbc(sql);
			Double stock = Double.parseDouble(binMap.get("qua").toString());
			Double mty = Double.parseDouble(request.getParameter("mat_qty").toString());
			if(binMap!=null){
				if( stock < mty){
					j.setSuccess(false);
					message = request.getParameter("mat_code").toString() +"库存为"+binMap.get("qua").toString();
					j.setMsg(message);
					return j;
				}
			}

//			tWzCkHeadService.addMain(tWzCkHead, tWzCkItemList);
//			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){

		}
		j.setMsg(message);
		return j;
	}




	/**
	 * 添加物料出库
	 *
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(TWzCkHeadEntity tWzCkHead, TWzCkHeadPage tWzCkHeadPage, HttpServletRequest request) {
		List<TWzCkItemEntity> tWzCkItemList =  tWzCkHeadPage.getTWzCkItemList();
		AjaxJson j = new AjaxJson();
		String message = "添加成功";
		try{
			if(tWzCkHead.getCkSgsp().equals("Y")){
				tWzCkHead.setBpmStatus("2");
			}else{
				tWzCkHead.setBpmStatus("3");
			}

			try{
//				String[] strs = tWzCkHead.getCkUsername() .split(",");
				//第一个分号之前的字符串自然就是数组里的第一个
				String username = tWzCkHead.getCkUsername();
				TSUser user =  systemService.findUniqueByProperty(TSUser.class,"userName",username);
				tWzCkHead.setCkName(user.getRealName());
				tWzCkHead.setCkUsername(user.getUserName());
				tWzCkHead.setOrgCode(user.getUserOrgList().get(0).getTsDepart().getOrgCode());
				tWzCkHead.setOrgName(user.getUserOrgList().get(0).getTsDepart().getDepartname());
			}catch (Exception e){

			}


			tWzCkHeadService.addMain(tWzCkHead, tWzCkItemList);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "物料出库添加失败";
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
		TWzCkHeadEntity tWzCkHead = systemService.getEntity(TWzCkHeadEntity.class,
				Integer.parseInt(id));// 获取抬头
		String hql0 = "from TWzCkItemEntity where 1 = 1 AND wZCK_HID = ? ";
		String sql = "select c.mat_code,c.mat_name,c.mat_location,h.ck_name,c.mat_qty,c.mat_unit,c.mat_price,(c.mat_qty*c.mat_price) as mat_amount,c.create_date\n" +
				" from t_wz_ck_item c LEFT JOIN t_wz_ck_head h ON c.wzck_hid = h.id where c.wzck_hid = " + id;
		List<TWzCkItemDto> tWzCkItemOldList =  jdbcDao.find(sql, TWzCkItemDto.class,null);
		try {
			StringBuffer sber = new StringBuffer();

			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();

			// 进行转码，使其支持中文文件名
			codedFileName = java.net.URLEncoder.encode("中文", "UTF-8");
			response.setHeader("content-disposition", "attachment;filename="
					+ id + ".xls");

			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("领料出库");
			sheet.setMargin(HSSFSheet.TopMargin,0.2);// 页边距（上）
			sheet.setMargin(HSSFSheet.BottomMargin,0.1);// 页边距（下）
			sheet.setMargin(HSSFSheet.LeftMargin,0.8);// 页边距（左）
			sheet.setMargin(HSSFSheet.RightMargin,0.0);// 页边距（右
			sheet.setColumnWidth(0, 5 * 256);
			sheet.setColumnWidth(1, 10 * 256);
			sheet.setColumnWidth(2, 15 * 200);
			sheet.setColumnWidth(3, 10 * 256);
			sheet.setColumnWidth(4, 10 * 256);
			sheet.setColumnWidth(5, 10 * 256);
			sheet.setColumnWidth(6, 5 * 256);
			sheet.setColumnWidth(7, 5 * 256);
			sheet.setColumnWidth(8, 10 * 256);
			//{ "序号", "物料编码", "物料名称",  "仓库", "领用人",  "数量", "单位", "单价","金额" }
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

			// Font f3=wb.createFont();
			// f3.setFontHeightInPoints((short) 10);
			// f3.setColor(IndexedColors.RED.getIndex());

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


			cs3.setFont(f2);
			cs3.setBorderLeft(CellStyle.BORDER_THIN);
			cs3.setBorderRight(CellStyle.BORDER_THIN);
			cs3.setBorderTop(CellStyle.BORDER_THIN);
			cs3.setBorderBottom(CellStyle.BORDER_THIN);
			cs3.setAlignment(CellStyle.ALIGN_CENTER);


			cs4.setFont(f2);
			cs4.setBorderLeft(CellStyle.BORDER_THIN);
			cs4.setBorderRight(CellStyle.BORDER_THIN);
			cs4.setBorderTop(CellStyle.BORDER_THIN);
			cs4.setBorderBottom(CellStyle.BORDER_THIN);


			cs5.setFont(f2);
			cs5.setBorderLeft(CellStyle.BORDER_NONE);
			cs5.setBorderRight(CellStyle.BORDER_NONE);
			cs5.setBorderTop(CellStyle.BORDER_NONE);
			cs5.setBorderBottom(CellStyle.BORDER_NONE);
//			cs5.setWrapText(true);

			Row row1 = sheet.createRow((short) 1); // 第二行标题
			row1.setHeight((short) 700);
			Cell cellTitle = row1.createCell(0);
			cellTitle.setCellValue(ResourceUtil.getConfigByName("comname")+"出库单");
			cellTitle.setCellStyle(cs);
			Row rowHead1 = sheet.createRow((short) 2); // 头部第一行
			rowHead1.setHeight((short) 500);
			Cell cellHead11 = rowHead1.createCell(0);
			cellHead11.setCellValue("出库单号：" + id+"                    类型：" + tWzCkHead.getBy2());
			cellHead11.setCellStyle(cs2);
			Cell cellHead12 = rowHead1.createCell(6);

			cellHead12.setCellValue("出库日期：" + DateUtils.date2Str(tWzCkHead.getCreateDate(), DateUtils.date_sdf));
			cellHead12.setCellStyle(cs2);


			// 合并单元格
			CellRangeAddress c = new CellRangeAddress(0, 0, 0, 8); // 第一行空白
			CellRangeAddress c0 = new CellRangeAddress(1, 1, 0, 8);// 第二行标题
			CellRangeAddress c11 = new CellRangeAddress(2, 2, 0, 5);// 第三行供应商
			CellRangeAddress c12 = new CellRangeAddress(2, 2, 6, 8);// 第三行单据日期

			sheet.addMergedRegion(c);
			sheet.addMergedRegion(c0);
			sheet.addMergedRegion(c11);
			sheet.addMergedRegion(c12);
			// sheet.addMergedRegion(c7);

			Row rowColumnName = sheet.createRow((short) 4); // 列名
			rowColumnName.setHeight((short) 500);
			String[] columnNames = { "序号", "物料编码", "物料名称",  "仓库", "领用人",  "数量", "单位", "单价",
					"金额" };

			Double totalAmount = 0d;
			Double totalNumber = 0d;

			for (int i = 0; i < columnNames.length; i++) {
				Cell cell = rowColumnName.createCell(i);
				cell.setCellValue(columnNames[i]);
				cell.setCellStyle(cs3);
			}
			int cellsNum = 4;
			int cerconNo = 1;
			for (int i = 0; i < tWzCkItemOldList.size(); i++) {
				TWzCkItemDto entity = tWzCkItemOldList.get(i);
				cellsNum++;
				Row rowColumnValue = sheet.createRow((short) cellsNum); // 列名
				rowColumnValue.setHeight((short) 500);
				Cell cell1 = rowColumnValue.createCell(0);
				cell1.setCellValue(cerconNo);
				cell1.setCellStyle(cs3);
				Cell cell2 = rowColumnValue.createCell(1);


				try {
						cell2.setCellStyle(cs3);
						cell2.setCellValue(entity.getMatCode());
					}catch (Exception e){

				}
				try {
				Cell cell3 = rowColumnValue.createCell(2);
				cell3.setCellValue(entity.getMatName());
				cell3.setCellStyle(cs3);
			}catch (Exception e){

			}
			try {
				Cell cell4 = rowColumnValue.createCell(3);
				cell4.setCellValue(entity.getMatLocation());
				cell4.setCellStyle(cs3);
			}catch (Exception e){

			}
		try {
				Cell cell5 = rowColumnValue.createCell(4);
				cell5.setCellValue(entity.getCkName());
				cell5.setCellStyle(cs3);
			}catch (Exception e){

			}
		try {


				Cell cell6 = rowColumnValue.createCell(5);
				cell6.setCellValue(StringUtil.moneyToString(entity.getMatQty(),"#.0000"));
				cell6.setCellStyle(cs3);
			    totalNumber = totalNumber + Double.parseDouble(entity.getMatQty());
				}catch (Exception e){

				}
				try {
					Cell cell7 = rowColumnValue.createCell(6);
					cell7.setCellValue(entity.getMatUnit());
					cell7.setCellStyle(cs3);
				}catch (Exception e){

				}
				try {
					Cell cell8 = rowColumnValue.createCell(7);
					cell8.setCellValue(StringUtil.moneyToString(entity.getMatPrice(),"#.00"));
					cell8.setCellStyle(cs3);
				}catch (Exception e){

				}
				try {
					Cell cell9 = rowColumnValue.createCell(8);
					cell9.setCellValue(StringUtil.moneyToString(entity.getMatAmount(),"#.00"));
					cell9.setCellStyle(cs3);
					totalAmount = totalAmount + Double.parseDouble(entity.getMatAmount());
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
			cellTotatl4.setCellValue("");
			cellTotatl4.setCellStyle(cs3);
			Cell cellTotatl5 = rowColumnInfo.createCell(8);
			cellTotatl5.setCellValue(StringUtil.moneyToString(totalAmount,"#.00"));
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
			cellrow3.setCellValue("制单人：                              领货人：                                审核人：");
			cellrow3.setCellStyle(cs5);

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
	 * 更新物料出库
	 *
	 * @return
	 */
	@RequestMapping(params = "doUpdatesp")
	@ResponseBody
	public AjaxJson doUpdatesp(String id, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String message = "审批成功";
		try{
			TWzCkHeadEntity tWzCkHead = systemService.get(TWzCkHeadEntity.class,Integer.parseInt(id));
			tWzCkHead.setBpmStatus("1");
			String hql0 = "from TWzCkItemEntity where 1 = 1 AND wzck_hid = ? ";
			List<TWzCkItemEntity> tWzCkItemList = systemService.findHql(hql0,id);
			for (TWzCkItemEntity tWzCkItemEntity : tWzCkItemList) {
				tWzCkItemEntity.setBpmStatus("1");
				systemService.saveOrUpdate(tWzCkItemEntity);
				TWzRepairEntity tWzRepairEntity = new TWzRepairEntity();
				tWzRepairEntity.setCkId(tWzCkHead.getId().toString());
				tWzRepairEntity.setCkItmeId(tWzCkItemEntity.getId());
				tWzRepairEntity.setMatCode(tWzCkItemEntity.getMatCode());
				tWzRepairEntity.setMatName(tWzCkItemEntity.getMatName());
				tWzRepairEntity.setMatLocation(tWzCkItemEntity.getMatLocation());
				tWzRepairEntity.setMatQty(tWzCkItemEntity.getMatQty());
				TWzMaterialEntity wz = systemService.findUniqueByProperty(TWzMaterialEntity.class, "matCode", tWzCkItemEntity.getMatCode());
				tWzRepairEntity.setMatGuige(wz.getMatGuige());
 				tWzRepairService.save(tWzRepairEntity);
			}
			systemService.saveOrUpdate(tWzCkHead);
			 String cuscosde = "";
			 String ckid = tWzCkHead.getId().toString();
				List<WmOmNoticeHEntity>  wmomh = systemService.findByProperty(WmOmNoticeHEntity.class, "imCusCode", ckid);
				if(wmomh!=null&&wmomh.size()>0){
					message="出库通知已生成";
					j.setMsg(message);
					return j;
				}

				List<WmOmNoticeIEntity> wmomNoticeIListnew = new ArrayList<WmOmNoticeIEntity>();
				for (TWzCkItemEntity page : tWzCkItemList) {
 						WmOmNoticeIEntity wmi = new WmOmNoticeIEntity();
						wmi.setGoodsId(page.getMatCode());
						MvGoodsEntity mvgoods = systemService.findUniqueByProperty(
								MvGoodsEntity.class, "goodsCode", wmi.getGoodsId());
						if (mvgoods != null) {
							wmi.setGoodsName(mvgoods.getGoodsName());
							wmi.setGoodsUnit(mvgoods.getShlDanWei());
						}
						try{
							wmi.setGoodsQua(page.getMatQty());
							String[] args=page.getMatQty().split("\\.");
							wmi.setGoodsQua(args[0]);
						}catch (Exception e){

						}

//                               wmi.setGoodsPrdData(billResult.getData().get(s).getDetail().get(k).getPdProdmadedate2User());
						wmi.setOtherId(page.getId());
//						wmi.setBinId(page.getBinId());
//						wmi.setBinOm(page.getBinOm());
						if(StringUtil.isNotEmpty(page.getMatBatch())){
							try{
								wmi.setGoodsProData(DateUtils.str2Date(page.getMatBatch(), DateUtils.date_sdf));

							}catch (Exception e){

							}
						}
						wmomNoticeIListnew.add(wmi);
 				}
				WmOmNoticeHEntity wmOmNoticeH = new WmOmNoticeHEntity();
//				wmOmNoticeH.setReMember(pageheader.getReMember());
//				wmOmNoticeH.setReCarno(pageheader.getReCarno());
//				wmOmNoticeH.setDelvMember(pageheader.getDelvMember());
//				wmOmNoticeH.setDelvMobile(pageheader.getDelvMobile());
//				wmOmNoticeH.setDelvData(DateUtils.date2Str()tWzCkHead.getDocDate());
				wmOmNoticeH.setOrderTypeCode("11");
				wmOmNoticeH.setCusCode(cuscosde);
				String noticeid = wmUtil.getNextomNoticeId(wmOmNoticeH.getOrderTypeCode());
				wmOmNoticeH.setOmNoticeId(noticeid);
				wmOmNoticeH.setOmBeizhu(tWzCkHead.getCkRemark());

				wmOmNoticeH.setImCusCode(ckid);
				wmOmNoticeHService.addMain(wmOmNoticeH, wmomNoticeIListnew);












			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "审批失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}



	/**
	 * 更新物料出库
	 *
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(TWzCkHeadEntity tWzCkHead, TWzCkHeadPage tWzCkHeadPage, HttpServletRequest request) {
		List<TWzCkItemEntity> tWzCkItemList =  tWzCkHeadPage.getTWzCkItemList();
		AjaxJson j = new AjaxJson();
		String message = "更新成功";
		try{

			try{
				String[] strs = tWzCkHead.getCkUsername() .split(",");
				//第一个分号之前的字符串自然就是数组里的第一个
				String userid = strs[0];
				TSUser user = systemService.get(TSUser.class,userid);
				tWzCkHead.setCkName(user.getRealName());
				tWzCkHead.setCkUsername(user.getUserName());

			}catch (Exception e){

			}

			tWzCkHeadService.updateMain(tWzCkHead, tWzCkItemList);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "更新物料出库失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 物料出库新增页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(TWzCkHeadEntity tWzCkHead, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tWzCkHead.getId())) {
			tWzCkHead = tWzCkHeadService.getEntity(TWzCkHeadEntity.class, tWzCkHead.getId());
			req.setAttribute("tWzCkHeadPage", tWzCkHead);
		}

		req.setAttribute("username", ResourceUtil.getSessionUser().getUserName());
		req.setAttribute("usernametext", ResourceUtil.getSessionUser().getRealName());

		return new ModelAndView("com/zzjee/wzyw/tWzCkHead-add");
	}

	/**
	 * 物料出库编辑页面跳转
	 *
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(TWzCkHeadEntity tWzCkHead, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tWzCkHead.getId())) {
			tWzCkHead = tWzCkHeadService.getEntity(TWzCkHeadEntity.class, tWzCkHead.getId());
			req.setAttribute("tWzCkHeadPage", tWzCkHead);
		}
		return new ModelAndView("com/zzjee/wzyw/tWzCkHead-update");
	}


	/**
	 * 加载明细列表[出库商品]
	 *
	 * @return
	 */
	@RequestMapping(params = "tWzCkItemList")
	public ModelAndView tWzCkItemList(TWzCkHeadEntity tWzCkHead, HttpServletRequest req) {

		//===================================================================================
		//获取参数
		Object id0 = tWzCkHead.getId();
		//===================================================================================
		//查询-出库商品
	    String hql0 = "from TWzCkItemEntity where 1 = 1 AND wZCK_HID = ? ";
	    try{
	    	List<TWzCkItemEntity> tWzCkItemEntityList = systemService.findHql(hql0,id0);
			req.setAttribute("tWzCkItemList", tWzCkItemEntityList);
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		return new ModelAndView("com/zzjee/wzyw/tWzCkItemList");
	}

	/**
	 * 加载明细列表[出库商品]不可编辑
	 *
	 * @return
	 */
	@RequestMapping(params = "tWzCkItemListUnEdit")
	public ModelAndView tWzCkItemListUnEdit(TWzCkHeadEntity tWzCkHead, HttpServletRequest req) {

		//===================================================================================
		//获取参数
		Object id0 = tWzCkHead.getId();
		//===================================================================================
		//查询-出库商品
		String hql0 = "from TWzCkItemEntity where 1 = 1 AND wZCK_HID = ? ";
		try{
			List<TWzCkItemEntity> tWzCkItemEntityList = systemService.findHql(hql0,id0);
			req.setAttribute("tWzCkItemList", tWzCkItemEntityList);
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		return new ModelAndView("com/zzjee/wzyw/tWzCkItemListUnEdit");
	}

    /**
    * 导出excel
    *
    * @param request
    * @param response
    */
    @RequestMapping(params = "exportXls")
    public String exportXls(TWzCkHeadEntity tWzCkHead, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid, ModelMap map) {
    	CriteriaQuery cq = new CriteriaQuery(TWzCkHeadEntity.class, dataGrid);
    	//查询条件组装器
    	org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tWzCkHead);
    	try{
    	//自定义追加查询条件
    	}catch (Exception e) {
    		throw new BusinessException(e.getMessage());
    	}
    	cq.add();
    	List<TWzCkHeadEntity> list=this.tWzCkHeadService.getListByCriteriaQuery(cq, false);
    	List<TWzCkHeadPage> pageList=new ArrayList<TWzCkHeadPage>();
        if(list!=null&&list.size()>0){
        	for(TWzCkHeadEntity entity:list){
        		try{
        		TWzCkHeadPage page=new TWzCkHeadPage();
        		   MyBeanUtils.copyBeanNotNull2Bean(entity,page);
            	    Object id0 = entity.getId();
				    String hql0 = "from TWzCkItemEntity where 1 = 1 AND wZCK_HID = ? ";
        	        List<TWzCkItemEntity> tWzCkItemEntityList = systemService.findHql(hql0,id0.toString());
            		page.setTWzCkItemList(tWzCkItemEntityList);
            		pageList.add(page);
            	}catch(Exception e){
            		logger.info(e.getMessage());
            	}
            }
        }
        map.put(NormalExcelConstants.FILE_NAME,"物料出库");
        map.put(NormalExcelConstants.CLASS,TWzCkHeadPage.class);
        map.put(NormalExcelConstants.PARAMS,new ExportParams("物料出库列表", "导出人:"+ ResourceUtil.getSessionUser().getRealName(),
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
				List<TWzCkHeadPage> list =  ExcelImportUtil.importExcel(file.getInputStream(), TWzCkHeadPage.class, params);
				TWzCkHeadEntity entity1=null;
				for (TWzCkHeadPage page : list) {
					entity1=new TWzCkHeadEntity();
					MyBeanUtils.copyBeanNotNull2Bean(page,entity1);
		            tWzCkHeadService.addMain(entity1, page.getTWzCkItemList());
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
		map.put(NormalExcelConstants.FILE_NAME,"物料出库");
		map.put(NormalExcelConstants.CLASS,TWzCkHeadPage.class);
		map.put(NormalExcelConstants.PARAMS,new ExportParams("物料出库列表", "导出人:"+ ResourceUtil.getSessionUser().getRealName(),
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
		req.setAttribute("controller_name", "tWzCkHeadController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}


 	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="物料出库列表信息",produces="application/json",httpMethod="GET")
	public ResponseMessage<List<TWzCkHeadPage>> list() {
		List<TWzCkHeadEntity> list= tWzCkHeadService.getList(TWzCkHeadEntity.class);
    	List<TWzCkHeadPage> pageList=new ArrayList<TWzCkHeadPage>();
        if(list!=null&&list.size()>0){
        	for(TWzCkHeadEntity entity:list){
        		try{
        			TWzCkHeadPage page=new TWzCkHeadPage();
        		   MyBeanUtils.copyBeanNotNull2Bean(entity,page);
					Object id0 = entity.getId();
				     String hql0 = "from TWzCkItemEntity where 1 = 1 AND wZCK_HID = ? ";
	    			List<TWzCkItemEntity> tWzCkItemOldList = this.tWzCkHeadService.findHql(hql0,id0);
            		page.setTWzCkItemList(tWzCkItemOldList);
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
	@ApiOperation(value="根据ID获取物料出库信息",notes="根据ID获取物料出库信息",httpMethod="GET",produces="application/json")
	public ResponseMessage<?> get(@ApiParam(required=true,name="id",value="ID")@PathVariable("id") String id) {
		TWzCkHeadEntity task = tWzCkHeadService.get(TWzCkHeadEntity.class, id);
		if (task == null) {
			return Result.error("根据ID获取物料出库信息为空");
		}
		TWzCkHeadPage page = new TWzCkHeadPage();
		try {
			MyBeanUtils.copyBeanNotNull2Bean(task, page);
				Object id0 = task.getId();
		    String hql0 = "from TWzCkItemEntity where 1 = 1 AND wZCK_HID = ? ";
			List<TWzCkItemEntity> tWzCkItemOldList = this.tWzCkHeadService.findHql(hql0,id0);
    		page.setTWzCkItemList(tWzCkItemOldList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Result.success(page);
	}

 	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="创建物料出库")
	public ResponseMessage<?> create(@ApiParam(name="物料出库对象")@RequestBody TWzCkHeadPage tWzCkHeadPage, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<TWzCkHeadPage>> failures = validator.validate(tWzCkHeadPage);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//保存
		List<TWzCkItemEntity> tWzCkItemList =  tWzCkHeadPage.getTWzCkItemList();

		TWzCkHeadEntity tWzCkHead = new TWzCkHeadEntity();
		try{
			MyBeanUtils.copyBeanNotNull2Bean(tWzCkHeadPage,tWzCkHead);
		}catch(Exception e){
            logger.info(e.getMessage());
            return Result.error("保存物料出库失败");
        }
		tWzCkHeadService.addMain(tWzCkHead, tWzCkItemList);

		return Result.success(tWzCkHead);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="更新物料出库",notes="更新物料出库")
	public ResponseMessage<?> update(@RequestBody TWzCkHeadPage tWzCkHeadPage) {
		//调用JSR303 Bean Validator进行校验，如果出错返回含400错误码及json格式的错误信息.
		Set<ConstraintViolation<TWzCkHeadPage>> failures = validator.validate(tWzCkHeadPage);
		if (!failures.isEmpty()) {
			return Result.error(JSONArray.toJSONString(BeanValidators.extractPropertyAndMessage(failures)));
		}

		//保存
		List<TWzCkItemEntity> tWzCkItemList =  tWzCkHeadPage.getTWzCkItemList();

		TWzCkHeadEntity tWzCkHead = new TWzCkHeadEntity();
		try{
			MyBeanUtils.copyBeanNotNull2Bean(tWzCkHeadPage,tWzCkHead);
		}catch(Exception e){
            logger.info(e.getMessage());
            return Result.error("物料出库更新失败");
        }
		tWzCkHeadService.updateMain(tWzCkHead, tWzCkItemList);

		//按Restful约定，返回204状态码, 无内容. 也可以返回200状态码.
		return Result.success();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value="删除物料出库")
	public ResponseMessage<?> delete(@ApiParam(name="id",value="ID",required=true)@PathVariable("id") String id) {
		logger.info("delete[{}]" + id);
		// 验证
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID不能为空");
		}
		try {
			TWzCkHeadEntity tWzCkHead = tWzCkHeadService.get(TWzCkHeadEntity.class, id);
			tWzCkHeadService.delMain(tWzCkHead);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("物料出库删除失败");
		}

		return Result.success();
	}
}
