package com.zzjee.wzpo.service;
import com.zzjee.wzpo.entity.VWzPoWqEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface VWzPoWqServiceI extends CommonService {

 	public void delete(VWzPoWqEntity entity) throws Exception;

 	public Serializable save(VWzPoWqEntity entity) throws Exception;

 	public void saveOrUpdate(VWzPoWqEntity entity) throws Exception;

}
