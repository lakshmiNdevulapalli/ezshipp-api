package com.ezshipp.api.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Data
public class ApplicationPropertyConfig {

    private @Value("${google.apikey}")
    String googleApiKey;

}
