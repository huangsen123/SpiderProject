package utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
        String path = System.getProperty("user.dir")+ File.separator+propName;
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
                propName = "consumer.properties";
                break;
            case 2:
                propName = "common.properties";
                break;
            default:
                break;
        }
        return propName;
    }


    /**
     * 处理kafka中的数据
     * @param json
     * @return
     */
    public static Map<String,String> dealJsonStr(String json){
        Map<String,String> map = new HashMap<>();
        StringBuffer sb = new StringBuffer();
        String field = Message.CSV_HEADER;
        String[] fields = field.split(",");
        int length = fields.length;
        try {
            JSONObject jsonObject = JSON.parseObject(json);
            for(int i=0;i<length;i++){
                if(i==length-1){
                    sb.append(jsonObject.getString(fields[i]));
                }else {
                    sb.append(jsonObject.getString(fields[i])).append(",");
                }
            }
            String dirName = "0000-00-00";
            String orderTiem = jsonObject.getString("order_time");
            if (!StringUtils.isBlank(orderTiem)){
                dirName = orderTiem.split(" ")[0];
            }
            map.put(dirName,sb.toString());

        }catch (Exception e){
            e.printStackTrace();
            logger.error(Message.ERROR_JSON+"["+json+"]",e);
        }
        return map;
    }

}
