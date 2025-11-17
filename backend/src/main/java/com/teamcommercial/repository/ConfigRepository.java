package com.teamcommercial.repository;

import com.teamcommercial.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {
    
    List<Config> findByEnableFullOutageNotification(Boolean enabled);
    
    List<Config> findByPartialOutageNotification(Boolean enabled);
    
    List<Config> findByStartStopNotification(Boolean enabled);
    
    List<Config> findByWaitTimeGreaterThan(Integer waitTime);
    
    List<Config> findByWaitTimeLessThan(Integer waitTime);
}

