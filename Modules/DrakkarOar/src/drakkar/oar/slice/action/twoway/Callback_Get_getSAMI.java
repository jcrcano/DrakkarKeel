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


public abstract class Callback_Get_getSAMI extends Ice.TwowayCallback
{
    public abstract void response(byte[] __ret);
    public abstract void exception(Ice.UserException __ex);

    public final void __completed(Ice.AsyncResult __result)
    {
        GetPrx __proxy = (GetPrx)__result.getProxy();
        byte[] __ret = null;
        try
        {
            __ret = __proxy.end_getSAMI(__result);
        }
        catch(Ice.UserException __ex)
        {
            exception(__ex);
            return;
        }
        catch(Ice.LocalException __ex)
        {
            exception(__ex);
            return;
        }
        response(__ret);
    }
}
