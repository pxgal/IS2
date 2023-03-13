/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.DAO;

import com.sun.org.apache.bcel.internal.generic.AALOAD;
import modelo.HibernateUtil;
import modelo.POJOS.Categorias;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author pablo
 */
public class CategoriasDAO {

    Session session;
    public void actualizarSalario(Categorias categoria) {
       
        //seteamos antes de actualizar la base de datos
        Categorias catActu = categoria;
        catActu.setSalarioBaseCategoria(categoria.getSalarioBaseCategoria() + 200);

        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(catActu);
        tx.commit();

    }
    public void setConector(Session session) {
        this.session = session;
    }
}
