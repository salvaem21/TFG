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
import com.salvaceloisma.tfg.service.AlumnoService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/alumno")
@Controller
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @GetMapping("/gestionarAlumnos")
    public String gestionarAlumnos(
            ModelMap m, HttpSession session) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || (usuario.getRol() != RolUsuario.ADMIN && usuario.getRol() != RolUsuario.DIRECTOR)) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        }
        m.put("alumnos", alumnoService.findAll());
        m.put("view", "alumno/verAlumnos");
        return "_t/frame";
    }

    @GetMapping("verAlumnos")
    public String verAlumnos(
            ModelMap m, HttpSession session) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || (usuario.getRol() != RolUsuario.ADMIN && usuario.getRol() != RolUsuario.DIRECTOR)) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        }
        m.put("alumnos", alumnoService.findAll());
        m.put("view", "alumno/verAlumnos");
        return "_t/frame";
    }

    @GetMapping("actualizarAlumno")
    public String actualizarAlumno(
            @RequestParam("id") Long idAlumno, HttpSession session,
            ModelMap m) throws DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || (usuario.getRol() != RolUsuario.ADMIN && usuario.getRol() != RolUsuario.DIRECTOR)) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        }
        m.put("alumno", alumnoService.findById(idAlumno));
        m.put("view", "alumno/actualizarAlumno");
        return "_t/frame";
    }

    @PostMapping("actualizarAlumno")
    public String actualizarAlumnoPost(
            @RequestParam("idAlumno") Long idAlumno,
            @RequestParam("dni") String dni,
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido, HttpSession s) throws DangerException, InfoException {
        try {
            alumnoService.update(idAlumno, dni, nombre, apellido);
        } catch (Exception e) {
            PRG.error("El alumno no pudo ser actualizado", "/alumno/verAlumnos");
        }
        PRG.info("Alumno actualizado correctamente.", "/alumno/verAlumnos");
        return "redirect:/alumno/verAlumnos";
    }

    @PostMapping("borrarAlumno")
    public String borrarAlumno(
            @RequestParam("id") Long idAlumno) throws DangerException, InfoException {
        try {
            alumnoService.delete(idAlumno);
        } catch (Exception e) {
            PRG.error("No se puede borrar el alumno", "/alumno/verAlumnos");
        }
        PRG.info("Alumno eliminado correctamente.", "/alumno/verAlumnos");
        return "redirect:/alumno/verAlumnos";
    }
}
