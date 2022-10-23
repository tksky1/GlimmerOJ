package cc.mcyou.glimmeroj.core.service;

import cc.mcyou.glimmeroj.core.dao.ProblemRepository;
import cc.mcyou.glimmeroj.core.data.JudgeData;
import cc.mcyou.glimmeroj.core.data.Session;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("prototype")
public class SessionRunnable implements Runnable{

    @Autowired
    JmsTemplate jms;
    @Autowired
    JudgeRunnable judgeRunnable;
    @Autowired
    ProblemRepository problemRepository;
    Thread thread;
    long beginTime;

    @Override
    public void run() {

        while(true){
            Session session = null;
            System.out.println("Waiting session in queue");
            while(session==null){
                try{
                    session = (Session) jms.receiveAndConvert("glimmer.oj.queue");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            List<JudgeData> data_list;
            String data = problemRepository.findById(session.getProblemId()).get().getData();
            data_list = JSON.parseArray(data,JudgeData.class);

            judgeRunnable.setJudgeRunnable(session,data_list,this);
            beginTime = System.currentTimeMillis();
            judgeRunnable.start();
            System.out.println("SessionRunnable: starting judging runnable");
            long runTime = 0;
            while(true){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                long nowTime = System.currentTimeMillis();
                if(judgeRunnable.done){
                    runTime = nowTime - beginTime;
                    break;
                }
                if(nowTime - beginTime>4000){
                    judgeRunnable.stat = 6;
                    System.out.println("TLE!");
                    judgeRunnable.setMessage("Time Limit Exceeded!");
                    judgeRunnable.upload();
                    judgeRunnable.destroy();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                }

            }
            System.out.println("Finish judging. Time Cost:"+runTime+"ms");

        }

    }

    public void start(){
        thread = new Thread(this,"JudgeSessionThread");
        thread.start();
    }

    public void setStartTime(Long time){
        beginTime = time;
    };
}
