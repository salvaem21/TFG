package com.salvaceloisma.tfg.controller.web;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.salvaceloisma.tfg.helper.PRG;
import com.salvaceloisma.tfg.service.inicioSesionService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/inicioSesion")
@Controller
public class InicioSesionController {

    @GetMapping("/inicioSesion")
    public String inicioSesion(
            ModelMap m) {
        m.put("view", "inicioSesion/inicioSesion");
        return "_t/frame";
    }

    @GetMapping("/registrarse")
    public String registrarse(
            ModelMap m) {
        m.put("view", "inicioSesion/registrarse");
        return "_t/frame";
    }

    @PostMapping("/registrarse")
    public String registrarsePost(
            @RequestParam("nombre") String nombre,
            @RequestParam("correo") String correo,
            @RequestParam("contrasenia") String contrasenia,
            @RequestParam("fechaNac") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaNac, HttpSession s) throws Exception {

        try {
            correo = correo + "@educa.madrid.org";
            inicioSesionService.save(nombre, correo, contrasenia, fechaNac);
        } catch (Exception e) {
            PRG.error("El usuario con el correo" + correo + " ya existe", "/inicioSesion/registrarse");
        }
        return "redirect:../";
    }

}
