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
import com.salvaceloisma.tfg.service.EmpresaService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/empresa")
@Controller
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping("r")
    public String r(
            ModelMap m) {
        m.put("empresas", empresaService.findAll());
        m.put("view", "empresa/r");
        return "_t/frame";
    }

    @GetMapping("c")
    public String c(
            ModelMap m,
            HttpSession s) {

        m.put("view", "empresa/c");
        return "_t/frame";
    }

    @PostMapping("c")
    public String cPost(
            @RequestParam("nombre") String nombre, HttpSession s) throws Exception {
 
        try {
            empresaService.save(nombre);
        } catch (Exception e) {
            PRG.error("El empresa " + nombre + " ya existe", "/empresa/c");
        }
        return "redirect:/empresa/r";
    }

    @GetMapping("u")
    public String update(
            @RequestParam("id") Long idEmpresa,
            ModelMap m) {
        m.put("empresa", empresaService.findById(idEmpresa));
        m.put("view", "empresa/u");
        return "_t/frame";
    }

    @PostMapping("u")
    public String updatePost(
            @RequestParam("id") Long idEmpresa,
            @RequestParam("nombre") String nombre) throws DangerException {
        try {
            empresaService.update(idEmpresa, nombre);
        } catch (Exception e) {
            PRG.error("El empresa no pudo ser actualizado", "/empresa/r");
        }
        return "redirect:/empresa/r";
    }

    @PostMapping("d")
    public String delete(
            @RequestParam("id") Long idEmpresa) throws DangerException {
        try {
            empresaService.delete(idEmpresa);
        } catch (Exception e) {
            PRG.error("No se puede borrar la empresa", "/empresa/r");
        }
        return "redirect:/empresa/r";
    }
}
