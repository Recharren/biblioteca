package com.proyecto.proyectoBSM.controladores;

import com.proyecto.proyectoBSM.entidades.Libro;
import com.proyecto.proyectoBSM.entidades.Prestamo;
import com.proyecto.proyectoBSM.excepciones.MiExcepcion;
import com.proyecto.proyectoBSM.servicios.LibroServicio;
import com.proyecto.proyectoBSM.servicios.PrestamoServicio;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PrestamoControlador {

    @Autowired
    private PrestamoServicio prestamoServicio;
    @Autowired
    private LibroServicio libroServicio;

    //se selecciona un libro de la lista para su reserva
    @GetMapping("/crearPrestamoUsuario/{id}")
    public String crearPrestamoUsuario(ModelMap modelo, @PathVariable Integer id) throws Exception {
        Prestamo prestamo = new Prestamo();
        try {
            Libro libro = libroServicio.buscarLibroPorId(id);
            modelo.put("libro", libro);
            modelo.put("prestamo", prestamo);
        } catch (Exception ex) {
            throw new MiExcepcion(ex.getMessage());
        }
        return "prestamo.html";
    }

//se registra un prestamo del libro seleccionado al dni del cliente (Solo visible por el admin)   
    @PostMapping("/registrarPrestamo")
    public String registrarPrestamo(ModelMap modelo, @RequestParam Libro libro, @RequestParam Long dni,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaPrestamo, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDevolucion) throws MiExcepcion {
        try {
            prestamoServicio.registrarPrestamo(fechaPrestamo, fechaDevolucion, libro, dni);
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            return "index.html";
        }
        modelo.put("mensaje", "El prestamo se registro correctamente");
        return "index.html";
    }

//se registra el prestamo del libro seleccionado
    @PostMapping("/registrarPrestamoUsuario")
    public String registrarPrestamoUsuario(ModelMap modelo, @RequestParam Integer idLibro, @RequestParam Long dni,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaPrestamo, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDevolucion) throws MiExcepcion {
        try {
            prestamoServicio.registrarPrestamoUsuario(fechaPrestamo, fechaDevolucion, idLibro, dni);
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            return "index.html";
        }
        modelo.put("mensaje", "El prestamo se registro correctamente");
        return "index.html";
    }

//se listan todos los prestamos (Solo visible por el admin)
    @GetMapping("/listarPrestamos")
    public String listarPrestamos(ModelMap modelo) throws Exception {
        try {
            List<Prestamo> prestamos = prestamoServicio.listarPrestamos();
            modelo.put("prestamos", prestamos);
        } catch (Exception ex) {
            throw new MiExcepcion("ERROR: al buscar Libros.");
        }
        return "listadoPrestamos.html";
    }

    //se da de baja un prestamo (Solo visible por el admin)
    @GetMapping("/bajaPrestamo/{id}")
    public String bajaPrestamo(ModelMap modelo, @PathVariable Integer id) throws MiExcepcion {
        try {
            prestamoServicio.bajaPrestamo(id);
            return "redirect:/listarPrestamos";
        } catch (Exception e) {
            throw new MiExcepcion("ERROR: Algo paso al intentar dar de alta un Prestamo.");
        }
    }

    //se da de alta un prestamo (Solo visible por el admin)
    @GetMapping("/altaPrestamo/{id}")
    public String altaPrestamo(ModelMap modelo, @PathVariable Integer id) throws MiExcepcion {
        try {
            prestamoServicio.altaPrestamo(id);
            return "redirect:/listarPrestamos";
        } catch (Exception e) {
            throw new MiExcepcion("ERROR: Algo paso al intentar dar de alta un Prestamo.");
        }
    }

    //se selecciona un prestamo para su modificacion (Solo visible por el admin)
    @GetMapping("/listarPrestamo/{id}")
    public String listarPrestamo(ModelMap modelo, @PathVariable Integer id) throws MiExcepcion {
        Prestamo prestamo = prestamoServicio.buscarPrestamoPorId(id);
        modelo.put("prestamo", prestamo);

        return "formularioPrestamo.html";
    }

    //se modifica un prestamo (Solo visible por el admin)
    @PostMapping("/modificarPrestamo")
    public String modificarPrestamo(ModelMap modelo, @RequestParam Integer id,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaDevolucion) throws MiExcepcion {
        prestamoServicio.modificarPrestamo(id, fechaDevolucion);
        modelo.put("mensaje", "El prestamo se modifico correctamente");
        return "index.html";
    }

    //se listan los prestamos de un cliente
    @GetMapping("/listarPrestamosPorUsuario/{id}")
    public String listarPrestamosPorUsuario(ModelMap modelo, @PathVariable Integer id) throws MiExcepcion {

        List<Prestamo> prestamos = prestamoServicio.listarPrestamosPorUsuario(id);

        modelo.put("prestamos", prestamos);

        return "listadoPrestamos.html";
    }
}
