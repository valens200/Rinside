package com.example.demo.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.MappedSuperclass;
import java.util.HashMap;
import java.util.Map;


@Data
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class Controller {
    private Map<String, String> message = new HashMap<>();
}
