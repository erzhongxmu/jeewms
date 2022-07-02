package com.zzjee.wzyw.service;

import com.zzjee.wzyw.entity.TWzPoHeadEntity;
import com.zzjee.wzyw.entity.TWzPoItemEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.util.List;

public interface TWzPoHeadServiceI extends CommonService {

 	@Override
    public <T> void delete(T entity);
	/**
	 * 添加一对多
	 *
	 */
	public void addMain(TWzPoHeadEntity tWzPoHead,
                        List<TWzPoItemEntity> tWzPoItemList) ;
	/**
	 * 修改一对多
	 *
	 */
	public void updateMain(TWzPoHeadEntity tWzPoHead,
                           List<TWzPoItemEntity> tWzPoItemList);
	public void delMain(TWzPoHeadEntity tWzPoHead);

 	/**
	 * 默认按钮-sql增强-新增操作
	 * @param t
	 * @return
	 */
 	public boolean doAddSql(TWzPoHeadEntity t);
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @param t
	 * @return
	 */
 	public boolean doUpdateSql(TWzPoHeadEntity t);
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @param t
	 * @return
	 */
 	public boolean doDelSql(TWzPoHeadEntity t);
}
