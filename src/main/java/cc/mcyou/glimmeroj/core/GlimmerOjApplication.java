package cc.mcyou.glimmeroj.core;

import cc.mcyou.glimmeroj.core.service.SessionRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.core.JmsTemplate;

@SpringBootApplication
public class GlimmerOjApplication {


    public static void main(String[] args) {
        SpringApplication.run(GlimmerOjApplication.class, args);


    }

}
