package com.proyecto.proyectoBSM.servicios;

import com.proyecto.proyectoBSM.entidades.Autor;
import com.proyecto.proyectoBSM.excepciones.MiExcepcion;
import com.proyecto.proyectoBSM.repositorios.AutorRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;

    //Se crea un autor
    @Transactional
    public void crearAutor(String nombre) throws MiExcepcion {

        if (nombre.equals(null) || nombre.isEmpty()) {
            throw new MiExcepcion("ERROR: El nombre del Autor es incorrecto.");
        }
        Autor autor = new Autor();
        autor.setNombre(nombre);
        autor.setAlta(true);
        autorRepositorio.save(autor);
    }

    //se listan todos los autores
    public List<Autor> listarAutores() throws MiExcepcion {
        return autorRepositorio.findAll();
    }

    //devuelve un autor segun su id
    public Autor buscarAutorPorId(Integer id) {
        return autorRepositorio.getById(id);
    }

    //se modifica un autor
    @Transactional
    public void modificarAutor(Integer id, String nombre) throws MiExcepcion {

        Optional<Autor> auxiliar = autorRepositorio.findById(id);
        if (auxiliar.isPresent()) {
            Autor autor = auxiliar.get();
            autor.setNombre(nombre);

            autorRepositorio.save(autor);
        } else {
            throw new MiExcepcion("ERROR: No se encontro el Autor para modificar.");
        }
    }

    //se da de alta un autor
    @Transactional
    public void altaAutor(Integer id) throws MiExcepcion {
        Optional<Autor> auxiliar = autorRepositorio.findById(id);
        if (auxiliar.isPresent()) {
            Autor autor = auxiliar.get();
            autor.setAlta(true);

            autorRepositorio.save(autor);
        } else {
            throw new MiExcepcion("ERROR: No se encontro el Autor para dar de alta.");
        }
    }

    //se da de baja un autor
    @Transactional
    public void bajaAutor(Integer id) throws MiExcepcion {
        Optional<Autor> auxiliar = autorRepositorio.findById(id);
        if (auxiliar.isPresent()) {
            Autor autor = auxiliar.get();
            autor.setAlta(false);

            autorRepositorio.save(autor);
        } else {
            throw new MiExcepcion("ERROR: No se encontro el Autor para dar de baja.");
        }
    }

}
