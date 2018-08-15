package task;

import hdfs.HdfsOperation;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2018/6/13 0013.
 */
public class ExcuteTimerTask {

    /**
     * 执行定时任务，立即执行，之后每隔3小时执行一次
     */
    public void excuteEtlTask(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                HdfsOperation hdfsOperation = new HdfsOperation();
                hdfsOperation.toEtl();
            }
        },0,1000*60*60*2);
    }

    public static void main(String[] args){
        ExcuteTimerTask task = new ExcuteTimerTask();
        task.excuteEtlTask();
    }
}
