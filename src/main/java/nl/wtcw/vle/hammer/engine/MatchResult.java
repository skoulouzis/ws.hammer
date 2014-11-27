/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.wtcw.vle.hammer.engine;

/**
 *
 * @author vguevara
 */
// <editor-fold defaultstate="collapsed" desc="MatchResult methods. Click on the + sign on the left to edit the code.">
public class MatchResult implements Comparable {

    private static final long serialVersionUID = 1L;
    private String name;
    private double similarity = 0.0;
    private String description;
    private String degreeOfMatch;
    private String url_OWL;
    private String url_XML;
    private String url_Arch;
    private String author;
    private String dateUploaded;

    MatchResult() {
        this.similarity = 0.0;
        this.name = "";
        this.description = "";
        this.degreeOfMatch = "";
        this.url_OWL = "";
        this.url_XML = "";
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getName() {
        return this.name;
    }

    public void setSimilarity(double value) {
        this.similarity = value;
    }

    public double getSimilarity() {
        return this.similarity;
    }

    public void setDescription(String value) {
        this.description = value;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDegreeOfMatch(String value) {
        this.degreeOfMatch = value;
    }

    public String getDegreeOfMatch() {
        return this.degreeOfMatch;
    }

    public void setOWL(String owl) {
        this.url_OWL = owl;
    }

    public String getOWL() {
        return this.url_OWL;
    }

    public void setXML(String xml) {
        this.url_XML = xml;
    }

    public String getXML() {
        return this.url_XML;
    }

    @Override
    public String toString() {
        return "(S-" + name + "), " + degreeOfMatch + ", SIM(" + similarity + ", " + url_OWL + ")";
    }

    public int compareTo(Object obj2) {
        final int EQUAL = 0;

        if (this == obj2) {
            return EQUAL;
        }

        MatchResult result = (MatchResult) obj2;
        if ((this.name.equals(result.getName()))
                && (this.degreeOfMatch.equals(result.getDegreeOfMatch()))
                && (this.similarity == result.getSimilarity())) {
            return EQUAL;
        } else {
            Float o1 = new Float(this.similarity);
            Float o2 = new Float(result.getSimilarity());

            return Float.compare(o1, o2);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MatchResult)) {
            return false;
        }

        MatchResult m = (MatchResult) obj;
        return (this.similarity == m.getSimilarity());
    }

    @Override
    public int hashCode() {
        int hash = 7;

        long value = new Long(Math.round(similarity * 100));

        hash = 29 * hash + (int) value;
        return hash;
    }

    public String getArch() {
        return url_Arch;
    }

    public void setArch(String url) {
        this.url_Arch = url;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDateUploaded() {
        return this.dateUploaded;
    }

    public void setDateUploaded(String dateUploaded) {
        this.dateUploaded = dateUploaded;
    }
}
    // </editor-fold>
