package com.salvaceloisma.tfg.controller.web;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.salvaceloisma.tfg.domain.Mensaje;
import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.exception.DangerException;
import com.salvaceloisma.tfg.exception.InfoException;
import com.salvaceloisma.tfg.helper.PRG;
import com.salvaceloisma.tfg.service.InicioSesionService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/inicioSesion")
@Controller
public class InicioSesionController {

    @Autowired
    private InicioSesionService inicioSesionService;

    //VISTA DE INICIO DE SESION
    @GetMapping("/inicioSesion")
    public String inicioSesion(
            ModelMap m, HttpSession session) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // SI NO ESTAS LOGEADO Y CON EL ROL CORRECTO SALTA EXCEPCION
        if (usuario != null) {
            PRG.error("Ya tienes una sesion iniciada. Cierra sesion y vuelve.");
        }
        m.put("view", "inicioSesion/inicioSesion");
        return "_t/frame";
    }

    //COMPRUEBA SI EL CORREO Y CONTRASEÑA ESTAN RELACIONADOS ENTRE SI Y CREA UNA SESION
    @PostMapping("/inicioSesion")
    public String inicioSesionPost(
            @RequestParam("correo") String correo,
            @RequestParam("contrasenia") String contrasenia,
            HttpSession s,
            ModelMap m) throws DangerException {
        try {
            correo = correo + "@educa.madrid.org";
            Usuario usuario = inicioSesionService.inicioSesion(correo, contrasenia);
            List<Mensaje> mensajesConNovedad = inicioSesionService.obtenerMensajesConNovedadParaUsuario(usuario);

            s.setAttribute("usuario", usuario);
            s.setAttribute("mensajesConNovedad", mensajesConNovedad);
        } catch (Exception e) {
            PRG.error("Usuario o contraseña incorrectos");
        }

        return "redirect:../";
    }

    //QUITA LA SESION CREADA CON EL INICIO DE SESION
    @GetMapping("/logout")
    public String logout(
            HttpSession s,
            ModelMap m, HttpSession session) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // SI NO ESTAS LOGEADO Y CON EL ROL CORRECTO SALTA EXCEPCION
        if (usuario == null) {
            PRG.error("No tienes ninguna sesion iniciada. Inicia sesion y vuelve.");
        }
        s.setAttribute("usuario", null);
        s.invalidate();
        return "redirect:../";
    }

    //VISTA DE CAMBIO DE CONTRASEÑAS
    @GetMapping("/cambiarContrasenia")
    public String cambiarContrasenia(
            ModelMap m, HttpSession session) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // SI NO ESTAS LOGEADO Y CON EL ROL CORRECTO SALTA EXCEPCION
        if (usuario == null) {
            PRG.error("No tienes ninguna sesion iniciada. Inicia sesion y vuelve.");
        }
        m.put("view", "inicioSesion/cambiarContrasenia");
        return "_t/frame";
    }

    //COMPRUEBA SI LA CONTRASEÑA DE LA USUARIO INICIADO ES IGUAL QUE LA CONTRASEÑA ACTUAL INTRODUCIDA
    @PostMapping("/cambiarContrasenia")
    public String cambiarContrasenia(
            @RequestParam("contraseniaActual") String contraseniaAntigua,
            @RequestParam("contraseniaNueva") String contraseniaNueva,
            HttpSession session,
            ModelMap modelMap) throws DangerException, InfoException {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        try {
            inicioSesionService.cambiarContrasenia(usuario, contraseniaAntigua, contraseniaNueva);
        } catch (Exception e) {
            PRG.error("La contraseña no es valida", "/inicioSesion/cambiarContrasenia");
        }
        PRG.info("Contraseña actualizada correctamente.", "/inicioSesion/cambiarContrasenia");
        return "redirect:../";
    }

    //PONE LOS MENSAJES COMO NUEVOS
    @PostMapping("/actualizarMensajes")
    public ResponseEntity<String> actualizarMensajes(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        inicioSesionService.marcarMensajesComoVistos(usuario);
        session.setAttribute("mensajesConNovedad", Collections.emptyList());
        return ResponseEntity.ok("Mensajes actualizados correctamente");
    }

}
