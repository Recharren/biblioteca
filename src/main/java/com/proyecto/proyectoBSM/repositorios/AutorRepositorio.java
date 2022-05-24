package com.proyecto.proyectoBSM.repositorios;

import com.proyecto.proyectoBSM.entidades.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepositorio extends JpaRepository<Autor, Integer> {
    @Query("SELECT a FROM Autor a WHERE a.nombre LIKE %:nombreautor% AND a.alta = true")
    public Autor buscarAutorPorNombre(@Param("nombreautor")String nombreAutor);
}
