package com.dev.project.Component;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoggingFilter implements Filter {
	@Override
	public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (request instanceof HttpServletRequest && response instanceof HttpServletResponse) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			HttpServletResponse httpResponse = (HttpServletResponse) response;

			long startTime = System.currentTimeMillis();

			// Log request details
			System.out.println("Incoming Request: " + httpRequest.getMethod() + " " + httpRequest.getRequestURI());

			chain.doFilter(request, response);

			long duration = System.currentTimeMillis() - startTime;

			// Log response details
			System.out.println("Outgoing Response: " + httpResponse.getStatus() + " | Time Taken: " + duration + "ms");
		} else {
			chain.doFilter(request, response);
		}
	}
}
