// **********************************************************************
//
// Copyright (c) 2003-2010 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.4.0

package drakkar.oar.slice.client;

// <auto-generated>
//
// Generated from file `ClientSide.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>


public interface ClientSidePrx extends drakkar.oar.slice.action.twoway.GetPrx
{
    public void _notify(byte[] response);

    public void _notify(byte[] response, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_notify(byte[] response);

    public Ice.AsyncResult begin_notify(byte[] response, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_notify(byte[] response, Ice.Callback __cb);

    public Ice.AsyncResult begin_notify(byte[] response, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_notify(byte[] response, Callback_ClientSide_notify __cb);

    public Ice.AsyncResult begin_notify(byte[] response, java.util.Map<String, String> __ctx, Callback_ClientSide_notify __cb);

    public void end_notify(Ice.AsyncResult __result);

    public boolean notify_async(AMI_ClientSide_notify __cb, byte[] response);

    public boolean notify_async(AMI_ClientSide_notify __cb, byte[] response, java.util.Map<String, String> __ctx);

    public void disconnect();

    public void disconnect(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_disconnect();

    public Ice.AsyncResult begin_disconnect(java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_disconnect(Ice.Callback __cb);

    public Ice.AsyncResult begin_disconnect(java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_disconnect(Callback_ClientSide_disconnect __cb);

    public Ice.AsyncResult begin_disconnect(java.util.Map<String, String> __ctx, Callback_ClientSide_disconnect __cb);

    public void end_disconnect(Ice.AsyncResult __result);

    public boolean disconnect_async(AMI_ClientSide_disconnect __cb);

    public boolean disconnect_async(AMI_ClientSide_disconnect __cb, java.util.Map<String, String> __ctx);
}