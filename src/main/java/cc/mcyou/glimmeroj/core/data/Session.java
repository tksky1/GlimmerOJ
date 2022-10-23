package cc.mcyou.glimmeroj.core.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class Session implements Serializable {
    private int userId;
    private String code;
    private int problemId;
}
