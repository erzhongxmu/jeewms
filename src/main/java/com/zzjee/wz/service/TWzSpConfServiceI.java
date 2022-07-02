package com.zzjee.wz.service;
import com.zzjee.wz.entity.TWzSpConfEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface TWzSpConfServiceI extends CommonService {

 	public void delete(TWzSpConfEntity entity) throws Exception;

 	public Serializable save(TWzSpConfEntity entity) throws Exception;

 	public void saveOrUpdate(TWzSpConfEntity entity) throws Exception;

}
