package com.zzjee.api;

import com.zzjee.md.controller.MdGoodsController;
import com.zzjee.md.entity.MdCusEntity;
import com.zzjee.md.entity.MdGoodsEntity;
import com.zzjee.md.entity.MvGoodsEntity;
import com.zzjee.rfid.entity.RfidBuseEntity;
import com.zzjee.wave.controller.WaveToDownController;
import com.zzjee.wave.controller.WaveToFjController;
import com.zzjee.wm.controller.*;
import com.zzjee.wm.entity.WmImNoticeHEntity;
import com.zzjee.wm.entity.WmImNoticeIEntity;
import com.zzjee.wm.entity.WmInQmIEntity;
import com.zzjee.wmapi.controller.WvGiController;
import com.zzjee.wmapi.controller.WvGiNoticeController;
import com.zzjee.wmapi.controller.WvNoticeController;
import com.zzjee.wmapi.entity.WvNoticeEntity;
import com.zzjee.wmutil.wmUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.HttpMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.JSONHelper;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.jwt.util.ResponseMessage;
import org.jeecgframework.jwt.util.Result;
import org.jeecgframework.web.system.pojo.base.TSBaseUser;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Package com.zzjee.api
 * @date 2021/7/22 10:47
 * @description
 */
@RestController
@RequestMapping("/pdaapi")
public class ApiController {
    @Autowired
    private UserService userService;
    @Autowired
    private WvNoticeController wvNoticeController;
    @Autowired
    private WmInQmIController wmInQmIController;
    @Autowired
    private WmToUpGoodsController wmToUpGoodsController;
    @Autowired
    private WvGiNoticeController wvGiNoticeController;
    @Autowired
    private WmToDownGoodsController wmToDownGoodsController;
    @Autowired
    private WvGiController wvGiController;
    @Autowired
    private WaveToDownController waveToDownController;
    @Autowired
    private WaveToFjController waveToFjController;
    @Autowired
    private WvStockController wvStockController;
    @Autowired
    private WmToMoveGoodsController wmToMoveGoodsController;
    @Autowired
    private WmSttInGoodsController wmSttInGoodsController;
    @Autowired
    private MdGoodsController mdGoodsController;
    @Autowired
    private wmomController wmomController;
    private static final Logger logger = Logger.getLogger(ApiController.class);

    //收货相关接口begin
    //收货列表
    @RequestMapping(value = "/wvNoticeController/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list1(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "searchstr", required = false) String searchstr,
                                   @RequestParam(value = "searchstr2", required = false) String searchstr2) {
        return wvNoticeController.list(username, searchstr, searchstr2);
    }

    //收货保存
    @RequestMapping(value = "/wmInQmIController/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> create1(@RequestParam String wmInQmIstr,
                                     UriComponentsBuilder uriBuilder) {
        return wmInQmIController.create(wmInQmIstr, uriBuilder);
    }
    //收货相关接口end

    //上架相关接口begin
    //上架列表
    @RequestMapping(value = "/wmInQmIController/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list2(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "searchstr", required = false) String searchstr,
                                   @RequestParam(value = "searchstr2", required = false) String searchstr2) {
        return wmInQmIController.list(username, searchstr, searchstr2);
    }

    //保存上架
    @RequestMapping(value = "/wmToUpGoodsController/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> create2(@RequestParam String wmToUpGoodsstr, UriComponentsBuilder uriBuilder) {
        return wmInQmIController.save(wmToUpGoodsstr, uriBuilder);
    }
    //上架相关接口end

    //下架相关接口begin
    //按单拣货列表
    //下架任务  PDA接口
    @RequestMapping(value = "/wvGiNoticeController/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list3(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "searchstr", required = false) String searchstr,
                                   @RequestParam(value = "searchstr2", required = false) String searchstr2,
                                   @RequestParam(value = "searchstr3", required = false) String searchstr3) {
        return wvGiNoticeController.lists(username, searchstr, searchstr2, searchstr3);
    }

    //保存下架
    //下架
    @RequestMapping(value = "/wmToDownGoodsController/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public synchronized ResponseEntity<?> create3(@RequestParam String wmToDownGoodsstr,
                                                  UriComponentsBuilder uriBuilder) {
        return wmToDownGoodsController.create(wmToDownGoodsstr, uriBuilder);
    }
    //下架相关接口end

    //装车复核列表
    @RequestMapping(value = "/wvGiController/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list4(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "searchstr", required = false) String searchstr,
                                   @RequestParam(value = "searchstr2", required = false) String searchstr2) {
        return wvGiController.list(username, searchstr, searchstr2);
    }

    //装车复核保存
    @RequestMapping(value = "/wmToDownGoodsController/change", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> update4(@RequestParam String wmToDownGoodsstr,
                                     UriComponentsBuilder uriBuilder) {
        return wmToDownGoodsController.update(wmToDownGoodsstr, uriBuilder);
    }

    // TODO: 2022/6/25 接口文档
    //波次下架列表
    @RequestMapping(value = "/waveToDownController/list/todown", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list5(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "searchstr", required = false) String searchstr,
                                   @RequestParam(value = "searchstr2", required = false) String searchstr2,
                                   @RequestParam(value = "searchstr3", required = false) String searchstr3) {
        return waveToDownController.list(username, searchstr, searchstr2, searchstr3);
    }

    //波次下架保存
    @RequestMapping(value = "/waveToDownController/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> create5(@RequestParam String waveToDownstr, UriComponentsBuilder uriBuilder) {
//        waveToDown.getWaveId(),waveToDown.getGoodsId(),waveToDown.getProData(),waveToDown.getTinId(),waveToDown.getBinId()
//         需要传入波次号，商品编码，生产日期，托盘号 储位号
        return waveToDownController.create(waveToDownstr, uriBuilder);
    }

    //波次分拣列表

    @RequestMapping(value = "/waveToFjController/list/tofj", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list6(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "searchstr", required = false) String searchstr,
                                   @RequestParam(value = "searchstr2", required = false) String searchstr2,
                                   @RequestParam(value = "searchstr3", required = false) String searchstr3,
                                   @RequestParam(value = "searchstr4", required = false) String searchstr4,//二次容器
                                   @RequestParam(value = "searchstr5", required = false) String searchstr5) {
        return waveToFjController.list(username, searchstr, searchstr2, searchstr3, searchstr4, searchstr5);
    }

    //波次分拣保存
    @RequestMapping(value = "/waveToFjController/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> create6(@RequestParam String waveToFjstr, UriComponentsBuilder uriBuilder) {
        //需传入ID和二次容器号
        return waveToFjController.create(waveToFjstr, uriBuilder);
    }

    //库存列表
    @RequestMapping(value = "/wvStockController/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list7(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "searchstr", required = false) String searchstr,
                                   @RequestParam(value = "searchstr2", required = false) String searchstr2) {
        return wvStockController.list(username, searchstr, searchstr2);
    }

    //移储列表
    public static final String ToMoveGoodsController = "/rest/wmToMoveGoodsController";

    @RequestMapping(value = "/wmToMoveGoodsController/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list8(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "searchstr", required = false) String searchstr,
                                   @RequestParam(value = "searchstr2", required = false) String searchstr2) {
        return wmToMoveGoodsController.list(username, searchstr, searchstr2);
    }
    //移储保存

    @RequestMapping(value = "/wmToMoveGoodsController/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> update8(@RequestParam String wmToMoveGoodsstr,
                                     UriComponentsBuilder uriBuilder) {
        return wmToMoveGoodsController.update(wmToMoveGoodsstr, uriBuilder);
    }

    //盘点列表
    //PDA接口
    @RequestMapping(value = "/wmSttInGoodsController/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list9(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "searchstr", required = false) String searchstr,
                                   @RequestParam(value = "searchstr2", required = false) String searchstr2) {
        return wmSttInGoodsController.list(username, searchstr, searchstr2);
    }

    //盘点保存
    @RequestMapping(value = "/wmSttInGoodsController/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> update9(@RequestParam String wmSttInGoodsstr,
                                     UriComponentsBuilder uriBuilder) {
        return wmSttInGoodsController.update(wmSttInGoodsstr, uriBuilder);
    }

    //商品列表
    public static final String GoodsController = "/rest/mdGoodsController";

    @RequestMapping(value = "/mdGoodsController/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list10(@RequestParam(value = "username", required = false) String username,
                                    @RequestParam(value = "all", required = false) String all,
                                    @RequestParam(value = "searchstr", required = false) String searchstr,
                                    @RequestParam(value = "searchstr2", required = false) String searchstr2,
                                    @RequestParam(value = "searchstrin1", required = false) String searchstrin1,
                                    @RequestParam(value = "searchstrin2", required = false) String searchstrin2,
                                    @RequestParam(value = "searchstrin3", required = false) String searchstrin3) {
        return mdGoodsController.list(username, all, searchstr, searchstr2, searchstrin1, searchstrin2, searchstrin3);
    }

    //商品信息保存
    public static final String mdGoodsControllerc = "/rest/mdGoodsController/change";

    @RequestMapping(value = "/mdGoodsController/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> update10(@RequestParam String mdGoodsstr,
                                      UriComponentsBuilder uriBuilder) {
        return mdGoodsController.update(mdGoodsstr, uriBuilder);
    }

    //商品下单
    @RequestMapping(value = "/mdGoodsController/order", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> xiadan(@RequestParam String mdGoodsstr,
                                    UriComponentsBuilder uriBuilder) {
        return mdGoodsController.xiadan(mdGoodsstr, uriBuilder);
    }

    @RequestMapping(value = "/rfidsave/{username}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "RFID保存", produces = "application/json", httpMethod = "POST")
    public ResponseMessage<?> rfid_save(@PathVariable("username") String username,
                                        @RequestBody wmientity wmientityin,
                                        HttpServletRequest request) {

        return wmomController.rfid_save(username, wmientityin, request);
    }
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResultApi<?> login(@RequestBody TSBaseUser tsBaseUser, HttpServletRequest request) {
        logger.info("获取TOKEN[{}]" + tsBaseUser.getUserName());
        ResultDO D0 = new  ResultDO();
        // 验证
        if (org.apache.commons.lang3.StringUtils.isEmpty(tsBaseUser.getUserName())) {
            return ResultApi.error("用户账号不能为空!");
        }
        // 验证
        if (StringUtils.isEmpty(tsBaseUser.getUserName())) {
            return ResultApi.error("用户密码不能为空!");
        }
        TSUser user = userService.checkUserExits(tsBaseUser.getUserName(), tsBaseUser.getPassword());
        if (user == null) {
            D0.setErrorMsg("用户账号密码错误!");
            D0.setOK(false);
            return ResultApi.error("获取TOKEN,账号密码错误[{}]!");
         }
        return ResultApi.OK(user);
    }
}
