package com.example;

import notscanned.HttpSourceConfiguration;
import notscanned.TransformerConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.stream.aggregate.AggregateApplicationBuilder;
import org.springframework.cloud.stream.aggregate.SharedChannelRegistry;
import org.springframework.cloud.stream.app.http.source.HttpSource;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = HttpSource.class)
//@Import(AggregatorParentConfiguration.class)
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	@ConditionalOnMissingBean(SharedChannelRegistry.class)
	public SharedChannelRegistry sharedChannelRegistry() {
		return new SharedChannelRegistry();
	}

	@Bean
	CommandLineRunner applicationContext(ConfigurableApplicationContext parent) {
		return args -> new AggregateApplicationBuilder().parent(parent)
				.from(HttpSourceConfiguration.class)
				.to(TransformerConfiguration.class)
				.run(args);
	}
}
