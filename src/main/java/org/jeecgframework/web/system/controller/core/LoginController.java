package org.jeecgframework.web.system.controller.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.enums.SysThemesEnum;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.IpUtil;
import org.jeecgframework.core.util.ListtoMenu;
import org.jeecgframework.core.util.LogUtil;
import org.jeecgframework.core.util.NumberComparator;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.SysThemesUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.web.system.manager.ClientManager;
import org.jeecgframework.web.system.pojo.base.Client;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.pojo.base.TSFunction;
import org.jeecgframework.web.system.pojo.base.TSRole;
import org.jeecgframework.web.system.pojo.base.TSRoleFunction;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.MutiLangServiceI;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * ????????????????????????
 * @author admin
 *
 */
//@Scope("prototype")
@Controller
@RequestMapping("/loginController")
public class LoginController extends BaseController{
	private Logger log = Logger.getLogger(LoginController.class);
	private SystemService systemService;
	private UserService userService;

	@Autowired
	private MutiLangServiceI mutiLangService;

	@Autowired
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	@Autowired
	public void setUserService(UserService userService) {

		this.userService = userService;
	}

	@RequestMapping(params = "goPwdInit")
	public String goPwdInit() {
		return "login/pwd_init";
	}


	/**
	 * ??????????????????
	 *
	 * @param user
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "checkuser")
	@ResponseBody
	public AjaxJson checkuser(TSUser user, HttpServletRequest req) {
		HttpSession session = req.getSession();
		AjaxJson j = new AjaxJson();
		//????????????
		if (req.getParameter("langCode")!=null) {
			req.getSession().setAttribute("lang", req.getParameter("langCode"));
		}
		//?????????
		String randCode = req.getParameter("randCode");
		if (StringUtils.isEmpty(randCode)) {
			j.setMsg(mutiLangService.getLang("common.enter.verifycode"));
			j.setSuccess(false);
		} else if (!randCode.equalsIgnoreCase(String.valueOf(session.getAttribute("randCode")))) {
			j.setMsg(mutiLangService.getLang("common.verifycode.error"));
			j.setSuccess(false);
		} else {
			//????????????????????????
			TSUser u = userService.checkUserExits(user);
			if (u == null) {
				j.setMsg(mutiLangService.getLang("common.username.or.password.error"));
				j.setSuccess(false);
				return j;
			}
			if (u != null && u.getStatus() != 0) {
				// ?????????????????????????????????????????????????????????????????????????????????
				Map<String, Object> attrMap = new HashMap<String, Object>();
				j.setAttributes(attrMap);

				String orgId = req.getParameter("orgId");
				if (oConvertUtils.isEmpty(orgId)) {
					// ??????????????????????????????????????????????????????????????????
					Long orgNum = systemService.getCountForJdbc("select count(1) from t_s_user_org where user_id = '" + u.getId() + "'");
					if (orgNum > 1) {
						attrMap.put("orgNum", orgNum);
						attrMap.put("user", u);
					} else {
						Map<String, Object> userOrgMap = systemService.findOneForJdbc("select org_id as orgId from t_s_user_org where user_id=?", u.getId());
						saveLoginSuccessInfo(req, u, (String) userOrgMap.get("orgId"));
					}
				} else {
					attrMap.put("orgNum", 1);
					saveLoginSuccessInfo(req, u, orgId);
				}
			} else {

				j.setMsg(mutiLangService.getLang("common.lock.user"));

				j.setSuccess(false);
			}
		}
		return j;
	}


	/**
	 * ????????????????????????
	 *
	 * @param user
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "changeDefaultOrg")
	@ResponseBody
	public AjaxJson changeDefaultOrg(TSUser user, HttpServletRequest req) {
		AjaxJson j = new AjaxJson();
		Map<String, Object> attrMap = new HashMap<String, Object>();
		String orgId = req.getParameter("orgId");
		TSUser u = userService.checkUserExits(user);
		if (oConvertUtils.isNotEmpty(orgId)) {
			attrMap.put("orgNum", 1);
			saveLoginSuccessInfo(req, u, orgId);
		}
		return j;
	}

    /**
     * ????????????????????????????????????????????????????????????????????????????????????????????????
     * @param req request
     * @param user ??????????????????
     * @param orgId ????????????
     */
    private void saveLoginSuccessInfo(HttpServletRequest req, TSUser user, String orgId) {
    	String message = null;
        TSDepart currentDepart = systemService.get(TSDepart.class, orgId);
        user.setCurrentDepart(currentDepart);

        HttpSession session = ContextHolderUtils.getSession();

        session.setAttribute(ResourceUtil.LOCAL_CLINET_USER, user);
        message = mutiLangService.getLang("common.user") + ": " + user.getUserName() + "["+ currentDepart.getDepartname() + "]" + mutiLangService.getLang("common.login.success");

        //??????session?????? ?????? ??????session???????????????????????????????????????????????????????????????Client??????
        Client clientOld = ClientManager.getInstance().getClient(session.getId());
		if(clientOld == null || clientOld.getUser() ==null ||user.getUserName().equals(clientOld.getUser().getUserName())){
			Client client = new Client();
	        client.setIp(IpUtil.getIpAddr(req));
	        client.setLogindatetime(new Date());
	        client.setUser(user);
	        ClientManager.getInstance().addClinet(session.getId(), client);
		} else {//???????????????????????????session?????????session=req.getSession(true)?????????session
			ClientManager.getInstance().removeClinet(session.getId());
			session.invalidate();
			session = req.getSession(true);//session?????????
			session.setAttribute(ResourceUtil.LOCAL_CLINET_USER, user);
			session.setAttribute("randCode",req.getParameter("randCode"));//???????????????
			checkuser(user,req);
		}



        // ??????????????????
        systemService.addLog(message, Globals.Log_Type_LOGIN, Globals.Log_Leavel_INFO);
    }


    /**
	 * ????????????
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "login")
	public String login(ModelMap modelMap,HttpServletRequest request,HttpServletResponse response) {
		TSUser user = ResourceUtil.getSessionUserName();
		String roles = "";
		if (user != null) {
			List<TSRoleUser> rUsers = systemService.findByProperty(TSRoleUser.class, "TSUser.id", user.getId());
			for (TSRoleUser ru : rUsers) {
				TSRole role = ru.getTSRole();
				roles += role.getRoleName() + ",";
			}
			if (roles.length() > 0) {
				roles = roles.substring(0, roles.length() - 1);
			}

            modelMap.put("roleName", roles.length()>3?roles.substring(0,3)+"...":roles);
            modelMap.put("userName", user.getUserName().length()>5?user.getUserName().substring(0, 5)+"...":user.getUserName());

            modelMap.put("currentOrgName", ClientManager.getInstance().getClient().getUser().getCurrentDepart().getDepartname());


			SysThemesEnum sysTheme = SysThemesUtil.getSysTheme(request);
			if("ace".equals(sysTheme.getStyle())||"diy".equals(sysTheme.getStyle())||"acele".equals(sysTheme.getStyle())||"hplus".equals(sysTheme.getStyle())){
				request.setAttribute("menuMap", getFunctionMap(user));
			}

			Cookie cookie = new Cookie("JEECGINDEXSTYLE", sysTheme.getStyle());
			//??????cookie?????????????????????
			cookie.setMaxAge(3600*24*30);
			response.addCookie(cookie);

			Cookie zIndexCookie = new Cookie("ZINDEXNUMBER", "1990");
			zIndexCookie.setMaxAge(3600*24);//??????
			response.addCookie(zIndexCookie);

			return sysTheme.getIndexPath();
		} else {
			return "login/login";
		}

	}

	/**
	 * ????????????
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "logout")
	public ModelAndView logout(HttpServletRequest request) {
		HttpSession session = ContextHolderUtils.getSession();
		TSUser user = ResourceUtil.getSessionUserName();

		try {
			systemService.addLog("??????" + user!=null?user.getUserName():"" + "?????????",Globals.Log_Type_EXIT, Globals.Log_Leavel_INFO);
		} catch (Exception e) {
			LogUtil.error(e.toString());
		}

		ClientManager.getInstance().removeClinet(session.getId());
		session.invalidate();
		ModelAndView modelAndView = new ModelAndView(new RedirectView("loginController.do?login"));
		return modelAndView;
	}

	/**
	 * ????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "left")
	public ModelAndView left(HttpServletRequest request) {
		TSUser user = ResourceUtil.getSessionUserName();
		HttpSession session = ContextHolderUtils.getSession();
        ModelAndView modelAndView = new ModelAndView();
		// ??????????????????
		if (user.getId() == null) {
			session.removeAttribute(Globals.USER_SESSION);
            modelAndView.setView(new RedirectView("loginController.do?login"));
		}else{
            modelAndView.setViewName("main/left");
            request.setAttribute("menuMap", getFunctionMap(user));
        }
		return modelAndView;
	}

	/**
	 * ???????????????map
	 *
	 * @param user
	 * @return
	 */
	private Map<Integer, List<TSFunction>> getFunctionMap(TSUser user) {
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());
		if (client.getFunctionMap() == null || client.getFunctionMap().size() == 0) {
			Map<Integer, List<TSFunction>> functionMap = new HashMap<Integer, List<TSFunction>>();
			Map<String, TSFunction> loginActionlist = getUserFunction(user);
			if (loginActionlist.size() > 0) {
				Collection<TSFunction> allFunctions = loginActionlist.values();
				for (TSFunction function : allFunctions) {
		            if(function.getFunctionType().intValue()==Globals.Function_TYPE_FROM.intValue()){
						//??????????????????????????? ??????????????????????????????
						continue;
					}
					if (!functionMap.containsKey(function.getFunctionLevel() + 0)) {
						functionMap.put(function.getFunctionLevel() + 0,
								new ArrayList<TSFunction>());
					}
					functionMap.get(function.getFunctionLevel() + 0).add(function);
				}
				// ???????????????
				Collection<List<TSFunction>> c = functionMap.values();
				for (List<TSFunction> list : c) {
					Collections.sort(list, new NumberComparator());
				}
			}
			client.setFunctionMap(functionMap);

			//?????????????????????????????????
			loginActionlist.clear();

			return functionMap;
		}else{
			return client.getFunctionMap();
		}
	}

	/**
	 * ????????????????????????
	 *
	 * @param user
	 * @return
	 */
	private Map<String, TSFunction> getUserFunction(TSUser user) {
		HttpSession session = ContextHolderUtils.getSession();
		Client client = ClientManager.getInstance().getClient(session.getId());

		if (client.getFunctions() == null || client.getFunctions().size() == 0) {

			Map<String, TSFunction> loginActionlist = new HashMap<String, TSFunction>();
	           StringBuilder hqlsb1=new StringBuilder("select distinct f from TSFunction f,TSRoleFunction rf,TSRoleUser ru  ").append("where ru.TSRole.id=rf.TSRole.id and rf.TSFunction.id=f.id and ru.TSUser.id=? ");
	           StringBuilder hqlsb2=new StringBuilder("select distinct c from TSFunction c,TSRoleFunction rf,TSRoleOrg b,TSUserOrg a ")
	           							.append("where a.tsDepart.id=b.tsDepart.id and b.tsRole.id=rf.TSRole.id and rf.TSFunction.id=c.id and a.tsUser.id=?");
	           List<TSFunction> list1 = systemService.findHql(hqlsb1.toString(),user.getId());
	           List<TSFunction> list2 = systemService.findHql(hqlsb2.toString(),user.getId());
	           for(TSFunction function:list1){
		              loginActionlist.put(function.getId(),function);
		       }
	           for(TSFunction function:list2){
		              loginActionlist.put(function.getId(),function);
		       }
            client.setFunctions(loginActionlist);
            list2.clear();
            list1.clear();

		}
		return client.getFunctions();
	}

    /**
     * ?????? ???????????? ?????? ??????????????????
     * @param loginActionlist ???????????????????????????
     * @param role ????????????
     * @deprecated
     */
    private void assembleFunctionsByRole(Map<String, TSFunction> loginActionlist, TSRole role) {
        List<TSRoleFunction> roleFunctionList = systemService.findByProperty(TSRoleFunction.class, "TSRole.id", role.getId());
        for (TSRoleFunction roleFunction : roleFunctionList) {
            TSFunction function = roleFunction.getTSFunction();
            if(function.getFunctionType().intValue()==Globals.Function_TYPE_FROM.intValue()){
				//??????????????????????????? ??????????????????????????????
				continue;
			}
            loginActionlist.put(function.getId(), function);
        }
    }


    /**
	 * ????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "home")
	public ModelAndView home(HttpServletRequest request) {

		SysThemesEnum sysTheme = SysThemesUtil.getSysTheme(request);
		//ACE ACE2 DIY????????????home.jsp?????????????????????js???css??????
		if("ace".equals(sysTheme.getStyle())||"diy".equals(sysTheme.getStyle())||"acele".equals(sysTheme.getStyle())){
			request.setAttribute("show", "1");
		} else {//default???shortcut????????????????????????????????????????????????
			request.setAttribute("show", "0");
		}

		return new ModelAndView("main/home");
	}

	  /**
	 * ACE????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "acehome")
	public ModelAndView acehome(HttpServletRequest request) {

		SysThemesEnum sysTheme = SysThemesUtil.getSysTheme(request);
		//ACE ACE2 DIY????????????home.jsp?????????????????????js???css??????
		if("ace".equals(sysTheme.getStyle())||"diy".equals(sysTheme.getStyle())||"acele".equals(sysTheme.getStyle())){
			request.setAttribute("show", "1");
		} else {//default???shortcut????????????????????????????????????????????????
			request.setAttribute("show", "0");
		}

		return new ModelAndView("main/acehome");
	}
	/**
	 * HPLUS????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "hplushome")
	public ModelAndView hplushome(HttpServletRequest request) {
		SysThemesEnum sysTheme = SysThemesUtil.getSysTheme(request);
		return new ModelAndView("main/hplushome");
	}
	/**
	 * ???????????????????????????
	 *
	 * @return
	 */
	@RequestMapping(params = "noAuth")
	public ModelAndView noAuth(HttpServletRequest request) {
		return new ModelAndView("common/noAuth");
	}
	/**
	 * @Title: top
	 * @Description: bootstrap??????????????????
	 * @param request
	 * @return ModelAndView
	 * @throws
	 */
	@RequestMapping(params = "top")
	public ModelAndView top(HttpServletRequest request) {
		TSUser user = ResourceUtil.getSessionUserName();
		HttpSession session = ContextHolderUtils.getSession();
		// ??????????????????
		if (user.getId() == null) {
			session.removeAttribute(Globals.USER_SESSION);
			return new ModelAndView(
					new RedirectView("loginController.do?login"));
		}
		request.setAttribute("menuMap", getFunctionMap(user));
		return new ModelAndView("main/bootstrap_top");
	}
	/**
	 * @Title: top
	 * @author gaofeng
	 * @Description: shortcut??????????????????
	 * @param request
	 * @return ModelAndView
	 * @throws
	 */
	@RequestMapping(params = "shortcut_top")
	public ModelAndView shortcut_top(HttpServletRequest request) {
		TSUser user = ResourceUtil.getSessionUserName();
		HttpSession session = ContextHolderUtils.getSession();
		// ??????????????????
		if (user.getId() == null) {
			session.removeAttribute(Globals.USER_SESSION);
			return new ModelAndView(
					new RedirectView("loginController.do?login"));
		}
		request.setAttribute("menuMap", getFunctionMap(user));
		return new ModelAndView("main/shortcut_top");
	}

	/**
	 * @Title: top
	 * @author:gaofeng
	 * @Description: shortcut?????????????????????????????????????????????ajax???????????????????????????????????????????????????
	 * @return AjaxJson
	 * @throws
	 */
    @RequestMapping(params = "primaryMenu")
    @ResponseBody
	public String getPrimaryMenu() {
		List<TSFunction> primaryMenu = getFunctionMap(ResourceUtil.getSessionUserName()).get(0);
        String floor = "";

        if (primaryMenu == null) {
            return floor;
        }

        for (TSFunction function : primaryMenu) {
            if(function.getFunctionLevel() == 0) {
            	String lang_key = function.getFunctionName();
            	String lang_context = mutiLangService.getLang(lang_key);
            	lang_context=lang_context.trim();

            	if("????????????".equals(lang_context)){

                	String ss = "<div style='width:67px;position: absolute;top:39px;text-align:center;color:#909090;font-size:13px;'><span style='letter-spacing:-1px;'>"+ lang_context +"</span></div>";
                    floor += " <li style='position: relative;'>"+ss+"<img class='imag1' src='plug-in/login/images/ywsq.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/ywsq-up.png' style='display: none;' /></li> ";
                }else if("????????????".equals(lang_context)){

                	String ss = "<div style='width:67px;position: absolute;top:39px;text-align:center;color:#909090;font-size:13px;'><span style='letter-spacing:-1px;'>"+ lang_context +"</span></div>";
                    floor += " <li style='position: relative;'>"+ss+"<img class='imag1' src='plug-in/login/images/grbg.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/grbg-up.png' style='display: none;' /></li> ";
                }else if("????????????".equals(lang_context)){

                	String ss = "<div style='width:67px;position: absolute;top:39px;text-align:center;color:#909090;font-size:13px;'><span style='letter-spacing:-1px;'>"+ lang_context +"</span></div>";
                    floor += " <li style='position: relative;'>"+ss+"<img class='imag1' src='plug-in/login/images/lcsj.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/lcsj-up.png' style='display: none;' /></li> ";
                }else if("Online ??????".equals(lang_context)){

                    floor += " <li><img class='imag1' src='plug-in/login/images/online.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/online_up.png' style='display: none;' />" + " </li> ";
                }else if("???????????????".equals(lang_context)){

                	String ss = "<div style='width:67px;position: absolute;top:39px;text-align:center;color:#909090;font-size:13px;'><span style='letter-spacing:-1px;'>"+ lang_context +"</span></div>";
                    floor += " <li style='position: relative;'>"+ss+"<img class='imag1' src='plug-in/login/images/zdybd.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/zdybd-up.png' style='display: none;' /></li> ";
                }else if("????????????".equals(lang_context)){

                    floor += " <li><img class='imag1' src='plug-in/login/images/xtjk.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/xtjk_up.png' style='display: none;' />" + " </li> ";
                }else if("????????????".equals(lang_context)){

                    floor += " <li><img class='imag1' src='plug-in/login/images/tjbb.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/tjbb_up.png' style='display: none;' />" + " </li> ";
                }else if("???????????????".equals(lang_context)){
                	String ss = "<div style='width:67px;position: absolute;top:39px;text-align:center;color:#909090;font-size:13px;'><span style='letter-spacing:-1px;'>"+ lang_context +"</span></div>";
                    floor += " <li style='position: relative;'>"+ss+"<img class='imag1' src='plug-in/login/images/msg.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/msg_up.png' style='display: none;' /></li> ";
                }else if("????????????".equals(lang_context)){

                    floor += " <li><img class='imag1' src='plug-in/login/images/xtgl.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/xtgl_up.png' style='display: none;' />" + " </li> ";
                }else if("????????????".equals(lang_context)){

                    floor += " <li><img class='imag1' src='plug-in/login/images/cysl.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/cysl_up.png' style='display: none;' />" + " </li> ";
                }else if(lang_context.contains("????????????")){

                	String s = "<div style='width:67px;position: absolute;top:39px;text-align:center;color:#909090;font-size:13px;'>????????????</div>";
                    floor += " <li style='position: relative;'>"+s+"<img class='imag1' src='plug-in/login/images/msg.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/msg_up.png' style='display: none;' /></li> ";
                }else{
                    //???????????????????????????????????????
                	String s="";
                    if(lang_context.length()>=5 && lang_context.length()<7){
                        s = "<div style='width:67px;position: absolute;top:39px;text-align:center;color:#909090;font-size:13px;'><span style='letter-spacing:-1px;'>"+ lang_context +"</span></div>";
                    }else if(lang_context.length()<5){
                        s = "<div style='width:67px;position: absolute;top:39px;text-align:center;color:#909090;font-size:13px;'>"+ lang_context +"</div>";
                    }else if(lang_context.length()>=7){
                        s = "<div style='width:67px;position: absolute;top:39px;text-align:center;color:#909090;font-size:13px;'><span style='letter-spacing:-1px;'>"+ lang_context.substring(0, 6) +"</span></div>";
                    }
                    floor += " <li style='position: relative;'>"+s+"<img class='imag1' src='plug-in/login/images/default.png' /> "
                            + " <img class='imag2' src='plug-in/login/images/default_up.png' style='display: none;' />"
                            +"</li> ";
                }

            }
        }

		return floor;
	}

	/**
	 * @Title: top
	 * @author:wangkun
	 * @Description: shortcut?????????????????????????????????????????????ajax???????????????????????????????????????????????????
	 * @return AjaxJson
	 * @throws
	 */
	@RequestMapping(params = "primaryMenuDiy")
	@ResponseBody
	public String getPrimaryMenuDiy() {
		//???????????????
		List<TSFunction> primaryMenu = getFunctionMap(ResourceUtil.getSessionUserName()).get(1);
		String floor = "";
		if (primaryMenu == null) {
			return floor;
		}
		String menuString = "user.manage role.manage department.manage menu.manage";
		for (TSFunction function : primaryMenu) {
			if(menuString.contains(function.getFunctionName())){
				if(function.getFunctionLevel() == 1) {

					String lang_key = function.getFunctionName();
					String lang_context = mutiLangService.getLang(lang_key);
					if("??????".equals(lang_key)){
						lang_context = "??????";
						String s = "";
						s = "<div style='width:67px;position: absolute;top:47px;text-align:center;color:#000000;font-size:12px;'>"+ lang_context +"</div>";
						floor += " <li><img class='imag1' src='plug-in/login/images/head_icon1.png' /> "
								+ " <img class='imag2' src='plug-in/login/images/head_icon1.png' style='display: none;' />" + s + " </li> ";
					} else if("Online ??????".equals(lang_context)){

						floor += " <li><img class='imag1' src='plug-in/login/images/online.png' /> "
								+ " <img class='imag2' src='plug-in/login/images/online_up.png' style='display: none;' />" + " </li> ";
					}else if("????????????".equals(lang_context)){

						floor += " <li><img class='imag1' src='plug-in/login/images/guanli.png' /> "
								+ " <img class='imag2' src='plug-in/login/images/guanli_up.png' style='display: none;' />" + " </li> ";
					}else if("????????????".equals(lang_context)){

						floor += " <li><img class='imag1' src='plug-in/login/images/xtgl.png' /> "
								+ " <img class='imag2' src='plug-in/login/images/xtgl_up.png' style='display: none;' />" + " </li> ";
					}else if("????????????".equals(lang_context)){

						floor += " <li><img class='imag1' src='plug-in/login/images/cysl.png' /> "
								+ " <img class='imag2' src='plug-in/login/images/cysl_up.png' style='display: none;' />" + " </li> ";
					}else if("????????????".equals(lang_context)){

						floor += " <li><img class='imag1' src='plug-in/login/images/xtjk.png' /> "
								+ " <img class='imag2' src='plug-in/login/images/xtjk_up.png' style='display: none;' />" + " </li> ";
					}else if(lang_context.contains("????????????")){
						String s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#909090;font-size:12px;'>????????????</div>";
						floor += " <li style='position: relative;'><img class='imag1' src='plug-in/login/images/msg.png' /> "
								+ " <img class='imag2' src='plug-in/login/images/msg_up.png' style='display: none;' />"
								+ s +"</li> ";
					}else{
						//???????????????????????????????????????
						String s = "";
						if(lang_context.length()>=5 && lang_context.length()<7){
							s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#000000;font-size:12px;'><span style='letter-spacing:-1px;'>"+ lang_context +"</span></div>";
						}else if(lang_context.length()<5){
							s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#000000;font-size:12px;'>"+ lang_context +"</div>";
						}else if(lang_context.length()>=7){
							s = "<div style='width:67px;position: absolute;top:40px;text-align:center;color:#000000;font-size:12px;'><span style='letter-spacing:-1px;'>"+ lang_context.substring(0, 6) +"</span></div>";
						}
						floor += " <li style='position: relative;'><img class='imag1' src='plug-in/login/images/head_icon2.png' /> "
								+ " <img class='imag2' src='plug-in/login/images/default_up.png' style='display: none;' />"
								+ s +"</li> ";
					}
				}
			}
		}

		return floor;
	}
	/**
	 * ????????????????????????????????????
	 */
	@RequestMapping(params = "getPrimaryMenuForWebos")
	@ResponseBody
	public AjaxJson getPrimaryMenuForWebos() {
		AjaxJson j = new AjaxJson();
		//??????????????????Session??????????????????????????????????????????
		Object getPrimaryMenuForWebos =  ContextHolderUtils.getSession().getAttribute("getPrimaryMenuForWebos");
		if(oConvertUtils.isNotEmpty(getPrimaryMenuForWebos)){
			j.setMsg(getPrimaryMenuForWebos.toString());
		}else{
			String PMenu = ListtoMenu.getWebosMenu(getFunctionMap(ResourceUtil.getSessionUserName()));
			ContextHolderUtils.getSession().setAttribute("getPrimaryMenuForWebos", PMenu);
			j.setMsg(PMenu);
		}
		return j;
	}

    /**
     * ?????????????????????
     * @return
     */
    @RequestMapping(params = "login2")
    public String login2(){
        return "login/login2";
    }
	/**
	 * ACE????????????
	 * @return
	 */
	@RequestMapping(params = "login3")
	public String login3(){
		return "login/login3";
	}
}
