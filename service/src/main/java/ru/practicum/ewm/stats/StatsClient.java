package ru.practicum.ewm.stats;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.exception.RemoteServerException;
import ru.practicum.ewm.stats.dto.StatsGetRequestDto;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
@Slf4j
public class StatsClient {

    public static void sendStatistics(String uriServer, String json) {
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        URI uri = URI.create(uriServer + "/hit");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RemoteServerException("Сервер статистики не отвечает");
        }
    }

    public static Long getViews(String uriServer, List<String> uris, Boolean unique) {
        java.net.http.HttpClient client = java.net.http.HttpClient.newBuilder().build();
        Gson gson = new GsonBuilder().create();
        URI uri = URI.create(uriServer + "/stats?" +
                "uris=" + uris.toString().substring(1, uris.toString().length() - 1) +
                "&unique=" + unique);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();
        String result;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            result = response.body();
        } catch (InterruptedException | IOException e) {
            throw new RemoteServerException("Сервер статистики не отвечает");
        }
        Type statisticDtoType = new TypeToken<List<StatsGetRequestDto>>() {
        }.getType();
        List<StatsGetRequestDto> statistic = gson.fromJson(result, statisticDtoType);
        long hits = 0;
        if (statistic != null && statistic.size() > 0) {
            hits =  statistic.get(0).getHits();
        }
        return hits;
    }
}
