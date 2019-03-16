package Services.DAO.Interfaces;

import DB.Likes;
import DB.TipoLike;
import DB.Usuario;

public interface LikesDAO {

    void guardar(Likes likes);

    int LikesByArticuloId(long id);
    int DislikesByArticuloId(long id);

    boolean existsUsuario(long id, TipoLike tipoLike);

    void borrar(Likes likes);

    Likes buscarByUsuario(Usuario usuario);
}
