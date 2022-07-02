package com.zzjee.wz.service.impl;


import com.zzjee.wz.entity.TWzMaterialEntity;
import com.zzjee.wz.service.TWzMaterialServiceI;
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

@Service("tWzMaterialService")
@Transactional
public class TWzMaterialServiceImpl extends CommonServiceImpl implements TWzMaterialServiceI {


 	@Override
    public void delete(TWzMaterialEntity entity) throws Exception{
 		super.delete(entity);
 		//执行删除操作增强业务
		this.doDelBus(entity);
 	}

 	@Override
    public Serializable save(TWzMaterialEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		//执行新增操作增强业务
 		this.doAddBus(entity);
 		return t;
 	}

 	@Override
    public void saveOrUpdate(TWzMaterialEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 		//执行更新操作增强业务
 		this.doUpdateBus(entity);
 	}

 	/**
	 * 新增操作增强业务
	 * @param t
	 * @return
	 */
	private void doAddBus(TWzMaterialEntity t) throws Exception{
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
	private void doUpdateBus(TWzMaterialEntity t) throws Exception{
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
	private void doDelBus(TWzMaterialEntity t) throws Exception{
	    //-----------------sql增强 start----------------------------
	 	//-----------------sql增强 end------------------------------

	 	//-----------------java增强 start---------------------------
	 	//-----------------java增强 end-----------------------------
 	}

 	private Map<String,Object> populationMap(TWzMaterialEntity t){
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
		map.put("mat_unit", t.getMatUnit());
		map.put("mat_guige", t.getMatGuige());
		map.put("mat_class", t.getMatClass());
		map.put("mat_price", t.getMatPrice());
		map.put("mat_aqkc", t.getMatAqkc());
		map.put("mat_location", t.getMatLocation());
		map.put("by1", t.getBy1());
		map.put("by2", t.getBy2());
		map.put("by3", t.getBy3());
		map.put("by4", t.getBy4());
		map.put("by5", t.getBy5());
		return map;
	}

 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @param t
	 * @return
	 */
 	public String replaceVal(String sql, TWzMaterialEntity t){
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
 		sql  = sql.replace("#{mat_unit}",String.valueOf(t.getMatUnit()));
 		sql  = sql.replace("#{mat_guige}",String.valueOf(t.getMatGuige()));
 		sql  = sql.replace("#{mat_class}",String.valueOf(t.getMatClass()));
 		sql  = sql.replace("#{mat_price}",String.valueOf(t.getMatPrice()));
 		sql  = sql.replace("#{mat_aqkc}",String.valueOf(t.getMatAqkc()));
 		sql  = sql.replace("#{mat_location}",String.valueOf(t.getMatLocation()));
 		sql  = sql.replace("#{by1}",String.valueOf(t.getBy1()));
 		sql  = sql.replace("#{by2}",String.valueOf(t.getBy2()));
 		sql  = sql.replace("#{by3}",String.valueOf(t.getBy3()));
 		sql  = sql.replace("#{by4}",String.valueOf(t.getBy4()));
 		sql  = sql.replace("#{by5}",String.valueOf(t.getBy5()));
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
					javaInter.execute("t_wz_material",data);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("执行JAVA增强出现异常！");
			}
		}
 	}
}
