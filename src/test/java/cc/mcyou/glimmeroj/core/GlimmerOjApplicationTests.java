package cc.mcyou.glimmeroj.core;

import cc.mcyou.glimmeroj.core.data.JudgeData;
import cc.mcyou.glimmeroj.core.data.Session;
import cc.mcyou.glimmeroj.core.service.JudgeRunnable;
import cc.mcyou.glimmeroj.core.service.SessionRunnable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SpringBootTest
class GlimmerOjApplicationTests {

    @Autowired
    SessionRunnable sessionRunnable;
    @Autowired
    JmsTemplate jmsTemplate;

    public static void main(String[] args){
        int a;
        Scanner scanner = new Scanner(System.in);
        a = scanner.nextInt();
        System.out.println(a);
    }

    @Test
    void contextLoads() throws Exception {

        sessionRunnable.start();
        List<JudgeData> list = new ArrayList<>();
        list.add(new JudgeData("1","1"));
        list.add(new JudgeData("2","2"));
        list.add(new JudgeData("0","0"));
        Session session = new Session(1,"import java.util.Scanner; public class Solution{ public static void main(String[] args){Scanner scanner = new Scanner(System.in);\n" +
                "        int a = scanner.nextInt(); while(true);\n" +
                "        }}",1);
        //JudgeRunnable judge = new JudgeRunnable(session,list );
        //judge.judge();
        jmsTemplate.convertAndSend("glimmer.oj.queue",session);

    }

}
