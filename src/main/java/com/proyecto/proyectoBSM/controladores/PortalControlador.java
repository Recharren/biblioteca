package com.proyecto.proyectoBSM.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PortalControlador {

    //Se mapean los templates
    
    @GetMapping("/")
    public String index() {
        return "index.html";
    }

      @GetMapping("/usar")
    public String usar() {
        return "usar.html";
    }

    @GetMapping("/libro.html")
    public String libro() {
        return "libro.html";
    }

    @GetMapping("/autor.html")
    public String autor() {
        return "autor.html";
    }

    @GetMapping("/editorial.html")
    public String editorial() {
        return "editorial.html";
    }

    @GetMapping("/cliente.html")
    public String cliente() {
        return "cliente.html";
    }

    @GetMapping("/prestamo.html")
    public String prestamo() {
        return "prestamo.html";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

}
