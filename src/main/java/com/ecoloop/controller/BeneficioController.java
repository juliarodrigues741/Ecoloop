package com.ecoloop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BeneficioController {

    @GetMapping("/beneficios")
    public String beneficios() {
        return "beneficios";
    }
}
