/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servlet.upload;

/**
 *
 * @author Juan
 */
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
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

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
        if (!isMultipartContent) {
            out.println("You are not trying to upload<br/>");
            return;
        }
        System.out.println("Subiendo archivo");

        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            List<FileItem> fields = upload.parseRequest(request);
            Iterator<FileItem> it = fields.iterator();
            if (!it.hasNext()) {
                out.println("No fields found");
                return;
            }
            while (it.hasNext()) {
                FileItem fileItem = it.next();
                File file = new File("C:\\destino\\" + fileItem.getName());
                out.println(file.getPath());

                fileItem.write(file);

            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(uploadFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
