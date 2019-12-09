package com.infopublic.util;

import org.springframework.context.ApplicationContext;

/**
 * 项目名称：
*/
public class Const {
	public static final String SESSION_SECURITY_CODE = "sessionSecCode";//验证码
	public static final String SESSION_USER = "sessionUser";			//session用的用户
	public static final String SESSION_MENU_RIGHTS = "sessionMenuRights";
//	public static final String sSESSION_ROLE_RIGHTS = "sessionRoleRights";
	public static final String SESSION_menuList = "menuList";			//当前菜单
	public static final String SESSION_allmenuList = "allmenuList";		//全部菜单
	public static final String SESSION_nousermenuList = "nousermenuList";//未登录时的菜单
	public static final String SESSION_QX = "QX";
	public static final String SESSION_nouserQX = "nouserQX";
//	public static final String SESSION_userpds = "userpds";			
	public static final String SESSION_USERID = "USERID";				//用户对象
	public static final String SESSION_USERNAME = "USERNAME";			//用户名
	public static final String SESSION_LOGINID = "LOGINID";			//登陆名
	public static final String SESSION_USERROLEID = "USERROLEID";			//用户角色
	public static final String SESSION_USERAREALIST = "USERAREALIST";			//用户所管理的所有下属区域列表(用户管理用)
	public static final String SESSION_MAPAREALIST = "MAPAREALIST";			//用户所管理的区域及所有下属区域列表(经纬度信息管理用)
	public static final String TRUE = "T";
	public static final String FALSE = "F";
	public static final String LOGIN = "/login_toLogin.do";				//登录地址
	public static final String TRAFFICRESEDATE = "admin/config/TRAFFICRESEDATE.txt";	//流量清零日期路径
	public static final String SYSNAME = "admin/config/SYSNAME.txt";	//系统名称路径
	public static final String PAGE	= "admin/config/PAGE.txt";			//分页条数配置路径
	public static final String EMAIL = "admin/config/EMAIL.txt";		//邮箱服务器配置路径
	public static final String SMS1 = "admin/config/SMS1.txt";			//短信账户配置路径1
	public static final String SMS2 = "admin/config/SMS2.txt";			//短信账户配置路径2
	public static final String FWATERM = "admin/config/FWATERM.txt";	//文字水印配置路径
	public static final String IWATERM = "admin/config/IWATERM.txt";	//图片水印配置路径
	public static final String WEIXIN	= "admin/config/WEIXIN.txt";	//微信配置路径
	public static final String WEBSOCKET = "admin/config/WEBSOCKET.txt";//WEBSOCKET配置路径
	public static final String CONFIG = "admin/config/config.properties";	//短信猫配置路径
	public static final String FILEPATH = "uploadFiles/";	//文件路径
	public static final String FILEPATHIMG = "uploadFiles/uploadImgs/";	//图片上传路径
	public static final String FILEPATHFILE = "uploadFiles/file/";		//文件上传路径
	public static final String FILEPATHAPPLY = "uploadFiles/applyfile/";		//申请文件上传路径
	public static final String FILEPATHPROTEMP = "uploadFiles/protemp/";		//导出节目excel文件压缩包临时文件夹
	public static final String FILEPATHPROSPEC = "uploadFiles/prospec/";		//特种节目文件夹
	public static final String FILEPATHPER = "uploadFiles/perfile/";	//节目文件夹
	public static final String NO_INTERCEPTOR_PATH = ".*/((api)|(login)|(logout)|(code)|(app)|(weixin)|(static)|(main)|(websocket)).*";	//不对匹配该值的访问路径拦截（正则）
	public static ApplicationContext WEB_APP_CONTEXT = null; //该值会在web容器启动时由WebAppContextListener初始化
	/**
	 * APP Constants
	 */
	//app注册接口_请求协议参数)
	public static final String[] APP_REGISTERED_PARAM_ARRAY = new String[]{"countries","uname","passwd","title","full_name","company_name","countries_code","area_code","telephone","mobile"};
	public static final String[] APP_REGISTERED_VALUE_ARRAY = new String[]{"国籍","邮箱帐号","密码","称谓","名称","公司名称","国家编号","区号","电话","手机号"};
	
	//app根据用户名获取会员信息接口_请求协议中的参数
	public static final String[] APP_GETAPPUSER_PARAM_ARRAY = new String[]{"USERNAME"};
	public static final String[] APP_GETAPPUSER_VALUE_ARRAY = new String[]{"用户名"};
		public static final String SESSION_FID = "FID";
		
		public static final String SESSION_FNAME = "FNAME";	
		
		public static final String[] LOGTYPE=new String[]{"登陆日志","操作日志"};
		public static final String TRAFFICRESEJOB = "TRAFFICRESEJOB";	//终端流量清零任务名
}
