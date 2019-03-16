package Services.Entitys;

import DB.Etiqueta;
import Services.DAO.EtiquetaDAORun;
import Services.DAO.Interfaces.EtiquetaDAO;

import java.util.List;
public class EtiquetaService implements EtiquetaDAO {
    private EtiquetaDAORun etiquetaDAO;
    private static EtiquetaService instancia;

    public static EtiquetaService getInstancia(){
        if (instancia == null)
            instancia = new EtiquetaService();

        return instancia;
    }

    public EtiquetaService() {
        etiquetaDAO = new EtiquetaDAORun(Etiqueta.class);
    }

    @Override
    public void insert(Etiqueta e) {
        etiquetaDAO.insert(e);
    }

    @Override
    public void update(Etiqueta e) {
        etiquetaDAO.update(e);
    }

    @Override
    public void delete(Etiqueta e) {
        etiquetaDAO.delete(e);
    }

    @Override
    public List<Etiqueta> getAll() {
        return etiquetaDAO.getAll();
    }

    @Override
    public Etiqueta getById(long id) {
        return etiquetaDAO.getById(id);
    }

    @Override
    public List<Etiqueta> getByArticulo(long id) {
        return etiquetaDAO.getByArticulo(id);
    }
}
