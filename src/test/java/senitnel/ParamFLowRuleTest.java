package senitnel;

import org.springframework.web.client.RestTemplate;

public class ParamFLowRuleTest {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(String.class.getName());
        String[] uris = new String[]{
                "http://localhost:9090/testHot?param1=MRPlus",
                "http://localhost:9091/testHot?param1=other",
                "http://localhost:9092/testHot?param1=MRPlus"
        };
        for (int i = 0; i < 20; i++) {
            restTemplate.getForObject(uris[0],String.class);
        }
        for (int i = 0; i < 25; i++) {
            restTemplate.getForObject(uris[1],String.class);
        }
        for (int i = 0; i < 5; i++) {
            restTemplate.getForObject(uris[2],String.class);
        }
    }
}
