package com.salvaceloisma.tfg.controller.web;

import java.util.Arrays;

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

    // CARGAR LA VISTA DE USUARIOS
    @GetMapping("/gestionarUsuarios")
    public String gestionarUsuarios(
            ModelMap m, HttpSession session) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        //SI NO ESTAS LOGEADO Y CON EL ROL CORRECTO SALTA EXCEPCION
        if (usuario == null || (usuario.getRol() != RolUsuario.ADMIN && usuario.getRol() != RolUsuario.DIRECTOR)) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        }
        m.put("usuarios", usuarioService.findAll());
        m.put("view", "usuario/verUsuarios");
        return "_t/frame";
    }

    // VISTA DE USUARIOS
    @GetMapping("verUsuarios")
    public String verUsuarios(
            ModelMap m, HttpSession session) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        //SI NO ESTAS LOGEADO Y CON EL ROL CORRECTO SALTA EXCEPCION
        if (usuario == null || (usuario.getRol() != RolUsuario.ADMIN && usuario.getRol() != RolUsuario.DIRECTOR)) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        }
        m.put("usuarios", usuarioService.findAll());
        m.put("view", "usuario/verUsuarios");
        return "_t/frame";

    }

    // VISTA PARA CREAR USUARIOS NUEVOS
    @GetMapping("/crearUsuario")
    public String crearUsuario(
            ModelMap m, HttpSession session) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        //SI NO ESTAS LOGEADO Y CON EL ROL CORRECTO SALTA EXCEPCION
        if (usuario == null || (usuario.getRol() != RolUsuario.ADMIN && usuario.getRol() != RolUsuario.DIRECTOR)) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        }
        RolUsuario[] rolesSinDirector = Arrays.stream(RolUsuario.values())
                .filter(rol -> rol != RolUsuario.DIRECTOR)
                .toArray(RolUsuario[]::new);

        m.put("roles", rolesSinDirector);
        m.put("view", "usuario/crearUsuario");
        return "_t/frame";
    }

    // MODIFICACION DE LA BASE DE DATOS PARA CREAR UN USUARIO
    @PostMapping("/crearUsuario")
    public String crearUsuarioPost(
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("correo") String correo,
            @RequestParam("contrasenia") String contrasenia,
            @RequestParam("rol") RolUsuario rol, HttpSession s) throws DangerException, InfoException {

        try {
            correo = correo + "@educa.madrid.org";
            usuarioService.save(nombre, apellido, correo, contrasenia, rol);
        } catch (Exception e) {
            PRG.error("El usuario ya existe", "/usuario/crearUsuario");
        }
        PRG.info("Usuario creado correctamente.", "/usuario/crearUsuario");
        return "redirect:../";
    }

    // VISTA PARA ACTUALIZAR USUARIOS
    @GetMapping("actualizarUsuario")
    public String actualizarUsuario(
            @RequestParam("id") Long idUsuario, HttpSession session,
            ModelMap m) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        //SI NO ESTAS LOGEADO Y CON EL ROL CORRECTO SALTA EXCEPCION
        if (usuario == null || (usuario.getRol() != RolUsuario.ADMIN && usuario.getRol() != RolUsuario.DIRECTOR)) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        }
        m.put("usuario", usuarioService.findById(idUsuario));
        m.put("view", "usuario/actualizarUsuario");
        return "_t/frame";
    }

    // MODIFICACION DE LA BASE DE DATOS PARA ACTUALIZAR LOS DATOS DEL USUARIO
    @PostMapping("actualizarUsuario")
    public String actualizarUsuarioPost(
            @RequestParam("idUsuario") Long idUsuario,
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido, HttpSession s) throws DangerException, InfoException {
        try {
            usuarioService.update(idUsuario, nombre, apellido);
        } catch (Exception e) {
            PRG.error("El alumno no pudo ser actualizado", "/usuario/verUsuarios");
        }
        PRG.info("Usuario actualizado correctamente.", "/usuario/verUsuarios");
        return "redirect:/usuario/verUsuarios";
    }

    // MODIFICACION DE LA BASE DE DATOS PARA ELIMINAR AL USUARIO
    @PostMapping("borrarUsuario")
    public String borrarUsuario(
            @RequestParam("id") Long idUsuario) throws DangerException, InfoException {
        try {
            usuarioService.delete(idUsuario);
        } catch (Exception e) {
            PRG.error("No se puede borrar el alumno", "/usuario/verUsuarios");
        }
        PRG.info("Usuario eliminado correctamente.", "/usuario/verUsuarios");
        return "redirect:/usuario/verUsuarios";
    }
}
