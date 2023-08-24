package com.example.spotiktelegramservice;

import com.example.spotiktelegramservice.props.HibernateProps;
import com.example.spotiktelegramservice.props.MailProps;
import com.example.spotiktelegramservice.props.TelegramProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({TelegramProps.class, MailProps.class, HibernateProps.class})
public class SpotikTelegramServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpotikTelegramServiceApplication.class, args);
    }

}
