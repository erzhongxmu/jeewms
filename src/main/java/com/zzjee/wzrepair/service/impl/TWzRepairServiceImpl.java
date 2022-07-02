package com.zzjee.wzrepair.service.impl;

import com.zzjee.wzrepair.entity.TWzRepairEntity;
import com.zzjee.wzrepair.service.TWzRepairServiceI;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.ApplicationContextUtil;
import org.jeecgframework.core.util.MyClassLoader;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.web.cgform.enhance.CgformEnhanceJavaInter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service("tWzRepairService")
@Transactional
public class TWzRepairServiceImpl extends CommonServiceImpl implements TWzRepairServiceI {


 	@Override
    public void delete(TWzRepairEntity entity) throws Exception{
 		super.delete(entity);
 		//执行删除操作增强业务
		this.doDelBus(entity);
 	}

 	@Override
    public Serializable save(TWzRepairEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		//执行新增操作增强业务
 		this.doAddBus(entity);
 		return t;
 	}

 	@Override
    public void saveOrUpdate(TWzRepairEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 		//执行更新操作增强业务
 		this.doUpdateBus(entity);
 	}

 	/**
	 * 新增操作增强业务
	 * @param t
	 * @return
	 */
	private void doAddBus(TWzRepairEntity t) throws Exception{
		//-----------------sql增强 start----------------------------
	 	//-----------------sql增强 end------------------------------

	 	//-----------------java增强 start---------------------------
	 	//-----------------java增强 end-----------------------------
 	}
 	/**
	 * 更新操作增强业务
	 * @param t
	 * @return
	 */
	private void doUpdateBus(TWzRepairEntity t) throws Exception{
		//-----------------sql增强 start----------------------------
	 	//-----------------sql增强 end------------------------------

	 	//-----------------java增强 start---------------------------
	 	//-----------------java增强 end-----------------------------
 	}
 	/**
	 * 删除操作增强业务
	 * @param t
	 * @return
	 */
	private void doDelBus(TWzRepairEntity t) throws Exception{
	    //-----------------sql增强 start----------------------------
	 	//-----------------sql增强 end------------------------------

	 	//-----------------java增强 start---------------------------
	 	//-----------------java增强 end-----------------------------
 	}

 	private Map<String,Object> populationMap(TWzRepairEntity t){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", t.getId());
		map.put("create_name", t.getCreateName());
		map.put("create_by", t.getCreateBy());
		map.put("create_date", t.getCreateDate());
		map.put("update_name", t.getUpdateName());
		map.put("update_by", t.getUpdateBy());
		map.put("update_date", t.getUpdateDate());
		map.put("sys_org_code", t.getSysOrgCode());
		map.put("sys_company_code", t.getSysCompanyCode());
		map.put("bpm_status", t.getBpmStatus());
		map.put("mat_code", t.getMatCode());
		map.put("mat_name", t.getMatName());
		map.put("mat_location", t.getMatLocation());
		map.put("mat_qty", t.getMatQty());
		map.put("mat_guige", t.getMatGuige());
		map.put("mat_unit", t.getMatUnit());
		map.put("mat_oriamount", t.getMatOriamount());
		map.put("mat_nowamount", t.getMatNowamount());
		map.put("repair_date", t.getRepairDate());
		map.put("repair_user", t.getRepairUser());
		map.put("qm_user", t.getQmUser());
		map.put("repair_remark", t.getRepairRemark());
		map.put("ck_id", t.getCkId());
		map.put("ck_itme_id", t.getCkItmeId());
		return map;
	}

 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @param t
	 * @return
	 */
 	public String replaceVal(String sql,TWzRepairEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{create_name}",String.valueOf(t.getCreateName()));
 		sql  = sql.replace("#{create_by}",String.valueOf(t.getCreateBy()));
 		sql  = sql.replace("#{create_date}",String.valueOf(t.getCreateDate()));
 		sql  = sql.replace("#{update_name}",String.valueOf(t.getUpdateName()));
 		sql  = sql.replace("#{update_by}",String.valueOf(t.getUpdateBy()));
 		sql  = sql.replace("#{update_date}",String.valueOf(t.getUpdateDate()));
 		sql  = sql.replace("#{sys_org_code}",String.valueOf(t.getSysOrgCode()));
 		sql  = sql.replace("#{sys_company_code}",String.valueOf(t.getSysCompanyCode()));
 		sql  = sql.replace("#{bpm_status}",String.valueOf(t.getBpmStatus()));
 		sql  = sql.replace("#{mat_code}",String.valueOf(t.getMatCode()));
 		sql  = sql.replace("#{mat_name}",String.valueOf(t.getMatName()));
 		sql  = sql.replace("#{mat_location}",String.valueOf(t.getMatLocation()));
 		sql  = sql.replace("#{mat_qty}",String.valueOf(t.getMatQty()));
 		sql  = sql.replace("#{mat_guige}",String.valueOf(t.getMatGuige()));
 		sql  = sql.replace("#{mat_unit}",String.valueOf(t.getMatUnit()));
 		sql  = sql.replace("#{mat_oriamount}",String.valueOf(t.getMatOriamount()));
 		sql  = sql.replace("#{mat_nowamount}",String.valueOf(t.getMatNowamount()));
 		sql  = sql.replace("#{repair_date}",String.valueOf(t.getRepairDate()));
 		sql  = sql.replace("#{repair_user}",String.valueOf(t.getRepairUser()));
 		sql  = sql.replace("#{qm_user}",String.valueOf(t.getQmUser()));
 		sql  = sql.replace("#{repair_remark}",String.valueOf(t.getRepairRemark()));
 		sql  = sql.replace("#{ck_id}",String.valueOf(t.getCkId()));
 		sql  = sql.replace("#{ck_itme_id}",String.valueOf(t.getCkItmeId()));
 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
 		return sql;
 	}

 	/**
	 * 执行JAVA增强
	 */
 	private void executeJavaExtend(String cgJavaType,String cgJavaValue,Map<String,Object> data) throws Exception {
 		if(StringUtil.isNotEmpty(cgJavaValue)){
			Object obj = null;
			try {
				if("class".equals(cgJavaType)){
					//因新增时已经校验了实例化是否可以成功，所以这块就不需要再做一次判断
					obj = MyClassLoader.getClassByScn(cgJavaValue).newInstance();
				}else if("spring".equals(cgJavaType)){
					obj = ApplicationContextUtil.getContext().getBean(cgJavaValue);
				}
				if(obj instanceof CgformEnhanceJavaInter){
					CgformEnhanceJavaInter javaInter = (CgformEnhanceJavaInter) obj;
					javaInter.execute("t_wz_repair",data);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("执行JAVA增强出现异常！");
			}
		}
 	}
}