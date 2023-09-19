package KVServer;

import service.exception.RequestFailedException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String url;
    private String apiToken;
    private HttpClient client;

    public KVTaskClient(String host, int port) {
        url = host + port;
        apiToken = register(url);
        client = HttpClient.newHttpClient();
    }

    private String register(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/register"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new RequestFailedException("Регистрация провалилась!" + response.statusCode());
            }
        } catch (IOException | InterruptedException exception) {
            throw new RequestFailedException("Неверный запрос!");
        }
    }

    public void put(String key, String json) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken))
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RequestFailedException("Сохранение не удалось!" + response.statusCode());
            }
        } catch (IOException | InterruptedException exception) {
            throw new RequestFailedException("Неверный запрос!");
        }
    }

    public String load(String key) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new RequestFailedException("Загрузка не выполнена!" + response.statusCode());
            }
        } catch (IOException | InterruptedException exception) {
            throw new RequestFailedException("Неверный запрос!");
        }
    }
}
