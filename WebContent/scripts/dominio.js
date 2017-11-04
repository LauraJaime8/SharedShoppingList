"use strict";

function Usuario() {
	this.id=-1;
	this.email=null;
}

Usuario.prototype.conectar = function() {
	var request=new XMLHttpRequest();
	request.open("get", "conectar.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState==4) {
			var resultado=JSON.parse(request.responseText);
			if (resultado.tipo=="OK") {
				usuario.email=resultado.email;
				usuario.id=resultado.id;
				sessionStorage.usuario=JSON.stringify(usuario);
				document.getElementById("tdLogin").setAttribute("style", "display:none");
				var href=document.createElement("p"); 
				tdMostrarEmail.appendChild(href);
				href.setAttribute("href", usuario.email);
				href.innerHTML=usuario.email;
				var href=document.createElement("a");
				href.setAttribute("href", "javascript:cargarUsuario(" + usuario.id + ")");
				usuario.cargarListas();
				conectarWebSocket();
				document.getElementById("tdNuevaLista").setAttribute("style", "display:visible");

			} else if (resultado.tipo=="ERROR") {
				alert("Error al acceder al sistema: " + resultado.texto);
			} else if (resultado.tipo=="NO_EXISTE") {
				document.getElementById("tdLogin").setAttribute("style", "display:visible");
			}
		}
	};
	request.send();
}

Usuario.prototype.cargarUsuario = function(id){
	var request=new XMLHttpRequest();
	request.open("post", "cargarUsuario.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState==4) {
			var resultado=JSON.parse(request.responseText);
			if (resultado.tipo=="OK") {
				user.email=resultado.user.email;
				user.mostrarUser();
			} else if (resultado.tipo=="ERROR") {
				alert("Error al acceder al sistema: " + resultado.error);
			} 
		}
	}
	request.send("id=" + id);
}


Usuario.prototype.mostrarUser=function(){
	var href=document.createElement("a"); 
	href.setAttribute("href", "javascript:mostrarUsuario(" + user.id + ")");
	href.innerHTML=user.nombre;
}


Usuario.prototype.login = function() {
	var email=document.getElementById("cajaEmailLogin").value;
	var pwd=document.getElementById("cajaPwd").value;
	
	var request=new XMLHttpRequest();
	request.open("post", "identificar.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState==4) {
			var resultado=JSON.parse(request.responseText);
			if (resultado.tipo=="OK") {
				usuario.email=resultado.email;
				usuario.id=resultado.id;
				sessionStorage.usuario=JSON.stringify(usuario);
				document.getElementById("tdLogin").setAttribute("style", "display:none");
				document.getElementById("tdRegistrar").setAttribute("style", "display:none");
				document.getElementById("tdBienvenido").setAttribute("style", "visible");
				document.getElementById("tdNuevaLista").setAttribute("style", "display:visible");
				document.getElementById("tdAddProducto").setAttribute("style", "display:none");
				usuario.cargarListas(); 
				conectarWebSocket();
			} else {
				alert("Error: " + resultado.texto);
			}
		}
	}
	var parametros= {
		email : email,
		pwd : pwd
	};
	var linea="p=" + JSON.stringify(parametros);
	request.send(linea);
}


Usuario.prototype.registrar = function() {
	var email=document.getElementById("cajaEmailRegistro").value;
	var pwd1=document.getElementById("cajaPwd1").value;
	var pwd2=document.getElementById("cajaPwd2").value;
	
	var request=new XMLHttpRequest();
	request.open("post", "registrar.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState==4) {
			var resultado=JSON.parse(request.responseText);
			if (resultado.tipo=="OK") {
				tdRegistrar.setAttribute("style", "display:none");
				usuario.mostrarNuevoRegistro(true);
				usuario.conectar();
				tdNuevaLista.setAttribute("style", "display:visible");
				tdAnadirProducto.setAttribute("style", "display:none");

			} else {
				alert("Error: " + resultado.texto);
			}
		}
	}
	var parametros= {
		email : email,
		pwd1 : pwd1,
		pwd2 : pwd2
	};
	var linea="p=" + JSON.stringify(parametros);
	request.send(linea);
}


Usuario.prototype.mostrarNuevoRegistro = function(visible) { 
	if(visible){
		alert("Se ha registrado correctamente")
	}else
		td.setAttribute("style", "display:none");
}

Usuario.prototype.crearLista = function() {
	var nombre=document.getElementById("cajaNuevaLista").value;
	lista=new Lista();
	lista.crear(nombre);
	document.getElementById("tdNuevaLista").setAttribute("style", "display:visible");
	document.getElementById("tdAddProducto").setAttribute("style", "display:none");
	
}

Usuario.prototype.eliminarLista = function() {
	var nombre=document.getElementById("cajaNuevaLista").value;
	lista=new Lista();
	lista.eliminar(nombre);
	document.getElementById("tdNuevaLista").setAttribute("style", "display:visible");	
}


Usuario.prototype.invitar = function() {
	var emailInvitado=document.getElementById("emailInvitado").value;
	var request=new XMLHttpRequest();
	request.open("post", "invitar.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState==4) {
			var resultado=JSON.parse(request.responseText);
			if (resultado.tipo=="OK") {
				alert("Se ha invitado a " + emailInvitado);
			} else if (resultado.tipo=="ERROR") {
				alert("Error al acceder al sistema: " + resultado.error);
			} 
		}
	}
	request.send("emailInvitado=" + emailInvitado + "&idLista=" + sessionStorage.idLista);
}



Usuario.prototype.cargarListas = function() {
	var request=new XMLHttpRequest();
	request.open("get", "cargarListas.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState==4) {
			var resultado=JSON.parse(request.responseText);
			if (resultado.tipo=="OK") {
				var listas=resultado.listas;
					for (var i=0; i<listas.length; i++) {
						panelListas.setAttribute("style", "display:visible");
						var lista=listas[i];
						var href=document.createElement("a"); //a es una referencia, un enlace
						panelListas.appendChild(href);
						href.setAttribute("href", "javascript:mostrar(" + lista.id + ")");
						href.innerHTML=lista.nombre;
						var br=document.createElement("br"); //br es espacio en blanco
						panelListas.appendChild(br);
					}
			} else {
				alert("Error: " + resultado.texto);
			}
		}
	}
	request.send();
}


function Lista() {
	this.idLista=-1;
	this.nombre=null;
}

Lista.prototype.cargar = function(idLista) {
	var request=new XMLHttpRequest();
	request.open("post", "cargarLista.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState==4) {
			var resultado=JSON.parse(request.responseText);
			if (resultado.tipo=="OK") {
				lista.idLista=idLista;
				lista.nombre=resultado.lista.nombre;
				lista.productos=resultado.lista.productos;
				lista.miembros=resultado.lista.miembros;
				lista.mostrar();
			} else if (resultado.tipo=="ERROR") {
				alert("Error al acceder al sistema: " + resultado.error);
			} 
		}
	}
	request.send("idLista=" + idLista);
}

Lista.prototype.eliminar = function(idLista) {
	var request=new XMLHttpRequest();
	request.open("post", "cargarLista.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState==4) {
			var resultado=JSON.parse(request.responseText);
			if (resultado.tipo=="OK") {
				lista.idLista=idLista;
				lista.nombre=resultado.lista.nombre;
				lista.productos=resultado.lista.productos;
				lista.miembros=resultado.lista.miembros;
				lista.mostrar();
			} else if (resultado.tipo=="ERROR") {
				alert("Error al acceder al sistema: " + resultado.error);
			} 
		}
	}
	request.send("idLista=" + idLista);
}



Lista.prototype.cargarMiembros = function(idLista) {
	var request=new XMLHttpRequest();
	request.open("post", "cargarLista.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState==4) {
			var resultado=JSON.parse(request.responseText);
			if (resultado.tipo=="OK") {
				lista.idLista=idLista;
				lista.miembros=resultado.lista.miembros;
				lista.mostrarMiembros();
			} else if (resultado.tipo=="ERROR") {
				alert("Error al acceder al sistema: " + resultado.error);
			} 
		}
	}
	request.send("idLista=" + idLista);
}


Lista.prototype.mostrar = function() {
	tableLista.innerHTML="<div class='table-responsive'><table class='table table-bordered'><div class='col-xs-6 col-md-3'><thead><tr><th rowspan='2'>Producto</th><th colspan='2'>Unidades</th></tr><tr><th>Existencias</th><th>Deseadas</th></tr></div></thead></table></div>";
	tdNuevaLista.setAttribute("style", "display:visible");
	tdAddProducto.setAttribute("style", "display:visible");
	sessionStorage.idLista=this.idLista;
	for (var i=0; i<this.productos.length; i++) {
		var producto=this.productos[i];
		var tr=document.createElement("tr"); tableLista.appendChild(tr);
		if (producto.cantidadDeseada-producto.cantidadExistente>0) {
			tr.setAttribute("style", "background-color:red");
		} else {
			tr.setAttribute("style", "background-color:green");
		}
		var td=document.createElement("td"); tr.appendChild(td);
		td.innerHTML="<a href='javascript:lista.editarProducto(" + producto.idProducto + ")'>" + producto.nombre + "</a>";
		td=document.createElement("td"); tr.appendChild(td);
		td.innerHTML=producto.cantidadExistente;
		td=document.createElement("td"); tr.appendChild(td);
		td.innerHTML=producto.cantidadDeseada;
		td=document.createElement("td"); tr.appendChild(td);
		var btnHecho=document.createElement("button"); td.appendChild(btnHecho);
		btnHecho.innerHTML="Comprar producto";
		btnHecho.setAttribute("onclick", "lista.comprar(" + producto.idProducto + ")");
	}
		
		tdMiembros.setAttribute("style", "display:visible");
		var trMiembros=document.createElement("tr"); 
		tdMiembros.appendChild(trMiembros);
		var th=document.createElement("th"); trMiembros.appendChild(th);
		th.setAttribute("colspan", "4");
			for (var i=0; i<this.miembros.length; i++) {
				var trMiembro=document.createElement("tr"); tdMiembros.appendChild(trMiembro);
				var td=document.createElement("td"); trMiembro.appendChild(td);
				td.setAttribute("colspan", "4");
				td.innerHTML=this.miembros[i].email;
			
		}
}



Lista.prototype.editarProducto = function(idProducto) {
	var producto=null;
	for (var i=0; i<this.productos.length; i++) {
		if (this.productos[i].idProducto==idProducto) {
			producto=this.productos[i];
			break;
		}
	}
	var btnEditarProducto=document.getElementById("btnEditarProducto");
	btnEditarProducto.setAttribute("style", "display:visible");
	btnEditarProducto.setAttribute("onclick", "guardarProducto(" + idProducto + ")");
	document.getElementById("nombreProducto").value=producto.nombre;
	document.getElementById("cantidadDeseada").value=producto.cantidadDeseada;
	document.getElementById("cantidadExistente").value=producto.cantidadExistente;
}




Lista.prototype.crear = function(nombreLista) {
	var request=new XMLHttpRequest();
	request.open("post", "crearLista.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState==4) {
			var resultado=JSON.parse(request.responseText);
			if (resultado.tipo=="OK") {
				alert("Se ha creado la lista " + nombreLista);
				usuario.cargarUltimaLista(); //Cargar solo la ultima lista que se ha creado
			} else if (resultado.tipo=="ERROR") {
				alert("La lista " + nombreLista + " ya existe");
			} 
		}
	}
	request.send("nombreLista=" + nombreLista);
}


Usuario.prototype.cargarUltimaLista = function() {
	var request=new XMLHttpRequest();
	request.open("get", "cargarListas.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState==4) {
			var resultado=JSON.parse(request.responseText);
			if (resultado.tipo=="OK") {
				var listas=resultado.listas;
					for (var i=0; i<listas.length; i++) {
						if(i==0){
							panelListas.setAttribute("style", "display:visible");
							var lista=listas[i];
							var href=document.createElement("a"); //a es una referencia, un enlace
							panelListas.appendChild(href);
							href.setAttribute("href", "javascript:mostrar(" + lista.id + ")");
							href.innerHTML=lista.nombre;
							var br=document.createElement("br"); //br es espacio en blanco
							panelListas.appendChild(br);
						}
					}
			} else {
				alert("Error: " + resultado.texto);
			}
		}
	}
	request.send();
}



Lista.prototype.eliminar = function(idLista) {
	var request=new XMLHttpRequest();
	request.open("get", "cargarListas.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState==4) {
			var resultado=JSON.parse(request.responseText);
			if (resultado.tipo=="OK") {
				var listas=resultado.listas;
					for (var i=0; i<listas.length; i++) {
							panelListas.setAttribute("style", "display:visible");
							var lista=listas[i];
							var href=document.createElement("a"); //a es una referencia, un enlace
							panelListas.appendChild(href);
							href.setAttribute("href", "javascript:mostrar(" + lista.id + ")");
							href.innerHTML=lista.nombre;
							var br=document.createElement("br"); //br es espacio en blanco
							panelListas.appendChild(br);
						}
					}
			} else {
				alert("Error: " + resultado.texto);
			}
		}
	
	request.send();
}





Lista.prototype.comprar = function(idProducto) {
	var request=new XMLHttpRequest();
	request.open("post", "comprar.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState==4) {
			var resultado=JSON.parse(request.responseText);
			if (resultado.tipo=="OK") {
				lista.cargar(lista.idLista);
			} else {
				alert("Error: " + resultado.texto);
			}
		}
	}
	var p = {
			idLista : this.idLista,
			idProducto : idProducto
	};
	var linea="p=" + JSON.stringify(p);
	request.send(linea);	
}




Lista.prototype.insertarProducto = function() {
	var nombre=document.getElementById("nombreProducto").value;
	var cantidadDeseada=parseInt(document.getElementById("cantidadDeseada").value);
	var cantidadExistente=parseInt(document.getElementById("cantidadExistente").value); 

	var request=new XMLHttpRequest();
	request.open("post", "addProducto.jsp");
	request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	request.onreadystatechange = function() {
		if (request.readyState==4) {
			var resultado=JSON.parse(request.responseText);
			if (resultado.tipo=="OK") {
				lista.cargar(lista.idLista);
			} else {
				alert("Error: " + resultado.texto);
			}
		}
	}
	
	var p = {
			idLista : this.idLista, 
			nombre : nombre, 
			cantidadDeseada : cantidadDeseada, 
			cantidadExistente : cantidadExistente
	};
	var linea="p=" + JSON.stringify(p);
	request.send(linea);	
}

