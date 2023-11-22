package com.mercadolibro.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "frequent_question")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FrequentQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 1000)
    private String question;
    @Column(length = 10000)
    private String answer;
    @Column(name = "question_order")
    private Integer order;
}
