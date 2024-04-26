package com.salvaceloisma.tfg.controller.web;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.salvaceloisma.tfg.exception.DangerException;
import com.salvaceloisma.tfg.helper.PRG;
import com.salvaceloisma.tfg.service.AlumnoService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/alumno")
@Controller
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    @GetMapping("r")
    public String r(
            ModelMap m) {
        m.put("alumnos", alumnoService.findAll());
        m.put("view", "alumno/r");
        return "_t/frame";
    }

    @GetMapping("c")
    public String c(
            ModelMap m,
            HttpSession s) {

        m.put("view", "alumno/c");
        return "_t/frame";
    }

    @PostMapping("c")
    public String cPost(
            @RequestParam("dni") String dni,
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("fechaNacimiento") LocalDate fechaNacimiento, HttpSession s) throws Exception {
        try {
            alumnoService.save(dni, nombre, apellido, fechaNacimiento);
        } catch (Exception e) {
            PRG.error("El alumno ya existe", "/alumno/c");
        }
        return "redirect:/alumno/r";
    }

    @GetMapping("u")
    public String update(
            @RequestParam("id") Long idAlumno,
            ModelMap m) {
        m.put("alumno", alumnoService.findById(idAlumno));
        m.put("view", "alumno/u");
        return "_t/frame";
    }

    @PostMapping("u")
    public String updatePost(
            @RequestParam("id") Long idAlumno,
            @RequestParam("nombre") String nombre) throws DangerException {
        try {
            alumnoService.update(idAlumno, nombre);
        } catch (Exception e) {
            PRG.error("El alumno no pudo ser actualizado", "/alumno/r");
        }
        return "redirect:/alumno/r";
    }

    @PostMapping("d")
    public String delete(
            @RequestParam("id") Long idAlumno) throws DangerException {
        try {
            alumnoService.delete(idAlumno);
        } catch (Exception e) {
            PRG.error("No se puede borrar el alumno", "/alumno/r");
        }
        return "redirect:/alumno/r";
    }
}
