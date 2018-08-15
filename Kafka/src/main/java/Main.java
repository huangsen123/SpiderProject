import service.Consumer;
import utils.CommonUtils;

import java.util.Properties;


/**
 * Created by Administrator on 2018/6/8 0008.
 */
public class Main {
    public static void main(String[] args){
        Properties prop = CommonUtils.loadProperties(2);
        Consumer consumer = new Consumer(prop.getProperty("topicName"),Integer.valueOf(prop.getProperty("threadNum")));
        consumer.startWork();
    }
}
