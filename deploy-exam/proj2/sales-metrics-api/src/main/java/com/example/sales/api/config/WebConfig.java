package com.example.sales.api.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(CorsProperties.class)
public class WebConfig implements WebMvcConfigurer {
    private final CorsProperties corsProperties;

    public WebConfig(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // /salesHistories以外のエンドポイントも含め、全てのエンドポイントにCORSを設定
        registry.addMapping("/**")
                .allowedOrigins(corsProperties.getAllowedOrigins().toArray(String[]::new))  // 許可するオリジン
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 許可するメソッド
                .allowedHeaders("*")  // 任意のヘッダーを許可
                .allowCredentials(true);  // クレデンシャル情報（例：クッキー）の送信を許可
    }
}