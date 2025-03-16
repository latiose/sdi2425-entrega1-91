package com.uniovi.gestor;
import com.uniovi.gestor.services.LogService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final LogService logService;

    public WebSecurityConfig(LogService logService) {
        this.logService = logService;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public SpringSecurityDialect securityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/css/**", "/images/**", "/script/**", "/", "/login/error","/login").permitAll()
                .antMatchers("/employee/changePassword").authenticated()
                .antMatchers("/employee/**").hasRole("ADMIN")
                .antMatchers("/vehicle/list/**").authenticated()
                .antMatchers("/vehicle/add/**").hasRole("ADMIN")
                .antMatchers("/journey/edit/**").hasRole("ADMIN")
                .antMatchers("/journey/**").hasAnyAuthority("ROLE_STANDARD", "ROLE_ADMIN")
                .antMatchers("/refuel/**").hasAnyAuthority("ROLE_STANDARD", "ROLE_ADMIN")
                .antMatchers("/logs/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/error-403")
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login/error")
                .permitAll()
                .defaultSuccessUrl("/login/success")
                .and()
                .logout()
                .addLogoutHandler(customLogoutHandler())
                .logoutSuccessUrl("/login?logout=true")
                .permitAll();
    }

    private LogoutHandler customLogoutHandler() {
        return (request, response, authentication) -> {
            if (authentication != null) {
                String dni = authentication.getName();
                logService.log("LOGOUT", "Usuario salió de sesión | DNI: " + dni);
            }
        };
    }
}