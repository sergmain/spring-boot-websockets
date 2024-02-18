
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

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: Serg
 * Date: 12.08.13
 * Time: 23:19
 */
@Data
@EqualsAndHashCode(of = {"username", "password"})
@NoArgsConstructor
public class Account implements UserDetails, Serializable {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SerializableGrantedAuthority implements GrantedAuthority {
        @Serial
        private static final long serialVersionUID = 8923383713825441981L;

        public String authority;
    }


    @Serial
    private static final long serialVersionUID = 708692073045562337L;

    public Long id;

    public Integer version;

    public Long companyId;

    public String username;

    public String password;

    public boolean accountNonExpired;

    public boolean accountNonLocked;

    public boolean credentialsNonExpired;

    public boolean enabled;

    public String publicName;

    @Nullable
    public String mailAddress;

    @Nullable
    public String phone;

    @Nullable
    private String phoneAsStr;

    public long createdOn;

    public long updatedOn;

    // this field contains Authorities, not role. I.e. authority is "ROLE_" + role
    @Nullable
    public String roles;

    @Nullable
    public String secretKey;

    public boolean twoFA;

    @JsonIgnore
    public final AccountRoles accountRoles = new AccountRoles(()-> roles, (o)->roles = o);

    public List<SerializableGrantedAuthority> getAuthorities() {
        return accountRoles.getAuthorities().stream()
                .map(o->new SerializableGrantedAuthority(o.authority))
                .collect(Collectors.toList());
    }

}
