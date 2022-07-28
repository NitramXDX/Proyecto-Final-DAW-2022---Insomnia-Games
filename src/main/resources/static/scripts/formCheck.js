
//Expresiones regulares
const letrasNumeros = /^[A-Za-z0-9\s:/()?¿¡!]*$/; //Letras y numeros convencionales con espacios
const precio = /^(\d+\.)?\d+$/;	//Precio con coma, sin negativos
const descuento = /^[0-9][0-9]?$|^99$/; //Sin negativos y sin decimales
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
//	mensaje2.style.display = "none";
//	mensaje.style.display = 'none';
	
	switch (e.target.id){
		
	case "nombre":
		status = letrasNumeros.test(e.target.value) && e.target.value!='';
		msj = "ERROR: Campo 'Nombre' incorrecto."
		change(status, e.target, msj);
        break;

  	case "precio":
		
		status = precio.test(e.target.value) && e.target.value!='' && e.target.value>=1;
		msj = "ERROR: Precio incorrecto."
		change(status, e.target, msj);
        break;

	case "descuento":
		status = e.target.value!='' && descuento.test(e.target.value);
		msj = "ERROR: Descuento incorrecto."
		change(status, e.target, msj);
        break;
	
	case "resumen":
		status = e.target.value!='';
		msj = "ERROR: El Resumen es necesario."
		change(status, e.target, msj);
        break;
	
	case "sinopsis":
		status = e.target.value!='';
		msj = "ERROR: La Sinopsis es necesaria."
		change(status, e.target, msj);
        break;

	case "fechasalida":
		status = e.target.value!='';
		msj = "ERROR: Fecha errónea."
		change(status, e.target, msj);
		break;
		
	case "clave":
		status = e.target.value!='';
		msj = "ERROR: Clave errónea."
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