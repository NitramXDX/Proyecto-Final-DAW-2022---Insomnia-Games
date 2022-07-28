package com.dawes.modelo;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "productos",uniqueConstraints = { @UniqueConstraint( columnNames = { "nombre", "idplataforma" } ) } ) 
//No puede haber un producto con la misma conbinación de plataforma y producto

public class ProductoVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idproducto;
	
	@Column(length = 45)
	private String nombre;
	
	@Column(precision=6, scale=2)
	private Double precio;
	
	@Column(precision=3)
	private int descuento = 0;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechasalida = LocalDate.now();
	
	@Column(length = 1000)
	private String sinopsis;
	
	@Column(length = 1000)
	private String resumen;
	
	@Column(length = 100)
	private String imagen = "default.jpg";
	
	//Plataforma a la que pertenece este producto
	@ManyToOne()
	@JoinColumn(name = "idplataforma")
	private PlataformaVO plataforma;
	
	//Lista de ejemplares de este producto
	@OneToMany(mappedBy = "producto")
	private List<EjemplarVO> ejemplar; 
	
	
}
