package com.mercadolibro.controllers;

import java.util.List;

import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/api/healt")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET })
public class HealtChectkController {
	
	@Autowired
	private TestService testService;

	@ApiOperation(value = "Health check", notes = "Health check")
	@GetMapping("/ping")
	public ResponseEntity<String> healtCheck() {
		return ResponseEntity.status(HttpStatus.OK).body("pong");
	}

	@ApiOperation(value = "Test list", notes = "This is a test service")
	@GetMapping("/test")
	public ResponseEntity<List<TestDTO>> listTest() {
		return ResponseEntity.status(HttpStatus.OK).body(testService.getAll().get());
	}

}
