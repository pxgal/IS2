/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.*;


/**
 * Clase que permite conectar con la base de datos
 * @author chenao
 *
 */
public class Conexion {
   static String bd = "nominas";
   static String login = "root";
   static String password = "2098";
   static String url = "jdbc:mysql://localhost/"+bd;

   Connection conn = null;

   /** Constructor de DbConnection */
   public Conexion() {
      try{
         //obtenemos el driver de para mysql
         Class.forName("com.mysql.jdbc.Driver");
         //obtenemos la conexi�n
         conn = DriverManager.getConnection(url,login,password);

         if (conn!=null){
            System.out.println("Conexion a base de datos "+bd+" OK");
         }
      }
      catch(SQLException e){
         System.out.println(e);
      }catch(ClassNotFoundException e){
         System.out.println(e);
      }catch(Exception e){
         System.out.println(e);
      }
   }
   /**Permite retornar la conexi�n*/
   public Connection getConnection(){
      return conn;
   }

   public void desconectar(){
      conn = null;
   }
}
