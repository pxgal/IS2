/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.DAO;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author pablo
 */
public class NominasDAO {

    Session session;

    public void setConector(Session session) {
        this.session = session;
    }

    public void eliminarNominas_IRPF_MAX() {
        Transaction tx = session.beginTransaction();
        Query queryMaxIRPF = session.createQuery("SELECT MAX(n.irpf) FROM Nomina n");
        Double maxIRPF = (Double) queryMaxIRPF.uniqueResult();
        Query delete = session.createQuery("DELETE FROM Nomina n WHERE n.irpf = :maxIRPF");
        delete.setParameter("maxIRPF", maxIRPF);
        int deletedCount = delete.executeUpdate();
        System.out.println("Se han eliminado " + deletedCount + " nominas con el IRPF igual a " + maxIRPF);
        tx.commit();
    }
}
