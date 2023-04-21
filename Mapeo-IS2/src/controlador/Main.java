/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.io.IOException;
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
import java.math.BigInteger;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.*;
import java.text.DecimalFormat;

/**
 *
 * @author pablo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //P3
        Excelmanager gestionE = new Excelmanager("/src/resources/SistemasInformacionII.xlsx");

        //Iterador de las filas de la hoja
        Iterator<Row> rowIterator = gestionE.getRowIterator();
        rowIterator.next();
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
                String newCCC = "";
                String IBAN = "";
                String cccPartes[] = new String[3];
                if (validarCalcuarCCC(ccc, cccPartes)) {//si el CCC es correcto
                    newCCC = unirPartes(cccPartes);

                    //1.2-crear IBAN
                    String pais = row.getCell(0).getStringCellValue();
                    IBAN = crearIBAN(newCCC, pais);

                } else {//si el CCC es incorrecto
                    newCCC = unirPartes(cccPartes);

                    //1.2-Crear IBAN
                    String pais = row.getCell(0).getStringCellValue();
                    IBAN = crearIBAN(newCCC, pais);

                    //1.2.1- añadirlo al XML
                }
                //1.1- imprimir en la cell
                writeCell(row, 1, newCCC);//el CCC
                writeCell(row, 2, IBAN);//el IBAN
                
                /* Obtener los datos para generar los correos */
                String nombreUsuario = row.getCell(10).getStringCellValue();
                String apellido1Usuario = row.getCell(8).getStringCellValue();
                String apellido2Usuario = row.getCell(9).getStringCellValue();
                String empresaUsuario = row.getCell(6).getStringCellValue();
                String emailUsuario = generarEmail(nombreUsuario, apellido1Usuario, apellido2Usuario, empresaUsuario);
                
                /* Imprimir en la celda de email */
                writeCell(row, 3, emailUsuario);
            }
        }
        gestionE.modAndShutDown();
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

    private static String unirPartes(String[] cccPartes) {

        StringBuilder sb = new StringBuilder();

        for (String palabra : cccPartes) {
            sb.append(palabra);
        }

        return sb.toString();
    }

    private static String crearIBAN(String ccc, String pais) {

        //sacar codificacion numerica de las letras del pais || A = 65 => 12 = A - 55
        int dPais1 = pais.charAt(0) - 55;
        int dPais2 = pais.charAt(1) - 55;

        String paisNum = "" + dPais1 + "" + dPais2 + "00";
        String codigo = ccc + paisNum;

        //hacer los calculos
        BigInteger num = new BigInteger(codigo);
        BigInteger modulo = num.mod(new BigInteger("97"));
        BigInteger resultado = new BigInteger("98").subtract(modulo);
        String codS = resultado.toString();
        if (codS.length() < 2) {
            codS = "0" + codS;
        }
        return "" + pais + codS + ccc + "";
    }
    
    private static String generarEmail(String nombre, String apellido1, String apellido2, String empresa){
        ArrayList<String> usuariosCorreo = new ArrayList<>();
        char letraNombre, letraApellido1, letraApellido2;
        letraNombre = nombre.charAt(0);
        letraApellido1 = apellido1.charAt(0);
        if(apellido2 != null){
            letraApellido2 = apellido2.charAt(0);
        }else{
            letraApellido2 = '\0';
        }
        String dominio = empresa + ".com";
        DecimalFormat formatoContador = new DecimalFormat("00");
        String user = Character.toString(letraNombre) + Character.toString(letraApellido1)+ Character.toString(letraApellido2);
        usuariosCorreo.add(user);
        int repes = 0;
        for(int i = 0 ; i < usuariosCorreo.size(); i++){
            if(usuariosCorreo.get(i).equals(user)){
                repes++;
            }
        }
        String email = user + formatoContador.format(repes) + dominio;
        return email;
    }

    private static void writeCell(Row row, int i, String in) {
        Cell celda = row.createCell(i);
        celda.setCellValue(in);

    }

}
