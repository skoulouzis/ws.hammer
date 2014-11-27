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

var srvcat_count= 0; 

function srvcat_add(){
    
    // Append div and set innner html
    div    = document.createElement("DIV");
    div.id = "SRVCAT" + srvcat_count;
    
    div.innerHTML =
    "<table style=\"width: 100%; text-align: left;\">" + 
    "<tr>"  +
    "    <td class=\"text_list\" colspan=\"3\">Category - <b>#" + srvcat_count + "</b></td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Name</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='scategoryname" + srvcat_count + "' NAME='scategoryname" + srvcat_count+"' VALUE='' SIZE=\"90\">" +
    "    </td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Taxonomy</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='staxonomy" + srvcat_count + "' NAME='staxonomy" + srvcat_count+"' VALUE='' SIZE=\"90\">" +
    "    </td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Value</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='svalue" + srvcat_count + "' NAME='svalue" + srvcat_count+"' VALUE='' SIZE=\"90\">" +
    "    </td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Code</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='scode" + srvcat_count + "' NAME='scode" + srvcat_count+"' VALUE='' SIZE=\"90\">" +
    "    </td>" +
    "</tr>" +
    "</table>";
    
    document.getElementById("SC").appendChild(div);
    
    srvcat_count = srvcat_count + 1;
}

function srvcat_unspsc_add(){
    
    // Append div and set innner html
    div    = document.createElement("DIV");
    div.id = "SRVCAT" + srvcat_count;
    
    div.innerHTML = 
    "<table style=\"width: 100%; text-align: left;\">" + 
    "<tr>"  +
    "    <td class=\"text_list\" colspan=\"3\">Category - <b>#" + srvcat_count + " (UNSPSC)</b></td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Name</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='scategoryname" + srvcat_count + "' NAME='scategoryname" + srvcat_count+"' VALUE='UNSPSC' SIZE=\"90\">" +
    "    </td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Taxonomy</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='staxonomy" + srvcat_count + "' NAME='staxonomy" + srvcat_count+"' VALUE='http://www.unspsc.org/' SIZE=\"90\">" +
    "    </td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Value</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='svalue" + srvcat_count + "' NAME='svalue" + srvcat_count+"' VALUE='' SIZE=\"90\">" +
    "    </td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Code</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='scode" + srvcat_count + "' NAME='scode" + srvcat_count+"' VALUE='' SIZE=\"90\">" +
    "    </td>" +
    "</tr>" +
    "</table>";
    
    document.getElementById("SC").appendChild(div);
    
    srvcat_count = srvcat_count + 1;
}

function srvcat_naisc_add(){
    
    // Append div and set innner html
    div    = document.createElement("DIV");
    div.id = "SRVCAT" + srvcat_count;
    
    div.innerHTML =
    "<table style=\"width: 100%; text-align: left;\">" + 
    "<tr>"  +
    "    <td class=\"text_list\" colspan=\"3\">Category - <b>#" + srvcat_count + " (NAISC)</b></td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Name</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='scategoryname" + srvcat_count + "' NAME='scategoryname" + srvcat_count+"' VALUE='NAISC' SIZE=\"80\">" +
    "    </td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Taxonomy</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='staxonomy" + srvcat_count + "' NAME='staxonomy" + srvcat_count+"' VALUE='http://www.census.gov/naics/' SIZE=\"80\">" +
    "    </td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Value</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='svalue" + srvcat_count + "' NAME='svalue" + srvcat_count+"' VALUE='' SIZE=\"80\">" +
    "    </td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Code</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='scode" + srvcat_count + "' NAME='scode" + srvcat_count+"' VALUE='' SIZE=\"80\">" +
    "    </td>" +
    "</tr>" +
    "</table>";

    document.getElementById("SC").appendChild(div);
    srvcat_count = srvcat_count + 1;
    
}

function srvcat_vle_add(){
    
    // Append div and set innner html
    div    = document.createElement("DIV");
    div.id = "SRVCAT" + srvcat_count;
    
    div.innerHTML =
    "<table style=\"width: 100%; text-align: left;\">" + 
    "<tr>"  +
    "    <td class=\"text_list\" colspan=\"3\">Category - <b>#" + srvcat_count + " (VL-e)</b></td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Name</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='scategoryname" + srvcat_count + "' NAME='scategoryname" + srvcat_count+"' VALUE='VL-e' SIZE=\"90\">" +
    "    </td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Taxonomy</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='staxonomy" + srvcat_count + "' NAME='staxonomy" + srvcat_count+"' VALUE='http://www.vl-e.nl/' SIZE=\"90\">" +
    "    </td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Value</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='svalue" + srvcat_count + "' NAME='svalue" + srvcat_count+"' VALUE='' SIZE=\"90\">" +
    "    </td>" +
    "</tr><tr>"  +
    "    <td class=\"table_title\">Code</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='scode" + srvcat_count + "' NAME='scode" + srvcat_count+"' VALUE='' SIZE=\"90\">" +
    "    </td>" +
    "</tr>" +
    "</table>";
    
    document.getElementById("SC").appendChild(div);
    
    srvcat_count = srvcat_count + 1;
}

function srvcat_del(){
    
    div = document.getElementById("SRVCAT" + (srvcat_count -1));
    
    if(div != null){
        document.getElementById("SC").removeChild(div);
        srvcat_count = srvcat_count - 1;
    }
}

function srvcat_string(){
    
    empty = "yes";
    srvcat =
    "        <profile:serviceCategory>\n";
    
    for( i=0; i< srvcat_count; i++){
        scategoryname = document.getElementById('scategoryname'+i).value;
        staxonomy = document.getElementById('staxonomy'+i).value;
        svalue = document.getElementById('svalue'+i).value;
        scode = document.getElementById('scode'+i).value;
        
        if((scategoryname != "") && (staxonomy != "")&& (svalue != "")&& (scode != "")){
            empty = "no";
            
            srvcat = srvcat + 
            "            <profile:ServiceCategory rdf:ID=\""+scategoryname+"\">\n" +
            "                <profile:categoryName>" + scategoryname + "</profile:categoryName>\n" +
            "                <profile:taxonomy>" + staxonomy + "</profile:taxonomy>\n" +
            "                <profile:value>" + svalue + "</profile:value>\n" +
            "                <profile:code>" + scode + "</profile:code>\n" +
            "            </profile:ServiceCategory>\n";
        }
    }
    
    srvcat = srvcat  +
    "        </profile:serviceCategory>\n";
    
    if(empty == "yes"){
        srvcat = "";
    } 
    
    return srvcat;
}

function srvcat_validate(){
    
    for( i=0; i< srvcat_count; i++){
        scategoryname = document.getElementById('scategoryname'+i).value;
        staxonomy = document.getElementById('staxonomy'+i).value;
        svalue = document.getElementById('svalue'+i).value;
        scode = document.getElementById('scode'+i).value;
        
        if(scategoryname == ""){
            alert("Please enter the 'Category Name' of the category No." + (i+1));
            return false;    
        }
        
        if(staxonomy == ""){
            alert("Please enter the 'Taxonomy' of the category  No." + (i+1));
            return false;    
        }
        
        if(svalue == ""){
            alert("Please enter the 'Value' of the category  No." + (i+1));
            return false;    
        }
        
        if(scode == ""){
            alert("Please enter the 'Code' of the category  No." + (i+1));
            return false;    
        }
    }
    
    return true;
}