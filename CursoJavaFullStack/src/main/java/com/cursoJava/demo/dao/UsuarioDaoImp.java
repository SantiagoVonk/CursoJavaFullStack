package com.cursoJava.demo.dao;

import com.cursoJava.demo.models.Usuario;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
// repository: tiene la funcionalidad de acceder al repositorio de la dase de datos
@Repository
//transacional: hace referencia a como va a armar y ejecutar las consultas sql ( fragmentos de transaccion )
@Transactional
public class UsuarioDaoImp implements UsuarioDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Usuario> getUsuarios() {
        //creo la consulta a la base de datos ( que hace referencia a la clase Usuario no a la tabla usuario )
        String query = "FROM Usuario";
        //guardo en una lista y asi retorno la lista
        List<Usuario> resultado = entityManager.createQuery(query).getResultList();
        return resultado;
    }

    @Override
    public void eliminar(Long id) {
        Usuario usuario = entityManager.find(Usuario.class, id);
        entityManager.remove(usuario);
    }

    @Override
    public void registrar(Usuario usuario) {
        entityManager.merge(usuario);
    }

    @Override
    public Usuario obtenerUsuarioPorCredenciales(Usuario usuario) {
        String query = "FROM Usuario WHERE email = :email";
        List<Usuario> lista = entityManager.createQuery(query)
                                    .setParameter("email", usuario.getEmail())
                                    .getResultList();

        //me aseguro que la lista no vuelva vacia, para evitar un null pointer exception
        if (lista.isEmpty()) {
            return null;
        }
        //recupero la password hash de la bd y guardo en variable
        String passwordHashed = lista.get(0).getPassword();

        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        //verifico que la hash pertenezca a la password enviado por usuario y return en caso true
        if (argon2.verify(passwordHashed, usuario.getPassword())) {
            return lista.get(0);
        }
            return null;
    }
}
