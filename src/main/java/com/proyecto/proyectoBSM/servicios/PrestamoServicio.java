package com.proyecto.proyectoBSM.servicios;

import com.proyecto.proyectoBSM.entidades.Cliente;
import com.proyecto.proyectoBSM.entidades.Libro;
import com.proyecto.proyectoBSM.entidades.Prestamo;
import com.proyecto.proyectoBSM.excepciones.MiExcepcion;
import com.proyecto.proyectoBSM.repositorios.ClienteRepositorio;
import com.proyecto.proyectoBSM.repositorios.LibroRepositorio;
import com.proyecto.proyectoBSM.repositorios.PrestamoRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrestamoServicio {

    @Autowired
    private PrestamoRepositorio prestamoRepositorio;

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    //se da de alta un prestamo
    @Transactional
    public void altaPrestamo(Integer id) throws MiExcepcion {
        Optional<Prestamo> auxiliar = prestamoRepositorio.findById(id);
        if (auxiliar.isPresent()) {
            Prestamo prestamo = auxiliar.get();
            prestamo.setAlta(true);

            prestamoRepositorio.save(prestamo);
        } else {
            throw new MiExcepcion("ERROR: No se encontro el Prestamo para dar de alta.");
        }
    }

    //se da de baja un prestamo
    @Transactional
    public void bajaPrestamo(Integer id) throws MiExcepcion {
        Optional<Prestamo> auxiliar = prestamoRepositorio.findById(id);
        if (auxiliar.isPresent()) {
            Prestamo prestamo = auxiliar.get();
            prestamo.setAlta(false);

            prestamoRepositorio.save(prestamo);
        } else {
            throw new MiExcepcion("ERROR: No se encontro el Prestamo para dar de baja.");
        }
    }

    //se modifican los datos de un prestamo
    @Transactional
    public void modificarPrestamo(Integer id, Date fechaDevolucion) throws MiExcepcion {

        Optional<Prestamo> auxiliar = prestamoRepositorio.findById(id);
        if (auxiliar.isPresent()) {
            Prestamo prestamo = auxiliar.get();
            prestamo.setFechaDevolucion(fechaDevolucion);

            prestamoRepositorio.save(prestamo);
        } else {
            throw new MiExcepcion("ERROR: no se encontro el prestamo correspondiente a este ID.");
        }
    }

    //devuelve una lista de todos los prestamos
    public List<Prestamo> listarPrestamos() {
        return prestamoRepositorio.findAll();
    }

    //devuelve un prestamo segun su id
    public Prestamo buscarPrestamoPorId(Integer id) {
        return prestamoRepositorio.getById(id);
    }

    //se validan los datos ingresados al momento de generar un nuevo prestamo
    public void validar(Date fechaPrestamo, Date fechaDevolucion) throws MiExcepcion {

        if (fechaPrestamo == null) {
            throw new MiExcepcion("ERROR: La fecha de prestamo no puede ser Nula.");
        }
        if (fechaDevolucion == null) {
            throw new MiExcepcion("ERROR: La fecha de prestamo no puede ser Nula.");
        }

    }

    //se crea un nuevo prestamo
    @Transactional
    public void registrarPrestamo(Date fechaPrestamo, Date fechaDevolucion, Libro libro, Long dni) throws MiExcepcion {

        if (libroRepositorio.restantes(libro.getId()) > 0) {

            if (buscarClientePorDocumento(dni) != null) {
                Prestamo prestamo = new Prestamo();
                prestamo.setFechaPrestamo(fechaPrestamo);
                prestamo.setFechaDevolucion(fechaDevolucion);
                prestamo.setAlta(true);
                prestamo.setLibro(libro);
                prestamo.setCliente(buscarClientePorDocumento(dni));
                libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() + 1);
                libro.setEjemplaresRestantes(libro.getEjemplaresRestantes() - 1);

                libroRepositorio.save(libro);
                prestamoRepositorio.save(prestamo);
            } else {
                throw new MiExcepcion("ERROR: no se encontro Cliente con ese DNI");
            }

        } else {
            throw new MiExcepcion("NO hay suficientes libros de " + libro.getTitulo() + " para prestar.");
        }

    }

    //se registra un prestamo segun el libro seleccionado
    @Transactional
    public void registrarPrestamoUsuario(Date fechaPrestamo, Date fechaDevolucion, Integer idLibro, Long dni) throws MiExcepcion {

        Libro libro = libroRepositorio.getById(idLibro);

        if (libroRepositorio.restantes(libro.getId()) > 0) {

            if (buscarClientePorDocumento(dni) != null) {
                Prestamo prestamo = new Prestamo();
                prestamo.setFechaPrestamo(fechaPrestamo);
                prestamo.setFechaDevolucion(fechaDevolucion);
                prestamo.setAlta(true);
                prestamo.setLibro(libro);
                prestamo.setCliente(buscarClientePorDocumento(dni));
                libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() + 1);
                libro.setEjemplaresRestantes(libro.getEjemplaresRestantes() - 1);

                libroRepositorio.save(libro);
                prestamoRepositorio.save(prestamo);
            } else {
                throw new MiExcepcion("ERROR: no se encontro Cliente con ese DNI");
            }

        } else {
            throw new MiExcepcion("NO hay suficientes libros de " + libro.getTitulo() + " para prestar.");
        }
    }

    //devuelve un cliente segun su DNI
    private Cliente buscarClientePorDocumento(Long dni) {
        return clienteRepositorio.buscarClientePorDocumento(dni);
    }

    //devuelve la lista de prestamos que realizo un usuario
    public List<Prestamo> listarPrestamosPorUsuario(Integer id) {
        Date vencimiento = new Date();
        return prestamoRepositorio.listarPrestamosPorUsuario(id, vencimiento);
    }
}
