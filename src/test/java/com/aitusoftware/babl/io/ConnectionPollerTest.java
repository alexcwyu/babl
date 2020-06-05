/*
 * Copyright 2019-2020 Aitu Software Limited.
 *
 * https://aitusoftware.com
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
package com.aitusoftware.babl.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Queue;

import com.aitusoftware.babl.config.SocketConfig;
import com.aitusoftware.babl.websocket.routing.ConnectionRouter;

import org.agrona.concurrent.SleepingMillisIdleStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConnectionPollerTest
{
    private ServerSocketChannel serverSocketChannel;
    private ConnectionPoller connectionPoller;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() throws IOException
    {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind(null);
        connectionPoller = new ConnectionPoller(serverSocketChannel, new Queue[]{new LinkedList<SocketChannel>()},
            new SleepingMillisIdleStrategy(1), new SocketConfig(), new RejectConnectionRouter());
        connectionPoller.onStart();
    }

    @Test
    void shouldCloseRejectedConnection() throws Exception
    {
        final SocketChannel socketChannel = SocketChannel.open(serverSocketChannel.getLocalAddress());
        final long deadlineMs = System.currentTimeMillis() + 10_000L;
        while (System.currentTimeMillis() < deadlineMs)
        {
            connectionPoller.doWork();

            if (!socketChannel.isOpen())
            {
                return;
            }
            try
            {
                if (socketChannel.write(ByteBuffer.allocate(1)) == -1)
                {
                    return;
                }
            }
            catch (final IOException e)
            {
                return;
            }

        }

        Assertions.fail();
    }

    private static final class RejectConnectionRouter implements ConnectionRouter
    {
        @Override
        public int allocateServer(final SocketChannel socketChannel)
        {
            return REJECT_CONNECTION;
        }
    }
}