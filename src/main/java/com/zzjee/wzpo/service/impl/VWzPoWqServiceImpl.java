package com.zzjee.wzpo.service.impl;

import com.zzjee.wzpo.entity.VWzPoWqEntity;
import com.zzjee.wzpo.service.VWzPoWqServiceI;
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

@Service("vWzPoWqService")
@Transactional
public class VWzPoWqServiceImpl extends CommonServiceImpl implements VWzPoWqServiceI {


 	@Override
    public void delete(VWzPoWqEntity entity) throws Exception{
 		super.delete(entity);
 		//执行删除操作增强业务
		this.doDelBus(entity);
 	}

 	@Override
    public Serializable save(VWzPoWqEntity entity) throws Exception{
 		Serializable t = super.save(entity);
 		//执行新增操作增强业务
 		this.doAddBus(entity);
 		return t;
 	}

 	@Override
    public void saveOrUpdate(VWzPoWqEntity entity) throws Exception{
 		super.saveOrUpdate(entity);
 		//执行更新操作增强业务
 		this.doUpdateBus(entity);
 	}

 	/**
	 * 新增操作增强业务
	 * @param t
	 * @return
	 */
	private void doAddBus(VWzPoWqEntity t) throws Exception{
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
	private void doUpdateBus(VWzPoWqEntity t) throws Exception{
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
	private void doDelBus(VWzPoWqEntity t) throws Exception{
	    //-----------------sql增强 start----------------------------
	 	//-----------------sql增强 end------------------------------

	 	//-----------------java增强 start---------------------------
	 	//-----------------java增强 end-----------------------------
 	}

 	private Map<String,Object> populationMap(VWzPoWqEntity t){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", t.getId());
		map.put("mat_code", t.getMatCode());
		map.put("mat_name", t.getMatName());
		map.put("mat_unit", t.getMatUnit());
		map.put("mat_location", t.getMatLocation());
		map.put("mat_batch", t.getMatBatch());
		map.put("mat_price", t.getMatPrice());
		map.put("mat_qty", t.getMatQty());
		map.put("mat_qty_wq", t.getMatQtyWq());
		return map;
	}

 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @param t
	 * @return
	 */
 	public String replaceVal(String sql,VWzPoWqEntity t){
 		sql  = sql.replace("#{id}",String.valueOf(t.getId()));
 		sql  = sql.replace("#{mat_code}",String.valueOf(t.getMatCode()));
 		sql  = sql.replace("#{mat_name}",String.valueOf(t.getMatName()));
 		sql  = sql.replace("#{mat_unit}",String.valueOf(t.getMatUnit()));
 		sql  = sql.replace("#{mat_location}",String.valueOf(t.getMatLocation()));
 		sql  = sql.replace("#{mat_batch}",String.valueOf(t.getMatBatch()));
 		sql  = sql.replace("#{mat_price}",String.valueOf(t.getMatPrice()));
 		sql  = sql.replace("#{mat_qty}",String.valueOf(t.getMatQty()));
 		sql  = sql.replace("#{mat_qty_wq}",String.valueOf(t.getMatQtyWq()));
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
					javaInter.execute("v_wz_po_wq",data);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("执行JAVA增强出现异常！");
			}
		}
 	}
}
