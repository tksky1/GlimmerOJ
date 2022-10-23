package cc.mcyou.glimmeroj.core.controller;

import cc.mcyou.glimmeroj.core.dao.UserRepository;
import cc.mcyou.glimmeroj.core.databaseEntity.UserEntity;
import cc.mcyou.glimmeroj.core.service.Initiator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Random;

@RestController
@RequestMapping("/user/")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/register")
    public Object register(@RequestParam("username")String username,@RequestParam("password") String password){
        System.out.println("getRegistration " +username);
        HashMap<String, Object> map = new HashMap<>();
        if(userRepository.findByUsername(username)!=null) {
            map.put("message", "用户名已被占用！");
        }else{
            map.put("message", "注册成功！");
            UserEntity user = new UserEntity(username,password);
            userRepository.save(user);
            int token = new Random().nextInt();
            int user_id = userRepository.findByUsername(username).getId();
            Initiator.token2UserId.put(token,user_id);
            map.put("token", token);
        }
        return map;
    }

    @PostMapping("/login")
    public Object login(@RequestParam String username,@RequestParam String password){
        System.out.println("Logging for "+username);
        UserEntity theUser = userRepository.findByUsername(username);
        HashMap<String, Object> response = new HashMap<>();
        if(theUser==null || !theUser.getPassword().equals(password)){
            response.put("success",false);
            response.put("message","用户名或密码错误！");
        }else{
            response.put("success",true);
            int token = new Random().nextInt();
            int user_id = theUser.getId();
            Initiator.token2UserId.put(token,user_id);
            response.put("token",token);
            response.put("message","登录成功！");
            response.put("isadmin",theUser.isIsadmin());
        }
        return response;
    }
}
