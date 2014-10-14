package com.example.busdevelop.buses;

import android.test.ActivityTestCase;

/**
 * Created by jossue on 12/10/14.
 */
public class EditarCuentaTest extends ActivityTestCase {

    public void testObtenerDatosUsuario(){
        String token = "327960192102c55a20b757e14dcc10dd";
        String url = "https://murmuring-anchorage-1614.herokuapp.com/users/4";
        Usuario usuarioPrueba = new Usuario();
        usuarioPrueba.parsearDatosUsuario(usuarioPrueba.getUsuario(token,url));

        //al coincidir id y correo sabemos que obtuvimos el usuario correcto
        assertEquals(4,usuarioPrueba.getId());
        assertEquals("antonio.brenes@gmail.com", usuarioPrueba.getEmail());

    }

    public void testActualizarDatos(){
        String token = "327960192102c55a20b757e14dcc10dd";
        String url = "https://murmuring-anchorage-1614.herokuapp.com/users/4";

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

    }
}
