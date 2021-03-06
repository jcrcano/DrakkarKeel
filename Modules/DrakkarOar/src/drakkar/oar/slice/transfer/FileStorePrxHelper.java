// **********************************************************************
//
// Copyright (c) 2003-2010 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.4.0

package drakkar.oar.slice.transfer;

// <auto-generated>
//
// Generated from file `FileTransfer.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>


public final class FileStorePrxHelper extends Ice.ObjectPrxHelperBase implements FileStorePrx
{
    public FilePrx
    read(String name, int num)
        throws FileAccessException
    {
        return read(name, num, null, false);
    }

    public FilePrx
    read(String name, int num, java.util.Map<String, String> __ctx)
        throws FileAccessException
    {
        return read(name, num, __ctx, true);
    }

    private FilePrx
    read(String name, int num, java.util.Map<String, String> __ctx, boolean __explicitCtx)
        throws FileAccessException
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
                __checkTwowayOnly("read");
                __delBase = __getDelegate(false);
                _FileStoreDel __del = (_FileStoreDel)__delBase;
                return __del.read(name, num, __ctx);
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

    private static final String __read_name = "read";

    public Ice.AsyncResult begin_read(String name, int num)
    {
        return begin_read(name, num, null, false, null);
    }

    public Ice.AsyncResult begin_read(String name, int num, java.util.Map<String, String> __ctx)
    {
        return begin_read(name, num, __ctx, true, null);
    }

    public Ice.AsyncResult begin_read(String name, int num, Ice.Callback __cb)
    {
        return begin_read(name, num, null, false, __cb);
    }

    public Ice.AsyncResult begin_read(String name, int num, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_read(name, num, __ctx, true, __cb);
    }

    public Ice.AsyncResult begin_read(String name, int num, Callback_FileStore_read __cb)
    {
        return begin_read(name, num, null, false, __cb);
    }

    public Ice.AsyncResult begin_read(String name, int num, java.util.Map<String, String> __ctx, Callback_FileStore_read __cb)
    {
        return begin_read(name, num, __ctx, true, __cb);
    }

    private Ice.AsyncResult begin_read(String name, int num, java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        __checkAsyncTwowayOnly(__read_name);
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __read_name, __cb);
        try
        {
            __result.__prepare(__read_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            IceInternal.BasicStream __os = __result.__os();
            __os.writeString(name);
            __os.writeInt(num);
            __os.endWriteEncaps();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    public FilePrx end_read(Ice.AsyncResult __result)
        throws FileAccessException
    {
        Ice.AsyncResult.__check(__result, this, __read_name);
        if(!__result.__wait())
        {
            try
            {
                __result.__throwUserException();
            }
            catch(FileAccessException __ex)
            {
                throw __ex;
            }
            catch(Ice.UserException __ex)
            {
                throw new Ice.UnknownUserException(__ex.ice_name());
            }
        }
        FilePrx __ret;
        IceInternal.BasicStream __is = __result.__is();
        __is.startReadEncaps();
        __ret = FilePrxHelper.__read(__is);
        __is.endReadEncaps();
        return __ret;
    }

    public void
    write(String name, int offset, byte[] bytes)
    {
        write(name, offset, bytes, null, false);
    }

    public void
    write(String name, int offset, byte[] bytes, java.util.Map<String, String> __ctx)
    {
        write(name, offset, bytes, __ctx, true);
    }

    private void
    write(String name, int offset, byte[] bytes, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __delBase = __getDelegate(false);
                _FileStoreDel __del = (_FileStoreDel)__delBase;
                __del.write(name, offset, bytes, __ctx);
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

    private static final String __write_name = "write";

    public Ice.AsyncResult begin_write(String name, int offset, byte[] bytes)
    {
        return begin_write(name, offset, bytes, null, false, null);
    }

    public Ice.AsyncResult begin_write(String name, int offset, byte[] bytes, java.util.Map<String, String> __ctx)
    {
        return begin_write(name, offset, bytes, __ctx, true, null);
    }

    public Ice.AsyncResult begin_write(String name, int offset, byte[] bytes, Ice.Callback __cb)
    {
        return begin_write(name, offset, bytes, null, false, __cb);
    }

    public Ice.AsyncResult begin_write(String name, int offset, byte[] bytes, java.util.Map<String, String> __ctx, Ice.Callback __cb)
    {
        return begin_write(name, offset, bytes, __ctx, true, __cb);
    }

    public Ice.AsyncResult begin_write(String name, int offset, byte[] bytes, Callback_FileStore_write __cb)
    {
        return begin_write(name, offset, bytes, null, false, __cb);
    }

    public Ice.AsyncResult begin_write(String name, int offset, byte[] bytes, java.util.Map<String, String> __ctx, Callback_FileStore_write __cb)
    {
        return begin_write(name, offset, bytes, __ctx, true, __cb);
    }

    private Ice.AsyncResult begin_write(String name, int offset, byte[] bytes, java.util.Map<String, String> __ctx, boolean __explicitCtx, IceInternal.CallbackBase __cb)
    {
        IceInternal.OutgoingAsync __result = new IceInternal.OutgoingAsync(this, __write_name, __cb);
        try
        {
            __result.__prepare(__write_name, Ice.OperationMode.Normal, __ctx, __explicitCtx);
            IceInternal.BasicStream __os = __result.__os();
            __os.writeString(name);
            __os.writeInt(offset);
            Ice.ByteSeqHelper.write(__os, bytes);
            __os.endWriteEncaps();
            __result.__send(true);
        }
        catch(Ice.LocalException __ex)
        {
            __result.__exceptionAsync(__ex);
        }
        return __result;
    }

    public void end_write(Ice.AsyncResult __result)
    {
        __end(__result, __write_name);
    }

    public static FileStorePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        FileStorePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (FileStorePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::drakkar::oar::slice::transfer::FileStore"))
                {
                    FileStorePrxHelper __h = new FileStorePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static FileStorePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        FileStorePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (FileStorePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::drakkar::oar::slice::transfer::FileStore", __ctx))
                {
                    FileStorePrxHelper __h = new FileStorePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static FileStorePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        FileStorePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::drakkar::oar::slice::transfer::FileStore"))
                {
                    FileStorePrxHelper __h = new FileStorePrxHelper();
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

    public static FileStorePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        FileStorePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::drakkar::oar::slice::transfer::FileStore", __ctx))
                {
                    FileStorePrxHelper __h = new FileStorePrxHelper();
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

    public static FileStorePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        FileStorePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (FileStorePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                FileStorePrxHelper __h = new FileStorePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static FileStorePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        FileStorePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            FileStorePrxHelper __h = new FileStorePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _FileStoreDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _FileStoreDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, FileStorePrx v)
    {
        __os.writeProxy(v);
    }

    public static FileStorePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            FileStorePrxHelper result = new FileStorePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
