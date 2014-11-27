/*
* WS-VLAM HAMMER
*
* This file was made by vguevara/UvA based on the OWL-S/UDDI Matchmaker Client, 
* which is Copyright (C) 2005 Katia Sycara, Softagents Lab, Carnegie Mellon University
* 
* This library is free software; you can redistribute it and/or modify it under the terms of the
* GNU Lesser General Public License as published by the Free Software Foundation; either version
* 2.1 of the License, or (at your option) any later version.
* 
* This library is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without
* even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
* 
* You should have received a copy of the GNU Lesser General Public License along with this library;
* if not, write to the
* 
* Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
* 
* The Intelligent Software Agents Lab The Robotics Institute Carnegie Mellon University 5000 Forbes
* Avenue Pittsburgh PA 15213
* 
* More information available at http://www.cs.cmu.edu/~softagents/
*/

function init(){
    resize(true)
}

function clear_all(){
    document.form.matchingType.value = "M0";
    document.form.minDegree.value = "Exact";
    document.form.threshold.value = "0";
    A_SLIDERS[0].f_setValue(0);

    document.form.serviceName.value = "";
    document.form.textDescription.value = "";

    while(contact_count != 0 ){
        contact_del();		
    }
    while(srvcat_count != 0){
        srvcat_del();
    }
    while(in_count != 0 ){
        in_del();		
    }
    while(out_count != 0 ){
        out_del();		
    }
    while(srvparam_count != 0){
        srvparam_del();
    }
    while(import_count != 0 ){
        import_del();		
    }

    resize(true)
}

function show_sample(){
    
    //Check if the forms have pre-filled value, if so delete them before 
    //filling up with sample values
//    while(in_count != 0 ){
//        in_del();		
//    }
//    while(out_count != 0 ){
//        out_del();		
//    }
//    while(contact_count != 0 ){
//        contact_del();		
//    }
//    while(srvcat_count != 0){
//        srvcat_del();
//    }
//    while(import_count != 0 ){
//        import_del();		
//    }

    clear_all();
    
    import_add();
    in_add();in_add();in_add();
    out_add();	
    contact_add();	
    srvcat_add();srvcat_add();
    
    resize(true);
    preset_t(true);
}

function show(){
    
    if( validate() == false)
        return;
    
    owls = generate();
    
    if(owls == ""){
        alert("Please fill out at least serviceName.");
        return;
    }
    
    nw = window.open("", "", "location=no,menubar=no,resizable=yes,scrollbars=yes");
    nw.document.write("<HTML>");
    nw.document.write("<HEAD><TITLE>WS-HAMMER: Generated OWL-S Profile</TITLE></HEAD>");
    nw.document.write("<BODY><xmp>" + owls + "</xmp></BODY>");
    nw.document.write("</HTML>");
    return;
}

function set_hidden(flag){
    
    if( validate() == false){	
        return false;
    } 
    
    if(flag == true){
        document.getElementById("h_form_action").value = "advertise";
        //maybe we can publish without using WS-SAW
    }
    else{
        document.getElementById("h_form_action").value = "query";
        //VGM uddi = generate_uddi_query();
        //VGM document.getElementById("h_form_hidden").value = uddi;	
        owls = generate_owls_query();
        document.getElementById("h_form_hidden").value = owls;	
        document.getElementById("h_form_matchingType").value = document.getElementById("shown_matchingType").value;
        document.getElementById("h_form_minDegree").value    = document.getElementById("shown_minDegree").value;
        document.getElementById("h_form_threshold").value    = document.getElementById("shown_threshold").value;
    }
    
    return true;
}

function validate(){
    
    if (contacts_validate() == false)
        return false;
    
    if (import_validate() == false)
        return false;
    
    if (in_validate() == false)
        return false;
    
    if (out_validate() == false)
        return false;
    
    if (srvcat_validate() == false)
        return false;
    
    if (srvparam_validate() == false)
        return false;
    
    return true;
}

function view(url){
    if(url != ""){
        url = hyperdaml + url;
        window.open(url,"","");
    }
}

function resize(templete){
    if(templete){
        
        //width = parseInt(document.body.clientWidth*0.9/10);
        //document.form.description.cols = width;
    }
    else{
        
        width = parseInt(document.body.clientWidth*0.9/10);
        document.form.comment.cols = width;
        
        width = parseInt(document.body.clientWidth/10);
        document.form.geographic.size = width;
        
        in_resize(width);                                        
        out_resize(width);                                           
        eff_resize(width);
        pre_resize(width);
    }
}

function preset_t(adv){
    document.form.serviceName.value = "BravoAirService";
    document.form.textDescription.value = "This is a text description automatically generated as example description";
    //document.form.hasProcess.value ="http://www.daml.ri.cmu.edu/matchmaker/online/BravoAirProcess";
    
    document.getElementById('contactName0').value ="WS-VLAM";
    document.getElementById('contactTitle0').value = "Research Group";
    document.getElementById('contactPhone0').value = "+31 20 525 7514";
    document.getElementById('contactEmail0').value ="adam@science.uva.nl";
    document.getElementById('contactFax0').value = "+31 20 525 7590";
    document.getElementById('contactAddress0').value ="Kruislaan 403, 1098 SJ";
    document.getElementById('contactUrl0').value = "http://www.science.uva.nl/~gvlam/wsvlam/";
    
    document.getElementById("importurl0").value = "http://www.daml.ri.cmu.edu/matchmaker/owl/Concepts.owl";
    
    document.getElementById('scategoryname0').value = "UNSPSC-Airline";
    document.getElementById('staxonomy0').value = "http://www.unspsc.org";
    document.getElementById('svalue0').value = "airline";
    document.getElementById('scode0').value = "10223525";
    
    document.getElementById('scategoryname1').value = "NAISC-Airline";
    document.getElementById('staxonomy1').value = "http://www.naisc.org";
    document.getElementById('svalue1').value = "airline";
    document.getElementById('scode1').value = "525";
    
    document.getElementById("inputname0").value = "DepartureAirport";
    document.getElementById("inputparamtype0").value = "http://www.daml.org/services/owl-s/1.1/Concepts.owl#Airport";
    
    document.getElementById("inputname1").value = "ArrivalAirport";
    document.getElementById("inputparamtype1").value = "http://www.daml.org/services/owl-s/1.1/Concepts.owl#Airport";
    
    document.getElementById("inputname2").value = "DepartureDate";
    document.getElementById("inputparamtype2").value = "http://www.daml.org/services/owl-s/1.1/Concepts.owl#Date";
    
    document.getElementById("outputname0").value  = "FlightItinerary";
    document.getElementById("outputparamtype0").value  = "http://www.daml.org/services/owl-s/1.1/Concepts.owl#FlightItinerary";
}

function generate_header(){

    header = 
    "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
    "<rdf:RDF\n" +
    "    xmlns=\"http://server.host/ws-hammer/\"\n" +
    "    xml:base=\"http://server.host/ws-hammer/query/query.owls\"\n" +
    "    xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n" +
    "    xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"\n" +
    "    xmlns:owl=\"http://www.w3.org/2002/07/owl#\"\n" +
    "    xmlns:daml=\"http://www.daml.org/2001/03/daml+oil#\"\n" +
    "    xmlns:service=\"http://www.daml.org/services/owl-s/1.1/Service.owl#\"\n" +
    "    xmlns:profile=\"http://www.daml.org/services/owl-s/1.1/Profile.owl#\"\n" +
    "    xmlns:process=\"http://www.daml.org/services/owl-s/1.1/Process.owl#\"\n" +
    "    xmlns:grounding=\"http://www.daml.org/services/owl-s/1.1/Grounding.owl#\"\n" +
    "    xmlns:actor=\"http://www.daml.org/services/owl-s/1.1/ActorDefault.owl#\"" +">\n" +
    "\n" +
    "    <owl:Ontology rdf:about=\"\">\n" +
    "        <owl:imports rdf:resource=\"http://www.daml.org/services/owl-s/1.1/ActorDefault.owl\"/>\n" +
    "        <owl:imports rdf:resource=\"http://www.daml.org/services/owl-s/1.1/Grounding.owl\"/>\n" +
    "        <owl:imports rdf:resource=\"http://www.daml.org/services/owl-s/1.1/Process.owl\"/>\n" +
    "        <owl:imports rdf:resource=\"http://www.daml.org/services/owl-s/1.1/Profile.owl\"/>\n" +
    "        <owl:imports rdf:resource=\"http://www.daml.org/services/owl-s/1.1/Service.owl\"/>\n" +
    "        <owl:imports rdf:resource=\"http://www.daml.org/services/owl-s/1.1/ProfileAdditionalParameters.owl\"/>\n";
    
    //adding import from web interface
    if(import_count != 0 ){
        header =  header + import_string();
    }
    
    header =  header + 
    "    </owl:Ontology>\n\n";
    
    return header;
}
  
function generate_process(){
    serviceName = document.form.serviceName.value;
    
    ///////////////////////////////////////////////////////
    process = 
    "    <process:AtomicProcess rdf:ID=\"" + serviceName + "_Process\">\n" +
    "    <!-- Descriptions of the parameters that will be used by IOPEs -->\n";
    
    process = process + in_string();
    process = process + out_string();
    
    process = process +
    "    </process:AtomicProcess>\n\n";
    
    return process;
}

function generate_profile(){
    
    serviceName = document.form.serviceName.value;
    textDescription = document.form.textDescription.value;
    //hasProcess = document.form.hasProcess.value;
    
    profile = 
    "    <profile:Profile rdf:ID=\"" + serviceName + "_Profile\">\n" +
    "        <profile:serviceName>" + serviceName + "</profile:serviceName>\n";
    
    if(textDescription != ""){
        profile = profile + 
        "        <profile:textDescription>" 
        + textDescription + "\n" +
        "        </profile:textDescription>\n";
    }
    
    profile = profile + contact_string();
    profile = profile + srvcat_string();
    profile = profile + srvparam_string();
    profile = profile + 
    "        <profile:has_process rdf:resource=\"#" + serviceName + "_Process\"/>\n";
    profile = profile + in_ref_string();
    profile = profile + out_ref_string();
    profile = profile + 
    "    </profile:Profile>\n\n";
    
    return profile;
}

function generate_service(){
    
    serviceName = document.form.serviceName.value;
    
    service = 
    "    <service:Service rdf:ID=\"" + serviceName + "_Service\">\n" +
    "        <service:describedBy rdf:resource=\"#" + serviceName + "_Process\"/>\n" +
    "        <service:presents    rdf:resource=\"#" + serviceName + "_Profile\"/>\n" +
    "    </service:Service>\n\n";
    
    return service;
}

function generate_footer(){
    
    footer =  "</rdf:RDF>\n";
    
    return footer;
}

function generate(){
    toReturn = "";
    toReturn = toReturn + generate_header();

    toReturn = toReturn + generate_process();
    toReturn = toReturn + generate_profile();
    toReturn = toReturn + generate_service();

    toReturn = toReturn + generate_footer();
    
    return toReturn;
}


//function generate(){
//    
//    serviceName = document.form.serviceName.value;
//    textDescription = document.form.textDescription.value;
//    //hasProcess = document.form.hasProcess.value;
//    
//    ///////////////////////////////////////////////////////
//    header = 
//    "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
//    "<rdf:RDF\n" +
//    "    xmlns=\"http://server.host/ws-hammer/\"\n" +
//    "    xml:base=\"http://server.host/ws-hammer/query/query.owls\"\n" +
//    "    xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n" +
//    "    xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"\n" +
//    "    xmlns:owl=\"http://www.w3.org/2002/07/owl#\"\n" +
//    "    xmlns:daml=\"http://www.daml.org/2001/03/daml+oil#\"\n" +
//    "    xmlns:service=\"http://www.daml.org/services/owl-s/1.1/Service.owl#\"\n" +
//    "    xmlns:profile=\"http://www.daml.org/services/owl-s/1.1/Profile.owl#\"\n" +
//    "    xmlns:process=\"http://www.daml.org/services/owl-s/1.1/Process.owl#\"\n" +
//    "    xmlns:grounding=\"http://www.daml.org/services/owl-s/1.1/Grounding.owl#\"\n" +
//    "    xmlns:actor=\"http://www.daml.org/services/owl-s/1.1/ActorDefault.owl#\"" +">\n" +
//    "\n" +
//    "    <owl:Ontology rdf:about=\"\">\n" +
//    "        <owl:imports rdf:resource=\"http://www.daml.org/services/owl-s/1.1/ActorDefault.owl\"/>\n" +
//    "        <owl:imports rdf:resource=\"http://www.daml.org/services/owl-s/1.1/Grounding.owl\"/>\n" +
//    "        <owl:imports rdf:resource=\"http://www.daml.org/services/owl-s/1.1/Process.owl\"/>\n" +
//    "        <owl:imports rdf:resource=\"http://www.daml.org/services/owl-s/1.1/Profile.owl\"/>\n" +
//    "        <owl:imports rdf:resource=\"http://www.daml.org/services/owl-s/1.1/Service.owl\"/>\n" +
//    "        <owl:imports rdf:resource=\"http://www.daml.org/services/owl-s/1.1/ProfileAdditionalParameters.owl\"/>\n";
//    
//    //adding import from web interface
//    if(import_count != 0 ){
//        header =  header + import_string();
//    }
//    
//    header =  header + 
//    "\n    </owl:Ontology>\n\n";
//    
//    ///////////////////////////////////////////////////////
//    process = 
//    "    <process:AtomicProcess rdf:ID=\"" + serviceName + "_Process\">\n" +
//    "    <!-- Descriptions of the parameters that will be used by IOPEs -->\n";
//    
//    process = process + in_string();
//    process = process + out_string();
//    
//    process = process +
//    "    </process:AtomicProcess>\n\n";
//    
//    ///////////////////////////////////////////////////////
//    profile = 
//    "    <profile:Profile rdf:ID=\"" + serviceName + "_Profile\">\n" +
//    "        <profile:serviceName>" + serviceName + "</profile:serviceName>\n";
//    
//    if(textDescription != ""){
//        profile = profile + 
//        "        <profile:textDescription>" 
//        + textDescription + "\n" +
//        "        </profile:textDescription>\n";
//    }
//    
//    profile = profile + contact_string();
//    profile = profile + srvcat_string();
//    profile = profile + srvparam_string();
//    profile = profile + qrating_string();
//    profile = profile + 
//    "        <profile:has_process rdf:resource=\"#" + serviceName + "_Process\"/>\n";
//    profile = profile + in_ref_string();
//    profile = profile + out_ref_string();
//    profile = profile + 
//    "    </profile:Profile>\n\n";
//    
//    ///////////////////////////////////////////////////////
//    footer = 
//    "    <service:Service rdf:ID=\"" + serviceName + "_Service\">\n" +
//    "        <service:describedBy rdf:resource=\"#" + serviceName + "_Process\"/>\n" +
//    "        <service:presents    rdf:resource=\"#" + serviceName + "_Profile\"/>\n" +
//    "    </service:Service>\n\n" +
//    "</rdf:RDF>\n";
//    
//    return header + process + profile + footer;
//}


function generate_owls_query(){
    return generate();
}