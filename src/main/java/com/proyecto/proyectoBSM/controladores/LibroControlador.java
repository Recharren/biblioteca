package com.proyecto.proyectoBSM.controladores;

import com.proyecto.proyectoBSM.entidades.Libro;
import com.proyecto.proyectoBSM.excepciones.MiExcepcion;
import com.proyecto.proyectoBSM.servicios.LibroServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LibroControlador {

    @Autowired
    LibroServicio libroServicio;

    //se registra un nuevo libro (Solo visible por el admin)
    @PostMapping("/registrarLibro")
    public String registrarLibro(ModelMap modelo, @RequestParam Long isbn, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrest, @RequestParam String autor, @RequestParam String editorial) {

        try {
            libroServicio.registrarLibro(isbn, titulo, anio, ejemplares, ejemplaresPrest, autor, editorial);
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("isbn", isbn);
            modelo.put("titulo", titulo);
            modelo.put("anio", anio);
            modelo.put("ejemplares", ejemplares);
            modelo.put("ejemplaresPrest", ejemplaresPrest);
            modelo.put("autor", autor);
            modelo.put("editorial", editorial);
            return "libro.html";
        }
        modelo.put("mensaje", "El libro " + titulo + " se registro correctamente");
        return "index.html";
    }

    //se da de alta una editorial (Solo visible por el admin)
    @GetMapping("/altaLibro/{id}")
    public String altaLibro(ModelMap modelo, @PathVariable Integer id) throws MiExcepcion {
        try {
            libroServicio.altaLibro(id);
            return "redirect:/listarLibros";
        } catch (Exception e) {
            throw new MiExcepcion("ERROR: Algo paso al intentar dar de alta Libro.");
        }
    }

    //se da de baja una editorial (Solo visible por el admin) 
    @GetMapping("/bajaLibro/{id}")
    public String bajaLibro(ModelMap modelo, @PathVariable Integer id) throws MiExcepcion {
        try {
            libroServicio.bajaLibro(id);
            return "redirect:/listarLibros";
        } catch (Exception e) {
            throw new MiExcepcion("ERROR: Algo paso al intentar dar de alta Libro.");
        }
    }

    //se listan todos los libros (Solo visible por el admin) 
    @GetMapping("/listarLibros")
    public String listarLibros(ModelMap modelo) throws Exception {
        try {
            List<Libro> libros = libroServicio.listarLibros();
            modelo.put("libros", libros);
        } catch (Exception ex) {
            throw new MiExcepcion("ERROR: al buscar Libros.");
        }
        return "listadoLibros.html";
    }

    //se selecciona un libro de la lista para modificarlo (Solo visible por el admin)    
    @GetMapping("/listarLibro/{id}")
    public String listarLibro(ModelMap modelo, @PathVariable Integer id) {
        Libro libro = libroServicio.buscarLibroPorId(id);
        modelo.put("libro", libro);
        return "formularioLibro.html";
    }

    //se modifica un libro (Solo visible por el admin)
    @PostMapping("/modificarLibro")
    public String modificarLibro(ModelMap modelo, @RequestParam Integer id, @RequestParam Long isbn,
                                 @RequestParam String titulo, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrestados,
                                 @RequestParam String autor, @RequestParam String editorial) throws MiExcepcion {
        String error = null;
        try {
            libroServicio.modificarLibro(id, isbn, titulo, anio, ejemplares, ejemplaresPrestados, autor, editorial);
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            return "index.html";
        }

        List<Libro> libros = libroServicio.listarLibros();
        modelo.put("libros", libros);

        modelo.put("mensaje", "El libro " + titulo + " se actualizo correctamente.");
        return "index.html";
    }

    //se busca un libro segun Titulo u Autor
    @PostMapping("/buscarLibro")
    public String buscarLibro(ModelMap modelo, @RequestParam Integer filtro, @RequestParam String palabra) {

        switch (filtro) {
            case 0: {
                try {
                    List<Libro> libros = libroServicio.buscarLibroPorTitulo(palabra);
                    modelo.put("libros", libros);
                } catch (MiExcepcion e) {
                    modelo.put("error", e.getMessage());
                    return "index.html";
                }
                return "listadoLibros.html";
            }
            case 1: {
                try {
                    List<Libro> libros = libroServicio.buscarLibroPorNombreAutor(palabra);
                    modelo.put("libros", libros);
                } catch (MiExcepcion e) {
                    modelo.put("error", e.getMessage());
                    return "index.html";
                }
                return "listadoLibros.html";
            }
        }
        return "";
    }

    // Carga 10 libros de ejemplo para poder utilizar la App
    @GetMapping("/cargarLibrosEjemplos")
    public String cargarLibrosEjemplos(ModelMap modelo) {
        try {
            libroServicio.cargarLibrosEjemplos();
            modelo.put("mensaje", "Los libros se cargaron correctamente");
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
        }
        List<Libro> libros = libroServicio.listarLibros();
        modelo.put("libros", libros);
        return "listadoLibros.html";
    }

}
