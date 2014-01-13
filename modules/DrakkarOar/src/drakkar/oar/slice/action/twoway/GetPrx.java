// **********************************************************************
//
// Copyright (c) 2003-2010 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.4.0

package drakkar.oar.slice.action.twoway;

// <auto-generated>
//
// Generated from file `Twoway.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>


public interface GetPrx extends Ice.ObjectPrx
{
    public byte[] getSAMI(byte[] request)
        throws drakkar.oar.slice.error.RequestException;

    public byte[] getSAMI(byte[] request, java.util.Map<String, String> __ctx)
        throws drakkar.oar.slice.error.RequestException;

    public Ice.AsyncResult begin_getSAMI(byte[] request);

    public Ice.AsyncResult begin_getSAMI(byte[] request, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getSAMI(byte[] request, Ice.Callback __cb);

    public Ice.AsyncResult begin_getSAMI(byte[] request, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getSAMI(byte[] request, Callback_Get_getSAMI __cb);

    public Ice.AsyncResult begin_getSAMI(byte[] request, java.util.Map<String, String> __ctx, Callback_Get_getSAMI __cb);

    public byte[] end_getSAMI(Ice.AsyncResult __result)
        throws drakkar.oar.slice.error.RequestException;

    public boolean getSAMI_async(AMI_Get_getSAMI __cb, byte[] request);

    public boolean getSAMI_async(AMI_Get_getSAMI __cb, byte[] request, java.util.Map<String, String> __ctx);

    public byte[] getAMID(byte[] request)
        throws drakkar.oar.slice.error.RequestException;

    public byte[] getAMID(byte[] request, java.util.Map<String, String> __ctx)
        throws drakkar.oar.slice.error.RequestException;

    public Ice.AsyncResult begin_getAMID(byte[] request);

    public Ice.AsyncResult begin_getAMID(byte[] request, java.util.Map<String, String> __ctx);

    public Ice.AsyncResult begin_getAMID(byte[] request, Ice.Callback __cb);

    public Ice.AsyncResult begin_getAMID(byte[] request, java.util.Map<String, String> __ctx, Ice.Callback __cb);

    public Ice.AsyncResult begin_getAMID(byte[] request, Callback_Get_getAMID __cb);

    public Ice.AsyncResult begin_getAMID(byte[] request, java.util.Map<String, String> __ctx, Callback_Get_getAMID __cb);

    public byte[] end_getAMID(Ice.AsyncResult __result)
        throws drakkar.oar.slice.error.RequestException;

    public boolean getAMID_async(AMI_Get_getAMID __cb, byte[] request);

    public boolean getAMID_async(AMI_Get_getAMID __cb, byte[] request, java.util.Map<String, String> __ctx);
}
