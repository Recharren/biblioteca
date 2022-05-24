package com.proyecto.proyectoBSM.repositorios;

import com.proyecto.proyectoBSM.entidades.Prestamo;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, Integer> {
    
    //se listan los prestamos que no esten vencidos
    @Query("SELECT a FROM Prestamo a WHERE cliente.id = :id AND a.fechaDevolucion>= :vencimiento")
    public List<Prestamo> listarPrestamosPorUsuario(@Param("id")Integer id, @Param ("vencimiento") Date vencimiento);
    
}
