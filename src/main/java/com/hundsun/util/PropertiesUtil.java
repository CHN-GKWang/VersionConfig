package com.hundsun.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

@PropertySource("classpath:application.properties")
@Component
public class PropertiesUtil {
	@Autowired
	private DataUtil dataUtil;
	@Autowired
	private SFTPUtil sftpUtil;
	@Value("${properties.path}")
	private String propertiespath;

	/**
	 * 解析客户数据
	 * 
	 * @param:@return
	 * @param:@throws UnsupportedEncodingException
	 * @return:List<String>
	 * @author:wanggk
	 * @date:2018年11月19日
	 * @version:V1.0
	 * @throws Exception
	 */
	public List<String> parseCustomer() throws Exception {
		/*
		 * InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(
		 * "Properties/customer.properties"); Properties properties = new Properties();
		 * properties.load(in);
		 */
		// String pathname =
		// ClassUtils.getDefaultClassLoader().getResource("Properties/customer.properties").getPath();
		// String pathname = getRealPath("Properties/customer.properties");
		File file1 = new File(propertiespath);
		if (!file1.exists()) {
			file1.mkdirs();
		}
		String pathname = propertiespath + "/customer.properties";
		File file = new File(pathname);
		if (!file.exists()) {
			file.createNewFile();
		}
		Properties properties = new Properties();
		properties = PropertiesLoaderUtils.loadProperties(new PathResource(file.getAbsolutePath()));
		List<String> customers = new ArrayList<>();
		String size = properties.getProperty("size");
		if (size != null) {
			size = new String(size.getBytes("ISO-8859-1"), "utf-8");
			for (int i = 1; i < Integer.parseInt(size) + 1; i++) {
				String name = properties.getProperty(String.valueOf(i));
				name = new String(name.getBytes("ISO-8859-1"), "utf-8");
				customers.add(name);
			}
		}
		return customers;
	}

	/**
	 * 解析依赖关系
	 * 
	 * @param:@return
	 * @param:@throws Exception
	 * @return:List<String>
	 * @author:wanggk
	 * @date:2018年12月3日
	 * @version:V1.0
	 */
	public List<String> parseDependence() throws Exception {
		String pathname = propertiespath + "/dependence.properties";
		File file = new File(pathname);
		if (!file.exists()) {
			file.createNewFile();
		}
		Properties properties = new Properties();
		properties = PropertiesLoaderUtils.loadProperties(new PathResource(file.getAbsolutePath()));
		List<String> patterns = new ArrayList<>();
		String size = properties.getProperty("size");
		if (size != null) {
			size = new String(size.getBytes("ISO-8859-1"), "utf-8");
			for (int i = 1; i < Integer.parseInt(size) + 1; i++) {
				String pattern = properties.getProperty(String.valueOf(i));
				pattern = new String(pattern.getBytes("ISO-8859-1"), "utf-8");
				patterns.add(pattern);
			}
		}
		return patterns;
	}

	/**
	 * 解析模块
	 * 
	 * @param:@return
	 * @param:@throws IOException
	 * @return:List<String>
	 * @author:wanggk
	 * @date:2018年11月21日
	 * @version:V1.0
	 * @throws Exception
	 */
	public List<String> parsePattern() throws Exception {
		String pathname = propertiespath + "/pattern.properties";
		File file = new File(pathname);
		if (!file.exists()) {
			file.createNewFile();
		}
		Properties properties = new Properties();
		properties = PropertiesLoaderUtils.loadProperties(new PathResource(file.getAbsolutePath()));
		List<String> patterns = new ArrayList<>();
		String size = properties.getProperty("size");
		if (size != null) {
			size = new String(size.getBytes("ISO-8859-1"), "utf-8");
			for (int i = 1; i < Integer.parseInt(size) + 1; i++) {
				String pattern = properties.getProperty(String.valueOf(i));
				pattern = new String(pattern.getBytes("ISO-8859-1"), "utf-8");
				patterns.add(pattern);
			}
		}
		return patterns;
	}

	/**
	 * 解析临时文件
	 * 
	 * @param:@return
	 * @param:@throws Exception
	 * @return:Map<String,String>
	 * @author:wanggk
	 * @date:2018年12月5日
	 * @version:V1.0
	 */
	public Map<String, String> parseTemporary() throws Exception {
		/*
		 * InputStream in = PropertiesUtil.class.getClassLoader().getResourceAsStream(
		 * "Properties/temporary.properties"); Properties properties = new Properties();
		 * properties.load(in);
		 */
		String pathname = propertiespath + "/temporary.properties";
		// String pathname =
		// ClassUtils.getDefaultClassLoader().getResource("Properties/temporary.properties").getPath();
		// String pathname = getRealPath("Properties/temporary.properties");
		File file = new File(pathname);
		if (!file.exists()) {
			file.createNewFile();
		}
		Properties properties = new Properties();
		properties = PropertiesLoaderUtils.loadProperties(new PathResource(file.getAbsolutePath()));
		Map<String, String> data = new HashMap<>();
		String pattern = properties.getProperty("pattern");
		String size = properties.getProperty("size");
		String grampath = properties.getProperty("grampath");
		String versionnumber = properties.getProperty("versionnumber");
		if (pattern == null || size == null || grampath == null || versionnumber == null) {
			return null;
		}
		size = new String(size.getBytes("ISO-8859-1"), "utf-8");
		data.put("size", size);
		pattern = new String(pattern.getBytes("ISO-8859-1"), "utf-8");
		data.put("pattern", pattern);
		grampath = new String(grampath.getBytes("ISO-8859-1"), "utf-8");
		data.put("grampath", grampath);
		versionnumber = new String(versionnumber.getBytes("ISO-8859-1"), "utf-8");
		data.put("versionnumber", versionnumber);
		for (int i = 1; i < Integer.valueOf(size) + 1; i++) {
			String modular = properties.getProperty("modular" + i);
			modular = new String(modular.getBytes("ISO-8859-1"), "utf-8");
			data.put("modular" + i, modular);
		}
		return data;
	}

	/***
	 * 写入客户数据
	 * 
	 * @param:@param name
	 * @param:@throws Exception
	 * @return:void
	 * @author:wanggk
	 * @date:2018年11月19日
	 * @version:V1.0
	 */
	public void customerWritein(List<String> name) throws Exception {
		Properties properties = new Properties();
		String path = propertiespath + "/customer.properties";
		// String path =
		// ClassUtils.getDefaultClassLoader().getResource("Properties/customer.properties").getPath();
		FileOutputStream oFile = new FileOutputStream(path, false);// true表示追加打开
		for (int i = 0; i < name.size(); i++) {
			properties.setProperty(String.valueOf(i + 1), name.get(i));
		}
		properties.setProperty("size", String.valueOf(name.size()));
		OutputStreamWriter oStreamWriter = new OutputStreamWriter(oFile, "utf-8");
		oStreamWriter.flush();
		oFile.flush();
		properties.store(oStreamWriter, "The New properties file");
		oStreamWriter.close();
		oFile.close();
	}

	/**
	 * 待添加模块写入
	 * 
	 * @param:@param patterns
	 * @param:@throws Exception
	 * @return:void
	 * @author:wanggk
	 * @date:2018年12月5日
	 * @version:V1.0
	 */
	public void patternWritein(List<String> patterns) throws Exception {
		Properties properties = new Properties();
		String path = propertiespath + "/pattern.properties";
		// String path =
		// ClassUtils.getDefaultClassLoader().getResource("Properties/pattern.properties").getPath();
		// String path = getRealPath("Properties/pattern.properties");
		FileOutputStream oFile = new FileOutputStream(path, false);// true表示追加打开
		for (int i = 0; i < patterns.size(); i++) {
			properties.setProperty(String.valueOf(i + 1), patterns.get(i));
		}
		properties.setProperty("size", String.valueOf(patterns.size()));
		OutputStreamWriter oStreamWriter = new OutputStreamWriter(oFile, "utf-8");
		properties.store(oStreamWriter, "The New properties file");
		oStreamWriter.flush();
		oStreamWriter.close();
		oFile.flush();
		oFile.close();
	}

	/**
	 * 依赖关系写入
	 * 
	 * @param:@param dependences
	 * @param:@throws Exception
	 * @return:void
	 * @author:wanggk
	 * @date:2018年12月5日
	 * @version:V1.0
	 */
	public void dependenceWritein(List<String> dependences) throws Exception {
		Properties properties = new Properties();
		String path = propertiespath + "/dependence.properties";
		FileOutputStream oFile = new FileOutputStream(path, false);// true表示追加打开
		for (int i = 0; i < dependences.size(); i++) {
			properties.setProperty(String.valueOf(i + 1), dependences.get(i));
		}
		properties.setProperty("size", String.valueOf(dependences.size()));
		OutputStreamWriter oStreamWriter = new OutputStreamWriter(oFile, "utf-8");
		properties.store(oStreamWriter, "The New properties file");
		oStreamWriter.flush();
		oStreamWriter.close();
		oFile.flush();
		oFile.close();
	}

	/**
	 * 临时文件写入
	 * 
	 * @param:@param data
	 * @param:@throws Exception
	 * @return:void
	 * @author:wanggk
	 * @date:2018年12月5日
	 * @version:V1.0
	 */
	public void temporaryWriteIn(Map<String, String> data) throws Exception {
		Properties properties = new Properties();
		String path = propertiespath + "/temporary.properties";
		// String path =
		// ClassUtils.getDefaultClassLoader().getResource("Properties/temporary.properties").getPath();
		// String path = getRealPath("Properties/temporary.properties");
		FileOutputStream oFile = new FileOutputStream(path, false);// true表示追加打开
		properties.setProperty("size", data.get("size"));
		properties.setProperty("grampath", data.get("grampath"));
		properties.setProperty("pattern", data.get("pattern"));
		properties.setProperty("versionnumber", data.get("versionnumber"));
		for (int i = 1; i < Integer.parseInt(data.get("size")) + 1; i++) {
			properties.setProperty("modular" + i, data.get("modular" + i));
		}
		OutputStreamWriter oStreamWriter = new OutputStreamWriter(oFile, "utf-8");
		properties.store(oStreamWriter, "The New properties file");
		oStreamWriter.flush();
		oStreamWriter.close();
		oFile.flush();
		oFile.close();
	}

	/**
	 * 获取properties路径
	 * 
	 * @param:@param file
	 * @param:@return
	 * @param:@throws Exception
	 * @return:String
	 * @author:wanggk
	 * @date:2018年11月19日
	 * @version:V1.0
	 */
	/*
	 * public String getRealPath(String file) throws Exception { Resource resource =
	 * new ClassPathResource(file); String[] a =
	 * resource.getFile().getAbsolutePath().split("\\\\"); String realpath = ""; int
	 * count = 0; for (int i = 0; i < a.length; i++) { if
	 * (a[i].equals("VersionConfigTool")) { count = i; break; } } for (int i = 0; i
	 * <= count; i++) { realpath = realpath + a[i] + "\\"; } realpath = realpath +
	 * "src\\main\\resources\\" + file; return realpath; }
	 */

	/**
	 * 确认模块处理
	 * 
	 * @param:@param arr
	 * @param:@param customer
	 * @param:@param ms
	 * @param:@param lj
	 * @param:@param versionnum
	 * @param:@return
	 * @param:@throws Exception
	 * @return:Map<String,String>
	 * @author:wanggk
	 * @date:2018年11月26日
	 * @version:V1.0
	 */
	public Map<String, String> confirmmk(String arr, String customer, String ms, String lj, String versionnum,String username)
			throws Exception {
		// TODO Auto-generated method stub
		if (arr == null) {
			return null;
		}
		Date date = new Date();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMdd  hh:MM:ss");
		String time = sDateFormat.format(date);
		String[] arrs = arr.split(",");
		Map<String, String> data = parseTemporary();
		List<String> patterns = parsePattern();
		List<String> deleteflag = new ArrayList<>();
		for (int i = 0; i < arrs.length; i++) {
			for (int k = 0; k < patterns.size(); k++) {
				String pattern = patterns.get(k).replaceAll(",", "-");
				if (pattern.equals(arrs[i])) {
					if (!deleteflag.contains(patterns.get(k))) {
						deleteflag.add(patterns.get(k));
					}
				}
			}
			for (int m = 0; m < deleteflag.size(); m++) {
				patterns.remove(deleteflag.get(m));
			}
			String[] strings = arrs[i].split("-");
			String mkname = strings[0];
			String mkversion = strings[1];
			String size = "";
			int flag = 0;
			if (data != null) {
				size = data.get("size");
				for (int j = 1; j < Integer.parseInt(size) + 1; j++) {
					String modular = data.get("modular" + j);
					String[] modulars = modular.split("\\|");
					if (mkname.equals(modulars[0]) && mkversion.equals(modulars[3])) {
						flag = 1;
						String newmodular = mkname + "|" + "修改" + "|" + "预发布" + "|" + mkversion + "|" + modulars[3]
								+ "|" + strings[2] + "|" + strings[3] + "|" + strings[4] + "|" + " " + "|" + " " + "|"
								+ username+"|"+time+"|"+strings[5];
						data.replace("modular" + j, modular, newmodular);
					}
				}
			}
			if (flag == 0) {
				String newmodular = mkname + "|" + "新增" + "|" + "预发布" + "|" + mkversion + "|" + "无" + "|" + strings[2]
						+ "|" + strings[3] + "|" + strings[4] + "|" + " " + "|" + " " + "|" 
						+ username+"|"+time+"|"+ strings[5];
				int one;
				if (data != null) {
					one = Integer.parseInt(size) + 1;
					data.put("modular" + one, newmodular);
					data.replace("size", size, String.valueOf(one));
					data.put("grampath", lj);
					data.put("versionnumber", versionnum);
					data.put("pattern", ms);
				} else {
					one = 1;
					data = new HashMap<>();
					data.put("grampath", lj);
					data.put("versionnumber", versionnum);
					data.put("pattern", ms);
					data.put("modular" + one, newmodular);
					data.put("size", String.valueOf(one));
				}
			}
		}
		patternWritein(patterns);
		temporaryWriteIn(data);
		return data;
	}

	/**
	 * 删除模块处理
	 * 
	 * @param:@param data
	 * @param:@return
	 * @param:@throws Exception
	 * @return:String
	 * @author:wanggk
	 * @date:2018年11月26日
	 * @version:V1.0
	 */
	public String deletemk(String data) throws Exception {
		// TODO Auto-generated method stub
		Map<String, String> parseTemporary = parseTemporary();
		String[] datas = data.split(",");
		String mkname = datas[0];
		String mkversion = datas[3];
		String size = parseTemporary.get("size");
		for (int j = 1; j < Integer.parseInt(size) + 1; j++) {
			String modular = parseTemporary.get("modular" + j);
			String[] modulars = modular.split("\\|");
			if (mkname.equals(modulars[0]) && mkversion.equals(modulars[3])) {
				if (datas[1].equals("新增")) {
					datas[1] = "新增删除";
					modulars[1] = "新增删除";
				} else if (datas[1].equals("修改")) {
					datas[1] = "修改删除";
					modulars[1] = "修改删除";
				} else if ((datas[1].trim()).equals("")) {
					datas[1] = "删除";
					modulars[1] = "删除";

				}
				String newmodular = StringUtils.join(modulars, "|");
				parseTemporary.replace("modular" + j, modular, newmodular);
			}
		}
		String newdatas = StringUtils.join(datas, ",");
		temporaryWriteIn(parseTemporary);
		return newdatas;
	}

	/**
	 * 确认删除模块处理
	 * 
	 * @param:@param data
	 * @param:@return
	 * @param:@throws Exception
	 * @return:String
	 * @author:wanggk
	 * @date:2018年11月26日
	 * @version:V1.0
	 */
	public String confirmdeletemk(String data) throws Exception {
		synchronized (this) {
			// TODO Auto-generated method stub
			Map<String, String> parseTemporary = parseTemporary();
			String[] datas = data.split(",");
			String mkname = datas[0];
			String mkversion = datas[3];
			String size = parseTemporary.get("size");
			for (int j = 1; j < Integer.parseInt(size) + 1; j++) {
				String modular = parseTemporary.get("modular" + j);
				String[] modulars = modular.split("\\|");
				if (mkname.equals(modulars[0]) && mkversion.equals(modulars[3])) {
					if (datas[1].equals("新增删除")) {
						parseTemporary.remove("modular" + j);
						parseTemporary.replace("size", size, String.valueOf(Integer.parseInt(size) - 1));
					} else if (datas[1].equals("修改删除")) {
						datas[1] = " ";
						modulars[1] = " ";
						String newmodular = StringUtils.join(modulars, "|");
						parseTemporary.replace("modular" + j, modular, newmodular);
					} else if ((datas[1]).equals("删除")) {
						parseTemporary.remove("modular" + j);
						parseTemporary.replace("size", size, String.valueOf(Integer.parseInt(size) - 1));
					}
				}
			}
			List<String> newmodulars = new ArrayList<>();
			for (int i = 1; i < Integer.parseInt(size) + 1; i++) {
				if (parseTemporary.get("modular" + i) == null) {
					continue;
				} else {
					newmodulars.add(parseTemporary.get("modular" + i));
					parseTemporary.remove("modular" + i);
				}
			}
			for (int i = 0; i < newmodulars.size(); i++) {
				parseTemporary.put("modular" + (i + 1), newmodulars.get(i));
			}
			String newdatas = StringUtils.join(datas, ",");
			temporaryWriteIn(parseTemporary);
			return newdatas;
		}
	}

	/**
	 * 清空临时配置文件
	 * 
	 * @param:@param data
	 * @param:@throws Exception
	 * @return:void
	 * @author:wanggk
	 * @date:2018年11月26日
	 * @version:V1.0
	 */
	public void temporaryClear(Map<String, String> data) throws Exception {
		// TODO Auto-generated method stub
		Properties properties = new Properties();
		String path = propertiespath + "/temporary.properties";
		// String path =
		// ClassUtils.getDefaultClassLoader().getResource("Properties/temporary.properties").getPath();
		// String path = getRealPath("Properties/temporary.properties");
		FileOutputStream oFile = new FileOutputStream(path, false);// true表示追加打开
		OutputStreamWriter oStreamWriter = new OutputStreamWriter(oFile, "utf-8");
		properties.store(oStreamWriter, "The New properties file");
		oStreamWriter.flush();
		oStreamWriter.close();
		oFile.flush();
		oFile.close();
	}

	/**
	 * 预发布
	 * 
	 * @param:@param flag
	 * @param:@param msflag
	 * @param:@param modulars
	 * @param:@param customer
	 * @param:@param ms
	 * @param:@param lj
	 * @param:@param versionnum
	 * @return:void
	 * @author:wanggk
	 * @date:2018年11月23日
	 * @version:V1.0
	 * @param path
	 * @param versionnum
	 * @param lj
	 * @param ms
	 * @param customer
	 * @param modulars
	 * @param msflag
	 * @param createnewversion
	 * @param updateall
	 * @param path
	 * @param versionval
	 * @param string
	 * @param path2
	 * @throws Exception
	 */
	public List<String> createprerelease(String updateall, String createnewversion, String msflag, String modulars,
			String customer, String ms, String lj, String versionnum, String versionval, String string, String user)
			throws Exception {
		// TODO Auto-generated method stub
		// 下载文件处理
		modulars = modulars.substring(1, modulars.length() - 1);
		List<String> paths = new ArrayList<>();
		String[] modularses = modulars.split(",");
		boolean hasjixian = false;
		// 遍历查找是否有基线模块
		for (int m = 0; m < modularses.length; m++) {
			String modular2 = modularses[m].substring(1, modularses[m].length() - 1);
			String[] attributes3 = modular2.split("\\|");
			if (attributes3[0].equals("基线")) {
				String temp1 = modularses[0];
				modularses[0] = modularses[m];
				modularses[m] = temp1;
				hasjixian = true;
				break;
			}
		}
		int init = 0;
		// 标识有没有基线模块
		if (hasjixian) {
			init = 1;
		}
		// 对数组根据日期进行排序
		for (int i = init; i < modularses.length; i++) {
			int k = i;
			for (int j = k + 1; j < modularses.length; j++) {
				String modular = modularses[k].substring(1, modularses[k].length() - 1);
				String[] attributes = modular.split("\\|");
				String modular1 = modularses[j].substring(1, modularses[j].length() - 1);
				String[] attributes1 = modular1.split("\\|");
				if (Integer.parseInt(attributes1[3]) < Integer.parseInt(attributes[3])) {
					k = j;
				}
				if (i != k) {
					String temp = modularses[i];
					modularses[i] = modularses[k];
					modularses[k] = temp;
				}
			}

		}
		if (msflag.equals("2")) {
			paths.add(versionnum);
		}
		for (int p = 0; p < modularses.length; p++) {
			String modular = modularses[p].substring(1, modularses[p].length() - 1);
			String[] attributes2 = modular.split("\\|");
			if (msflag.equals("1")) {
				if (attributes2[1].equals("新增") || attributes2[1].equals("修改")) {
					String name = "";
					if (attributes2[0].equals("基线")) {
						name = attributes2[3];
					} else {
						name = attributes2[3] + "_" + attributes2[0];
					}
					String newpath = attributes2[5] + "/" + name;
					paths.add(newpath);
				}
			} else {
				String name = "";
				if (attributes2[0].equals("基线")) {
					name = attributes2[3];
				} else {
					name = attributes2[3] + "_" + attributes2[0];
				}
				String newpath = attributes2[5] + "/" + name;
				paths.add(newpath);
			}

		}
		List<String> allfile = new ArrayList<>();
		for (int i = 0; i < paths.size(); i++) {
			List<String> path2 = sftpUtil.getPath(paths.get(i));
			if (path2 != null) {
				for (int j = 0; j < path2.size(); j++) {
					allfile.add(path2.get(j));
				}
			}
		}
		// 创建或更新配置文件
		if (createnewversion.equals("1")) {
			Map<String, String> data = new HashMap<>();
			data.put("size", String.valueOf(modularses.length));
			data.put("grampath", lj);
			data.put("pattern", ms);
			data.put("versionnumber", versionval);
			data.put("customer", customer);
			for (int i = 0; i < modularses.length; i++) {
				String modular = modularses[i].substring(1, modularses[i].length() - 1);
				data.put("modular" + (i + 1), modular);
			}
			dataUtil.createProperties(data, string, user);
		} else {
			if (msflag.equals("1")) {
				if (updateall.equals("1")) {
					List<String> patterns = new ArrayList<>();
					for (int i = 0; i < modularses.length; i++) {
						String modular = modularses[i].substring(1, modularses[i].length() - 1);
						String[] attr = modular.split("\\|");
						if (attr[1].equals("新增") && attr[1].equals("修改")) {
							attr[1] = "";
							attr[2] = "已发布";
							String newmodular = StringUtils.join(attr, "|");
							patterns.add(newmodular);
						}

					}
					dataUtil.updateProperties(patterns, customer, string, user);
				} else {
					// 使用一次并不保存配置
				}
			} else {
				// 使用一次并不保存配置
			}
		}
		return allfile;
	}

	/**
	 * 发布
	 * 
	 * @param:@param flag
	 * @param:@param msflag
	 * @param:@param modulars
	 * @param:@param customer
	 * @param:@param ms
	 * @param:@param lj
	 * @param:@param versionnum
	 * @return:void
	 * @author:wanggk
	 * @date:2018年11月23日
	 * @version:V1.0
	 * @param path
	 * @param versionnum
	 * @param lj
	 * @param ms
	 * @param customer
	 * @param modulars
	 * @param msflag
	 * @param createnewversion
	 * @param updateall
	 * @param path
	 * @param versionval
	 * @param string
	 * @param path2
	 * @throws Exception
	 */
	public List<String> createrelease(String updateall, String createnewversion, String msflag, String modulars,
			String customer, String ms, String lj, String versionnum, String versionval, String string, String user)
			throws Exception {
		// TODO Auto-generated method stub
		// 下载文件处理
		Date date = new Date();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMdd  hh:MM:ss");
		String time = sDateFormat.format(date);
		modulars = modulars.substring(1, modulars.length() - 1);
		List<String> paths = new ArrayList<>();
		String[] modularses = modulars.split(",");
		boolean hasjixian = false;
		// 遍历查找是否有基线模块
		for (int m = 0; m < modularses.length; m++) {
			String modular2 = modularses[m].substring(1, modularses[m].length() - 1);
			String[] attributes3 = modular2.split("\\|");
			if (attributes3[0].equals("基线")) {
				String temp1 = modularses[0];
				modularses[0] = modularses[m];
				modularses[m] = temp1;
				hasjixian = true;
				break;
			}
		}
		int init = 0;
		// 标识有没有基线模块
		if (hasjixian) {
			init = 1;
		}
		// 对数组根据日期进行排序
		for (int i = init; i < modularses.length; i++) {
			int k = i;
			for (int j = k + 1; j < modularses.length; j++) {
				String modular = modularses[k].substring(1, modularses[k].length() - 1);
				String[] attributes = modular.split("\\|");
				String modular1 = modularses[j].substring(1, modularses[j].length() - 1);
				String[] attributes1 = modular1.split("\\|");
				if (Integer.parseInt(attributes1[3]) < Integer.parseInt(attributes[3])) {
					k = j;
				}
				if (i != k) {
					String temp = modularses[i];
					modularses[i] = modularses[k];
					modularses[k] = temp;
				}
			}

		}
		if (msflag.equals("2")) {
			paths.add(versionnum);
		}
		for (int p = 0; p < modularses.length; p++) {
			String modular = modularses[p].substring(1, modularses[p].length() - 1);
			String[] attributes2 = modular.split("\\|");
			if (msflag.equals("1")) {
				if (attributes2[1].equals("新增") || attributes2[1].equals("修改")) {
					String name = "";
					if (attributes2[0].equals("基线")) {
						name = attributes2[3];
					} else {
						name = attributes2[3] + "_" + attributes2[0];
					}
					String newpath = attributes2[5] + "/" + name;
					paths.add(newpath);
				}
			} else {
				String name = "";
				if (attributes2[0].equals("基线")) {
					name = attributes2[3];
				} else {
					name = attributes2[3] + "_" + attributes2[0];
				}
				String newpath = attributes2[5] + "/" + name;
				paths.add(newpath);
			}

		}
		List<String> allfile = new ArrayList<>();
		for (int i = 0; i < paths.size(); i++) {
			List<String> path2 = sftpUtil.getPath(paths.get(i));
			if (path2 != null) {
				for (int j = 0; j < path2.size(); j++) {
					allfile.add(path2.get(j));
				}
			}
		}
		// 创建或更新配置文件
		if (createnewversion.equals("1")) {
			Map<String, String> data = new HashMap<>();
			data.put("size", String.valueOf(modularses.length));
			data.put("grampath", lj);
			data.put("pattern", ms);
			data.put("versionnumber", versionval);
			data.put("customer", customer);
			for (int i = 0; i < modularses.length; i++) {
				String modular = modularses[i].substring(1, modularses[i].length() - 1);
				String[] attr = modular.split("\\|");
				attr[1] = "";
				attr[2] = "已发布";
				attr[8] = user;
				attr[9] = time;
				String newmodular = StringUtils.join(attr, "|");
				data.put("modular" + (i + 1), newmodular);
			}
			dataUtil.createProperties(data, string, user);
		} else {
			if (msflag.equals("1")) {
				if (updateall.equals("1")) {
					List<String> patterns = new ArrayList<>();
					for (int i = 0; i < modularses.length; i++) {
						String modular = modularses[i].substring(1, modularses[i].length() - 1);
						String[] attr = modular.split("\\|");
						if (attr[1].equals("新增") && attr[1].equals("修改")) {
							attr[1] = "";
							attr[2] = "已发布";
							attr[8] = user;
							attr[9] = time;
							String newmodular = StringUtils.join(attr, "|");
							patterns.add(newmodular);
						}
					}
					dataUtil.updateProperties(patterns, customer, string, user);
				} else {
					// 使用一次并不保存配置
				}
			} else {
				// 使用一次并不保存配置
			}
		}
		return allfile;
	}

	/**
	 * 删除依赖
	 * 
	 * @param:@param data
	 * @param:@throws Exception
	 * @return:void
	 * @author:wanggk
	 * @date:2018年12月5日
	 * @version:V1.0
	 */
	public void deleteyl(String data) throws Exception {
		// TODO Auto-generated method stub
		String[] depens = data.split("\\|");
		List<String> parseDependence = parseDependence();
		for (int i = 0; i < parseDependence.size(); i++) {
			String depen = parseDependence.get(i);
			String[] depenss = depen.split("\\|");
			if ((depens[0].trim()).equals(depenss[0].trim()) && (depens[1].trim()).equals(depenss[1].trim())
					&& (depens[2].trim()).equals(depenss[2].trim()) && (depens[3].trim()).equals(depenss[3].trim())) {
				parseDependence.remove(i);
				break;
			}
		}
		dependenceWritein(parseDependence);
	}

	/**
	 * 获取依赖关系
	 * 
	 * @param:@param newmkpaths
	 * @param:@return
	 * @param:@throws Exception
	 * @return:List<String>
	 * @author:wanggk
	 * @date:2018年12月5日
	 * @version:V1.0
	 */
	public List<String> getDepence(List<String> newmkpaths) throws Exception {
		// TODO Auto-generated method stub]
		List<String> parseDependence = parseDependence();
		List<String> dependences = new ArrayList<>();
		List<String> delete = new ArrayList<>();
		List<String> parsePattern = parsePattern();
		for (int i = 0; i < parseDependence.size(); i++) {
			String depen = parseDependence.get(i);
			String[] depens = depen.split("\\|");
			for (int j = 0; j < newmkpaths.size(); j++) {
				String name = newmkpaths.get(j);
				if (name.equals(depens[0])) {
					String[] paths = depens[1].split(",");
					for (int k = 0; k < paths.length; k++) {
						dependences.add(paths[k]);
					}
					break;
				}
			}
		}
		// 去除本List中的重复
		for (int i = 0; i < dependences.size(); i++) {
			String[] paths = dependences.get(i).split("/");
			String nv = paths[paths.length - 1];
			String[] nvs = nv.split("_");
			String time = nvs[0];
			String name = nvs[1];
			for (int j = i + 1; j < dependences.size(); j++) {
				String[] paths1 = dependences.get(j).split("/");
				String nv1 = paths1[paths1.length - 1];
				String[] nvs1 = nv1.split("_");
				String time1 = nvs1[0];
				String name1 = nvs1[1];
				if (name1.equals(name)) {
					if (Integer.parseInt(time1) > Integer.parseInt(time)) {
						delete.add(dependences.get(i));
					} else {
						delete.add(dependences.get(j));
					}
				}
			}
		}
		for (int i = 0; i < delete.size(); i++) {
			String deleteitem = delete.get(i);
			dependences.remove(deleteitem);
		}
		delete.clear();
		// 去除和文件比较的重复
		for (int i = 0; i < dependences.size(); i++) {
			String[] paths2 = dependences.get(i).split("/");
			String nv2 = paths2[paths2.length - 1];
			String[] nvs2 = nv2.split("_");
			String time2 = nvs2[0];
			String name2 = nvs2[1];
			for (int j = 0; j < parsePattern.size(); j++) {
				String[] paths3 = parsePattern.get(j).split(",");
				if (paths3[0].equals(name2)) {
					if (Integer.parseInt(paths3[1]) > Integer.parseInt(time2)) {
						delete.add(dependences.get(i));
					} else {
						parsePattern.remove(parsePattern.get(j));
					}
				}

			}
		}
		for (int i = 0; i < delete.size(); i++) {
			String deleteitem = delete.get(i);
			dependences.remove(deleteitem);
		}
		delete.clear();
		patternWritein(parsePattern);
		return dependences;
	}

	/**
	 * 添加待添加模块
	 * 
	 * @param:@param mkjson
	 * @param:@throws Exception
	 * @return:void
	 * @author:wanggk
	 * @date:2018年12月5日
	 * @version:V1.0
	 */
	public void addmk(String mkjson) throws Exception {
		// TODO Auto-generated method stub
		mkjson = mkjson.substring(1, mkjson.length() - 1);
		String[] mkjsons = mkjson.split("\\|");
		
		String[] names = mkjsons[0].split(",");
		String addtime = mkjsons[3].replaceAll("-", "");
		List<String> delete = new ArrayList<>();
		List<String> newmkpaths = new ArrayList<>();
		for (int i = 0; i < names.length; i++) {
			String newmkpath = mkjsons[1] + "/" + names[i];
			newmkpaths.add(newmkpath);
		}
		List<String> depen = getDepence(newmkpaths);
		List<String> patterns = parsePattern();
		for (int i = 0; i < depen.size(); i++) {
			String depens = depen.get(i);
			String[] depennames = depens.split("/");
			String[] nvs = depennames[depennames.length - 1].split("_");
			String time = nvs[0];
			String name = nvs[1];
			String pattern = name + "," + time + "," + mkjsons[1] + "," + mkjsons[2] + "," + addtime + "," + mkjsons[4];
			patterns.add(pattern);
		}
		for (int i = 0; i < names.length; i++) {
			String[] nvs = names[i].split("_");
			String version = nvs[0];
			String name = "";
			if (nvs.length == 1) {
				if (nvs[0].equals("Instance")) {
					name = "Instance";
				} else {
					name = "基线";
				}
			} else {
				name = nvs[1];
			}
			String pattern = name + "," + version + "," + mkjsons[1] + "," + mkjsons[2] + "," + addtime + ","
					+ mkjsons[4];
			patterns.add(pattern);
		}
		//去掉list中的重复
		for (int i = 0; i < patterns.size(); i++) {
			String[] paths = patterns.get(i).split(",");
			String time = paths[1];
			String name = paths[0];
			for (int j = i + 1; j < patterns.size(); j++) {
				String[] paths1 = patterns.get(j).split(",");
				String time1 = paths1[1];
				String name1 = paths1[0];
				if (name1.equals(name)) {
					if (Integer.parseInt(time1) >= Integer.parseInt(time)) {
						delete.add(patterns.get(i));
					} else {
						delete.add(patterns.get(j));
					}
				}
			}
		}
		for (int i = 0; i < delete.size(); i++) {
			String deleteitem = delete.get(i);
			patterns.remove(deleteitem);
		}
		patternWritein(patterns);
	}
}
