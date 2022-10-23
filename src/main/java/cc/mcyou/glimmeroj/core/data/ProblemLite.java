package cc.mcyou.glimmeroj.core.data;

import cc.mcyou.glimmeroj.core.databaseEntity.ProblemEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ProblemLite {
    public ProblemLite(ProblemEntity entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
    }

    int id;
    String title;
}
