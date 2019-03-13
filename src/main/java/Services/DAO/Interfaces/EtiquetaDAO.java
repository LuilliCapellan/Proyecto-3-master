package Services.DAO.Interfaces;

import DB.Etiqueta;

import java.util.List;

public interface EtiquetaDAO {

    void insert(Etiqueta e);

    void update(Etiqueta e);

    void delete(Etiqueta e);

    List<Etiqueta> getAll();

    Etiqueta getById(long id);

    List<Etiqueta> getByArticulo(long id);
}

