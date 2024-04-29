package com.salvaceloisma.tfg.controller.web;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.enumerados.EstadoSolicitud;
import com.salvaceloisma.tfg.exception.DangerException;
import com.salvaceloisma.tfg.helper.PRG;
import com.salvaceloisma.tfg.service.SolicitudService;

import jakarta.servlet.http.HttpSession;

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
            @RequestParam("idSolicitud") String idSolicitud,
            @RequestParam("numeroConvenio") Integer numeroConvenio,
            @RequestParam("empresa") String empresa, 
            @RequestParam("cif") String cif, 
            @RequestParam("tutorEmpresa") String tutorEmpresa, 
            @RequestParam("direccion") String direccion, 
            @RequestParam("localidad") String localidad, 
            @RequestParam("cp") String cp, 
            @RequestParam("cicloFormativo") String cicloFormativo, 
            @RequestParam("usuario") Usuario usuario, 
            @RequestParam("fechaInicio") LocalDate fechaInicio, 
            @RequestParam("fechaFin") LocalDate fechaFin, 
            @RequestParam("horasDia") int horasDia, 
            @RequestParam("horasTotales") int horasTotales, 
            @RequestParam("horario") String horario, 
            @RequestParam("observaciones") String observaciones,
            @RequestParam("estado") EstadoSolicitud estado,
            HttpSession s) throws DangerException {
 
        try {
            solicitudService.save(idSolicitud, numeroConvenio, empresa, cif, tutorEmpresa, direccion, localidad, cp, cicloFormativo, usuario, fechaInicio, fechaFin, horasDia, horasTotales, horario, observaciones, estado);
        } catch (Exception e) {
            PRG.error("La solicitud " + idSolicitud + " ya existe", "/solicitud/c");
        }
        return "redirect:/solicitud/r";
    }

    @GetMapping("u")
    public String update(
            @RequestParam("id") String idSolicitud,
            ModelMap m) {
        m.put("solicitud", solicitudService.findById(idSolicitud));
        m.put("view", "solicitud/u");
        return "_t/frame";
    }

    @PostMapping("u")
    public String updatePost(
        @RequestParam("idSolicitud") String idSolicitud,
            @RequestParam("numeroConvenio") Integer numeroConvenio,
            @RequestParam("empresa") String empresa, 
            @RequestParam("cif") String cif, 
            @RequestParam("tutorEmpresa") String tutorEmpresa, 
            @RequestParam("direccion") String direccion, 
            @RequestParam("localidad") String localidad, 
            @RequestParam("cp") String cp, 
            @RequestParam("cicloFormativo") String cicloFormativo, 
            @RequestParam("usuario") Usuario usuario, 
            @RequestParam("fechaInicio") LocalDate fechaInicio, 
            @RequestParam("fechaFin") LocalDate fechaFin, 
            @RequestParam("horasDia") int horasDia, 
            @RequestParam("horasTotales") int horasTotales, 
            @RequestParam("horario") String horario, 
            @RequestParam("observaciones") String observaciones,
            @RequestParam("estado") EstadoSolicitud estado,
            HttpSession s) throws DangerException {
        try {
            solicitudService.update(idSolicitud, numeroConvenio, empresa, cif, tutorEmpresa, direccion, localidad, cp, cicloFormativo, usuario, fechaInicio, fechaFin, horasDia, horasTotales, horario, observaciones, estado);
        } catch (Exception e) {
            PRG.error("La solicitud " + idSolicitud + " ya existe", "/solicitud/r");
        }
        return "redirect:/solicitud/r";
    }

    @PostMapping("d")
    public String delete(
            @RequestParam("id") String idSolicitud) throws DangerException {
        try {
            solicitudService.delete(idSolicitud);
        } catch (Exception e) {
            PRG.error("No se puede borrar la solicitud", "/solicitud/r");
        }
        return "redirect:/solicitud/r";
    }
}
