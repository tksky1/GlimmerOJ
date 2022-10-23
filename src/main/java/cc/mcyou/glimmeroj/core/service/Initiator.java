package cc.mcyou.glimmeroj.core.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
public class Initiator {

    @Autowired
    SessionRunnable sessionRunnable;

    public static HashMap<Integer, Integer> token2UserId = new HashMap<>();

    @PostConstruct
    public void init(){
        sessionRunnable.start();
        sessionRunnable.start();
        sessionRunnable.start();
    }


}
