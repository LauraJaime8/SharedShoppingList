<%@ page language="java" contentType="application/json" pageEncoding="UTF-8"%>

<%@page import="edu.uclm.esi.listadecompra.dominio.Manager"%>
<%@page import="edu.uclm.esi.listadecompra.dominio.Usuario"%>
<%@page import="org.json.simple.JSONObject"%>

<%
	String nombreLista=request.getParameter("nombreLista");
	
	JSONObject resultado=new JSONObject();		
	try {
		Usuario usuario=(Usuario) session.getAttribute("usuario");
		Manager.get().crearLista(usuario, nombreLista);
		resultado.put("tipo", "OK");
	}
	catch (Exception e) {
		resultado.put("tipo", "ERROR");
		resultado.put("texto", e.getMessage());
	}
	out.println(resultado);
%>