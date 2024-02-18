
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

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * User: Serg
 * Date: 12.08.13
 * Time: 23:17
 */
@SuppressWarnings("FieldCanBeLocal")
@Service
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class CustomUserDetails implements UserDetailsService {

    public static final String REST_USER = "rest";

    public static final long MAIN_USER_ID = Integer.MAX_VALUE - 5L;

    private final AdditionalCustomUserDetails additionalCustomUserDetails;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // strength - 10
        // pass     - 123
        // bcrypt   - $2a$10$jaQkP.gqwgenn.xKtjWIbeP4X.LDJx92FKaQ9VfrN2jgdOUTPTMIu

        ComplexUsername complexUsername = ComplexUsername.getInstance(username);
        if (complexUsername == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        if (REST_USER.equals(complexUsername.getUsername())) {
            return additionalCustomUserDetails.restUserAccount;
        }
        throw new UsernameNotFoundException("Username not found");
    }
}
