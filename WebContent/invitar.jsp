<%@ page language="java" contentType="application/json" pageEncoding="UTF-8"%>

<%@page import="edu.uclm.esi.listadecompra.dominio.Usuario"%>
<%@page import="edu.uclm.esi.listadecompra.dominio.Lista"%>
<%@page import="edu.uclm.esi.listadecompra.dominio.Manager"%>
<%@page import="edu.uclm.esi.listadecompra.dao.DAOLista"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@page import="org.json.simple.JSONObject"%>

<%
	String emailInvitado=request.getParameter("emailInvitado");
	int idLista=Integer.parseInt(request.getParameter("idLista"));
	JSONObject resultado=new JSONObject();
	try {
		Lista lista=DAOLista.select(idLista);
		Usuario usuario=(Usuario) session.getAttribute("usuario");
		usuario.invitar(emailInvitado, lista);
		resultado.put("tipo", "OK");
	}
	catch (Exception e) {
		resultado.put("tipo", "ERROR");
		resultado.put("texto", e.getMessage());
	}
	out.println(resultado);
%>