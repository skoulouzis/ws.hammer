/*
 * Settings.java
 *
 * Created on March 23, 2006, 12:09 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package nl.wtcw.vle.hammer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

/**
 *
 * @author vguevara
 */
public class WebUtil {

    private static String version = "0.9 (build: 20080320/alpha release)";
    public static final String[] archExt = {"zip", "tar", "gz"};

    public static boolean FileExists(String filename) {
        boolean exit = false;
        boolean isArch = false;

        try {
            File inputFile = new File(filename);

            //S.K.: Tmp fix for archive files
            isArch = isFileArch(filename);

            if (!isArch && inputFile.canRead()) {
                exit = true;
            } else if (isArch && inputFile.exists()) {
                exit = true;
            } else {
                exit = false;
            }
        } catch (Exception e) {
            System.out.println("WebUtil (FileExists: There was an error:\n" + e);
            e.printStackTrace();
        }
        
        return exit;
    }

    public static void FileCopy(String source, String target)
            throws Exception {
        File inputFile = new File(source);
        File outputFile = new File(target);

        InputStream in = new FileInputStream(inputFile);
        OutputStream out = new FileOutputStream(outputFile);

        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        in.close();
        out.close();
    }

    public static void FileURICopy(URI source, String target)
            throws Exception {

        BufferedReader in = URIBufferedReader(source);
        File outputFile = new File(target);
        FileWriter out = new FileWriter(outputFile);
        //OutputStream out = new FileOutputStream(outputFile);

        char[] buf = new char[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }

        in.close();
        out.close();
    }

    //Returns a reader to the URL
    public static BufferedReader URLBufferedReader(URL source)
            throws Exception {

        BufferedReader in = new BufferedReader(new InputStreamReader(source.openStream()));
        return in;
    }

    //Returns a reader to the URI
    public static BufferedReader URIBufferedReader(URI source)
            throws Exception {
        URL sourceURL = source.toURL();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(sourceURL.openStream()));
        return in;
    }

    /* remove leading whitespace */
    public static String ltrim(String source) {
        return source.replaceAll("^\\s+", "");
    }

    /* remove trailing whitespace */
    public static String rtrim(String source) {
        return source.replaceAll("\\s+$", "");
    }

    /* replace multiple whitespaces between words with single blank */
    public static String itrim(String source) {
        return source.replaceAll("\\b\\s{2,}\\b", " ");
    }

    /* remove all superfluous whitespaces in source string */
    public static String supertrim(String source) {
//        return itrim(ltrim(rtrim(source)));
        return itrim(source.trim());
    }

    //Returns a random positive :) number in String
    public static String Random() {
        Random generator = new Random();
        int r = generator.nextInt();
        if (r < 0) {
            r = r * -1;
        }

        return String.valueOf(r);
    }

    public static String Index() {
        Long l = System.currentTimeMillis();
        return l.toString();
    }

    private static boolean isFileArch(String filename) {
        String ext = "";
        int mid = filename.lastIndexOf(".");
        ext = filename.substring(mid + 1, filename.length());

        for (String fExt : archExt) {
            if (ext.equals(fExt)) {
                return true;
            }
        }
        return false;
    }
    
     public  static String addDateToFileName(String fileNameOrPath) {
        File f = new File(fileNameOrPath);
        String fileName = f.getName();
        
        String fname = "";
        String ext = "";
        String[] parts = fileName.split("\\.");
        fname = parts[0];
        for (int i = 1; i < parts.length; i++) {
            ext += "." + parts[i];
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
        Calendar currentDate = Calendar.getInstance();
        String dateNow = formatter.format(currentDate.getTime());
        fname += dateNow;
        
        parts = fileNameOrPath.split(File.separator);
        String fullName = fileNameOrPath.replace(fileName, fname + ext);
        
        return fullName;
    }
}
