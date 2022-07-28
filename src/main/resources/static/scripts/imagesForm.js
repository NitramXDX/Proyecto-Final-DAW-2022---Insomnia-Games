//Cambio de Foto en el formulario
const fileInput = document.querySelector("#file");

if(fileInput) fileInput.addEventListener("change", setPhoto);

function setPhoto(e) {
    let fileInput = document.querySelector("#file");
    let photo = document.querySelector("form img");
 	let img = URL.createObjectURL(fileInput.files[0]); 
	photo.src=img;
}