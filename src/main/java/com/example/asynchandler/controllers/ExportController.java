package com.example.asynchandler.controllers;

import com.example.asynchandler.models.Status;
import com.example.asynchandler.models.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/")
@EnableAsync
public class ExportController {
    private Map<String, Task> taskMap = new HashMap<>();

    @Async
    @GetMapping("$export")
    public CompletableFuture<ResponseEntity<Task>> performTask() {
        String taskId = UUID.randomUUID().toString();
        String contentLocation = "http://localhost:8080/$export-poll-status/" + taskId;
        Task task = new Task(taskId, contentLocation, LocalDateTime.now());
        task.start();

        taskMap.put(taskId, task);
        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.ACCEPTED).body(task));
    }

    @GetMapping("$export-poll-status/{taskId}")
    public ResponseEntity<Object> checkJobStatus(@PathVariable String taskId) {
        if (taskMap.containsKey(taskId)) {
            Task task = taskMap.get(taskId);
            if (task.isInProgress()) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Export in progress");
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(task.getResponse());
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task with ID " + taskId + " not found");
        }
    }
}
