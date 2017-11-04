"use strict";

var url="ws://" + window.location.hostname + ":" + window.location.port + "/listaDeCompra/servidorWebSocket";
var chat;

function conectarWebSocket() {
	url=url + "?idUsuario=" + usuario.id;
	chat=new WebSocket(url);

	chat.onopen = function() {
		document.getElementById("listening").innerHTML="Escuchando";
		alert("Se ha conectado con el usuario: "+ usuario.email);
	} 
	
	chat.onerror = function() {
		document.getElementById("listening").innerHTML="Sin escuchar";
	}
	
	chat.onmessage = function(mensaje) {
		mensaje=JSON.parse(mensaje.data);
		var tipo=mensaje.tipo;
		if (tipo=="ACTUALIZACION_DE_LISTA") {
			var idLista=mensaje.idLista;		
			if (sessionStorage.idLista==idLista) {
				alert("Actualización en lista " + idLista); 
			}
		} else if (tipo=="ERROR") {
			alert(mensaje.texto);
		}
	}		
}