package com.dawes.modelo;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "ejemplares")
@AllArgsConstructor
@NoArgsConstructor
public class EjemplarVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idejemplar;
	
	@Column(length = 100, unique = true)
	private String clave;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechainserc = LocalDate.now();
	
	//producto al que pertenece
	@ManyToOne()
	@JoinColumn(name = "idproducto")
	private ProductoVO producto;
	
	//Linea de pedido al que pertenece
	@OneToOne(mappedBy = "ejemplar")
	private LineaPedidoVO lineapedido;
	
}
