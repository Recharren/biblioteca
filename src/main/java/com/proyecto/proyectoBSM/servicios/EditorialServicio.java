package com.proyecto.proyectoBSM.servicios;

import com.proyecto.proyectoBSM.entidades.Editorial;
import com.proyecto.proyectoBSM.excepciones.MiExcepcion;
import com.proyecto.proyectoBSM.repositorios.EditorialRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditorialServicio {

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    //se crea una nueva editorial
    @Transactional
    public void crearEditorial(String nombre) throws MiExcepcion {

        if (nombre.equals(null) || nombre.isEmpty()) {
            throw new MiExcepcion("ERROR: El nombre de la Editorial no puede ser nula.");
        }

        Editorial editorial = new Editorial();
        editorial.setNombre(nombre);
        editorial.setAlta(true);
        editorialRepositorio.save(editorial);
    }

    //se listan todas las editoriales
    public List<Editorial> listarEditoriales() throws MiExcepcion {
        return editorialRepositorio.findAll();
    }

    //devuelve una editorial segun su id
    public Editorial buscarEditorialPorId(Integer id) {
        return editorialRepositorio.getById(id);
    }

    //se modifica una editorial
    @Transactional
    public void modificarEditorial(Integer id, String nombre) throws MiExcepcion {

        Optional<Editorial> auxiliar = editorialRepositorio.findById(id);
        if (auxiliar.isPresent()) {
            Editorial editorial = auxiliar.get();
            editorial.setNombre(nombre);
            editorialRepositorio.save(editorial);
        } else {
            throw new MiExcepcion("ERROR: No se encontro la editorial para modificar.");
        }
    }

    //se da de alta una editorial
    @Transactional
    public void altaEditorial(Integer id) throws MiExcepcion {
        Optional<Editorial> auxiliar = editorialRepositorio.findById(id);
        if (auxiliar.isPresent()) {
            Editorial editorial = auxiliar.get();
            editorial.setAlta(true);
            editorialRepositorio.save(editorial);
        } else {
            throw new MiExcepcion("ERROR: No se encontro la Editorial para dar de alta.");
        }
    }

    //se da de baja una editorial
    @Transactional
    public void bajaEditorial(Integer id) throws MiExcepcion {
        Optional<Editorial> auxiliar = editorialRepositorio.findById(id);
        if (auxiliar.isPresent()) {
            Editorial editorial = auxiliar.get();
            editorial.setAlta(false);
            editorialRepositorio.save(editorial);
        } else {
            throw new MiExcepcion("ERROR: No se encontro la Editorial para dar de baja.");
        }
    }
}
