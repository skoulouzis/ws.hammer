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

var contact_count = 0;

function contact_add(){
    
    // Append div and set innner html
    div    = document.createElement("DIV");
    div.id = "CONTACT" + contact_count;
    
    div.innerHTML =
    "<table style=\"width: 100%; text-align: left;\">" + 
    "<tr>"  +
    "    <td class=\"text_list\" colspan=\"3\">Detailed contact information - <b>#" + contact_count + "</b></td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Name</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='contactName" + contact_count + "' NAME='contactName" + contact_count + "' VALUE='' SIZE=\"91\">" +
    "    </td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Title</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='contactTitle" + contact_count + "' NAME='contactTitle" + contact_count + "' VALUE='' SIZE=\"91\">" +
    "    </td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Phone</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='contactPhone" + contact_count + "' NAME='contactPhone" + contact_count + "' VALUE='' SIZE=\"91\">" +
    "    </td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Fax</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='contactFax" + contact_count + "' NAME='contactFax" + contact_count + "' VALUE='' SIZE=\"91\">" +
    "    </td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Address</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='contactAddress" + contact_count + "' NAME='contactAddress" + contact_count + "' VALUE='' SIZE=\"91\">" +
    "    </td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Email</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='contactEmail" + contact_count + "' NAME='contactEmail" + contact_count + "' VALUE='' SIZE=\"91\">" +
    "    </td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Web URL</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='contactUrl" + contact_count + "' NAME='contactUrl" + contact_count + "' VALUE='' SIZE=\"91\">" +
    "    </td>" +
    "</tr>" +
    "</table>";
    
    document.getElementById("CI").appendChild(div);
    
    contact_count = contact_count + 1;
}

function contact_del(){
    
    div = document.getElementById("CONTACT" + (contact_count -1));
    //div = document.all.item("INPUT",(in_count-1));
    
    if(div != null){
        document.getElementById("CI").removeChild(div);
        //document.all.DI.removeChild(div);
        contact_count = contact_count - 1;
    }
}

function contact_string(){
    
    empty = "yes";
    contacts = 
    "        <profile:contactInformation>\n";
    
    for( i=0; i< contact_count; i++){
        
        empty = "no";
        
        contactName = document.getElementById('contactName'+i).value;
        contactTitle = document.getElementById('contactTitle'+i).value;
        contactPhone = document.getElementById('contactPhone'+i).value;
        contactEmail = document.getElementById('contactEmail'+i).value;
        contactFax = document.getElementById('contactFax'+i).value;
        contactAddress = document.getElementById('contactAddress'+i).value;
        contactUrl = document.getElementById('contactUrl'+i).value;
        
        contacts = contacts + 
        "            <actor:Actor rdf:ID=\"" + contactName  + "\">\n" +
        "                <actor:name>"+ contactName +"</actor:name>\n" +
        "                <actor:title>"+ contactTitle +"</actor:title>\n" +
        "                <actor:phone>"+ contactPhone +"</actor:phone>\n" +
        "                <actor:fax>"+ contactEmail +" </actor:fax>\n" +
        "                <actor:email>"+ contactFax +"</actor:email>\n" +
        "                <actor:physicalAddress>"+ contactAddress +"</actor:physicalAddress>\n" +
        "                <actor:webURL>"+ contactUrl +"</actor:webURL>\n" +
        "            </actor:Actor>\n";
    }
    
    contacts = contacts +
    "        </profile:contactInformation>\n";
    
    if(empty == "yes"){
        contacts = "";
    } 
    
    return contacts;
}

function contacts_validate(){
    for( i=0; i< contact_count; i++){
        
        contactName = document.getElementById('contactName'+i).value;
        contactTitle = document.getElementById('contactTitle'+i).value;
        contactPhone = document.getElementById('contactPhone'+i).value;
        contactEmail = document.getElementById('contactEmail'+i).value;
        contactFax = document.getElementById('contactFax'+i).value;
        contactAddress = document.getElementById('contactAddress'+i).value;
        contactUrl = document.getElementById('contactUrl'+i).value;
        
        if(contactName == ""){
            alert("Please enter the 'Name' of Contact No." + (i+1));
            return false;    
        }
        
        if(contactTitle == ""){
            alert("Please enter the 'Title' of Contact No." + (i+1));
            return false;    
        }
        
        if(contactPhone == ""){
            alert("Please enter the 'Phone' of Contact No." + (i+1));
            return false;    
        }
        
        if(contactEmail == ""){
            alert("Please enter the 'Email' of Contact No." + (i+1));
            return false;    
        }
        
        if(contactFax == ""){
            alert("Please enter the 'Fax' of Contact No." + (i+1));
            return false;    
        }
        
        if(contactAddress == ""){
            alert("Please enter the 'Address' of Contact No." + (i+1));
            return false;    
        }
        
        if(contactUrl == ""){
            alert("Please enter the 'URL' of Contact No." + (i+1));
            return false;    
        }
    }
    
    return true;
}