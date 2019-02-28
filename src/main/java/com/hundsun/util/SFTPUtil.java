package com.hundsun.util;

import org.springframework.stereotype.Component;

import com.hundsun.entity.Connector;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.io.IOUtils;

/**
 * Ftp连接工具
 * 
 * @Title: SFTPUtil.java
 * @Package:com.hundsun.util
 * @author:Wanggk
 * @date:2018年11月19日
 * @version:V1.0
 */
@Component
public class SFTPUtil {
	private ChannelSftp sftp;

	private Session session;

	public SFTPUtil() {
	}

	/**
	 * 连接Ftp
	 * 
	 * @param:@param connector
	 * @param:@return
	 * @return:boolean
	 * @author:wanggk
	 * @date:2018年11月19日
	 * @version:V1.0
	 */
	public boolean login(Connector connector) {
		String address = connector.getAddress();
		int port = connector.getPort();
		String username = connector.getUsername();
		String password = connector.getPassword();
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(username, address, port);
			session.setPassword(password);
			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			return true;
		} catch (JSchException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 关闭连接
	 * 
	 * @param:
	 * @return:void
	 * @author:wanggk
	 * @date:2018年11月19日
	 * @version:V1.0
	 */
	public void logout() {
		if (sftp != null) {
			if (sftp.isConnected()) {
				sftp.disconnect();
			}
		}
		if (session != null) {
			if (session.isConnected()) {
				session.disconnect();
			}
		}
	}
	/**
	 * 将输入流的数据上传到sftp作为文件。文件完整路径=basePath+directory
	 * 
	 * @param:@param basePath服务器的基础路径
	 * @param:@param directory上传到该目录
	 * @param:@param sftpFileNamesftp端文件名
	 * @param:@param input输入流
	 * @param:@throws SftpException
	 * @return:void
	 * @author:wanggk
	 * @date:2018年11月19日
	 * @version:V1.0
	 */
	/*
	 * public void upload(String basePath, String directory, String sftpFileName,
	 * InputStream input) throws SftpException { try { sftp.cd(basePath);
	 * sftp.cd(directory); } catch (SftpException e) { // 目录不存在，则创建文件夹 String[] dirs
	 * = directory.split("/"); String tempPath = basePath; for (String dir : dirs) {
	 * if (null == dir || "".equals(dir)) continue; tempPath += "/" + dir; try {
	 * sftp.cd(tempPath); } catch (SftpException ex) { sftp.mkdir(tempPath);
	 * sftp.cd(tempPath); } } } sftp.put(input, sftpFileName); // 上传文件 }
	 */

	/**
	 * 下载文件
	 * 
	 * @param:@param directory下载目录
	 * @param:@param downloadFile下载的文件名
	 * @param:@return字节数组
	 * @param:@throws SftpException
	 * @param:@throws IOException
	 * @return:byte[]
	 * @author:wanggk
	 * @date:2018年11月19日
	 * @version:V1.0
	 */
	public byte[] download(String directory, String downloadFile) throws SftpException, IOException {
		if (directory != null && !"".equals(directory)) {
			sftp.cd(directory);
		}
		InputStream is = sftp.get(downloadFile);
		byte[] fileData = IOUtils.toByteArray(is);
		return fileData;
	}

	/**
	 * 删除文件
	 * 
	 * @param:@param directory 要删除文件所在目录
	 * @param:@param deleteFile 要删除的文件
	 * @param:@throws SftpException
	 * @return:void
	 * @author:wanggk
	 * @date:2018年11月19日
	 * @version:V1.0
	 */
	/*
	 * public void delete(String directory, String deleteFile) throws SftpException
	 * { sftp.cd(directory); sftp.rm(deleteFile); }
	 */
	/**
	 * 列出目录下的文件
	 * 
	 * @param:@param directory要列出的目录
	 * @param:@return
	 * @param:@throws SftpException
	 * @return:Vector<?>
	 * @author:wanggk
	 * @date:2018年11月19日
	 * @version:V1.0
	 */
	public Vector<LsEntry> listFiles(String directory) throws SftpException {
		return sftp.ls(directory);
	}

	/**
	 * 下载文件
	 * 
	 * @param:@param directory
	 * @param:@param downloadFile
	 * @param:@param saveFile
	 * @param:@return
	 * @return:File
	 * @author:wanggk
	 * @date:2018年11月23日
	 * @version:V1.0
	 */
	public File download(String directory, String downloadFile, String saveFile) {
		try {
			sftp.cd(directory);
			File file = new File(saveFile);
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			sftp.get(downloadFile, fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
			return file;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 下载目录
	 * 
	 * @param:@param root FTP的目录/FA6.0/Business/20181105_回购/C客户端/bin
	 * @param:@param localpath 本地保存目录D:/test/Instance
	 * @return:void
	 * @author:wanggk
	 * @date:2018年11月23日
	 * @version:V1.0
	 * @throws SftpException
	 */
	public List<String> getPath(String root) throws SftpException {
		List<String> paths = new ArrayList<>();
		List<String> addpaths = new ArrayList<>();
		List<String> delepaths = new ArrayList<>();
		List<String> allpaths = new ArrayList<>();
		paths.add(root);
		while (paths.size() > 0) {
			for (String subpath : paths) {
				Vector<LsEntry> listFiles = listFiles(subpath);
				for (LsEntry item : listFiles) {
					String longname = item.getLongname();
					String[] attrs = longname.split(" ");
					String filetype = attrs[0].substring(0, 1);
					String path = item.getFilename();
					if (filetype.equals("d")) {
						addpaths.add(subpath + "/" + path);
					} else {
						allpaths.add(subpath + "/" + path);
					}
					delepaths.add(subpath);
				}
			}
			paths.clear();
			for (int i = 0; i < addpaths.size(); i++) {
				String addpath = addpaths.get(i);
				paths.add(addpath);
			}
			addpaths.clear();
			delepaths.clear();
		}
		return allpaths;
	}

	/**
	 * 下载单个文件
	 * 
	 * @param:@param path
	 * @param:@param localpath
	 * @param:@return
	 * @return:String
	 * @author:wanggk
	 * @date:2018年12月5日
	 * @version:V1.0
	 */
	public String downfile(String path, String localpath) {
		File file1 = new File(localpath);
		if (!file1.exists()) {
			file1.mkdirs();
		}
		String finalpath = "";
		String file = path;// allpaths.get(i);
		if (file == null) {
			return "stop";
		}
		String[] filepaths = file.split("\\/");
		for (int j = 1; j < filepaths.length - 1; j++) {
			finalpath += "/" + filepaths[j];
		}
		String sublocalpath = "";
		for (int j = 4; j < filepaths.length - 1; j++) {
			sublocalpath += "/" + filepaths[j];
		}
		File file2 = new File(localpath + sublocalpath);
		if (!file2.exists()) {
			file2.mkdirs();
		}
		File download = download(finalpath, filepaths[filepaths.length - 1],
				localpath + sublocalpath + "/" + filepaths[filepaths.length - 1]);
		if (download == null) {
			return "error";
		}
		return "success";
	}
}
