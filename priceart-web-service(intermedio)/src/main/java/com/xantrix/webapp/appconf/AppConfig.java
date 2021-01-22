package com.xantrix.webapp.appconf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("application")
public class AppConfig {

	private String listino;
}
