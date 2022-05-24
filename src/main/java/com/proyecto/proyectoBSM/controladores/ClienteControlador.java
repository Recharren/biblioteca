package com.proyecto.proyectoBSM.controladores;

import com.proyecto.proyectoBSM.entidades.Cliente;
import com.proyecto.proyectoBSM.excepciones.MiExcepcion;
import com.proyecto.proyectoBSM.servicios.ClienteServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClienteControlador {

    @Autowired
    public ClienteServicio clienteServicio;

    //se registra un nuevo cliente (Solo visible por el admin)
    @PostMapping("/registrarCliente")
    public String registrarCliente(ModelMap modelo, @RequestParam Long documento, @RequestParam String nombre, @RequestParam String apellido,
            @RequestParam String telefono, @RequestParam String email, @RequestParam String clave, @RequestParam String clave2) {
        try {
            clienteServicio.crearCliente(documento, nombre, apellido, telefono, email, clave, clave2);
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("documento", documento);
            modelo.put("nombre", nombre);
            modelo.put("apellido", apellido);
            modelo.put("telefono", telefono);
            modelo.put("email", email);
            modelo.put("clave", clave);
            modelo.put("clave2", clave2);
            modelo.put("error", ex.getMessage());
            return "cliente.html";
        }
        modelo.put("mensaje", "El Cliente se registro correctamente");
        return "index.html";
    }

    //se listan todos los clientes (Solo visible por el admin)
    @GetMapping("/listarClientes")
    public String listarClientes(ModelMap modelo) throws Exception {
        try {
            List<Cliente> clientes = clienteServicio.listarClientes();
            modelo.put("clientes", clientes);
        } catch (Exception ex) {
            throw new MiExcepcion("ERROR: al buscar Clientes.");
        }
        return "listadoClientes.html";
    }

    //se da de baja un cliente (Solo visible por el admin)
    @GetMapping("/bajaCliente/{id}")
    public String bajaCliente(ModelMap modelo, @PathVariable Integer id) throws MiExcepcion {
        try {
            clienteServicio.bajaCliente(id);
            return "redirect:/listarClientes";
        } catch (Exception e) {
            throw new MiExcepcion("ERROR: Algo paso al intentar dar de alta Cliente.");
        }
    }

    //se da de alta un cliente (Solo visible por el admin)
    @GetMapping("/altaCliente/{id}")
    public String altaCliente(ModelMap modelo, @PathVariable Integer id) throws MiExcepcion {
        try {
            clienteServicio.altaCliente(id);
            return "redirect:/listarClientes";
        } catch (Exception e) {
            throw new MiExcepcion("ERROR: Algo paso al intentar dar de alta Cliente.");
        }
    }

    //se selecciona un cliente de la lista para modificarlo
    @GetMapping("/listarCliente/{id}")
    public String listarCliente(ModelMap modelo, @PathVariable Integer id) {
        Cliente cliente = clienteServicio.buscarClientePorId(id);
        modelo.put("cliente", cliente);
        return "formularioCliente.html";
    }

    //se modifican los datos de un cliente (Solo visible por el admin)
    @PostMapping("/modificarCliente")
    public String modificarCliente(ModelMap modelo, @RequestParam Integer id, @RequestParam Long documento, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String telefono) throws MiExcepcion {
        try {
            clienteServicio.modificarCliente(id, documento, nombre, apellido, telefono);

        } catch (Exception e) {
        }
        modelo.put("mensaje", "El cliente " + nombre + " se actualizo correctamente.");
        return "index.html";

    }
}
