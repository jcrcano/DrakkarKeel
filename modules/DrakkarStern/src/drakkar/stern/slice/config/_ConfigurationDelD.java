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


public final class _ConfigurationDelD extends Ice._ObjectDelD implements _ConfigurationDel
{
    public void
    sendAMID(final byte[] request, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper,
               drakkar.oar.slice.error.RequestException
    {
        throw new Ice.CollocationOptimizationException();
    }

    public void
    sendSAMI(final byte[] request, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper,
               drakkar.oar.slice.error.RequestException
    {
        final Ice.Current __current = new Ice.Current();
        __initCurrent(__current, "sendSAMI", Ice.OperationMode.Normal, __ctx);
        IceInternal.Direct __direct = null;
        try
        {
            __direct = new IceInternal.Direct(__current)
            {
                public Ice.DispatchStatus run(Ice.Object __obj)
                {
                    Configuration __servant = null;
                    try
                    {
                        __servant = (Configuration)__obj;
                    }
                    catch(ClassCastException __ex)
                    {
                        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
                    }
                    try
                    {
                        __servant.sendSAMI(request, __current);
                        return Ice.DispatchStatus.DispatchOK;
                    }
                    catch(Ice.UserException __ex)
                    {
                        setUserException(__ex);
                        return Ice.DispatchStatus.DispatchUserException;
                    }
                }
            };
            try
            {
                Ice.DispatchStatus __status = __direct.servant().__collocDispatch(__direct);
                if(__status == Ice.DispatchStatus.DispatchUserException)
                {
                    __direct.throwUserException();
                }
                assert __status == Ice.DispatchStatus.DispatchOK;
            }
            finally
            {
                __direct.destroy();
            }
        }
        catch(drakkar.oar.slice.error.RequestException | Ice.SystemException __ex)
        {
            throw __ex;
        }
        catch(java.lang.Throwable __ex)
        {
            IceInternal.LocalExceptionWrapper.throwWrapper(__ex);
        }
    }

    public byte[]
    getAMID(final byte[] request, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper,
               drakkar.oar.slice.error.RequestException
    {
        throw new Ice.CollocationOptimizationException();
    }

    public byte[]
    getSAMI(final byte[] request, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper,
               drakkar.oar.slice.error.RequestException
    {
        final Ice.Current __current = new Ice.Current();
        __initCurrent(__current, "getSAMI", Ice.OperationMode.Normal, __ctx);
        final Ice.ByteSeqHolder __result = new Ice.ByteSeqHolder();
        IceInternal.Direct __direct = null;
        try
        {
            __direct = new IceInternal.Direct(__current)
            {
                public Ice.DispatchStatus run(Ice.Object __obj)
                {
                    Configuration __servant = null;
                    try
                    {
                        __servant = (Configuration)__obj;
                    }
                    catch(ClassCastException __ex)
                    {
                        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
                    }
                    try
                    {
                        __result.value = __servant.getSAMI(request, __current);
                        return Ice.DispatchStatus.DispatchOK;
                    }
                    catch(Ice.UserException __ex)
                    {
                        setUserException(__ex);
                        return Ice.DispatchStatus.DispatchUserException;
                    }
                }
            };
            try
            {
                Ice.DispatchStatus __status = __direct.servant().__collocDispatch(__direct);
                if(__status == Ice.DispatchStatus.DispatchUserException)
                {
                    __direct.throwUserException();
                }
                assert __status == Ice.DispatchStatus.DispatchOK;
                return __result.value;
            }
            finally
            {
                __direct.destroy();
            }
        }
        catch(drakkar.oar.slice.error.RequestException | Ice.SystemException __ex)
        {
            throw __ex;
        }
        catch(java.lang.Throwable __ex)
        {
            IceInternal.LocalExceptionWrapper.throwWrapper(__ex);
        }
        return __result.value;
    }
}
