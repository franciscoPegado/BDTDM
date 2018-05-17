/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.esp.controle;

import br.gov.ce.esp.dao.HibernateDAO;
import br.gov.ce.esp.dao.InterfaceDAO;
import br.gov.ce.esp.pojos.Usuarios;
import br.gov.ce.esp.util.FacesUtils;
import br.gov.ce.esp.util.HibernateUtil;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author weider
 */
@ManagedBean(name = "usuarioControle")
@SessionScoped
public class UsuarioControle implements Serializable {

    private Usuarios usuarios = null;
    private Session session = null;
    private InterfaceDAO usuarioDAO = null;
    private Boolean autenticado = false;

    public UsuarioControle() {
        usuarios = new Usuarios();
        usuarioDAO = new HibernateDAO();
    }

    public Usuarios getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(Usuarios usuarios) {
        this.usuarios = usuarios;
    }

    public Boolean getAutenticado() {
        return autenticado;
    }

    public String Login() {
        //Cria a sessao com o banco
        //session = HibernateUtil.getSessionFactory().openSession();
        //cria a query HQL
        //Query q = session.createQuery("from Usuarios u where u.cpf = :cpf and u.senha = :senha");
        //add o parametro a consulta
        //q.setParameter("cpf", usuarios.getCpf());
        //q.setParameter("senha", usuarios.getSenha());
        //add a consulta a lista
        //List<Usuarios> listaUsuarios = q.list();
        //if (listaUsuarios != null && listaUsuarios.size() > 0) {
            //usuarios = listaUsuarios.get(0);
            //if (usuarios.getCpf() != null && usuarios.getSenha().equals(usuarios.getSenha())) {
                autenticado = true;
                return "/admin/principal.xhtml";
            //}
        //}
        //FacesUtils.mostrarMensagem("Erro de Autenticação!!!");
        //return null;
    }

    public String confirmar() {
        if (usuarios.getCodigo() == 0) {
            this.usuarioDAO.inserir(usuarios);
            usuarios = new Usuarios();
        } else {
            this.usuarioDAO.atualizar(usuarios);
            usuarios = new Usuarios();
        }
        return null;
    }

    public void cancelarLogin(ActionEvent actionEvent) {
        usuarios = new Usuarios();
    }
}
