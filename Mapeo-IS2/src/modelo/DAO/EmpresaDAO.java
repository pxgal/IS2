/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.DAO;

import modelo.POJOS.Empresas;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author pablo
 */
public class EmpresaDAO {

    Session session;

    public void setConector(Session session) {
        this.session = session;
    }

    public void actualizarNombreEmpresa(Empresas empresas) {
        Transaction tx = session.beginTransaction();
        String hql = "UPDATE Empresas e SET e.nombre = CONCAT(e.nombre, '2023') WHERE e.idEmpresa != :idEmpTrabajador";
        Query query = session.createQuery(hql);
        query.setParameter("idEmpTrabajador", empresas.getIdEmpresa());
        int updatedCount = query.executeUpdate();
        tx.commit();

    }
}
