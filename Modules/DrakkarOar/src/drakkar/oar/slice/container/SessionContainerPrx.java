// **********************************************************************
//
// Copyright (c) 2003-2010 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.4.0

package drakkar.oar.slice.container;

// <auto-generated>
//
// Generated from file `Container.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>


public interface SessionContainerPrx extends drakkar.oar.slice.action.RCPOperationsPrx
{
    public void disconnect();

    public void disconnect(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_disconnect();

    public Ice.AsyncResult begin_disconnect(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_disconnect(Ice.Callback __cb);

    public Ice.AsyncResult begin_disconnect(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_disconnect(Callback_SessionContainer_disconnect __cb);

    public Ice.AsyncResult begin_disconnect(java.util.Map<String, String> __ctx, Callback_SessionContainer_disconnect __cb);

    public void end_disconnect(Ice.AsyncResult __result);
}
