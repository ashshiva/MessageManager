package com.project.messagemanager;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = "com.project.messagemanager")
public class Application {
	private static final long serialVersionUID = 374856068754635L;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}
	
//	@Bean
//	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//		return args -> {
//			String[] beanNames = ctx.getBeanDefinitionNames();
//			Arrays.sort(beanNames);
//			for(String beanName: beanNames)
//				System.out.println("Bean: "+beanName);
//		};
//		
//	}

}
