package com.dawes.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineaPedidoDTO {
	private int cantidad;
	private String imagen;
	private String nombre;
	private String plataforma;
	private Double precio;
}
