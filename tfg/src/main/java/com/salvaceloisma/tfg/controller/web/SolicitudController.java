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
import com.salvaceloisma.tfg.service.SolicitudService;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate; // Importa LocalDate en lugar de LocalDateTime

@RequestMapping("/solicitud")
@Controller
public class SolicitudController {

    @Autowired
    private SolicitudService solicitudService;

    @GetMapping("r")
    public String r(ModelMap m) {
        m.put("solicitudes", solicitudService.findAll());
        m.put("view", "solicitud/r");
        return "_t/frame";
    }

    @GetMapping("c")
    public String c(ModelMap m) {
        m.put("view", "solicitud/c");
        return "_t/frame";
    }

    @PostMapping("c")
    public String cPost(
            @RequestParam("fechaInicio") LocalDate fechaInicio,
            @RequestParam("fechaFin") LocalDate fechaFin,
            @RequestParam("horario") String horario,
            @RequestParam("numeroConvenio") String numeroConvenio,
            @RequestParam("nombre") String nombre,
            @RequestParam("estado") boolean estado,
            @RequestParam("datosAlumno") String datosAlumno,
            HttpSession s) throws DangerException {
 
        try {
            solicitudService.save(fechaInicio, fechaFin, horario, numeroConvenio, null, estado, datosAlumno);
        } catch (Exception e) {
            PRG.error("La solicitud " + nombre + " ya existe", "/solicitud/c");
        }
        return "redirect:/solicitud/r";
    }

    @GetMapping("u")
    public String update(
            @RequestParam("id") Long idSolicitud,
            ModelMap m) {
        m.put("solicitud", solicitudService.findById(idSolicitud));
        m.put("view", "solicitud/u");
        return "_t/frame";
    }

    @PostMapping("u")
    public String updatePost(
            @RequestParam("id") Long idSolicitud,
            @RequestParam("fechaInicio") LocalDate fechaInicio,
            @RequestParam("fechaFin") LocalDate fechaFin,
            @RequestParam("horario") String horario,
            @RequestParam("numeroConvenio") String numeroConvenio,
            @RequestParam("nombre") String nombre,
            @RequestParam("estado") boolean estado,
            @RequestParam("datosAlumno") String datosAlumno,
            HttpSession s) throws DangerException {
        try {
            solicitudService.update(idSolicitud, fechaInicio, fechaFin, horario, numeroConvenio, null, estado, datosAlumno);
        } catch (Exception e) {
            PRG.error("La solicitud no pudo ser actualizada", "/solicitud/r");
        }
        return "redirect:/solicitud/r";
    }

    @PostMapping("d")
    public String delete(
            @RequestParam("id") Long idSolicitud) throws DangerException {
        try {
            solicitudService.delete(idSolicitud);
        } catch (Exception e) {
            PRG.error("No se puede borrar la solicitud", "/solicitud/r");
        }
        return "redirect:/solicitud/r";
    }
}
