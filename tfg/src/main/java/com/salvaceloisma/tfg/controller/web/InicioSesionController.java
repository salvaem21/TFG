package com.salvaceloisma.tfg.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.enumerados.RolUsuario;
import com.salvaceloisma.tfg.exception.DangerException;
import com.salvaceloisma.tfg.helper.PRG;
import com.salvaceloisma.tfg.service.AlumnoService;
import com.salvaceloisma.tfg.service.InicioSesionService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/inicioSesion")
@Controller
public class InicioSesionController {

    @Autowired
    private InicioSesionService inicioSesionService;

    @Autowired
    private AlumnoService alumnoService;

    @GetMapping("/inicioSesion")
    public String inicioSesion(
            ModelMap m) {
        m.put("view", "inicioSesion/inicioSesion");
        return "_t/frame";
    }

    @PostMapping("/inicioSesion")
    public String inicioSesionPost(
            @RequestParam("correo") String correo,
            @RequestParam("contrasenia") String contrasenia,
            HttpSession s,
            ModelMap m) throws DangerException {
        try {
            correo = correo + "@educa.madrid.org";
            Usuario usuario = inicioSesionService.inicioSesion(correo, contrasenia);

            s.setAttribute("usuario", usuario);
        } catch (Exception e) {
            PRG.error("Usuario o contraseña incorrectos");
        }

        return "redirect:../";
    }

    @GetMapping("/logout")
    public String logout(
            HttpSession s,
            ModelMap m) {
        s.setAttribute("usuario", null);
        s.invalidate();
        return "redirect:../";
    }

    @GetMapping("/crearUsuario")
    public String crearUsuario(
            ModelMap m) {
        m.put("view", "inicioSesion/crearUsuario");
        return "_t/frame";
    }

    @PostMapping("/crearUsuario")
    public String crearUsuarioPost(
            @RequestParam("nombre") String nombre,
            @RequestParam("correo") String correo,
            @RequestParam("contrasenia") String contrasenia,
            @RequestParam("dni") String dni,
            @RequestParam("rol") RolUsuario rol, HttpSession s) throws Exception {

        try {
            correo = correo + "@educa.madrid.org";
            inicioSesionService.save(nombre, correo, contrasenia, dni, rol);
        } catch (Exception e) {
            PRG.error("El usuario ya existe", "/inicioSesion/crearUsuario");
        }
        return "redirect:../";
    }

    @GetMapping("/gestionarAlumnos")
    public String gestionarAlumnos(
            ModelMap m) {
        m.put("alumnos", alumnoService.findAll());
        m.put("view", "alumno/r");
        return "_t/frame";
    }

    @GetMapping("/cambiarContrasenia")
    public String cambiarContrasenia(
            ModelMap m) {
        m.put("view", "inicioSesion/cambiarContrasenia");
        return "_t/frame";
    }

    @PostMapping("/cambiarContrasenia")
    public String cambiarContrasenia(
            @RequestParam("contraseniaActual") String contraseniaAntigua,
            @RequestParam("contraseniaNueva") String contraseniaNueva,
            HttpSession session,
            ModelMap modelMap) throws DangerException {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            inicioSesionService.cambiarContrasenia(usuario, contraseniaAntigua, contraseniaNueva);

        } catch (Exception e) {
            PRG.error("La contraseña no es valida", "/inicioSesion/cambiarContrasenia");
        }

        return "redirect:../";
    }
}
