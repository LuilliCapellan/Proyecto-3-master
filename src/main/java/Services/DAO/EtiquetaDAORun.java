package Services.DAO;

import DB.Etiqueta;
import Services.DAO.Interfaces.EtiquetaDAO;
import Services.Entitys.ArticuloService;
import Services.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EtiquetaDAORun implements EtiquetaDAO {

    @Override
    public void insert(Etiqueta e){
        Connection con = null;

        try	{

            con = Service.getInstancia().getConnection();
            String sql = "Insert into PUBLIC.ETIQUETA(id, ETIQUETA, ARTICULO) values(nextval('SECUENCIA_ETIQUETA') , ?, ?); ";
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            preparedStatement.setString(1, e.getEtiqueta());
            preparedStatement.setLong(2, e.getArticulo().getId());

            preparedStatement.execute();


        } catch (Exception e1) {
            Logger.getLogger(EtiquetaDAORun.class.getName()).log(Level.SEVERE, null, e1);
        }finally{

            try {
                assert con != null;
                con.close();
            } catch (SQLException e1) {
                Logger.getLogger(EtiquetaDAORun.class.getName()).log(Level.SEVERE, null, e1);
            }
        }
    }

    @Override
    public void update(Etiqueta e) {

        Connection con = null;
        try {

            con = Service.getInstancia().getConnection();
            String sql = "UPDATE PUBLIC.ETIQUETA u SET id = ?, ETIQUETA= ? WHERE u.id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);

            preparedStatement.setLong(1, e.getId());
            preparedStatement.setString(2, e.getEtiqueta());
            preparedStatement.setLong(3, e.getId());

            preparedStatement.executeUpdate();

        }catch (Exception e1){
            Logger.getLogger(EtiquetaDAORun.class.getName()).log(Level.SEVERE, null, e1);
        }finally {
            try {
                assert con != null;
                con.close();
            } catch (SQLException e1) {
                Logger.getLogger(EtiquetaDAORun.class.getName()).log(Level.SEVERE, null, e1);
            }
        }

    }

    @Override
    public void delete(Etiqueta e) {
        Connection con = null;

        try {
            con = Service.getInstancia().getConnection();
            String sql = "delete from PUBLIC.ETIQUETA where id=?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, e.getId());

            preparedStatement.execute();

        }catch (Exception e1){
            Logger.getLogger(EtiquetaDAORun.class.getName()).log(Level.SEVERE, null, e1);

        }finally {
            try {
                assert con != null;
                con.close();
            } catch (SQLException e1) {
                Logger.getLogger(EtiquetaDAORun.class.getName()).log(Level.SEVERE, null, e1);

            }
        }
    }

    @Override
    public List<Etiqueta> getAll() {
        List<Etiqueta> list = new ArrayList<>();
        Connection con = null;

        try {
            String sql = "select * from PUBLIC.ETIQUETA";
            con = Service.getInstancia().getConnection();

            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Etiqueta etiqueta = new Etiqueta();
                ArticuloService articuloService = new ArticuloService();
                etiqueta.setId(resultSet.getLong("id"));
                etiqueta.setEtiqueta(resultSet.getString("etiqueta"));
                etiqueta.setArticulo(articuloService.getById(resultSet.getLong("articulo")));
                list.add(etiqueta);
            }
        } catch (Exception e) {
            Logger.getLogger(EtiquetaDAORun.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            try {
                assert con != null;
                con.close();
            } catch (SQLException e) {
                Logger.getLogger(EtiquetaDAORun.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        return list;
    }

    @Override
    public Etiqueta getById(long id) {

        Connection con = null;
        Etiqueta etiqueta = null;

        try {
            con = Service.getInstancia().getConnection();
            String sql = "SELECT * FROM PUBLIC.ETIQUETA u WHERE u.ID = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){

                etiqueta = new Etiqueta();
                etiqueta.setId(resultSet.getLong("id"));
                etiqueta.setEtiqueta(resultSet.getString("etiqueta"));
            }

        }catch (Exception e){
            Logger.getLogger(EtiquetaDAORun.class.getName()).log(Level.SEVERE, null, e);

        }finally {
            try {
                assert con != null;
                con.close();
            } catch (SQLException e) {
                Logger.getLogger(EtiquetaDAORun.class.getName()).log(Level.SEVERE, null, e);

            }
        }
        return etiqueta;
    }


    @Override
    public List<Etiqueta> getByArticulo(long id) {
        Connection con = null;
        List<Etiqueta> list = new ArrayList<>();

        try {
            con = Service.getInstancia().getConnection();
            String sql = "SELECT * FROM PUBLIC.ETIQUETA u WHERE u.ARTICULO = ?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){

                Etiqueta etiqueta = new Etiqueta();
                etiqueta.setId(resultSet.getLong("id"));
                etiqueta.setEtiqueta(resultSet.getString("etiqueta"));
                list.add(etiqueta);
            }

        }catch (Exception e){
            Logger.getLogger(EtiquetaDAORun.class.getName()).log(Level.SEVERE, null, e);

        }finally {
            try {
                assert con != null;
                con.close();
            } catch (SQLException e) {
                Logger.getLogger(EtiquetaDAORun.class.getName()).log(Level.SEVERE, null, e);

            }
        }
        return list;
    }
}
