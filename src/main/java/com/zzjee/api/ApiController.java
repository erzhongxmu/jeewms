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

    //??????????????????begin
    //????????????
    @RequestMapping(value = "/wvNoticeController/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list1(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "searchstr", required = false) String searchstr,
                                   @RequestParam(value = "searchstr2", required = false) String searchstr2) {
        return wvNoticeController.list(username, searchstr, searchstr2);
    }

    //????????????
    @RequestMapping(value = "/wmInQmIController/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> create1(@RequestParam String wmInQmIstr,
                                     UriComponentsBuilder uriBuilder) {
        return wmInQmIController.create(wmInQmIstr, uriBuilder);
    }
    //??????????????????end

    //??????????????????begin
    //????????????
    @RequestMapping(value = "/wmInQmIController/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list2(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "searchstr", required = false) String searchstr,
                                   @RequestParam(value = "searchstr2", required = false) String searchstr2) {
        return wmInQmIController.list(username, searchstr, searchstr2);
    }

    //????????????
    @RequestMapping(value = "/wmToUpGoodsController/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> create2(@RequestParam String wmToUpGoodsstr, UriComponentsBuilder uriBuilder) {
        return wmInQmIController.save(wmToUpGoodsstr, uriBuilder);
    }
    //??????????????????end

    //??????????????????begin
    //??????????????????
    //????????????  PDA??????
    @RequestMapping(value = "/wvGiNoticeController/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list3(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "searchstr", required = false) String searchstr,
                                   @RequestParam(value = "searchstr2", required = false) String searchstr2,
                                   @RequestParam(value = "searchstr3", required = false) String searchstr3) {
        return wvGiNoticeController.lists(username, searchstr, searchstr2, searchstr3);
    }

    //????????????
    //??????
    @RequestMapping(value = "/wmToDownGoodsController/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public synchronized ResponseEntity<?> create3(@RequestParam String wmToDownGoodsstr,
                                                  UriComponentsBuilder uriBuilder) {
        return wmToDownGoodsController.create(wmToDownGoodsstr, uriBuilder);
    }
    //??????????????????end

    //??????????????????
    @RequestMapping(value = "/wvGiController/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list4(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "searchstr", required = false) String searchstr,
                                   @RequestParam(value = "searchstr2", required = false) String searchstr2) {
        return wvGiController.list(username, searchstr, searchstr2);
    }

    //??????????????????
    @RequestMapping(value = "/wmToDownGoodsController/change", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> update4(@RequestParam String wmToDownGoodsstr,
                                     UriComponentsBuilder uriBuilder) {
        return wmToDownGoodsController.update(wmToDownGoodsstr, uriBuilder);
    }

    // TODO: 2022/6/25 ????????????
    //??????????????????
    @RequestMapping(value = "/waveToDownController/list/todown", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list5(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "searchstr", required = false) String searchstr,
                                   @RequestParam(value = "searchstr2", required = false) String searchstr2,
                                   @RequestParam(value = "searchstr3", required = false) String searchstr3) {
        return waveToDownController.list(username, searchstr, searchstr2, searchstr3);
    }

    //??????????????????
    @RequestMapping(value = "/waveToDownController/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> create5(@RequestParam String waveToDownstr, UriComponentsBuilder uriBuilder) {
//        waveToDown.getWaveId(),waveToDown.getGoodsId(),waveToDown.getProData(),waveToDown.getTinId(),waveToDown.getBinId()
//         ??????????????????????????????????????????????????????????????? ?????????
        return waveToDownController.create(waveToDownstr, uriBuilder);
    }

    //??????????????????

    @RequestMapping(value = "/waveToFjController/list/tofj", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list6(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "searchstr", required = false) String searchstr,
                                   @RequestParam(value = "searchstr2", required = false) String searchstr2,
                                   @RequestParam(value = "searchstr3", required = false) String searchstr3,
                                   @RequestParam(value = "searchstr4", required = false) String searchstr4,//????????????
                                   @RequestParam(value = "searchstr5", required = false) String searchstr5) {
        return waveToFjController.list(username, searchstr, searchstr2, searchstr3, searchstr4, searchstr5);
    }

    //??????????????????
    @RequestMapping(value = "/waveToFjController/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> create6(@RequestParam String waveToFjstr, UriComponentsBuilder uriBuilder) {
        //?????????ID??????????????????
        return waveToFjController.create(waveToFjstr, uriBuilder);
    }

    //????????????
    @RequestMapping(value = "/wvStockController/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list7(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "searchstr", required = false) String searchstr,
                                   @RequestParam(value = "searchstr2", required = false) String searchstr2) {
        return wvStockController.list(username, searchstr, searchstr2);
    }

    //????????????
    public static final String ToMoveGoodsController = "/rest/wmToMoveGoodsController";

    @RequestMapping(value = "/wmToMoveGoodsController/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list8(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "searchstr", required = false) String searchstr,
                                   @RequestParam(value = "searchstr2", required = false) String searchstr2) {
        return wmToMoveGoodsController.list(username, searchstr, searchstr2);
    }
    //????????????

    @RequestMapping(value = "/wmToMoveGoodsController/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> update8(@RequestParam String wmToMoveGoodsstr,
                                     UriComponentsBuilder uriBuilder) {
        return wmToMoveGoodsController.update(wmToMoveGoodsstr, uriBuilder);
    }

    //????????????
    //PDA??????
    @RequestMapping(value = "/wmSttInGoodsController/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> list9(@RequestParam(value = "username", required = false) String username,
                                   @RequestParam(value = "searchstr", required = false) String searchstr,
                                   @RequestParam(value = "searchstr2", required = false) String searchstr2) {
        return wmSttInGoodsController.list(username, searchstr, searchstr2);
    }

    //????????????
    @RequestMapping(value = "/wmSttInGoodsController/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> update9(@RequestParam String wmSttInGoodsstr,
                                     UriComponentsBuilder uriBuilder) {
        return wmSttInGoodsController.update(wmSttInGoodsstr, uriBuilder);
    }

    //????????????
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

    //??????????????????
    public static final String mdGoodsControllerc = "/rest/mdGoodsController/change";

    @RequestMapping(value = "/mdGoodsController/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> update10(@RequestParam String mdGoodsstr,
                                      UriComponentsBuilder uriBuilder) {
        return mdGoodsController.update(mdGoodsstr, uriBuilder);
    }

    //????????????
    @RequestMapping(value = "/mdGoodsController/order", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> xiadan(@RequestParam String mdGoodsstr,
                                    UriComponentsBuilder uriBuilder) {
        return mdGoodsController.xiadan(mdGoodsstr, uriBuilder);
    }

    @RequestMapping(value = "/rfidsave/{username}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ApiOperation(value = "RFID??????", produces = "application/json", httpMethod = "POST")
    public ResponseMessage<?> rfid_save(@PathVariable("username") String username,
                                        @RequestBody wmientity wmientityin,
                                        HttpServletRequest request) {

        return wmomController.rfid_save(username, wmientityin, request);
    }
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResultApi<?> login(@RequestBody TSBaseUser tsBaseUser, HttpServletRequest request) {
        logger.info("??????TOKEN[{}]" + tsBaseUser.getUserName());
        ResultDO D0 = new  ResultDO();
        // ??????
        if (org.apache.commons.lang3.StringUtils.isEmpty(tsBaseUser.getUserName())) {
            return ResultApi.error("????????????????????????!");
        }
        // ??????
        if (StringUtils.isEmpty(tsBaseUser.getUserName())) {
            return ResultApi.error("????????????????????????!");
        }
        TSUser user = userService.checkUserExits(tsBaseUser.getUserName(), tsBaseUser.getPassword());
        if (user == null) {
            D0.setErrorMsg("????????????????????????!");
            D0.setOK(false);
            return ResultApi.error("??????TOKEN,??????????????????[{}]!");
         }
        return ResultApi.OK(user);
    }
}
