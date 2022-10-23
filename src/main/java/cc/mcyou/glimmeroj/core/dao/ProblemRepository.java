package cc.mcyou.glimmeroj.core.dao;

import cc.mcyou.glimmeroj.core.databaseEntity.ProblemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemRepository extends JpaRepository<ProblemEntity, Integer> {

}
