package cc.mcyou.glimmeroj.core.dao;

import cc.mcyou.glimmeroj.core.databaseEntity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    public UserEntity findByUsername(String username);


}
