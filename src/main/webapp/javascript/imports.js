/*
* WS-VLAM HAMMER
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

var import_count = 0;

function import_add(){
    
    // Append div and set innner html
    div    = document.createElement("DIV");
    div.id = "IMPORT" + import_count;
    
    div.innerHTML = 
    "<table style=\"width: 100%; text-align: left;\">" + 
    "<tr>"  +
    "    <td class=\"table_title\">Ontology URL</td>" +
    "    <td class=\"table_title\">:</td>" +
    "    <td class=\"text_list\">" +
    "      <INPUT TYPE=text ID='importurl" + import_count + "' NAME='importurl" + import_count + "' VALUE='' SIZE=\"88\">" +
    "    </td>" +
    "</tr>" +
    "</table>";

    document.getElementById("IM").appendChild(div);
    
    import_count = import_count + 1;
}

function import_del(){
    
    div = document.getElementById("IMPORT" + (import_count -1));
    
    if(div != null){
        document.getElementById("IM").removeChild(div);
        import_count = import_count - 1;
    }
}

function import_string(){
    
    imports = "";
    
    for( i=0; i< import_count; i++){
        
        importurl = document.getElementById('importurl'+i).value;
        
        if((importurl != "")){
            imports = imports + 
            "        <owl:imports rdf:resource=\"" + importurl + "\"/>\n";
        }
    }
    
    return imports;
}

function import_validate(){
    
    for(i = 0; i < import_count; i++){
        
        importurl = document.getElementById("importurl"+i).value;
        
        if((importurl == "")){      
            alert("Please enter the 'URL' of the import No." + (i+1));
            return false;
        }
    }
    
    return true;
}