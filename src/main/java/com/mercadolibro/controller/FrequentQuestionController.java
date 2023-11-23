package com.mercadolibro.controller;

import com.mercadolibro.dto.FrequentQuestionCreateDTO;
import com.mercadolibro.entity.FrequentQuestion;
import com.mercadolibro.exception.ResourceNotFoundException;
import com.mercadolibro.service.FrequentQuestionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question")
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH })
public class FrequentQuestionController {
    private final FrequentQuestionService frequentQuestionService;

    public FrequentQuestionController(FrequentQuestionService frequentQuestionService) {
        this.frequentQuestionService = frequentQuestionService;
    }

    @GetMapping
    public ResponseEntity<List<FrequentQuestion>> getAll() {
        return new ResponseEntity<>(frequentQuestionService.getAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<FrequentQuestion> create(@RequestBody FrequentQuestionCreateDTO frequentQuestion) {
        return new ResponseEntity<>(frequentQuestionService.create(frequentQuestion), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FrequentQuestion> findById(@PathVariable Integer id) throws ResourceNotFoundException {
        return new ResponseEntity<>(frequentQuestionService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws ResourceNotFoundException {
        frequentQuestionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FrequentQuestion> update(@PathVariable Integer id, @RequestBody FrequentQuestionCreateDTO frequentQuestion) throws ResourceNotFoundException {
        return new ResponseEntity<>(frequentQuestionService.update(id, frequentQuestion), HttpStatus.OK);
    }
}
