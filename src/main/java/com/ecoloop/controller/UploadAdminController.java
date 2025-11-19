package com.ecoloop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UploadAdminController {

    @GetMapping("/uploads")
    public String uploads() {
        return "uploads";
    }
}
