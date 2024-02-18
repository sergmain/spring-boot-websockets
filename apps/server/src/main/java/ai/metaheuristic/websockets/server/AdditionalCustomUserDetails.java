
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

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static ai.metaheuristic.websockets.server.CustomUserDetails.REST_USER;
import static ai.metaheuristic.websockets.server.SecConsts.ROLE_ASSET_REST_ACCESS;

/**
 * @author Sergio Lissner
 * Date: 8/18/2023
 * Time: 10:24 PM
 */
@Service
@RequiredArgsConstructor(onConstructor_={@Autowired})
public class AdditionalCustomUserDetails {

    public String restUserPassword;
    public String restUserPasswordEncoded;
    public Account restUserAccount;

    @PostConstruct
    public void init() {
        restUserPassword = "123";
        // strength - 10
        // pass     - 123
        // bcrypt   - $2a$10$jaQkP.gqwgenn.xKtjWIbeP4X.LDJx92FKaQ9VfrN2jgdOUTPTMIu

        restUserPasswordEncoded = "$2a$10$jaQkP.gqwgenn.xKtjWIbeP4X.LDJx92FKaQ9VfrN2jgdOUTPTMIu";

        restUserAccount = new Account();
        restUserAccount.setId( Integer.MAX_VALUE -6L );
        restUserAccount.setCompanyId( 1L );
        restUserAccount.setUsername(REST_USER);
        restUserAccount.setAccountNonExpired(true);
        restUserAccount.setAccountNonLocked(true);
        restUserAccount.setCredentialsNonExpired(true);
        restUserAccount.setEnabled(true);
        restUserAccount.setPassword(restUserPasswordEncoded);
        restUserAccount.setRoles(SecConsts.ROLE_SERVER_REST_ACCESS+", " + ROLE_ASSET_REST_ACCESS);

        int i=0;
    }
}
