//package com.example.BorrowBookService.config;
//
//import com.example.BorrowBookService.security.JwtAuthenticationInterceptor;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@RequiredArgsConstructor
//public class WebMvcConfig implements WebMvcConfigurer {
//    private final JwtAuthenticationInterceptor jwtAuthenticationInterceptor;
//
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(jwtAuthenticationInterceptor)
//                .addPathPatterns("/api/**")
//                .excludePathPatterns("/api/health", "/api/metrics");
//    }
//}