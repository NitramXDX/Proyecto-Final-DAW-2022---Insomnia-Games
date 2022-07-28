package com.dawes.modelo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "plataformas")
public class PlataformaVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idplataforma;
	
	@Column(length = 45, unique = true)
	private String nombre;
	
	@Column(length = 100)
	private String imagen;
	
	//Lista de productos en esta plataforma
	@OneToMany(mappedBy = "plataforma")
	private List<ProductoVO> producto;
}
