/**
 * Copyright (C) 2022 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ancevt.util.args;

import com.ancevt.util.args.reflection.ArgsCommand;
import com.ancevt.util.args.reflection.ArgsParameter;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArgsReflectionTest {

    @Test
    void testCreate() {
        TestConnectionCommand testConnectionCommand
            = Args.of("connect --host localhost -p 3333 --debug --empty").convert(TestConnectionCommand.class);

        assertThat(testConnectionCommand.getCommand(), is("connect"));
        assertThat(testConnectionCommand.getHost(), is("localhost"));
        assertThat(testConnectionCommand.getPort(), is(3333));
        assertTrue(testConnectionCommand.isDebug());
    }

    @Test
    void testFill() {
        TestConnectionCommand testConnectionCommand = new TestConnectionCommand();
        testConnectionCommand.host = "oldvalue";
        Args.of("-h localhost -p 3333").convert(testConnectionCommand);
        assertThat(testConnectionCommand.getHost(), is("localhost"));
    }

    private static class TestConnectionCommand {

        @ArgsCommand
        private String command;

        @ArgsParameter(names = {"--host", "-h"}, required = true)
        private String host;

        @ArgsParameter(names = {"--port", "-p"}, required = true)
        private int port;

        @ArgsParameter(names = "--debug")
        private boolean debug;

        public String getCommand() {
            return command;
        }

        public String getHost() {
            return host;
        }

        public int getPort() {
            return port;
        }

        public boolean isDebug() {
            return debug;
        }

        @Override
        public String toString() {
            return "TestConnectionCommand{" +
                "command='" + command + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", debug=" + debug +
                '}';
        }
    }
}
