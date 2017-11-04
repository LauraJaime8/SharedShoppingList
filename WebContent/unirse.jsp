<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="edu.uclm.esi.listadecompra.dominio.Usuario"%>
<%@page import="edu.uclm.esi.listadecompra.dominio.Lista"%>
<%@page import="edu.uclm.esi.listadecompra.dominio.Manager"%>
<%@page import="edu.uclm.esi.listadecompra.dao.DAOLista"%>

<%
	String token=request.getParameter("p");
	try {
		Usuario usuario=Usuario.unirsePorToken(token);
		session.setAttribute("usuario", usuario);
		Cookie cookie=new Cookie("idUsuario", ""+usuario.getId());
		cookie.setMaxAge(365*24*3600);
		response.addCookie(cookie);
	%>
	<jsp:forward page="index.html"></jsp:forward>
	<%
	}
	catch (Exception e) {
	%>
		<jsp:forward page="index.html"></jsp:forward>
	<%
	}

%>