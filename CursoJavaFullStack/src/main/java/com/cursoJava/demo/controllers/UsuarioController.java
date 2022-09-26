package com.cursoJava.demo.controllers;

import com.cursoJava.demo.dao.UsuarioDao;
import com.cursoJava.demo.models.Usuario;
import com.cursoJava.demo.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;

@RestController
public class UsuarioController {
    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(value = "api/usuarios/{id}", method = RequestMethod.GET)
    public Usuario getUsuario(@PathVariable Long id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre("Clark");
        usuario.setApellido("kent");
        usuario.setEmail("clarkkent@planeta.com");
        usuario.setTelefono("1234567890");

        return usuario;
    }

    @RequestMapping(value = "api/usuarios", method = RequestMethod.GET)
    public List<Usuario>  getUsuarios(@RequestHeader(value="Authorization") String token) {

        if (!validarToken(token)) {return null;}

        return usuarioDao.getUsuarios();
    }

    @RequestMapping(value = "api/usuarios/{id}", method = RequestMethod.DELETE)
    public void deleteUsuario(@RequestHeader(value="Authorization") String token ,@PathVariable Long id) {

        if (!validarToken(token)) {return;}
        usuarioDao.eliminar(id);
    }


    @RequestMapping(value = "api/usuarios", method = RequestMethod.POST)
    public void  registrarUsuarios(@RequestBody Usuario usuario) {
        //usamos argon2 para encriptar la password
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(1, 1024, 1, usuario.getPassword());
        //seteamos la password ya encriptada y lo guardamos en la bd
        usuario.setPassword(hash);
        usuarioDao.registrar(usuario);
    }

    public boolean validarToken(String token) {
        String usuarioId =  jwtUtil.getKey(token);
        return usuarioId != null;
    }
}