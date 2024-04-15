package org.agaray.spring.pap2024.controller.rest;

import java.util.List;

import org.agaray.spring.pap2024.domain.Pais;
import org.agaray.spring.pap2024.service.PaisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/pais")
public class PaisRestController {
    
    @Autowired
    private PaisService paisService;

    @GetMapping("r")
    public List<Pais> r() {
        return paisService.findAll();
    }
}
