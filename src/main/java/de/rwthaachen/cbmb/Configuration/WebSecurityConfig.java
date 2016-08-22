package de.rwthaachen.cbmb.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan("de.rwthaachen.cbmb")
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)   //comment it
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    @Qualifier("userDetailsService")
//    UserDetailsService userDetailsService;
//
//    @Autowired
//    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService);
//        auth.authenticationProvider(authenticationProvider());
//    }
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.authorizeRequests()
                .antMatchers("/register", "/resources/**", "resources/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").permitAll()
//                .usernameParameter("username").passwordParameter("password")
                .and().exceptionHandling().accessDeniedPage("/Access_Denied");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("asdflk").password("asdflk").roles("USER");
    }



//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//
//        http.authorizeRequests()
//                .antMatchers("/logout", "/register", "/console", "/console/*", "/console/**", "resources/**",
//                        "resources/**/**").permitAll()
////                .antMatchers("/admin/**","/newuser").permitAll()
////                .antMatchers("/db/**").permitAll()
//                .anyRequest().authenticated()
//                .and().formLogin().loginPage("/login");
////                .usernameParameter("username").passwordParameter("password");
////                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/index");
////                .usernameParameter("username").passwordParameter("password")
////                .and().exceptionHandling().accessDeniedPage("/Access_Denied");
//    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("asdflk").password("asdflk").roles("USER");
//    }


//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http.csrf().disable();
//
//        http.authorizeRequests()
//                .antMatchers("/**", "/*", "resources/**").permitAll()
////                .antMatchers("/*").hasRole("USER")
//                .anyRequest().authenticated()
//                .and()
//                .formLogin().loginPage("/login").permitAll()
//                .and()
//                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login");
//
//
////        http
////                .authorizeRequests()
////                .antMatchers("/login", "/logout", "/register", "/console", "/console/*", "/console/**", "resources/**").permitAll()
////                .anyRequest().authenticated()
////                .and()
////                .formLogin()
////                .loginPage("/login")
////                .permitAll();
//
//        // Logout and redirection:
//        //
////        http.logout()
////                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
////                .invalidateHttpSession(true)
////                .logoutSuccessUrl("/login");
////
////        http.authorizeRequests()
////                .antMatchers("/login", "/logout", "/register", "resources/**").permitAll()
////                .anyRequest().authenticated()
////                .and()
////                .formLogin()
////                .loginPage("/login")
////                .loginProcessingUrl("/login");
//    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth)
//            throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user").password("user").roles("USER");
//    }
}