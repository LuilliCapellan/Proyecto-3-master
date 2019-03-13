package Services.DAO.Interfaces;

import DB.Comentario;

import java.util.List;

public interface ComentarioDAO {

    void insert(Comentario e);

    void update(Comentario e);

    void delete(Comentario e);

    List<Comentario> getAll();

    Comentario getById(long id);

    List<Comentario> getByArticulo(long id);
}
