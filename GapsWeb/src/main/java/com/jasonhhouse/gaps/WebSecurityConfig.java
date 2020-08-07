/*
 * Copyright 2020 Jason H House
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.jasonhhouse.gaps;


import com.jasonhhouse.gaps.service.IoService;
import java.io.IOException;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

    private final YamlConfig myConfig;

    private final IoService ioService;

    private final GapsService gapsService;

    @Autowired
    public WebSecurityConfig(YamlConfig myConfig, IoService ioService, GapsService gapsService) {
        this.myConfig = myConfig;
        this.ioService = ioService;
        this.gapsService = gapsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LOGGER.info("Name: {}",  myConfig.getName());
        LOGGER.info("Description: {}", myConfig.getDescription());
        LOGGER.info("Version: {}" , myConfig.getVersion());
        LOGGER.info("LoginEnabled: {}", myConfig.getLoginEnabled());

        if (myConfig.getLoginEnabled() && myConfig.getSslEnabled()) {
            LOGGER.info("Login Enabled. Configuring site security with ssl.");
            http.cors().and().csrf().disable()
                    .authorizeRequests().antMatchers("/images/gaps.ico",
                    "/css/bootstrap.min.css",
                    "/css/input.min.css",
                    "/js/jquery-3.4.1.min.js",
                    "/js/bootstrap.bundle.min.js",
                    "/js/index.min.js",
                    "/images/final-2.svg",
                    "/images/final-gaps.svg").permitAll()
                    .anyRequest().fullyAuthenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .and()
                    .logout()
                    .permitAll();
        } else if (Boolean.TRUE.equals(myConfig.getLoginEnabled()) && !myConfig.getSslEnabled()) {
            LOGGER.info("Login Enabled. Configuring site security without ssl.");

            http.cors().and().csrf().disable()
                    .authorizeRequests()
                    .anyRequest().fullyAuthenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/home")
                    .permitAll()
                    .and()
                    .logout()
                    .permitAll();

        } else {
            http.cors().and().csrf().disable();
        }
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        LOGGER.info("userDetailsService()");
        if (Boolean.TRUE.equals(myConfig.getLoginEnabled())) {

            PlexProperties plexProperties = null;
            try {
                plexProperties = ioService.readProperties();
            } catch (IOException e) {
                LOGGER.warn("No properties found to get password. Generating new password");
            }

            String password;
            if (plexProperties == null || StringUtils.isEmpty(plexProperties.getPassword())) {
                password = UUID.randomUUID().toString();
                plexProperties = new PlexProperties();
                plexProperties.setPassword(password);
                LOGGER.info("Gaps Password: {}", password);
                try {
                    ioService.writeProperties(plexProperties);
                    gapsService.updatePlexSearch(plexProperties);
                } catch (IOException e) {
                    LOGGER.error("Failed to write out password to properties file.");
                }
            } else {
                LOGGER.info("Using password from /usr/data/gaps.properties");
                password = plexProperties.getPassword();
            }

            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            UserDetails userDetails = User.withUsername("user")
                    .password(encoder.encode(password))
                    .roles("USER")
                    .build();

            return new InMemoryUserDetailsManager(userDetails);
        } else {
            return super.userDetailsService();
        }
    }
}