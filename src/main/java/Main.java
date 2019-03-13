import DB.Articulo;
import DB.Comentario;
import DB.Etiqueta;
import DB.Usuario;
import Services.Crypto;
import Services.Entitys.ArticuloService;
import Services.Entitys.ComentarioService;
import Services.Entitys.EtiquetaService;
import Services.Entitys.UsuarioService;
import Services.Filtros;
import Services.ServiceInit;
import freemarker.template.Configuration;
import freemarker.template.Version;
import spark.ModelAndView;
import spark.Session;
import spark.template.freemarker.FreeMarkerEngine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class Main {
    public static Usuario usuario;
    static final String iv = "0123456789123456"; // This has to be 16 characters
    static final String secretKeyUSer = "qwerty987654321";
    static final String secretKeyContra = "12345678923";

    public static void main(String[] args) throws SQLException {

        ServiceInit.iniciaDb();

        ServiceInit.createTable();

        UsuarioService usuarioService = new UsuarioService();
        ArticuloService articuloService = new ArticuloService();
        EtiquetaService etiquetaService = new EtiquetaService();
        ComentarioService comentarioService = new ComentarioService();

        staticFiles.location("/Template");

        Configuration configuration = new Configuration(new Version(2, 3, 0));
        configuration.setClassForTemplateLoading(Main.class, "/Template");

        FreeMarkerEngine freeMarkerEngine = new FreeMarkerEngine(configuration);

        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            Map<String, String> cookies = request.cookies();

            String[] llaveValor = new String[2];
            request.cookie("login");
            for (String key : cookies.keySet()) {
                System.out.println("llave: " + key + " valor: " + cookies.get(key));
                llaveValor = cookies.get(key).split(",");

            }

            if (request.cookie("login") != null) {

            }

            if (llaveValor.length > 1) {
                Crypto crypto = new Crypto();

                System.out.println(llaveValor[0] + " contra: " + llaveValor[1]);
                String user = crypto.decrypt(llaveValor[0], iv, secretKeyUSer);
                String contra = crypto.decrypt(llaveValor[1], iv, secretKeyContra);

                Usuario usuario1 = usuarioService.validateLogIn(user, contra);
                if (usuario1 != null) {
                    usuario = usuario1;
                    request.session(true);
                    request.session().attribute("usuario", usuario);

                    response.redirect("/inicio");
//                    return modelAndView(attributes, "home.ftl");
                }
            }
            return new ModelAndView(attributes, "login.ftl");
        }, freeMarkerEngine);

        get("/inicio", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("titulo", "Inicio");
            attributes.put("etiquetas", etiquetaService.getAll());
            attributes.put("list", articuloService.getAll());
            userLevel(attributes);
            return new ModelAndView(attributes, "home.ftl");
        }, freeMarkerEngine);


        get("/verMas/:id", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            String idArticulo = request.params("id");
            userLevel(attributes);
            Articulo articulo2 = articuloService.getById(Integer.parseInt(idArticulo));
            attributes.put("articulo", articulo2);
            attributes.put("comentarios", comentarioService.getByArticulo(Integer.parseInt(idArticulo)));
            attributes.put("etiquetas", etiquetaService.getByArticulo(Integer.parseInt(idArticulo)));
            return new ModelAndView(attributes, "post.ftl");
        }, freeMarkerEngine);


        post("/agregarComentario", (request, response) -> {

            if (usuario == null)
                response.redirect("/errorPost/401");

            String comentario = request.queryParams("comentario");
            String articulo = request.queryParams("articulo");


            Articulo articulo1 = articuloService.getById(Integer.parseInt(articulo));

            comentarioService.insert(new Comentario(comentario, usuario, articulo1));


            response.redirect("/verMas/" + articulo);
            return "";
        });

        get("/agregarPost", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            userLevel(attributes);
            return new ModelAndView(attributes, "agregarPost.ftl");

        }, freeMarkerEngine);
        get("/editarPost/:id", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            userLevel(attributes);
            String idArticulo = request.params("id");
            long articleid = Integer.parseInt(idArticulo);

            List<Articulo> a = new ArrayList<>();

            if (usuario != null) {

                a = articuloService.getbyAutor(usuario.getId());
            } else {
                response.redirect("/errorPost/401");
            }

            if (articuloService.buscarPost(a, articleid) || usuario.getAdministrator()) {

                Articulo editar = articuloService.getById(Integer.parseInt(idArticulo));
                attributes.put("articulo", editar);
                List<Etiqueta> tags = etiquetaService.getByArticulo(Integer.parseInt(idArticulo));
                StringBuilder res = new StringBuilder();
                for (int i = 0; i < tags.size(); i++) {
                    if (i == tags.size() - 1) {
                        res.append(tags.get(i).getEtiqueta());
                    } else {
                        res.append(tags.get(i).getEtiqueta()).append(",");
                    }
                }
                String ResultingTagString = String.valueOf(res);
                attributes.put("etiquetas", ResultingTagString);
            } else {
                response.redirect("/errorPost/401");
            }


            return new ModelAndView(attributes, "editarPost.ftl");
        }, freeMarkerEngine);

        post("/guardarPost", (request, response) -> {
            Usuario autor = usuario;
            String titulo = request.queryParams("titulo");
            String cuerpo = request.queryParams("cuerpo");
            long now = System.currentTimeMillis();
            java.sql.Date nowsql = new java.sql.Date(now);

            String etiquetas = request.queryParams("etiquetas");

            String[] tagsarray = etiquetas.split(",");
            Long articleid = articuloService.getNextID();
            Articulo art = new Articulo(titulo, cuerpo, autor, nowsql);
            articuloService.insert(art);


            for (String s : tagsarray) {
                Etiqueta e = new Etiqueta(s, articuloService.getById(articleid));
                etiquetaService.insert(e);
            }


            response.redirect("/inicio");
            return "";
        });

        post("/iniciarSesion", (request, response) -> {

            String user = request.queryParams("usuario");
            String contra = request.queryParams("password");
            String recordar = request.queryParams("remember");

            System.out.println(recordar);


            System.out.println(user + " pass : " + contra);
            Usuario usuario1 = usuarioService.validateLogIn(user, contra);

            if (usuario1 != null) {

                if (recordar != null && recordar.equalsIgnoreCase("on")) {
                    request.session(true);
                    request.session().attribute("usuario", usuario1);

                    Crypto crypto = new Crypto();
                    String userEncrypt = crypto.encrypt(user, iv, secretKeyUSer);
                    String contraEncrypt = crypto.encrypt(contra, iv, secretKeyContra);

//                final String decryptedData = crypto.decrypt(encryptedData, iv, secretKey);

                    System.out.println("user encryp: " + userEncrypt + " contra encryp: " + contraEncrypt);

                    response.cookie("/", "login", userEncrypt + "," + contraEncrypt, 604800, false); //incluyendo el path del cookie.
                }
                usuario = usuario1;
                request.session(true);
                request.session().attribute("usuario", usuario);
                response.redirect("/inicio");
            } else {
                response.redirect("/");
            }
            return "";
        });

        post("/editarPost/:id", (request, response) -> {


            String idArticulo = request.params("id");
            Long articleid = Long.parseLong(idArticulo);


            Usuario autor = usuario;
            String titulo = request.queryParams("titulo");
            String cuerpo = request.queryParams("cuerpo");
            long now = System.currentTimeMillis();
            java.sql.Date nowsql = new java.sql.Date(now);
            Articulo art = new Articulo(articleid, titulo, cuerpo, autor, nowsql);
            articuloService.update(art);
            String tags = request.queryParams("etiquetas");

            String[] tagArray = tags.split(",");

            List<Etiqueta> l = etiquetaService.getByArticulo(articleid);

            for (String aTagArray : tagArray) {
                boolean exists = false;
                for (Etiqueta e : l) {
                    if (aTagArray.equalsIgnoreCase(e.getEtiqueta())) {
                        exists = true;
                    }
                }
                if (!exists) {
                    etiquetaService.insert(new Etiqueta(aTagArray, art));
                }
            }
            response.redirect("/verMas/" + idArticulo);


            return "";
        });

        get("/eliminarPost/:id/:articulo", (request, response) -> {

            String id = request.params("id");
            String articulo = request.params("articulo");

            long idAutor = Integer.parseInt(id);
            long idArticulo = Integer.parseInt(articulo);

            if (usuario != null && ((idAutor == usuario.getId()) || usuario.getAdministrator())) {
                articuloService.delete(articuloService.getById(idArticulo));
                response.redirect("/inicio");
            } else {
                response.redirect("/errorPost/401");
            }

            return "";
        });

        get("/agregarUsuario", (request, response) -> {

            Map<String, Object> attributes = new HashMap<>();
            userLevel(attributes);
            return new ModelAndView(attributes, "agregarUsuario.ftl");
        }, freeMarkerEngine);

        post("/guardarUsuario", (request, response) -> {
            String username = request.queryParams("usuario");
            String nombre = request.queryParams("nombre");
            String pass = request.queryParams("pass");
            String autor = request.queryParams("autor");
            String admin = request.queryParams("admin");
            Usuario u = new Usuario(username, nombre, pass, admin != null, autor != null);
            usuarioService.insert(u);
            response.redirect("/inicio");
            return "";
        });


        get("/logOut", (request, response) -> {

            usuario = null;
            Session session = request.session(true);
            session.invalidate();
            response.removeCookie("/", "login");
            response.redirect("/");

            return "";
        });

        get("/errorPost/:error", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            String primero, segundo, tercero;
            String error = request.params("error");

            primero = error.substring(0, 1);
            segundo = error.substring(1, 2);
            tercero = error.substring(2, 3);

            System.out.println("numero: " + primero + " " + segundo + " " + tercero);
            attributes.put("primero", primero);
            attributes.put("segundo", segundo);
            attributes.put("tercero", tercero);
            attributes.put("mensaje", "Usted no tiene permisos para esta area!");

            return new ModelAndView(attributes, "error.ftl");
        }, freeMarkerEngine);

        get("/verUsuarios", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();

            attributes.put("usuarios", usuarioService.getAll());

            return new ModelAndView(attributes, "verUsuarios.ftl");
        }, freeMarkerEngine);


        get("/ver/:id", (request, response) -> {
            String id = request.params("id");

            Usuario usuario = usuarioService.getById(Integer.parseInt(id));


            Map<String, Object> attributes = new HashMap<>();
            attributes.put("titulo", "Posts de " + usuario.getNombre());
            attributes.put("usuario", usuario);
            attributes.put("etiquetas", etiquetaService.getAll());
            attributes.put("list", articuloService.getbyAutor(usuario.getId()));
            userLevel(attributes);
            return new ModelAndView(attributes, "home.ftl");
        }, freeMarkerEngine);


        new Filtros().filtros();
    }

    private static void userLevel(Map<String, Object> attributes) {
        attributes.put("usuario", usuario);

        if (usuario != null) {
            attributes.put("admin", usuario.getAdministrator());
            attributes.put("autor", usuario.getAutor());
            attributes.put("usuarioName", usuario.getNombre());
        }
    }
}
