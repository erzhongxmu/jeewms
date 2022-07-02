package com.zzjee.wzyw.service.impl;

import com.zzjee.wzyw.entity.TWzPoHeadEntity;
import com.zzjee.wzyw.entity.TWzPoItemEntity;
import com.zzjee.wzyw.service.TWzPoHeadServiceI;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service("tWzPoHeadService")
@Transactional
public class TWzPoHeadServiceImpl extends CommonServiceImpl implements TWzPoHeadServiceI {

 	@Override
    public <T> void delete(T entity) {
 		super.delete(entity);
 		//执行删除操作配置的sql增强
		this.doDelSql((TWzPoHeadEntity)entity);
 	}

	@Override
    public void addMain(TWzPoHeadEntity tWzPoHead,
                        List<TWzPoItemEntity> tWzPoItemList){
			//保存主信息
			this.save(tWzPoHead);

			/**保存-采购订单行项目*/
			for(TWzPoItemEntity tWzPoItem:tWzPoItemList){
				//外键设置
				tWzPoItem.setPoId(tWzPoHead.getId().toString());
				this.save(tWzPoItem);
			}
			//执行新增操作配置的sql增强
 			this.doAddSql(tWzPoHead);
	}


	@Override
    public void updateMain(TWzPoHeadEntity tWzPoHead,
                           List<TWzPoItemEntity> tWzPoItemList) {
		//保存主表信息
		if(StringUtil.isNotEmpty(tWzPoHead.getId())){
			try {
				TWzPoHeadEntity temp = findUniqueByProperty(TWzPoHeadEntity.class, "id", tWzPoHead.getId());
				MyBeanUtils.copyBeanNotNull2Bean(tWzPoHead, temp);
				this.saveOrUpdate(temp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			this.saveOrUpdate(tWzPoHead);
		}
		//===================================================================================
		//获取参数
		Object id0 = tWzPoHead.getId();
		//===================================================================================
		//1.查询出数据库的明细数据-采购订单行项目
	    String hql0 = "from TWzPoItemEntity where 1 = 1 AND pO_ID = ? ";
	    List<TWzPoItemEntity> tWzPoItemOldList = this.findHql(hql0,id0);
		//2.筛选更新明细数据-采购订单行项目
		if(tWzPoItemList!=null&&tWzPoItemList.size()>0){
		for(TWzPoItemEntity oldE:tWzPoItemOldList){
			boolean isUpdate = false;
				for(TWzPoItemEntity sendE:tWzPoItemList){
					//需要更新的明细数据-采购订单行项目
					if(oldE.getId().equals(sendE.getId())){
		    			try {
							MyBeanUtils.copyBeanNotNull2Bean(sendE,oldE);
							this.saveOrUpdate(oldE);
						} catch (Exception e) {
							e.printStackTrace();
							throw new BusinessException(e.getMessage());
						}
						isUpdate= true;
		    			break;
		    		}
		    	}
	    		if(!isUpdate){
		    		//如果数据库存在的明细，前台没有传递过来则是删除-采购订单行项目
		    		super.delete(oldE);
	    		}

			}
			//3.持久化新增的数据-采购订单行项目
			for(TWzPoItemEntity tWzPoItem:tWzPoItemList){
				if(oConvertUtils.isEmpty(tWzPoItem.getId())){
					//外键设置
					tWzPoItem.setPoId(tWzPoHead.getId().toString());
					this.save(tWzPoItem);
				}
			}
		}
		//执行更新操作配置的sql增强
 		this.doUpdateSql(tWzPoHead);
	}


	@Override
    public void delMain(TWzPoHeadEntity tWzPoHead) {
		//删除主表信息
		this.delete(tWzPoHead);
		//===================================================================================
		//获取参数
		Object id0 = tWzPoHead.getId();
		//===================================================================================
		//删除-采购订单行项目
	    String hql0 = "from TWzPoItemEntity where 1 = 1 AND pO_ID = ? ";
	    List<TWzPoItemEntity> tWzPoItemOldList = this.findHql(hql0,id0);
		this.deleteAllEntitie(tWzPoItemOldList);
	}


 	/**
	 * 默认按钮-sql增强-新增操作
	 * @param t
	 * @return
	 */
 	@Override
    public boolean doAddSql(TWzPoHeadEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @param t
	 * @return
	 */
 	@Override
    public boolean doUpdateSql(TWzPoHeadEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @param t
	 * @return
	 */
 	@Override
    public boolean doDelSql(TWzPoHeadEntity t){
	 	return true;
 	}

 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @return
	 */
 	public String replaceVal(String sql,TWzPoHeadEntity t){
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
 		sql  = sql.replace("#{vendor_code}",String.valueOf(t.getVendorCode()));
 		sql  = sql.replace("#{vendor_name}",String.valueOf(t.getVendorName()));
 		sql  = sql.replace("#{doc_date}",String.valueOf(t.getDocDate()));
 		sql  = sql.replace("#{po_remark}",String.valueOf(t.getPoRemark()));
 		sql  = sql.replace("#{po_by1}",String.valueOf(t.getPoBy1()));
 		sql  = sql.replace("#{po_by2}",String.valueOf(t.getPoBy2()));
 		sql  = sql.replace("#{po_by3}",String.valueOf(t.getPoBy3()));
 		sql  = sql.replace("#{po_by4}",String.valueOf(t.getPoBy4()));
 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
 		return sql;
 	}
}
