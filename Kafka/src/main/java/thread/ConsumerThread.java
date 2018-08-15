package thread;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.apache.log4j.Logger;
import utils.CommonUtils;
import utils.FileOperation;
import utils.Message;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

/**
 * Created by Administrator on 2018/6/8 0008.
 */
public class ConsumerThread implements Runnable {

    private static Logger logger = Logger.getLogger(ConsumerThread.class);

    private KafkaStream<byte[],byte[]> kafkaStream;

    public ConsumerThread(KafkaStream<byte[],byte[]> stream){
        this.kafkaStream = stream;
    }

    @Override
    public void run() {
        ConsumerIterator<byte[], byte[]> it = kafkaStream.iterator();
        while (it.hasNext()){
            MessageAndMetadata<byte[], byte[]> next = it.next();
            String value = null;
            try {
                value = new String(next.message(),"UTF-8");
                FileOperation.toFile(CommonUtils.dealJsonStr(value));
                logger.info(MessageFormat.format("数据{0}写入完成",value));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                logger.error(Message.ERROR_ENCODING,e);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(MessageFormat.format("数据写入失败",value),e);
            }
        }
    }
}
