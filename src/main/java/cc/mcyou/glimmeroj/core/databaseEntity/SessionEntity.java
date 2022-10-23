package cc.mcyou.glimmeroj.core.databaseEntity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "session")
public class SessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @NonNull
    int stat_id;
    @NonNull
    String judge_message;
    @NonNull
    int problemid;
    @NonNull
    int userid;
}
