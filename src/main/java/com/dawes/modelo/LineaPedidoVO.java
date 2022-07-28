package com.dawes.modelo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lineaspedidos")
public class LineaPedidoVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idlineapedido;
	
	@Column(length = 45)
	private String nombreprod;
	
	@Column(length = 45)
	private String plataforma;
	
	@Column(precision=6, scale=2)
	private Double importe;
	
	@Column(length = 100)
	private String imagen = "default.jpg";
	
	//Pedido al que pertenece la linea
	@ManyToOne()
	@JoinColumn(name = "idpedido")
	private PedidoVO pedido;
	
	//Ejemplar al que va asociada la linea
	@OneToOne()
	@JoinColumn(name = "idejemplar")
	private EjemplarVO ejemplar;
	
}
