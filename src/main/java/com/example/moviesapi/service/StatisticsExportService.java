package com.example.moviesapi.service;

import com.example.moviesapi.dto.MovieStatistics;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Service
public class StatisticsExportService {

    private final StatisticsService statisticsService;
    private final ObjectMapper objectMapper;

    @Autowired
    public StatisticsExportService(StatisticsService statisticsService, ObjectMapper objectMapper) {
        this.statisticsService = statisticsService;
        this.objectMapper = objectMapper;
    }

    public byte[] exportToJson() throws IOException {
        MovieStatistics stats = statisticsService.generateStatistics();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(stats);
    }

    public byte[] exportToCsv() throws IOException {
        MovieStatistics stats = statisticsService.generateStatistics();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(output);

        // Write headers
        writer.println("Category,Metric,Value");

        // Write time-based statistics
        stats.getMoviesByYear().forEach((year, count) ->
                writer.println("Movies by Year," + year + "," + count));

        stats.getAverageDurationByYear().forEach((year, duration) ->
                writer.println("Average Duration by Year," + year + "," + duration));

        // Write genre statistics
        stats.getMoviesByGenre().forEach((genre, count) ->
                writer.println("Movies by Genre," + genre + "," + count));

        // Write actor statistics
        stats.getActorMovieCounts().forEach((actor, count) ->
                writer.println("Movies by Actor," + actor + "," + count));

        writer.flush();
        return output.toByteArray();
    }
}