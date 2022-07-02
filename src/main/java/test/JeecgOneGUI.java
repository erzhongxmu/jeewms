package test;

import com.sap.conn.jco.JCoTable;
import com.zzjee.sap.SapRFC;
import org.jeecgframework.codegenerate.window.CodeWindow;
import org.jeecgframework.core.util.DateUtils;

import java.sql.SQLOutput;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * 【单表模型】代码生成器入口
 * @author admin
 * @site www.jeecg.org
 *
 */
public class JeecgOneGUI {

	public static void main(String[] args) throws ParseException {
//		CodeWindow  codeWindow = new CodeWindow();
//		codeWindow.pack();
// 		try {
//			SapRFC saprfc = SapRFC.getInstance();
//			saprfc.prepare("ZZKMES_FM_PP112");
//			saprfc.execCall();
////            saprfc.getParamTableList()
//			JCoTable tab = saprfc.getParamTableList("ES_OUTPUT");
//			System.out.print("rows:" + tab.getNumRows());
// 		} catch (Exception e) {
//			System.out.print(e.getMessage());
//		}


		Calendar now = DateUtils.getCalendar();
		Calendar scrq = DateUtils.parseCalendar("2021-04-05","yyyy-MM-dd");
		int rq = DateUtils.dateDiff('d',now,scrq);
		System.out.println(rq);
	}
}
