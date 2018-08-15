package utils;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/12 0012.
 */
public class OperationCsv {

    private static Logger logger = Logger.getLogger(OperationCsv.class);

    /**
     * 从输入流中获取数据
     * @param fsDataInputStream
     * @param time
     * @return
     */
    public static List<String> getDataFromInputStream(FSDataInputStream fsDataInputStream,String time){
        List<String> data = new ArrayList<>();
        if(null==fsDataInputStream){
            return data;
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(fsDataInputStream,"UTF-8"));
            String line;
            while ((line=br.readLine()) != null){
                String[] arr = line.split(",");
                if("plantform_name".equals(arr[0])){
                    continue;
                }

                long t = CommonUtils.strDateToLong(arr[20]);

                if(t<=dealStrTime(time)){
                    continue;
                }
                logger.info("正在从流中读取数据：" + line);
				if (arr[16].equals("0")) {
					StringBuffer sb = new StringBuffer();
					sb.append(arr[0]).append(",").append(arr[1]).append(",").append(arr[2]).append(",").append(arr[3])
							.append(",").append(arr[4]).append(",").append(arr[5]).append(",").append("0000")
							.append(",").append("00").append(",").append("00").append(",").append(arr[15])
							.append(",").append(arr[12]).append(",").append(arr[7]).append(",").append(arr[21])
							.append(",").append(arr[10]).append(",").append(arr[16]).append(",").append(arr[17])
							.append(",").append("0");
					data.add(sb.toString());
				}else {
					String[] dateArr = arr[19].split(" ")[0].split("-");
					StringBuffer sb = new StringBuffer();
					sb.append(arr[0]).append(",").append(arr[1]).append(",").append(arr[2]).append(",").append(arr[3])
							.append(",").append(arr[4]).append(",").append(arr[5]).append(",").append(dateArr[0])
							.append(",").append(dateArr[1]).append(",").append(dateArr[2]).append(",").append(arr[15])
							.append(",").append(arr[12]).append(",").append(arr[7]).append(",").append(arr[21])
							.append(",").append(arr[10]).append(",").append(arr[16]).append(",").append(arr[17])
							.append(",").append("1");
					data.add(sb.toString());
				}
           }
            fsDataInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(Message.ERROR_READ_FILE,e);
        }finally {
            if(br!=null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    /**
     * 数据写入文件
     * @param fsDataOutputStream
     * @param data
     */
    public static void writeToCsv(FSDataOutputStream fsDataOutputStream,List<String> data){
        if(fsDataOutputStream!=null){
            BufferedWriter bw = null;
            try {
                bw = new BufferedWriter(new OutputStreamWriter(fsDataOutputStream,"UTF-8"));
                for (String str:data) {
                    logger.info("正在写入数据：" + str);
                    bw.write(str);
                    bw.newLine();
                }
                bw.flush();
                fsDataOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(Message.ERROR_WRITER_FILE,e);
            }finally {
                if(bw!=null){
                    try {
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    /**
     * 处理时间字符串，出现异常则为新的文件
     * @param time
     * @return
     */
    public static long dealStrTime(String time){
        try {
            return Long.valueOf(time);
        }catch (Exception e){
            return 0;
        }
    }

    public static void main(String[] args){

        String str = "as,v,c,,";
        str = str.replace("t","#@#");
        System.out.println(str);
    }

}
