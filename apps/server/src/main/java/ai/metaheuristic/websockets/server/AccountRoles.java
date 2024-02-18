
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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Serge
 * Date: 8/30/2020
 * Time: 3:30 PM
 */
public class AccountRoles {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SerializableGrantedAuthority implements Serializable {
        @Serial
        private static final long serialVersionUID = 4854416219001153110L;

        public String authority;
    }

    @Data
    public static class InitedRoles {
        public boolean inited;
        public final List<String> roles = new ArrayList<>();

        public void reset() {
            inited = false;
            roles.clear();
        }
        public boolean contains(String role) {
            if (!inited) {
                throw new IllegalStateException("(!inited)");
            }
            return roles.contains(role);
        }

        public void addRole(String role) {
            roles.add(role);
        }

        public void removeRole(String role) {
            roles.remove(role);
        }

        public String asString() {
            return String.join(", ", roles);
        }
    }

    private final Supplier<String> roleGetter;
    private final Consumer<String> roleSetter;

    private final InitedRoles initedRoles = new InitedRoles();
    private final List<SerializableGrantedAuthority> authorities = new ArrayList<>();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    public AccountRoles(Supplier<String> roleGetter, Consumer<String> roleSetter) {
        this.roleSetter = roleSetter;
        this.roleGetter = roleGetter;
    }

    public boolean hasRole(String role) {
        initRoles();
        try {
            readLock.lock();
            return initedRoles.contains(role);
        } finally {
            readLock.unlock();
        }
    }

    public List<SerializableGrantedAuthority> getAuthorities() {
        initRoles();
        try {
            readLock.lock();
            return authorities;
        } finally {
            readLock.unlock();
        }
    }

    public List<String> getRolesAsList() {
        initRoles();
        try {
            readLock.lock();
            return new ArrayList<>(initedRoles.roles);
        } finally {
            readLock.unlock();
        }
    }

    public String asString() {
        initRoles();
        try {
            readLock.lock();
            return String.join(",", initedRoles.roles);
        } finally {
            readLock.unlock();
        }
    }

    public void addRole(String role) {
        try {
            writeLock.lock();

            initedRoles.addRole(role);
            this.roleSetter.accept(initedRoles.asString());
            initedRoles.reset();
            authorities.clear();
            initRoles();
        } finally {
            writeLock.unlock();
        }
    }

    public void removeRole(String role) {
        try {
            writeLock.lock();

            initedRoles.removeRole(role);
            this.roleSetter.accept(initedRoles.asString());
            initedRoles.reset();

            authorities.clear();
            initRoles();
        } finally {
            writeLock.unlock();
        }
    }

    private void initRoles() {
        if (initedRoles.inited) {
            return;
        }
        try {
            writeLock.lock();
            if (initedRoles.inited) {
                return;
            }
            if (this.roleGetter.get()!=null) {
                StringTokenizer st = new StringTokenizer(this.roleGetter.get(), ",");
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    if (token==null || token.isBlank()) {
                        continue;
                    }
                    String role = fixNaming(token.trim());
                    initedRoles.roles.add(role);
                    authorities.add(new SerializableGrantedAuthority(role));
                }
            }
            initedRoles.inited = true;
        } finally {
            writeLock.unlock();
        }
    }

    private static String fixNaming(String role) {
        return switch (role) {
            case "ROLE_MASTER_OPERATOR" -> "ROLE_MAIN_OPERATOR";
            case "ROLE_MASTER_SUPPORT" -> "ROLE_MAIN_SUPPORT";
            case "ROLE_MASTER_ASSET_MANAGER" -> "ROLE_MAIN_ASSET_MANAGER";
            default -> role;
        };
    }

}
