package com.zzjee.wz.service;
import com.zzjee.wz.entity.TWzMaterialEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface TWzMaterialServiceI extends CommonService {

 	public void delete(TWzMaterialEntity entity) throws Exception;

 	public Serializable save(TWzMaterialEntity entity) throws Exception;

 	public void saveOrUpdate(TWzMaterialEntity entity) throws Exception;

}
