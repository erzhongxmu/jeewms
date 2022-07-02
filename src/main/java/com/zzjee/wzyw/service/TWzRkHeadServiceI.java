package com.zzjee.wzyw.service;

import com.zzjee.wzyw.entity.TWzRkHeadEntity;
import com.zzjee.wzyw.entity.TWzRkItemEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.util.List;

public interface TWzRkHeadServiceI extends CommonService {

 	@Override
    public <T> void delete(T entity);
	/**
	 * 添加一对多
	 *
	 */
	public void addMain(TWzRkHeadEntity tWzRkHead,
                        List<TWzRkItemEntity> tWzRkItemList) ;
	/**
	 * 修改一对多
	 *
	 */
	public void updateMain(TWzRkHeadEntity tWzRkHead,
                           List<TWzRkItemEntity> tWzRkItemList);
	public void delMain(TWzRkHeadEntity tWzRkHead);

 	/**
	 * 默认按钮-sql增强-新增操作
	 * @param t
	 * @return
	 */
 	public boolean doAddSql(TWzRkHeadEntity t);
 	/**
	 * 默认按钮-sql增强-更新操作
	 * @param t
	 * @return
	 */
 	public boolean doUpdateSql(TWzRkHeadEntity t);
 	/**
	 * 默认按钮-sql增强-删除操作
	 * @param t
	 * @return
	 */
 	public boolean doDelSql(TWzRkHeadEntity t);
}
