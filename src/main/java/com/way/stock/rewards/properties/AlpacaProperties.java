package com.way.stock.rewards.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.way.util.propertiesutil.Properties;

@Configuration
@PropertySource("classpath:config/config-${spring.profiles.active:local}.properties")
@ConfigurationProperties(prefix = "way.alpaca")
public class AlpacaProperties extends Properties {

	private String apiKey;
	private String url;

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
