package com.ecoloop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConfiguracaoController {

    @GetMapping("/configuracoes")
    public String configuracoes() {
        return "configuracoes";
    }
}
