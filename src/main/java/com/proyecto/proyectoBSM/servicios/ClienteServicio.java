package com.proyecto.proyectoBSM.servicios;

import com.proyecto.proyectoBSM.entidades.Cliente;
import com.proyecto.proyectoBSM.enums.Rol;
import com.proyecto.proyectoBSM.excepciones.MiExcepcion;
import com.proyecto.proyectoBSM.repositorios.ClienteRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class ClienteServicio implements UserDetailsService {

    @Autowired
    public ClienteRepositorio clienteRepositorio;

    //se crea un cliente y se validan los datos ingresados
    @Transactional
    public void crearCliente(Long documento, String nombre, String apellido, String telefono, String email, String clave, String clave2) throws MiExcepcion {

        
        validar(documento, nombre, apellido, telefono, email, clave, clave2);

        Cliente cliente = new Cliente();
        cliente.setDocumento(documento);
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setTelefono(telefono);
        cliente.setEmail(email);
        cliente.setAlta(Boolean.TRUE);
        
        if (apellido.toLowerCase().equals("admin")){
        cliente.setRol(Rol.ADMIN);
        }
        else {
        cliente.setRol(Rol.USUARIO);
        }
        //se encripta la clave
        String encriptada = new BCryptPasswordEncoder().encode(clave);
        cliente.setClave(encriptada);
        clienteRepositorio.save(cliente);

    }

    //se da de alta a un cliente
    @Transactional
    public void altaCliente(Integer id) throws MiExcepcion {
        Optional<Cliente> auxiliar = clienteRepositorio.findById(id);
        
        if (auxiliar.isPresent()) {
            Cliente cliente = auxiliar.get();
            cliente.setAlta(true);

            clienteRepositorio.save(cliente);
        } else {
            throw new MiExcepcion("ERROR: No se encontro Libro para dar de alta.");
        }
    }

    //se da de baja a un cliente
    @Transactional
    public void bajaCliente(Integer id) throws MiExcepcion {
        Optional<Cliente> auxiliar = clienteRepositorio.findById(id);
        if (auxiliar.isPresent()) {
            Cliente cliente = auxiliar.get();
            cliente.setAlta(false);

            clienteRepositorio.save(cliente);
        } else {
            throw new MiExcepcion("ERROR: No se encontro Libro para dar de baja.");
        }
    }

    //se modifica un cliente
    @Transactional
    public void modificarCliente(Integer id, Long documento, String nombre, String apellido, String telefono) throws MiExcepcion {

        Optional<Cliente> auxiliar = clienteRepositorio.findById(id);
        if (auxiliar.isPresent()) {
            Cliente cliente = auxiliar.get();
            cliente.setDocumento(documento);
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setTelefono(telefono);

            clienteRepositorio.save(cliente);
        } else {
            throw new MiExcepcion("ERROR: Hubo un problema al modificar el cliente " + nombre + " " + apellido);
        }
    }

    //se listan todos los clientes
    public List<Cliente> listarClientes() {
        return clienteRepositorio.findAll();
    }

    //se validan los datos ingresados al momento de crear un cliente
    public void validar(Long documento, String nombre, String apellido, String telefono, String email, String clave, String clave2) throws MiExcepcion {

        if (documento == null) {
            throw new MiExcepcion("ERROR: el documento no puede ser nulo");
        }
        if (nombre.equals(null)) {
            throw new MiExcepcion("ERROR: el nombre no puede ser nulo.");
        }
        if (apellido.equals(null)) {
            throw new MiExcepcion("ERROR: el apellido no puede ser nulo.");
        }
        if (telefono.equals(null)) {
            throw new MiExcepcion("ERROR: el telefono no puede ser nulo.");
        }

        if (email == null || email.isEmpty()) {
            throw new MiExcepcion("El mail no puede ser nulo");
        }
        if (clave == null || clave.isEmpty() || clave.length() <= 7) {
            throw new MiExcepcion("La clave del usuario no puede ser nula y tiene que tener mas de siete digitos");
        }

        if (!clave.equals(clave2)) {
            throw new MiExcepcion("Las claves deben ser iguales");
        }
    }

    //devuelve un cliente segun su id
    public Cliente buscarClientePorId(Integer id) {
        return clienteRepositorio.getById(id);
    }

    //metodo encargado validar el correo y realizar el login
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Cliente cliente = null;
        try {
            cliente = clienteRepositorio.buscarClientePorEmail(email);
        } catch (Exception e) {
            throw new UsernameNotFoundException("No se encontro el usuario.");
        }

        if (cliente != null) {

            List<GrantedAuthority> permisos = new ArrayList<>();

            // Creo una lista de permisos
            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_" + cliente.getRol());
            permisos.add(p1);

            //Esto me permite guardar el OBJETO USUARIO(Cliente) LOG, para luego ser utilizado
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", cliente);

            User user = new User(cliente.getEmail(), cliente.getClave(), permisos);
            return user;
        } else {
            return null;
        }
    }

}
