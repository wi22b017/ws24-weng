package at.fhtw.bweng.security;

import at.fhtw.bweng.security.jwt.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailService customUserDetailService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CustomUserDetailService customUserDetailService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailService = customUserDetailService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.addAllowedOrigin("http://localhost:8080"); // Frontend origin
                    corsConfig.addAllowedMethod("*"); // Allow all HTTP methods
                    corsConfig.addAllowedHeader("*"); // Allow all headers
                    corsConfig.setAllowCredentials(true); // Allow credentials
                    return corsConfig;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(registry ->
                        registry
                                .requestMatchers(HttpMethod.POST, "/users").permitAll() // Everyone can create a user (register)
                                .requestMatchers(HttpMethod.GET, "/users").hasAuthority("ADMIN") // Only ADMINS can see a list of users
                                .requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("ADMIN") // Only ADMINS can delete users
                                .requestMatchers(HttpMethod.POST, "/auth/token").permitAll() // Everyone can login (request a token)
                                .requestMatchers(HttpMethod.POST, "/pictures/").hasAuthority("ADMIN") // Only ADMINS can upload pictures directly to minio, users do it via userController
                                .requestMatchers(HttpMethod.GET, "/pictures/**").hasAuthority("ADMIN") // Only ADMINS can GET pictures directly from minio, users do it via userController
                                .requestMatchers(HttpMethod.POST, "/paymentMethods").hasAuthority("ADMIN") // Only ADMINS can create new payment methods
                                .requestMatchers(HttpMethod.GET, "/paymentMethods/**").permitAll() // Everyone can see payment methods
                                .requestMatchers(HttpMethod.PUT, "/paymentMethods/**").hasAuthority("ADMIN") // Only ADMINS can update payment methods
                                .requestMatchers(HttpMethod.DELETE, "/paymentMethods/**").hasAuthority("ADMIN") // Only ADMINS can delete payment methods
                                .requestMatchers("/passengers/**").hasAuthority("ADMIN") // Only ADMINS can create, read, update, delete passengers
                                .requestMatchers("/aircrafts/**").hasAuthority("ADMIN") // Only ADMINS can create, read, update, delete aircrafts
                                .requestMatchers("/airlines/**").hasAuthority("ADMIN") // Only ADMINS can create, read, update, delete airlines
                                .requestMatchers("/baggages/**").hasAuthority("ADMIN") // Only ADMINS can create, read, update, delete baggages
                                .requestMatchers(HttpMethod.POST, "/flights/").hasAuthority("ADMIN") // Only ADMINS can create new flights
                                .requestMatchers(HttpMethod.PUT, "/flights/**").hasAuthority("ADMIN") // Only ADMINS can update new flights
                                .requestMatchers(HttpMethod.DELETE, "/flights/**").hasAuthority("ADMIN") // Only ADMINS can delete new flights
                                .requestMatchers(HttpMethod.GET, "/flights").permitAll() // Everyone can see flights
                                .requestMatchers(HttpMethod.GET, "/airports").permitAll() // Everyone can see airports
                                .requestMatchers(HttpMethod.POST, "/baggageTypes/").hasAuthority("ADMIN") // Only ADMINS can create new baggageTypes
                                .requestMatchers(HttpMethod.PUT, "/baggageTypes/**").hasAuthority("ADMIN") // Only ADMINS can update new baggageTypes
                                .requestMatchers(HttpMethod.DELETE, "/baggageTypes/**").hasAuthority("ADMIN") // Only ADMINS can delete new baggageTypes
                                .requestMatchers(HttpMethod.GET, "/baggageTypes").permitAll() // Everyone can see baggageTypes
                                .requestMatchers(HttpMethod.GET, "/bookings/").hasAuthority("ADMIN") // Only ADMINS can see bookings
                                .requestMatchers(HttpMethod.PUT, "/bookings/**").hasAuthority("ADMIN") // Only ADMINS can update bookings
                                .requestMatchers(HttpMethod.DELETE, "/bookings/**").hasAuthority("ADMIN") // Only ADMINS can update bookings
                                //----------------
                                //.requestMatchers(HttpMethod.GET, "/airlines").permitAll()
                                //.requestMatchers(HttpMethod.GET, "/aircrafts").permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Preflight requests
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        //                .httpBasic(basic -> {
//                });

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, UserDetailsService userDetailsService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

        return authenticationManagerBuilder.build();
    }
}

