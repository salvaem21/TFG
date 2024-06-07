package com.salvaceloisma.tfg.controller.web;

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
            ModelMap m, HttpSession session) throws DangerException {
        
        m.put("alumnos", alumnoService.findAll());
        m.put("view", "alumno/r");
        return "_t/frame";
    }

    @GetMapping("u")
    public String update(
            @RequestParam("id") Long idAlumno, HttpSession session,
            ModelMap m) throws DangerException {
        
        m.put("alumno", alumnoService.findById(idAlumno));
        m.put("view", "alumno/u");
        return "_t/frame";
    }

    @PostMapping("u")
    public String updatePost(
            @RequestParam("idAlumno") Long idAlumno,
            @RequestParam("dni") String dni,
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido, HttpSession s) throws DangerException {
        try {
            alumnoService.update(idAlumno, dni, nombre, apellido);
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
