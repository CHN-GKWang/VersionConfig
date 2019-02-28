package com.hundsun.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

@PropertySource("classpath:application.properties")
@Component
public class DataUtil {

	@Value("${properties.path}")
	private String propertiespath;

	/**
	 * 获取该用户的数据
	 * 
	 * @param:@param customer
	 * @param:@param version
	 * @param:@return
	 * @param:@throws IOException
	 * @return:Map<String,String>
	 * @author:wanggk
	 * @date:2018年12月5日
	 * @version:V1.0
	 */
	public Map<String, String> getData(String customer, String version) throws IOException {
		// TODO Auto-generated method stub
		String rootPath = propertiespath;
		// String rootPath =
		// ClassUtils.getDefaultClassLoader().getResource("Properties").getPath();
		File file = new File(rootPath + "/" + customer);
		Map<String, String> data = new HashMap<>();
		File file1 = new File(file.getAbsolutePath() + "/" + version + ".properties");
		Properties properties = new Properties();
		properties = PropertiesLoaderUtils.loadProperties(new PathResource(file1.getAbsolutePath()));
		String pattern = properties.getProperty("pattern");
		pattern = new String(pattern.getBytes("ISO-8859-1"), "utf-8");
		data.put("pattern", pattern);
		String grampath = properties.getProperty("grampath");
		grampath = new String(grampath.getBytes("ISO-8859-1"), "utf-8");
		data.put("grampath", grampath);
		String mainversion = properties.getProperty("mainversion");
		mainversion = new String(mainversion.getBytes("ISO-8859-1"), "utf-8");
		data.put("mainversion", mainversion);
		String versionnumber = properties.getProperty("versionnumber");
		versionnumber = new String(versionnumber.getBytes("ISO-8859-1"), "utf-8");
		data.put("versionnumber", versionnumber);
		String size = properties.getProperty("size");
		size = new String(size.getBytes("ISO-8859-1"), "utf-8");
		data.put("size", size);
		for (int i = 1; i < Integer.valueOf(size) + 1; i++) {
			String modular = properties.getProperty("modular" + i);
			modular = new String(modular.getBytes("ISO-8859-1"), "utf-8");
			data.put("modular" + i, modular);
		}
		return data;
	}

	/**
	 * 获取相应客户的最新数据
	 * 
	 * @param:@param name
	 * @param:@return
	 * @param:@throws IOException
	 * @return:Map<String,String>
	 * @author:wanggk
	 * @date:2018年11月26日
	 * @version:V1.0
	 */
	public Map<String, String> getNewestData(String name) throws IOException {
		String rootPath = propertiespath;
		// String rootPath =
		// ClassUtils.getDefaultClassLoader().getResource("Properties").getPath();
		Map<String, String> data = new HashMap<>();
		File file = new File(rootPath);
		if (!file.exists()) {
			file.mkdirs();
			return null;
		} else {
			File[] files = file.listFiles();
			for (File file2 : files) {
				if (file2.getName().equals(name)) {
					File file3 = new File(file2.getAbsolutePath() + "/全量计数文件.txt");
					if (!file3.exists()) {
						// 全量计数文件不存在即没有数据文件
						return null;
					} else {
						// 全量计数文件存在
						FileInputStream fileInputStream = new FileInputStream(file3);
						InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
						BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
						String line = bufferedReader.readLine();
						bufferedReader.close();
						inputStreamReader.close();
						fileInputStream.close();
						if (line.equals(null)) {
							return null;
						} else {
							File file4 = new File(file2.getAbsolutePath() + "/" + line + ".properties");
							Properties properties = new Properties();
							properties = PropertiesLoaderUtils
									.loadProperties(new PathResource(file4.getAbsolutePath()));
							String pattern = properties.getProperty("pattern");
							pattern = new String(pattern.getBytes("ISO-8859-1"), "utf-8");
							data.put("pattern", pattern);
							String grampath = properties.getProperty("grampath");
							grampath = new String(grampath.getBytes("ISO-8859-1"), "utf-8");
							data.put("grampath", grampath);
							String mainversion = properties.getProperty("mainversion");
							mainversion = new String(mainversion.getBytes("ISO-8859-1"), "utf-8");
							data.put("mainversion", mainversion);
							String versionnumber = properties.getProperty("versionnumber");
							versionnumber = new String(versionnumber.getBytes("ISO-8859-1"), "utf-8");
							data.put("versionnumber", versionnumber);
							String size = properties.getProperty("size");
							size = new String(size.getBytes("ISO-8859-1"), "utf-8");
							data.put("size", size);
							for (int i = 1; i < Integer.valueOf(size) + 1; i++) {
								String modular = properties.getProperty("modular" + i);
								modular = new String(modular.getBytes("ISO-8859-1"), "utf-8");
								data.put("modular" + i, modular);
							}
						}
					}
				}
			}
			return data;
		}
	}

	/**
	 * 获取用户的全部版本
	 * 
	 * @param:@param customer
	 * @param:@return
	 * @param:@throws IOException
	 * @return:List<String>
	 * @author:wanggk
	 * @date:2018年12月5日
	 * @version:V1.0
	 */
	public List<String> getVersion(String customer) throws IOException {
		// TODO Auto-generated method stub
		String rootPath = propertiespath;
		// String rootPath =
		// ClassUtils.getDefaultClassLoader().getResource("Properties").getPath();
		List<String> versions = new ArrayList<>();
		File file = new File(rootPath);
		if (!file.exists()) {
			file.mkdirs();
			return null;
		} else {
			File[] files = file.listFiles();
			for (File file2 : files) {
				if (file2.getName().equals(customer)) {
					File file3 = new File(file2.getAbsolutePath());
					File[] listFiles = file3.listFiles();
					for (File file4 : listFiles) {
						if (file4.getName().endsWith("properties")) {
							String[] version = file4.getName().split("\\.");
							versions.add(version[0]);
						}
					}
				}
			}
		}
		return versions;
	}

	/**
	 * 创建properties文件
	 * 
	 * @param:@param data
	 * @param:@throws Exception
	 * @return:void
	 * @author:wanggk
	 * @date:2018年11月26日
	 * @version:V1.0
	 * @param string
	 */
	public void createProperties(Map<String, String> data, String string, String user) throws Exception {
		String customer = data.get("customer");
		String ms = data.get("pattern");
		String msName = "";
		if (ms.equals("1")) {
			msName = "增量";
		} else {
			msName = "全量";
		}
		String rootPath = propertiespath;
		// String rootPath =
		// ClassUtils.getDefaultClassLoader().getResource("Properties").getPath();
		String customPath = rootPath + "\\" + customer;
		File customFile = new File(customPath);
		if (!customFile.exists()) {
			customFile.mkdirs();
		}
		String txtPath = customPath + "\\" + msName + "计数文件.txt";
		File txtFile = new File(txtPath);
		if (!txtFile.exists()) {
			txtFile.createNewFile();
		}
		Date date = new Date();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = sDateFormat.format(date);
		if (txtFile.length() == 0) {
			String propertiesName = "";
			if (string.equals("预发布")) {
				propertiesName = customer + time + msName + "1" + user + time + "(预发布)";
			} else {
				propertiesName = customer + time + msName + "1";
			}
			String propertiesPath = customPath + "\\" + propertiesName + ".properties";
			File propertiesFile = new File(propertiesPath);
			propertiesFile.createNewFile();
			FileOutputStream fileOutputStream = new FileOutputStream(txtFile);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf-8");
			outputStreamWriter.write(propertiesName);
			outputStreamWriter.close();
			fileOutputStream.close();
			data.put("mainversion", propertiesName);
			propertiesWritein(data, propertiesFile.getAbsolutePath());
		} else {
			FileInputStream fileInputStream = new FileInputStream(txtFile);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String name = bufferedReader.readLine();
			String num = name.substring(name.length() - 1, name.length());
			int indexOf =0;
			if (msName.equals("增量")) {
				indexOf = name.indexOf("增量");
			}else {
				indexOf = name.indexOf("全量");
			}
			int flag = 0;
			for (int i = 0; i < name.length(); i++) {
				if (name.charAt(i)>=97&&name.charAt(i)<=122) {
					flag = i;
					break;
				}
			}
			if (num.equals(")")) {
				num = name.substring(indexOf+2,flag);
			}
			String propertiesName = "";
			if (string.equals("预发布")) {
				propertiesName = customer + time + msName + (Integer.parseInt(num) + 1) + user + time + "(预发布)";
			} else {
				propertiesName = customer + time + msName + (Integer.parseInt(num) + 1);
			}
			String propertiesPath = customPath + "\\" + propertiesName + ".properties";
			File propertiesFile = new File(propertiesPath);
			propertiesFile.createNewFile();
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();
			FileOutputStream fileOutputStream = new FileOutputStream(txtFile);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf-8");
			outputStreamWriter.write(propertiesName);
			outputStreamWriter.close();
			fileOutputStream.close();
			data.put("mainversion", propertiesName);
			propertiesWritein(data, propertiesFile.getAbsolutePath());
		}
	}

	/**
	 * 生成properties文件后写入数据
	 * 
	 * @param:@param data
	 * @param:@param path
	 * @param:@throws Exception
	 * @return:void
	 * @author:wanggk
	 * @date:2018年11月26日
	 * @version:V1.0
	 */
	public void propertiesWritein(Map<String, String> data, String path) throws Exception {
		// TODO Auto-generated method stub
		Properties properties = new Properties();
		FileOutputStream oFile = new FileOutputStream(path);// true表示追加打开
		properties.setProperty("size", data.get("size"));
		properties.setProperty("grampath", data.get("grampath"));
		properties.setProperty("pattern", data.get("pattern"));
		properties.setProperty("mainversion", data.get("mainversion"));
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
	 * 更新模块信息
	 * 
	 * @param:@param patterns
	 * @param:@param customer
	 * @param:@throws Exception
	 * @return:void
	 * @author:wanggk
	 * @date:2018年11月26日
	 * @version:V1.0
	 */
	public void updateProperties(List<String> patterns, String customer, String string, String user) throws Exception {
		Map<String, String> data = getNewestData(customer);
		String rootPath = propertiespath;
		// String rootPath =
		// ClassUtils.getDefaultClassLoader().getResource("Properties").getPath();
		String customPath = rootPath + "\\" + customer;
		String txtPath = customPath + "\\增量计数文件.txt";
		File txtFile = new File(txtPath);
		if (!txtFile.exists()) {
			txtFile.createNewFile();
		}
		Date date = new Date();
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMdd");
		String time = sDateFormat.format(date);
		String num = "";
		if (txtFile.length() == 0) {
			num = "0";
		} else {
			FileInputStream fileInputStream = new FileInputStream(txtFile);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String name = bufferedReader.readLine();
			num = name.substring(name.length() - 1, name.length());
			if (num.equals(")")) {
				num = name.substring(12, 13);
			}
			bufferedReader.close();
			inputStreamReader.close();
			fileInputStream.close();
		}
		String propertiesName = "";
		if (string.equals("预发布")) {
			propertiesName = customer + time + "增量" + (Integer.parseInt(num) + 1) + user + time + "(预发布)";
		} else {
			propertiesName = customer + time + "增量" + (Integer.parseInt(num) + 1);
		}
		String propertiesPath = customPath + "\\" + propertiesName + ".properties";
		File propertiesFile = new File(propertiesPath);
		propertiesFile.createNewFile();
		FileOutputStream fileOutputStream = new FileOutputStream(txtFile);
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf-8");
		outputStreamWriter.write(propertiesName);
		outputStreamWriter.close();
		fileOutputStream.close();
		String size = data.get("size");
		for (int i = 0; i < Integer.valueOf(size) - 1; i++) {
			data.remove("modular" + i);
		}
		data.replace("size", size, String.valueOf(patterns.size()));
		for (int i = 0; i < patterns.size(); i++) {
			data.put("modular" + (i + 1), patterns.get(i));
		}
		propertiesWritein(data, propertiesFile.getAbsolutePath());
	}
}
