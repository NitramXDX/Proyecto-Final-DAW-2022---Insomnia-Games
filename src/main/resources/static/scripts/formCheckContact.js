
//Expresiones regulares
const nombreApellidos = /^[\w'\-,.][^0-9_!¡?÷?¿/\\+=@#$%ˆ&*(){}|~<>;:[\]]{1,}$/; //nombre con acentos
const email = /^[^@]+@[^@]+\.[a-zA-Z]{2,}$/;
const textAreaContacto = /^[A-Za-z0-9]/; 


//Variables
var status = true; //Status de referencia para el envio de datos y para las validaciones
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
	
	switch (e.target.id){

	case "nombre":
		status = nombreApellidos.test(e.target.value) && e.target.value!='';
		msj = "ERROR: Campo 'Nombre y Apellidos' inválido."
		change(status, e.target, msj);
        break;
	
	case "email":
		status = email.test(e.target.value) && e.target.value!='';
		msj = "ERROR: Email incorrecto."
		change(status, e.target, msj);
        break;

	case "asunto":
		status = textAreaContacto.test(e.target.value) || e.target.value=='';
		msj = "ERROR: Asunto no válido."
		change(status, e.target, msj);
		break;
	
	case "mensaje":
		status = textAreaContacto.test(e.target.value) && e.target.value!='';
		msj = "ERROR: Mensaje no válido."
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
	if(status==='false'){
		event.preventDefault();
	}
	
}