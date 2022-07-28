
	//Carga de los productos en caso de que existan en el localStorage
	//	localStorage.setItem("prods", null);
	let carritoContent = localStorage.getItem("prods");
	if(carritoContent != null) {
		writeCartItems(JSON.parse(carritoContent));
	}
	
	// Carrito
    const carritoButton = document.querySelector("#cart");
    const carrito = document.querySelector(".carrito");
	if (carritoButton) carritoButton.addEventListener("click", openCloseCart);
    var carritoStatus = 0;


    function openCloseCart() {
        if (carritoStatus == 0) {
            carrito.style.display = "block";
            carritoButton.style.color = "#F47119";
            carritoStatus = 1;
        } else {
            carrito.style.display = "none";
            carritoButton.style.color = "white";
            carritoStatus = 0;
        }
    }

	//Añadir producto al carrito
	const buttonAdd = document.querySelector("#addToCart");
	const bodyCart = document.querySelector("#bodyCart");
	if(buttonAdd) buttonAdd.addEventListener("click", addProduct);
	
	//Desactivado de boton de compra
	const buttonShop = document.querySelector(".buttonShop");
	if(buttonShop && bodyCart.innerHTML == "Su carrito está vacío"){
		buttonShop.parentElement.href='#';
	}
	else {
		buttonShop.parentElement.href='/pedidos/user/tramitar';
	}
	
	async function addProduct() {
				
		let precio = document.querySelector("#precio").textContent; 
		
		precio = precio - (precio * (document.querySelector("#descuento") ? document.querySelector("#descuento").textContent : 0)) / 100;
		
		if (bodyCart.innerHTML == "Su carrito está vacío")
                bodyCart.innerHTML = "";

		const producto = {
			"imagen" : document.querySelector("#imagen").getAttribute("src"),
			"nombre" : document.querySelector("#nombre").textContent,
			"plataforma" : document.querySelector("#plataforma").textContent,
			"precio" : precio.toFixed(2),
			"cantidad" : 1,
		};
		
		//Comprobación y suma de cantidad al añadir el mismo producto
		for (let item of document.querySelectorAll(".detalleCart")) 
			if (item.textContent.includes(`${producto["nombre"]}`) && item.textContent.includes(`${producto["plataforma"]}`)){
				item.querySelector("input").value++;
				actPrices();
				return;
			} 
		
		const productos = []; //Añadir los productos previos ? aunque igual no porque ya tendrían que estar en el carrito los necesarios, innerhtml
		productos.push(producto);
			
		writeCartItems(productos);
	}
	
	//Escritura de productos en el carrito
	async function writeCartItems(newProds) {
		
		if(newProds==null) return; //Evita errores
		
		
		let bodyCart = document.querySelector("#bodyCart");
		
		if(!bodyCart) return;
		
		if (bodyCart.innerHTML == "Su carrito está vacío")
                bodyCart.innerHTML = "";
		for (let articulo of newProds){
			bodyCart.innerHTML += `
					<div class="articuloCart">
				<img class="imgCart" src="${articulo["imagen"]}" alt="${articulo["nombre"]}"">
				<div class="detalleCart">
					<p class="nombre">${articulo["nombre"]}</p>
					<p class="plataforma">${articulo["plataforma"]}</p>  
					<div class="infoPrecio">
						<p class="cartPrecio"><span>${articulo["precio"]}</span>&nbsp€</p>
						<div class="botones">
							<button class="resta">-</button>
							<input type="text" maxlength="2" value="${articulo["cantidad"]}">
							<button class="suma">+</button>
						</div>
						<p class="cartPrecioTotal"><span>${articulo["precio"]}</span>&nbsp€</p><i class="fa fa-trash-alt" aria-hidden="true"></i>
					</div>
				</div>
			</div>
			`;
		}
		
		actEvents();
		actPrices();
	}
	
	// Asignación de eventos para los nuevos items
	async function actEvents() {
		for (let item of document.querySelectorAll(".suma"))
			item.addEventListener("click", suma);
		for (let item of document.querySelectorAll(".resta"))
			item.addEventListener("click", resta);
    	for (let item of document.querySelectorAll(".fa-trash-alt"))
            item.addEventListener("click", selectBorrar);
	}	

	//Actualizacion de precios y valores numericos
	async function actPrices() {
		let carritoActual = document.querySelectorAll(".detalleCart");
		let importeFinal = 0;
		var cartCount = 0;
		for (let item of carritoActual) { 
			item.querySelector(".cartPrecioTotal>span").textContent = (item.querySelector(".cartPrecio>span").textContent * item.querySelector("input").value).toFixed(2);
			importeFinal = parseFloat(importeFinal) + parseFloat(item.querySelector(".cartPrecioTotal>span").textContent);
			cartCount = parseInt(cartCount) + parseInt(item.querySelector("input").value);
		}
		//Importe general del carrito
		if (!importeFinal) importeFinal= "0.00";
		document.querySelector(".precioTotal").textContent = parseFloat(importeFinal).toFixed(2);
		
		//Cantidad actual en el carrito
		let buttonShop = document.querySelector(".buttonShop");
		if (cartCount){
			document.querySelector("#cartCount").textContent = cartCount;
			buttonShop.parentElement.href='/pedidos/user/tramitar';
		}
		else {
			document.querySelector("#cartCount").textContent = 0;
			buttonShop.parentElement.href='#';
		}
			
		
		//Guardado del carrito en el localStorage
		let articulos='';
		for (let item of document.querySelectorAll(".articuloCart")){
			articulos += `{
				"imagen" : "${item.querySelector("img").src}",
				"nombre" : "${item.querySelector(".nombre").textContent}",
				"plataforma" : "${item.querySelector(".plataforma").textContent}",
				"precio" : "${item.querySelector(".cartPrecio > span").textContent}",
				"cantidad" : "${item.querySelector(".botones > input").value}"
			},`;
		}
		
		if (articulos!=''){
			articulos = "[" + articulos;
			articulos = articulos.substring(0, articulos.length - 1) + "]";
			localStorage.setItem('prods', articulos);
		}
	
	}
	
	//Suma de un item
	async function suma(e) {
		if(e.target.previousElementSibling.value >= 99){  e.target.previousElementSibling.value = 99; return; };
		if(e.target.previousElementSibling.value < 1){  e.target.previousElementSibling.value = 1; return; };
		e.target.previousElementSibling.value++;
		actPrices();
	}
	
	//Resta de un item
	async function resta(e) {
		if(e.target.nextElementSibling.value <= 1){  e.target.nextElementSibling.value = 1; return; };
		e.target.nextElementSibling.value--;
		actPrices();
	}
	
	//Borrar producto del carrito
    const popUpSi = document.querySelector("#si");
    const popUpNo = document.querySelector("#no");
	if(popUpSi) popUpSi.addEventListener("click", siPopUpBorrar);
    if(popUpNo) popUpNo.addEventListener("click", noPopUp);
	var elementoBorrar; //Variable para almacenar el producto a borrar	
	
	async function selectBorrar(e) {
        document.querySelector("#velo").style.display = "flex";
        document.querySelector("#popUp").style.display = "flex";
		
        //Crear referencia al elemento a borrar
        elementoBorrar = e.target.parentElement.parentElement.parentElement;
    }
	
    async function siPopUpBorrar() {
        elementoBorrar.remove();
        if (document.querySelector("#bodyCart").childElementCount == 0) {
            document.querySelector("#bodyCart").textContent = "Su carrito está vacío";
			localStorage.clear();
        }
        actPrices();
        document.querySelector("#velo").style.display = "none";
        document.querySelector("#popUp").style.display = "none";
    }

    async function noPopUp() {
        event.preventDefault();
        document.querySelector("#velo").style.display = "none";
        document.querySelector("#popUp").style.display = "none";
    }

	//Colores de los mensajes
	const error= document.querySelector("#error");
	
	if (error){
		 if (error.textContent.substring(0,5).toUpperCase()=="ERROR"){ 
			error.style.color = "red";
		}
		else if (error.textContent.substring(0,5).toUpperCase()!="ERROR") error.style.color="green";
	}

	//Ocultar contenido de tablas vacias
	const tables = document.querySelectorAll("tbody");	
	
	if(tables)
		for (table of tables){
			if(table.childElementCount<=1) {
				let newContent = '';
				let count = table.querySelector("tr").childElementCount;
				for(let i ; count>0 ; count--){
					newContent += '<td></td>';
				}
				table.innerHTML += '<tr>'+newContent+'</tr>';
			}	
		}
		
	//Barra de búsqueda
	async function getGamesJSON() {
		const response = await fetch(`/api/productos/`);
		return await response.json();
	}
	
	const buscador = document.querySelector("#buscador input");
	buscador.addEventListener("input", search);

	const resultadoDiv = document.querySelector("#resultado-buscador");
	
	async function search(e) {
	    var lineas = 4;
		var limit = 24;
	    var buscadorValue = e.target.value;
		var data = await getGamesJSON();
		
	    //Se vacia la busqueda anterior
	    resultadoDiv.textContent = "";
	    for (child of resultadoDiv.querySelectorAll("p")) {
	        child.remove();
	    }

	    for (producto of data) {
	
	        //Solo muestra las 3 primeras coincidencias, si el campo no contiene nada no muestra nada, no contempla los espacios
	        if (producto["nombre"].toUpperCase().replaceAll(" ", "").includes(buscadorValue.toUpperCase().replaceAll(" ", "")) && e.target.value != "" && lineas > 0 && buscadorValue.replaceAll(" ", "") != "") {
	
				let fixedName = '';
				
	            if (producto["nombre"].length > limit) {
	                fixedName = producto["nombre"].substring(0, limit - 3).concat("...");
	            } else {
	                fixedName = producto["nombre"];
	            }
				
				

				//Asignacion del elemento
				let item = document.createElement("a");
				item.href=`/productos/detalle?nombre=${producto["nombre"]}&plataforma=${producto["plataforma"]}`;
				item.title = `${producto["nombre"]}`;
				
				let html = `
				<div>
					<div>
						<img src="/images/products/${producto["imagen"]}"></img>
					</div>
					<div>
						<p>${fixedName}</p>
						<p>${producto["plataforma"]}</p>
						<p><span class="strikethrough">${producto["precio"]}€</span><span class="discountResultado">- ${producto["descuento"]}%</span><span class="finalPrice">${(producto["precio"]-(producto["precio"] * producto["descuento"])/100).toFixed(2)}€</span></p>
					</div>
				</div>
				`; 
				
				item.innerHTML = html;
				
	            resultadoDiv.appendChild(item);


	            lineas--;
	        }
	    }
    }

		
	



