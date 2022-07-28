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

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pedidos")
public class PedidoVO {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idpedido;  //Se identifica cada pedido por esta clave

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechacompra = LocalDate.now();
	
	@Column(length=10)
	private String estado = "Pendiente"; //Tres estados: Pendiente - En Trámite - Resuelto 

	@Column(precision=6, scale=2)
	private Double importe;
	
	//Lista de lineaspedidos que contiene el pedido
	@OneToMany(mappedBy = "pedido")
	private List<LineaPedidoVO> lineapedido;
	
	//Usuario al que pertenece el pedido
	@ManyToOne()
	@JoinColumn(name = "idusuario")
	private UsuarioVO usuario;

}
