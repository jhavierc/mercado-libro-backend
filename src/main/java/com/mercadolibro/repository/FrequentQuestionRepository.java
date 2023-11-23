package com.mercadolibro.repository;

import com.mercadolibro.entity.FrequentQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FrequentQuestionRepository extends JpaRepository<FrequentQuestion, Integer> {
}
