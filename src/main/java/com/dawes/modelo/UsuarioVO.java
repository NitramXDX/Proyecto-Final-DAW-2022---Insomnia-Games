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
@Table(name ="usuarios")
public class UsuarioVO {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idusuario;
	@Column(length = 150)
	private String avatar = "default.jpg";
	@Column(length = 45)
	private String nombre;
	@Column(length = 100)
	private String apellidos;
	@Column(length = 15, unique = true) 
	private String nick; //A los usuarios se identifican por el nick, una persona puede tener más de una cuenta creada
	
	@Column(length = 300)
	private String password;
	
	@Column(length = 12)
	private String tlf;
	@Column(length = 45, unique = true)
	private String email;
	
	private boolean privado = false;
	
	private boolean status = true;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	LocalDate fecharegistro = LocalDate.now();
	
	//Lista de pedidos del usuario
	@OneToMany(mappedBy = "usuario")
	private List<PedidoVO> pedido;
	
	//Roles
	@ManyToOne()
	@JoinColumn(name="idrole")
	private RoleVO role;
	
}
