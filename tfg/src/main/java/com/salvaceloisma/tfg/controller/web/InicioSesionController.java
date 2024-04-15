package com.salvaceloisma.tfg.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/inicioSesion")
@Controller
public class InicioSesionController {

    @GetMapping("/inicioSesion")
    public String inicioSesion(
        ModelMap m
    ) {
        return "inicioSesion/inicioSesion";
    }

    @GetMapping("/registrarse")
    public String registrarse(
        ModelMap m
    ) {
        return "inicioSesion/registrarse";
    }
	
}
