package dev.mathallama.config;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsCfg {

    private static final String[] LOCAL_DEV_ORIGINS = {
            "http://localhost",
            "http://localhost:80",
            "http://localhost:8080",
            "http://127.0.0.1",
            "http://127.0.0.1:80",
            "http://127.0.0.1:8080",
            "http://frontend"
    };

    @Bean
    public WebMvcConfigurer cors(@Value("${FRONTEND_ORIGIN:}") String originProp) {
        final Set<String> configuredOrigins = new LinkedHashSet<>();

        if (StringUtils.hasText(originProp)) {
            Arrays.stream(originProp.split(","))
                    .map(String::trim)
                    .filter(StringUtils::hasText)
                    .forEach(configuredOrigins::add);
        }

        configuredOrigins.addAll(Arrays.asList(LOCAL_DEV_ORIGINS));

        final String[] resolvedOrigins = configuredOrigins.isEmpty()
                ? new String[]{"http://localhost"}
                : configuredOrigins.toArray(String[]::new);

        final boolean usePatterns = Arrays.stream(resolvedOrigins).anyMatch(o -> o.contains("*"));

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                var registration = registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);

                if (usePatterns) {
                    registration.allowedOriginPatterns(resolvedOrigins);
                } else {
                    registration.allowedOrigins(resolvedOrigins);
                }
            }
        };
    }
}
