/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import modelo.POJOS.Trabajador;
import modelo.POJOS.Empresas;
import modelo.POJOS.Categorias;

/**
 *
 * @author pablo
 */
public class Excelmanager {

    Workbook workbook;
    Sheet sheet;
    Iterator<Row> rowIterator;
     String rutaRelativa;
    public Excelmanager(String file) {
        try {
            this.rutaRelativa = System.getProperty("user.dir") + file;
            FileInputStream fileIn = new FileInputStream(rutaRelativa);
            this.workbook = new XSSFWorkbook(fileIn);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Excelmanager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Excelmanager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Sheet getSheet(int i) {
        this.sheet = workbook.getSheetAt(i);
        return sheet;
    }

    public Iterator<Row> getRowIterator() {
        return sheet.rowIterator();
    }

    public void modAndShutDown() throws FileNotFoundException, IOException {
        workbook.write(new FileOutputStream(rutaRelativa));
        workbook.close();
    }
}
