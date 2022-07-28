package com.dawes.ServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dawes.Service.UsuarioService;
import com.dawes.modelo.UsuarioVO;
import com.dawes.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService,UserDetailsService {
	@Autowired
	UsuarioRepository ur;

	@Override
	public <S extends UsuarioVO> S save(S entity) {
		return ur.save(entity);
	}

	@Override
	public Optional<UsuarioVO> findById(Integer id) {
		return ur.findById(id);
	}

	@Override
	public boolean existsById(Integer id) {
		return ur.existsById(id);
	}

	@Override
	public Iterable<UsuarioVO> findAll() {
		return ur.findAll();
	}

	@Override
	public long count() {
		return ur.count();
	}

	@Override
	public void deleteById(Integer id) {
		ur.deleteById(id);
	}
	
	public Map<String, Object> pagination(Map<String, Object> params, int itemPerPage) {
		return ur.pagination(params, itemPerPage);
	}

	//Seguridad
	@Override
	public UserDetails loadUserByUsername(String nick) throws UsernameNotFoundException {
		
		UsuarioVO u = ur.findUsuarioVOByNick(nick).get();
		if (u == null) {
			System.out.println("El usuario "+nick+" no existe!");
			throw new UsernameNotFoundException("El usuario "+nick+" no existe!");
		}
		
		//Identificar Usuarios Baneados
		if (!u.isStatus()) {
			System.out.println("El usuario "+nick+" ha sido baneado!");
			throw new UsernameNotFoundException("El usuario "+nick+" ha sido baneado!");
		}
		
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		if (u.getRole()!=null) {
				GrantedAuthority authority = new SimpleGrantedAuthority(u.getRole().getNombre());
				grantList.add(authority);
		}
		
		UserDetails userDetails =  (UserDetails) new User(u.getNick(), 
		u.getPassword(), grantList);
		
		System.out.println(userDetails);
		
		return userDetails;
	}
	
	
	
	//Métodos Personalizados
	@Override
	public Optional<UsuarioVO> findUsuarioVOByNick(String nick) {
		return ur.findUsuarioVOByNick(nick);
	}

	
}
