package kz.aparking.authservice;

import com.nexmo.client.NexmoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NexmoConfiguration {

    @Value("${nexmo.api.key}")
    private String apiKey;

    @Value("${nexmo.api.secret}")
    private String apiSecret;

    @Bean
    public NexmoClient nexmoClient() {
        return NexmoClient.builder()
                .apiKey(apiKey)
                .apiSecret(apiSecret)
                .build();
    }
}
