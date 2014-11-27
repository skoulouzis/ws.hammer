/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.wtcw.vle.hammer.servlet;

import java.io.*;
import java.net.*;
//
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
//
import javax.servlet.*;
import javax.servlet.http.*;
//
import nl.wtcw.vle.hammer.util.HTMLWiz;
import nl.wtcw.vle.hammer.util.WebUtil;

/**
 *
 * @author vguevara
 */
public class Download extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            HTMLWiz.doHeader(out, "Download");

            String host = this.getServletContext().getInitParameter("host");
            String serverFolder = this.getServletContext().getInitParameter("serverFolder");

            String[] answer = request.getParameterValues("answer");
            if (answer != null) {

                Vector<String> filenames = new Vector<String>();

                for (int i = 0; i < answer.length; i++) {
                    filenames.addElement(answer[i] + ".owls");
                    filenames.addElement(answer[i] + ".xml");
                }

                // Create a buffer for reading the files
                byte[] buf = new byte[1024];

                // Create the ZIP file & URL
                String tmpFilename = "download." + WebUtil.Index() + ".zip";
                String outURL = host + "/resources/download/" + tmpFilename;
                String outFilename = serverFolder + File.separatorChar + ".." + File.separatorChar + "resources" + File.separatorChar + "download" + File.separatorChar + tmpFilename;

                ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(outFilename));
                outZip.setComment("WS-VLAM HAMMER: Automatically generated at " + outURL + ".");

                // Compress the files
                for (String file : filenames) {
                    FileInputStream in = new FileInputStream(serverFolder + File.separatorChar + file);

                    // Add ZIP entry to output stream.
                    outZip.putNextEntry(new ZipEntry(file));

                    // Transfer bytes from the file to the ZIP file
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        outZip.write(buf, 0, len);
                    }

                    // Complete the entry
                    outZip.closeEntry();
                    in.close();
                }

                // Complete the ZIP file
                outZip.close();
                
                HTMLWiz.doSearchForm(out, "New Search!");
                        
                out.println("<p class=\"text_head\">ZIP File</p>");
                out.println("<p class=\"text_body\">Please retrieve your selected descriptions enclosed within a ZIP file. This file can downloaded from the following link:<br>");
                out.println("<br>");
                out.println("<center>");
                out.println("<a href=\"" + outURL + "\"><img alt=\"Temporary file to download.\" src=\"resources/download-zip.png\" border=\"0\"></a>");
                out.println("</center>");
                out.println("<br>");
                out.println("<br>");
                out.println("<br>");
                out.println("<br>");
                out.println("<br>");
                out.println("<br>");
                out.println("<br>");
                out.println("<br>");
                out.println("<br>");
                out.println("<br>");
                out.println("<p class=\"text_head\">WARNING</p>");
                out.println("<p class=\"text_body\">HAMMER does not provide web hosting facilities, so you will need to download the ZIP file on your own file system. ");
                out.println("The generated file will only be kept on this web server for less than an hour (~30 min.). You <b>MUST</b> download them before they are deleted.</p>");
                
                HTMLWiz.doLastSearchForm(out, "New search!");
            } else {
                HTMLWiz.doSearchForm(out, "New Search!");
                out.println("<p class=\"text_head\">No descriptions were selected.</p>");
                out.println("<p class=\"text_body\">Please select first the descriptions you want to download.<br>");
            }

            HTMLWiz.doAppendix(out);

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
