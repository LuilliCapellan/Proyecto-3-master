package Services.DAO;

import DB.Comentario;
import Services.CRUD;
import Services.DAO.Interfaces.ComentarioDAO;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class ComentarioDAORun extends CRUD<Comentario> implements ComentarioDAO {

    public ComentarioDAORun(Class<Comentario> comentarioClass) {
        super(comentarioClass);
    }

    @Override
    public void insert(Comentario e) {
        crear(e);

    }

    @Override
    public void update(Comentario e) {
        editar(e);


    }

    @Override
    public void delete(Comentario e) {
        eliminar(e);
    }

    @Override
    public List<Comentario> getAll() {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Comentario.findAllComentario");
        return (List<Comentario>) query.getSingleResult();
    }

    @Override
    public Comentario getById(long id) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Comentario.findComentarioById");
        query.setParameter("id", id);
        return (Comentario) query.getSingleResult();
    }

    @Override
    public List<Comentario> getByArticulo(long id) {
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery("Comentario.findAllComentarioByArticulo");
        query.setParameter("id", id);
        return (List<Comentario>) query.getResultList();
    }
}