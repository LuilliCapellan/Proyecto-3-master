package Services.DAO;

import DB.Usuario;
import Services.DAO.Interfaces.UsuarioDAO;
import Services.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioDAORun implements UsuarioDAO {


    @Override
    public void insert(Usuario e) {
        Connection con = null;

        try {

            con = Service.getInstancia().getConnection();
            String sql = "Insert into PUBLIC.USUARIO (id, username, nombre, pass, administrador, autor) values(NEXTVAL('SECUENCIA_USUARIO'), ?, ?, ?, ?, ?); ";
            PreparedStatement preparedStatement = con.prepareStatement(sql);

//        preparedStatement.setLong(1, e.getId());
            preparedStatement.setString(1, e.getUsername());
            preparedStatement.setString(2, e.getNombre());
            preparedStatement.setString(3, e.getPassword());
            preparedStatement.setBoolean(4, e.getAdministrator());
            preparedStatement.setBoolean(5, e.getAutor());


            preparedStatement.execute();


        } catch (Exception e1) {
            Logger.getLogger(UsuarioDAORun.class.getName()).log(Level.SEVERE, null, e1);
        } finally {

            try {
                assert con != null;
                con.close();
            } catch (SQLException e1) {
                Logger.getLogger(UsuarioDAORun.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    @Override
    public void update(Usuario e) {

        Connection con = null;
        try {

            con = Service.getInstancia().getConnection();
            String sql = "UPDATE PUBLIC.USUARIO u SET id = ?, USERNAME = ?, NOMBRE= ?,  PASS = ?, ADMINISTRADOR = ?, AUTOR = ? WHERE u.id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            preparedStatement.setLong(1, e.getId());
            preparedStatement.setString(2, e.getUsername());
            preparedStatement.setString(3, e.getNombre());
            preparedStatement.setString(4, e.getPassword());
            preparedStatement.setBoolean(5, e.getAdministrator());
            preparedStatement.setBoolean(6, e.getAutor());
            preparedStatement.setLong(7, e.getId());

            preparedStatement.executeUpdate();

        } catch (Exception e1) {
            Logger.getLogger(UsuarioDAORun.class.getName()).log(Level.SEVERE, null, e1);
        } finally {
            try {
                assert con != null;
                con.close();
            } catch (SQLException e1) {
                Logger.getLogger(UsuarioDAORun.class.getName()).log(Level.SEVERE, null, e1);
            }
        }

    }

    @Override
    public void delete(Usuario e) {
        Connection con = null;

        try {
            con = Service.getInstancia().getConnection();
            String sql = "delete from PUBLIC.USUARIO where id=?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, e.getId());

            preparedStatement.execute();

        } catch (Exception e1) {
            Logger.getLogger(UsuarioDAORun.class.getName()).log(Level.SEVERE, null, e1);

        } finally {
            try {
                assert con != null;
                con.close();
            } catch (SQLException e1) {
                Logger.getLogger(UsuarioDAORun.class.getName()).log(Level.SEVERE, null, e1);

            }
        }
    }

    @Override
    public List<Usuario> getAll() {
        List<Usuario> list = new ArrayList<>();
        Connection con = null;

        try {
            String sql = "select * from PUBLIC.USUARIO";
            con = Service.getInstancia().getConnection();

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                Usuario usuario = new Usuario();
                setRes(resultSet, usuario);

                list.add(usuario);
            }
        } catch (Exception e) {
            Logger.getLogger(UsuarioDAORun.class.getName()).log(Level.SEVERE, null, e);

        } finally {
            try {
                assert con != null;
                con.close();
            } catch (SQLException e) {
                Logger.getLogger(UsuarioDAORun.class.getName()).log(Level.SEVERE, null, e);

            }
        }


        return list;
    }

    private void setRes(ResultSet resultSet, Usuario usuario) throws SQLException {
        usuario.setId(resultSet.getLong("id"));
        usuario.setNombre(resultSet.getString("nombre"));
        usuario.setUsername(resultSet.getString("username"));
        usuario.setPassword(resultSet.getString("pass"));
        usuario.setAdministrator(resultSet.getBoolean("administrador"));
        usuario.setAutor(resultSet.getBoolean("autor"));
    }

    @Override
    public Usuario getById(long id) {

        Connection con = null;
        Usuario usuario = null;

        try {
            con = Service.getInstancia().getConnection();
            String sql = "SELECT * FROM PUBLIC.USUARIO u WHERE u.ID = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                usuario = new Usuario();
                setRes(resultSet, usuario);
            }

        } catch (Exception e) {
            Logger.getLogger(UsuarioDAORun.class.getName()).log(Level.SEVERE, null, e);

        } finally {
            try {
                assert con != null;
                con.close();
            } catch (SQLException e) {
                Logger.getLogger(UsuarioDAORun.class.getName()).log(Level.SEVERE, null, e);

            }
        }
        return usuario;
    }

    @Override
    public Usuario validateLogIn(String user, String pass) {


        Usuario usuario = null;

        Connection con = null;
        try {

            String query = "SELECT * FROM USUARIO u WHERE u.USERNAME = ? AND u.PASS = ?";
            con = Service.getInstancia().getConnection();
            //
            PreparedStatement prepareStatement = con.prepareStatement(query);
            //Antes de ejecutar seteo los parametros.
            prepareStatement.setString(1, user);
            prepareStatement.setString(2, pass);

            ResultSet resultSet = prepareStatement.executeQuery();
//            String admin = "INSERT INTO USUARIO VALUES (ID = ?,USERNAME = ?,NOMBRE = ? , PASS = ?,ADMIN = ?, AUTOR = ?)";
//            PreparedStatement prepareStatement = con.prepareStatement(admin);
//            //Antes de ejecutar seteo los parametros.
//            prepareStatement.setString(1, "1");
//            prepareStatement.setString(2, "admin");
//            prepareStatement.setString(3, "admin");
//            prepareStatement.setString(4, "admin");
//            prepareStatement.setString(5, "true");
//            prepareStatement.setString(6, "true");

            while (resultSet.next()) {

                usuario = new Usuario();
                usuario.setId(resultSet.getLong("id"));
                usuario.setNombre(resultSet.getString("nombre"));
                usuario.setUsername(resultSet.getString("username"));
                usuario.setPassword(resultSet.getString("pass"));
                usuario.setAdministrator(resultSet.getBoolean("administrador"));
                usuario.setAutor(resultSet.getBoolean("autor"));
            }


        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAORun.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                assert con != null;
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuarioDAORun.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return usuario;
    }


}
