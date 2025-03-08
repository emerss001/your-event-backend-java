package com.emersondev.yourEvent.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record NewEventRequest(String title, String location, Double price, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime, String email, String name) {
}
