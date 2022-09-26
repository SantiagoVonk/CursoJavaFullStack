package com.cursoJava.demo.controllers;

import com.cursoJava.demo.dao.UsuarioDao;
import com.cursoJava.demo.models.Usuario;
import com.cursoJava.demo.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class AuthController {
    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(value = "api/login", method = RequestMethod.POST)
    public String  login(@RequestBody Usuario usuario) {

       Usuario usuarioLogueado = usuarioDao.obtenerUsuarioPorCredenciales(usuario);
       if (usuarioLogueado != null) {

            String tokenJWT =  jwtUtil.create(String.valueOf(usuarioLogueado.getId()), usuarioLogueado.getEmail());
            //aqui podemos agregar mas info para devolver para no hacer tantas peticiones al servidor
            return tokenJWT;
        }else {
            return "FALLO";
        }
    }
}

