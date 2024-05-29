package com.salvaceloisma.tfg.controller.web;

import org.apache.pdfbox.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.salvaceloisma.tfg.domain.Alumno;
import com.salvaceloisma.tfg.domain.Mensaje;
import com.salvaceloisma.tfg.domain.Solicitud;
import com.salvaceloisma.tfg.domain.Usuario;
import com.salvaceloisma.tfg.enumerados.EstadoSolicitud;
import com.salvaceloisma.tfg.enumerados.RolUsuario;
import com.salvaceloisma.tfg.exception.DangerException;
import com.salvaceloisma.tfg.exception.InfoException;
import com.salvaceloisma.tfg.helper.PRG;
import com.salvaceloisma.tfg.service.AlumnoService;
// import com.salvaceloisma.tfg.service.ArchivoService;
import com.salvaceloisma.tfg.service.ArchivoServiceImpl;
import com.salvaceloisma.tfg.service.EmailService;
import com.salvaceloisma.tfg.service.InicioSesionService;
import com.salvaceloisma.tfg.service.MensajeService;
import com.salvaceloisma.tfg.service.SolicitudService;
import com.salvaceloisma.tfg.enumerados.Grados;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private MensajeService mensajeService;

    // @Autowired
    // private ArchivoService archivoService;

    @Autowired
    private ArchivoServiceImpl archivoServiceImpl;

    @GetMapping("/enviarDatosAJefatura")
    public String crearDocumento(ModelMap m) {
        m.put("usuariosJefatura", inicioSesionService.obtenerUsuariosPorRol(RolUsuario.JEFATURA));
        m.put("grados", Grados.values()); // Añadir el enum Grados al modelo
        m.put("view", "profesor/enviarDatosAlumnos");
        return "_t/frame";
    }

    @PostMapping("/enviarDatosAJefatura")
    public String crearDocumento(HttpServletResponse response, HttpSession session,
            @RequestParam(required = false) String idSolicitud, // Nuevo parámetro para identificar si se está creando o
                                                                // actualizando
            @RequestParam String numeroConvenio,
            @RequestParam String tutorAlumno,
            @RequestParam String nombreEmpresa, @RequestParam String tutorEmpresa,
            @RequestParam String cifEmpresa, @RequestParam String direccionPracticas,
            @RequestParam String localidadPracticas, @RequestParam String codigoPostalPracticas,
            @RequestParam String[] apellidosAlumno, @RequestParam String[] nombreAlumno,
            @RequestParam String[] nifAlumno, @RequestParam String cicloFormativoAlumno,
            @RequestParam Integer horasTotales, @RequestParam LocalDate fechaInicio,
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

        // Verificar si se está creando una nueva solicitud o actualizando una existente
        boolean creandoNuevaSolicitud = (idSolicitud == null || idSolicitud.isEmpty());
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

        try {

            Solicitud solicitud = null;
            if (creandoNuevaSolicitud) {
                solicitud = solicitudService.save(numeroConvenio, nombreEmpresa, cifEmpresa, tutorEmpresa,
                        direccionPracticas, localidadPracticas, codigoPostalPracticas, cicloFormativoAlumno, usuario,
                        fechaInicio, fechaFin, horasDia, horasTotales, horario, observaciones, estado);
                idSolicitud = solicitud.getIdSolicitud();
                String empresa = solicitud.getEmpresa();

                String rutaUsuario = usuario.getRutaCarpeta();
                String rutaSolicitud = rutaUsuario + "/" + empresa + "/" + idSolicitud;
                File carpetaSolicitud = new File(rutaSolicitud);
                if (!carpetaSolicitud.exists()) {
                    carpetaSolicitud.mkdirs(); // Crear la carpeta si no existe
                }
                solicitud.setRutaSolicitud(rutaSolicitud);
                solicitudService.save(solicitud);
            } else {
                // Si se está actualizando, obtener la solicitud existente y actualizar sus
                // datos
                solicitudService.update(idSolicitud, numeroConvenio, nombreEmpresa, cifEmpresa, tutorEmpresa,
                        direccionPracticas, localidadPracticas, codigoPostalPracticas, cicloFormativoAlumno, usuario,
                        fechaInicio, fechaFin, horasDia, horasTotales, horario, observaciones, estado);

                solicitud = solicitudService.findById(idSolicitud);
                idSolicitud = solicitud.getIdSolicitud();
                String empresa = solicitud.getEmpresa();

                String rutaUsuario = usuario.getRutaCarpeta();
                String rutaSolicitud = rutaUsuario + "/" + empresa + "/" + idSolicitud;
                File carpetaSolicitud = new File(rutaSolicitud);
                if (!carpetaSolicitud.exists()) {
                    carpetaSolicitud.mkdirs(); // Crear la carpeta si no existe
                }
                solicitud.setRutaSolicitud(rutaSolicitud);
                solicitudService.save(solicitud);
            }
            // Obtener lista de alumnos actuales de la solicitud
            List<Alumno> alumnosActuales = alumnoService.findBySolicitudIdSolicitud(idSolicitud);
            List<String> nifAlumnosActuales = alumnosActuales.stream()
                    .map(Alumno::getDni)
                    .collect(Collectors.toList());

            // Crear lista de NIFs recibidos en la solicitud
            List<String> nifAlumnosRecibidos = Arrays.asList(nifAlumno);

            // Determinar alumnos a eliminar
            for (Alumno alumno : alumnosActuales) {
                if (!nifAlumnosRecibidos.contains(alumno.getDni())) {
                    alumnoService.deleteByDni(alumno.getDni());
                }
            }

            // Crear o actualizar los alumnos
            for (int i = 0; i < apellidosAlumno.length; i++) {
                datos.append("Alumno ").append(i + 1).append(":\n");
                datos.append("Apellidos alumno: ").append(apellidosAlumno[i]).append("\n");
                datos.append("Nombre alumno: ").append(nombreAlumno[i]).append("\n");
                datos.append("NIF alumno: ").append(nifAlumno[i]).append("\n");
                datos.append("Ciclo formativo: ").append(cicloFormativoAlumno).append("\n");
                datos.append("Fecha de nacimiento alumno: ").append("\n");
                datos.append("\n");
                try {
                    if (creandoNuevaSolicitud || !nifAlumnosActuales.contains(nifAlumno[i])) {
                        alumnoService.save(nifAlumno[i], nombreAlumno[i], apellidosAlumno[i], idSolicitud);
                    } else {
                        alumnoService.updateByDni(nifAlumno[i], nombreAlumno[i], apellidosAlumno[i], idSolicitud);
                    }
                } catch (Exception e) {
                    PRG.error("Los alumnos no pueden ser creados o actualizados.");
                }
            }

            mensajeService.enviarMensaje(remitente, destinatario, observaciones, solicitud);
            emailService.enviarEmail(correo, "Datos pendientes de ser revisados por Jefatura.",
                    nombre + " ha enviado unos datos.");
        } catch (Exception e) {
            PRG.error("Los datos no pudieron enviarse correctamente.");
        }
        return "redirect:../";
    }

    // --------------------------------------------------------------------------------------------------------------------------------------------//
    // LISTADOS TODAS LAS SOLICITUDES
    @GetMapping("/listadoAllSolicitudes")
    public String allSolicitudes(ModelMap m) {
        List<Solicitud> allSolicitudes = solicitudService.findAll();

        // Procesar el campo de horario de cada solicitud
        for (Solicitud solicitud : allSolicitudes) {
            String horarioProcesado = solicitud.getHorario().replace("Segundo horario:",
                    "<br><strong>Segundo horario:</strong><br><br>");
            horarioProcesado = horarioProcesado.replace(".", "<br>");
            solicitud.setHorario(horarioProcesado);
            // Cargar los alumnos vinculados a esta solicitud (LISTA) ya que se utilizara en
            // la vista
            solicitud.setAlumnos(alumnoService.findBySolicitudIdSolicitud(solicitud.getIdSolicitud()));
        }
        m.put("solicitudes", allSolicitudes);
        m.put("view", "solicitud/solicitudesAll");

        return "_t/frame";
    }
    // ---------------JEFATURA------------------------/////////////////////////////////

    @GetMapping("/recibirDatosJefatura")
    public String recibirMensajes(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        model.addAttribute("nombreUsuario", usuario.getNombre()); // Agregar nombre del usuario al modelo

        // Obtener los mensajes enviados y recibidos por el usuario
        List<Mensaje> mensajes = mensajeService.recibirMensajes(usuario);

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
        Solicitud solicitud = solicitudService.findById(idSolicitud);
        String horarioSinSegundoHorario = solicitud.getHorario().replace("Segundo horario:", "");
        solicitud.setHorario(horarioSinSegundoHorario); // ELIMINAMOS EL TEXTO PARA QUE NO DE ERROR AL RECORRER.
        m.put("solicitud", solicitud);
        m.put("alumnos", alumnoService.findBySolicitudIdSolicitud(idSolicitud));
        m.put("usuariosJefatura", inicioSesionService.obtenerUsuariosPorRol(RolUsuario.JEFATURA));
        m.put("view", "jefatura/corregirDatosJefatura");
        return "_t/frame";
    }
    /*
     * @GetMapping("/corregirDatosJefatura/{idSolicitud}")
     * public String corregirDatos(@PathVariable("idSolicitud") String idSolicitud,
     * Model model) {
     * Solicitud solicitud = solicitudService.findById(idSolicitud);
     * String horarioSinSegundoHorario =
     * solicitud.getHorario().replace("Segundo horario:", "");
     * if (solicitud == null) {
     * }
     * solicitud.setHorario(horarioSinSegundoHorario); //ELIMINAMOS EL TEXTO PARA
     * QUE NO DE ERROR AL RECORRER.
     * List<Alumno> alumnos = alumnoService.findBySolicitudIdSolicitud(idSolicitud);
     * model.addAttribute("solicitud", solicitud);
     * model.addAttribute("alumnos", alumnos);
     * model.addAttribute("usuariosJefatura",
     * inicioSesionService.obtenerUsuariosPorRol(RolUsuario.JEFATURA));
     * model.addAttribute("view", "jefatura/corregirDatosJefatura");
     * return "_t/frame";
     * }
     */

    // MENSAJE Y NOTIFICACIÓN
    @PostMapping("/corregirDatosJefaturaObservaciones")
    public String verificarDocumentoEnviarObservacionACorregir(HttpServletResponse response, HttpSession session,
            @RequestParam("idSolicitud") String idSolicitud,
            @RequestParam("observaciones") String observaciones) throws Exception {
        if (observaciones == null || observaciones.isEmpty()) {
            PRG.error("Las observaciones estan vacias", "../");
        }
        Mensaje mensaje = mensajeService.findBySolicitudIdSolicitud(idSolicitud);
        // Invertimos el correo devuelta
        Usuario destinatario = mensaje.getRemitente();
        Usuario remitente = mensaje.getDestinatario();
        String destinatarioCorreo = destinatario.getCorreo();
        String remitenteCorreo = destinatario.getCorreo();
        EstadoSolicitud estadoSolicitud = EstadoSolicitud.RECHAZADO_JEFATURA;

        try {
            mensajeService.actualizarMensaje(idSolicitud, destinatario, remitente, observaciones);
            solicitudService.cambiarEstadoSolicitud(idSolicitud, estadoSolicitud, remitente);
            emailService.enviarEmail(destinatarioCorreo, remitenteCorreo, "Datos pendientes de ser revisados.");

            PRG.info("Correción enviada correctamente.");
        } catch (IOException e) {
            PRG.error("Error al subir el archivo.", "/jefatura/corregirDatosJefatura");

        }

        return "redirect: ../";
    }

    // ARCHIVO Y NOTIFICACIÓN
    @PostMapping("/corregirDatosJefaturaArchivo")
    public String verificarDocumento(HttpServletResponse response, HttpSession session,
            @RequestParam("idSolicitud") String idSolicitud,
            @RequestParam("archivoPDF") MultipartFile archivo,
            @RequestParam String numeroConvenio,
            @RequestParam String tutorAlumno,
            @RequestParam String nombreEmpresa, @RequestParam String tutorEmpresa,
            @RequestParam String cifEmpresa, @RequestParam String direccionPracticas,
            @RequestParam String localidadPracticas, @RequestParam String codigoPostalPracticas,
            @RequestParam String cicloFormativoAlumno, @RequestParam int horasTotales,
            @RequestParam LocalDate fechaInicio, @RequestParam LocalDate fechaFin,
            @RequestParam LocalTime lunesInicio1, @RequestParam LocalTime martesInicio1,
            @RequestParam LocalTime lunesFin1, @RequestParam LocalTime martesFin1,
            @RequestParam LocalTime miercolesInicio1, @RequestParam LocalTime juevesInicio1,
            @RequestParam LocalTime viernesInicio1, @RequestParam LocalTime miercolesFin1,
            @RequestParam LocalTime juevesFin1, @RequestParam LocalTime viernesFin1,
            @RequestParam LocalTime lunesInicio2, @RequestParam LocalTime miercolesInicio2,
            @RequestParam LocalTime martesInicio2, @RequestParam LocalTime juevesInicio2,
            @RequestParam LocalTime viernesInicio2, @RequestParam LocalTime lunesFin2,
            @RequestParam LocalTime martesFin2, @RequestParam LocalTime miercolesFin2,
            @RequestParam LocalTime juevesFin2, @RequestParam LocalTime viernesFin2,
            @RequestParam int horasDia,
            @RequestParam String[] apellidosAlumno, @RequestParam String[] nombreAlumno,
            @RequestParam String[] nifAlumno) throws Exception {

        if (archivo == null || archivo.isEmpty()) {
            PRG.error("El archivo no ha sido seleccionado", "../");
        }

        try {
            // Obtener la solicitud para recuperar la ruta
            Solicitud solicitud = solicitudService.findById(idSolicitud);

            // Actualizar los datos de la solicitud
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


            solicitudService.update(idSolicitud, numeroConvenio, nombreEmpresa, cifEmpresa, tutorEmpresa,
                    direccionPracticas,
                    localidadPracticas, codigoPostalPracticas, cicloFormativoAlumno, solicitud.getUsuario(), fechaInicio, fechaFin,
                    horasDia, horasTotales, horario, null, solicitud.getEstado());

            // Actualizar o crear los alumnos
            for (int i = 0; i < apellidosAlumno.length; i++) {
                String dniAlumno = nifAlumno[i];
                Alumno alumnoExistente = alumnoService.findByDniAndSolicitudIdSolicitud(dniAlumno, idSolicitud);
                if (alumnoExistente != null) {
                    // Actualizar el alumno existente
                    alumnoService.updateByDni(dniAlumno, nombreAlumno[i], apellidosAlumno[i], idSolicitud);
                } else {
                    // Crear un nuevo alumno
                    alumnoService.save(nifAlumno[i], nombreAlumno[i], apellidosAlumno[i], idSolicitud);
                }
            }
            solicitud = solicitudService.findById(idSolicitud);
            String rutaUsuario = solicitud.getUsuario().getRutaCarpeta();
            String rutaSolicitud = rutaUsuario + "/" + solicitud.getEmpresa() + "/" + idSolicitud;
            solicitud.setRutaSolicitud(rutaSolicitud);
            solicitudService.save(solicitud);
            File carpetaSolicitud = new File(rutaSolicitud);
            if (!carpetaSolicitud.exists()) {
                carpetaSolicitud.mkdirs(); // Crear la carpeta si no existe
            }
            String nombreArchivo = "APROBADO_POR_JEFATURA " + idSolicitud + ".pdf";

            // Guardar el archivo en la ruta especificada
            archivoServiceImpl.guardarArchivo(archivo, solicitud.getRutaSolicitud(), nombreArchivo);


            // Actualizar la notificación y el estado de la solicitud
            EstadoSolicitud estadoSolicitud = EstadoSolicitud.APROBADO_JEFATURA_PDF;

            Usuario remitente = (Usuario) session.getAttribute("usuario");
            Usuario destinatario = mensajeService.findBySolicitudIdSolicitud(idSolicitud).getRemitente();
            mensajeService.actualizarNotificacion(idSolicitud, destinatario, remitente);
            solicitudService.cambiarEstadoSolicitud(idSolicitud, estadoSolicitud, remitente);

            // Enviar el correo
            emailService.enviarEmail(destinatario.getCorreo(), remitente.getCorreo(),
                    "Solicitud aceptada. Revisa tu bandeja de entrada.");

            PRG.info("Corrección enviada correctamente.");
        } catch (IOException e) {
            PRG.error("Error al subir el archivo.", "/jefatura/corregirDatosJefatura");
        }

        return "redirect: ../";
    }

    // ----------------PROFESOR---------------------------//////////////////////////////////////////////

    // RECHAZADOS
    @GetMapping("/correccionSolicitudListado")
    public String recibirCorrecionDatosDeJefatura(ModelMap m, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        m.addAttribute("nombreUsuario", usuario.getNombre()); // Agregar nombre del usuario al modelo

        // Obtener los mensajes recibidos por el usuario
        EstadoSolicitud estadoRechazado = EstadoSolicitud.RECHAZADO_JEFATURA;
        List<Mensaje> mensajes = mensajeService.recibirMensajes(usuario);
        m.put("estadoRechazado", estadoRechazado);
        m.put("mensajes", mensajes);
        m.put("view", "profesor/solicitudesPendientesCorregir");

        return "_t/frame";
    }

    @GetMapping("/correccionSolicitud")
    public String modificarDocumento(@RequestParam("id") String idSolicitud, ModelMap m) {
        Solicitud solicitud = solicitudService.findById(idSolicitud);
        String horarioSinSegundoHorario = solicitud.getHorario().replace("Segundo horario:", "");
        solicitud.setHorario(horarioSinSegundoHorario); // ELIMINAMOS EL TEXTO PARA QUE NO DE ERROR AL RECORRER.
        m.put("solicitud", solicitud);
        m.put("alumnos", alumnoService.findBySolicitudIdSolicitud(idSolicitud));
        m.put("usuariosJefatura", inicioSesionService.obtenerUsuariosPorRol(RolUsuario.JEFATURA));
        m.put("view", "profesor/solicitudCorreccion");
        return "_t/frame";
    }

    // APROBADOS
    @GetMapping("/solicitudListadoOk")
    public String aprobadosDatosDeJefatura(ModelMap m, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        m.addAttribute("nombreUsuario", usuario.getNombre()); // Agregar nombre del usuario al modelo

        // Obtener los mensajes recibidos por el usuario
        EstadoSolicitud estadoAprobado1 = EstadoSolicitud.APROBADO_JEFATURA_PDF;
        EstadoSolicitud estadoAprobado2 = EstadoSolicitud.RECHAZADO_DIRECCION;

        List<Mensaje> mensajes = mensajeService.recibirMensajes(usuario);
        m.put("estadoAprobado1", estadoAprobado1);
        m.put("estadoAprobado2", estadoAprobado2);
        m.put("mensajes", mensajes);
        m.put("view", "profesor/solicitudesAprobados");

        return "_t/frame";
    }

    // FINALIZADOS
    @GetMapping("/solicitudesFinalizadas")
    public String aprobadosPorDireccion(ModelMap m, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        // m.addAttribute("nombreUsuario", usuario.getNombre()); // Agregar nombre del
        // usuario al modelo

        // Obtener los mensajes recibidos por el usuario
        EstadoSolicitud estadoFinalizado = EstadoSolicitud.SOLICITUD_FINALIZADA;

        List<Solicitud> allSolicitudes = solicitudService.findAllByUsuario(usuario);

        // Procesar el campo de horario de cada solicitud
        for (Solicitud solicitud : allSolicitudes) {
            String horarioProcesado = solicitud.getHorario().replace("Segundo horario:",
                    "<br><strong>Segundo horario:</strong><br><br>");
            horarioProcesado = horarioProcesado.replace(".", "<br>");
            solicitud.setHorario(horarioProcesado);
            // Cargar los alumnos vinculados a esta solicitud (LISTA) ya que se utilizara en
            // la vista
            solicitud.setAlumnos(alumnoService.findBySolicitudIdSolicitud(solicitud.getIdSolicitud()));
        }
        m.put("estadoFinalizado", estadoFinalizado);
        m.put("solicitudes", allSolicitudes);
        m.put("view", "profesor/solicitudesFinalizadas");

        return "_t/frame";
    }

    // TODAS LAS SOLICITUDES DEL PROFESOR
    @GetMapping("/solicitudesAllProfesor")
    public String todasLasSolicitudesProfesor(ModelMap m, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        List<Solicitud> allSolicitudesProfesor = solicitudService.findAllByUsuario(usuario);

        // Procesar el campo de horario de cada solicitud
        for (Solicitud solicitud : allSolicitudesProfesor) {
            String horarioProcesado = solicitud.getHorario().replace("Segundo horario:",
                    "<br><strong>Segundo horario:</strong><br><br>");
            horarioProcesado = horarioProcesado.replace(".", "<br>");
            solicitud.setHorario(horarioProcesado);
            // Cargar los alumnos vinculados a esta solicitud (LISTA) ya que se utilizara en
            // la vista
            solicitud.setAlumnos(alumnoService.findBySolicitudIdSolicitud(solicitud.getIdSolicitud()));
        }
        m.put("solicitudes", allSolicitudesProfesor);
        m.put("view", "profesor/solicitudesAllProfesor");

        return "_t/frame";
    }

    // ARCHIVO Y NOTIFICACIÓN PROFESOR JEFATURA
    @PostMapping("/solicitudListadoOk")
    public String solicitudADireccion(HttpServletResponse response,
            HttpSession session,
            @RequestParam("idSolicitud") String idSolicitud,
            @RequestParam("archivo") MultipartFile archivo) throws Exception {

        // Verificar si el archivo está vacío
        if (archivo.isEmpty()) {
            PRG.error("Por favor, seleccione un archivo antes de enviar.", "/enviarDatos/solicitudListadoOk");
            return "redirect:/profesor/solicitudesAprobados";
        }

        Mensaje mensaje = mensajeService.findBySolicitudIdSolicitud(idSolicitud);

        // Obtener los usuarios con el rol de DIRECTOR
        List<Usuario> directores = inicioSesionService.obtenerUsuariosPorRol(RolUsuario.DIRECTOR);
        if (directores.isEmpty()) {
            // Manejar el caso donde no hay directores encontrados
            PRG.error("No se encontraron usuarios con el rol de DIRECTOR.", "/profesor/solicitudesAprobados");
            return "redirect:/profesor/solicitudesAprobados";
        }
        // Obtener el primer director de la lista
        Usuario destinatario = directores.get(0);
        Usuario remitente = mensaje.getDestinatario();
        String destinatarioCorreo = destinatario.getCorreo();
        String remitenteCorreo = remitente.getCorreo();
        EstadoSolicitud estadoSolicitud = EstadoSolicitud.PENDIENTE_FIRMA_DIRECCION;

        try {
            // Obtener la solicitud para recuperar la ruta
            Solicitud solicitud = solicitudService.findById(idSolicitud);
            String rutaSolicitud = solicitud.getRutaSolicitud();

            String nombreArchivo = "FIRMADO_POR_EMPRESA " + idSolicitud + ".pdf";

            // Guardar el archivo en la ruta especificada
            archivoServiceImpl.guardarArchivo(archivo, rutaSolicitud, nombreArchivo);
            mensajeService.actualizarNotificacion(idSolicitud, destinatario, remitente);
            solicitudService.cambiarEstadoSolicitud(idSolicitud, estadoSolicitud, remitente);
            emailService.enviarEmail(destinatarioCorreo, remitenteCorreo,
                    "Solicitud pendiente. Revisa tu bandeja de entrada.");

            PRG.info("Correción enviada correctamente.");
        } catch (IOException e) {
            PRG.error("Error al subir el archivo.", "/profesor/solicitudesAprobados");
        }

        return "redirect: ../";
    }

    // ---------------------------DIRECCION /////////////////

    // LISTADO PARA DIRECCIÓN
    @GetMapping("/pendientesDireccionLista")
    public String pendienteDeAprobarDireccion(ModelMap m, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        m.addAttribute("nombreUsuario", usuario.getNombre()); // Agregar nombre del usuario al modelo

        // Obtener los mensajes recibidos por el usuario
        EstadoSolicitud estadoPendienteDireccion = EstadoSolicitud.PENDIENTE_FIRMA_DIRECCION;
        List<Mensaje> mensajes = mensajeService.recibirMensajes(usuario);
        m.put("estadoPendiente", estadoPendienteDireccion);
        m.put("mensajes", mensajes);
        m.put("view", "direccion/solicitudesPendientes");

        return "_t/frame";
    }

    // SOLICITUD UNA A UNA
    @GetMapping("/solicitudPendiente")
    public String solicitudTratadoDireccion(ModelMap m, @RequestParam("id") String idSolicitud) {
        Solicitud solicitud = solicitudService.findById(idSolicitud);
        m.put("solicitud", solicitud);
        m.put("view", "direccion/solicitudPendiente");

        return "_t/frame";
    }

    // ARCHIVO Y NOTIFICACIÓN DIRECCIÓN PROFESOR
    @PostMapping("/solicitudAceptadaDireccion")
    public String solicitudFinalizacion(
            @RequestParam("idSolicitud") String idSolicitud,
            @RequestParam("archivoPDF") MultipartFile archivo) throws Exception {

        // // Verificar si el archivo está vacío
        // if (archivo.isEmpty()) {
        // PRG.error("Por favor, seleccione un archivo antes de enviar.",
        // "/direccion/solicitudesPendientes");
        // return "redirect:/direccion/solicitudesPendientes";
        // }

        if (archivo == null || archivo.isEmpty()) {
            PRG.error("El archivo no ha sido seleccionado", "../");
        }

        Mensaje mensaje = mensajeService.findBySolicitudIdSolicitud(idSolicitud);
        Usuario destinatario = mensaje.getRemitente();
        Usuario remitente = mensaje.getDestinatario();
        String destinatarioCorreo = destinatario.getCorreo();
        String remitenteCorreo = remitente.getCorreo();
        EstadoSolicitud estadoSolicitud = EstadoSolicitud.SOLICITUD_FINALIZADA;

        try {
            // Obtener la solicitud para recuperar la ruta
            Solicitud solicitud = solicitudService.findById(idSolicitud);
            String rutaSolicitud = solicitud.getRutaSolicitud();

            String nombreArchivo = "SOLICITUD_FINALIZADA " + idSolicitud + ".pdf";

            // Guardar el archivo en la ruta especificada
            archivoServiceImpl.guardarArchivo(archivo, rutaSolicitud, nombreArchivo);

            // Actualizar la notificación y el estado de la solicitud
            mensajeService.actualizarNotificacion(idSolicitud, destinatario, remitente);
            solicitudService.cambiarEstadoSolicitud(idSolicitud, estadoSolicitud, remitente);

            // Enviar el correo
            emailService.enviarEmail(destinatarioCorreo, remitenteCorreo,
                    "Solicitud aceptada. Revisa tu bandeja de entrada.");

            PRG.info("Corrección enviada correctamente.");
        } catch (IOException e) {
            PRG.error("Error al subir el archivo.", "/direccion/solicitudesPendientes");
        }

        return "redirect: ../";
    }

    // MENSAJE Y NOTIFICACIÓN
    @PostMapping("/corregirDatosDireccionObservaciones")
    public String enviarObservacionACorregir(
            @RequestParam("idSolicitud") String idSolicitud,
            @RequestParam("observaciones") String observaciones) throws Exception {

        if (observaciones == null || observaciones.isEmpty()) {
            PRG.error("Las observaciones estan vacias", "../");
        }
        Mensaje mensaje = mensajeService.findBySolicitudIdSolicitud(idSolicitud);
        // Invertimos el correo de vuelta
        Usuario destinatario = mensaje.getRemitente();
        Usuario remitente = mensaje.getDestinatario();
        String destinatarioCorreo = destinatario.getCorreo();
        String remitenteCorreo = remitente.getCorreo();
        EstadoSolicitud estadoSolicitud = EstadoSolicitud.RECHAZADO_DIRECCION;

        try {
            mensajeService.actualizarMensaje(idSolicitud, destinatario, remitente, observaciones);
            solicitudService.cambiarEstadoSolicitud(idSolicitud, estadoSolicitud, remitente);
            emailService.enviarEmail(destinatarioCorreo, remitenteCorreo, "Datos pendientes de ser revisados.");
            PRG.info("Corrección enviada correctamente.");
        } catch (IOException e) {
            PRG.error("Error al subir el archivo.", "/direccion/solicitudesPendientes");
        }
        return "redirect:/";
    }

    // ESTOS 3 METODOS SON PARA DESCARGAR LOS ARCHIVOS
    @GetMapping("/descargarSolicitudAprobadaJefatura/{idSolicitud}")
    public void descargarSolicitudAprobadaJefatura(@PathVariable String idSolicitud, HttpServletResponse response)
            throws IOException, DangerException {
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
    public void descargarSolicitudFirmadaEmpresa(@PathVariable String idSolicitud, HttpServletResponse response)
            throws IOException, DangerException {
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
    public void descargarSolicitudFirmadaDireccion(@PathVariable String idSolicitud, HttpServletResponse response)
            throws IOException, DangerException {
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

    @PostMapping("/errorSinSeleccionarJeftura")
    public void errorSinSeleccionarJefatura()
            throws IOException, DangerException {
        PRG.error("Tienes que seleccionar una opción.", "/enviarDatos/recibirDatosJefatura");
    }

    @PostMapping("/errorSinSeleccionarDireccion")
    public void errorSinSeleccionarDireccion()
            throws IOException, DangerException {
        PRG.error("Tienes que seleccionar una opción.", "/enviarDatos/pendientesDireccionLista");
    }
}
