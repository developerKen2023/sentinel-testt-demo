package senitnel;

import org.springframework.web.client.RestTemplate;

public class SentinelClusterTest {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String[] uris = new String[]{
                "http://localhost:9090/kenTest/aaa",
                "http://localhost:9091/kenTest/bbb",
                "http://localhost:9092/kenTest/ccc"
        };
//        for (int i = 0; i < 20; i++) {
//            restTemplate.getForObject(uris[0],String.class);
//        }
        for (int i = 0; i < 22; i++) {
            restTemplate.getForObject(uris[1],String.class);
        }
        for (int i = 0; i < 22; i++) {
            restTemplate.getForObject(uris[2],String.class);
        }
    }
}
