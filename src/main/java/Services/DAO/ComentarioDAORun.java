package Services.DAO;

import DB.Comentario;
import DB.Usuario;
import Services.DAO.Interfaces.ComentarioDAO;
import Services.Entitys.UsuarioService;
import Services.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ComentarioDAORun implements ComentarioDAO {

    @Override
    public void insert(Comentario e) {
        Connection con = null;

        try	{

            con = Service.getInstancia().getConnection();
            String sql = "Insert into PUBLIC.COMENTARIO(id, COMENTARIO, AUTOR, ARTICULO) values(nextval('SECUENCIA_COMENTARIO'), ?, ?, ?); ";
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            preparedStatement.setString(1, e.getComentario());
            preparedStatement.setLong(2, e.getAutor().getId());
            preparedStatement.setLong(3, e.getArticulo().getId());
            preparedStatement.execute();


        } catch (Exception e1) {
            Logger.getLogger(ComentarioDAORun.class.getName()).log(Level.SEVERE, null, e1);
        }finally{

            try {
                assert con != null;
                con.close();
            } catch (SQLException e1) {
                Logger.getLogger(ComentarioDAORun.class.getName()).log(Level.SEVERE, null, e1);
            }
        }
    }

    @Override
    public void update(Comentario e) {
        Connection con = null;
        try {

            con = Service.getInstancia().getConnection();
            String sql = "UPDATE PUBLIC.COMENTARIO u SET COMENTARIO = ?, AUTOR = ?, ARTICULO = ? WHERE u.id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            preparedStatement.setString(1, e.getComentario());
            preparedStatement.setLong(2, e.getAutor().getId());
            preparedStatement.setLong(3, e.getArticulo().getId());
            preparedStatement.setLong(4, e.getId());

            preparedStatement.executeUpdate();

        }catch (Exception e1){
            Logger.getLogger(ComentarioDAORun.class.getName()).log(Level.SEVERE, null, e1);
        }finally {
            try {
                assert con != null;
                con.close();
            } catch (SQLException e1) {
                Logger.getLogger(ComentarioDAORun.class.getName()).log(Level.SEVERE, null, e1);
            }
        }

    }

    @Override
    public void delete(Comentario e) {
        Connection con = null;

        try {
            con = Service.getInstancia().getConnection();
            String sql = "delete from PUBLIC.COMENTARIO where id=?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, e.getId());

            preparedStatement.execute();

        }catch (Exception e1){
            Logger.getLogger(ComentarioDAORun.class.getName()).log(Level.SEVERE, null, e1);

        }finally {
            try {
                assert con != null;
                con.close();
            } catch (SQLException e1) {
                Logger.getLogger(ComentarioDAORun.class.getName()).log(Level.SEVERE, null, e1);

            }
        }

    }

    @Override
    public List<Comentario> getAll() {
        List<Comentario> list = new ArrayList<>();
        Connection con = null;

        try {
            String sql = "select * from PUBLIC.COMENTARIO";
            con = Service.getInstancia().getConnection();

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                Comentario comentario = new Comentario();
                comentario.setId(resultSet.getLong("id"));
                comentario.setComentario(resultSet.getString("comentario"));

                list.add(comentario);
            }
        } catch (Exception e) {
            Logger.getLogger(ComentarioDAORun.class.getName()).log(Level.SEVERE, null, e);

        }finally{
            try {
                assert con != null;
                con.close();
            } catch (SQLException e) {
                Logger.getLogger(ComentarioDAORun.class.getName()).log(Level.SEVERE, null, e);
            }
        }


        return list;
    }

    @Override
    public Comentario getById(long id) {
        Connection con = null;
        Comentario comentario = null;

        try {
            con = Service.getInstancia().getConnection();
            String sql = "SELECT * FROM PUBLIC.COMENTARIO u WHERE u.ID = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){

                comentario = new Comentario();
                comentario.setId(resultSet.getLong("id"));
                comentario.setComentario(resultSet.getString("comentario"));
            }

        }catch (Exception e){
            Logger.getLogger(ComentarioDAORun.class.getName()).log(Level.SEVERE, null, e);

        }finally {
            try {
                assert con != null;
                con.close();
            } catch (SQLException e) {
                Logger.getLogger(ComentarioDAORun.class.getName()).log(Level.SEVERE, null, e);

            }
        }
        return comentario;
    }

    @Override
    public List<Comentario> getByArticulo(long id) {
        Connection con = null;
        List<Comentario> list = new ArrayList<>();

        try {
            con = Service.getInstancia().getConnection();
            String sql = "SELECT * FROM PUBLIC.COMENTARIO  WHERE ARTICULO = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){

                Comentario comentario = new Comentario();
                comentario.setId(resultSet.getLong("ID"));
                comentario.setComentario(resultSet.getString("comentario"));
                UsuarioService usuarioService = new UsuarioService();
                Usuario usuario = usuarioService.getById(resultSet.getLong("autor"));
                comentario.setAutor(usuario);
                list.add(comentario);
            }

        }catch (Exception e){
            Logger.getLogger(ComentarioDAORun.class.getName()).log(Level.SEVERE, null, e);

        }finally {
            try {
                assert con != null;
                con.close();
            } catch (SQLException e) {
                Logger.getLogger(ComentarioDAORun.class.getName()).log(Level.SEVERE, null, e);

            }
        }
        return list;
    }
}
