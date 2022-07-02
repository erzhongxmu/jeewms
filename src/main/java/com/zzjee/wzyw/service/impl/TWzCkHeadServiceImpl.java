package com.zzjee.wzyw.service.impl;

import com.zzjee.wzyw.entity.TWzCkHeadEntity;
import com.zzjee.wzyw.entity.TWzCkItemEntity;
import com.zzjee.wzyw.service.TWzCkHeadServiceI;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service("tWzCkHeadService")
@Transactional
public class TWzCkHeadServiceImpl extends CommonServiceImpl implements TWzCkHeadServiceI {

 	@Override
    public <T> void delete(T entity) {
 		super.delete(entity);
 		//执行删除操作配置的sql增强
		this.doDelSql((TWzCkHeadEntity)entity);
 	}

	@Override
    public void addMain(TWzCkHeadEntity tWzCkHead,
                        List<TWzCkItemEntity> tWzCkItemList){
			//保存主信息
//		    tWzCkHead.setBpmStatus("0");
			this.save(tWzCkHead);

			/**保存-出库商品*/
			for(TWzCkItemEntity tWzCkItem:tWzCkItemList){
				//外键设置
				tWzCkItem.setWzckHid(tWzCkHead.getId().toString());
//				tWzCkItem.setBpmStatus("0");
				this.save(tWzCkItem);
			}
			//执行新增操作配置的sql增强
 			this.doAddSql(tWzCkHead);
	}


	@Override
    public void updateMain(TWzCkHeadEntity tWzCkHead,
                           List<TWzCkItemEntity> tWzCkItemList) {
		//保存主表信息
		if(StringUtil.isNotEmpty(tWzCkHead.getId())){
			try {
				TWzCkHeadEntity temp = findUniqueByProperty(TWzCkHeadEntity.class, "id", tWzCkHead.getId());
				MyBeanUtils.copyBeanNotNull2Bean(tWzCkHead, temp);
				this.saveOrUpdate(temp);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			this.saveOrUpdate(tWzCkHead);
		}
		//===================================================================================
		//获取参数
		Object id0 = tWzCkHead.getId();
		//===================================================================================
		//1.查询出数据库的明细数据-出库商品
	    String hql0 = "from TWzCkItemEntity where 1 = 1 AND wZCK_HID = ? ";
	    List<TWzCkItemEntity> tWzCkItemOldList = this.findHql(hql0,id0);
		//2.筛选更新明细数据-出库商品
		if(tWzCkItemList!=null&&tWzCkItemList.size()>0){
		for(TWzCkItemEntity oldE:tWzCkItemOldList){
			boolean isUpdate = false;
				for(TWzCkItemEntity sendE:tWzCkItemList){
					//需要更新的明细数据-出库商品
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
		    		//如果数据库存在的明细，前台没有传递过来则是删除-出库商品
		    		super.delete(oldE);
	    		}

			}
			//3.持久化新增的数据-出库商品
			for(TWzCkItemEntity tWzCkItem:tWzCkItemList){
				if(oConvertUtils.isEmpty(tWzCkItem.getId())){
					//外键设置
					tWzCkItem.setWzckHid(tWzCkHead.getId().toString());
					this.save(tWzCkItem);
				}
			}
		}
		//执行更新操作配置的sql增强
 		this.doUpdateSql(tWzCkHead);
	}


	@Override
    public void delMain(TWzCkHeadEntity tWzCkHead) {
		//删除主表信息
		this.delete(tWzCkHead);
		//===================================================================================
		//获取参数
		Object id0 = tWzCkHead.getId();
		//===================================================================================
		//删除-出库商品
	    String hql0 = "from TWzCkItemEntity where 1 = 1 AND wZCK_HID = ? ";
	    List<TWzCkItemEntity> tWzCkItemOldList = this.findHql(hql0,id0);
		this.deleteAllEntitie(tWzCkItemOldList);
	}


 	/**
	 * 默认按钮-sql增强-新增操作
	 * @param t
	 * @return
	 */
 	@Override
    public boolean doAddSql(TWzCkHeadEntity t){

		String tsql = "call p_wz_shenpi("+"'"+t.getId()+")";
		try {
			this.executeSql(tsql);
		} catch (Exception e) {
			// TODO: handle exception
		}
//		wzUtil.saveentity(t.getId().toString());//审批控制
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @param t
	 * @return
	 */
 	@Override
    public boolean doUpdateSql(TWzCkHeadEntity t){
	 	return true;
 	}
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @param t
	 * @return
	 */
 	@Override
    public boolean doDelSql(TWzCkHeadEntity t){
	 	return true;
 	}

 	/**
	 * 替换sql中的变量
	 * @param sql
	 * @return
	 */
 	public String replaceVal(String sql,TWzCkHeadEntity t){
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
 		sql  = sql.replace("#{org_code}",String.valueOf(t.getOrgCode()));
 		sql  = sql.replace("#{org_name}",String.valueOf(t.getOrgName()));
 		sql  = sql.replace("#{ck_username}",String.valueOf(t.getCkUsername()));
 		sql  = sql.replace("#{ck_name}",String.valueOf(t.getCkName()));
 		sql  = sql.replace("#{doc_date}",String.valueOf(t.getDocDate()));
 		sql  = sql.replace("#{ck_value}",String.valueOf(t.getCkValue()));
 		sql  = sql.replace("#{ck_remark}",String.valueOf(t.getCkRemark()));
 		sql  = sql.replace("#{ck_sgsp}",String.valueOf(t.getCkSgsp()));
 		sql  = sql.replace("#{ck_fujian}",String.valueOf(t.getCkFujian()));
 		sql  = sql.replace("#{by1}",String.valueOf(t.getBy1()));
 		sql  = sql.replace("#{by2}",String.valueOf(t.getBy2()));
 		sql  = sql.replace("#{by3}",String.valueOf(t.getBy3()));
 		sql  = sql.replace("#{by4}",String.valueOf(t.getBy4()));
 		sql  = sql.replace("#{by5}",String.valueOf(t.getBy5()));
 		sql  = sql.replace("#{UUID}",UUID.randomUUID().toString());
 		return sql;
 	}
}
