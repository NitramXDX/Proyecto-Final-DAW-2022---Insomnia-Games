
//Expresiones regulares
const username = /^[A-Za-z0-9]*$/; //Letras, numeros convencionales con espacios y caracteres especiales
const nombreApellidos = /^[\w'\-,.][^0-9_!¡?÷?¿/\\+=@#$%ˆ&*(){}|~<>;:[\]]{1,}$/; //nombre con acentos
const email = /^[^@]+@[^@]+\.[a-zA-Z]{2,}$/;
const pass = /^[\S]+$/;
const tlf = /(\+34|0034|34)?[ -]*(6|7)[ -]*([0-9][ -]*){8}/;
var status = false; //Status de referencia para el envio de datos y para las validaciones
var mensaje2 = document.querySelector("#error");
var mensaje = document.querySelector("#error2");
var msj = ''; //Mensaje de error

//Submit Button
const submit = document.querySelector("input[type=submit]");

//Identificación de campos de formulario
const inputs = document.querySelectorAll("form input");

if (inputs) 
	for (let input of inputs) {
        input.addEventListener("blur", check);
    }

const textareas = document.querySelectorAll("form textarea");
if (textareas) 
	for (let textarea of textareas) {
        textarea.addEventListener("blur", check);
    }

//Identificación (Switch)
function check(e) {
	//Arreglo de dobles espacios y espacios laterales para todos los mensajes
	e.target.value = e.target.value.replace(/\s\s+/g, " ").trim(); 
//	mensaje2.style.display = "none";
//	mensaje.style.display = 'none';
	
	switch (e.target.id){		
		
	case "nombre":
		status = nombreApellidos.test(e.target.value) && e.target.value!='';
		msj = "ERROR: Campo 'Nombre' inválido."
		change(status, e.target, msj);
        break;

	case "apellidos":
		status = nombreApellidos.test(e.target.value) && e.target.value!='';
		msj = "ERROR: Campo 'Apellidos' inválido."
		change(status, e.target, msj);
        break;

  	case "nick":
		status = username.test(e.target.value) && e.target.value!='';
		msj = "ERROR: Nombre de Usuario inválido."
		change(status, e.target, msj);
        break;

	case "password":
		status = pass.test(e.target.value) && e.target.value!='';
		msj = "ERROR: Contraseña no válida."
		change(status, e.target, msj);
        break;
	
	case "password2":
		status = pass.test(e.target.value) && e.target.value!='' && e.target.value==document.querySelector("#password").value;
		msj = "ERROR: La contraseña no coincide."
		change(status, e.target, msj);
        break;
	
	case "tlf":
		status = tlf.test(e.target.value) && e.target.value!='' && (e.target.value.length==9 || e.target.value.length==12) ;
		msj = "ERROR: Número de teléfono inválido."
		change(status, e.target, msj);
        break;

	case "email":
		status = email.test(e.target.value) && e.target.value!='';
		msj = "ERROR: Email incorrecto."
		change(status, e.target, msj);
        break;
	}
}

//Cambio de colores
function change(status, target, error) {
	if (status === 'false') {
        	target.style.border = "solid 2px rgb(255, 34, 34)";
			mensaje.style.display='inline-block';
			mensaje.textContent = error;
			mensaje.style.color = 'red';
        } else {
			target.style.border = "none";
//			mensaje.style.display='none';
			mensaje.textContent = '';
        }
}

//Corte de funcionamiento estandar y envio
submit.addEventListener("click",sendInfo);

function sendInfo() {
	if(status==='false' || document.querySelector("#password").value != document.querySelector("#password2").value){
		
		if (document.querySelector("#password").value != document.querySelector("#password2").value) mensaje.textContent = "ERROR: Las contraseñas no coinciden.";
		
		event.preventDefault();
	}
	
}