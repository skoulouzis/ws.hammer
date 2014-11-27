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



function ontology_help(){
	
	nw = window.open("", "", "height=200,width=300,location=no,menubar=no,resizable=no,scrollbars=no");
	nw.document.write("<HTML>");
	nw.document.write("<HEAD><TITLE>What is Ontology ?</TITLE></HEAD>");
	nw.document.write("<BODY style='color: rgb(0, 0, 0); background-color: rgb(255, 255, 204);'link='#000099' vlink='#990099' alink='#000099'><b>What should you add in ontology ? </b><br>You should add the URLs of ontology files, which are required by your OWL-S defintion</BODY>");
	nw.document.write("</HTML>");

}
function contact_help(){
	
	nw = window.open("", "", "height=300,width=300,location=no,menubar=no,resizable=no,scrollbars=no");
	nw.document.write("<HTML>");
	nw.document.write("<HEAD><TITLE>What is Contact ?</TITLE></HEAD>");
	nw.document.write("<BODY style='color: rgb(0, 0, 0); background-color: rgb(255, 255, 204);'link='#000099' vlink='#990099' alink='#000099'><b>What should you add in contact ? </b><br>Add the contact\'s information (like name, title, phone etc) of your service description. <br> <br>Sample contact information.<br><br><b>Contact Information</b><br><b>name : </b>Naveen Srinivasan<br><b>title : </b>Research Programmer<br><b>phone : </b>412-268-3740<br><b>email : </b>naveen@cs.cmu.edu<br><b>fax : </b>No Fax<br><b>physicalAddress : </b>5000 Forbes<br><b>webURL : </b>http://www.naveen.com</BODY>");
	nw.document.write("</HTML>");

}