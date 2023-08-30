package com.example.asynchandler.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Status {
    @JsonProperty("X-Progress")
    private String progress;
    private String timeTaken;
}
