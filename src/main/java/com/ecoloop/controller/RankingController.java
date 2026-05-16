package com.ecoloop.controller;

import com.ecoloop.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ranking")
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping("/top10")
    public List<String> obterTop10() {
        return rankingService.obterTop10();
    }
}