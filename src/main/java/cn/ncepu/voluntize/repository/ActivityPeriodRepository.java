package cn.ncepu.voluntize.repository;

import cn.ncepu.voluntize.entity.ActivityPeriod;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ActivityPeriodRepository extends JpaRepository<ActivityPeriod, String> {
}
