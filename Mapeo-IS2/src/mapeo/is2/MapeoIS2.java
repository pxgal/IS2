/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapeo.is2;

import java.sql.Connection;
import java.util.Scanner;
import modelo.Conexion;
import modelo.DAO.TrabajadorDAO;
import modelo.POJOS.Trabajador;

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
        Connection con = new Conexion().getConnection();
        System.out.println("Introducir el CIF del trabajador:");
        Scanner scan = new Scanner(System.in);
        String cif = scan.nextLine();
        TrabajadorDAO trabajadorDAO = new TrabajadorDAO();
        Trabajador trabajador = trabajadorDAO.encontrarPorCif(cif);
        if (trabajador == null) {
            System.out.println("No existe");
        } else {
             System.out.println("Existe");
        }
    }

}
