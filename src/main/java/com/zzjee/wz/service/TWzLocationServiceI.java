package com.zzjee.wz.service;
import com.zzjee.wz.entity.TWzLocationEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface TWzLocationServiceI extends CommonService {

 	public void delete(TWzLocationEntity entity) throws Exception;

 	public Serializable save(TWzLocationEntity entity) throws Exception;

 	public void saveOrUpdate(TWzLocationEntity entity) throws Exception;

}
