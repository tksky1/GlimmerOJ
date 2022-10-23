package cc.mcyou.glimmeroj.core.databaseEntity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @NonNull
    String username;
    @NonNull
    String password;
    boolean isadmin;
}
