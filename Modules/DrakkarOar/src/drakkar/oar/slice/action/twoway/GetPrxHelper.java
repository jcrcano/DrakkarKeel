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


public final class GetPrxHelper extends Ice.ObjectPrxHelperBase implements GetPrx
{
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
                _GetDel __del = (_GetDel)__delBase;
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

    public Ice.AsyncResult begin_getAMID(byte[] request, Callback_Get_getAMID __cb)
    {
        return begin_getAMID(request, null, false, __cb);
    }

    public Ice.AsyncResult begin_getAMID(byte[] request, java.util.Map<String, String> __ctx, Callback_Get_getAMID __cb)
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
    getAMID_async(AMI_Get_getAMID __cb, byte[] request)
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
    getAMID_async(AMI_Get_getAMID __cb, byte[] request, java.util.Map<String, String> __ctx)
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
                _GetDel __del = (_GetDel)__delBase;
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

    public Ice.AsyncResult begin_getSAMI(byte[] request, Callback_Get_getSAMI __cb)
    {
        return begin_getSAMI(request, null, false, __cb);
    }

    public Ice.AsyncResult begin_getSAMI(byte[] request, java.util.Map<String, String> __ctx, Callback_Get_getSAMI __cb)
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
    getSAMI_async(AMI_Get_getSAMI __cb, byte[] request)
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
    getSAMI_async(AMI_Get_getSAMI __cb, byte[] request, java.util.Map<String, String> __ctx)
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

    public static GetPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        GetPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (GetPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::drakkar::oar::slice::action::twoway::Get"))
                {
                    GetPrxHelper __h = new GetPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static GetPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        GetPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (GetPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::drakkar::oar::slice::action::twoway::Get", __ctx))
                {
                    GetPrxHelper __h = new GetPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static GetPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        GetPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::drakkar::oar::slice::action::twoway::Get"))
                {
                    GetPrxHelper __h = new GetPrxHelper();
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

    public static GetPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        GetPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::drakkar::oar::slice::action::twoway::Get", __ctx))
                {
                    GetPrxHelper __h = new GetPrxHelper();
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

    public static GetPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        GetPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (GetPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                GetPrxHelper __h = new GetPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static GetPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        GetPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            GetPrxHelper __h = new GetPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _GetDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _GetDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, GetPrx v)
    {
        __os.writeProxy(v);
    }

    public static GetPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            GetPrxHelper result = new GetPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
