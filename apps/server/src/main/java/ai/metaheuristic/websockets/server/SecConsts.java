
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

import java.util.List;

/**
 * @author Serge
 * Date: 10/30/2019
 * Time: 5:18 PM
 */
public class SecConsts {
    public static final String ROLE_SERVER_REST_ACCESS = "ROLE_SERVER_REST_ACCESS";
    public static final String ROLE_ASSET_REST_ACCESS = "ROLE_ASSET_REST_ACCESS";
    public static final String ROLE_BILLING = "ROLE_BILLING";

    public static final String ROLE_MAIN_ADMIN = "ROLE_MAIN_ADMIN";
    public static final String ROLE_MAIN_OPERATOR = "ROLE_MAIN_OPERATOR";
    public static final String ROLE_MAIN_SUPPORT = "ROLE_MAIN_SUPPORT";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final List<String> POSSIBLE_ROLES = List.of(ROLE_ADMIN,"ROLE_MANAGER","ROLE_OPERATOR", "ROLE_DATA");
    public static final List<String> COMPANY_1_POSSIBLE_ROLES =
            List.of(ROLE_SERVER_REST_ACCESS, ROLE_ASSET_REST_ACCESS, ROLE_MAIN_OPERATOR,
                    ROLE_MAIN_SUPPORT, ROLE_BILLING, "ROLE_MAIN_ASSET_MANAGER");
}
