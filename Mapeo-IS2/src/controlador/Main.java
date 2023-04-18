/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import modelo.DAO.CategoriasDAO;
import modelo.DAO.EmpresaDAO;
import modelo.DAO.NominasDAO;
import modelo.DAO.TrabajadorDAO;
import modelo.Excelmanager;
import modelo.HibernateUtil;
import modelo.POJOS.Empresas;
import modelo.POJOS.Trabajador;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author pablo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        /**
         * P1 SessionFactory sf = HibernateUtil.getSessionFactory(); Session
         * session = sf.openSession();
         *
         *
         * System.out.println("Introducir el CIF del trabajador:"); Scanner scan
         * = new Scanner(System.in); String cif = scan.nextLine(); TrabajadorDAO
         * trabajadorDAO = new TrabajadorDAO();
         * trabajadorDAO.setConector(session); Trabajador trabajador =
         * trabajadorDAO.encontrarPorCif(cif); if (trabajador == null) {
         * System.out.println("No hemos encontrado al trabajador en nuestro
         * sistema"); } else { //datos Trabajador System.out.println("Nombre
         * trabajador: " + trabajador.getNombre());
         * System.out.println("Apellidos trabajador: " +
         * trabajador.getApellido1() + " " + trabajador.getApellido2());
         * System.out.println("NIF trabajador: " + trabajador.getNifnie());
         *
         * //Datos Empresa Empresas empresa = trabajador.getEmpresas();
         * System.out.println("Nombre Empresa: " + empresa.getNombre());
         * System.out.println("Nombre Empresa: " + empresa.getCif());
         *
         * //Numero de trabajadores int numTrabajadores =
         * empresa.getTrabajadors().size(); System.out.println("Numero de
         * trabajadores en " + empresa.getNombre() + ": " + numTrabajadores);
         *
         * //Actualizar salario categorias CategoriasDAO categoriaDAO = new
         * CategoriasDAO(); categoriaDAO.setConector(session);
         * categoriaDAO.actualizarSalario(trabajador.getCategorias());
         *
         * //Cambiar nombre EmpresaDAO empresaDAO = new EmpresaDAO();
         * empresaDAO.setConector(session);
         * empresaDAO.actualizarNombreEmpresa(trabajador.getEmpresas());
         *
         * //Eliminar nominas en base al IRPF maximo NominasDAO nominaDAO = new
         * NominasDAO(); nominaDAO.setConector(session);
         * nominaDAO.eliminarNominas_IRPF_MAX(); // HibernateUtil.shutdown(); }
         */
        /**
         * P3
         */
        Excelmanager gestionE = new Excelmanager("/src/resources/SistemasInformacionII.xlsx");
        Sheet traC = gestionE.getSheet(0);

        //Iterador de las filas de la hoja
        Iterator<Row> rowIterator = gestionE.getRowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            // Verifica si la fila tiene al menos una celda con valor
            boolean isRowNotEmpty = false;
            for (Cell cell : row) {
                if (cell.getCellType() != CellType.BLANK) {
                    isRowNotEmpty = true;
                    break;
                }
            }
            // Si la fila no está vacía, procesa la fila
            if (isRowNotEmpty) {
                //1- Verificacion del codigo de cuenta bancaria de cliente y generacion del IBAN asociado
                String ccc = row.getCell(1).getStringCellValue();
                String cccPartes[] = new String[3];
                if (validarCalcuarCCC(ccc, cccPartes)) {//si es correcto

                    StringBuilder sb = new StringBuilder();

                    for (String palabra : cccPartes) {
                        sb.append(palabra);
                    }

                    String newccc = sb.toString();
                    System.out.println(newccc);
                } else {//si es incorrecto

                }
            }
        }
    }

    private static boolean validarCalcuarCCC(String ccc, String[] cccPartes) {
        boolean ok = false;

        if (ccc.length() != 20) {
            return false;
        }

        String parte1 = ccc.substring(0, 8);
        String parte2 = ccc.substring(8, 10);
        String parte3 = ccc.substring(10);

        //primer digito
        String p_0 = "00" + parte1;
        int suma = 0;
        for (int i = 0; i < p_0.length(); i++) {
            int num = Character.getNumericValue(p_0.charAt(i));
            int index = (int) (Math.pow(2, i) % 11);
            suma = suma + (num * index);
        }
        int digito1 = (11 - (suma % 11)) % 11;
        if (digito1 == Character.getNumericValue(parte2.charAt(0))) {
            ok = true;
        }

        //segundo digito
        int suma2 = 0;
        for (int i = 0; i < parte3.length(); i++) {
            int num = Character.getNumericValue(parte3.charAt(i));
            int index = (int) (Math.pow(2, i) % 11);
            suma2 = suma2 + (num * index);
        }
        int digito2 = (11 - (suma2 % 11)) % 11;
        if (digito2 == Character.getNumericValue(parte2.charAt(1))) {
            ok = true;
        } else {
            ok = false;
        }

        cccPartes[0] = parte1;
        cccPartes[1] = "" + digito1 + "" + digito2 + "";
        cccPartes[2] = parte3;
        return ok;
    }

}
