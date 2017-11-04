<%@ page language="java" contentType="application/json" pageEncoding="UTF-8"%>

<%@page import="edu.uclm.esi.listadecompra.dominio.Manager"%>
<%@page import="edu.uclm.esi.listadecompra.dominio.Usuario"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@page import="org.json.simple.JSONObject"%>

<%
	String p=request.getParameter("p");
	JSONParser jsp=new JSONParser();
	JSONObject objeto=(JSONObject) jsp.parse(p);
	
	String email=objeto.get("email").toString();
	String pwd1=objeto.get("pwd1").toString();
	String pwd2=objeto.get("pwd2").toString();

	JSONObject resultado=new JSONObject();		
	if (!pwd1.equals(pwd2)) {
		resultado.put("tipo", "ERROR");
		resultado.put("texto", "Las passwords no coinciden");
	} else if (pwd1.length()<4) {
		resultado.put("tipo", "ERROR");
		resultado.put("texto", "Tu contraseña es muy corta");
	} else {
		try {
			Usuario usuario=Manager.get().registrar(email, pwd1);
			resultado.put("tipo", "OK");
			resultado.put("texto", "Se ha registrado correctamente");
			
			Cookie cookie=new Cookie("idUsuario", ""+usuario.getId());
			cookie.setMaxAge(365*24*3600);
			response.addCookie(cookie);
		}
		catch (Exception e) {
			resultado.put("tipo", "ERROR");
			resultado.put("texto", e.getMessage());
		}
	}
	out.println(resultado);
%>