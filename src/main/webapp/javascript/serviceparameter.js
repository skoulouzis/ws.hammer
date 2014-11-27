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

var srvparam_count= 0; 

function srvparam_add(){
    
    // Append div and set innner html
    div    = document.createElement("DIV");
    div.id = "SRVPARAM" + srvparam_count;
    
    div.innerHTML =
    "<table style=\"width: 100%; text-align: left;\">" + 
    "<tr>" +
    "    <td class=\"text_list\" colspan=\"3\">Parameter - <b>#" + srvparam_count + "</b></td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Parameter Name</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='sparamname" + srvparam_count + "' NAME='sparam" + srvparam_count+"' VALUE='' SIZE=\"85\">" +
    "    </td>" +
    "</tr><tr>" +
    "    <td class=\"table_title\">sParameter</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='ssparam" + srvparam_count + "' NAME='ssparm" + srvparam_count+"' VALUE='' SIZE=\"85\">" +
    "    </td>" +
    "</tr>" +
    "</table>";

    document.getElementById("SP").appendChild(div);
    
    srvparam_count = srvparam_count + 1;
}

function srvparam_del(){
    
    div = document.getElementById("SRVPARAM" + (srvparam_count -1));
    
    if(div != null){
        document.getElementById("SP").removeChild(div);
        srvparam_count = srvparam_count - 1;
    }
}

function srvparam_string(){
    
    empty = "yes";
    srvparam =
    "        <profile:serviceParameter>\n";
    
    for( i=0; i< srvparam_count; i++){
        sparamname = document.getElementById('sparamname'+i).value;
        ssparam = document.getElementById('ssparam'+i).value;
        
        if((sparamname != "") && (ssparam != "")){
            empty = "no";
            
            srvparam = srvparam + 
            "            <profile:ServiceParameter rdf:ID=\""+sparamname+"\">\n" +
            "                <profile:serviceParameterName>" + sparamname + "</serviceParameterName>\n" +
            "                <profile:sParameter>" + ssparam + "</sParameter>\n" +
            "            </profile:ServiceParameter>\n";
        }
    }
    
    srvparam = srvparam + 
    "        <profile:serviceParameter>\n";
    
    if(empty == "yes"){
        srvparam = "";
    } 
    
    return srvparam;
}

function srvparam_validate(){
    for( i=0; i< srvparam_count; i++){
        
        sparamname = document.getElementById('sparamname'+i).value;
        ssparam = document.getElementById('ssparam'+i).value;
        
        if(sparamname == ""){
            alert("Please enter the  parameter 'Name' of the service parameter  No." + (i+1));
            return false;    
        }
        
        if(ssparam == ""){
            alert("Please enter the 'sParameter' of the service parameter  No." + (i+1));
            return false;    
        }
    }
    
    return true;
}