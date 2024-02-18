/*
 *    Copyright 2024 Sergio Lissner, Metaheuristic
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package ai.metaheuristic.websockets.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.security.config.Customizer.withDefaults;


/**
 * @author Sergio Lissner
 * Date: 2/6/2024
 * Time: 12:17 PM
 */
@Configuration
public class Config {

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Configuration
    @EnableWebSocketMessageBroker
    public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint("/ws/dispatcher");
        }

        @Override
        public void configureMessageBroker(MessageBrokerRegistry config) {
            config.setApplicationDestinationPrefixes("/ws/dispatcher");
            config.enableSimpleBroker("/topic");
        }

        @Override
        public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
            registry.setMessageSizeLimit(4 * 1024);
            //registry.setTimeToFirstMessage(30000);
        }
    }

    @Bean
    public SecurityFilterChain restFilterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(withDefaults())
            .sessionManagement((sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(OPTIONS).permitAll() // allow CORS option calls for Swagger UI
                .requestMatchers("/","/index.html", "/*.js", "/*.css", "/favicon.ico", "/assets/**","/resources/**", "/rest/login").permitAll()
                .requestMatchers("/rest/v1/standalone/anon/**", "/rest/v1/dispatcher/anon/**").permitAll()
                .requestMatchers("/rest/v1/dispatcher/status/**").hasAuthority("ROLE_MAIN_ADMIN")
                .requestMatchers("/task").hasAuthority("ROLE_SERVER_REST_ACCESS")
                .requestMatchers("/ws/**").hasAuthority("ROLE_SERVER_REST_ACCESS")
                .anyRequest().denyAll()
            )
            .csrf(AbstractHttpConfigurer::disable)
            .headers((headers) -> headers
                .contentTypeOptions(withDefaults())
                .xssProtection(withDefaults())
                .cacheControl(withDefaults())
                .httpStrictTransportSecurity(withDefaults())
                .frameOptions(withDefaults()));

        return http.build();
    }

}
