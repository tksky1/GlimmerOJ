package cc.mcyou.glimmeroj.core.service;

import cc.mcyou.glimmeroj.core.dao.SessionRepository;
import cc.mcyou.glimmeroj.core.data.JudgeData;
import cc.mcyou.glimmeroj.core.data.Session;
import cc.mcyou.glimmeroj.core.databaseEntity.SessionEntity;
import cc.mcyou.glimmeroj.core.otherEntities.JdkNotFoundException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Service
@Scope("prototype")
@NoArgsConstructor
public class JudgeRunnable implements Runnable,Judge{

    private String workdir;
    private String className;
    private String message;
    public int stat;

    private int userId;
    private String code;
    private int problemId;
    private List<JudgeData> data_list;

    private boolean stop = false;
    public boolean done = false;
    private SessionRunnable sessionRunnable;
    private Thread thread;
    @Autowired
    SessionRepository sessionRepository;

    public void setJudgeRunnable(Session session, List<JudgeData> data_list, SessionRunnable sessionRunnable){
        workdir = System.getProperty("user.dir");
        className = workdir + "\\Solution.java";
        stat = 1;
        this.userId = session.getUserId();
        this.code = session.getCode();
        this.problemId = session.getProblemId();
        this.data_list = data_list;
        this.sessionRunnable = sessionRunnable;
    }

    @Override
    public void onError(String message) {
        stat = 6;
        setMessage(message);
    }


    public void upload(){
        SessionEntity entity= new SessionEntity();
        entity.setJudge_message(message);
        entity.setProblemid(problemId);
        entity.setStat_id(stat);
        entity.setUserid(userId);
        sessionRepository.save(entity);
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void destroy(){
        thread.interrupt();
        stop = true;
        stat = 5;
        setMessage("Time Limit Exceeded!");
    }

    @Override
    public void judge() throws IOException, JdkNotFoundException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if(compiler==null) throw new JdkNotFoundException();
        System.out.println(className);
        Files.writeString(Paths.get(className),code);
        int compileResult = -1;
        try{
            compileResult = compiler.run(null,null,null,className);
        }catch (Exception e){
            stat = 4;
            setMessage("Compile Error! Error Message: "+e.getMessage());
        }

        System.out.println("编译结果代码："+compileResult);
        if (compileResult == 0) {
            sessionRunnable.beginTime = System.currentTimeMillis();
            boolean ac = true;
            int testCase = 0;

            for(JudgeData data: data_list){
                testCase++;
                System.out.println("测试点"+testCase);
                Runtime run = Runtime.getRuntime();
                try{
                    Process process = run.exec("java -cp "+ workdir +" Solution");
                    InputStream errorStream = process.getErrorStream();
                    OutputStream os = process.getOutputStream();
                    os.write(data.getInput().getBytes());
                    os.close();
                    if(errorStream.read()!=-1){
                        stat = 6;
                        onError("Runtime Error！message: e"+ new String(errorStream.readAllBytes()));
                        ac = false;
                        errorStream.close();
                        break;
                    }
                    InputStream is = process.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    StringBuilder ans = new StringBuilder();
                    String now = "";

                    while ((now = br.readLine()) != null) {
                        ans.append(now).append('\n');
                    }
                    br.close();is.close();
                    String output = ans.toString();
                    String correct_ans = data.getOutput();

                    while(output.endsWith("\n")|| output.endsWith(" ")){
                        output = output.substring(0, output.length() - 1);
                    }

                    System.out.println("TaskCase "+testCase);
                    System.out.println("输出:"+output);
                    System.out.println("答案:"+correct_ans);
                    System.out.println("---");
                    if(!correct_ans.equals(output)){
                        ac = false;
                        stat = 3;
                        setMessage("Wrong Answer on Testcase "+testCase);
                        break;
                    }
                    is.close();
                }catch (Exception e){
                    stat = 6;
                    onError("Runtime Error！message: "+e.getMessage());
                    break;
                }
                if(stop){
                    ac = false;
                    stat = 5;
                    setMessage("Time Limit Exceeded!");
                    break;
                }

            }
            if(ac){
                stat = 2;
                setMessage("Accepted!");
            }

        }else{
            stat = 4;
            setMessage("编译错误！请检查主类是否为Solution，以及程序是否符合语法！");
        }

        done = true;
        System.out.printf("评测结果：%d-%s\n",stat,message);
        upload();

    }

    @Override
    public void run() {
        try {
            judge();
        } catch (IOException | JdkNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(){
        thread = new Thread(this,"JudgeThread");
        thread.start();
    }
}
