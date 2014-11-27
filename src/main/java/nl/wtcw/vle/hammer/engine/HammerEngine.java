/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.wtcw.vle.hammer.engine;

import EDU.cmu.Atlas.owls1_1.builder.OWLS_Builder_Util;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Object_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_Builder;
import EDU.cmu.Atlas.owls1_1.builder.OWLS_Store_BuilderFactory;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfActorException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfExpressionException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfParameterException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProcessException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfProfileException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfResultException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceCategoryException;
import EDU.cmu.Atlas.owls1_1.exception.NotInstanceOfServiceException;
import EDU.cmu.Atlas.owls1_1.expression.Condition;
import EDU.cmu.Atlas.owls1_1.process.Process;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.Vector;
import java.util.Iterator;
import java.util.SortedSet;

import org.apache.log4j.Logger;

import owlsmx.SimilarityMatchingEngine;
import owlsmx.data.MatchedService;
import owlsmx.exceptions.MatchingException;
import owlsmx.similaritymeasures.SimilarityMeasure;
import owlsmx.utils.MatchmakerUtils;

import EDU.cmu.Atlas.owls1_1.service.Service;
import EDU.cmu.Atlas.owls1_1.service.OWLSServiceModel;
import EDU.cmu.Atlas.owls1_1.parser.DefaultOWLSErrorHandler;
import EDU.cmu.Atlas.owls1_1.parser.OWLSServiceParser;
import EDU.cmu.Atlas.owls1_1.process.AtomicProcess;
import EDU.cmu.Atlas.owls1_1.process.Input;
import EDU.cmu.Atlas.owls1_1.process.InputList;
import EDU.cmu.Atlas.owls1_1.process.Output;
import EDU.cmu.Atlas.owls1_1.process.OutputList;
import EDU.cmu.Atlas.owls1_1.process.PreConditionList;
import EDU.cmu.Atlas.owls1_1.process.Result;
import EDU.cmu.Atlas.owls1_1.process.ResultList;
import EDU.cmu.Atlas.owls1_1.profile.Actor;
import EDU.cmu.Atlas.owls1_1.profile.ActorsList;
import EDU.cmu.Atlas.owls1_1.profile.Profile;
import EDU.cmu.Atlas.owls1_1.profile.ProfileList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategoriesList;
import EDU.cmu.Atlas.owls1_1.profile.ServiceCategory;
import EDU.cmu.Atlas.owls1_1.service.ServiceList;
import EDU.cmu.Atlas.owls1_1.writer.OWLSServiceWriter;
import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import nl.wtcw.vle.hammer.util.WebUtil;
import org.mindswap.owl.OWLFactory;
import sun.net.www.protocol.http.AuthCache;

/**
 *
 * @author vguevara
 */
public class HammerEngine {

    public static final int SIMILARITY_KEYWORD = -1; // Purely logic-based semantic matching (not hybrid)
    public static final int SIMILARITY_LOGIC = 0; // Purely logic-based semantic matching (not hybrid)
    public static final int SIMILARITY_LOI = 1; // M0 + Loss-of-information similarity measure
    public static final int SIMILARITY_EXTENDED_JACCARD = 2; // M0 + Extended Jacquard similarity measure
    public static final int SIMILARITY_COSINE = 3; // M0 + Cosine similarity measure
    public static final int SIMILARITY_JENSEN_SHANNON = 4; // M0 + Jensen - Shannon divergence based similarity measure
    //
    public static final int EXACT = 0; // Minimum degree of (hybrid) semantic match required.
    public static final int PLUGIN = 1;
    public static final int SUBSUMES = 2;
    public static final int SUBSUMES_BY = 3;
    public static final int NEAREST_NEIGBOUR = 4;
    // <editor-fold defaultstate="collapsed" desc="Hammer Variables. Click on the + sign on the left to edit the code.">
    private SimilarityMatchingEngine engineOWLSMX;
    //
    private URI queryPath;
    private Integer matchingType;
    private int minDegree;
    private Double treshold;
    //
    private int counter = 0;
    private String serverFolder;
    private String urlServer;
    //
    private Logger logger = Logger.getLogger(HammerEngine.class);
    //
    private Vector<Service> serviceList = new Vector<Service>();
    private Vector<String> serviceListIndex = new Vector<String>();
    public static final String[] ARCH_TYPES = {"tar", "tar.gz", "zip"};

    private void dummy(URI queryPath) {
        logger.info("[1parseURI PATH] " + queryPath.toString());
        Service service = this.parseURI(queryPath.toString());

        logger.info("[2parseURI PATH] " + queryPath.toString());
    }

    private void loadRecursive(File[] files, FileFilter filter) {
        File[] nextLevelFiles;
        for (int i = 0; i < files.length; i++) {

            if (files[i].isFile()) {
                try {
                    logger.debug("[ADDING FILE] " + (i + 1) + "/" + files.length + " " + (files[i].toString()));
                    //Reads the service
                    String filename = files[i].getName();
                    //URI newURI = new URI(urlServer + "/services/1.1/" + filename);
                    URI newURI = new URI(urlServer + "/repository/" + files[i].getParentFile().getName() + "/" + filename);

                    Service service = this.parseURI(newURI.toString());

                    // add new service in the semantic and keywords engine
                    if (service != null) {
                        engineOWLSMX.addService(newURI);
                        serviceList.add(service);
                        serviceListIndex.add(newURI.toString());
                        counter++;
                    }
                } catch (Exception ex) {
                    logger.warn("[FAILED ADDING FILE] " + (i + 1) + "/" + files.length + " " + (files[i].toString()));
                    ex.printStackTrace();
                }
            } else if (files[i].isDirectory()) {

                nextLevelFiles = files[i].listFiles(filter);
                loadRecursive(nextLevelFiles, filter);
            }
        }
    }

    private String getArchURL(String substring) {
        String[] parts = substring.split("/");
        String path = serverFolder + File.separator + parts[parts.length - 2] + "/" + parts[parts.length - 1];

        File arch;
        String ext = "";
        for (int i = 0; i < ARCH_TYPES.length; i++) {
            ext = ARCH_TYPES[i];
            arch = new File(path + "." + ext);
            if (arch.exists()) {
                break;
            }
        }
        return substring + "." + ext;
    }

    private String getComponentAuthor(String substring) {
        String[] parts = substring.split("/");
        return parts[parts.length - 2];
    }

    private String getDateUploaded(String subString) {
        String[] parts = subString.split("/");
        String path = serverFolder + File.separator + parts[parts.length - 2] + "/" + parts[parts.length - 1];

        File arch = null;
        String ext = "";
        for (int i = 0; i < ARCH_TYPES.length; i++) {
            ext = ARCH_TYPES[i];
            arch = new File(path + "." + ext);
            if (arch.exists()) {
                break;
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM-dd");
        String mod = formatter.format(arch.lastModified());
        return mod;
    }

    // </editor-fold>
    private class OwlFileFilter implements java.io.FileFilter {

        public boolean accept(File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".owls");
        }

        public String getDescription() {
            return ".owls files";
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Hammer Engine :)
    ////////////////////////////////////////////////////////////////////////////
    public HammerEngine(String filepath, String webserver) {
        serverFolder = filepath;
        urlServer = webserver;

        if ((urlServer.lastIndexOf("/") + 1) == urlServer.length()) {
            urlServer = urlServer.substring(0, urlServer.lastIndexOf("/"));
        }

        logger.info("[REPOSITORY PATH] " + serverFolder);

        // Sets initial (default) values
        matchingType = HammerEngine.SIMILARITY_LOGIC;
        minDegree = HammerEngine.EXACT;
        treshold = 0.0;

        engineOWLSMX = new SimilarityMatchingEngine();
        loadRepository();
    }

    public int size() {
        return counter;
    }

    public void printRegistry() {
        logger.info("[PRINT REPOSITORY / SIZE] " + serverFolder + " / " + counter);
        for (String elem : serviceListIndex) {
            logger.info("[PRINT REPOSITORY / elem] " + elem);
        }
    }

    private void loadRepository() {
        //Gets the list of files to process
        counter = 0;
        File serviceDirectory = new File(serverFolder);
        OwlFileFilter filter = new OwlFileFilter(); //this line gets the "owl" and "owls" files
        File[] files = serviceDirectory.listFiles((FileFilter) filter);

        loadRecursive(files, (FileFilter) filter);

    }

    ////////////////////////////////////////////////////////////////////////////
    // Publishing operations
    ////////////////////////////////////////////////////////////////////////////
    public void publish(String author, URI toPublishOWL, URI toPublishXML, URI toPublishArch) throws Exception {

        String owlFile = serverFolder + File.separator + author + File.separator
                + toPublishOWL.toString().substring(toPublishOWL.toString().lastIndexOf("/") + 1);
        String xmlFile = serverFolder + File.separator + author + File.separator
                + toPublishXML.toString().substring(toPublishXML.toString().lastIndexOf("/") + 1);
        String archFile = serverFolder + File.separator + author + File.separator
                + toPublishArch.toString().substring(toPublishArch.toString().lastIndexOf("/") + 1);

        if (WebUtil.FileExists(owlFile) || WebUtil.FileExists(owlFile) || WebUtil.FileExists(archFile)) {
            throw new Exception("Advertisement already in the repository");
        }

        publishOWLS(toPublishOWL, author);  //parse the file and save it again
        String newXmlFile = WebUtil.addDateToFileName(xmlFile);
        WebUtil.FileURICopy(toPublishXML, newXmlFile); // copy the file
        String newArchFile = WebUtil.addDateToFileName(archFile);
        WebUtil.FileURICopy(toPublishArch, newArchFile); // copy the file
    }
//
//        public void publish(URI toPublishOWL, URI toPublishXML,URI toPublishArch)
//            throws Exception {
//
//        String owlFile = serverFolder + File.separator +
//                toPublishOWL.toString().substring(toPublishOWL.toString().lastIndexOf("/") + 1);
//        String xmlFile = serverFolder + File.separator +
//                toPublishXML.toString().substring(toPublishXML.toString().lastIndexOf("/") + 1);
//
//        String archFile = serverFolder + File.separator +
//                toPublishArch.toString().substring(toPublishArch.toString().lastIndexOf("/") + 1);
//
//        if (WebUtil.FileExists(owlFile) || WebUtil.FileExists(owlFile) || WebUtil.FileExists(archFile)) {
//            throw new Exception("Advertisement already in the repository");
//        }
//
//        publishOWLS(toPublishOWL);  //parse the file and save it again
//        WebUtil.FileURICopy(toPublishXML, xmlFile); // copy the file
//
//        WebUtil.FileURICopy(toPublishArch, archFile); // copy the file
//    }

    public boolean revoke(URI toRevokeOWL) {
        return revokeOWLS(toRevokeOWL);
    }

    private void publishOWLS(URI owlSService, String author) {
        Service service = null;
        String baseServiceURI = null;

        // read and parse the URI
        service = parseURI(owlSService.toString());
        String filename = getFilename(service);

        try {
            //creates the list of services to write
            ServiceList srvList = createsCopy(service);

            String newFileName = WebUtil.addDateToFileName(filename);

            //creates an output stream to write the ontology to
            FileOutputStream newOutputStream = new FileOutputStream(serverFolder + File.separator + author + File.separator + newFileName);
            //Sets the new baseURI (repository)
            baseServiceURI = urlServer + "/repository/" + author + "/" + newFileName;

            //Writing the OWL-S in server folder
            logger.debug("[SAVE TO FILE] Saving service to " + serverFolder);
            OWLSServiceWriter.write(srvList, baseServiceURI, newOutputStream);

            // reads and parses the new URI
            logger.debug("[READ NEW FILE] From " + serverFolder);
            URI newURI = new URI(baseServiceURI);
            service = this.parseURI(newURI.toString());

            // add new service in the semantic and keywords engine
            engineOWLSMX.addService(newURI);
            serviceListIndex.add(newURI.toString());
            serviceList.add(service);
        } catch (Exception e) {
            logger.error("[ADDING OWLS FILE] URI : " + owlSService.toString());
            e.printStackTrace();
        }
    }

    private boolean revokeOWLS(URI owlSService) {
        boolean erased = false;

        String filename;
        String wsvlamXML;

        //gets base URI
        try {
            String serviceURI = owlSService.toString();
            if (serviceURI.length() > 0) { //it is not empty
                int last_index = serviceURI.lastIndexOf("/");
                filename = serviceURI.substring(last_index);

                if (filename.lastIndexOf(".owls") > 0) {
                    wsvlamXML = filename.substring(0, filename.lastIndexOf(".owls")) + ".xml";
                } else if (filename.lastIndexOf(".owl") > 0) {
                    wsvlamXML = filename.substring(0, filename.lastIndexOf(".owl")) + ".xml";
                } else {
                    wsvlamXML = "";
                }

                //Erases file
                File toDelete = new File(serverFolder + File.separator + filename);
                File toDeleteXML = new File(serverFolder + File.separator + wsvlamXML);

                erased = toDelete.delete();

                //removes from engines
                if (erased) {
                    engineOWLSMX.removeService(owlSService);

                    int index = getIndex(serviceURI);
                    serviceList.removeElementAt(index);
                    serviceListIndex.removeElementAt(index);

                    toDeleteXML.delete();
                }
            }
        } catch (Exception ex) {
            logger.error("[FAILED REVOKE] URL : " + owlSService.toString());
            ex.printStackTrace();
        }

        return erased;
    }

    private int getIndex(String uri) {
        int result = -1;

        for (int i = 0; i < serviceListIndex.size(); i++) {
            if (uri.equals(serviceListIndex.elementAt(i))) {
                return i;
            }
        }

        return result;
    }

    private String getFilename(Service service) {
        String result = "";

        //gets base URI
        String baseServiceURI = service.getURI();

        if (baseServiceURI.length() > 0) { //it is not empty
            int last_index = baseServiceURI.lastIndexOf("/");
            result = baseServiceURI.substring(last_index + 1);

            last_index = result.lastIndexOf("#");
            if (last_index > 0) {
                result = result.substring(0, last_index);
            }
        }

        return result;
    }

    private Service parseURI(String uri) {
        Service service = null;

        //Creating an instance of default error handler.
        DefaultOWLSErrorHandler errHandler = new DefaultOWLSErrorHandler();


        //creating instance of parser
        OWLSServiceParser parser = new OWLSServiceParser();

        //Null????????????
        OWLSServiceModel model = parser.read(uri, errHandler);

        if (model == null) {
            String errors = "";
            Exception[] exceptions = errHandler.getErrors();
            for (Exception e : exceptions) {
                errors += e.getMessage() + "\n";
            }
            throw new NullPointerException("Model for " + uri + " is Null!" + errors);
        }

        //creating the service to return
        if (model.getServiceList().size() > 0) {
            service = model.getServiceList().getNthService(0);
        }

        //Print the warning and errors generated while parsing
        logger.debug(errHandler);

        return service;
    }

    private String getIDName(String uri_source) {
        //This returns only the shortname
        //For example:  http://some.server.out.there/name.of.file#short_name
        //it will return 'short_name' (without the '#')

        String uri = uri_source.replaceAll(" ", "_");

        int last_index = uri.lastIndexOf("#");
        if (last_index > 0) {
            return uri.substring(last_index + 1);
        } else {
            return uri;
        }
    }

    private ServiceList createsCopy(Service source) {
        //Setup of the factories
        OWLS_Object_Builder factory = OWLS_Object_BuilderFactory.instance();
        OWLS_Store_Builder listFactory = OWLS_Store_BuilderFactory.instance();

        // Creates first the profile
        ProfileList sourceProfileList = (ProfileList) source.getPresents();
        Profile sourceProfile = null;
        Profile targetProfile = null;

        //It copies only one profile
        if (sourceProfileList.size() > 0) {
            sourceProfile = sourceProfileList.getNthProfile(0);
        } else {
            return null;
        }

        //Returns empty if there is no profile
        if (sourceProfile == null) {
            return null;
        } else {
            try {
                String idRDF = getIDName(sourceProfile.getURI());
                targetProfile = factory.createProfile(idRDF);
            } catch (NotInstanceOfProfileException ex) {
                logger.error("[ADDING OWLS FILE] ID RDF : " + sourceProfile.getURI());
                ex.printStackTrace();
            }
        }

        if (sourceProfile.getServiceName() != null) {
            targetProfile.setServiceName(sourceProfile.getServiceName());
        }

        if (sourceProfile.getTextDescription() != null) {
            targetProfile.setTextDescription(sourceProfile.getTextDescription());
        }

        if (sourceProfile.getServiceClassifications() != null) {
            Vector serviceClassifications = new Vector(sourceProfile.getServiceClassifications());
            targetProfile.setServiceClassifications(serviceClassifications);
        }

        if (sourceProfile.getServiceProducts() != null) {
            Vector serviceProducts = new Vector(sourceProfile.getServiceProducts());
            targetProfile.setServiceProducts(serviceProducts);
        }

        if (sourceProfile.getContactInformation() != null) {
            ActorsList sourceActorList = sourceProfile.getContactInformation();
            ActorsList targetActorList = listFactory.createActorsList();

            for (int i = 0; i < sourceActorList.size(); i++) {
                try {
                    Actor sourceActor = sourceActorList.getNthActor(i);
                    String idRDF = getIDName(sourceActor.getURI());
                    Actor targetActor = factory.createActor(idRDF);

                    targetActor.setAddress(sourceActor.getAddress());
                    targetActor.setEmail(sourceActor.getEmail());
                    targetActor.setFax(sourceActor.getFax());
                    targetActor.setName(sourceActor.getName());
                    targetActor.setPhone(sourceActor.getPhone());
                    targetActor.setTitle(sourceActor.getTitle());
                    targetActor.setWebURL(sourceActor.getWebURL());

                    targetActorList.addActor(targetActor);
                } catch (NotInstanceOfActorException ex) {
                    logger.error("[ADDING OWLS FILE] ID RDF : " + sourceProfile.getURI());
                    ex.printStackTrace();
                }
            }

            targetProfile.setContactInformation(targetActorList);
        }

        if (sourceProfile.getServiceCategory() != null) {
            ServiceCategoriesList sourceCategoryList = sourceProfile.getServiceCategory();
            ServiceCategoriesList targetCategoryList = listFactory.createServiceCategoriesList();

            for (int i = 0; i < sourceCategoryList.size(); i++) {
                try {
                    ServiceCategory sourceCategory = sourceCategoryList.getServiceCategoryAt(i);
                    String idRDF = getIDName(sourceCategory.getURI());
                    ServiceCategory targetCategory = factory.createServiceCategory(idRDF);

                    targetCategory.setCategoryName(sourceCategory.getCategoryName());
                    targetCategory.setCode(sourceCategory.getCode());
                    targetCategory.setTaxonomy(sourceCategory.getTaxonomy());
                    targetCategory.setValue(sourceCategory.getValue());

                    targetCategoryList.addServiceCategory(targetCategory);
                } catch (NotInstanceOfServiceCategoryException ex) {
                    logger.error("[ADDING OWLS FILE] ID RDF : " + sourceProfile.getURI());
                    ex.printStackTrace();
                }
            }

            targetProfile.setServiceCategory(targetCategoryList);
        }

        InputList hasInputList = null;
        if (sourceProfile.getInputList() != null) {
            InputList sourceInputList = sourceProfile.getInputList();
            InputList targetInputList = listFactory.createInputList();

            for (int i = 0; i < sourceInputList.size(); i++) {
                try {
                    Input sourceInput = sourceInputList.getNthInput(i);
                    String idRDF = getIDName(sourceInput.getURI());
                    Input targetInput = (Input) factory.createParameter(idRDF, OWLS_Builder_Util.INPUT);

                    targetInput.setParameterType(sourceInput.getParameterType());
                    targetInput.setParameterValue(sourceInput.getParameterValue());

                    targetInputList.addInput(targetInput);
                } catch (NotInstanceOfParameterException ex) {
                    logger.error("[ADDING OWLS FILE] ID RDF : " + sourceProfile.getURI());
                    ex.printStackTrace();
                }
            }

            targetProfile.setInputList(targetInputList);
            hasInputList = targetInputList;
        }

        OutputList hasOutputList = null;
        if (sourceProfile.getOutputList() != null) {
            OutputList sourceOutputList = sourceProfile.getOutputList();
            OutputList targetOutputList = listFactory.createOutputList();

            for (int i = 0; i < sourceOutputList.size(); i++) {
                try {
                    Output sourceOutput = sourceOutputList.getNthOutput(i);
                    String idRDF = getIDName(sourceOutput.getURI());
                    Output targetOutput = (Output) factory.createParameter(idRDF, OWLS_Builder_Util.OUTPUT);

                    targetOutput.setParameterType(sourceOutput.getParameterType());
                    targetOutput.setParameterValue(sourceOutput.getParameterValue());

                    targetOutputList.addOutput(targetOutput);
                } catch (NotInstanceOfParameterException ex) {
                    logger.error("[ADDING OWLS FILE] ID RDF : " + sourceProfile.getURI());
                    ex.printStackTrace();
                }

            }

            targetProfile.setOutputList(targetOutputList);
            hasOutputList =
                    targetOutputList;
        }

        if (sourceProfile.getPreconditionList() != null) {
            PreConditionList sourcePreconditionList = sourceProfile.getPreconditionList();
            PreConditionList targetPreconditionList = listFactory.createPreConditionList();

            for (int i = 0; i < sourcePreconditionList.size(); i++) {
                try {
                    Condition sourcePrecondition = sourcePreconditionList.getNthPreCondition(i);
                    String idRDF = getIDName(sourcePrecondition.getURI());
                    Condition targetPrecondition = factory.createCondition(idRDF);

                    targetPrecondition.setExpressionBody(sourcePrecondition.getExpressionBody());
                    targetPrecondition.setExpressionLanguage(sourcePrecondition.getExpressionLanguage());

                    targetPreconditionList.addPreCondition(targetPrecondition);
                } catch (NotInstanceOfExpressionException ex) {
                    logger.error("[ADDING OWLS FILE] ID RDF : " + sourceProfile.getURI());
                    ex.printStackTrace();
                }

            }

            targetProfile.setPreconditionList(targetPreconditionList);
        }

        if (sourceProfile.getResultList() != null) {
            ResultList sourceResultList = sourceProfile.getResultList();
            ResultList targetResultList = listFactory.createResultList();

            for (int i = 0; i < sourceResultList.size(); i++) {
                try {
                    Result sourceResult = sourceResultList.getNthResult(i);
                    String idRDF = getIDName(sourceResult.getURI());
                    Result targetResult = factory.createResult(idRDF);

                    //Please note that this is not DEEP COPY!!!! T.B.M.
                    targetResult.setHasEffects(sourceResult.getHasEffects());
                    targetResult.setInCondition(sourceResult.getInCondition());
                    targetResult.setResultVarList(sourceResult.getResultVarList());
                    targetResult.setWithOutputs(sourceResult.getWithOutputs());

                    targetResultList.addResult(targetResult);
                } catch (NotInstanceOfResultException ex) {
                    logger.error("[ADDING OWLS FILE] ID RDF : " + sourceProfile.getURI());
                    ex.printStackTrace();
                }

            }

            targetProfile.setResultList(targetResultList);
        }

        if (sourceProfile.getHasProcess() != null) {
            try {
                Process sourceHasProcess = sourceProfile.getHasProcess();
                String idRDF = getIDName(sourceHasProcess.getURI());
                //Process targetHasProcess = factory.createProcess(idRDF);

                if (sourceHasProcess.isAtomic()) {
                    AtomicProcess targetHasProcess = (AtomicProcess) factory.createProcess(idRDF, OWLS_Builder_Util.ATOMIC);

                    // Makes a proccess out of the Profile information
                    if (sourceHasProcess.getName() != null) {
                        targetHasProcess.setName(idRDF);
                    }

                    if (hasInputList != null) {
                        targetHasProcess.setInputList(hasInputList);
                    }

                    if (hasOutputList != null) {
                        targetHasProcess.setOutputList(hasOutputList);
                    }

                    targetProfile.setHasProcess(targetHasProcess);

//                } else if (sourceHasProcess.isComposite()) {
//                    CompositeProcess targetHasProcess = (CompositeProcess) factory.createProcess(idRDF, OWLS_Builder_Util.COMPOSITE);
//                } else if (sourceHasProcess.isSimple()) {
//                    SimpleProcess targetHasProcess = (SimpleProcess) factory.createProcess(idRDF, OWLS_Builder_Util.SIMPLE);
                }

            } catch (NotInstanceOfProcessException ex) {
                logger.error("[ADDING OWLS FILE] ID RDF : " + sourceProfile.getURI());
                ex.printStackTrace();
            }

        }

        ProfileList targetProfileList = listFactory.createProfileList();
        targetProfileList.addProfile(targetProfile);

        //Creating Service object
        Service target = null;
        try {
            String idRDF = getIDName(source.getURI());
            target = factory.createService(idRDF);
            target.setPresents(targetProfileList);

            if (targetProfile.getHasProcess() != null) {
                target.setDescribes(targetProfile.getHasProcess());
            }

        } catch (NotInstanceOfServiceException e) {
            logger.error("[ADDING OWLS FILE] ID RDF : " + sourceProfile.getURI());
            e.printStackTrace();
            return null;
        }

        //Creating a service list and adding the service
        ServiceList serviceListToReturn = listFactory.createServiceList();
        serviceListToReturn.addService(target);

        return serviceListToReturn;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Discovery operations
    ////////////////////////////////////////////////////////////////////////////
    public void setQuery(URI queryProfile) {
        queryPath = queryProfile;
    }

    public URI getQuery() {
        return queryPath;
    }

    public Vector<MatchResult> executeQuery() {
        Vector<MatchResult> matchResults = new Vector<MatchResult>();

        if (this.getSimilarityMeasure() == HammerEngine.SIMILARITY_KEYWORD) {
            logger.debug("[QUERY] KEYWORDS");

            String[] keywords = getKeywords();

            if (keywords == null) {
                matchResults = null;
            } else if ((keywords.length == 1) && (keywords[0].trim().equals(""))) {
                matchResults = null;
            } else {
                logger.debug("[QUERY] (keywords): (" + keywords + ")");
                for (Service elem : serviceList) {

                    MatchResult mr = getServiceMatch(elem, keywords);

                    if (mr != null) {
                        matchResults.addElement(mr);
                    }
                }

                Collections.sort(matchResults, Collections.reverseOrder());
            }

        } else {
            try {
                logger.debug("[QUERY] Hybrid");
                logger.debug("[QUERY] (MinDegree/Treshold/Query): (" + getMinDegree() + "/" + getTreshold() + "/" + queryPath.toString() + ")");

                URI query = queryPath;
                //dummy(queryPath);

                if (query != null) {
                    SortedSet result = engineOWLSMX.matchRequest(query, getMinDegree(), getTreshold());

                    matchResults = processSemanticResult(result);
                }

            } catch (MatchingException ex) {
                logger.error("[EXECUTING HYBRID QUERY] (MinDegree/Treshold/Query): (" + getMinDegree() + "/" + getTreshold() + "/" + queryPath.toString() + ")");
                ex.printStackTrace();
            }
        }

        return matchResults;
    }

    public void setSimilarityMeasure(int measure) {
        switch (measure) {
            case HammerEngine.SIMILARITY_LOI:
                matchingType = HammerEngine.SIMILARITY_LOI;
                engineOWLSMX.setSimilarityMeasure(SimilarityMeasure.SIMILARITY_LOI);
                break;
            case HammerEngine.SIMILARITY_EXTENDED_JACCARD:
                matchingType = HammerEngine.SIMILARITY_EXTENDED_JACCARD;
                engineOWLSMX.setSimilarityMeasure(SimilarityMeasure.SIMILARITY_EXTENDED_JACCARD);
                break;
            case HammerEngine.SIMILARITY_COSINE:
                matchingType = HammerEngine.SIMILARITY_COSINE;
                engineOWLSMX.setSimilarityMeasure(SimilarityMeasure.SIMILARITY_COSINE);
                break;
            case HammerEngine.SIMILARITY_JENSEN_SHANNON:
                matchingType = HammerEngine.SIMILARITY_JENSEN_SHANNON;
                engineOWLSMX.setSimilarityMeasure(SimilarityMeasure.SIMILARITY_JENSEN_SHANNON);
                break;
            case HammerEngine.SIMILARITY_LOGIC:
                matchingType = HammerEngine.SIMILARITY_LOGIC;
                engineOWLSMX.setSimilarityMeasure(SimilarityMeasure.SIMILARITY_NONE);
                break;
            default:  // HammerEngine.SIMILARITY_KEYWORD
                matchingType = HammerEngine.SIMILARITY_KEYWORD;
                engineOWLSMX.setSimilarityMeasure(SimilarityMeasure.SIMILARITY_NONE);
                break;
        }
    }

    public int getSimilarityMeasure() {
        return this.matchingType;
    }

    public void setMinDegree(int degree) {
        switch (degree) {
            case HammerEngine.EXACT:
                this.minDegree = HammerEngine.EXACT;
                break;
            case HammerEngine.PLUGIN:
                this.minDegree = HammerEngine.PLUGIN;
                break;
            case HammerEngine.SUBSUMES:
                this.minDegree = HammerEngine.SUBSUMES;
                break;
            case HammerEngine.SUBSUMES_BY:
                this.minDegree = HammerEngine.SUBSUMES_BY;
                break;
            case HammerEngine.NEAREST_NEIGBOUR:
                this.minDegree = HammerEngine.NEAREST_NEIGBOUR;
                break;
            default:  // This is an error
                this.minDegree = -1;
        }
    }

    public int getMinDegree() {
        return this.minDegree;
    }

    public void setTreshold(int minTreshold) {
        treshold = (minTreshold / 100.0);
    }

    public int getTreshold() {
        long value = new Long(Math.round(treshold * 100));
        return (int) value;
    }

////////////////////////////////////////////////////////////////////////////
// Supporting query operations
////////////////////////////////////////////////////////////////////////////
    private MatchResult getServiceMatch(Service service, String[] keywords) {
        // Comprares service with keywords
        // -- returns null if there is no comparison

        MatchResult result = null;

        Profile profile = null;
        ProfileList profileList = (ProfileList) service.getPresents();

        String baseURI = service.getURI();

        if (profileList.size() > 0) {
            profile = profileList.getNthProfile(0);
        }

        // if profile is null or query is empty, return <null>
        if ((profile == null) || (keywords.length == 0)) {
            return null;
        }

        result = new MatchResult();
        result.setSimilarity(0.0);

        // Read Description
        if (profile.getTextDescription() != null) {
            String textDescription = profile.getTextDescription();

            for (int i = 0; i
                    < keywords.length; i++) {
                if (textDescription.toLowerCase().indexOf(keywords[i].toLowerCase()) != -1) {
                    result.setSimilarity(result.getSimilarity() + 1);
                }

            }
        }

        // Read ContactInformation
        ActorsList actorList = profile.getContactInformation();
        if (actorList != null) {
            for (int i = 0; i < actorList.size(); i++) {
                Actor actor = actorList.getNthActor(i);

                String actorTitle = actor.getTitle();
                String actorName = actor.getName();
                String actorAddress = actor.getAddress();
                String actorEmail = actor.getEmail();
                String actorPhone = actor.getPhone();
                String actorFax = actor.getFax();
                String actorWebURL = actor.getWebURL();

                for (int j = 0; j < keywords.length; j++) {
                    if (actorTitle.toLowerCase().indexOf(keywords[j].toLowerCase()) != -1) {
                        result.setSimilarity(result.getSimilarity() + 1);
                    }

                    if (actorName.toLowerCase().indexOf(keywords[j].toLowerCase()) != -1) {
                        result.setSimilarity(result.getSimilarity() + 1);
                    }

                    if (actorAddress.toLowerCase().indexOf(keywords[j].toLowerCase()) != -1) {
                        result.setSimilarity(result.getSimilarity() + 1);
                    }

                    if (actorEmail.toLowerCase().indexOf(keywords[j].toLowerCase()) != -1) {
                        result.setSimilarity(result.getSimilarity() + 1);
                    }

                    if (actorPhone.toLowerCase().indexOf(keywords[j].toLowerCase()) != -1) {
                        result.setSimilarity(result.getSimilarity() + 1);
                    }

                    if (actorFax.toLowerCase().indexOf(keywords[j].toLowerCase()) != -1) {
                        result.setSimilarity(result.getSimilarity() + 1);
                    }

                    if (actorWebURL.toLowerCase().indexOf(keywords[j].toLowerCase()) != -1) {
                        result.setSimilarity(result.getSimilarity() + 1);
                    }
                }
            }
        }

        // Read categories
        ServiceCategoriesList categories = profile.getServiceCategory();
        if (categories != null) {
            for (int i = 0; i < categories.size(); i++) {

                ServiceCategory category = categories.getServiceCategoryAt(i);

                if (category.getCategoryName() != null) {
                    String categoryName = category.getCategoryName();
                    String categoryTaxonomy = category.getTaxonomy();
                    String categoryCode = category.getCode();
                    String categoryValue = category.getValue();

                    for (int j = 0; j < keywords.length; j++) {
                        if (categoryName.toLowerCase().indexOf(keywords[j].toLowerCase()) != -1) {
                            result.setSimilarity(result.getSimilarity() + 1);
                        }

                        if (categoryTaxonomy.toLowerCase().indexOf(keywords[j].toLowerCase()) != -1) {
                            result.setSimilarity(result.getSimilarity() + 1);
                        }

                        if (categoryCode.toLowerCase().indexOf(keywords[j].toLowerCase()) != -1) {
                            result.setSimilarity(result.getSimilarity() + 1);
                        }

                        if (categoryValue.toLowerCase().indexOf(keywords[j].toLowerCase()) != -1) {
                            result.setSimilarity(result.getSimilarity() + 1);
                        }
                    }
                }
            }
        }

        // Read Inputs
        InputList inputList = profile.getInputList();
        for (int i = 0; i < inputList.size(); i++) {
            Input input = inputList.getNthInput(i);

            String type = input.getParameterType();
            String value = input.getParameterValue();
            String uri = input.getURI();

            for (int j = 0; j < keywords.length; j++) {
                if (type.toLowerCase().indexOf(keywords[j].toLowerCase()) != -1) {
                    result.setSimilarity(result.getSimilarity() + 1);
                }

                if (value.toLowerCase().indexOf(keywords[j].toLowerCase()) != -1) {
                    result.setSimilarity(result.getSimilarity() + 1);
                }

                if (uri.toLowerCase().indexOf(keywords[j].toLowerCase()) != -1) {
                    result.setSimilarity(result.getSimilarity() + 1);
                }
            }
        }

        // Read Outputs
        OutputList outputList = profile.getOutputList();
        for (int i = 0; i < outputList.size(); i++) {
            Output output = outputList.getNthOutput(i);

            String type = output.getParameterType();
            String value = output.getParameterValue();
            String uri = output.getURI();

            for (int j = 0; j < keywords.length; j++) {
                if (type.toLowerCase().indexOf(keywords[j].toLowerCase()) != -1) {
                    result.setSimilarity(result.getSimilarity() + 1);
                }

                if (value.toLowerCase().indexOf(keywords[j].toLowerCase()) != -1) {
                    result.setSimilarity(result.getSimilarity() + 1);
                }

                if (uri.toLowerCase().indexOf(keywords[j].toLowerCase()) != -1) {
                    result.setSimilarity(result.getSimilarity() + 1);
                }
            }
        }

        if (profile.getServiceName() != null) {
            String serviceName = profile.getServiceName();
            result.setName(serviceName);

            for (int i = 0; i < keywords.length; i++) {
                if (serviceName.toLowerCase().indexOf(keywords[i].toLowerCase()) != -1) {
                    result.setSimilarity(result.getSimilarity() + 1);
                }
            }
        }

        result.setDegreeOfMatch("KEYWORD");
        result.setDescription(getServiceDescription(service));
        result.setOWL(baseURI);

        String archURL = "";
        String author = "";
        String date = "";
        String subString = "";
        if (baseURI.lastIndexOf(".owls") > 0) {
            subString = baseURI.substring(0, baseURI.lastIndexOf(".owls"));

            archURL = getArchURL(subString);
            author = getComponentAuthor(subString);
            date = getDateUploaded(subString);

        } else if (baseURI.lastIndexOf(".owl") > 0) {
            subString = baseURI.substring(0, baseURI.lastIndexOf(".owl"));
            archURL = getArchURL(subString);
            author = getComponentAuthor(subString);
            date = getDateUploaded(subString);
        } else {
            subString = "";
        }
        result.setXML(subString + ".xml");
        result.setArch(archURL);
        result.setAuthor(author);


        result.setDateUploaded(date);


        if (result.getSimilarity() == 0.0) {
            return null;
        } else {
            return result;
        }

    }

    private Vector<MatchResult> processSemanticResult(SortedSet result) {
        Vector<MatchResult> toReturn = new Vector<MatchResult>();

        if (result != null) {
            Iterator iter = result.iterator();
            MatchedService response;

            while (iter.hasNext()) {
                response = (MatchedService) iter.next();
                String baseURI = response.serviceURI.toString();

                MatchResult toAdd = new MatchResult();

                Service service = serviceList.elementAt(this.getIndex(baseURI));

                toAdd.setName(this.getServiceName(service));
                toAdd.setSimilarity(response.similarity);
                toAdd.setDescription(this.getServiceDescription(service));
                toAdd.setDegreeOfMatch(MatchmakerUtils.degreeOfMatchIntToString(response.degreeOfMatch));
                toAdd.setOWL(baseURI);
                if (baseURI.lastIndexOf(".owls") > 0) {
                    toAdd.setXML(baseURI.substring(0, baseURI.lastIndexOf(".owls")) + ".xml");
                } else if (baseURI.lastIndexOf(".owl") > 0) {
                    toAdd.setXML(baseURI.substring(0, baseURI.lastIndexOf(".owl")) + ".xml");
                } else {
                    toAdd.setXML("");
                }

                toReturn.addElement(toAdd);
            }

            Collections.sort(toReturn, Collections.reverseOrder());
        }

        return toReturn;
    }

    private String getServiceName(Service service) {
        String toReturn = "<unknown>";

        Profile profile = null;
        ProfileList profileList = (ProfileList) service.getPresents();

        if (profileList.size() > 0) {
            profile = profileList.getNthProfile(0);

            if (profile.getServiceName() != null) {
                toReturn = profile.getServiceName();
            }
        }

        return toReturn;
    }

    private String getServiceDescription(Service service) {
        String toReturn = "<empty>";

        Profile profile = null;
        ProfileList profileList = (ProfileList) service.getPresents();

        if (profileList.size() > 0) {
            profile = profileList.getNthProfile(0);
            toReturn = "";
        }

        if (profile == null) {
            return null;
        }

        if (profile.getServiceName() != null) {
            String serviceName = profile.getServiceName();
            toReturn = toReturn + serviceName;
        }

        if (profile.getTextDescription() != null) {
            String textDescription = profile.getTextDescription();

            if (textDescription.length() < 50) {
                toReturn = toReturn + " (" + textDescription + ")";
            } else {
                toReturn = toReturn + " (" + textDescription.substring(0, 46) + "...)";
            }
        }

        // Read ContactInformation
        ActorsList actorList = profile.getContactInformation();
        if (actorList != null) {
            for (int i = 0; i < actorList.size(); i++) {
                String actorName = actorList.getNthActor(i).getName();
                toReturn = toReturn + " (Contact: " + actorName + ")";
            }
        }

        // Read categories
        ServiceCategoriesList categories = profile.getServiceCategory();
        if (categories != null) {
            for (int i = 0; i < categories.size(); i++) {

                ServiceCategory category = categories.getServiceCategoryAt(i);

                if (category.getCategoryName() != null) {
                    String categoryName = category.getCategoryName();

                    if (i == 0) {
                        toReturn = toReturn + " (" + categoryName;
                    } else {
                        toReturn = toReturn + "," + categoryName;
                    }

                    if (i == categories.size() - 1) {
                        toReturn = toReturn + ")";
                    }
                }
            }
        }

        // Read Inputs
        InputList inputList = profile.getInputList();
        for (int i = 0; i < inputList.size(); i++) {
            Input input = inputList.getNthInput(i);

            String value = input.getParameterValue();

            if (i == 0) {
                toReturn = toReturn + " {in: " + value.trim();
            } else {
                toReturn = toReturn + "," + value.trim();
            }

            if (i == inputList.size() - 1) {
                toReturn = toReturn + "}";
            }
        }

        // Read Outputs
        OutputList outputList = profile.getOutputList();
        for (int i = 0; i < outputList.size(); i++) {
            Output output = outputList.getNthOutput(i);

            String value = output.getParameterValue();

            if (i == 0) {
                toReturn = toReturn + " {out: " + value.trim();
            } else {
                toReturn = toReturn + "," + value.trim();
            }

            if (i == outputList.size() - 1) {
                toReturn = toReturn + "}";
            }
        }

        return toReturn;
    }

    private String[] getKeywords() {
        String[] result;

        String query = getKeywordsFromURL();
        query =
                WebUtil.supertrim(query);

        //gets keywords
        result =
                query.split(" ");

        return result;
    }

    private String getKeywordsFromURL() {
        String result = null;

        try {
            result = "";
            BufferedReader reader = WebUtil.URIBufferedReader(queryPath);

            String strLine;
            //Read file Line-By-Line
            while ((strLine = reader.readLine()) != null) {
                // this statement reads the line from the file and 
                // adds it to the result to return
                result = result + " " + strLine.trim();
            }

            // dispose all the resources after using them.
            reader.close();
        } catch (Exception ex) {
            logger.error("[READING QUERY FILE] (Query path): (" + queryPath + ")");
            ex.printStackTrace();
        }

        return result;
    }

    private URI preProcessQuery() {
        URI toReturn = null;

        try {
            // read and parse the URI
            Service service = parseURI(queryPath.toString());
            String filename = getFilename(service);

            //creates the list of services to write and the index
            ServiceList srvList = createsCopy(service);
            String index = WebUtil.Index();

            //creates an output stream to write the ontology to
            FileOutputStream newOutputStream = new FileOutputStream(serverFolder + File.separator + index + File.separator + filename);
            //Sets the new baseURI (repository)
            String baseServiceURI = urlServer + "/repository/temp/" + index + "/" + filename;

            //Writing the OWL-S in server folder
            logger.debug("[QUERY PREPROCESS] Set query to " + serverFolder + File.separator + index + File.separator + filename);
            OWLSServiceWriter.write(srvList, baseServiceURI, newOutputStream);

            // reads and parses the new URI
            logger.debug("QUERY PREPROCESS] Read URI from " + baseServiceURI);
            toReturn = new URI(baseServiceURI);
//            service = this.parseURI(newURI.toString());
//
//            // add new service in the semantic and keywords engine
//            engineOWLSMX.addService(newURI);
//            serviceListIndex.add(newURI.toString());
//            serviceList.add(service);
        } catch (Exception e) {
            logger.error("[QUERY PREPROCESS] Error processing query: " + queryPath.toString());
            e.printStackTrace();
        }

        return toReturn;
    }

////////////////////////////////////////////////////////////////////////////
// Test operations
////////////////////////////////////////////////////////////////////////////
    private static void printResult(Vector<MatchResult> result, int value) {

        if (result != null) {
            System.out.println("printResult: Result has found [" + result.size() + " out of " + value + "] responses.");

            for (MatchResult elem : result) {
                System.out.println(elem.toString());
                System.out.println(elem.getDescription());
            }

        } else {
            System.out.println("printResult: Result was null!!!!");
        }
    }

    public static void main(String[] args) {
        // VGM: I use this only for testing... nothing more.
        try {
            String repository;
            //repository = "D:\\Programs\\Apache\\Tomcat-6.0.14\\webapps\\ROOT\\repository";
            //repository = "D:\\Programs\\Apache\\Tomcat-6.0.14\\webapps\\ROOT\\services\\1.1\\\repOWL";
            //repository = "D:\\Programs\\Apache\\Tomcat-6.0.14\\webapps\\ROOT\\services\\1.1\\repOWL";
            repository = "D:\\development\\netbeans_projects\\ws-hammer\\build\\web\\repository";
            //repository = "D:\\development\\owlsmx_1_1b\\testfiles\\owls-tc2\\services\\1.1";

            String server = "http://localhost:8084/ws-hammer";

            HammerEngine engine = new HammerEngine(repository, server);
            int size = engine.size();

            Vector<MatchResult> rs;

            //URI query = new URI("http://pc-vlab15.science.uva.nl:8084/ws-hammer/repository/temp/1206030324587.tmp");
            URI query = new URI("http://pc-vlab15.science.uva.nl:8084/ws-hammer/repository/temp/1206032067930.tmp");

//URI query = new URI("http://pc-vlab19.science.uva.nl:8081/ws-vlam/repository/omnimatch.owls");
//URI query = new URI("http://127.0.0.1/repository/omnimatch.owls");
//URI query = new URI("http://127.0.0.1/queries/1.1/1personbicyclecar_price_service.owls");
//URI query = new URI("http://127.0.0.1/queries/1.1/book_price_service.owls");
//            URI query = new URI("http://127.0.0.1/queries/1.1/BookNonMedicalTransport_service.owls");

            engine.logger.info("[QUERY] : " + query.toString());
            engine.setQuery(query);

            engine.setTreshold(20);

            engine.logger.info("[BASE]");
            org.mindswap.owl.OWLKnowledgeBase base = OWLFactory.createKB();
            engine.logger.info("[ONTO]");
            org.mindswap.owl.OWLOntology onto = base.read(query);
            engine.logger.info("[SERVICE]");
            org.mindswap.owls.service.Service service = onto.getService();

            engine.logger.info("[DONE]");

            //Vector inputurilist=MatchmakerUtils.getURIList(service.getProfile().getInputs());


//
//            ////////////////////////////////////////////////////////////
//
//            engine.logger.info("[SIMILARITY] : SIMILARITY_LOGIC");
//            engine.setSimilarityMeasure(HammerEngine.SIMILARITY_LOGIC);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs = engine.executeQuery();
//
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            ////////////////////////////////////////////////////////////
//
//            engine.logger.info("[SIMILARITY] : SIMILARITY_LOI");
//            engine.setSimilarityMeasure(HammerEngine.SIMILARITY_LOI);
//
//            engine.logger.info("[MIN_DEGREE] : EXACT");
//            engine.setMinDegree(HammerEngine.EXACT);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            engine.logger.info("[MIN_DEGREE] : PLUGIN");
//            engine.setMinDegree(HammerEngine.PLUGIN);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            engine.logger.info("[MIN_DEGREE] : SUBSUMES");
//            engine.setMinDegree(HammerEngine.SUBSUMES);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            engine.logger.info("[MIN_DEGREE] : SUBSUMES_BY");
//            engine.setMinDegree(HammerEngine.SUBSUMES_BY);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            engine.logger.info("[MIN_DEGREE] : NEAREST_NEIGBOUR");
//            engine.setMinDegree(HammerEngine.NEAREST_NEIGBOUR);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            ////////////////////////////////////////////////////////////
//
//            engine.logger.info("[SIMILARITY] : SIMILARITY_COSINE");
//            engine.setSimilarityMeasure(HammerEngine.SIMILARITY_COSINE);
//
//            engine.logger.info("[MIN_DEGREE] : EXACT");
//            engine.setMinDegree(HammerEngine.EXACT);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            engine.logger.info("[MIN_DEGREE] : PLUGIN");
//            engine.setMinDegree(HammerEngine.PLUGIN);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            engine.logger.info("[MIN_DEGREE] : SUBSUMES");
//            engine.setMinDegree(HammerEngine.SUBSUMES);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            engine.logger.info("[MIN_DEGREE] : SUBSUMES_BY");
//            engine.setMinDegree(HammerEngine.SUBSUMES_BY);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            engine.logger.info("[MIN_DEGREE] : NEAREST_NEIGBOUR");
//            engine.setMinDegree(HammerEngine.NEAREST_NEIGBOUR);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            ////////////////////////////////////////////////////////////
//
//            engine.logger.info("[SIMILARITY] : SIMILARITY_EXTENDED_JACCARD");
//            engine.setSimilarityMeasure(HammerEngine.SIMILARITY_EXTENDED_JACCARD);
//
//            engine.logger.info("[MIN_DEGREE] : EXACT");
//            engine.setMinDegree(HammerEngine.EXACT);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            engine.logger.info("[MIN_DEGREE] : PLUGIN");
//            engine.setMinDegree(HammerEngine.PLUGIN);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            engine.logger.info("[MIN_DEGREE] : SUBSUMES");
//            engine.setMinDegree(HammerEngine.SUBSUMES);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            engine.logger.info("[MIN_DEGREE] : SUBSUMES_BY");
//            engine.setMinDegree(HammerEngine.SUBSUMES_BY);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            engine.logger.info("[MIN_DEGREE] : NEAREST_NEIGBOUR");
//            engine.setMinDegree(HammerEngine.NEAREST_NEIGBOUR);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            ////////////////////////////////////////////////////////////
//
//            engine.logger.info("[SIMILARITY] : SIMILARITY_JENSEN_SHANNON");
//            engine.setSimilarityMeasure(HammerEngine.SIMILARITY_JENSEN_SHANNON);
//
//            engine.logger.info("[MIN_DEGREE] : EXACT");
//            engine.setMinDegree(HammerEngine.EXACT);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            engine.logger.info("[MIN_DEGREE] : PLUGIN");
//            engine.setMinDegree(HammerEngine.PLUGIN);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            engine.logger.info("[MIN_DEGREE] : SUBSUMES");
//            engine.setMinDegree(HammerEngine.SUBSUMES);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            engine.logger.info("[MIN_DEGREE] : SUBSUMES_BY");
//            engine.setMinDegree(HammerEngine.SUBSUMES_BY);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");
//
//            engine.logger.info("[MIN_DEGREE] : NEAREST_NEIGBOUR");
//            engine.setMinDegree(HammerEngine.NEAREST_NEIGBOUR);
//
//            engine.logger.info("[BEFORE EXECUTION]");
//            rs =
//                    engine.executeQuery();
//            engine.logger.info("[AFTER EXECUTION]");
//            printResult(rs, size);
//            engine.logger.info("[END OF RESULTS]");

            ////////////////////////////////////////////////////////////
            engine.printRegistry();

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
