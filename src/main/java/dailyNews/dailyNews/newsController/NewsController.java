package dailyNews.dailyNews.newsController;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class NewsController {

    @Value("${NEWS_API_KEY:${newsapi.key}}")
    private String newsApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/api/news")
    public ResponseEntity<?> getNews() {
        String url = "https://newsapi.org/v2/everything?q=news&sortBy=publishedAt&pageSize=11&apiKey=" + newsApiKey;

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }

    @GetMapping("/api/news/{category}")
    public ResponseEntity<?> getNewsByCategory(@PathVariable String category) {
        String url = "https://newsapi.org/v2/top-headlines?category=" + category +
        "&pageSize=10&apiKey=" + newsApiKey;
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

    }
}
