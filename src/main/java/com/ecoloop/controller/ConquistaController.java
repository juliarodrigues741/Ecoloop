package com.ecoloop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConquistaController {

    @GetMapping("/conquistas")
    public String conquistas() {
        return "conquistas";
    }
}
