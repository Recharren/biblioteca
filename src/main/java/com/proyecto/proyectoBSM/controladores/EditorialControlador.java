package com.proyecto.proyectoBSM.controladores;

import com.proyecto.proyectoBSM.entidades.Editorial;
import com.proyecto.proyectoBSM.excepciones.MiExcepcion;
import com.proyecto.proyectoBSM.servicios.EditorialServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EditorialControlador {

    @Autowired
    EditorialServicio editorialServicio;

//se registra una nueva editorial (Solo visible por el admin)    
    @PostMapping("/registrarEditorial")
    public String registrarEditorial(ModelMap modelo, @RequestParam String nombre) throws MiExcepcion {

        try {
            editorialServicio.crearEditorial(nombre);
        } catch (MiExcepcion e) {
            modelo.put("error", e.getMessage());
            return "editorial.html";
        }
        modelo.put("mensaje", "La editorial " + nombre + " se registro correctamente");
        return "index.html";
    }

//se listan todas las editoriales (Solo visible por el admin)
    @GetMapping("/listarEditoriales")
    public String listarEditoriales(ModelMap modelo) throws MiExcepcion {
        try {
            List<Editorial> editoriales = editorialServicio.listarEditoriales();
            modelo.put("editoriales", editoriales);
        } catch (MiExcepcion ex) {
            throw new MiExcepcion("ERROR: al buscar editoriales.");
        }
        return "listadoEditoriales.html";
    }

//se selecciona una editorial de la lista para modificarla (Solo visible por el admin)     
    @GetMapping("/listarEditorial/{id}")
    public String listarEditorial(ModelMap modelo, @PathVariable Integer id) {

        Editorial editorial = editorialServicio.buscarEditorialPorId(id);
        modelo.put("editorial", editorial);

        return "formularioEditorial.html";
    }

//se modifica una editorial (Solo visible por el admin)
    @PostMapping("/modificarEditorial")
    public String modificarEditorial(ModelMap modelo, @RequestParam Integer id, @RequestParam String nombre) throws MiExcepcion {
        try {
            editorialServicio.modificarEditorial(id, nombre);
        } catch (Exception e) {
            throw new MiExcepcion("ERROR: Algo paso al modificar la editorial.");
        }

        modelo.put("mensaje", "La editorial " + nombre + " se actualizo correctamente.");

        return "listadoEditoriales.html";
    }

//se da de alta una editorial (Solo visible por el admin)
    @GetMapping("/altaEditorial/{id}")
    public String altaEditorial(ModelMap modelo, @PathVariable Integer id) throws MiExcepcion {
        try {
            editorialServicio.altaEditorial(id);
            return "redirect:/listarEditoriales";
        } catch (Exception e) {
            throw new MiExcepcion("ERROR: Algo paso al intentar dar de alta la Editorial.");
        }
    }

//se da de baja una editorial (Solo visible por el admin)
    @GetMapping("/bajaEditorial/{id}")
    public String bajaEditorial(ModelMap modelo, @PathVariable Integer id) throws MiExcepcion {
        try {
            editorialServicio.bajaEditorial(id);
            return "redirect:/listarEditoriales";
        } catch (Exception e) {
            throw new MiExcepcion("ERROR: Algo paso al intentar dar de baja la Editorial.");
        }
    }

}
