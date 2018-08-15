package utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/25 0025.
 */
public class FileOperation {

    /**
     * 创建文件夹，并返回当前文件夹的路径
     * @return
     */
    public static String mkDir(){
        String path = System.getProperty("user.dir") + File.separator + Message.DIRNAME;
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        return path;
    }

    /**
     * 每写一行进行换行操作
     * @param value
     * @param writer
     * @throws IOException
     */
    public static void writeRow(String value,BufferedWriter writer) throws IOException {
        writer.write(value);
        writer.newLine();
    }

    /**
     * 将数据写入文件
     * @param map
     * @throws Exception
     */
    public static void toFile(Map<String,String> map) throws Exception {
        for(Map.Entry<String,String> entry : map.entrySet()){
            String dirName = entry.getKey();
            String value = entry.getValue();
            String currentPath = mkDir();
            File file = new File(currentPath + File.separator + dirName + Message.SUFFIX);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true),"UTF-8"));
            if(file.length()==0){
                writeRow(Message.CSV_HEADER,bw);
            }
            writeRow(value,bw);
            bw.flush();
            bw.close();
        }
    }

    public static void main(String[] args){
        Map<String,String> map = new HashMap<>();
        map.put("2018-07-25","ss");
        try {
            toFile(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
