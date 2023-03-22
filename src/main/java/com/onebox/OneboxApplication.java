package com.onebox;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.onebox.mapper.CartMapper;
import com.onebox.mapper.Mapper;

@SpringBootApplication
@Configuration
@EnableScheduling
public class OneboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(OneboxApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	public CartMapper wishlistMapper() {
		return new Mapper();
	}
}
