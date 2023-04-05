package com.dp.hloworld.controller;

import com.dp.hloworld.model.Language;
import com.dp.hloworld.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/language")
public class LanguageController {

    @Autowired
    LanguageRepository languageRepository;

    @PostMapping(value="/new")
    public void save(@RequestBody Language language) {
        languageRepository.save(language);
    }

    @GetMapping("/all")
    public List<Language> get(){
        return languageRepository.findAll();
    }
}
