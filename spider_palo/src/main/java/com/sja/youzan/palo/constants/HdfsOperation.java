package com.sja.youzan.palo.constants;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


public class HdfsOperation {
	private volatile Configuration configuration;



	private final String fs_defaultFS;
	private final String policy;
	private final String enable;
	private final String src_path;

	// 存放所有的文件
	private Map<String, FileStatus> map = new HashMap<>();

	public HdfsOperation() {
		Properties properties = CommonUtils.loadProperties(1);
		this.fs_defaultFS = properties.getProperty("fs.defaultFS");
		this.policy = properties.getProperty("dfs.client.block.write.replace-datanode-on-failure.policy");
		this.enable = properties.getProperty("dfs.client.block.write.replace-datanode-on-failure.enable");
		this.src_path = properties.getProperty("srcpath");
	}

	/**
	 * 获取Configuration
	 * 
	 * @return
	 */
	public Configuration getConfiguration() {
		if (null == configuration) {
			synchronized (this) {
				if (null == configuration) {
					configuration = new Configuration();
					configuration.set("fs.defaultFS", fs_defaultFS);
					configuration.set("dfs.client.block.write.replace-datanode-on-failure.policy", policy);
					configuration.set("dfs.client.block.write.replace-datanode-on-failure.enable", enable);
				}
				return configuration;
			}
		} else {
			return configuration;
		}
	}

	/**
	 * 获取所有的文件
	 * 
	 * @param path
	 */
	public void getAllFiles(String path) {
		try {
			FileSystem fileSystem = FileSystem.get(URI.create(path), getConfiguration());
			FileStatus[] fileStatuses = fileSystem.listStatus(new Path(path));
			for (FileStatus fileStatus : fileStatuses) {
				if (fileStatus.isDirectory()) {
					String temp_path = fileStatus.getPath().toString().substring(fs_defaultFS.length());
					getAllFiles(temp_path);
				} else {
					map.put(CommonUtils.getLastName(fileStatus.getPath().toString(), 1), fileStatus);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取已经修改或者新增的文件
	 * 
	 * @return
	 */
	public Map<String, FileStatus> getModifyFile() {
		Map<String, FileStatus> modifyMap = new HashMap<>();
		getAllFiles(src_path);
		Map<String, String> mapTime = CommonUtils.getAllLastTime();
		for (Map.Entry<String, FileStatus> entry : map.entrySet()) {
			String name = entry.getKey();
			FileStatus fileStatus = entry.getValue();
			long modificationTime = fileStatus.getModificationTime();
			String val = name + "=" + modificationTime;
			if (mapTime.containsKey(name)) {
				long lastTime = Long.valueOf(mapTime.get(name));
				if (modificationTime > lastTime) {
					modifyMap.put(lastTime + "", fileStatus);
					CommonUtils.writeLastTime(val);
				} else {
					continue;
				}
			} else {
				modifyMap.put(name, fileStatus);
				CommonUtils.writeLastTime(val);
			}
		}
		return modifyMap;
	}

	public static void main(String[] args) {
		HdfsOperation hdfsOperation = new HdfsOperation();
		Map<String, FileStatus> modifyMap = hdfsOperation.getModifyFile();
		Set<String> keySet = modifyMap.keySet();
		for (Iterator<String> it = keySet.iterator(); it.hasNext(); ) {
	     String key = it.next();
	     FileStatus value = modifyMap.get(key);
		System.out.println(value.getPath().toString());
		}
	}

}
