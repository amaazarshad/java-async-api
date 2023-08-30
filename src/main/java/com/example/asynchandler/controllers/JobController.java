package com.example.asynchandler.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/job")
@EnableAsync
public class JobController {

    private boolean isTaskInProgress = false;

    @Async
    @GetMapping
    public CompletableFuture<ResponseEntity<String>> performTask() {
        isTaskInProgress = true;

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(15); // Simulate a 20-second delay
                isTaskInProgress = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.ACCEPTED).body("Task started"));
    }

    @GetMapping("/status")
    public ResponseEntity<String> checkJobStatus() {
        if (isTaskInProgress) {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Task is in progress");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Task has completed");
        }
    }
}
