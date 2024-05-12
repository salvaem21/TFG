package com.salvaceloisma.tfg.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.salvaceloisma.tfg.domain.Mensaje;
import com.salvaceloisma.tfg.domain.Solicitud;
import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.enumerados.EstadoSolicitud;
import com.salvaceloisma.tfg.enumerados.RolUsuario;
import com.salvaceloisma.tfg.exception.DangerException;
import com.salvaceloisma.tfg.exception.InfoException;
import com.salvaceloisma.tfg.helper.PRG;
import com.salvaceloisma.tfg.service.AlumnoService;
import com.salvaceloisma.tfg.service.EmailService;
import com.salvaceloisma.tfg.service.InicioSesionService;
import com.salvaceloisma.tfg.service.SolicitudService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/enviarDatos")
@Controller
public class EnviarDatosController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private InicioSesionService inicioSesionService;

    @Autowired
    private AlumnoService alumnoService;

    @Autowired
    private SolicitudService solicitudService;

    @GetMapping("/enviarDatosAJefatura")
    public String crearDocumento(ModelMap m) {
        m.put("usuariosJefatura", inicioSesionService.obtenerUsuariosPorRol(RolUsuario.JEFATURA));
        m.put("view", "profesor/enviarDatosAlumnos");
        return "_t/frame";
    }

    @PostMapping("/enviarDatosAJefatura")
    public String crearDocumento(HttpServletResponse response, HttpSession session,
            @RequestParam String numeroConvenio,
            @RequestParam String tutorAlumno,
            @RequestParam String nombreEmpresa, @RequestParam String tutorEmpresa,
            @RequestParam String cifEmpresa, @RequestParam String direccionPracticas,
            @RequestParam String localidadPracticas, @RequestParam String codigoPostalPracticas,
            @RequestParam String[] apellidosAlumno, @RequestParam String[] nombreAlumno,
            @RequestParam String[] nifAlumno, @RequestParam String cicloFormativoAlumno,
            @RequestParam Integer horasTotales,@RequestParam LocalDate fechaInicio, 
            @RequestParam LocalDate fechaFin, @RequestParam LocalTime lunesInicio1,
            @RequestParam LocalTime martesInicio1, @RequestParam LocalTime lunesFin1,
            @RequestParam LocalTime martesFin1, @RequestParam LocalTime miercolesInicio1,
            @RequestParam LocalTime juevesInicio1, @RequestParam LocalTime viernesInicio1,
            @RequestParam LocalTime miercolesFin1, @RequestParam LocalTime juevesFin1,
            @RequestParam LocalTime viernesFin1, @RequestParam LocalTime lunesInicio2,
            @RequestParam LocalTime miercolesInicio2,
            @RequestParam LocalTime martesInicio2, @RequestParam LocalTime juevesInicio2,
            @RequestParam LocalTime viernesInicio2, @RequestParam LocalTime lunesFin2,
            @RequestParam LocalTime martesFin2,
            @RequestParam LocalTime miercolesFin2, @RequestParam LocalTime juevesFin2,
            @RequestParam LocalTime viernesFin2, @RequestParam Integer horasDia,
            @RequestParam("rolUsuario") Long usuarioEnvio, ModelMap m)
            throws DangerException, InfoException {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String nombre = usuario.getNombre();

        String correo = inicioSesionService.findById(usuarioEnvio).getCorreo();

        StringBuilder horarioBuilder = new StringBuilder();
        horarioBuilder.append("Lunes: ").append(lunesInicio1).append(" - ").append(lunesFin1).append(".\n")
                .append("Martes: ").append(martesInicio1).append(" - ").append(martesFin1).append(".\n")
                .append("Miércoles: ").append(miercolesInicio1).append(" - ").append(miercolesFin1).append(".\n")
                .append("Jueves: ").append(juevesInicio1).append(" - ").append(juevesFin1).append(".\n")
                .append("Viernes: ").append(viernesInicio1).append(" - ").append(viernesFin1).append(".\n")
                .append("Segundo horario: \n")
                .append("Lunes: ").append(lunesInicio2).append(" - ").append(lunesFin2).append(".\n")
                .append("Martes: ").append(martesInicio2).append(" - ").append(martesFin2).append(".\n")
                .append("Miércoles: ").append(miercolesInicio2).append(" - ").append(miercolesFin2).append(".\n")
                .append("Jueves: ").append(juevesInicio2).append(" - ").append(juevesFin2).append(".\n")
                .append("Viernes: ").append(viernesInicio2).append(" - ").append(viernesFin2).append(".");
        String horario = horarioBuilder.toString();

        StringBuilder datos = new StringBuilder();

        // Agregar los datos comunes al final
        datos.append("Datos comunes:\n");
        datos.append("Tutor alumno: ").append(tutorAlumno).append("\n");
        datos.append("Numero Convenio: ").append(numeroConvenio).append("\n");
        datos.append("Nombre empresa: ").append(nombreEmpresa).append("\n");
        datos.append("Tutor empresa: ").append(tutorEmpresa).append("\n");
        datos.append("CIF empresa: ").append(cifEmpresa).append("\n");
        datos.append("Dirección prácticas: ").append(direccionPracticas).append("\n");
        datos.append("Localidad prácticas: ").append(localidadPracticas).append("\n");
        datos.append("Código postal prácticas: ").append(codigoPostalPracticas).append("\n");
        datos.append("Horas totales: ").append(horasTotales).append("\n");
        datos.append("Fecha de inicio: ").append(fechaInicio).append("\n");
        datos.append("Fecha de fin: ").append(fechaFin).append("\n");
        datos.append("Horas por día: ").append(horasDia).append("\n");

        Usuario remitente = (Usuario) session.getAttribute("usuario");
        Usuario destinatario = inicioSesionService.findById(usuarioEnvio);
        // Establecer el estado por defecto a PENDIENTE_JEFATURA
        EstadoSolicitud estado = EstadoSolicitud.PENDIENTE_FIRMA_JEFATURA;
        String observaciones = "";
        String idSolicitud = "";

        try {
            Solicitud solicitud = solicitudService.save(numeroConvenio, nombreEmpresa, cifEmpresa, tutorEmpresa,
                    direccionPracticas, localidadPracticas, codigoPostalPracticas, cicloFormativoAlumno, usuario,
                    fechaInicio, fechaFin, horasDia, horasTotales, horario, observaciones, estado);
            idSolicitud = solicitud.getIdSolicitud();
            

            // Construir el mensaje para cada alumno
            for (int i = 0; i < apellidosAlumno.length; i++) {
                datos.append("Alumno ").append(i + 1).append(":\n");
                datos.append("Apellidos alumno: ").append(apellidosAlumno[i]).append("\n");
                datos.append("Nombre alumno: ").append(nombreAlumno[i]).append("\n");
                datos.append("NIF alumno: ").append(nifAlumno[i]).append("\n");
                datos.append("Ciclo formativo: ").append(cicloFormativoAlumno).append("\n");
                datos.append("Fecha de nacimiento alumno: ").append("\n");
                datos.append("\n");
                try {
                    alumnoService.save(nifAlumno[i], nombreAlumno[i], apellidosAlumno[i], idSolicitud);
                } catch (Exception e) {
                    PRG.error("El NIF de ese alumno ya esta en uso");
                }
            }
            inicioSesionService.enviarMensaje(remitente, destinatario, datos.toString(), solicitud);
            emailService.enviarEmail(correo, "Datos pendientes de ser revisados por Jefatura.",
                    nombre + " ha enviado unos datos.");
        } catch (Exception e) {
            PRG.error("Los datos no pudieron enviarse correctamente.");
        }

        return "redirect:../";
    }

    @GetMapping("/recibirDatosJefatura")
    public String recibirMensajes(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("nombreUsuario", usuario.getNombre()); // Agregar nombre del usuario al modelo

        // Obtener los mensajes enviados y recibidos por el usuario
        List<Mensaje> mensajes = inicioSesionService.recibirMensajes(usuario);

        // Extraer las solicitudes de los mensajes y agregarlas a una lista
        List<Solicitud> solicitudes = new ArrayList<>();
        for (Mensaje mensaje : mensajes) {
            Solicitud solicitud = mensaje.getSolicitud();
            if (solicitud != null) {
                // Cargar los alumnos vinculados a esta solicitud
                solicitud.setAlumnos(alumnoService.findBySolicitudIdSolicitud(solicitud.getIdSolicitud()));
                solicitudes.add(solicitud);
            }
        }

        model.addAttribute("solicitudes", solicitudes);
        model.addAttribute("view", "jefatura/recibirDatosJefatura");

        return "_t/frame";
    }

    @GetMapping("/corregirDatosJefatura")
    public String verificarDocumento(
            @RequestParam("id") String idSolicitud,
            ModelMap m) {
        m.put("solicitud", solicitudService.findById(idSolicitud));
        m.put("alumnos", alumnoService.findBySolicitudIdSolicitud(idSolicitud));
        m.put("usuariosJefatura", inicioSesionService.obtenerUsuariosPorRol(RolUsuario.JEFATURA));
        m.put("view", "jefatura/corregirDatosJefatura");
        return "_t/frame";
    }
}
