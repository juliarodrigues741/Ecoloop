package com.ecoloop.controller;

import com.ecoloop.service.NivelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/nivel")
public class NivelController {

    @Autowired
    private NivelService nivelService;

    @GetMapping("/progresso/{id}")
    public List<String> calcularProgresso(@PathVariable int id) {
        return nivelService.calcularProgresso(id);
    }
}