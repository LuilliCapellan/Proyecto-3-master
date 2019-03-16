package Services.Entitys;

import DB.Usuario;
import Services.DAO.Interfaces.UsuarioDAO;
import Services.DAO.UsuarioDAORun;

import java.util.List;

public class UsuarioService implements UsuarioDAO {
    private static UsuarioService instancia;

    public static UsuarioService getInstancia() {
        if (instancia == null)
            instancia = new UsuarioService();

        return instancia;
    }

    private UsuarioDAORun usuarioDAO;

    public UsuarioService() {
        usuarioDAO = new UsuarioDAORun(Usuario.class);
    }

    @Override
    public void insert(Usuario e) {
        usuarioDAO.insert(e);
    }

    @Override
    public void update(Usuario e) {
        usuarioDAO.update(e);
    }

    @Override
    public void delete(Usuario e) {
        usuarioDAO.delete(e);
    }

    @Override
    public List<Usuario> getAll() {
        return usuarioDAO.getAll();
    }

    @Override
    public Usuario getById(long id) {
        return usuarioDAO.getById(id);
    }

    @Override
    public Usuario validateLogIn(String user, String pass) {
        return usuarioDAO.validateLogIn(user, pass);
    }


}
