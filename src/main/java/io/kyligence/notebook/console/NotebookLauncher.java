package io.kyligence.notebook.console;


import io.kyligence.notebook.console.filter.UrlFilter;
import io.kyligence.saas.iam.sdk.filter.AuthenticationProcessingFilter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

import javax.servlet.MultipartConfigElement;
import java.io.File;

@SpringBootApplication
public class NotebookLauncher {

    public static void main(String[] args) {
        SpringApplication.run(NotebookLauncher.class);
    }

    @Bean
    public FilterRegistrationBean<UrlFilter> registerFilter() {
        FilterRegistrationBean<UrlFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new UrlFilter());
        registration.addUrlPatterns("/*");
        registration.setName("UrlFilter");
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean<AuthenticationProcessingFilter> authFilter() {
        FilterRegistrationBean<AuthenticationProcessingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new AuthenticationProcessingFilter());
        registration.addUrlPatterns("/*");
        registration.setName("AuthenticationProcessingFilter");
        registration.setOrder(2);
        return registration;
    }


    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String notebookHome = System.getProperty("NOTEBOOK_HOME");
        String tmpUploadLocation = notebookHome + "/tmp";
        File tmpUploadDir = new File(tmpUploadLocation);

        if (!tmpUploadDir.exists()) {
            tmpUploadDir.mkdirs();
        }
        factory.setLocation(tmpUploadLocation);
        return factory.createMultipartConfig();
    }

}
