package com.proyecto.proyectoBSM.repositorios;

import com.proyecto.proyectoBSM.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, Integer> {
    
    //se busca un libro segun su autor en BD
    @Query("SELECT a FROM Libro a WHERE a.autor.nombre LIKE %:nombreautor% AND a.alta = true AND a.autor.alta = true")
    public List<Libro> buscarLibroPorAutor(@Param("nombreautor")String nombreAutor);
    
    //se busca un libro segun su titulo en BD
    @Query("SELECT a FROM Libro a WHERE a.titulo LIKE %:titulo% AND a.alta = true")
    public List<Libro> buscarLibroPorTitulo(@Param("titulo")String titulo);
    
    //este metodo nos devuelve la cantidad de ejemplares restantes segun el libro seleccionado
    @Query ("SELECT a.ejemplaresRestantes FROM Libro a WHERE a.id = :idLibro")
    public int restantes(@Param("idLibro")Integer idLibro);

    @Query("SELECT a FROM Libro a WHERE a.isbn = :isbn")
    public Libro buscarLibroPorIsbn(@Param("isbn")Long isbn);
}
