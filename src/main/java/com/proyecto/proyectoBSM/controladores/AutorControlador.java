package com.proyecto.proyectoBSM.controladores;

import com.proyecto.proyectoBSM.entidades.Autor;
import com.proyecto.proyectoBSM.excepciones.MiExcepcion;
import com.proyecto.proyectoBSM.servicios.AutorServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AutorControlador {

    @Autowired
    private AutorServicio autorServicio;

//este metodo nos deriba al template autor.html para su registro (Solo visible por el admin)
    @PostMapping("/registrarAutor")
    public String registrarAutor(ModelMap modelo, @RequestParam String nombre) throws MiExcepcion {
        try {
            autorServicio.crearAutor(nombre);
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "autor.html";
        }
        modelo.put("mensaje", "El Autor " + nombre + " se registro correctamente");
        return "index.html";
    }

//lista todos los autores (Solo visible por el admin)
    @GetMapping("/listarAutores")
    public String listarAutores(ModelMap modelo) throws MiExcepcion {
        try {
            List<Autor> autores = autorServicio.listarAutores();
            modelo.put("autores", autores);
        } catch (MiExcepcion ex) {
            throw new MiExcepcion("ERROR: al buscar autores.");
        }
        return "listadoAutores.html";
    }

//Se selecciona un autor de la lista para modificarlo (Solo visible por el admin)
    @GetMapping("/listarAutor/{id}")
    public String listarAutor(ModelMap modelo, @PathVariable Integer id) {

        Autor autor = autorServicio.buscarAutorPorId(id);
        modelo.put("autor", autor);

        return "formularioAutor.html";
    }

//Se modifica el nombre del autor (Solo visible por el admin)
    @PostMapping("/modificarAutor")
    public String modificarAutor(ModelMap modelo, @RequestParam Integer id, @RequestParam String nombre) throws MiExcepcion {
        try {
            autorServicio.modificarAutor(id, nombre);
        } catch (Exception e) {
            throw new MiExcepcion("ERROR: Algo paso al modificar el Autor.");
        }
        try {
            List<Autor> autores = autorServicio.listarAutores();
            modelo.put("autores", autores);
        } catch (MiExcepcion ex) {
            throw new MiExcepcion("ERROR: al buscar autores.");
        }
        modelo.put("mensaje", "El Autor " + nombre + " se actualizo correctamente.");
        return "listadoAutores.html";

    }

//se da de alta a un autor (Solo visible por el admin)
    @GetMapping("/altaAutor/{id}")
    public String altaAutor(ModelMap modelo, @PathVariable Integer id) throws MiExcepcion {
        try {
            autorServicio.altaAutor(id);
            return "redirect:/listarAutores";
        } catch (Exception e) {
            throw new MiExcepcion("ERROR: Algo paso al intentar dar de alta el Autor.");
        }
    }

//se da de baja a un autor (Solo visible por el admin)
    @GetMapping("/bajaAutor/{id}")
    public String bajaAutor(ModelMap modelo, @PathVariable Integer id) throws MiExcepcion {
        try {
            autorServicio.bajaAutor(id);
            return "redirect:/listarAutores";
        } catch (Exception e) {
            throw new MiExcepcion("ERROR: Algo paso al intentar dar de baja el Autor.");
        }
    }

}
