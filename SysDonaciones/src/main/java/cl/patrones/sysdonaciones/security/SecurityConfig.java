package cl.patrones.sysdonaciones.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
			.formLogin(form -> form.loginPage("/").loginProcessingUrl("/ingresar").permitAll())
			.headers(h -> h.frameOptions(fo -> fo.sameOrigin()))
		;
		return httpSecurity.build();
	}

	@Bean
	UserDetailsService userDetailsService(PasswordEncoder encoder) {
		var roleVoluntario = "VOLUNTARIO";
		
		var user1 = User.builder()
				.username("josePerez")
				.password(encoder.encode("1234"))
				.roles( roleVoluntario )
				.build()
		;
		var user2 = User.builder()
				.username("sAndrade")
				.password(encoder.encode("4321"))
				.roles( roleVoluntario )
				.build()
		;
		var user3 = User.builder()
				.username("tSoto")
				.password(encoder.encode("6789"))
				.roles( roleVoluntario )
				.build()
		;
		var user4 = User.builder()
				.username("wAraya")
				.password(encoder.encode("9876"))
				.roles( roleVoluntario )
				.build()
		;
		return new InMemoryUserDetailsManager(user1, user2, user3, user4);
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
