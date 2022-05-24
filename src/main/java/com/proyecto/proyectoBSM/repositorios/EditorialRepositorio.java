package com.proyecto.proyectoBSM.repositorios;

import com.proyecto.proyectoBSM.entidades.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, Integer> {
    
    //se busca una editorial segun su nombre en BD
    @Query("SELECT a FROM Editorial a WHERE a.nombre LIKE %:nombreEditorial%")
    public Editorial buscarEditorialPorNombre(@Param("nombreEditorial")String nombreEditorial);
}
