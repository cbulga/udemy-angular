package com.xantrix.webapp.security;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service("customUserDetailsService")
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserConfig userConfig;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		if (userId == null || userId.length() < 2) {
			String errorMessage = "Nome utente assente o non valido";
			log.warn(errorMessage);
			throw new UsernameNotFoundException(errorMessage);
		}

		Utenti utente = this.getHttpValue(userId);

		if (utente == null) {
			String errorMessage = String.format("Utente %s non Trovato!!", userId);
			log.warn(errorMessage);
			throw new UsernameNotFoundException(errorMessage);
		}

		UserBuilder builder = null;
		builder = org.springframework.security.core.userdetails.User.withUsername(utente.getUserId());
		builder.disabled((utente.getAttivo().equals("Si") ? false : true));
		builder.password(utente.getPassword());

		String[] profili = utente.getRuoli().stream().map(a -> "ROLE_" + a).toArray(String[]::new);

		builder.authorities(profili);

		return builder.build();
	}

	protected Utenti getHttpValue(String userId) {
		URI url = null;

		try {
			String srvUrl = userConfig.getSrvUrl();
			url = new URI(srvUrl + userId);
		} catch (URISyntaxException e) {
			log.warn("Errors on URI to contact the authentication service", e);
		}

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userConfig.getUserId(), userConfig.getPassword()));

		Utenti utente = null;

		try {
			utente = restTemplate.getForObject(url, Utenti.class);
		} catch (Exception e) {
			log.warn("Connessione al servizio di autenticazione non riuscita!!");
		}

		return utente;
	}
}
