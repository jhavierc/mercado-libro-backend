package com.mercadolibro.service;

import java.util.List;
import java.util.Optional;

import com.mercadolibro.dto.TestDTO;

public interface TestService {
	
	public Optional<List<TestDTO>> getAll();

}
