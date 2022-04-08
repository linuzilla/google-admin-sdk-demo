package com.example.googleapidemo.config;

import com.example.googleapidemo.properties.GoogleWorkspaceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mac Liu (linuzilla@gmail.com)
 */
@Configuration
@EnableConfigurationProperties(GoogleWorkspaceProperties.class)
public class PropertiesConfig {
}
