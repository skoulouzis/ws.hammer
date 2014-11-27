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

////////////////////////////
// Input
////////////////////////////
var in_count  = 0;

function in_add(){
    
    // Append div and set innner html
    div    = document.createElement("DIV");
    div.id = "INPUT" + in_count;
    
    div.innerHTML = 
    "<table style=\"width: 100%; text-align: left;\" >" + 
    "<tr>" +
    "    <td class=\"text_list\" colspan=\"3\">Input port - <b>#" + in_count + "</b></td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Input Name</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='inputname" + in_count+"' NAME='inputname" + in_count+"' VALUE='' SIZE=\"85\">" +
    "    </td>" +
    "</tr><tr>" +
    "    <td class=\"table_title\">Input Type (URI)</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='inputparamtype" + in_count+"' NAME='inputparamtype" + in_count+"' VALUE='' SIZE=\"85\">" +
    "    </td>" +
    "</tr>" +
    "</table>";

    document.getElementById("DI").appendChild(div);
    
    in_count = in_count + 1;
}

function in_del(){
    
    div = document.getElementById("INPUT" + (in_count -1));
    //div = document.all.item("INPUT",(in_count-1));
    
    if(div != null){
        document.getElementById("DI").removeChild(div);
        //document.all.DI.removeChild(div);
        in_count = in_count - 1;
    }
}

function in_resize(width){
    
    for(i = 0; i < in_count; i++){
        document.getElementById("inputname"+i).size = width;     
        document.getElementById("inputparamtype"+i).size = width;
    }  
}

function in_string(){
    //For the construction of the 'process'
    
    inputs = "";
    for(i = 0; i < in_count; i++){
        
        inputName = document.getElementById("inputname"+i).value;
        parameterType  = document.getElementById("inputparamtype"+i).value;
        
        if((parametertype != "") && (inputname != "")){
            inputs = inputs +
            "        <process:hasInput>\n" +
            "            <process:Input rdf:ID=\"" + inputName + "\">\n" +
            "                <process:parameterType rdf:datatype=\"http://www.w3.org/2001/XMLSchema#anyURI\"\n" +
            "                >" + parameterType + "</process:parameterType>\n" +
            "            </process:Input>\n" +
            "        </process:hasInput>\n";
        }
    }
    return inputs;
}

function in_ref_string(){
    //For the references at the 'profile'
    
    inputs_ref = "";
    for(i = 0; i < in_count; i++){
        inputName = document.getElementById("inputname"+i).value;
        parameterType  = document.getElementById("inputparamtype"+i).value;
        
        if((inputName != "") && (parameterType != "")){
            inputs_ref = inputs_ref +  
            "        <profile:hasInput rdf:resource=\"#" + inputName + "\"/>\n" 
        }
    }
    
    return inputs_ref;
}

function in_validate(){
    
    for(i = 0; i < in_count; i++){
        
        inputname = document.getElementById("inputname"+i).value;
        parametertype  = document.getElementById("inputparamtype"+i).value;
        
        if(inputname == ""){
            alert("Please enter the 'Input Name' of Input No." + (i+1));
            return false;
            
        }
        if (parametertype == ""){
            alert("Please enter the 'Parameter Type' of Input No." + (i+1));
            return false;
        }
    }
    
    return true;
}

////////////////////////////
// Output
////////////////////////////
var out_count = 0;

function out_add(){
    
    // Append div and set innner html
    div    = document.createElement("DIV");
    div.id = "OUTPUT"+ out_count;
    
    div.innerHTML = 
    "<table style=\"width: 100%; text-align: left;\" >" + 
    "<tr>" +
    "    <td class=\"text_list\" colspan=\"3\">Output port - <b>#" + out_count + "</b></td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Output Name</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='outputname"+ out_count +"' NAME='outputname"+ out_count +"' VALUE='' SIZE=\"83\">" + 
    "    </td>" +
    "</tr><tr>" +
    "    <td class=\"table_title\">Output Type (URI)</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='outputparamtype"+ out_count +"' NAME='outputparamtype"+ out_count +"' VALUE='' SIZE=\"83\">" + 
    "    </td>" +
    "</tr>" +
    "</table>";
    
    document.getElementById("DO").appendChild(div);
    
    out_count = out_count + 1;
}

function out_del(){
    
    div = document.getElementById("OUTPUT" + (out_count -1));
    //div = document.all.item("OUTPUT",(out_count-1));
    
    if(div != null){
        document.getElementById("DO").removeChild(div);
        //document.all.DO.removeChild(div);
        out_count = out_count - 1;
    }
}

function out_string(){
    //For the construction of the 'process'
    
    outputs = "";
    for(i = 0; i < out_count; i++){
        
        outputName = document.getElementById("outputname"+i).value;
        outputParamType  = document.getElementById("outputparamtype"+i).value;
        
        if((outputName != "") && (outputParamType != "")){
            outputs = outputs + 
            "        <process:hasOutput>\n" +
            "            <process:Output rdf:ID=\"" + outputName + "\">\n" +
            "                <process:parameterType rdf:datatype=\"http://www.w3.org/2001/XMLSchema#anyURI\"\n" +
            "                >" + outputParamType + "</process:parameterType>\n" +
            "            </process:Output>\n" +
            "        </process:hasOutput>\n";
        }
    }
    
    return outputs;
}

function out_ref_string(){
    //For the references at the 'profile'
    
    outputs_ref = "";
    for(i = 0; i < out_count; i++){
        outputName = document.getElementById("outputname"+i).value;
        
        if((outputName != "") && (outputParamType != "")){
            outputs_ref = outputs_ref +  
            "        <profile:hasOutput rdf:resource=\"#" + outputName + "\"/>\n" 
        }
    }
    
    return outputs_ref;
}

function out_resize(width){
    for(i = 0; i < out_count; i++){
        document.getElementById("outputname"+i).size = width;
        document.getElementById("outputparamtype"+i).size = width;
    } 
}  

function out_validate(){
    
    for(i = 0; i < out_count; i++){
        
        outputName = document.getElementById("outputname"+i).value;
        outputParamType  = document.getElementById("outputparamtype"+i).value;
        
        if(outputName == ""){
            alert("Please enter the Output Name of Ouput No." + (i+1));
            return false;    
        }
        
        if (outputParamType == ""){
            alert("Please enter the Parameter Type of Ouput No." + (i+1));
            return false;     
        }
    }
    
    return true;  	
}