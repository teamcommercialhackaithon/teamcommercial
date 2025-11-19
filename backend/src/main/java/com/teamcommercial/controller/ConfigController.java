package com.teamcommercial.controller;

import com.teamcommercial.entity.Config;
import com.teamcommercial.service.ConfigService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/configs")
@CrossOrigin(origins = "http://localhost:4200")
public class ConfigController {

    private final ConfigService configService;

    @Autowired
    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping
    public ResponseEntity<Page<Config>> getAllConfigs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(defaultValue = "configId") String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<Config> configs = configService.getAllConfigs(pageable);
        return ResponseEntity.ok(configs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Config> getConfigById(@PathVariable Long id) {
        return configService.getConfigById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/full-outage/{enabled}")
    public ResponseEntity<List<Config>> getConfigsByFullOutageNotification(@PathVariable Boolean enabled) {
        List<Config> configs = configService.getConfigsByFullOutageNotification(enabled);
        return ResponseEntity.ok(configs);
    }

    @GetMapping("/partial-outage/{enabled}")
    public ResponseEntity<List<Config>> getConfigsByPartialOutageNotification(@PathVariable Boolean enabled) {
        List<Config> configs = configService.getConfigsByPartialOutageNotification(enabled);
        return ResponseEntity.ok(configs);
    }

    @GetMapping("/start-stop/{enabled}")
    public ResponseEntity<List<Config>> getConfigsByStartStopNotification(@PathVariable Boolean enabled) {
        List<Config> configs = configService.getConfigsByStartStopNotification(enabled);
        return ResponseEntity.ok(configs);
    }

    @PostMapping
    public ResponseEntity<Config> createConfig(@Valid @RequestBody Config config) {
        Config createdConfig = configService.createConfig(config);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConfig);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Config> updateConfig(@PathVariable Long id, 
                                                @Valid @RequestBody Config config) {
        try {
            Config updatedConfig = configService.updateConfig(id, config);
            return ResponseEntity.ok(updatedConfig);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/toggle-full-outage")
    public ResponseEntity<Config> toggleFullOutageNotification(@PathVariable Long id) {
        try {
            Config config = configService.toggleFullOutageNotification(id);
            return ResponseEntity.ok(config);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/toggle-partial-outage")
    public ResponseEntity<Config> togglePartialOutageNotification(@PathVariable Long id) {
        try {
            Config config = configService.togglePartialOutageNotification(id);
            return ResponseEntity.ok(config);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/toggle-start-stop")
    public ResponseEntity<Config> toggleStartStopNotification(@PathVariable Long id) {
        try {
            Config config = configService.toggleStartStopNotification(id);
            return ResponseEntity.ok(config);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConfig(@PathVariable Long id) {
        try {
            configService.deleteConfig(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

