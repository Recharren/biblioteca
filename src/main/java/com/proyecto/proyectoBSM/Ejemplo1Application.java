package com.proyecto.proyectoBSM;

import com.proyecto.proyectoBSM.servicios.ClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Ejemplo1Application {

    @Autowired
    private ClienteServicio clienteServicio;

    public static void main(String[] args) {
        SpringApplication.run(Ejemplo1Application.class, args);
    }

    //Este metodo encripta las contraseñas
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(clienteServicio)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}
