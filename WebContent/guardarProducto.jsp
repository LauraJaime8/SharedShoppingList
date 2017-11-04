<%@ page language="java" contentType="application/json" pageEncoding="UTF-8"%>

<%@page import="edu.uclm.esi.listadecompra.dominio.Manager"%>
<%@page import="edu.uclm.esi.listadecompra.dominio.Usuario"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@page import="org.json.simple.JSONObject"%>

<%
	String p=request.getParameter("p");
	JSONParser jsp=new JSONParser();
	JSONObject objeto=(JSONObject) jsp.parse(p);
	
	int idLista=Integer.parseInt(objeto.get("idLista").toString());
	int idProducto=Integer.parseInt(objeto.get("idProducto").toString());
	String nombre=objeto.get("nombre").toString();
	int cantidadDeseada=Integer.parseInt(objeto.get("cantidadDeseada").toString());
	int cantidadExistente=Integer.parseInt(objeto.get("cantidadExistente").toString());
	
	Usuario usuario=(Usuario) session.getAttribute("usuario");

	JSONObject resultado=new JSONObject();		
	try {
		Manager.get().guardarProducto(usuario, idLista, idProducto, nombre, cantidadDeseada, cantidadExistente);
		resultado.put("tipo", "OK");
	}
	catch (Exception e) {
		resultado.put("tipo", "ERROR");
		resultado.put("texto", e.getMessage());
	}
	out.println(resultado);
%>