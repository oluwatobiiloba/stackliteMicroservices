package com.service.discovery.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Value(value = "${app.eureka.username}")
    private String username;

    @Value(value = "${app.eureka.password}")
    private String password;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .formLogin(Customizer.withDefaults());
        return http.build();
    }



//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username(username)
//                .password(password)
//                .authorities("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }

//    @Override
//    public void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.csrf().disable()
//                .authorizeHttpRequests()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .httpBasic();
//    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        System.out.println(username + password);
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests((authorizeHttpRequests) ->
//                        authorizeHttpRequests
//                                .anyRequest()
//                                .authenticated()
//                )
//                .httpBasic(withDefaults());
//        return http.build();
//    }



}
