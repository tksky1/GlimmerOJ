package cc.mcyou.glimmeroj.core.controller;

import cc.mcyou.glimmeroj.core.dao.ProblemRepository;
import cc.mcyou.glimmeroj.core.dao.UserRepository;
import cc.mcyou.glimmeroj.core.data.JudgeData;
import cc.mcyou.glimmeroj.core.data.ProblemLite;
import cc.mcyou.glimmeroj.core.data.ProblemNoData;
import cc.mcyou.glimmeroj.core.databaseEntity.ProblemEntity;
import cc.mcyou.glimmeroj.core.service.Initiator;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class ProblemController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProblemRepository problemRepository;
    @PostMapping("/admin/addProblem")
    public Object addProblem(@RequestBody HashMap<String, Object> param){
        int token = (int) param.get("token");
        if(!Initiator.token2UserId.containsKey(token)){
            HashMap<String, Object> response = new HashMap<>();
            response.put("success",false);
            response.put("message","You dont have permission");
            return response;
        }
        int user_id = Initiator.token2UserId.get(token);

        HashMap<String, Object> response = new HashMap<>();

        if(userRepository.findById(user_id).get().isIsadmin()){
            HashMap<String,String> problem = (HashMap<String, String>) param.get("problem");
            ProblemEntity problemEntity = new ProblemEntity(problem.get("title"),problem.get("content"),
                    problem.get("input"),problem.get("output"),problem.get("in_sample"),problem.get("out_sample"),"");
            List<JudgeData> data = (List<JudgeData>) param.get("data");
            problemEntity.setData(JSON.toJSONString(data));
            problemRepository.save(problemEntity);
            response.put("success",true);
            response.put("message","success");
        }else{
            response.put("success",false);
            response.put("message","You dont have permission");
        }
        return response;
    }

    @GetMapping("/problems")
    public Object problems(@RequestParam int token){
        HashMap<String,Object> response = new HashMap<>();
        if(Initiator.token2UserId.containsKey(token)){
            List<ProblemEntity> problems_entity = problemRepository.findAll();
            List<ProblemLite> problems = new ArrayList<>();
            for(ProblemEntity entity: problems_entity) problems.add(new ProblemLite(entity));
            response.put("problems",problems);
            response.put("success",true);
            response.put("message","success");
        }else{
            response.put("success",false);
            response.put("message","token不正确！请重新登录！");
        }
        return response;
    }

    @GetMapping("/problem")
    public Object SingleProblem(@RequestParam int token, @RequestParam int id){
        HashMap<String,Object> response = new HashMap<>();
        if(Initiator.token2UserId.containsKey(token)){
            ProblemEntity problem_entity = problemRepository.findById(id).get();
            ProblemNoData problem = new ProblemNoData(problem_entity);

            response.put("problems",problem);
            response.put("success",true);
            response.put("message","success");
        }else{
            response.put("success",false);
            response.put("message","token不正确！请重新登录！");
        }
        return response;

    }

}
