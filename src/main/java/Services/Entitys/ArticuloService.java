package Services.Entitys;

import DB.Articulo;
import Services.DAO.ArticuloDAORun;
import Services.DAO.Interfaces.ArticuloDAO;

import java.util.List;

public class ArticuloService implements ArticuloDAO {
    private ArticuloDAORun articuloDAO;

    public ArticuloService(){
        articuloDAO = new ArticuloDAORun();
    }

    @Override
    public void insert(Articulo e) {
        articuloDAO.insert(e);
    }

    @Override
    public void update(Articulo e) {
        articuloDAO.update(e);
    }

    @Override
    public long getNextID() {
        return articuloDAO.getNextID();
    }

    @Override
    public void delete(Articulo e) {
        articuloDAO.delete(e);
    }

    @Override
    public List<Articulo> getAll() {
        return articuloDAO.getAll();
    }

    @Override
    public Articulo getById(long id) {
        return articuloDAO.getById(id);
    }

    @Override
    public List<Articulo> getbyAutor(long id) {
        return articuloDAO.getbyAutor(id);
    }


    public boolean buscarPost(List<Articulo> articulos, long id){

        for (Articulo articulo:articulos) {
            if (articulo.getId() == id)
                return true;
        }

        return false;
    }
}
