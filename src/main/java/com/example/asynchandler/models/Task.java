package com.example.asynchandler.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Data
public class Task {
    private String taskId;
    private String contentLocation;
    @JsonIgnore
    private boolean inProgress;
    @JsonIgnore
    private LocalDateTime startTime;
    @JsonIgnore
    private Object response;

    public Task(String taskId, String contentLocation, LocalDateTime startTime) {
        this.taskId = taskId;
        this.contentLocation = contentLocation;
        this.startTime = startTime;
    }

    public void start() {
        new Thread(() -> {
            try {
                getData();
                setInProgress(false);
            } catch (Exception e) {
                throw e;
            }
        }).start();
        setInProgress(true);
    }
    void getData() {
        String url = "https://api.instantwebtools.net/v1/passenger?size=2000";
        RestTemplate restTemplate = new RestTemplate();
        response = restTemplate.getForObject(url, Object.class);
    }

    public synchronized boolean isInProgress() {
        return inProgress;
    }

    public synchronized void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }
}
