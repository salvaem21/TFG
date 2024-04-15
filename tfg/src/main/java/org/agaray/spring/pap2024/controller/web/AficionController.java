package org.agaray.spring.pap2024.controller.web;

import org.agaray.spring.pap2024.exception.DangerException;
import org.agaray.spring.pap2024.helper.PRG;
import org.agaray.spring.pap2024.service.AficionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/aficion")
@Controller
public class AficionController {

    @Autowired
    private AficionService aficionService;

    @GetMapping("r")
    public String r(
            ModelMap m) {
        m.put("aficiones", aficionService.findAll());
        m.put("view", "aficion/r");
        return "_t/frame";
    }

    @GetMapping("c")
    public String c(
            ModelMap m) {
        m.put("view", "aficion/c");
        return "_t/frame";
    }

    @PostMapping("c")
    public String cPost(
            @RequestParam("nombre") String nombre) throws DangerException {
        try {
            aficionService.save(nombre);
        } catch (Exception e) {
            PRG.error("La afición " + nombre + " ya existe", "/aficion/c");
        }
        return "redirect:/aficion/r";
    }
    
    
    @GetMapping("u")
    public String update(
        @RequestParam("id") Long idAficion,
        ModelMap m
    ) {
        m.put("aficion", aficionService.findById(idAficion));
        m.put("view", "aficion/u");
        return "_t/frame";
    }

    @PostMapping("u")
    public String updatePost(
        @RequestParam("idAficion") Long idAficion,
        @RequestParam("nombre") String nombre
    ) throws DangerException {
        try {
            aficionService.update(idAficion, nombre);
        }
        catch (Exception e) {
            PRG.error("La afición "+nombre+" ya está registrada","/aficion/r");
        }
        return "redirect:/aficion/r";
    }


}
