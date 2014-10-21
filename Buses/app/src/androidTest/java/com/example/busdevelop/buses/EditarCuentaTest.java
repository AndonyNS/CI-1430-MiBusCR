package com.example.busdevelop.buses;

import android.test.ActivityTestCase;

/**
 * Created by jossue on 12/10/14.
 */
public class EditarCuentaTest extends ActivityTestCase {

    public void testObtenerDatosUsuario(){
        String token = "b0936d7e239775e770ce002307f0acda";
        String url = "https://murmuring-anchorage-1614.herokuapp.com/users/1";
        Usuario usuarioPrueba = new Usuario();
        usuarioPrueba.parsearDatosUsuario(usuarioPrueba.getUsuario(token,url));

        //al coincidir id y correo sabemos que obtuvimos el usuario correcto
        assertEquals(1,usuarioPrueba.getId());
        assertEquals("Api@MiBusCR.co.cr", usuarioPrueba.getEmail());

    }

    public void testActualizarDatos(){
        String token = "b0936d7e239775e770ce002307f0acda";
        String url = "https://murmuring-anchorage-1614.herokuapp.com/users/1";

        Usuario usuarioViejo = new Usuario();
        Usuario usuarioNuevo = new Usuario();

        usuarioViejo.parsearDatosUsuario(usuarioViejo.getUsuario(token,url));
        usuarioNuevo.parsearDatosUsuario(usuarioNuevo.getUsuario(token,url));

        //actualizar Usuario
        usuarioNuevo.setCiudad("Cartago");
        usuarioNuevo.actualizarUsuario(token, url);
        //Se vuelve a obtener el con el mismo id y token anterior
        usuarioNuevo.parsearDatosUsuario(usuarioNuevo.getUsuario(token,url));
        assertEquals("Cartago", usuarioNuevo.getCiudad());
        //restableser valores
        usuarioNuevo.setCiudad("san jose");
        usuarioNuevo.actualizarUsuario(token, url);
        usuarioNuevo.parsearDatosUsuario(usuarioNuevo.getUsuario(token,url));
        assertEquals("san jose", usuarioNuevo.getCiudad());


    }
}
