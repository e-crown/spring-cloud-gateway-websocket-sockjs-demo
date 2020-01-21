package com.mt.demo.gateway.cors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

import static org.springframework.web.cors.CorsConfiguration.ALL;

/**
 * 解决跨域问题
 *
 * @author: hanmeng
 * @since:
 **/
@Configuration
public class CorsConfig {

    @Value("${gateway.cors.allowOrigin.AllowedOrigins:*}")
    private String[] origins;

    @Value("${gateway.cors.allowOrigin.path:/**}")
    private String path;


    /**
     * 跨域设置
     * @return
     */
    @Bean
    public CorsWebFilter corsFilter() {
        final CorsConfiguration config = new CorsConfiguration();
        if (null != origins && 0 != origins.length) {
            for (String origin : origins) {
                config.addAllowedOrigin(origin);
            }
        }else {
            config.addAllowedOrigin(ALL);
        }
        config.setAllowCredentials(true);
        config.addAllowedHeader(ALL);
        config.addAllowedMethod(ALL);
        config.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        if (StringUtils.isBlank(path)) {
            path = "/**";
        }
        source.registerCorsConfiguration(path, config);
        return new CorsWebFilter(source);
    }
}
