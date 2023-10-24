package com.mercadolibro.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mercadolibro.dto.TestDTO;
import com.mercadolibro.service.TestService;

@RestController
@RequestMapping("/api/health")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET })
public class HealthCheckController {
	
	@Autowired
	private TestService testService;


	@GetMapping("/ping")
	public ResponseEntity<String> healthCheck() {
		return ResponseEntity.status(HttpStatus.OK).body("pong");
	}
	
	@GetMapping("/test")
	public ResponseEntity<List<TestDTO>> listTest() {
		return ResponseEntity.status(HttpStatus.OK).body(testService.getAll().get());
	}

}
