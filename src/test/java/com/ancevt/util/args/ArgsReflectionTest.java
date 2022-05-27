package com.ancevt.util.args;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class ArgsReflectionTest {

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

        @Contract(pure = true)
        @Override
        public @NotNull String toString() {
            return "TestConnectionCommand{" +
                    "command='" + command + '\'' +
                    ", host='" + host + '\'' +
                    ", port=" + port +
                    ", debugMode=" + debug +
                    '}';
        }
    }
}