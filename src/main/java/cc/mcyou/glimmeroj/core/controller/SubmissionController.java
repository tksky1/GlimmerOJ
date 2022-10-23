package cc.mcyou.glimmeroj.core.controller;

import cc.mcyou.glimmeroj.core.dao.SessionRepository;
import cc.mcyou.glimmeroj.core.data.Session;
import cc.mcyou.glimmeroj.core.databaseEntity.SessionEntity;
import cc.mcyou.glimmeroj.core.service.Initiator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/submit/")
public class SubmissionController {

    @Autowired
    JmsTemplate jmsTemplate;
    @Autowired
    SessionRepository sessionRepository;

    @PostMapping("/post")
    public Object submit(@RequestParam int token, @RequestParam int id, @RequestParam String code){
        HashMap<String,Object> response = new HashMap<>();
        if(Initiator.token2UserId.containsKey(token)){
            Session session = new Session(Initiator.token2UserId.get(token),code,id);
            jmsTemplate.convertAndSend("glimmer.oj.queue",session);
            response.put("success",true);
            response.put("message","success");
        }else{
            response.put("success",false);
            response.put("message","token无效，请重新登录！");
        }
        return response;
    }

    @GetMapping("sessions")
    public Object getAllSubmissions(@RequestParam int token){
        HashMap<String,Object> response = new HashMap<>();
        if(Initiator.token2UserId.containsKey(token)){
            int user_id = Initiator.token2UserId.get(token);
            List<SessionEntity> sessions = sessionRepository.findAllByUserid(user_id);
            response.put("sessions",sessions);
            response.put("success",true);
            response.put("message","success");
        }else{
            response.put("success",false);
            response.put("message","token无效，请重新登录！");
        }
        return response;

    }

}
