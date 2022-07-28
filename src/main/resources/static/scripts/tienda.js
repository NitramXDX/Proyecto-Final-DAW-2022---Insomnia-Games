
//Mostrar productos en las secciones indicadas, jutno con una función para especificar características

async function getGamesJSON() {
	const response = await fetch(`/api/productos/`);
	return await response.json();
}

async function getPlatformsJSON() {
	const response = await fetch(`/api/plataformas/`);
	return await response.json();
}

//Filter Inputs
const nombre = document.querySelector("#nombre");
const plataforma = document.querySelector("#plataforma");
const precioMin = document.querySelector("#precioMin");
const precioMax = document.querySelector("#precioMax");

//Actualización Filtros
if (document.querySelector("#nombre")) document.querySelector("#nombre").addEventListener("input", writeGamesMultiFiltered);
if (document.querySelector("#plataforma")) document.querySelector("#plataforma").addEventListener("input", writeGamesMultiFiltered);
if (document.querySelector("#precioMin")) document.querySelector("#precioMin").addEventListener("input", writeGamesMultiFiltered);
if (document.querySelector("#precioMax")) document.querySelector("#precioMax").addEventListener("input", writeGamesMultiFiltered);



 /*
 * String id: target div id
 * String param: json param to filter 
 * String value: param value
 * int size: number of items
 */
async function writeGames(id, size, param, value) {
	const section = document.querySelector("#"+id);
	if (!section) return "";
	
	const data = await getGamesJSON();
	let html = "";
	let count = size;
	
	if (param && value) {
		dataFiltered = data.filter((filtered) => ((filtered[param]).toLowerCase() == value.toLowerCase()));
	} 
	else{
		dataFiltered = await data;
	}
	
	for (let producto of dataFiltered.reverse()){
		html +=`
			<div class="item">
				<a href="/productos/detalle?nombre=${producto["nombre"]}&plataforma=${producto["plataforma"]}">
					<img class="productImg" src="/images/products/${producto["imagen"]}" alt="${producto["nombre"]}"/>
					<div class="discount"  style="${producto["descuento"]==0 ? 'display: none' : ''}">${'-'+producto["descuento"]+'%'}</div>
					<img class="platformImg" src="/images/platforms/${producto["plataformaImagen"]}">
				</a>
				<div class="productInfo">
					<div>${producto["nombre"]}</div>
					<div>${(producto["precio"] - (producto["precio"] * producto["descuento"]) / 100).toFixed(2)}&nbsp€</div>
				</div>
			</div>
		`;
		count--;
		if (count == 0) break;
	}
	
	section.innerHTML = html; 	
}

async function writeDiscountedGames(id, size) {
	const section = document.querySelector("#"+id);
	if (!section) return "";
	
	const data = await getGamesJSON();
	let html = "";
	let count = size;
	
	//Orden en función del valor del descuento
	data.sort(function (a, b) {
	  if (a["descuento"] > b["descuento"]) {
	    return -1;
	  }
	  if (a["descuento"] < b["descuento"]) {
	    return 1;
	  }
	  return 0;
	});
	
	
	for (let producto of data){
		html +=`
			<div class="item">
				<a href="/productos/detalle?nombre=${producto["nombre"]}&plataforma=${producto["plataforma"]}">
					<img class="productImg" src="/images/products/${producto["imagen"]}" alt="${producto["nombre"]}"/>
					<div class="discount"  style="${producto["descuento"]==0 ? 'display: none' : ''}">${'-'+producto["descuento"]+'%'}</div>
					<img class="platformImg" src="/images/platforms/${producto["plataformaImagen"]}">
				</a>
				<div class="productInfo">
					<div>${producto["nombre"]}</div>
					<div>${(producto["precio"] - (producto["precio"] * producto["descuento"]) / 100).toFixed(2)}&nbsp€</div>
				</div>
			</div>
		`;
		count--;
		if (count == 0) break;
	}
	
	section.innerHTML = html; 	
}

async function writeGamesMultiFiltered(size) {
	
	//Parámetros de búsqueda en la tienda
	let id = "games";
	let count = size;
	
	const section = document.querySelector("#"+id);
	if (!section) return "";
	
	const data = await getGamesJSON();
	let html = "";
	
	let dataFiltered = await data;
	
	dataFiltered = data
		.filter( result => (result["nombre"]).toLowerCase().replaceAll(" ","").includes(nombre.value.toLowerCase().replaceAll(" ","")))
		.filter( result => (result["plataforma"]).toLowerCase().replaceAll(" ","").includes(plataforma.value.toLowerCase().replaceAll(" ","")))
		.filter( result => ( parseFloat(result["precio"]) - parseFloat(result["precio"]*result["descuento"])/100) >= (precioMin.value))
		.filter( result => ( parseFloat(result["precio"]) - parseFloat(result["precio"]*result["descuento"])/100) <= (precioMax.value));
	
	for (let producto of dataFiltered.reverse()){
		html +=`
			<div class="item">
				<a href="/productos/detalle?nombre=${producto["nombre"]}&plataforma=${producto["plataforma"]}">
					<img class="productImg" src="/images/products/${producto["imagen"]}" alt="${producto["nombre"]}"/>
					<div class="discount"  style="${producto["descuento"]==0 ? 'display: none' : ''}">${'-'+producto["descuento"]+'%'}</div>
					<img class="platformImg" src="/images/platforms/${producto["plataformaImagen"]}">
				</a>
				<div class="productInfo">
					<div>${producto["nombre"]}</div>
					<div>${(producto["precio"] - (producto["precio"] * producto["descuento"]) / 100).toFixed(2)}&nbsp€</div>
				</div>
			</div>
		`;
		count--;
		if (count == 0) break;
	}
	
	section.innerHTML = html; 	
		

}


 /*
 * String id: target div id
 * int size: number of items
 */
async function writePlatforms(id, size) {
	const section = document.querySelector("#"+id);
	if (!section) return "";
	 
	const data = await getPlatformsJSON();
	
	let html = "";
	let count = size;
	
	for (let plataforma of data){
		html +=`
			<div class="platform">
				<a href="/tienda?filtro=${plataforma["nombre"]}">
					<img src="/images/platforms/${plataforma["imagen"]}" alt="${plataforma["nombre"]}"/>
				</a>
				<div class="productInfo">
					<div>${plataforma["nombre"]}</div>
				</div>
			</div>
		`;
		count--;
		if (count == 0) break;
	}
	
	section.innerHTML = html; 	
}


async function writeProdsImages(id, id1, id2) {
	const section = document.querySelector("#"+id);
	if (!section) return "";
	
	const data = await getGamesJSON();
	
	let html = "";
	html +=`
		<div id="${id1}">
    		<a href="/productos/detalle?nombre=${data[data.length - 1]["nombre"]}&plataforma=${data[data.length - 1]["plataforma"]}">
				<img src="/images/products/${data[data.length - 1]["imagen"]}" >
			</a>
    	</div>
    	
    	<div id="${id2}">
			<a href="/productos/detalle?nombre=${data[data.length - 2]["nombre"]}&plataforma=${data[data.length - 2]["plataforma"]}">
	    		<img src="/images/products/${data[data.length - 2]["imagen"]}" >
			</a>
    	</div>
	`;
	section.innerHTML = html; 
}

//Juegos
writeGames("newGames", 6);

//Juegos: Mayores ofertas
writeDiscountedGames("discountGames", 6);

//Solo imágenes
writeProdsImages("headerImg", "left", "right");

//Plataformas
writePlatforms("platforms", 6);

//Juegos: Filtrados Tienda
writeGamesMultiFiltered(35);

