/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.sql.Connection;
import java.util.Scanner;
import modelo.DAO.CategoriasDAO;
import modelo.DAO.EmpresaDAO;
import modelo.DAO.NominasDAO;
import modelo.DAO.TrabajadorDAO;
import modelo.HibernateUtil;
import modelo.POJOS.Empresas;
import modelo.POJOS.Trabajador;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author pablo
 */
public class MapeoIS2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();
        
        
        System.out.println("Introducir el CIF del trabajador:");
        Scanner scan = new Scanner(System.in);
        String cif = scan.nextLine();
        TrabajadorDAO trabajadorDAO = new TrabajadorDAO();
        trabajadorDAO.setConector(session);
        Trabajador trabajador = trabajadorDAO.encontrarPorCif(cif);
        if (trabajador == null) {
            System.out.println("No hemos encontrado al trabajador en nuestro sistema");
        } else {
            //datos Trabajador
            System.out.println("Nombre trabajador: " + trabajador.getNombre());
            System.out.println("Apellidos trabajador: " + trabajador.getApellido1() + " " + trabajador.getApellido2());
            System.out.println("NIF trabajador: " + trabajador.getNifnie());

            //Datos Empresa
            Empresas empresa = trabajador.getEmpresas();
            System.out.println("Nombre Empresa: " + empresa.getNombre());
            System.out.println("Nombre Empresa: " + empresa.getCif());

            //Numero de trabajadores
            int numTrabajadores = empresa.getTrabajadors().size();
            System.out.println("Numero de trabajadores en " + empresa.getNombre() + ": " + numTrabajadores);

            //Actualizar salario categorias
            CategoriasDAO categoriaDAO = new CategoriasDAO();
            categoriaDAO.setConector(session);
            categoriaDAO.actualizarSalario(trabajador.getCategorias());
            
            //Cambiar nombre
            EmpresaDAO empresaDAO = new EmpresaDAO();
            empresaDAO.setConector(session);
            empresaDAO.actualizarNombreEmpresa(trabajador.getEmpresas());
            
            //Eliminar nominas en base al IRPF maximo
            NominasDAO nominaDAO = new NominasDAO();
            nominaDAO.setConector(session);
            nominaDAO.eliminarNominas_IRPF_MAX();
            //
            HibernateUtil.shutdown();
        }
    }

}
