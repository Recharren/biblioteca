package com.proyecto.proyectoBSM.servicios;

import com.proyecto.proyectoBSM.entidades.Autor;
import com.proyecto.proyectoBSM.entidades.Editorial;
import com.proyecto.proyectoBSM.entidades.Libro;
import com.proyecto.proyectoBSM.excepciones.MiExcepcion;
import com.proyecto.proyectoBSM.repositorios.AutorRepositorio;
import com.proyecto.proyectoBSM.repositorios.EditorialRepositorio;
import com.proyecto.proyectoBSM.repositorios.LibroRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LibroServicio {
    
        @Autowired
    private LibroRepositorio libroRepositorio;
        
        @Autowired
        private AutorRepositorio autorRepositorio;
        
        @Autowired
        private EditorialRepositorio editorialRepositorio;
    
    //se registra un nuevo libro y se validan sus datos    
    @Transactional
    public void registrarLibro (Long isbn,String titulo,Integer anio,Integer ejemplares,Integer ejemplaresPrestados,String autor,String editorial) throws MiExcepcion{
        
        validar(isbn,titulo,anio,ejemplares,ejemplaresPrestados);
        
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setAnio(anio);
        libro.setEjemplares(ejemplares);
        libro.setEjemplaresPrestados(ejemplaresPrestados);
        libro.setEjemplaresRestantes(ejemplares - ejemplaresPrestados);
        libro.setAlta(true);
        libro.setAutor(buscarAutorPorNombre(autor));
        libro.setEditorial(buscarEditorialPorNombre(editorial));
        libroRepositorio.save(libro);
    }
    
    //se busca un autor por nombre (nos sirve para validar al momento de crear libros)
    public Autor buscarAutorPorNombre(String nombreAutor) throws MiExcepcion {
        Autor autor = autorRepositorio.buscarAutorPorNombre(nombreAutor);
        if (autor!=null) {
            return autor;
        }
        throw new MiExcepcion("ERROR: El Autor no se encuentra en la Base de Datos.");
    }
    
    //se busca una editorial por su nombre (nos sirve para validar al momento de crear libros)
    public Editorial buscarEditorialPorNombre(String nombreEditorial) throws MiExcepcion{
        Editorial editorial = editorialRepositorio.buscarEditorialPorNombre(nombreEditorial);
        if (editorial != null) {
            return editorial;
        }
        throw new MiExcepcion("ERROR: La editorial no se encuentra en la Base de Datos.");
    }
     
    //se validan los datos ingresados
    public void validar(Long isbn,String titulo,Integer anio,Integer ejemplares, Integer ejemplaresPrestados) throws MiExcepcion{
        if (isbn <=0) {
            throw new MiExcepcion("ERROR: El numero isbn del libro es incorrecto.");
        }
        if (titulo.isEmpty() || titulo.equals(null)) {
            throw new MiExcepcion("ERROR: El titulo del libro esta vacio o nulo.");
        }
        if (anio <=0 || anio.equals(null)) {
            throw new MiExcepcion("ERROR: El año del libro es incorrecto.");
        }
        if (ejemplares<0) {
            throw new MiExcepcion("ERROR: La cantidad de ejemplares debe ser positivo o cero.");
        }   
        if (ejemplaresPrestados>ejemplares || ejemplaresPrestados<0) {
            throw new MiExcepcion("ERROR: Los ejemplares prestados son incorrectos.");
        }
    }
    
    //devuelve un libro segun su id
    public Libro buscarLibroPorId(Integer id) {
        return libroRepositorio.getById(id);
    }
    
    //se da de alta un libro
    @Transactional
    public void altaLibro(Integer id) throws MiExcepcion {
        Optional<Libro> auxiliar = libroRepositorio.findById(id);
        if (auxiliar.isPresent()) {
            Libro libro = auxiliar.get();
            libro.setAlta(true);
            libroRepositorio.save(libro);
        } else {
            throw new MiExcepcion("ERROR: No se encontro Libro para dar de alta.");
        }
    }
    
    //se da de baja un libro
    @Transactional
    public void bajaLibro(Integer id) throws MiExcepcion {
        Optional<Libro> auxiliar = libroRepositorio.findById(id);
        if (auxiliar.isPresent()) {
            Libro libro = auxiliar.get();
            libro.setAlta(false);
            libroRepositorio.save(libro);
        } else {
            throw new MiExcepcion("ERROR: No se encontro Libro para dar de alta.");
        }
    }
    
    //se listan todos los libros
    public List<Libro> listarLibros() {
        return libroRepositorio.findAll();
    }
    
    //se modifican los datos de un libro
    @Transactional
    public void modificarLibro(Integer id, Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, String autor, String editorial) throws MiExcepcion {

        Optional<Libro> auxiliar = libroRepositorio.findById(id);
        if (auxiliar.isPresent()) {

            Libro libro = auxiliar.get();
            libro.setIsbn(isbn);
            libro.setTitulo(titulo);
            libro.setAnio(anio);
            libro.setEjemplares(ejemplares);
            libro.setEjemplaresPrestados(ejemplaresPrestados);
            libro.setEjemplaresRestantes(ejemplares - ejemplaresPrestados);
            libro.setAutor(buscarAutorPorNombre(autor));
            libro.setEditorial(buscarEditorialPorNombre(editorial));
            libroRepositorio.save(libro);
        } else {
            throw new MiExcepcion("ERROR: No se encontro el Libro para modificar.");
        }
    }
    
    //devuelve una lista de libros segun el titulo ingresado
    public List<Libro> buscarLibroPorTitulo(String titulo) throws MiExcepcion {
        List<Libro> libros = null;
        try {
            libros = libroRepositorio.buscarLibroPorTitulo(titulo);
            
        } catch (Exception e) {
            throw new MiExcepcion("ERROR: Al buscar libros por titulo.");
        }
        if (!libros.isEmpty()) {
            return libros;
        } else {
            throw new MiExcepcion("No se encontró ningun libro con el titulo: "+titulo);
            
        }
    }
    
    //devuelve una lista de libros segun nombre del autor
    public List<Libro> buscarLibroPorNombreAutor(String palabra) throws MiExcepcion {
        List<Libro> libros = null;
        try {
            libros = libroRepositorio.buscarLibroPorAutor(palabra);
            
        } catch (Exception e) {
            throw new MiExcepcion("ERROR: Al buscar libros por nombre de Autor.");
        }
        if (!libros.isEmpty()) {
            return libros;
        } else {
            throw new MiExcepcion("No se encontró ningun libro con el nombre de Autor: "+palabra);
            
        }
        
    }

    // Carga una lista de libros como ejemplo, si los libros ya estan cargados el sistema lanza un aviso.
    public void cargarLibrosEjemplos() throws MiExcepcion {
        if (libroRepositorio.buscarLibroPorIsbn(9789878727899L)==null){
            libroRepositorio.save(new Libro(9789878727899L,"Pasión y obsesión",2022,50,0,50,true,autorRepositorio.save(new Autor("Vacca , Julián Nahuel",true)),editorialRepositorio.save(new Editorial("Autores de Argentina",true))));
            libroRepositorio.save(new Libro(9789502332543L,"El informe de Brodie",2010,50,0,50,true,autorRepositorio.save(new Autor("Borges, Jorge Luis",true)),editorialRepositorio.save(new Editorial("Eudeba",true))));
            libroRepositorio.save(new Libro(9789500766746L,"Los conjurados",2004,50,0,50,true,autorRepositorio.save(new Autor("Borges, Jorge Luis",true)),editorialRepositorio.save(new Editorial("Sudamericana",true))));
            libroRepositorio.save(new Libro(9789874429780L,"Los sueños de Helena",2022,50,0,50,true,autorRepositorio.save(new Autor("Galeano, Eduardo",true)),editorialRepositorio.save(new Editorial("Libros del Zorro Rojo",true))));
            libroRepositorio.save(new Libro(9789500751681L,"Relato de un náufrago",2015,50,0,50,true,autorRepositorio.save(new Autor("García Márquez, Gabriel",true)),editorialRepositorio.save(new Editorial("Sudamericana",true))));
            libroRepositorio.save(new Libro(9789500751698L,"El amor en los tiempos del cólera",2015,50,0,50,true,autorRepositorio.save(new Autor("García Márquez, Gabriel",true)),editorialRepositorio.save(new Editorial("Sudamericana",true))));
            libroRepositorio.save(new Libro(9789877381962L,"Todos los fuegos el fuego",2016,50,0,50,true,autorRepositorio.save(new Autor("Cortázar, Julio",true)),editorialRepositorio.save(new Editorial("Alfaguara",true))));
            libroRepositorio.save(new Libro(9789877381986L,"Final del juego",2016,50,0,50,true,autorRepositorio.save(new Autor("Cortázar, Julio",true)),editorialRepositorio.save(new Editorial("Alfaguara",true))));
            libroRepositorio.save(new Libro(9789876283427L,"Harry Potter y la cámara secreta",2014,50,0,50,true,autorRepositorio.save(new Autor("Rowling, Joanne K.",true)),editorialRepositorio.save(new Editorial("Edhasa",true))));
            libroRepositorio.save(new Libro(9789500419574L,"Harry Potter y la piedra filosofal",2000,50,0,50,true,autorRepositorio.save(new Autor("Rowling, Joanne K.",true)),editorialRepositorio.save(new Editorial("Edhasa",true))));

        } else {
            throw new MiExcepcion("Los libros de ejemplo ya están cargados");
        }
    }
}
