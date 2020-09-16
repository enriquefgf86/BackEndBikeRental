package com.example.bikestore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;

@SpringBootApplication
public class BikestoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BikestoreApplication.class, args);
	}
	@Bean
	public PasswordEncoder passwordEncoder(){
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public CommandLineRunner initData(BikeTypeRepository bikeTypeRepository,UserRepository userRepository,
									  BikeRepository bikeRepository, InventoryBikeRepository inventoryBikeRepository,
									  RentBikeRepository rentBikeRepository) {
		return (args) -> {
            User user1=new User("Ramon Garcia",passwordEncoder().encode("ramon"),"ramon@yahoo.com","unactive");
			User user2=new User("Luna Vidal",passwordEncoder().encode("vidal"),"vidal@yahoo.com","unactive");
			User user3=new User("Gor Van ",passwordEncoder().encode("van"),"van@yahoo.com","unactive");
			User user4=new User("Rio Ferdinand",passwordEncoder().encode("rio"),"rio@yahoo.com","unactive");
			User user5=new User("Shaka Sankofa",passwordEncoder().encode("shaka"),"shaka@yahoo.com","unactive");
			User user6=new User("Marshall Matters",passwordEncoder().encode("eminem"),"eminem@yahoo.com","unactive");

			userRepository.save(user1);
			userRepository.save(user2);
			userRepository.save(user3);
			userRepository.save(user4);
			userRepository.save(user5);
			userRepository.save(user6);

			BikeType bikeType1=new BikeType("montanesa",12.00);
			BikeType bikeType2=new BikeType("normal",10.00);

			bikeTypeRepository.save(bikeType1);
			bikeTypeRepository.save(bikeType2);

			Bike bike1=new Bike(bikeType1,
//					new HasMap<>(Arrays.asList(rentBike2,rentBike14)),
					Arrays.asList(user6,user1),"https://s14761.pcdn.co/wp-content/uploads/2020/01/Nukeproof-Reactor-290C-RS-Review-Test-Trail-Bike-Enduro-LR1387-1140x760.jpg","formance in rough terrain being built to handle these types of terrain and features");
			Bike bike2=new Bike(bikeType1,
//					new HashSet<>(Arrays.asList(rentBike5,rentBike8,rentBike11)),
					Arrays.asList(user2,user3,user5),"https://www.expocafeperu.com/w/2020/01/specialized-mountain-bike-frames-29-inch-mountain-bike-frames-sale-intense-mountain-bike-frames-for-sale-freeride-mountain-bike-frames.jpg","rrain being built to handle these types of terrain and features");
			Bike bike3=new Bike(bikeType2,
//					new HashSet<>(Arrays.asList(rentBike1,rentBike3,rentBike4,rentBike13)),
					Arrays.asList(user1,user1,user5,user2),"https://i.pinimg.com/originals/d1/85/1a/d1851accc94d208813cdf9635124d2f4.jpg","eing built to handle these types of terrain and features");
			Bike bike4=new Bike(bikeType2,
//					new HashSet<>(Arrays.asList(rentBike6,rentBike7,rentBike9)),
					Arrays.asList(user3,user3,user4),"https://image.freepik.com/foto-gratis/bicicleta-ciudad-blanca-pared-ladrillo_23-2148232084.jpg","o handle these types of terrain and features");
			Bike bike5=new Bike(bikeType2,
//					new HashSet<>(Arrays.asList(rentBike10,rentBike12,rentBike15)),
					Arrays.asList(user5,user5,user6),"https://rentandrollmadrid.com/wp-content/uploads/2020/01/inicio-paseo.jpg"," these types of terrain and features");

			bikeRepository.save(bike1);
			bikeRepository.save(bike2);
			bikeRepository.save(bike3);
			bikeRepository.save(bike4);
			bikeRepository.save(bike5);

			RentBike rentBike1=new RentBike(user1,bike1,3,4,3.00,43.00,"finished");
			RentBike rentBike2=new RentBike(user1,bike1,2,2,0.00,24.00,"finished");
			RentBike rentBike3=new RentBike(user1,bike1,1,2,3.00,23.00,"finished");
			RentBike rentBike4=new RentBike(user2,bike3,3,4,3.00,43.00,"finished");
			RentBike rentBike5=new RentBike(user2,bike2,2,2,0.00,24.00,"finished");
			RentBike rentBike6=new RentBike(user3,bike4,1,2,3.00,23.00,"finished");
			RentBike rentBike7=new RentBike(user3,bike4,3,4,3.00,43.00,"finished");
			RentBike rentBike8=new RentBike(user3,bike2,2,2,0.00,24.00,"finished");
			RentBike rentBike9=new RentBike(user4,bike4,1,2,3.00,23.00,"finished");
			RentBike rentBike10=new RentBike(user5,bike5,3,4,3.00,43.00,"finished");
			RentBike rentBike11=new RentBike(user5,bike2,2,2,0.00,24.00,"finished");
			RentBike rentBike12=new RentBike(user5,bike5,1,2,3.00,23.00,"finished");
			RentBike rentBike13=new RentBike(user5,bike3,3,4,3.00,43.00,"finished");
			RentBike rentBike14=new RentBike(user6,bike1,2,2,0.00,24.00,"finished");
			RentBike rentBike15=new RentBike(user6,bike5,1,2,3.00,23.00,"finished");

			rentBikeRepository.save(rentBike1);
			rentBikeRepository.save(rentBike2);
			rentBikeRepository.save(rentBike3);
			rentBikeRepository.save(rentBike4);
			rentBikeRepository.save(rentBike5);
			rentBikeRepository.save(rentBike6);
			rentBikeRepository.save(rentBike7);
			rentBikeRepository.save(rentBike8);
			rentBikeRepository.save(rentBike9);
			rentBikeRepository.save(rentBike10);
			rentBikeRepository.save(rentBike11);
			rentBikeRepository.save(rentBike12);
			rentBikeRepository.save(rentBike13);
			rentBikeRepository.save(rentBike14);
			rentBikeRepository.save(rentBike15);


			InventoryBike inventoryBike1=new InventoryBike(bike1,false);
			InventoryBike inventoryBike2=new InventoryBike(bike2,false);
			InventoryBike inventoryBike3=new InventoryBike(bike3,false);
			InventoryBike inventoryBike4=new InventoryBike(bike4,false);
			InventoryBike inventoryBike5=new InventoryBike(bike5,false);

			inventoryBikeRepository.save(inventoryBike1);
			inventoryBikeRepository.save(inventoryBike2);
			inventoryBikeRepository.save(inventoryBike3);
			inventoryBikeRepository.save(inventoryBike4);
			inventoryBikeRepository.save(inventoryBike5);

		};
	}
}
@Configuration
@EnableWebSecurity
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
	@Autowired
	UserRepository userRepository;
	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			User user = userRepository.findByUserName(inputName);
			if (user != null) {
				return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getUserPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and();///de heroku tambien
		http.authorizeRequests()
				.antMatchers("/bikes/all").permitAll()
				.antMatchers("/bikes/details/for/user").permitAll()
				.antMatchers("/bikes/user/register").permitAll()
				.antMatchers("/h2-console/**").permitAll()
				.antMatchers("/rest/**").hasAuthority("ADMIN")
				.antMatchers("/**").hasAuthority("USER")
				.anyRequest().fullyAuthenticated();
		/////Autorizaciones y permisos para los distintos niveles de seguridad que tendria el usuario segun su casificacion
		http.formLogin()
				.usernameParameter("name")
				.passwordParameter("password")
				.loginPage("/bikes/login");

		http.logout().logoutUrl("/api/logout");

		http.csrf().disable();

		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
//        http.headers().frameOptions().disable();
		http.headers().frameOptions().sameOrigin();
	}

	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}



	@Bean////importando Heroku a la base de datos
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		// The value of the 'Access-Control-Allow-Origin' header in the response must not be the wildcard '*' when the request's credentials mode is 'include'.

		configuration.setAllowedOrigins(Arrays.asList("http://localhost:8082"));
		configuration.setAllowedMethods(Arrays.asList("HEAD",
				"GET", "POST", "PUT", "DELETE", "PATCH","OPTIONS"));
		// setAllowCredentials(true) is important, otherwise:
		// will fail with 403 Invalid CORS request
		configuration.setAllowCredentials(true);
		// setAllowedHeaders is important! Without it, OPTIONS preflight request
		configuration.setAllowedHeaders(Arrays.asList("Accept","Access-Control-Request-Method","Access-Control-Request-Headers",
				"Accept-Language","Authorization","Content-Type","Request-Name","Request-Surname","Origin","X-Request-AppVersion",
				"X-Request-OsVersion", "X-Request-Device", "X-Requested-With"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

//	@Configuration
//	public class CorsConfiguration implements WebMvcConfigurer {
//
//		@Override
//		public void addCorsMappings(CorsRegistry registry) {
//			registry.addMapping("/**")
//					.allowedOrigins("http://localhost:8082")
//					.allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH")
//			.allowCredentials(true);
//		}
//	}

}



