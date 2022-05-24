package com.proyecto.proyectoBSM.repositorios;

import com.proyecto.proyectoBSM.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Integer> {
    
    //se busca un cliente por documento en BD
    @Query("SELECT a FROM Cliente a WHERE a.documento = :dni")
    public Cliente buscarClientePorDocumento(@Param("dni") Long dni);

    //se busca un cliente por correo electronico en BD
    @Query("SELECT a FROM Cliente a WHERE a.email = :email")
    public Cliente buscarClientePorEmail(@Param("email")String email);
}
