package com.teamcommercial.service;

import com.teamcommercial.entity.Config;
import com.teamcommercial.repository.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ConfigService {

    private final ConfigRepository configRepository;

    @Autowired
    public ConfigService(ConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    public List<Config> getAllConfigs() {
        return configRepository.findAll();
    }

    public Optional<Config> getConfigById(Long id) {
        return configRepository.findById(id);
    }

    public List<Config> getConfigsByFullOutageNotification(Boolean enabled) {
        return configRepository.findByEnableFullOutageNotification(enabled);
    }

    public List<Config> getConfigsByPartialOutageNotification(Boolean enabled) {
        return configRepository.findByPartialOutageNotification(enabled);
    }

    public List<Config> getConfigsByStartStopNotification(Boolean enabled) {
        return configRepository.findByStartStopNotification(enabled);
    }

    public Config createConfig(Config config) {
        return configRepository.save(config);
    }

    public Config updateConfig(Long id, Config configDetails) {
        Config config = configRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Config not found with id: " + id));
        
        config.setWaitTime(configDetails.getWaitTime());
        config.setEnableFullOutageNotification(configDetails.getEnableFullOutageNotification());
        config.setPartialOutageNotification(configDetails.getPartialOutageNotification());
        config.setStartStopNotification(configDetails.getStartStopNotification());
        
        return configRepository.save(config);
    }

    public void deleteConfig(Long id) {
        Config config = configRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Config not found with id: " + id));
        configRepository.delete(config);
    }

    public Config toggleFullOutageNotification(Long id) {
        Config config = configRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Config not found with id: " + id));
        config.setEnableFullOutageNotification(!config.getEnableFullOutageNotification());
        return configRepository.save(config);
    }

    public Config togglePartialOutageNotification(Long id) {
        Config config = configRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Config not found with id: " + id));
        config.setPartialOutageNotification(!config.getPartialOutageNotification());
        return configRepository.save(config);
    }

    public Config toggleStartStopNotification(Long id) {
        Config config = configRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Config not found with id: " + id));
        config.setStartStopNotification(!config.getStartStopNotification());
        return configRepository.save(config);
    }
}

