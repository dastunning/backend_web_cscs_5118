package com.backend.components;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConnectionCountComponent {
    private final HikariDataSource hikariDataSource;

    @Scheduled(fixedRate = 300000) // Run every 5 minutes (300000 milliseconds)
    public void printConnectionCount() {
        log.info("Hikari getIdleConnections: " + hikariDataSource.getHikariPoolMXBean().getIdleConnections());
        log.info("Hikari getThreadsAwaitingConnection: " + hikariDataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        log.info("Hikari getTotalConnections: " + hikariDataSource.getHikariPoolMXBean().getTotalConnections());
        log.info("Hikari active connections: " + hikariDataSource.getHikariPoolMXBean().getActiveConnections());
    }

}
