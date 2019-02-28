package com.hundsun.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hundsun.entity.Connector;
import com.hundsun.util.DataUtil;
import com.hundsun.util.PropertiesUtil;
import com.hundsun.util.SFTPUtil;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpException;

@Controller
public class MainController {
	public String address = null;
	public int port;
	public String username = null;
	public String password = null;
	public String localcustomer = null;
	public String localcustomerversion = null;
	public String localms = null;
	public String locallj = null;
	public String localversionnum = null;
	@Autowired
	private SFTPUtil sftpUtil;
	@Autowired
	private PropertiesUtil propertiesUtil;
	@Autowired
	private DataUtil dataUtil;

	/**
	 * 处理登陆请求
	 * 
	 * @param:@param model
	 * @param:@param connector
	 * @param:@return
	 * @param:@throws SftpException
	 * @param:@throws UnsupportedEncodingException
	 * @return:String
	 * @author:wanggk
	 * @date:2018年11月19日
	 * @version:V1.0
	 * @throws Exception
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(Model model, Connector connector) throws Exception {
		Boolean loginflag = sftpUtil.login(connector);
		if (loginflag) {
			username = connector.getUsername();
			port = connector.getPort();
			password = connector.getPassword();
			address = connector.getAddress();
			List<String> paths = new ArrayList<>();
			String root = "/FA6.0/Platform";
			Vector<LsEntry> listFiles = sftpUtil.listFiles(root);
			for (LsEntry item : listFiles) {
				String path = item.getFilename();
				paths.add(path);
			}
			List<String> paths1 = new ArrayList<>();
			String root1 = "/FA6.0";
			Vector<LsEntry> listFiles1 = sftpUtil.listFiles(root1);
			for (LsEntry item : listFiles1) {
				String path = item.getFilename();
				paths1.add(path);
			}
			List<String> customers = propertiesUtil.parseCustomer();
			List<String> patterns = propertiesUtil.parsePattern();
			model.addAttribute("root", root);
			model.addAttribute("customers", customers);
			model.addAttribute("patterns", patterns);
			model.addAttribute("lj", connector.getAddress() + ":" + connector.getPort());
			model.addAttribute("paths", paths);
			model.addAttribute("paths1", paths1);
			return "index";
		} else {
			model.addAttribute("address", connector.getAddress());
			model.addAttribute("port", connector.getPort());
			model.addAttribute("username", connector.getUsername());
			model.addAttribute("msg", "登陆信息有误,请检查登陆信息");
			return "login";
		}

	}

	/**
	 * 启动项目跳转到登陆界面
	 * 
	 * @param:@return
	 * @return:String
	 * @author:wanggk
	 * @date:2018年11月19日
	 * @version:V1.0
	 */
	@RequestMapping(value = "/")
	public String index() {
		return "login";
	}

	@RequestMapping(value = "/index")
	public String index1() {
		return "login";
	}

	/**
	 * 处理添加客户的请求
	 * 
	 * @param:@param model
	 * @param:@param customer
	 * @param:@return
	 * @param:@throws Exception
	 * @return:String
	 * @author:wanggk
	 * @date:2018年11月19日
	 * @version:V1.0
	 */
	@RequestMapping(value = "/addkh")
	@ResponseBody
	public List<String> addkh(@RequestParam(required = true, name = "khaddname") String khaddname,
			@RequestParam(required = true, name = "khaddnumber") String khaddnumber) throws Exception {
		List<String> customers = propertiesUtil.parseCustomer();
		String newcustomer = khaddnumber + "_" + khaddname;
		customers.add(newcustomer);
		propertiesUtil.customerWritein(customers);
		return customers;
	}

	/**
	 * 处理添加模块请求
	 * 
	 * @param:@param addmkname
	 * @param:@param addmkversionn
	 * @param:@param addmkpath
	 * @param:@param addmkuser
	 * @param:@param addtime
	 * @param:@param addmkuserbz
	 * @param:@return
	 * @param:@throws Exception
	 * @return:Map<String,String>
	 * @author:wanggk
	 * @date:2018年11月21日
	 * @version:V1.0
	 */
	@RequestMapping(value = "/addmk")
	@ResponseBody
	public List<String> addmk(@RequestParam(required = true, name = "mkjson") String mkjson) throws Exception {
		propertiesUtil.addmk(mkjson);
		List<String> data = propertiesUtil.parsePattern();
		return data;
	}

	/**
	 * 处理选择版本后加载数据
	 * 
	 * @param:@param customer
	 * @param:@return
	 * @param:@throws Exception
	 * @return:Map<String,String>
	 * @author:wanggk
	 * @date:2018年11月20日
	 * @version:V1.0
	 */
	@RequestMapping(value = "/loaddata")
	@ResponseBody
	public Map<String, String> loaddata(@RequestParam(required = true, name = "customer") String customer,
			@RequestParam(required = true, name = "version") String version) throws Exception {
		Map<String, String> data = new HashMap<>();
		if (version.equals("请选择版本")) {
			propertiesUtil.temporaryClear(data);
			return data;
		}else {
			data = dataUtil.getData(customer, version);
			if (data.size() != 0) {
				propertiesUtil.temporaryWriteIn(data);
			} else {
				propertiesUtil.temporaryClear(data);
			}
		}
		return data;
	}
	@RequestMapping(value = "/loadtemp")
	@ResponseBody
	public Map<String, String> loadtemp() throws Exception {
		Map<String, String> data = new HashMap<>();
		data = propertiesUtil.parseTemporary();
		return data;
	}

	/**
	 * 处理选择用户
	 * 
	 * @param:@param customer
	 * @param:@return
	 * @param:@throws Exception
	 * @return:List<String>
	 * @author:wanggk
	 * @date:2018年11月26日
	 * @version:V1.0
	 */
	@RequestMapping(value = "/loaduser")
	@ResponseBody
	public List<String> loaduser(@RequestParam(required = true, name = "customer") String customer,
			@RequestParam(required = true, name = "flag") String flag) throws Exception {
		List<String> versions = dataUtil.getVersion(customer);
		Map<String, String> data = new HashMap<>();
		if (flag.equals("0")) {
			propertiesUtil.temporaryClear(data);
		}
		return versions;
	}

	/**
	 * 处理模块添加的确定请求
	 * 
	 * @param:@param arr
	 * @param:@param customer
	 * @param:@return
	 * @param:@throws Exception
	 * @return:Map<String,String>
	 * @author:wanggk
	 * @date:2018年11月22日
	 * @version:V1.0
	 */
	@RequestMapping(value = "/confirmmk")
	@ResponseBody
	public Map<String, String> confirmmk(@RequestParam(required = false, name = "arr") String arr,
			@RequestParam(required = true, name = "customer") String customer,
			@RequestParam(required = true, name = "ms") String ms,
			@RequestParam(required = true, name = "lj") String lj,
			@RequestParam(required = true, name = "versionnum") String versionnum) throws Exception {
		Map<String, String> data = propertiesUtil.confirmmk(arr, customer, ms, lj, versionnum,username);
		return data;
	}

	/**
	 * 删除模块列表中的模块
	 * 
	 * @param:@param data
	 * @param:@return
	 * @param:@throws Exception
	 * @return:String
	 * @author:wanggk
	 * @date:2018年11月23日
	 * @version:V1.0
	 */
	@RequestMapping(value = "/deletemk")
	@ResponseBody
	public String deletemk(@RequestParam(required = false, name = "data") String data) throws Exception {
		String deletemk = propertiesUtil.deletemk(data);
		return deletemk;
	}

	/**
	 * 确认删除模块列表中的模块
	 * 
	 * @param:@param data
	 * @param:@return
	 * @param:@throws Exception
	 * @return:String
	 * @author:wanggk
	 * @date:2018年11月23日
	 * @version:V1.0
	 */
	@RequestMapping(value = "/confirmdeletemk")
	@ResponseBody
	public String confirmdeletemk(@RequestParam(required = false, name = "data") String data) throws Exception {
		String confirmdeletemk = propertiesUtil.confirmdeletemk(data);
		return confirmdeletemk;
	}

	/**
	 * 加载模块
	 * 
	 * @param:@param path
	 * @param:@return
	 * @param:@throws SftpException
	 * @return:List<String>
	 * @author:wanggk
	 * @date:2018年11月23日
	 * @version:V1.0
	 */
	@RequestMapping(value = "/loadmk")
	@ResponseBody
	public List<String> loadmk(@RequestParam(required = false, name = "path") String path) throws SftpException {
		List<String> paths = new ArrayList<>();
		Vector<LsEntry> listFiles = sftpUtil.listFiles(path);
		for (LsEntry item : listFiles) {
			String subpath = item.getFilename();
			paths.add(subpath);
		}
		return paths;
	}

	/**
	 * 退出系统
	 * 
	 * @param:@return
	 * @return:String
	 * @author:wanggk
	 * @date:2018年11月23日
	 * @version:V1.0
	 */
	@RequestMapping(value = "/logout")
	public String logout() {
		sftpUtil.logout();
		return "redirect:/index";
	}

	/**
	 * 生成预发布包
	 * 
	 * @param:@param updateall
	 * @param:@param createnewversion
	 * @param:@param msflag
	 * @param:@param modulars
	 * @param:@param customer
	 * @param:@param ms
	 * @param:@param lj
	 * @param:@param versionnum
	 * @param:@param versionval
	 * @param:@return
	 * @param:@throws Exception
	 * @return:List<String>
	 * @author:wanggk
	 * @date:2018年12月5日
	 * @version:V1.0
	 */
	@RequestMapping(value = "/createprerelease")
	@ResponseBody
	public List<String> createprerelease(@RequestParam(required = false, name = "updateall") String updateall,
			@RequestParam(required = false, name = "createnewversion") String createnewversion,
			@RequestParam(required = false, name = "msflag") String msflag,
			@RequestParam(required = false, name = "modulars") String modulars,
			@RequestParam(required = false, name = "customer") String customer,
			@RequestParam(required = false, name = "ms") String ms,
			@RequestParam(required = false, name = "lj") String lj,
			@RequestParam(required = false, name = "versionnum") String versionnum,
			@RequestParam(required = false, name = "versionval") String versionval) throws Exception {
		List<String> createprerelease = propertiesUtil.createprerelease(updateall, createnewversion, msflag, modulars,
				customer, ms, lj, versionnum, versionval, "预发布", username);
		return createprerelease;
	}

	/**
	 * 生成发布包
	 * 
	 * @param:@param updateall
	 * @param:@param createnewversion
	 * @param:@param msflag
	 * @param:@param modulars
	 * @param:@param customer
	 * @param:@param ms
	 * @param:@param lj
	 * @param:@param versionnum
	 * @param:@param versionval
	 * @param:@return
	 * @param:@throws Exception
	 * @return:List<String>
	 * @author:wanggk
	 * @date:2018年12月5日
	 * @version:V1.0
	 */
	@RequestMapping(value = "/createrelease")
	@ResponseBody
	public List<String> createrelease(@RequestParam(required = false, name = "updateall") String updateall,
			@RequestParam(required = false, name = "createnewversion") String createnewversion,
			@RequestParam(required = false, name = "msflag") String msflag,
			@RequestParam(required = false, name = "modulars") String modulars,
			@RequestParam(required = false, name = "customer") String customer,
			@RequestParam(required = false, name = "ms") String ms,
			@RequestParam(required = false, name = "lj") String lj,
			@RequestParam(required = false, name = "versionnum") String versionnum,
			@RequestParam(required = false, name = "versionval") String versionval) throws Exception {
		List<String> createrelease = propertiesUtil.createrelease(updateall, createnewversion, msflag, modulars,
				customer, ms, lj, versionnum, versionval, "发布", username);
		return createrelease;
	}

	/**
	 * 判断是否有 版本
	 * 
	 * @param:@param customer
	 * @param:@return
	 * @param:@throws Exception
	 * @return:Map<String,String>
	 * @author:wanggk
	 * @date:2018年12月5日
	 * @version:V1.0
	 */
	@RequestMapping(value = "/hasallversion")
	@ResponseBody
	public Map<String, String> hasallversion(@RequestParam(required = false, name = "customer") String customer)
			throws Exception {
		Map<String, String> newestData = dataUtil.getNewestData(customer);
		if (dataUtil.getNewestData(customer) == null) {
			return null;
		} else {
			if (newestData.size() == 0) {
				return null;
			}
		}
		return newestData;
	}

	/**
	 * 下载文件
	 * 
	 * @param:@param path
	 * @param:@param localpath
	 * @param:@return
	 * @param:@throws Exception
	 * @return:String
	 * @author:wanggk
	 * @date:2018年12月5日
	 * @version:V1.0
	 */
	@RequestMapping(value = "/downfile")
	@ResponseBody
	public String downfile(@RequestParam(required = false, name = "path") String path,
			@RequestParam(required = false, name = "localpath") String localpath) throws Exception {
		String downfile = sftpUtil.downfile(path, localpath);
		return downfile;
	}

	/**
	 * 维护模块依赖
	 * 
	 * @param:@param model
	 * @param:@return
	 * @param:@throws Exception
	 * @return:String
	 * @author:wanggk
	 * @date:2018年12月5日
	 * @version:V1.0
	 */
	@RequestMapping(value = "/gotodepen", method = RequestMethod.GET)
	public String gotodepen(Model model,@RequestParam(required = false, name = "customer") String customer,
			@RequestParam(required = false, name = "customerversion") String customerversion,
			@RequestParam(required = false, name = "ms") String ms,
			@RequestParam(required = false, name = "lj") String lj,
			@RequestParam(required = false, name = "versionnum") String versionnum) throws Exception {
		localcustomer = customer;
		localcustomerversion = customerversion;
		localms = ms;
		locallj = lj;
		localversionnum = versionnum;
		List<String> paths1 = new ArrayList<>();
		String root1 = "/FA6.0";
		Vector<LsEntry> listFiles1 = sftpUtil.listFiles(root1);
		for (LsEntry item : listFiles1) {
			String path = item.getFilename();
			paths1.add(path);
		}
		List<String> parseDependence = propertiesUtil.parseDependence();
		model.addAttribute("dependences", parseDependence);
		model.addAttribute("paths1", paths1);
		return "depenrelationship";
	}

	/**
	 * 添加模块依赖
	 * 
	 * @param:@param strings
	 * @param:@return
	 * @param:@throws Exception
	 * @return:String
	 * @author:wanggk
	 * @date:2018年12月5日
	 * @version:V1.0
	 */
	@RequestMapping(value = "/addrelationship", method = RequestMethod.GET)
	@ResponseBody
	public String addrelationship(@RequestParam(required = false, name = "strings") String strings) throws Exception {
		String[] depens = strings.split("\\|");
		String ylmstring = depens[1];
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = simpleDateFormat.format(date);
		String dependence = depens[0] + "|" + ylmstring + "|" + username + "|" + time;
		List<String> parseDependence = propertiesUtil.parseDependence();
		parseDependence.add(dependence);
		propertiesUtil.dependenceWritein(parseDependence);
		return username + "," + time;
	}

	/**
	 * 删除模块依赖
	 * 
	 * @param:@param data
	 * @param:@return
	 * @param:@throws Exception
	 * @return:String
	 * @author:wanggk
	 * @date:2018年12月5日
	 * @version:V1.0
	 */
	@RequestMapping(value = "/deleteyl", method = RequestMethod.GET)
	@ResponseBody
	public String deleteyl(@RequestParam(required = false, name = "data") String data) throws Exception {
		propertiesUtil.deleteyl(data);
		return "depenrelationship";
	}

	/**
	 * 返回主页面
	 * 
	 * @param:@param model
	 * @param:@return
	 * @return:String
	 * @author:wanggk
	 * @date:2018年12月5日
	 * @version:V1.0
	 * @throws Exception
	 */
	@RequestMapping(value = "/doreturn", method = RequestMethod.GET)
	public String doreturn(Model model) throws Exception {
		Connector connector = new Connector(address, port, username, password);
		Boolean loginflag = sftpUtil.login(connector);
		if (loginflag) {
			username = connector.getUsername();
			port = connector.getPort();
			password = connector.getPassword();
			address = connector.getAddress();
			List<String> paths = new ArrayList<>();
			String root = "/FA6.0/Platform";
			Vector<LsEntry> listFiles = sftpUtil.listFiles(root);
			for (LsEntry item : listFiles) {
				String path = item.getFilename();
				paths.add(path);
			}
			List<String> paths1 = new ArrayList<>();
			String root1 = "/FA6.0";
			Vector<LsEntry> listFiles1 = sftpUtil.listFiles(root1);
			for (LsEntry item : listFiles1) {
				String path = item.getFilename();
				paths1.add(path);
			}
			List<String> customers = propertiesUtil.parseCustomer();
			List<String> patterns = propertiesUtil.parsePattern();
			model.addAttribute("root", root);
			model.addAttribute("customers", customers);
			model.addAttribute("patterns", patterns);
			model.addAttribute("localcustomer", localcustomer);
			model.addAttribute("localcustomerversion", localcustomerversion);
			model.addAttribute("localms", localms);
			model.addAttribute("locallj", locallj);
			model.addAttribute("lj", locallj);
			model.addAttribute("localversionnum", localversionnum);
			model.addAttribute("paths", paths);
			model.addAttribute("paths1", paths1);
			localcustomer=null;
			localms=null;
			localcustomerversion=null;
			localversionnum=null;
			locallj=null;
			return "index";
		} else {
			model.addAttribute("address", connector.getAddress());
			model.addAttribute("port", connector.getPort());
			model.addAttribute("username", connector.getUsername());
			model.addAttribute("msg", "登陆信息有误,请检查登陆信息");
			return "login";
		}
	}
}
