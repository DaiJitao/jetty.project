//
//  ========================================================================
//  Copyright (c) 1995-2017 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.websocket.tests;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.eclipse.jetty.util.BufferUtil;
import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.FrameCallback;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
import org.eclipse.jetty.websocket.common.WebSocketFrame;

public class OutgoingFramesCapture implements OutgoingFrames
{
    public BlockingQueue<WebSocketFrame> frames = new LinkedBlockingDeque<>();
    
    public void assertFrameCount(int expectedCount)
    {
        assertThat("Frame Count", frames.size(), is(expectedCount));
    }
    
    @Deprecated
    public void assertHasFrame(byte opCode, int expectedCount)
    {
        assertHasOpCount(opCode, expectedCount);
    }
    
    public void assertHasOpCount(byte opCode, int expectedCount)
    {
        assertThat("Frame Count [op=" + opCode + "]", getFrameCount(opCode), is(expectedCount));
    }
    
    public void dump()
    {
        System.out.printf("Captured %d outgoing writes%n",frames.size());
        int i=0;
        for (WebSocketFrame frame: frames)
        {
            System.out.printf("[%3d] %s%n",i++,frame);
            System.out.printf("      %s%n",BufferUtil.toDetailString(frame.getPayload()));
        }
    }

    public int getFrameCount(byte op)
    {
        int count = 0;
        for (WebSocketFrame frame : frames)
        {
            if (frame.getOpCode() == op)
            {
                count++;
            }
        }
        return count;
    }

    @Override
    public void outgoingFrame(Frame frame, FrameCallback callback, BatchMode batchMode)
    {
        frames.add(WebSocketFrame.copy(frame));
        // Consume bytes
        ByteBuffer payload = frame.getPayload();
        payload.position(payload.limit());
        // notify callback
        if (callback != null)
        {
            callback.succeed();
        }
    }
}
