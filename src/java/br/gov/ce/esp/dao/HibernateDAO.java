/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.gov.ce.esp.dao;

import br.gov.ce.esp.util.FacesUtils;
import br.gov.ce.esp.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author weider
 */
public class HibernateDAO implements InterfaceDAO {

    private Session session = null;
    private Transaction tx = null;

    public void inserir(Object obj) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(obj);
            FacesUtils.mostrarMensagem("Salvo com sucesso!!!");
            tx.commit();
            session.close();
        } catch (HibernateException hbe) {
            FacesUtils.mostrarMensagem("Erro ao salvar " + hbe);
            tx.rollback();
            session.close();
        }
    }

    public void atualizar(Object obj) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.update(obj);
            FacesUtils.mostrarMensagem("Atualizado com sucesso!!!");
            tx.commit();
            session.close();
            obj = new Object();
        } catch (HibernateException hbe) {
            FacesUtils.mostrarMensagem("Erro ao atualizar " + hbe);
            tx.rollback();
            session.close();
        }
    }

    public void deletar(Object obj) {
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.delete(obj);
            FacesUtils.mostrarMensagem("Deletado com sucesso!!!");
            tx.commit();
            session.close();
        } catch (HibernateException hbe) {
            FacesUtils.mostrarMensagem("Erro ao deletar " + hbe);
            tx.rollback();
            session.close();
        }
    }
}
