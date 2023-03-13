/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.DAO;

import java.util.List;
import modelo.HibernateUtil;
import modelo.POJOS.Empresas;
import modelo.POJOS.Trabajador;
import org.hibernate.*;

/**
 *
 * @author pablo
 */
public class TrabajadorDAO {
    
    Session session;
    public Trabajador encontrarPorCif(String cif) {
        String HQL = "FROM Trabajador WHERE NIFNIE = :cif";
        Query query = session.createQuery(HQL);
        query.setParameter("cif", cif);
        List<Trabajador> result = query.list();
        if(result.size()==1){
            return (Trabajador) result.get(0);
        }
        return (Trabajador) null;
    }

    public void setConector(Session session) {
        this.session = session;
    }
}
