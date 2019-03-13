package Services;

import DB.Usuario;
import Services.Entitys.UsuarioService;
import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ServiceInit {
    public static void iniciaDb() throws SQLException {
        Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
    }

    public static void stopDb() throws SQLException {
        Server.shutdownTcpServer("tcp://localhost:9092", "", true, true);
    }

    public static void createTable() throws SQLException {
        String createUsuario = "CREATE TABLE IF NOT EXISTS USUARIO \n" +
                "(\n" +
                "ID INTEGER PRIMARY KEY NOT NULL, \n" +
                "USERNAME VARCHAR(32) NOT NULL, \n" +
                "NOMBRE VARCHAR(64) NOT NULL, \n" +
                "PASS VARCHAR(32) NOT NULL, \n" +
                "ADMINISTRADOR BOOLEAN NOT NULL, \n" +
                "AUTOR BOOLEAN NOT NULL \n" +
                ");";

        String createEtiqueta = "CREATE TABLE IF NOT EXISTS ETIQUETA \n" +
                "(\n" +
                "ID INTEGER PRIMARY KEY NOT NULL, \n" +
                "ETIQUETA VARCHAR(32) NOT NULL, \n" +
                "ARTICULO INTEGER NOT NULL , \n" +
                "FOREIGN KEY (ARTICULO) REFERENCES ARTICULO(ID)" +
                "ON DELETE CASCADE );";


        String createComentario = "CREATE TABLE IF NOT EXISTS COMENTARIO \n" +
                "(\n" +
                "ID INTEGER PRIMARY KEY NOT NULL, \n" +
                "COMENTARIO VARCHAR(2048) NOT NULL, \n" +
                "AUTOR INTEGER NOT NULL , \n" +
                "ARTICULO INTEGER NOT NULL , \n" +
                "FOREIGN KEY (AUTOR) REFERENCES USUARIO(ID), \n" +
                "FOREIGN KEY (ARTICULO) REFERENCES ARTICULO(ID)" +
                "ON DELETE CASCADE );";

        String createArticulo = "CREATE TABLE IF NOT EXISTS ARTICULO \n" +
                "(\n" +
                "ID INTEGER PRIMARY KEY NOT NULL, \n" +
                "TITULO VARCHAR(128) NOT NULL, \n" +
                "CUERPO VARCHAR(8192) NOT NULL, \n" +
                "AUTOR INTEGER NOT NULL, \n " +
                "FECHA DATE NOT NULL , \n" +
                "FOREIGN KEY (AUTOR) REFERENCES USUARIO(ID)" +
                ");";


        String admin = "INSERT INTO USUARIO VALUES (SECUENCIA_USUARIO.nextval,'ADMIN','ADMIN','ADMIN',TRUE,TRUE)";
        //String admin = "SELECT * FROM USUARIO";
        String secuenciaUsuario = "CREATE SEQUENCE IF NOT EXISTS SECUENCIA_USUARIO START WITH 0 INCREMENT BY 1";
        String secuenciaArticulo = "CREATE SEQUENCE IF NOT EXISTS SECUENCIA_ARTICULO START WITH 0 INCREMENT BY 1";
        String secuenciaEtiqueta = "CREATE SEQUENCE IF NOT EXISTS SECUENCIA_ETIQUETA START WITH 0 INCREMENT BY 1";
        String secuenciaComentario = "CREATE SEQUENCE IF NOT EXISTS SECUENCIA_COMENTARIO START WITH 0 INCREMENT BY 1";


        Connection _conn = Service.getInstancia().getConnection();
        Statement stm = _conn.createStatement();
        //PreparedStatement prepareStatement = _conn.prepareStatement(admin);
        //Antes de ejecutar seteo los parametros.
        stm.execute(createUsuario);
        stm.execute(createArticulo);
        stm.execute(createComentario);
        stm.execute(createEtiqueta);

        stm.execute(secuenciaUsuario);
        stm.execute(secuenciaArticulo);
        stm.execute(secuenciaEtiqueta);
        stm.execute(secuenciaComentario);

        UsuarioService usuarioService = new UsuarioService();
        Usuario usuario = usuarioService.validateLogIn("admin", "admin");

        if (usuario == null) {
            stm.execute(admin);
        }

        stm.close();
        _conn.close();
    }
}
