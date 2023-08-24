package com.example.spotiktelegramservice.props;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mail")
public record MailProps(String username, String password) {
}
