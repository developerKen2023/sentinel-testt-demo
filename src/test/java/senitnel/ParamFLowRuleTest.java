package senitnel;

import org.springframework.web.client.RestTemplate;

public class ParamFLowRuleTest {
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(String.class.getName());
        String[] uris = new String[]{
                "http://localhost:8040/order-service/testHot?param1=MRPlus",
                "http://localhost:8040/order-service/testHot?param1=other",
                "http://localhost:8040/order-service/testHot?param1=MRPlus"
        };
        for (int i = 0; i < 1; i++) {
            restTemplate.getForObject(uris[0],String.class);
        }
        for (int i = 0; i < 45; i++) {
            restTemplate.getForObject(uris[1],String.class);
        }
        for (int i = 0; i < 5; i++) {
            restTemplate.getForObject(uris[2],String.class);
        }
    }
}
