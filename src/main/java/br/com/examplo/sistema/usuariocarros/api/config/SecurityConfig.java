package br.com.examplo.sistema.usuariocarros.api.config;

import java.util.Arrays;

import br.com.examplo.sistema.usuariocarros.security.JWTAuthEntryPoint;
import br.com.examplo.sistema.usuariocarros.security.JWTAuthorizationFilter;
import br.com.examplo.sistema.usuariocarros.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String USERS = "/api/users/**";

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/v2/api-docs", "/h2-console/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.cors().and().csrf().disable();

        http.authorizeRequests()//
                .antMatchers(HttpMethod.POST, "/api/signin/**").permitAll()//endpoint para logar
                .antMatchers(HttpMethod.POST, USERS).permitAll()
                .antMatchers(HttpMethod.PUT, USERS).permitAll()
                .antMatchers(HttpMethod.DELETE, USERS).permitAll()
                .antMatchers(HttpMethod.GET, USERS).permitAll()
                .antMatchers(HttpMethod.POST, "/h2-console").permitAll()
                .antMatchers(HttpMethod.GET, "/h2-console").permitAll()
                .antMatchers(HttpMethod.PUT, "/h2-console").permitAll()
                .antMatchers(HttpMethod.DELETE, "/h2-console").permitAll()
                .anyRequest().authenticated();

        http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.exceptionHandling().authenticationEntryPoint(new JWTAuthEntryPoint());
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
