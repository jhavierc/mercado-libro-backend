package com.mercadolibro.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.mercadolibro.dto.TestDTO;
import com.mercadolibro.service.TestService;
import org.springframework.stereotype.Service;

public class TestServiceImpl implements TestService{

	@Override
	public Optional<List<TestDTO>> getAll() {
		List<TestDTO> list = new ArrayList<>();
		list.add(new TestDTO(1,"A"));
		list.add(new TestDTO(2,"B"));
		list.add(new TestDTO(3,"C"));
		return Optional.of(list);
	}

}
