package com.salvaceloisma.tfg.controller.web;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.salvaceloisma.tfg.exception.DangerException;
import com.salvaceloisma.tfg.exception.InfoException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.UUID;

@RequestMapping("/crearDocumento")
@Controller
public class FormularioController {
    @GetMapping("/enviarDocumento")
    public String enviarDocumento(ModelMap m) {
        m.put("view", "documento/enviarDocumento");
        return "_t/frame";
    }

    @GetMapping("/crearDocumento")
    public String crearDocumento(ModelMap m) {
        m.put("view", "documento/crearDocumento");
        return "_t/frame";
    }

    @PostMapping("/crearDocumento")
    public String crearDocumento(HttpServletResponse response, @RequestParam String numeroConvenio,@RequestParam String nombreEmpresa,@RequestParam String tutorEmpresa,
    @RequestParam String cifEmpresa, @RequestParam String direccionPracticas, @RequestParam String localidadPracticas, @RequestParam String codigoPostalPracticas, 
    @RequestParam String apellidosAlumno, @RequestParam String nombreAlumno, @RequestParam String nifAlumno, @RequestParam String cicloFormativoAlumno, 
    @RequestParam String tutorAlumno, @RequestParam String nifTutorAlumno, @RequestParam String fechaDeNacimientoAlumno, @RequestParam String horasTotales, 
    @RequestParam String fechaInicio, @RequestParam String fechaFin, @RequestParam LocalTime lunesInicio1, @RequestParam LocalTime martesInicio1, @RequestParam LocalTime lunesFin1, 
    @RequestParam LocalTime martesFin1, @RequestParam LocalTime miercolesInicio1, @RequestParam LocalTime juevesInicio1, @RequestParam LocalTime viernesInicio1, 
    @RequestParam LocalTime miercolesFin1, @RequestParam LocalTime juevesFin1, @RequestParam LocalTime viernesFin1, @RequestParam LocalTime lunesInicio2, @RequestParam LocalTime miercolesInicio2, 
    @RequestParam LocalTime martesInicio2, @RequestParam LocalTime juevesInicio2, @RequestParam LocalTime viernesInicio2, @RequestParam LocalTime lunesFin2, @RequestParam LocalTime martesFin2, 
    @RequestParam LocalTime miercolesFin2, @RequestParam LocalTime juevesFin2, @RequestParam LocalTime viernesFin2, @RequestParam String horasDia) throws DangerException, InfoException {
        try {
            // Rellenar los campos del formulario
            // ...

            // Generate a unique file name
            String fileName = UUID.randomUUID().toString() + ".pdf";

            // Save the filled PDF document to a file
            try (PDDocument documento = PDDocument.load(new File("PDF/PlantillaPDF.pdf"))) {
                PDDocumentCatalog docCatalog = documento.getDocumentCatalog();
                PDAcroForm acroForm = docCatalog.getAcroForm();

                // Rellenar los campos del formulario
                acroForm.getField("NumeroConvenio").setValue(numeroConvenio);
                acroForm.getField("NombreEmpresa").setValue(nombreEmpresa);
                acroForm.getField("TutorEmpresa").setValue(tutorEmpresa);
                acroForm.getField("CIFEmpresa").setValue(cifEmpresa);
                acroForm.getField("DireccionPracticas").setValue(direccionPracticas);
                acroForm.getField("LocalidadPracticas").setValue(localidadPracticas);
                acroForm.getField("CodigoPostalPracticas").setValue(codigoPostalPracticas);
                acroForm.getField("ApellidosAlumno").setValue(apellidosAlumno);
                acroForm.getField("NombreAlumno").setValue(nombreAlumno);
                acroForm.getField("NIFAlumno").setValue(nifAlumno);
                acroForm.getField("CicloFormativoAlumno").setValue(cicloFormativoAlumno);
                acroForm.getField("TutorAlumno").setValue(tutorAlumno);
                acroForm.getField("NIFTutorAlumno").setValue(nifTutorAlumno);
                acroForm.getField("FechaDeNacimientoAlumno").setValue(fechaDeNacimientoAlumno);
                acroForm.getField("HorasTotales").setValue(horasTotales);
                acroForm.getField("FechaInicio").setValue(fechaInicio);
                acroForm.getField("FechaFin").setValue(fechaFin);
                acroForm.getField("1LunesInicio").setValue(lunesInicio1.toString());
                acroForm.getField("1MartesInicio").setValue(martesInicio1.toString());
                acroForm.getField("1LunesFin").setValue(lunesFin1.toString());
                acroForm.getField("1MartesFin").setValue(martesFin1.toString());
                acroForm.getField("1MiercolesInicio").setValue(miercolesInicio1.toString());
                acroForm.getField("1JuevesInicio").setValue(juevesInicio1.toString());
                acroForm.getField("1ViernesInicio").setValue(viernesInicio1.toString());
                acroForm.getField("1MiercolesFin").setValue(miercolesFin1.toString());
                acroForm.getField("1JuevesFin").setValue(juevesFin1.toString());
                acroForm.getField("1ViernesFin").setValue(viernesFin1.toString());
                acroForm.getField("2LunesInicio").setValue(lunesInicio2.toString());
                acroForm.getField("2MartesInicio").setValue(martesInicio2.toString());
                acroForm.getField("2MiercolesInicio").setValue(miercolesInicio2.toString());
                acroForm.getField("2JuevesInicio").setValue(juevesInicio2.toString());
                acroForm.getField("2ViernesInicio").setValue(viernesInicio2.toString());
                acroForm.getField("2LunesFin").setValue(lunesFin2.toString());
                acroForm.getField("2MartesFin").setValue(martesFin2.toString());
                acroForm.getField("2MiercolesFin").setValue(miercolesFin2.toString());
                acroForm.getField("2JuevesFin").setValue(juevesFin2.toString());
                acroForm.getField("2ViernesFin").setValue(viernesFin2.toString());
                acroForm.getField("HorasDia").setValue(horasDia);

                // Save the filled PDF document to a file
                documento.save(new File("PDF/" + fileName));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Set the response headers
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            // Redirigir al usuario a la página principal
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }
}
