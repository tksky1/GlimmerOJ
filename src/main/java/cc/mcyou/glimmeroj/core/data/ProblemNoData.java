package cc.mcyou.glimmeroj.core.data;

import cc.mcyou.glimmeroj.core.databaseEntity.ProblemEntity;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
public class ProblemNoData {
    public ProblemNoData(ProblemEntity entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.input = entity.getInput();
        this.output = entity.getOutput();
        this.in_sample = entity.getIn_sample();
        this.out_sample = entity.getOut_sample();
    }
    int id;
    String title;
    String content;
    String input;
    String output;
    String in_sample;
    String out_sample;

}