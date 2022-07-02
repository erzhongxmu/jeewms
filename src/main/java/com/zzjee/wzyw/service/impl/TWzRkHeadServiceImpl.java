package com.zzjee.wzyw.service.impl;

import com.zzjee.wzyw.entity.TWzRkHeadEntity;
import com.zzjee.wzyw.entity.TWzRkItemEntity;
import com.zzjee.wzyw.service.TWzRkHeadServiceI;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service("tWzRkHeadService")
@Transactional
public class TWzRkHeadServiceImpl extends CommonServiceImpl implements TWzRkHeadServiceI {

 	@Override
    public <T> void delete(T entity) {
 		super.delete(entity);
 		//执行删除操作配置的sql增强
		this.doDelSql((TWzRkHeadEntity)entity);
 	}

	@Override
    public void addMain(TWzRkHeadEntity tWzRkHead,
                        List<TWzRkItemEntity> tWzRkItemList){
			//保存主信息
		    tWzRkHead.setBpmStatus("0");
			this.save(tWzRkHead);

			/**保存-入库商品*/
			for(TWzRkItemEntity tWzRkItem:tWzRkItemList){
				//外键设置
				tWzRkItem.setWzrkHid(tWzRkHead.getId().toString());
				tWzRkItem.setBpmStatus("0");
				this.save(tWzRkItem);
			}
			//执行新增操作配置的sql增强
 			this.doAddSql(tWzRkHead);
	}


	@Override
    public void updateMain(TWzRkHeadEntity tWzRkHead,
                           List<TWzRkItemEntity> tWzRkItemList) {
		//保存主表信息
		if(StringUtil.isNotEmpty(tWzRkHead.getId())){
			try {
				TWzRkHeadEntity temp = findUniqueByProperty(TWzRkHeadEntity.class, "id", tWzRkHead.getId());
				MyBeanUtils.copyBeanNotNull2Bean(tWzRkHead, temp);
				this.saveOrUpdate(temp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			this.saveOrUpdate(tWzRkHead);
		}
		//===================================================================================
		//获取参数
		Object id0 = tWzRkHead.getId();
		//===================================================================================
		//1.查询出数据库的明细数据-入库商品
	    String hql0 = "from TWzRkItemEntity where 1 = 1 AND wZRK_HID = ? ";
	    List<TWzRkItemEntity> tWzRkItemOldList = this.findHql(hql0,id0);
		//2.筛选更新明细数据-入库商品
		if(tWzRkItemList!=null&&tWzRkItemList.size()>0){
		for(TWzRkItemEntity oldE:tWzRkItemOldList){
			boolean isUpdate = false;
				for(TWzRkItemEntity sendE:tWzRkItemList){
					//需要更新的明细数据-入库商品
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
		    		//如果数据库存在的明细，前台没有传递过来则是删除-入库商品
		    		super.delete(oldE);
	    		}

			}
			//3.持久化新增的数据-入库商品
			for(TWzRkItemEntity tWzRkItem:tWzRkItemList){
				if(oConvertUtils.isEmpty(tWzRkItem.getId())){
					//外键设置
					tWzRkItem.setWzrkHid(tWzRkHead.getId().toString());
					this.save(tWzRkItem);
				}
			}
		}
		//执行更新操作配置的sql增强
 		this.doUpdateSql(tWzRkHead);
	}


	@Override
    public void delMain(TWzRkHeadEntity tWzRkHead) {
		//删除主表信息
		this.delete(tWzRkHead);
		//===================================================================================
		//获取参数
		Object id0 = tWzRkHead.getId();
		//===================================================================================
		//删除-入库商品
	    String hql0 = "from TWzRkItemEntity where 1 = 1 AND wZRK_HID = ? ";
	    List<TWzRkItemEntity> tWzRkItemOldList = this.findHql(hql0,id0);
		this.deleteAllEntitie(tWzRkItemOldList);
	}


 	/**
	 * 默认按钮-sql增强-新增操作
	 * @param t
	 * @return
	 */
 	@Override
    public boolean doAddSql(TWzRkHeadEntity t){

	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @param t
	 * @return
	 */
 	@Override
    public boolean doUpdateSql(TWzRkHeadEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @param t
	 * @return
	 */
 	@Override
    public boolean doDelSql(TWzRkHeadEntity t){
	 	return true;
 	}

 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @return
	 */
 	public String replaceVal(String sql, TWzRkHeadEntity t){
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
 		sql  = sql.replace("#{rk_remark}",String.valueOf(t.getRkRemark()));
 		sql  = sql.replace("#{rk_fujian}",String.valueOf(t.getRkFujian()));
 		sql  = sql.replace("#{by1}",String.valueOf(t.getBy1()));
 		sql  = sql.replace("#{by2}",String.valueOf(t.getBy2()));
 		sql  = sql.replace("#{by3}",String.valueOf(t.getBy3()));
 		sql  = sql.replace("#{by4}",String.valueOf(t.getBy4()));
 		sql  = sql.replace("#{by5}",String.valueOf(t.getBy5()));
 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
 		return sql;
 	}
}
