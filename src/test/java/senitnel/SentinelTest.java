package senitnel;

import org.springframework.web.client.RestTemplate;

public class SentinelTest {

    public static void main(String[] args) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        String[] uris = new String[]{
                "http://localhost:9090/justTestA",
                "http://localhost:9091/justTestA",
                "http://localhost:9092/justTestA"
        };
        for (int i = 0; i < 3; i++) {
            restTemplate.getForObject(uris[i],String.class);
        }
    }
}
