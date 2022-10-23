package cc.mcyou.glimmeroj.core.dao;

import cc.mcyou.glimmeroj.core.databaseEntity.SessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Integer> {
    public List<SessionEntity> findAllByUserid(int user_id);

}
