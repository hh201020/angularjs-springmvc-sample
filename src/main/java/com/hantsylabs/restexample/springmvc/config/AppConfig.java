package com.hantsylabs.restexample.springmvc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.hantsylabs.restexample.springmvc.Constants;

@Configuration
@ComponentScan(
        basePackageClasses = {Constants.class},
        excludeFilters = {
            @Filter(
                    type = FilterType.ANNOTATION,
                    value = {
                        RestController.class,
                        ControllerAdvice.class,
                        Configuration.class
                    }
            )
        }
)
@PropertySource("classpath:/app.properties")
@PropertySource(value = "classpath:/database.properties", ignoreResourceNotFound = true)
public class AppConfig {

}
