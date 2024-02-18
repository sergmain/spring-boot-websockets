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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Sergio Lissner
 * Date: 2/6/2024
 * Time: 12:08 PM
 */
@RestController
public class ServerRestController {

    private final SimpMessagingTemplate template;

    @Autowired
    public ServerRestController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @RequestMapping(path="/task", method=GET)
    @ResponseBody
    public String greet(String t) {
        String text = "[" + LocalDateTime.now() + "]:" + t;
        this.template.convertAndSend("/topic/events", text);
        return text;
    }


}
