package cc.mcyou.glimmeroj.core.databaseEntity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name="problem")
public class ProblemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @NonNull
    String title;
    @NonNull
    String content;
    @NonNull
    String input;
    @NonNull
    String output;
    @NonNull
    String in_sample;
    @NonNull
    String out_sample;
    @NonNull
    private String data;

}
