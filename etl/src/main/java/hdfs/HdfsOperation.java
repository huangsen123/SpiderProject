package hdfs;


import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.fs.permission.FsAction;
import org.apache.hadoop.fs.permission.FsPermission;
import org.apache.log4j.Logger;
import utils.CommonUtils;
import utils.Message;
import utils.OperationCsv;

import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * Created by Administrator on 2018/6/12 0012.
 */
public class HdfsOperation {

    private static Logger logger = Logger.getLogger(HdfsOperation.class);

    private volatile Configuration configuration;

    //权限设置
    private FsPermission permission = new FsPermission(FsAction.ALL,FsAction.ALL,FsAction.ALL);

    private final String fs_defaultFS;
    private final String policy;
    private final String enable;
    private final String src_path;
    private final String dest_path;

    //存放所有的文件
    private Map<String,FileStatus> map = new HashMap<>();

    public HdfsOperation(){
        Properties properties = CommonUtils.loadProperties(1);
        this.fs_defaultFS = properties.getProperty("fs.defaultFS");
        this.policy = properties.getProperty("dfs.client.block.write.replace-datanode-on-failure.policy");
        this.enable = properties.getProperty("dfs.client.block.write.replace-datanode-on-failure.enable");
        this.src_path = properties.getProperty("srcpath");
        this.dest_path = properties.getProperty("destpath");
    }

    /**
     * 获取Configuration
     * @return
     */
    public Configuration getConfiguration(){
        if(null==configuration){
            synchronized (this){
                if(null==configuration){
                    configuration = new Configuration();
                    configuration.set("fs.defaultFS",fs_defaultFS);
                    configuration.set("dfs.client.block.write.replace-datanode-on-failure.policy",policy);
                    configuration.set("dfs.client.block.write.replace-datanode-on-failure.enable",enable);
                }
                return configuration;
            }
        }else{
            return configuration;
        }
    }

    /**
     * 获取FSDataOutputStream
     * @param path
     * @return
     */
    public FSDataOutputStream getFSDataOutputStream(String path){
        if(StringUtils.isBlank(path)){
            return null;
        }
        try {
            FileSystem fileSystem = FileSystem.get(URI.create(path), getConfiguration());
            return fileSystem.append(new Path(path));
        }catch (IOException e){
            e.printStackTrace();
            logger.error(Message.ERROR_METHOD+"["+this.getClass().getName()+"."+Thread.currentThread().getStackTrace()[1].getMethodName()+"]",e);
        }
        return null;
    }

    /**
     * 创建FSDataOutputStream
     * @return
     */
    public FSDataOutputStream createNewFSDataOutputStream(String path) {
        if(StringUtils.isBlank(path)){
            return null;
        }
        try {
            FileSystem fileSystem = FileSystem.get(URI.create(path), getConfiguration());
            return  fileSystem.create(new Path(path));
        }catch (IOException e){
            e.printStackTrace();
            logger.error(Message.ERROR_METHOD+"["+this.getClass().getName()+"."+Thread.currentThread().getStackTrace()[1].getMethodName()+"]",e);
        }
        return null;
    }

    /**
     * 获取输入流
     * @param path
     * @return
     */
    public FSDataInputStream getFSDataInputStream(String path){
        if(StringUtils.isBlank(path)){
            return null;
        }
        try {
            FileSystem fileSystem = FileSystem.get(URI.create(path),getConfiguration());

            return fileSystem.open(new Path(path));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(Message.ERROR_METHOD+"["+this.getClass().getName()+"."+Thread.currentThread().getStackTrace()[1].getMethodName()+"]",e);
        }
        return null;
    }

    /**
     * 获取所有的文件
     * @param path
     */
    public void getAllFiles(String path){
        try {
            FileSystem fileSystem = FileSystem.get(URI.create(path),getConfiguration());
            FileStatus[] fileStatuses = fileSystem.listStatus(new Path(path));
            for(FileStatus fileStatus:fileStatuses){
                if(fileStatus.isDirectory()){
                  String temp_path = fileStatus.getPath().toString().substring(fs_defaultFS.length());
                  getAllFiles(temp_path);
                }else{
                    map.put(CommonUtils.getLastName(fileStatus.getPath().toString(),1),fileStatus);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(Message.ERROR_METHOD+"["+this.getClass().getName()+"."+Thread.currentThread().getStackTrace()[1].getMethodName()+"]",e);
        }
    }

    /**
     * 获取已经修改或者新增的文件
     * @return
     */
    public Map<String,FileStatus> getModifyFile(){
        Map<String,FileStatus> modifyMap = new HashMap<>();
        getAllFiles(src_path);
        Map<String, String> mapTime = CommonUtils.getAllLastTime();
        for (Map.Entry<String,FileStatus> entry:map.entrySet()) {
            String name = entry.getKey();
            FileStatus fileStatus = entry.getValue();
            long modificationTime = fileStatus.getModificationTime();
            String val = name + "=" + modificationTime;
            if(mapTime.containsKey(name)){
                long lastTime = Long.valueOf(mapTime.get(name));
                if(modificationTime>lastTime){
                    modifyMap.put(lastTime+"",fileStatus);
                    CommonUtils.writeLastTime(val);
                }else {
                    continue;
                }
            }else{
                modifyMap.put(name,fileStatus);
                CommonUtils.writeLastTime(val);
            }
        }
        return modifyMap;
    }


    /**
     * 创建文件夹
     * @param dir
     */
    public void makeDir(String dir){
        dir = dest_path + "/" + dir;
        try {
            FileSystem fileSystem = FileSystem.get(URI.create(dir),getConfiguration());
            Path p = new Path(dir);
            if(!fileSystem.exists(p)){
                fileSystem.mkdirs(p, permission);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(Message.ERROR_METHOD+"["+this.getClass().getName()+"."+Thread.currentThread().getStackTrace()[1].getMethodName()+"]",e);
        }
    }

    /**
     * 获取输出流
     * @param path
     * @return
     */
    public FSDataOutputStream get_fsDataOutputStream(String path){

        FSDataOutputStream fsDataOutputStream = null;
        if(StringUtils.isBlank(path)){
            return fsDataOutputStream;
        }
        String dirName = CommonUtils.getLastName(path, 2);
        String fileName = CommonUtils.getLastName(path,1);
        makeDir(dirName);
        path = dest_path + "/" + dirName + "/" + fileName;
        try {
            FileSystem fileSystem = FileSystem.get(URI.create(path),getConfiguration());
            if(fileSystem.exists(new Path(path))){
                fsDataOutputStream = getFSDataOutputStream(path);
            }else{
                fsDataOutputStream = createNewFSDataOutputStream(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(Message.ERROR_METHOD+"["+this.getClass().getName()+"."+Thread.currentThread().getStackTrace()[1].getMethodName()+"]",e);
        }
        return fsDataOutputStream;
    }

    /**
     * 获取输入流中的数据
     * @param path
     * @param time
     * @return
     */
    public  List<String> getData(String path,String time){
        FSDataInputStream fsDataInputStream = getFSDataInputStream(path);
        return OperationCsv.getDataFromInputStream(fsDataInputStream, time);
    }

    /**
     * 开始数据处理过程
     */
    public void toEtl(){
        Map<String, FileStatus> modifyFile = getModifyFile();
        for (Map.Entry<String,FileStatus> entry: modifyFile.entrySet()) {
            String time = entry.getKey();
            FileStatus fileStatus = entry.getValue();
            String path = fileStatus.getPath().toString().substring(fs_defaultFS.length());
            List<String> data = getData(path, time);
            FSDataOutputStream fsDataOutputStream = get_fsDataOutputStream(path);
            OperationCsv.writeToCsv(fsDataOutputStream,data);
        }
    }

    public static void main(String[] args){
        HdfsOperation hdfsOperation = new HdfsOperation();
        hdfsOperation.toEtl();
        System.out.println("success");
    }


}
