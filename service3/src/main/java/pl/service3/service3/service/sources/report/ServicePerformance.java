package pl.service3.service3.service.sources.report;

import java.util.HashMap;

/**
 * Klasa przechowująca informacje o wydajność serwisu.
 * Czas odpowiedzi zbiera serwis wykonujący zapytania.
 */
public record ServicePerformance(HashMap<String, Double> cpu,
                                 HashMap<String, Double> memory,
                                 HashMap<Integer, RequestStat> answerTime) {}
