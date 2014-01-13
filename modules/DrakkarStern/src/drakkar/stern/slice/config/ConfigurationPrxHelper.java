// **********************************************************************
//
// Copyright (c) 2003-2010 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.4.0

package drakkar.stern.slice.config;

// <auto-generated>
//
// Generated from file `Configuration.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>


public final class ConfigurationPrxHelper extends Ice.ObjectPrxHelperBase implements ConfigurationPrx
{
    public void
    sendAMID(byte[] request)
        throws drakkar.oar.slice.error.RequestException
    {
        sendAMID(request, null, false);
    }

    public void
    sendAMID(byte[] request, java.util.Map<String, String> __ctx)
        throws drakkar.oar.slice.error.RequestException
    {
        sendAMID(request, __ctx, true);
    }

    private void
    sendAMID(byte[] request, java.util.Map<String, String> __ctx, boolean __explicitCtx)
        throws drakkar.oar.slice.error.RequestException
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __checkTwowayOnly("sendAMID");
                __delBase = __getDelegate(false);
                _ConfigurationDel __del = (_ConfigurationDel)__delBase;
                __del.sendAMID(request, __ctx);
                return;
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    private static final String __sendAMID_name = "sendAMID";

    public Ice.AsyncResult begin_sendAMID(byte[] request)
    {
        return begin_sendAMID(request, null, false, null);
    }

    public Ice.AsyncResult begin_sendAMID(byte[] request, java.util.Map<String, String> __ctx)
    {
        return begin_sendAMID(request, __ctx, true, null);
    }

    public Ice.AsyncResult begin_sendAMID(byte[] request, Ice.Callback __cb)
    {
        return begin_sendAMID(request, null, false, __cb);
    }

    public Ice.AsyncResult begin_sendAMID(byte[] request, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_sendAMID(request, __ctx, true, __cb);
    }

    public Ice.AsyncResult begin_sendAMID(byte[] request, drakkar.oar.slice.action.oneway.Callback_Send_sendAMID __cb)
    {
        return begin_sendAMID(request, null, false, __cb);
    }

    public Ice.AsyncResult begin_sendAMID(byte[] request, java.util.Map<String, String> __ctx, drakkar.oar.slice.action.oneway.Callback_Send_sendAMID __cb)
    {
        return begin_sendAMID(request, __ctx, true, __cb);
    }

    private Ice.AsyncResult begin_sendAMID(byte[] request, java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__sendAMID_name);
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __sendAMID_name, __cb);
        try
        {
            __result.__prepare(__sendAMID_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            IceInternal.BasicStream __os = __result.__os();
            Ice.ByteSeqHelper.write(__os, request);
            __os.endWriteEncaps();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    public void end_sendAMID(Ice.AsyncResult __result)
        throws drakkar.oar.slice.error.RequestException
    {
        Ice.AsyncResult.__check(__result, this, __sendAMID_name);
        if(!__result.__wait())
        {
            try
            {
                __result.__throwUserException();
            }
            catch(drakkar.oar.slice.error.RequestException __ex)
            {
                throw __ex;
            }
            catch(Ice.UserException __ex)
            {
                throw new Ice.UnknownUserException(__ex.ice_name());
            }
        }
        IceInternal.BasicStream __is = __result.__is();
        __is.skipEmptyEncaps();
    }

    public boolean
    sendAMID_async(drakkar.oar.slice.action.oneway.AMI_Send_sendAMID __cb, byte[] request)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__sendAMID_name);
            __r = begin_sendAMID(request, null, false, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __sendAMID_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public boolean
    sendAMID_async(drakkar.oar.slice.action.oneway.AMI_Send_sendAMID __cb, byte[] request, java.util.Map<String, String> __ctx)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__sendAMID_name);
            __r = begin_sendAMID(request, __ctx, true, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __sendAMID_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public void
    sendSAMI(byte[] request)
        throws drakkar.oar.slice.error.RequestException
    {
        sendSAMI(request, null, false);
    }

    public void
    sendSAMI(byte[] request, java.util.Map<String, String> __ctx)
        throws drakkar.oar.slice.error.RequestException
    {
        sendSAMI(request, __ctx, true);
    }

    private void
    sendSAMI(byte[] request, java.util.Map<String, String> __ctx, boolean __explicitCtx)
        throws drakkar.oar.slice.error.RequestException
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __checkTwowayOnly("sendSAMI");
                __delBase = __getDelegate(false);
                _ConfigurationDel __del = (_ConfigurationDel)__delBase;
                __del.sendSAMI(request, __ctx);
                return;
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    private static final String __sendSAMI_name = "sendSAMI";

    public Ice.AsyncResult begin_sendSAMI(byte[] request)
    {
        return begin_sendSAMI(request, null, false, null);
    }

    public Ice.AsyncResult begin_sendSAMI(byte[] request, java.util.Map<String, String> __ctx)
    {
        return begin_sendSAMI(request, __ctx, true, null);
    }

    public Ice.AsyncResult begin_sendSAMI(byte[] request, Ice.Callback __cb)
    {
        return begin_sendSAMI(request, null, false, __cb);
    }

    public Ice.AsyncResult begin_sendSAMI(byte[] request, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_sendSAMI(request, __ctx, true, __cb);
    }

    public Ice.AsyncResult begin_sendSAMI(byte[] request, drakkar.oar.slice.action.oneway.Callback_Send_sendSAMI __cb)
    {
        return begin_sendSAMI(request, null, false, __cb);
    }

    public Ice.AsyncResult begin_sendSAMI(byte[] request, java.util.Map<String, String> __ctx, drakkar.oar.slice.action.oneway.Callback_Send_sendSAMI __cb)
    {
        return begin_sendSAMI(request, __ctx, true, __cb);
    }

    private Ice.AsyncResult begin_sendSAMI(byte[] request, java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__sendSAMI_name);
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __sendSAMI_name, __cb);
        try
        {
            __result.__prepare(__sendSAMI_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            IceInternal.BasicStream __os = __result.__os();
            Ice.ByteSeqHelper.write(__os, request);
            __os.endWriteEncaps();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    public void end_sendSAMI(Ice.AsyncResult __result)
        throws drakkar.oar.slice.error.RequestException
    {
        Ice.AsyncResult.__check(__result, this, __sendSAMI_name);
        if(!__result.__wait())
        {
            try
            {
                __result.__throwUserException();
            }
            catch(drakkar.oar.slice.error.RequestException __ex)
            {
                throw __ex;
            }
            catch(Ice.UserException __ex)
            {
                throw new Ice.UnknownUserException(__ex.ice_name());
            }
        }
        IceInternal.BasicStream __is = __result.__is();
        __is.skipEmptyEncaps();
    }

    public boolean
    sendSAMI_async(drakkar.oar.slice.action.oneway.AMI_Send_sendSAMI __cb, byte[] request)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__sendSAMI_name);
            __r = begin_sendSAMI(request, null, false, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __sendSAMI_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public boolean
    sendSAMI_async(drakkar.oar.slice.action.oneway.AMI_Send_sendSAMI __cb, byte[] request, java.util.Map<String, String> __ctx)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__sendSAMI_name);
            __r = begin_sendSAMI(request, __ctx, true, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __sendSAMI_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public byte[]
    getAMID(byte[] request)
        throws drakkar.oar.slice.error.RequestException
    {
        return getAMID(request, null, false);
    }

    public byte[]
    getAMID(byte[] request, java.util.Map<String, String> __ctx)
        throws drakkar.oar.slice.error.RequestException
    {
        return getAMID(request, __ctx, true);
    }

    private byte[]
    getAMID(byte[] request, java.util.Map<String, String> __ctx, boolean __explicitCtx)
        throws drakkar.oar.slice.error.RequestException
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __checkTwowayOnly("getAMID");
                __delBase = __getDelegate(false);
                _ConfigurationDel __del = (_ConfigurationDel)__delBase;
                return __del.getAMID(request, __ctx);
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    private static final String __getAMID_name = "getAMID";

    public Ice.AsyncResult begin_getAMID(byte[] request)
    {
        return begin_getAMID(request, null, false, null);
    }

    public Ice.AsyncResult begin_getAMID(byte[] request, java.util.Map<String, String> __ctx)
    {
        return begin_getAMID(request, __ctx, true, null);
    }

    public Ice.AsyncResult begin_getAMID(byte[] request, Ice.Callback __cb)
    {
        return begin_getAMID(request, null, false, __cb);
    }

    public Ice.AsyncResult begin_getAMID(byte[] request, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_getAMID(request, __ctx, true, __cb);
    }

    public Ice.AsyncResult begin_getAMID(byte[] request, drakkar.oar.slice.action.twoway.Callback_Get_getAMID __cb)
    {
        return begin_getAMID(request, null, false, __cb);
    }

    public Ice.AsyncResult begin_getAMID(byte[] request, java.util.Map<String, String> __ctx, drakkar.oar.slice.action.twoway.Callback_Get_getAMID __cb)
    {
        return begin_getAMID(request, __ctx, true, __cb);
    }

    private Ice.AsyncResult begin_getAMID(byte[] request, java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__getAMID_name);
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __getAMID_name, __cb);
        try
        {
            __result.__prepare(__getAMID_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            IceInternal.BasicStream __os = __result.__os();
            Ice.ByteSeqHelper.write(__os, request);
            __os.endWriteEncaps();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    public byte[] end_getAMID(Ice.AsyncResult __result)
        throws drakkar.oar.slice.error.RequestException
    {
        Ice.AsyncResult.__check(__result, this, __getAMID_name);
        if(!__result.__wait())
        {
            try
            {
                __result.__throwUserException();
            }
            catch(drakkar.oar.slice.error.RequestException __ex)
            {
                throw __ex;
            }
            catch(Ice.UserException __ex)
            {
                throw new Ice.UnknownUserException(__ex.ice_name());
            }
        }
        byte[] __ret;
        IceInternal.BasicStream __is = __result.__is();
        __is.startReadEncaps();
        __ret = Ice.ByteSeqHelper.read(__is);
        __is.endReadEncaps();
        return __ret;
    }

    public boolean
    getAMID_async(drakkar.oar.slice.action.twoway.AMI_Get_getAMID __cb, byte[] request)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__getAMID_name);
            __r = begin_getAMID(request, null, false, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __getAMID_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public boolean
    getAMID_async(drakkar.oar.slice.action.twoway.AMI_Get_getAMID __cb, byte[] request, java.util.Map<String, String> __ctx)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__getAMID_name);
            __r = begin_getAMID(request, __ctx, true, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __getAMID_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public byte[]
    getSAMI(byte[] request)
        throws drakkar.oar.slice.error.RequestException
    {
        return getSAMI(request, null, false);
    }

    public byte[]
    getSAMI(byte[] request, java.util.Map<String, String> __ctx)
        throws drakkar.oar.slice.error.RequestException
    {
        return getSAMI(request, __ctx, true);
    }

    private byte[]
    getSAMI(byte[] request, java.util.Map<String, String> __ctx, boolean __explicitCtx)
        throws drakkar.oar.slice.error.RequestException
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __checkTwowayOnly("getSAMI");
                __delBase = __getDelegate(false);
                _ConfigurationDel __del = (_ConfigurationDel)__delBase;
                return __del.getSAMI(request, __ctx);
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    private static final String __getSAMI_name = "getSAMI";

    public Ice.AsyncResult begin_getSAMI(byte[] request)
    {
        return begin_getSAMI(request, null, false, null);
    }

    public Ice.AsyncResult begin_getSAMI(byte[] request, java.util.Map<String, String> __ctx)
    {
        return begin_getSAMI(request, __ctx, true, null);
    }

    public Ice.AsyncResult begin_getSAMI(byte[] request, Ice.Callback __cb)
    {
        return begin_getSAMI(request, null, false, __cb);
    }

    public Ice.AsyncResult begin_getSAMI(byte[] request, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_getSAMI(request, __ctx, true, __cb);
    }

    public Ice.AsyncResult begin_getSAMI(byte[] request, drakkar.oar.slice.action.twoway.Callback_Get_getSAMI __cb)
    {
        return begin_getSAMI(request, null, false, __cb);
    }

    public Ice.AsyncResult begin_getSAMI(byte[] request, java.util.Map<String, String> __ctx, drakkar.oar.slice.action.twoway.Callback_Get_getSAMI __cb)
    {
        return begin_getSAMI(request, __ctx, true, __cb);
    }

    private Ice.AsyncResult begin_getSAMI(byte[] request, java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__getSAMI_name);
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __getSAMI_name, __cb);
        try
        {
            __result.__prepare(__getSAMI_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            IceInternal.BasicStream __os = __result.__os();
            Ice.ByteSeqHelper.write(__os, request);
            __os.endWriteEncaps();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    public byte[] end_getSAMI(Ice.AsyncResult __result)
        throws drakkar.oar.slice.error.RequestException
    {
        Ice.AsyncResult.__check(__result, this, __getSAMI_name);
        if(!__result.__wait())
        {
            try
            {
                __result.__throwUserException();
            }
            catch(drakkar.oar.slice.error.RequestException __ex)
            {
                throw __ex;
            }
            catch(Ice.UserException __ex)
            {
                throw new Ice.UnknownUserException(__ex.ice_name());
            }
        }
        byte[] __ret;
        IceInternal.BasicStream __is = __result.__is();
        __is.startReadEncaps();
        __ret = Ice.ByteSeqHelper.read(__is);
        __is.endReadEncaps();
        return __ret;
    }

    public boolean
    getSAMI_async(drakkar.oar.slice.action.twoway.AMI_Get_getSAMI __cb, byte[] request)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__getSAMI_name);
            __r = begin_getSAMI(request, null, false, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __getSAMI_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public boolean
    getSAMI_async(drakkar.oar.slice.action.twoway.AMI_Get_getSAMI __cb, byte[] request, java.util.Map<String, String> __ctx)
    {
        Ice.AsyncResult __r;
        try
        {
            __checkTwowayOnly(__getSAMI_name);
            __r = begin_getSAMI(request, __ctx, true, __cb);
        }
        catch(Ice.TwowayOnlyException ex)
        {
            __r = new IceInternal.OutgoingAsync(this, __getSAMI_name, __cb);
            __r.__exceptionAsync(ex);
        }
        return __r.sentSynchronously();
    }

    public static ConfigurationPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        ConfigurationPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ConfigurationPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::drakkar::stern::slice::config::Configuration"))
                {
                    ConfigurationPrxHelper __h = new ConfigurationPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ConfigurationPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        ConfigurationPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ConfigurationPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::drakkar::stern::slice::config::Configuration", __ctx))
                {
                    ConfigurationPrxHelper __h = new ConfigurationPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ConfigurationPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ConfigurationPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::drakkar::stern::slice::config::Configuration"))
                {
                    ConfigurationPrxHelper __h = new ConfigurationPrxHelper();
                    __h.__copyFrom(__bb);
                    __d = __h;
                }
            }
            catch(Ice.FacetNotExistException ex)
            {
            }
        }
        return __d;
    }

    public static ConfigurationPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        ConfigurationPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::drakkar::stern::slice::config::Configuration", __ctx))
                {
                    ConfigurationPrxHelper __h = new ConfigurationPrxHelper();
                    __h.__copyFrom(__bb);
                    __d = __h;
                }
            }
            catch(Ice.FacetNotExistException ex)
            {
            }
        }
        return __d;
    }

    public static ConfigurationPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        ConfigurationPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ConfigurationPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                ConfigurationPrxHelper __h = new ConfigurationPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static ConfigurationPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ConfigurationPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            ConfigurationPrxHelper __h = new ConfigurationPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _ConfigurationDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _ConfigurationDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, ConfigurationPrx v)
    {
        __os.writeProxy(v);
    }

    public static ConfigurationPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            ConfigurationPrxHelper result = new ConfigurationPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
