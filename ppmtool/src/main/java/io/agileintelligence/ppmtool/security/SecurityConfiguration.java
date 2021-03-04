package io.agileintelligence.ppmtool.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.agileintelligence.ppmtool.services.CustomUserdetailsService;

import static io.agileintelligence.ppmtool.security.SecurityConstants.SIGN_UP_URLS;
import static io.agileintelligence.ppmtool.security.SecurityConstants.H2_URL;

@Configuration
@EnableWebSecurity // to enable the security
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private CustomUserdetailsService customUserdetailsService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationEntryFilter() {
        return new JwtAuthenticationFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable() // to disable cors and csrf
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler) // exceptionhandling by
                                                                                   // customizing auth entry
                                                                                   // point
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Rest API we want is
                                                                                                  // stateless and don't
                                                                                                  // want to store
                                                                                                  // sessions
                .and().headers().frameOptions().sameOrigin() // To enable H2 Database
                .and().authorizeRequests()
                .antMatchers("/", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg", "/**/*.html",
                        "/**/*.css", "/**/*.js")
                .permitAll() // to by default give permissions to these routes
                .antMatchers(SIGN_UP_URLS).permitAll().antMatchers(H2_URL).permitAll().anyRequest().authenticated();

        http.addFilterBefore(jwtAuthenticationEntryFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserdetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}
