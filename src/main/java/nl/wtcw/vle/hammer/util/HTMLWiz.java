/*
 * HTMLWiz.java
 *
 * Created on March 20, 2007, 6:41 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package nl.wtcw.vle.hammer.util;

import java.io.*;

/**
 * HTML utility for Olingo
 *
 * @author vguevara
 *
 */
public final class HTMLWiz {

    /**
     * doHeader creates the initial templates for Servlets.
     *
     * @param out The message string to be the header
     * @param title Title of the page
     */
    public static void doHeader(PrintWriter out, String title)
            throws IOException {
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<html>");
        out.println("    <head>");
        out.println("        <link href=\"resources/style_vle.css\" rel=\"stylesheet\" type=\"text/css\">");
        out.println("        <link href=\"resources/favicon.ico\" rel=\"shortcut icon\" type=\"image/x-icon\" />");
        out.println("        <script>");
        out.println("            function printMe()  {");
        out.println("            parent.focus();");
        out.println("            parent.print();     }");
        out.println("        </script>");
        out.println("        <meta content=\"text/html; charset=ISO-8859-1\" http-equiv=\"content-type\">");
        out.println("        <title>WS-VLAM HAMMER: An sematic-based workflow match-maker (" + title + ")</title>");
        out.println("    </head>");
        out.println("    <body topmargin=\"0\" bottommargin=\"0\" leftmargin=\"0\" marginheight=\"0\" marginwidth=\"0\">");
        out.println("        <table style=\"width: 100%; text-align: left; height: 100%\" border=\"0\" cellpadding=\"0\"");
        out.println("            cellspacing=\"0\">");
        out.println("            <tbody>");
        out.println("                <tr style=\"vertical-align: top; height: 50px\">");
        out.println("                    <td style=\"vertical-align: top; width: 10px; background-color: rgb(0, 0, 0);\"></td>");
        out.println("                    <td style=\"vertical-align: top; width: 130px; background-color: rgb(0, 0, 0);\"></td>");
        out.println("                    <td style=\"vertical-align: top; width: 7px; background-color: rgb(102, 102, 102);\"></td>");
        out.println("                    <td style=\"vertical-align: top; width: 50px; background-color: rgb(204, 204, 204);\"></td>");
        out.println("                    <td style=\"vertical-align: top; background-color: rgb(204, 204, 204); text-align: right;\">");
        out.println("                        <a href=\"JavaScript:printMe();\">");
        out.println("                            <img title=\"Print\" alt=\"Print\" src=\"resources/printer.gif\" style=\"border: 0px solid ; width: 45px; height: 41px;\">");
        out.println("                        </a>");
        out.println("                    </td>");
        out.println("                    <td style=\"vertical-align: top; horizontal-align: left; width: 50px; background-color: rgb(204, 204, 204);\"></td>");
        out.println("                </tr>");
        out.println("                <tr style=\"vertical-align: top;\">");
        out.println("                    <td style=\"vertical-align: top; width: 10px; background-color: rgb(0, 0, 0);\"><br>");
        out.println("                    </td>");
        out.println("                    <td style=\"vertical-align: top; width: 130px; color: rgb(255, 255, 255); background-color: rgb(0, 0, 0);\">");
        out.println("                        <table style=\"color: rgb(255, 255, 255); width: 130px;\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
        out.println("                            <tbody>");
        out.println("                                <tr>");
        out.println("                                    <td>");
        out.println("                                        <img border=\"0\" src=\"resources/DESCRIPTION.png\" width=\"120\" height=\"19\"></a><br>");
        out.println("                                        <a href=\"index.html\"><img border=\"0\" src=\"resources/overview.png\" width=\"120\" height=\"19\" name=\"overview\" onmouseover=\"document['overview'].src='resources/overviewb.png'\" onmouseout=\"document['overview'].src='resources/overview.png'\"></a><br>");
        out.println("                                        <a href=\"howtos.html\"><img border=\"0\" src=\"resources/HOW-TOs.png\" width=\"120\" height=\"19\" name=\"howtos\" onmouseover=\"document['howtos'].src='resources/HOW-TOsb.png'\" onmouseout=\"document['howtos'].src='resources/HOW-TOs.png'\"></a><br>");
        out.println("                                        <a href=\"support.html\"><img border=\"0\" src=\"resources/support.png\" width=\"120\" height=\"19\" name=\"support\" onmouseover=\"document['support'].src='resources/supportb.png'\" onmouseout=\"document['support'].src='resources/support.png'\"></a><br>");
        out.println("                                        <a href=\"about.html\"><img border=\"0\" src=\"resources/about.png\" width=\"120\" height=\"19\" name=\"about\" onmouseover=\"document['about'].src='resources/aboutb.png'\" onmouseout=\"document['about'].src='resources/about.png'\"></a><br>");
        out.println("                                    </td>");
        out.println("                                </tr>");
        out.println("                            </tbody>");
        out.println("                        </table>");

        /*
        out.println("                        <table style=\"color: rgb(255, 255, 255); width: 130px;\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
        out.println("                            <tbody>");
        out.println("                                <tr>");
        out.println("                                    <td><big><span style=\"font-weight: bold;\"><br>Description</span></big><br></td>");
        out.println("                                </tr>");
        out.println("                                <tr>");
        out.println("                                    <td><span id=\"option1\" class=\"main_menu_normal\"><a href=\"index.html\">&nbsp; overview</a></span></td>");
        out.println("                                </tr>");
        out.println("                                <tr>");
        out.println("                                    <td><span id=\"option2\" class=\"main_menu_normal\"><a href=\"howtos.html\">&nbsp; HOW-TOs</a></span></td>");
        out.println("                                </tr>");
        out.println("                                <tr>");
        out.println("                                    <td><span id=\"option3\" class=\"main_menu_normal\"><a href=\"support.html\">&nbsp; support</a></span></td>");
        out.println("                                </tr>");
        out.println("                                <tr>");
        out.println("                                    <td><span id=\"option4\" class=\"main_menu_normal\"><a href=\"about.html\">&nbsp; about</a></span></td>");
        out.println("                                </tr>");
        out.println("                                <tr>");
        out.println("                                    <td><big><span style=\"font-weight: bold;\"><br>Generation</span></big><br>");
        out.println("                                    </td>");
        out.println("                                </tr>");
        out.println("                                <tr>");
        out.println("                                    <td><span id=\"option5\" class=\"main_menu_normal\"><a href=\"relational.html\">&nbsp; relational</a></span></td>");
        out.println("                                </tr>");
        out.println("                                <tr>");
        out.println("                                    <td><span id=\"option6\" class=\"main_menu_normal\"><a href=\"java.html\">&nbsp; java</a></span></td>");
        out.println("                                </tr>");
        out.println("                                <tr>");
        out.println("                                    <td><span id=\"option7\" class=\"main_menu_normal\"><a href=\"xmlschema.html\">&nbsp; xml schema</a></span></td>");
        out.println("                                </tr>");
        out.println("                                <tr>");
        out.println("                                    <td><span id=\"option8\" class=\"main_menu_normal\"><a href=\"castor.html\">&nbsp; castor</a></span></td>");
        out.println("                                </tr>");
        out.println("                                <tr>");
        out.println("                                    <td><span id=\"option9\" class=\"main_menu_normal\"><a href=\"hibernate.html\">&nbsp; hibernate</a></span></td>");
        out.println("                                </tr>");
        out.println("                            </tbody>");
        out.println("                        </table>");
         */
        out.println("                        <br>");
        out.println("                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">");
        out.println("                            <tbody>");
        out.println("                                <tr>");
        out.println("                                    <td style=\"text-align: center;\" valign=\"top\"><br>");
        out.println("                                        <img alt=\"HAMMER\" title=\"HAMMER\" src=\"resources/hammer_logo.png\"");
        out.println("                                        style=\"border: 0px solid ; width: 120px; height: 141px;\">");
        out.println("                                    </td>");
        out.println("                                </tr>");
        out.println("                            </tbody>");
        out.println("                        </table>");
        out.println("                    </td>");
        out.println("                    <td style=\"vertical-align: top; width: 7px; background-color: rgb(102, 102, 102);\"><br>");
        out.println("                    </td>");
        out.println("                    <td style=\"vertical-align: top; width: 50px;\"><br>");
        out.println("                    </td>");
        out.println("                    <td style=\"vertical-align: top;\">");
//        out.println("                        &nbsp;");
    }

    public static void doLastSearchForm(PrintWriter out, String search) {
        out.println("<form name=\"formE\" action=\"HammerEngineServlet\" method=\"POST\">");
        out.println("   <p class=\"text_body\">");
        out.println("   <table border=\"0\" bgcolor=\"#EEEEFF\" width=\"100%\">");
        out.println("       <tr><td>");
        out.println("       <center><br>");
        out.println("       <input type=\"hidden\" name=\"requestType\" value=\"query\">");
        out.println("       <input type=\"hidden\" name=\"queryType\" value=\"keywords\">");
        out.println("       <input type=\"text\" name=\"query\" size=\"85\" value=\"" + search.trim() + "\">");
        out.println("       <input style=\"background-color: #6495ed; color: #ffffff; font-weight: bold;\" type=\"submit\" value=\"Search\">");
        out.println("       <br><br></center>");
        out.println("       </td><td>");
        out.println("   </table>");
        out.println("   </p>");
        out.println("</form>");
    }

    public static void doTitle(PrintWriter out, String title) {
        out.println("&nbsp;");
        out.println("                 <table border=\"0\" style=\"text-align: center; margin-left:auto; margin-right:auto;\">");
        out.println("                     <tr><td style=\"text-align: center; width: 60px;\">");
        out.println("                             <img alt=\"WS-HAMMER Logo\" title=\"WS-HAMMER\" src=\"resources/hammer_logo_web_white.png\" style=\"border: 0px solid;\">");
        out.println("                         </td><td class=\"text_title\" style=\"text-align: left;\">");
        out.println("                             " + title);
        out.println("                     </td></tr>");
        out.println("                     <tr><td colspan=\"2\">");
        out.println("                             <img alt=\"WS-VLAM\" title=\"WS-VLAM Hammmer\" src=\"resources/ws-vlam_web_top_name.png\" style=\"border: 0px solid ;\">");
        out.println("                     </td></tr>");
        out.println("                 </table>");
    }

    public static void doSearchForm(PrintWriter out, String search) {
        out.println("                        <form name=\"form\" action=\"HammerEngineServlet\" method=\"GET\">");
        out.println("                            &nbsp;");
        out.println("                            <table border=\"0\" style=\"text-align: center; margin-left:auto; margin-right:auto;\">");
        out.println("                                <tr><td style=\"text-align: center; width: 60px;\">");
        out.println("                                        <img alt=\"WS-HAMMER Logo\" title=\"WS-HAMMER\" src=\"resources/hammer_logo_web_white.png\" style=\"border: 0px solid;\">");
        out.println("                                    </td><td class=\"text_title\" style=\"text-align: left;\">");
        out.println("                                        <input type=\"text\" name=\"query\" size=\"55\" value=\"" + search.trim() + "\">");
        out.println("                                        <input style=\"background-color: #6495ed; color: #ffffff; font-weight: bold;\" type=\"submit\" value=\"Search\">");
        out.println("                                        <span style=\"text-align: left; font-size: 10px;\"><a href=\"discover_advanced.html\">Advanced Search</a></span>");
        out.println("                                        <input type=\"hidden\" name=\"requestType\" value=\"query\">");
        out.println("                                        <input type=\"hidden\" name=\"queryType\" value=\"keywords\">");
        out.println("                                </td></tr>");
        out.println("                                <tr><td colspan=\"2\">");
        out.println("                                        <img alt=\"WS-VLAM\" title=\"WS-VLAM Hammmer\" src=\"resources/ws-vlam_web_top_name.png\" style=\"border: 0px solid ;\">");
        out.println("                                </td></tr>");
        out.println("                            </table>");
        out.println("                        </form>");
    }

    /**
     *
     * doAppendix creates the last part templates for Servlets.
     *
     */
    public static void doAppendix(PrintWriter out)
            throws IOException {
        out.println("                        <br><br>");
        out.println("                    </td>");
        out.println("                    <td style=\"vertical-align: top; horizontal-align: left; width: 50px;\"></td>");
        out.println("                </tr>");
        out.println("                <tr style=\"vertical-align: bottom;\">");
        out.println("                    <td style=\"vertical-align: top; width: 10px; background-color: rgb(0, 0, 0);\"><br>");
        out.println("                    </td>");
        out.println("                    <td style=\"vertical-align: middle; width: 130px; background-color: rgb(0, 0, 0); text-align: center; color: rgb(255, 255, 255);\">");
        out.println("                    VL-e SP 2.5<br>");
        out.println("                    <small><small>Kruislaan 403<br>1098 SJ, Amsterdam</></small></small></td>");
        out.println("                    <td style=\"vertical-align: top; width: 7px; background-color: rgb(102, 102, 102);\"><br>");
        out.println("                    </td>");
        out.println("                    <td style=\"vertical-align: top; width: 50px;\"><br>");
        out.println("                    </td>");
        out.println("                    <td style=\"vertical-align: middle; text-align: left;\">");
        out.println("                        <small>Copyright &copy; 2005-2008. UvA and Contributors. All rights reserved.</small><br>");
        out.println("                        <img src=\"resources/line_light.gif\" border=\"0\" height=\"3\" width=\"100%\">");
        out.println("                        <img src=\"resources/vle_sticker.png\" width=\"340\" height=\"56\"> ");
        out.println("                    </td>");
        out.println("                    <td style=\"vertical-align: top; horizontal-align: left; width: 50px;\"></td>");
        out.println("                </tr>");
        out.println("            </tbody>");
        out.println("        </table>");
        out.println("    </body>");
        out.println("</html>");
    }
}
