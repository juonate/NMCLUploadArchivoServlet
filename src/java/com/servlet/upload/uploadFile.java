/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servlet.upload;

/**
 *
 * @author Juan
 */
import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class uploadFile extends HttpServlet {

    private static final long serialVersionUID = 1L;
    File file = null;
    String fechaText;
    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat formatoFechaNombre = new SimpleDateFormat("yyyyMMddHHmmss");
    String uploadDir = "C:\\destino\\";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/xml");
        PrintWriter out = response.getWriter();
        Date fecha = new Date();
        String fechaNombre = formatoFechaNombre.format(fecha);

        boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
        if (!isMultipartContent) {
            imprimir("Error: no es un archivo válido.");
            return;
        }
        imprimir("Subiendo archivo");

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> fields = upload.parseRequest(request);
            Iterator<FileItem> it = fields.iterator();
            if (!it.hasNext()) {
                imprimir("No existen archivos.");
                return;
            }
            while (it.hasNext()) {
//                out.println(it.toString());
                FileItem fileItem = it.next();
                String rutaArchivo = guardarArchivo();
                file = new File(rutaArchivo + "\\" + fechaNombre + fileItem.getName());

                fileItem.write(file);
                if (file.canRead()) {
                    imprimir("Se ha subido correctamente el archivo: " + file.getPath());
                }

            }

        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(uploadFile.class.getName()).log(Level.SEVERE, null, ex);
        }
;
        
        out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                +"<parametros>"
                + "<fechaText>"+fechaText+"</fechaText>"
                + "<rutaArchivo>"+file.getAbsolutePath()+"</rutaArchivo>"
                +"</parametros>");
        
    }

    public void crearDirectorio(String rutaDirectorio) {
        File directorio = new File(rutaDirectorio);
        directorio.mkdir();
    }

    public String guardarArchivo() {
        Date fecha =  new Date();
        Calendar fechaActual = Calendar.getInstance();
        fechaText = formatoFecha.format(fecha);
        String anio = Integer.toString(fechaActual.get(Calendar.YEAR));

        File carpetaAnual = new File(uploadDir + anio);
        String rutaCarpetaAnual = carpetaAnual.getPath();
        File carpetaDiaria = new File(uploadDir + anio + "\\" + fechaText);
        String rutaCarpetaDiaria = carpetaDiaria.getPath();

        if (carpetaAnual.exists()) {
            imprimir("Carpeta anual ya existe");
            if (carpetaDiaria.exists()) {
                imprimir("carpeta diaria ya existe");
                return rutaCarpetaDiaria;
            } else {
                crearDirectorio(rutaCarpetaDiaria);
                imprimir("se creó la carpeta diaria");
                return rutaCarpetaDiaria;
            }

        } else {
            crearDirectorio(rutaCarpetaAnual);
            imprimir("se creó la carpeta anual");
            if (carpetaDiaria.exists()) {
                imprimir("Carpeta diaria ya existe");
                return rutaCarpetaDiaria;
            } else {
                crearDirectorio(rutaCarpetaDiaria);
                imprimir("se creó la carpeta diaria");
                return rutaCarpetaDiaria;
            }
        }
        
    }

    public static void imprimir(Object o) {
        System.out.println(o);
    }
}
