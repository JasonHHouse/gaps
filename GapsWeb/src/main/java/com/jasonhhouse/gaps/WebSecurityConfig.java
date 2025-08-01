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


import com.jasonhhouse.gaps.properties.PlexProperties;
import com.jasonhhouse.gaps.service.FileIoService;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

    private final GapsConfiguration gapsConfiguration;
    private final FileIoService fileIoService;

    public WebSecurityConfig(GapsConfiguration gapsConfiguration, FileIoService fileIoService) {
        this.gapsConfiguration = gapsConfiguration;
        this.fileIoService = fileIoService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        LOGGER.info("Name: {}", gapsConfiguration.getName());
        LOGGER.info("Description: {}", gapsConfiguration.getDescription());
        LOGGER.info("Version: {}", gapsConfiguration.getVersion());
        LOGGER.info("LoginEnabled: {}", gapsConfiguration.getLoginEnabled());

        if (gapsConfiguration.getLoginEnabled() && gapsConfiguration.getSslEnabled()) {
            LOGGER.info("Login Enabled. Configuring site security with ssl.");

            http.cors().and().csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/rss/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic();
        } else if (Boolean.TRUE.equals(gapsConfiguration.getLoginEnabled()) && Boolean.FALSE.equals(gapsConfiguration.getSslEnabled())) {
            LOGGER.info("Login Enabled. Configuring site security without ssl.");

            http.cors().and().csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/rss/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .httpBasic();
        } else {
            //TODO
            //Test needing cors and csrf disabled
            http.cors().and().csrf().disable();
        }
        
        return http.build();
    }



    @Bean
    public UserDetailsService userDetailsService() {
        LOGGER.info("userDetailsService()");
        if (Boolean.TRUE.equals(gapsConfiguration.getLoginEnabled())) {

            PlexProperties plexProperties = fileIoService.readProperties();

            String password;
            if (StringUtils.isEmpty(plexProperties.getPassword())) {
                password = UUID.randomUUID().toString();
                plexProperties = new PlexProperties();
                plexProperties.setPassword(password);
                LOGGER.info("Gaps Password: {}", password);
                fileIoService.writeProperties(plexProperties);
            } else {
                LOGGER.info("Using password from {}{}", gapsConfiguration.getStorageFolder(), gapsConfiguration.getProperties().getGapsProperties());
                password = plexProperties.getPassword();
            }

            PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            UserDetails userDetails = User.withUsername("user")
                    .password(encoder.encode(password))
                    .roles("USER")
                    .build();

            return new InMemoryUserDetailsManager(userDetails);
        } else {
            return new InMemoryUserDetailsManager();
        }
    }
}