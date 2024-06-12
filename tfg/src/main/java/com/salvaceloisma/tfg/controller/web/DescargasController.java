package com.salvaceloisma.tfg.controller.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.pdfbox.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.salvaceloisma.tfg.domain.Solicitud;
import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.exception.DangerException;
import com.salvaceloisma.tfg.helper.PRG;
import com.salvaceloisma.tfg.service.SolicitudService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/descargas")
@Controller
public class DescargasController {

    @Autowired
    private SolicitudService solicitudService;

    @GetMapping("/descargarSolicitudAprobadaJefatura/{idSolicitud}")
    public void descargarSolicitudAprobadaJefatura(@PathVariable String idSolicitud, HttpSession session,
            HttpServletResponse response)
            throws IOException, DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        }

        Solicitud solicitud = solicitudService.findById(idSolicitud);
        String rutaArchivo = solicitud.getRutaSolicitud() + "/APROBADO_POR_JEFATURA " + idSolicitud + ".pdf";

        File archivoPDF = new File(rutaArchivo);
        if (archivoPDF.exists()) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + archivoPDF.getName());
            try (FileInputStream fis = new FileInputStream(archivoPDF)) {
                IOUtils.copy(fis, response.getOutputStream());
                response.getOutputStream().flush();
            }
        } else {
            PRG.error("El archivo no existe.");
        }
    }

    @GetMapping("/descargarSolicitudFirmadaEmpresa/{idSolicitud}")
    public void descargarSolicitudFirmadaEmpresa(@PathVariable String idSolicitud, HttpSession session,
            HttpServletResponse response)
            throws IOException, DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        }
        Solicitud solicitud = solicitudService.findById(idSolicitud);
        String rutaArchivo = solicitud.getRutaSolicitud() + "/FIRMADO_POR_EMPRESA " + idSolicitud + ".pdf";

        File archivoPDF = new File(rutaArchivo);
        if (archivoPDF.exists()) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + archivoPDF.getName());
            try (FileInputStream fis = new FileInputStream(archivoPDF)) {
                IOUtils.copy(fis, response.getOutputStream());
                response.getOutputStream().flush();
            }
        } else {
            PRG.error("El archivo no existe.");
        }
    }

    @GetMapping("/descargarSolicitudFirmadaDireccion/{idSolicitud}")
    public void descargarSolicitudFirmadaDireccion(@PathVariable String idSolicitud, HttpSession session,
            HttpServletResponse response)
            throws IOException, DangerException {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            PRG.error("No tienes los privilegios necesarios para realizar esta accion.");
        }
        Solicitud solicitud = solicitudService.findById(idSolicitud);
        String rutaArchivo = solicitud.getRutaSolicitud() + "/SOLICITUD_FINALIZADA " + idSolicitud + ".pdf";

        File archivoPDF = new File(rutaArchivo);
        if (archivoPDF.exists()) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + archivoPDF.getName());
            try (FileInputStream fis = new FileInputStream(archivoPDF)) {
                IOUtils.copy(fis, response.getOutputStream());
                response.getOutputStream().flush();
            }
        } else {
            PRG.error("El archivo no existe.");
        }
    }

}
