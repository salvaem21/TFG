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
import com.salvaceloisma.tfg.exception.InfoException;
import com.salvaceloisma.tfg.helper.PRG;
import com.salvaceloisma.tfg.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/usuario")
@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("r")
    public String r(
            ModelMap m, HttpSession session) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || (usuario.getRol() != RolUsuario.ADMIN && usuario.getRol() != RolUsuario.DIRECTOR)) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        }
        m.put("usuarios", usuarioService.findAll());
        m.put("view", "usuario/r");
        return "_t/frame";

    }

    @GetMapping("u")
    public String update(
            @RequestParam("id") Long idUsuario, HttpSession session,
            ModelMap m) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || (usuario.getRol() != RolUsuario.ADMIN && usuario.getRol() != RolUsuario.DIRECTOR)) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        }
        m.put("usuario", usuarioService.findById(idUsuario));
        m.put("view", "usuario/u");
        return "_t/frame";
    }

    @PostMapping("u")
    public String updatePost(
            @RequestParam("idUsuario") Long idUsuario,
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido, HttpSession s) throws DangerException, InfoException {
        try {
            usuarioService.update(idUsuario, nombre, apellido);
        } catch (Exception e) {
            PRG.error("El alumno no pudo ser actualizado", "/usuario/r");
        }
        PRG.info("Usuario actualizado correctamente.", "/usuario/r");
        return "redirect:/usuario/r";
    }

    @PostMapping("d")
    public String delete(
            @RequestParam("id") Long idUsuario) throws DangerException, InfoException {
        try {
            usuarioService.delete(idUsuario);
        } catch (Exception e) {
            PRG.error("No se puede borrar el alumno", "/usuario/r");
        }
        PRG.info("Usuario eliminado correctamente.", "/usuario/r");
        return "redirect:/usuario/r";
    }
}
