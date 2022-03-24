package com.springboot.panecillos.app.models.service;





public class UsuarioService {
	/*
	private final static Logger log=LoggerFactory.getLogger(UsuarioService.class);
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario=usuarioDao.findByUsername(username);
		if(usuario==null) {
			log.error("Error, no existe el usuario "+username+" en el sistema");
			throw new UsernameNotFoundException("Error, no existe el usuario "+username+" en el sistema");
		}
		List<GrantedAuthority> authorities=usuario.getRoles()
					.stream()
					.map(n-> new SimpleGrantedAuthority(n.getNombre()))
					.peek(authority-> log.info("Role: "+authority.getAuthority()))
					.collect(Collectors.toList());
		
		return new User(usuario.getUsername(), 
						usuario.getPassword(),
						usuario.getEnabled(), true, true, true, 
						authorities);
	}
	*/

}
