package com.oreily.restclient.services;

import com.oreily.restclient.json.JokeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class JokeService {

    private RestTemplate restTemplate;

    @Autowired
    public JokeService(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }

    public String getJokeSync(String first, String last) {
        String base = "https://api.icndb.com/jokes/random?limitTo=[nerdy]";
        String url = String.format("%s&firstName=%s&lastName=%s", base, first, last);
        return restTemplate.getForObject(base, JokeResponse.class).getValue().getJoke();
    }

    public Mono<String> getJokeAsync(String first, String last) {
        WebClient client = WebClient.create("https://api.icndb.com");
        String path = "/jokes/random?limitTo=[nerdy]&firstName={first}&lastName={last}";
        return client.get()
                .uri(path, first, last)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JokeResponse.class)
                .map(jokeResponse -> jokeResponse.getValue().getJoke());
    }
}
