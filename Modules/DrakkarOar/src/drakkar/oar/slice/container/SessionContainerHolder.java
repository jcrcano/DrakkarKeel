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


public final class SessionContainerHolder extends Ice.ObjectHolderBase<SessionContainer>
{
    public
    SessionContainerHolder()
    {
    }

    public
    SessionContainerHolder(SessionContainer value)
    {
        this.value = value;
    }

    public void
    patch(Ice.Object v)
    {
        try
        {
            value = (SessionContainer)v;
        }
        catch(ClassCastException ex)
        {
            IceInternal.Ex.throwUOE(type(), v.ice_id());
        }
    }

    public String
    type()
    {
        return _SessionContainerDisp.ice_staticId();
    }
}