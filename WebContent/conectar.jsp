<%@ page language="java" contentType="application/json" pageEncoding="UTF-8"%>

<%@page import="edu.uclm.esi.listadecompra.dominio.Usuario"%>
<%@page import="edu.uclm.esi.listadecompra.dominio.Manager"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@page import="org.json.simple.JSONObject"%>

<%
	Cookie[] cookies=request.getCookies();
	Cookie cookieIdUsuario=null;
	if (cookies!=null) {
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("idUsuario")) {
				cookieIdUsuario=cookie;
				break;
			}
		}
	}
	
	JSONObject resultado=new JSONObject();
	if (cookieIdUsuario!=null) {
		int idUsuario=Integer.parseInt(cookieIdUsuario.getValue());
		try {
			Usuario usuario=Manager.get().conectar(idUsuario);
			resultado.put("tipo", "OK");
			resultado.put("id", usuario.getId());
			resultado.put("email", usuario.getEmail());	
			session.putValue("usuario", usuario);
		}
		catch (Exception e) {
			resultado.put("tipo", "ERROR");
			System.out.println(e.getMessage());
			resultado.put("texto", e.getMessage());
		}
	} else {
		resultado.put("tipo", "NO_EXISTE");
	}
	out.println(resultado);
%>