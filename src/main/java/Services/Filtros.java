package Services;

import DB.Usuario;

import static spark.Spark.before;
import static spark.Spark.halt;

public class Filtros {
    public void filtros() {


        before((request, response) -> System.out.println("Ruta antes: " + request.pathInfo()));

        before("/agregarPost", (request, response) -> {

            Usuario usuario = request.session(true).attribute("usuario");

            if (usuario == null || (!usuario.getAutor() && !usuario.getAdministrator())) {
                response.redirect("/inicio");
            }

        });

        before("/agregarUsuario", (request, response) -> {

            Usuario usuario = request.session(true).attribute("usuario");

            if (usuario == null || !usuario.getAdministrator()) {
                response.redirect("/inicio");
            }

        });

        before("/agregarComentario", (request, response) -> {

            Usuario usuario = request.session(true).attribute("usuario");

            if (usuario == null) {
                response.redirect("/inicio");
            }

        });

    }

}
