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
import com.salvaceloisma.tfg.service._BeanService;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/_bean")
@Controller
public class _BeanController {

    @Autowired
    private _BeanService _beanService;

    @GetMapping("r")
    public String r(
            ModelMap m) {
        m.put("_beanes", _beanService.findAll());
        m.put("view", "_bean/r");
        return "_t/frame";
    }

    @GetMapping("c")
    public String c(
            ModelMap m,
            HttpSession s) {

        m.put("view", "_bean/c");
        return "_t/frame";
    }

    @PostMapping("c")
    public String cPost(
            @RequestParam("nombre") String nombre, HttpSession s) throws Exception {
 
        try {
            _beanService.save(nombre);
        } catch (Exception e) {
            PRG.error("El _bean " + nombre + " ya existe", "/_bean/c");
        }
        return "redirect:/_bean/r";
    }

    @GetMapping("u")
    public String update(
            @RequestParam("id") Long id_Bean,
            ModelMap m) {
        m.put("_bean", _beanService.findById(id_Bean));
        m.put("view", "_bean/u");
        return "_t/frame";
    }

    @PostMapping("u")
    public String updatePost(
            @RequestParam("id") Long id_Bean,
            @RequestParam("nombre") String nombre) throws DangerException {
        try {
            _beanService.update(id_Bean, nombre);
        } catch (Exception e) {
            PRG.error("El _bean no pudo ser actualizado", "/_bean/r");
        }
        return "redirect:/_bean/r";
    }

    @PostMapping("d")
    public String delete(
            @RequestParam("id") Long id_Bean) throws DangerException {
        try {
            _beanService.delete(id_Bean);
        } catch (Exception e) {
            PRG.error("No se puede borrar el _bean", "/_bean/r");
        }
        return "redirect:/_bean/r";
    }
}
