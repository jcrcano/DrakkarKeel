// **********************************************************************
//
// Copyright (c) 2003-2010 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.4.0

package drakkar.oar.slice.management;

// <auto-generated>
//
// Generated from file `Server.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>


public interface ServerManagerPrx extends Ice.ObjectPrx
{
    public ManagerPrx getManager();

    public ManagerPrx getManager(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getManager();

    public Ice.AsyncResult begin_getManager(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getManager(Ice.Callback __cb);

    public Ice.AsyncResult begin_getManager(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getManager(Callback_ServerManager_getManager __cb);

    public Ice.AsyncResult begin_getManager(java.util.Map<String, String> __ctx, Callback_ServerManager_getManager __cb);

    public ManagerPrx end_getManager(Ice.AsyncResult __result);
}
