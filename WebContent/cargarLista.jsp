<%@ page language="java" contentType="application/json" pageEncoding="UTF-8"%>

<%@page import="edu.uclm.esi.listadecompra.dominio.Lista"%>
<%@page import="edu.uclm.esi.listadecompra.dominio.Usuario"%>
<%@page import="edu.uclm.esi.listadecompra.dominio.Manager"%>
<%@page import="org.json.simple.JSONArray"%>
<%@page import="org.json.simple.JSONObject"%>

<%
	Usuario usuario=(Usuario) session.getAttribute("usuario");
	
	JSONObject resultado=new JSONObject();
	if (usuario!=null) {
		try {
			int idLista=Integer.parseInt(request.getParameter("idLista"));
			JSONObject lista=Manager.get().getLista(idLista);
			resultado.put("tipo", "OK");
			resultado.put("lista", lista);
		}
		catch (Exception e) {
			resultado.put("tipo", "ERROR");
			resultado.put("texto", e.getMessage());
		}
	} else {
		resultado.put("tipo", "NO_EXISTE");
	}
	out.println(resultado);
%>