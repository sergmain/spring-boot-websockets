

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

import lombok.Data;
import org.springframework.lang.Nullable;

/**
 * @author Sergio Lissner
 * Date: 8/18/2023
 * Time: 1:35 AM
 */
@Data
public class ComplexUsername {
    String username;

    private ComplexUsername(String username) {
        this.username = username;
    }

    @Nullable
    public static ComplexUsername getInstance(String fullUsername) {
        int idx = fullUsername.lastIndexOf('=');
        final String username;
        if (idx == -1) {
            username = fullUsername;
        } else {
            username = fullUsername.substring(0, idx);
        }
        ComplexUsername complexUsername = new ComplexUsername(username);

        return complexUsername.isValid() ? complexUsername : null;
    }

    private boolean isValid() {
        return username != null && !username.isBlank();
    }
}
