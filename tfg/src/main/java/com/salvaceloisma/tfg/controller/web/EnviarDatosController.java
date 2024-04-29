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
import com.salvaceloisma.tfg.service.EmailService;
import com.salvaceloisma.tfg.service.InicioSesionService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.time.LocalTime;
import java.util.List;

@RequestMapping("/enviarDatos")
@Controller
public class EnviarDatosController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private InicioSesionService inicioSesionService;

    @GetMapping("/enviarDatosAJefatura")
    public String crearDocumento(ModelMap m) {
        List<Usuario> usuariosJefatura = inicioSesionService.obtenerUsuariosPorRol(RolUsuario.JEFATURA);
        m.put("usuariosJefatura", usuariosJefatura);
        m.put("view", "profesor/enviarDatosAlumnos");
        return "_t/frame";
    }

    @PostMapping("/enviarDatosAJefatura")
    public String crearDocumento(HttpServletResponse response, HttpSession session, @RequestParam String numeroConvenio,
            @RequestParam String tutorAlumno, @RequestParam String nifTutorAlumno,
            @RequestParam String nombreEmpresa, @RequestParam String tutorEmpresa,
            @RequestParam String cifEmpresa, @RequestParam String direccionPracticas,
            @RequestParam String localidadPracticas, @RequestParam String codigoPostalPracticas,
            @RequestParam String[] apellidosAlumno, @RequestParam String[] nombreAlumno, @RequestParam String[] nifAlumno,
            @RequestParam String[] cicloFormativoAlumno,
            @RequestParam String[] fechaDeNacimientoAlumno, @RequestParam String horasTotales,
            @RequestParam String fechaInicio, @RequestParam String fechaFin, @RequestParam LocalTime lunesInicio1,
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
            @RequestParam LocalTime viernesFin2, @RequestParam String horasDia,
            @RequestParam("rolUsuario") Long usuarioEnvio, ModelMap m)
            throws DangerException, InfoException {
    
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String nombre = usuario.getNombre();
    
        String correo = inicioSesionService.findById(usuarioEnvio).getCorreo();
    
        StringBuilder datos = new StringBuilder();
    
        // Construir el mensaje para cada alumno
        for (int i = 0; i < apellidosAlumno.length; i++) {
            datos.append("Alumno ").append(i + 1).append(":\n");
            datos.append("Apellidos alumno: ").append(apellidosAlumno[i]).append("\n");
            datos.append("Nombre alumno: ").append(nombreAlumno[i]).append("\n");
            datos.append("NIF alumno: ").append(nifAlumno[i]).append("\n");
            datos.append("Ciclo formativo alumno: ").append(cicloFormativoAlumno[i]).append("\n");
            datos.append("Fecha de nacimiento alumno: ").append(fechaDeNacimientoAlumno[i]).append("\n");
            datos.append("\n");
        }
    
        // Agregar los datos comunes al final
        datos.append("Datos comunes:\n");
        datos.append("Tutor alumno: ").append(tutorAlumno).append("\n");
        datos.append("NIF tutor alumno: ").append(nifTutorAlumno).append("\n");
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
    
        try {
            emailService.enviarEmail(correo, "Envio de datos de " + nombre, "Estos son los datos:\n" + datos.toString());
        } catch (Exception e) {
            PRG.error("El correo no puedo enviarse correctamente.");
        }
        
        return "redirect:../";
    }
    
}
