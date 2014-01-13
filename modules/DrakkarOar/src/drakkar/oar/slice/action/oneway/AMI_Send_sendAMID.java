// **********************************************************************
//
// Copyright (c) 2003-2010 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.4.0

package drakkar.oar.slice.action.oneway;

// <auto-generated>
//
// Generated from file `Oneway.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>


public abstract class AMI_Send_sendAMID extends Callback_Send_sendAMID
{
    public abstract void ice_response();

    /**
     * ice_exception indicates to the caller that
     * the operation completed with an exception.
     * @param ex The Ice run-time exception to be raised.
     **/
    public abstract void ice_exception(Ice.LocalException ex);

    /**
     * ice_exception indicates to the caller that
     * the operation completed with an exception.
     * @param ex The user exception to be raised.
     **/
    public abstract void ice_exception(Ice.UserException ex);

    public final void response()
    {
        ice_response();
    }

    public final void exception(Ice.UserException __ex)
    {
        ice_exception(__ex);
    }

    public final void exception(Ice.LocalException __ex)
    {
        ice_exception(__ex);
    }

    @Override public final void sent(boolean sentSynchronously)
    {
        if(!sentSynchronously && this instanceof Ice.AMISentCallback)
        {
            ((Ice.AMISentCallback)this).ice_sent();
        }
    }
}
