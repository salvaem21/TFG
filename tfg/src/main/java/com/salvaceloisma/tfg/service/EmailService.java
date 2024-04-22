package com.salvaceloisma.tfg.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ByteArrayResource;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmailConArchivo(String receptor, String cabecera, String texto, MultipartFile archivo)
            throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(receptor);
        helper.setSubject(cabecera);
        helper.setText(texto);

        // Adjuntar el archivo al mensaje
        helper.addAttachment(archivo.getOriginalFilename(), new ByteArrayResource(archivo.getBytes()));

        mailSender.send(message);
    }
}
