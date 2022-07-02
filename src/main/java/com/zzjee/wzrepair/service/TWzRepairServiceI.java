package com.zzjee.wzrepair.service;
import com.zzjee.wzrepair.entity.TWzRepairEntity;
import org.jeecgframework.core.common.service.CommonService;

import java.io.Serializable;

public interface TWzRepairServiceI extends CommonService {

 	public void delete(TWzRepairEntity entity) throws Exception;

 	public Serializable save(TWzRepairEntity entity) throws Exception;

 	public void saveOrUpdate(TWzRepairEntity entity) throws Exception;

}
