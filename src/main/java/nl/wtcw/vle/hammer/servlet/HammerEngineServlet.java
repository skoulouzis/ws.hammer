/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.wtcw.vle.hammer.servlet;

import java.io.*;
import java.net.*;
//
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
//
import javax.servlet.*;
import javax.servlet.http.*;
//
import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.UploadBean;
import javazoom.upload.UploadFile;
//
import nl.wtcw.vle.hammer.engine.HammerEngine;
import nl.wtcw.vle.hammer.engine.MatchResult;
import nl.wtcw.vle.hammer.util.HTMLWiz;
import nl.wtcw.vle.hammer.util.WebUtil;
//
import org.apache.log4j.Logger;

/**
 *
 * @author vguevara
 */
public class HammerEngineServlet extends HttpServlet {

    private HammerEngine engine;
    private String host;
    private String serverFolder;
    private String whitleListW;
    private String whitleListO;
    private String whiteListArch;
    private Logger logger = Logger.getLogger(HammerEngineServlet.class);

    @Override
    public void init() throws ServletException {
        // Gets the parameters
        serverFolder = this.getServletContext().getInitParameter("serverFolder");

        host = this.getServletContext().getInitParameter("host");
        if ((host.lastIndexOf("/") + 1) == host.length()) {
            host = host.substring(0, host.lastIndexOf("/"));
        }

        whitleListW = this.getServletContext().getInitParameter("whiteListWSVLAM");
        whitleListO = this.getServletContext().getInitParameter("whiteListOWLS");
        whiteListArch = this.getServletContext().getInitParameter("whiteListArch");

        engine = new HammerEngine(serverFolder, host);
        //engine.printRegistry();
    }

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

            // checks if the request is for publishing or not (attached files!)
            if (MultipartFormDataRequest.isMultipartFormData(request)) {
                //this request is to publish
                HTMLWiz.doHeader(out, "Publish");

                processPublishRequest(request, out);
            } else {

                String action = request.getParameter("requestType");
                if (action.toUpperCase().equals("REVOKE")) {
                    HTMLWiz.doHeader(out, "Revoke of services");
                    processRevokeRequest(request, out);
                } else if (action.toUpperCase().equals("QUERY")) {
                    HTMLWiz.doHeader(out, "Discovery of services");
                    String queryType = request.getParameter("queryType");
                    if (queryType.toUpperCase().equals("ADVANCED")) {
                        processAdvancedQueryRequest(request, out);
                    } else { // the query request is the "KEYWORDS"
                        processSimpleQueryRequest(request, out);
                    }
                }
            }

            HTMLWiz.doAppendix(out);

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
        return "WS-VLAM HAMMER: Main servelet";
    }

    private void doConfigTable(PrintWriter out, URI finalOWL, URI finalXML, URI finalArch, String author) {
        String owlFilename = serverFolder + File.separator + author + File.separator
                + finalOWL.toString().substring(finalOWL.toString().lastIndexOf("/") + 1);
        String xmlFilename = serverFolder + File.separator + author + File.separator
                + finalXML.toString().substring(finalXML.toString().lastIndexOf("/") + 1);
        String archFilename = serverFolder + File.separator + author + File.separator
                + finalArch.toString().substring(finalArch.toString().lastIndexOf("/") + 1);

        File fileOWL = new File(owlFilename);
        File fileXML = new File(xmlFilename);
        File fileArch = new File(archFilename);

        String filesizeOWL = " (" + fileOWL.length() + " bytes)";
        String filesizeXML = " (" + fileXML.length() + " bytes)";
        String filesizeArch = " (" + fileArch.length() + " bytes)";

        HTMLWiz.doTitle(out, "Advertisement status");

        out.println("<table width=\"625\">");

        out.println("   <tr>");
        out.println("       <td class=\"table_head\"><p>Parameter</p></td>");
        out.println("       <td colspan=\"2\" class=\"table_head\">Value</td>");
        out.println("   </tr>");

        out.println("   <tr>");
        out.println("       <td class=\"table_title\" width=\"122\">OWL-S file</td>");
        out.println("       <td class=\"text_list\">" + fileOWL.getName() + " " + filesizeOWL + "</td>");
        out.println("   </tr>");

        out.println("   <tr>");
        out.println("       <td class=\"table_title\" width=\"122\">OWL-S URL</td>");
        out.println("       <td class=\"text_list\"> <a href=\"" + finalOWL + "\">" + finalOWL + "</a>");
        out.println("   </tr>");

        out.println("   <tr>");
        out.println("       <td class=\"table_title\" width=\"122\">WS-VLAM file</td>");
        out.println("       <td class=\"text_list\">" + fileXML.getName() + " " + filesizeXML + "</td>");
        out.println("   </tr>");

        out.println("   <tr>");
        out.println("       <td class=\"table_title\" width=\"122\">WS-VLAM URL</td>");
        out.println("       <td class=\"text_list\"> <a href=\"" + finalXML + "\">" + finalXML + "</a>");
        out.println("   </tr>");


        out.println("   <tr>");
        out.println("       <td class=\"table_title\" width=\"122\">Archive file</td>");
        out.println("       <td class=\"text_list\">" + fileArch.getName() + " " + filesizeArch + "</td>");
        out.println("   </tr>");

        out.println("   <tr>");
        out.println("       <td class=\"table_title\" width=\"122\">Archive URL</td>");
        out.println("       <td class=\"text_list\"> <a href=\"" + finalArch + "\">" + finalArch + "</a>");
        out.println("   </tr>");

        out.println("</table><br>");
    }

    private void doEmptyResults(PrintWriter out, String search) {
        out.println("   <p class=\"text_head\">No results found.</p>");
        out.println("   <p class=\"text_body\">");
        out.println("      Your search -<b>" + search + "</b>- did not macht any documents in the WS-VLAM repository.");
        out.println("   </p>");
        out.println("   <p class=\"text_body\">");
        out.println("      Suggestions:");
        out.println("   </p>");
        out.println("   <ul class=\"text_body\">");
        out.println("         <li>Make sure all words are spelled correctly.</li>");
        out.println("         <li>Try different keywords.</li>");
        out.println("         <li>Try more general keywords.</li>");
        out.println("         <li>Try fewer keywords.</li>");
        out.println("         <li>Try another type of search [by "
                + "<a href=\"discover.html\"> keywords </a>, by "
                + "<a href=\"discover_advanced.html\">OWL-S profile</a>].</li>");
        out.println("   </ul>");
        out.println("   <p>&nbsp;</p>");
        out.println("   <p>&nbsp;</p>");

        HTMLWiz.doLastSearchForm(out, search);
    }

    private void doErrorTable(PrintWriter out, String filenameOWL, String filenameXML, String filenameArch) {

        HTMLWiz.doTitle(out, "Advertisement status (error)");

        out.println("<table width=\"625\">");
        out.println("   <tr>");
        out.println("       <td class=\"table_head\"><p>Parameter</p></td>");
        out.println("       <td colspan=\"2\" class=\"table_head\">Value</td>");
        out.println("   </tr>");

        if (filenameOWL == null) {
            out.println("   <tr>");
            out.println("       <td class=\"table_title\" width=\"122\">WS-VLAM file</td>");
            out.println("       <td class=\"text_list\"> <img src=\"resources/cancel.gif\" style=\"border: 0px solid;\"> The WS-VLAM file (*.xml) was not uploaded.</td>");
            out.println("   </tr>");
        }

        if (filenameXML == null) {
            out.println("   <tr>");
            out.println("       <td class=\"table_title\" width=\"122\">OWL-S file</td>");
            out.println("       <td class=\"text_list\"> <img src=\"resources/cancel.gif\" style=\"border: 0px solid;\"> The OWL-S file (*.owl, *.owls) was not uploaded.</td>");
            out.println("   </tr>");
        }

        if (filenameArch == null) {
            out.println("   <tr>");
            out.println("       <td class=\"table_title\" width=\"122\">Archive file</td>");
            out.println("       <td class=\"text_list\"> <img src=\"resources/cancel.gif\" style=\"border: 0px solid;\"> The Archive file (*.tar, *.tar.gz,*.zip) was not uploaded.</td>");
            out.println("   </tr>");
        }

        out.println("</table><br>");
    }

    private void printResults(PrintWriter out, Vector<MatchResult> results) {
        out.println("<form name=\"form\" action=\"Download\" method=\"POST\">");

        if (results.size() > 0) {
            out.println("<img src=\"resources/arrow_left_down.gif\" border=\"0\">");
            out.println("<input type=\"image\" src=\"resources/download-list.png\" name=\"Download List\">");
            out.println("<table border=\"0\" width=\"100%\">");
        }

        for (MatchResult elem : results) {
            String name = elem.getName().trim()+elem.getDateUploaded().trim();
            
            out.println("       <tr>");
            out.println("          <td>");
            out.println("             <input type=\"checkbox\" name=\"answer\" value=\"" + elem.getName().trim()  + "\" />");
            out.println("          </td><td>");
            out.println("             <b>" + name + "</b> - ");
            out.println("             <b> Uploaded by "+elem.getAuthor() +"</b>");
            out.println("             <font style=\"font-family: Arial, Helvetica, sans-serif\" size=\"-2\">");
            out.println("             <b>[</b>With a degree of <b>" + elem.getSimilarity() + "</b> of match<b>]</b>");
            out.println("             </font>");
            out.println("          </td>");
            out.println("       <tr>");
            out.println("          <td></td>");
            out.println("          <td class=\"text_list\">");
            out.println(elem.getDescription());
            out.println("          </td>");
            out.println("       </tr>");
            out.println("       <tr>");
            out.println("          <td></td>");
            out.println("          <td class=\"text_list\">");
            out.println("           <a href=\"" + elem.getOWL() + "\"><img alt=\"" + elem.getOWL() + "\" src=\"resources/owls-file.png\" border=\"0\"> </a>&nbsp;&nbsp;&nbsp;");
            out.println("           <a href=\"" + elem.getXML() + "\"><img alt=\"" + elem.getXML() + "\" src=\"resources/ws-vlam-file.png\" border=\"0\"> </a> <br>&nbsp;");
            out.println("           <a href=\"" + elem.getArch() + "\"><img alt=\"" + elem.getArch() + "\" src=\"resources/download-zip.png\" border=\"0\"> </a> <br>&nbsp;");
            out.println("          </td>");
            out.println("       </tr>");
        }

        if (results.size() > 0) {
            out.println("</table>");
            out.println("<img src=\"resources/arrow_left_up.gif\" border=\"0\">");
            out.println("<input type=\"image\" src=\"resources/download-list.png\" name=\"Download List\">");
        }

        out.println("</form>");
    }

    private void processAdvancedQueryRequest(HttpServletRequest request, PrintWriter out) {
        String search = request.getParameter("data");
        //search = WebUtil.supertrim(search);

        URI query = saveString(search, true);

        HTMLWiz.doSearchForm(out, "[Advanced discovery]");

        if (query == null) {
        } else {
            String matchingType = request.getParameter("matchingType");
            String minDegree = request.getParameter("minDegree");
            String threshold = request.getParameter("threshold");

            if (matchingType == null) {
                matchingType = "M0";
            }
            if (minDegree == null) {
                minDegree = "EXACT";
            }
            if (threshold == null) {
                threshold = "0";
            }

            engine.setQuery(query);

            if (matchingType.toUpperCase().equals("M0")) {
                engine.setSimilarityMeasure(HammerEngine.SIMILARITY_LOGIC);
            } else if (matchingType.toUpperCase().equals("M1")) {
                engine.setSimilarityMeasure(HammerEngine.SIMILARITY_LOI);
            } else if (matchingType.toUpperCase().equals("M2")) {
                engine.setSimilarityMeasure(HammerEngine.SIMILARITY_EXTENDED_JACCARD);
            } else if (matchingType.toUpperCase().equals("M3")) {
                engine.setSimilarityMeasure(HammerEngine.SIMILARITY_COSINE);
            } else if (matchingType.toUpperCase().equals("M4")) {
                engine.setSimilarityMeasure(HammerEngine.SIMILARITY_JENSEN_SHANNON);
            } else //default
            {
                engine.setSimilarityMeasure(HammerEngine.SIMILARITY_LOGIC);
            }

            if (minDegree.toUpperCase().equals("EXACT")) {
                engine.setMinDegree(HammerEngine.EXACT);
            } else if (minDegree.toUpperCase().equals("PLUGIN")) {
                engine.setMinDegree(HammerEngine.PLUGIN);
            } else if (minDegree.toUpperCase().equals("SUBSUMES")) {
                engine.setMinDegree(HammerEngine.SUBSUMES);
            } else if (minDegree.toUpperCase().equals("SUBSUMES-BY")) {
                engine.setMinDegree(HammerEngine.SUBSUMES_BY);
            } else if (minDegree.toUpperCase().equals("NEAREST-NEIGBOUR")) {
                engine.setMinDegree(HammerEngine.NEAREST_NEIGBOUR);
            }

            engine.setTreshold(Integer.valueOf(threshold));

            Vector<MatchResult> results = engine.executeQuery();

            if ((results == null) || (results.size() == 0)) {
                doEmptyResults(out,
                        "[advanced discovery ("
                        + matchingType + ", "
                        + minDegree + ", "
                        + threshold + ")]");
            } else {
                printResults(out, results);
            }
        }
    }

    private void processPublishRequest(HttpServletRequest originalrequest, PrintWriter out) {
        // temporal index 
        String index = WebUtil.Index();

        //setting up enviroment
        String tmpStoreFolder = serverFolder + File.separator + "temp" + File.separator + index + File.separator;
        String repositoryURI = host + "/repository/";
        String tempURI = repositoryURI + "temp/" + index + "/";

        String filenameOWL = null;
        String filenameXML = null;
        String filenameArch = null;
        String author = null;

        URI finalOWL = null;
        URI finalXML = null;
        URI finalArch = null;
        try {

            // Uses MultipartFormDataRequest to parse the HTTP request.
            MultipartFormDataRequest request = new MultipartFormDataRequest(originalrequest);
            author = request.getParameter("author");
            repositoryURI += author + "/";
            File authorFolder = new File(serverFolder + File.separator + author);
            if (!authorFolder.exists()) {
                authorFolder.mkdir();
            }


            //Retrieving filename
            Hashtable files = request.getFiles();

            if ((files != null) && (!files.isEmpty())) {

                // Uses the UploadBean now to store the file
                UploadBean upLoad = new UploadBean();
                upLoad.setOverwrite(true);
                upLoad.setFolderstore(tmpStoreFolder);

                //Writes the OWL-S file
                upLoad.setWhitelist(whitleListO);
                upLoad.store(request, "fileOWLS");

                //Writes the XML file
                upLoad.setWhitelist(whitleListW);
                upLoad.store(request, "fileXML");

                //Writes the Arch file
                upLoad.setWhitelist(whiteListArch);
                upLoad.store(request, "fileArch");

                //Gets the name of the OWL-S file
                UploadFile fileOWL = (UploadFile) files.get("fileOWLS");
                if (fileOWL.getFileSize() > 0) {
                    filenameOWL = fileOWL.getFileName();
                } else {
                    filenameOWL = null;
                }

                //Gets the name of the XML file
                UploadFile fileXML = (UploadFile) files.get("fileXML");
                if (fileXML.getFileSize() > 0) {
                    filenameXML = fileXML.getFileName();
                } else {
                    filenameXML = null;
                }


                //Gets the name of the Arch file
                UploadFile fileArch = (UploadFile) files.get("fileArch");
                if (fileArch.getFileSize() > 0) {
                    filenameArch = fileArch.getFileName();
                } else {
                    fileArch = null;
                }
            }
        } catch (Exception ex) {
            filenameOWL = null;
            filenameXML = null;
            filenameArch = null;
            logger.error("[PROCESSING MultipartFormDataRequest]");
            ex.printStackTrace();
        }

        // either temporally files must be written 
        // before calling the Hammer engine
        if (filenameOWL == null || filenameXML == null || filenameArch == null || author == null || author.equals("")) {
            out.println("<p class=\"text_head\">Invalid workflow description. Make sure you have provided an author name, and valid OWL, XML, and GZ files."
                    + " Please click <a href=\"publish.html\">here.</a>"
                    + " to try again.</p>");
            doErrorTable(out, filenameOWL, filenameXML, filenameArch);
        } else {
            try {
                URI tmpPublishOWL = new URI(tempURI + filenameOWL);
                URI tmpPublishXML = new URI(tempURI + filenameXML);
                URI tmpPublishArch = new URI(tempURI + filenameArch);

                //calls the engine to perform the actual advertisement
                engine.publish(author, tmpPublishOWL, tmpPublishXML, tmpPublishArch);

                //possible URIs (?) of published files
                //they may not be the real ones... <check later>
                finalOWL = new URI(repositoryURI + WebUtil.addDateToFileName(filenameOWL));
                finalXML = new URI(repositoryURI + WebUtil.addDateToFileName(filenameXML));
                finalArch = new URI(repositoryURI + WebUtil.addDateToFileName(filenameArch));

                doConfigTable(out, finalOWL, finalXML, finalArch, author);

            } catch (Exception ex) {

                if (ex.getMessage().equals("Advertisement already in the repository")) {


//                    String confirmVersion = "<form>"
//                            + "<p class=\"text_body\">Advertisement already in the repository. Is this a new version?<br>	"
//                            + "<input type=\"submit\" name=\"submitNew\" value=\"Yes\">"
//                            + "<input type=\"submit\" name=\"submitOld\" value=\"No\">"
//                            + "<br>"
//                            + "<br>"
//                            + "</form>";
//                    out.println(confirmVersion);


                    out.println("<p class=\"text_head\">Advertisement already in the repository</p>");
                    doErrorTable(out, null, null, null);
                } else {
                    logger.error("[CREATE URI] baseURI (temp-OWL) : (" + tempURI + ")(" + filenameOWL + ")");
                    logger.error("[CREATE URI] baseURI (final-OWL) : (" + repositoryURI + ")(" + filenameOWL + ")");
                    ex.printStackTrace();
                }

            } finally {
                //clean up and add date to file names
                deleteFolder(new File(tmpStoreFolder));

            }

            out.println("<p>&nbsp;</p>");
            out.println("<p class=\"text_body\"><a href=\"" + host + "\"><b>> Return to homepage.</b></a></p>");
        }
    }

    private void processRevokeRequest(HttpServletRequest request, PrintWriter out) {
        String owls_url = request.getParameter("owls_url");
        out.println("<p>&nbsp;</p>");
        out.println("<p class=\"text_head\">Status</p>");

        try {
            URI toRevokeOWL = new URI(owls_url);

            if (engine.revoke(toRevokeOWL)) {
                out.println("<p class=\"text_body\">The advertisement asociated with the URL '" + owls_url + "' was succesfuly withdrawn.</p>");
            } else {
                out.println("<p class=\"text_body\">WS-HAMMER was unable to withdraw the advertisement asociated with the URL '" + owls_url + "'.</p>");
            }
        } catch (URISyntaxException ex) {
            out.println("<p class=\"text_body\">WS-HAMMER was unable to withdraw the advertisement asociated with the URL '" + owls_url + "'. The syntax of the URL is not valid!</p>");
        }
    }

    private void processSimpleQueryRequest(HttpServletRequest request, PrintWriter out) {
        String search = request.getParameter("query");
        search = WebUtil.supertrim(search);

        URI query = saveString(search, false);

        HTMLWiz.doSearchForm(out, search);

        if (query == null) {
        } else {
            engine.setQuery(query);
            engine.setSimilarityMeasure(HammerEngine.SIMILARITY_KEYWORD);

            Vector<MatchResult> results = engine.executeQuery();

            if ((results == null) || (results.size() == 0)) {
                doEmptyResults(out, search);
            } else {
                printResults(out, results);
            }
        }
    }

    private URI saveString(String toSave, boolean advanced) {
        URI toReturn = null;

        String index = WebUtil.Index();
        String filename = index + ".tmp";
        String fullPath = serverFolder + File.separator + "temp" + File.separator + filename;
        String tempURI = host + "/repository/temp/" + filename;

        try {

            File outFile = new File(fullPath);
            FileWriter out = new FileWriter(outFile);

            if (advanced) {
                toSave = toSave.replace("http://server.host/ws-hammer/query/query.owls", tempURI);
                toSave = toSave.replace("http://server.host/ws-hammer/", host);
            }

            out.write(toSave);
            out.close();

            toReturn = new URI(tempURI);
        } catch (Exception ex) {
            logger.error("[CREATION OF TEMP FILE] fullPath : (" + fullPath + ")");
            ex.printStackTrace();
        }

        return toReturn;
    }
    // </editor-fold>

    private boolean deleteFolder(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteFolder(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();

    }
}
