
//Mostrar en una sección los productos de la compra
//Recibimos los productos del carrito
const prods = localStorage.getItem("prods");	//Productos en localStorga
const prodList = document.querySelector("#prodsList");  //Lista de productos en la tabla
const cart = document.querySelector("#cart");	//Productos que contiene el carrito

if(cart) cart.style.display = "none";

if(prods==null) {
			document.querySelector("#section").innerHTML = `
								<div id="content">
									<h1>Su carrito está vacío</h1>
									<img id="emptyCartImg" src="/images/resources/emptycart.png"/>
								</div>
								`;	
}			
	
if(prods!=null) makeList(prods);

async function makeList(prods) {
	
	//Pintado de la lista de productos
	for (let item of JSON.parse(prods)) {
		prodList.innerHTML += `
			<tr>
				<td>
					<div id="imgContainerSquare">
						<img src="/images/products/${item["imagen"].substring(item["imagen"].lastIndexOf("/")+1,item["imagen"].length)}"></img>
					</div>
				</td>
				<td><a href="/productos/detalle?nombre=${item["nombre"]}&plataforma=${item["plataforma"]}">${item["nombre"]}</a></td> 
				<td>${item["plataforma"]}</td> 
				<td>${item["precio"]}€</td> 
				<td>${item["cantidad"]} ${item["cantidad"]==1 ? 'ud' : 'uds'}</td> 
				<td>${(item["precio"]*item["cantidad"]).toFixed(2)}€</td>
			</tr>
		` ;
	}
	
	let total = 0;
	for (let item of JSON.parse(prods)) {
		total += parseFloat(item["precio"])*item["cantidad"];
	}
	
	
	document.querySelector("#totalPrice").textContent = total.toFixed(2) + `€`;
}


const confirmarCompra = document.querySelector("#confirmarCompra");

if (confirmarCompra) confirmarCompra.addEventListener("click", tramitar);

async function tramitar() {
	
    if(prods==null) return;
	
	let success = `
		<div class="exito"> 
			<h1>PEDIDO PROCESADO CON ÉXITO</h1>
			<h2>"Gracias por confiar en nosotros"</h2>
			<img src="/images/resources/compraRealizada.png"></img>
			<a href="/usuarios/user/usuarioPedidos"><h2>Historial de pedidos</h2></a>
		</div>
	`;
	let json = `{"lineaspedido": ${prods} }`; 
	
	fetch("/api/pedidos/user/persistencia", {
	    method: "POST",
	    body: json,
	    headers: {
	        "Content-type": "application/json; charset=UTF-8"
	    }
	})
	.then(response => response.json())
	.then(localStorage.clear())
	.then(document.querySelector("#section").innerHTML=success);
	
}

