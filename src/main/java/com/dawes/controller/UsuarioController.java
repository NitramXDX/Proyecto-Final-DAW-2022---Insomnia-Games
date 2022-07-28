package com.dawes.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.User;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dawes.Service.FileService;
import com.dawes.Service.PedidoService;
import com.dawes.Service.RoleService;
import com.dawes.Service.SeguridadService;
import com.dawes.Service.UsuarioService;
import com.dawes.modelo.PedidoVO;
import com.dawes.modelo.ProductoVO;
import com.dawes.modelo.UsuarioVO;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
	
	
			@Autowired
			private UsuarioService us;
			
			@Autowired
			private PedidoService ps;
			
			@Autowired
			private RoleService rs;
			
			@Autowired
			private SeguridadService ss;
			
			@Autowired
			private FileService fs;
			
			
			//ADMIN ROLE
			//Persistencia de de la clase
			@RequestMapping("/admin/persistir")
			public String persistir(@ModelAttribute UsuarioVO obj, Model modelo, MultipartFile imagen) {
				try {
					//Control de omisión del campo contraseña en el formulario
					if(obj.getPassword().isBlank()) {
						obj.setPassword(us.findById(obj.getIdusuario()).get().getPassword());
					} else {
						//Encriptado de la contraseña
						obj.setPassword(ss.encripta(obj.getPassword()));
					}	
					
					//Carga de una imagen para el avatar
					if(!imagen.isEmpty()) {
						//Borrado de la imagen anterior: Url + Nombre del archivo
						if (us.existsById(obj.getIdusuario())) fs.fileDelete("src/main/resources/static/images/userAvatars", us.findById(obj.getIdusuario()).get().getAvatar());
						//Método creado para salvar archivos
						fs.fileSave("src/main/resources/static/images/userAvatars", imagen, "avatar"+obj.getNick().replaceAll(" ", "").replaceAll("[^a-zA-Z0-9]", ""));
						//Se almacena en la base de datos el nombre de la imagen
						obj.setAvatar("avatar"+obj.getNick().replaceAll(" ","").replaceAll("[^a-zA-Z0-9]", "")+imagen.getOriginalFilename().substring(imagen.getOriginalFilename().lastIndexOf(".")));
					} else {
						//Control de omisión del campo imagen
						if(us.existsById(obj.getIdusuario()))
							obj.setAvatar(us.findById(obj.getIdusuario()).get().getAvatar()); 
						else
							obj.setAvatar("default.jpg");
					}
					
					//Persistencia
					if (!us.findById(obj.getIdusuario()).isEmpty()) {
						us.save(obj);
						String mensaje = "Usuario modificado.";
						return "redirect:/usuarios/admin/modificarForm?mensaje="+mensaje+"&id="+obj.getIdusuario();
					} else {
						us.save(obj);
						String mensaje = "Usuario '"+obj.getNick()+"' guardado.";
						return "redirect:/usuarios/admin/insertarForm?mensaje="+mensaje;
					}
				} catch (Exception e) {
					String error = "ERROR: Error en la persistencia.";
					if(e.getCause() instanceof ConstraintViolationException) {
						error = "ERROR: Usuario ya existente.";
						if (!us.findById(obj.getIdusuario()).isEmpty()) {
							return "redirect:/usuarios/admin/modificarForm?mensaje="+error+"&id="+obj.getIdusuario();
						}
					return "redirect:/usuarios/admin/insertarForm?mensaje="+error;
					}
					if(e.getCause() instanceof DataException) {
						error = "ERROR: Longitud de campo superada.";
						if (!us.findById(obj.getIdusuario()).isEmpty()) {
							return "redirect:/usuarios/admin/modificarForm?mensaje="+error+"&id="+obj.getIdusuario();
						}
						return "redirect:/usuarios/admin/insertarForm?mensaje="+error;	
					}
					return "redirect:/usuarios/admin/insertarForm?mensaje="+error;
				}
			}
			
			
			//Lista de todos los objetos de esta clase
			@RequestMapping("/admin")
			public String mostrar(@RequestParam Map<String, Object> params, Model modelo, String mensaje) {
				
				Map<String, Object> paginas = us.pagination(params, 6);
				
				modelo.addAttribute("usuarios", paginas.get("list"));
				modelo.addAttribute("pages", paginas.get("pages"));
				modelo.addAttribute("current", paginas.get("current"));
				modelo.addAttribute("next", paginas.get("next"));
				modelo.addAttribute("prev", paginas.get("prev"));
				modelo.addAttribute("last", paginas.get("last"));
				
				modelo.addAttribute("byNombre", Comparator.comparing(UsuarioVO::getNick));
				modelo.addAttribute("mensaje", mensaje);
				return "admin/usuarios/mostrar";
			}
			
			//Insertar un objeto (Form que luego persiste)
			@RequestMapping("/admin/insertarForm")
			public String insertar(Model modelo, String mensaje) {
				UsuarioVO nuevoUsuario= new UsuarioVO();
				nuevoUsuario.setRole(rs.findByNombre("USER").get()); //Se preestablece el role como USER (ESta causando el error)
				modelo.addAttribute("usuario", nuevoUsuario);
				modelo.addAttribute("pedidos", ps.findAll());
				modelo.addAttribute("roles", rs.findAll());
				modelo.addAttribute("mensaje", mensaje);
				return "admin/usuarios/insertarForm";
			}
			
			//Modificar un objeto (Form que luego persiste) 
			@RequestMapping("/admin/modificarForm")
			public String modificar(@RequestParam int id, Model modelo, String mensaje) {
				try {
					modelo.addAttribute("usuario",us.findById(id).get());
					modelo.addAttribute("pedidos", ps.findAll());
					modelo.addAttribute("roles", rs.findAll());
					modelo.addAttribute("mensaje", mensaje);
					return "admin/usuarios/modificarForm";
				} catch (Exception e) {
					String error = "ERROR: El usuario no existe.";
					return "redirect:/usuarios/admin?mensaje="+error;
				}
			}
			
			//Delete
			@RequestMapping("/admin/eliminar")
			public String delete(@RequestParam int id) {
				try {
					String mensaje = "Usuario "+us.findById(id).get().getNick()+" eliminado.";
					String nombreImg = us.findById(id).get().getAvatar();
					us.deleteById(id);
					fs.fileDelete("src/main/resources/static/images/userAvatars", nombreImg);
					return "redirect:/usuarios/admin?mensaje="+mensaje;
				} catch (Exception e) {
					String error = "ERROR: El usuario no existe.";
					if (e.getCause() instanceof ConstraintViolationException) {
						error = "ERROR: Este usuario cuenta con pedidos asignados.";
						return "redirect:/usuarios/admin?mensaje="+error;
					}
					return "redirect:/usuarios/admin?mensaje="+error;
				}
			}
			
			//Ver los pedidos de un usuario
			@RequestMapping("/admin/usuarioPedidos")
			public String usuarioPedidos(Model modelo, @RequestParam int idusuario, String mensaje) {
				try {
					//Para preestablecer el valor del idusuario del pedido
					PedidoVO pedido = new PedidoVO();
					pedido.setUsuario(us.findById(idusuario).get());
					modelo.addAttribute("pedido", pedido);
					
					modelo.addAttribute("usuario", us.findById(idusuario).get());
					
					//Lista ordenada de pedidos de un usuario
					List<PedidoVO> pedidos = us.findById(idusuario).get().getPedido();
					//Comparador para ordenar en función de un elemento de la clase y ordenado co Collections
					Collections.sort(pedidos, Comparator.comparing(PedidoVO::getFechacompra));
					//Orden inverso de la lista
					Collections.reverse(pedidos);
					modelo.addAttribute("pedidos", pedidos);
					
					modelo.addAttribute("mensaje", mensaje);
					
					return "admin/usuarios/usuarioPedidos";
				} catch (NoSuchElementException e) {
					String error = "ERROR: El usuario no existe.";
					return "redirect:/usuarios/admin?mensaje="+error;
				}
			}
			
			//USER ROLE PERMISSION
			@RequestMapping("/user/perfil")
			public String usuarioPerfil(Model modelo, Principal principal, String mensaje) {
				UsuarioVO usuario = us.findUsuarioVOByNick(principal.getName()).get();
				modelo.addAttribute("usuario", usuario);
				modelo.addAttribute("mensaje", mensaje);
				return "user/perfil";
			}
			
			//Ver los pedidos de un usuario
			@RequestMapping("/user/usuarioPedidos")
			public String usuarioPerfilPedidos(Model modelo, Principal principal, String mensaje) {
				try {
					UsuarioVO usuario =us.findUsuarioVOByNick(principal.getName()).get(); 
					modelo.addAttribute("usuario", usuario);
					
					//Lista ordenada de pedidos de un usuario
					List<PedidoVO> pedidos = us.findById(usuario.getIdusuario()).get().getPedido();
					//Comparador para ordenar en función de un elemento de la clase y ordenado co Collections
					Collections.sort(pedidos, Comparator.comparing(PedidoVO::getFechacompra));
					//Orden inverso de la lista
					Collections.reverse(pedidos);
					modelo.addAttribute("pedidos", pedidos);
					
					modelo.addAttribute("mensaje", mensaje);
					
					return "user/usuarioPedidos";
				} catch (Exception e) {
					return "404";
				}
			}
			
			@RequestMapping("/user/modUserPersistir")
			public String modUser(@ModelAttribute UsuarioVO obj, Model modelo, Principal principal, MultipartFile imagen, HttpServletRequest request, RedirectAttributes redirectAttrs) {
				try {
					//Identificamos el usuario que se va a modificar
					UsuarioVO usuario = us.findUsuarioVOByNick(principal.getName()).get();
					
					obj.setIdusuario(usuario.getIdusuario());
					obj.setNick(usuario.getNick());
					
					//Control de omisión del campo contraseña en el formulario
					if(obj.getPassword().isBlank()) {
						obj.setPassword(us.findById(obj.getIdusuario()).get().getPassword());
					} else {
						//Encriptado de la contraseña
						obj.setPassword(ss.encripta(obj.getPassword()));
					}	
	
					//Carga de una imagen para el avatar
					if(!imagen.isEmpty()) {
						//Borrado de la imagen anterior: Url + Nombre del archivo
						if (us.existsById(obj.getIdusuario())) fs.fileDelete("src/main/resources/static/images/userAvatars", us.findById(obj.getIdusuario()).get().getAvatar());
						//Método creado para salvar archivos
						fs.fileSave("src/main/resources/static/images/userAvatars", imagen, "avatar"+obj.getNick().replaceAll(" ", "").replaceAll("[^a-zA-Z0-9]", ""));
						//Se almacena en la base de datos el nombre de la imagen
						obj.setAvatar("avatar"+obj.getNick().replaceAll(" ","").replaceAll("[^a-zA-Z0-9]", "")+imagen.getOriginalFilename().substring(imagen.getOriginalFilename().lastIndexOf(".")));
					} else {
						//Control de omisión del campo imagen
						if(us.existsById(obj.getIdusuario()))
							obj.setAvatar(us.findById(obj.getIdusuario()).get().getAvatar()); 
						else
							obj.setAvatar("default.jpg");
					}
					
					obj.setRole(usuario.getRole()); //Añadimos el role por defecto
					
					us.save(obj);
					String mensaje = "Usuario modificado.";
					
					return "redirect:/usuarios/user/perfil?mensaje="+mensaje;
				} catch (Exception e) {
					String error = "ERROR: Error en la modificación.";
					if(e.getCause() instanceof ConstraintViolationException) {
						error = "ERROR: Nick, Email o Tlf ya registrado.";
						return "redirect:/usuarios/user/perfil?mensaje="+error;
					}
					if(e.getCause() instanceof DataException) {
						error = "ERROR: Longitud de campo superada.";
						return "redirect:/usuarios/user/perfil?mensaje="+error;
					}
					return "redirect:/usuarios/user/perfil?mensaje="+error;
				}
			}
			
			//ANONIMOUS
			@RequestMapping("/registroForm")
			public String usuarioregistro(Model modelo, String mensaje)  {
				modelo.addAttribute("usuario", new UsuarioVO());
				modelo.addAttribute("mensaje", mensaje);
				return "registro";
			}
			
			@RequestMapping("/anonimousPersistir")
			public String anonimousPersistir(@ModelAttribute UsuarioVO obj, Model modelo, MultipartFile imagen, HttpServletRequest request) {
				try {
					//Control de omisión del campo contraseña en el formulario
					if(obj.getPassword().isBlank()) {
						obj.setPassword(us.findById(obj.getIdusuario()).get().getPassword());
					} else {
						//Encriptado de la contraseña
						obj.setPassword(ss.encripta(obj.getPassword()));
					}	
					
					//Carga de una imagen para el avatar
					if(!imagen.isEmpty()) {
						//Borrado de la imagen anterior: Url + Nombre del archivo
						if (us.existsById(obj.getIdusuario())) fs.fileDelete("src/main/resources/static/images/userAvatars", us.findById(obj.getIdusuario()).get().getAvatar());
						//Método creado para salvar archivos
						fs.fileSave("src/main/resources/static/images/userAvatars", imagen, "avatar"+obj.getNick().replace(" ", ""));
						//Se almacena en la base de datos el nombre de la imagen
						obj.setAvatar("avatar"+obj.getNick().replace(" ","")+imagen.getOriginalFilename().substring(imagen.getOriginalFilename().lastIndexOf(".")));
					} else {
						//Control de omisión del campo imagen
						if(us.existsById(obj.getIdusuario()))
							obj.setAvatar(us.findById(obj.getIdusuario()).get().getAvatar()); 
						else
							obj.setAvatar("default.jpg");
					}
					
					//Persistencia
					obj.setStatus(true);
					obj.setRole(rs.findByNombre("USER").get());
					us.save(obj);
					String mensaje = "Tu cuenta ha sido creada. Te estamos esperando ;)"; 
					return "redirect:/login?mensaje="+mensaje;
					
				} catch (Exception e) {
					String error = "ERROR: Error en el registro.";
					if(e.getCause() instanceof ConstraintViolationException) {
						error = "ERROR: Nombre de usuario, Tlf o Email no disponible.";
						return "redirect:/usuarios/registroForm?mensaje="+error;
					}
					if(e.getCause() instanceof DataException) {
						error = "ERROR: Longitud de campo superada.";
						return "redirect:/usuarios/registroForm?mensaje="+error;
					}
					return "redirect:/usuarios/registroForm?mensaje="+error;
				}
			}
	
}
