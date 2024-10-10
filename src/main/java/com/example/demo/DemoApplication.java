package com.example.demo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@SpringBootApplication
@EnableAsync
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}

@RestController
class MyController {

	@GetMapping
	String home() {
		return "Hello";
	}
}

@Component
@RequiredArgsConstructor
class CustomFilter extends OncePerRequestFilter {

	private @NonNull final ApplicationEventPublisher applicationEventPublisher;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		this.applicationEventPublisher.publishEvent(new MyApplicationEvent("HTTP_REQUEST" , request.getRequestURI()));
		filterChain.doFilter(request, response);
	}
}

@Service
class MyApplicationModuleListener {

	@org.springframework.modulith.events.ApplicationModuleListener
	void on(MyApplicationEvent event) {
		System.out.println(event.message());
	}

}

record MyApplicationEvent(String type, String message) {}