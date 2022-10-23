package cc.mcyou.glimmeroj.core.service;

import cc.mcyou.glimmeroj.core.data.JudgeData;
import cc.mcyou.glimmeroj.core.otherEntities.JdkNotFoundException;

import java.util.List;

public interface Judge {
    void onError(String message);
    void upload();
    void judge() throws Exception;
}
