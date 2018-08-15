package utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Administrator on 2018/6/8 0008.
 */
public class CommonUtils {

    private static Logger logger = Logger.getLogger(CommonUtils.class);

    /**
     * 加载配置文件
     * @param type
     * @return
     */
    public static Properties loadProperties(int type) {
        String propName = getPropName(type);
        String path = System.getProperty("user.dir")+ File.separator + propName;
        Properties properties = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream(path);
            properties.load(is);
        } catch (Exception e) {
            logger.error(Message.UNFIND_FILE+"：["+propName+"]",e);
            e.printStackTrace();
        }
        return  properties;
    }

    /**
     * 加载不同的配置文件
     * @param type
     * @return
     */
    public static String getPropName(int type){
        String propName = "";
        switch (type) {
            case 1:
                propName = "hdfs.properties";
                break;
            default:
                break;
        }
        return propName;
    }


    /**
     * 获取路径中最后一个斜杠后面的名称或者是倒数第二个
     * @param path
     * @param type 1:获取倒数第一个，2：获取倒数第二个
     * @return
     */
    public static String getLastName(String path, int type){
        String str = null;
        if(StringUtils.isBlank(path)){
            return str;
        }
        String[] arr = path.split("/");
        if(type==1){
            str = arr[arr.length-1];
        }else if(type==2){
            str = arr[arr.length-2];
        }
        return str;
    }


    /**
     * 获取所有的文件中所有的时间存入map中
     * @return
     */
    public static Map<String,String> getAllLastTime(){
        Map<String,String> map = new HashMap<>();
        String path = System.getProperty("user.dir")+File.separator+"time.properties";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(path),"UTF-8"));
            String line;
            while((line=br.readLine())!=null){
                String[] split = line.split("=");
                map.put(split[0],split[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(Message.ERROR_FIND,e);
        }finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    /**
     * 将文件的修改时间写入配置
     * @param val
     */
    public static void writeLastTime(String val){
        String path = System.getProperty("user.dir")+File.separator+"time.properties";
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path,true),"UTF-8"));
            bw.write(val);
            bw.newLine();
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(Message.ERROR_WRITE,e);
        } finally {
            if(bw!=null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 字符串日期转long日期
     * @param date
     * @return
     * @throws ParseException
     */
    public static long strDateToLong(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        return sdf.parse(date).getTime();
    }

}
