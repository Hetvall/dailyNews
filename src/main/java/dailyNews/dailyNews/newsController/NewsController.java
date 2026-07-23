package dailyNews.dailyNews.newsController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class NewsController {

    private static final Logger log = LoggerFactory.getLogger(NewsController.class);

    @Value("${NEWS_API_KEY:${newsapi.key}}")
    private String newsApiKey;

    private final RestTemplate restTemplate;

    public NewsController() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(8000);
        this.restTemplate = new RestTemplate(requestFactory);
    }

    @GetMapping("/api/news")
    public ResponseEntity<?> getNews() {
        String url = "https://newsapi.org/v2/everything?q=news&sortBy=publishedAt&pageSize=11&apiKey=" + newsApiKey;
        return fetchNews(url);
    }

    @GetMapping("/api/news/{category}")
    public ResponseEntity<?> getNewsByCategory(@PathVariable String category) {
        String url = "https://newsapi.org/v2/top-headlines?category=" + category +
                "&pageSize=10&apiKey=" + newsApiKey;
        return fetchNews(url);
    }

    private ResponseEntity<?> fetchNews(String url) {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        } catch (RestClientResponseException ex) {
            // NewsAPI returned a non-2xx status (e.g. 401 invalid key, 426 upgrade required for prod use, 429 rate limit)
            log.error("NewsAPI returned {}: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of(
                    "error", "NewsAPI request failed",
                    "upstreamStatus", ex.getStatusCode().value(),
                    "upstreamBody", ex.getResponseBodyAsString()
            ));
        } catch (ResourceAccessException ex) {
            // Timeout or network failure reaching NewsAPI
            log.error("NewsAPI request timed out or was unreachable", ex);
            return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).body(Map.of(
                    "error", "NewsAPI request timed out or was unreachable",
                    "message", String.valueOf(ex.getMessage())
            ));
        }
    }
}
