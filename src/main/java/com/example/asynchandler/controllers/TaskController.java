package com.example.asynchandler.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/task")
@EnableAsync
public class TaskController {

    private boolean isTaskInProgress = false;

    @Async
    @GetMapping
    public CompletableFuture<ResponseEntity<String>> performTask() {
        if (isTaskInProgress) {
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.ACCEPTED).body("Task is in progress"));
        }

        isTaskInProgress = true;

        CompletableFuture.runAsync(() -> {
            // Execute your task here asynchronously
            // For simulation purposes, let's sleep for 20 seconds
            try {
                Thread.sleep(10000);
                isTaskInProgress = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Task completed, no need to update isTaskInProgress as this method is asynchronous
        });

        if(!isTaskInProgress){
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK).body("Task has completed"));
        }
        else{
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.ACCEPTED).body("Task has started its execution"));
        }

    }
}
