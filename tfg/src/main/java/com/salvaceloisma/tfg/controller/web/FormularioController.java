package com.salvaceloisma.tfg.controller.web;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@RequestMapping("/crearDocumento")
@Controller
public class FormularioController {

    @GetMapping("/crearDocumento")
    public String crearDocumento(ModelMap m) {
        m.put("view", "documento/crearDocumento");
        return "_t/frame";    }

    @PostMapping("/crearDocumento")
    public void crearDocumento(@RequestParam String nombre, @RequestParam String email, HttpServletResponse response) {
        try {
            // Creamos un nuevo documento PDF
            PDDocument documento = new PDDocument();
            PDPage pagina = new PDPage();
            documento.addPage(pagina);

            // Escribimos el contenido en la página
            PDPageContentStream contentStream = new PDPageContentStream(documento, pagina);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Nombre: " + nombre);
            contentStream.newLine();
            contentStream.showText("Email: " + email);
            contentStream.endText();
            contentStream.close();

            // Escribimos el documento en la respuesta HTTP
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=formulario.pdf");
            documento.save(response.getOutputStream());
            documento.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
