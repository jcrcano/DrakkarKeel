// **********************************************************************
//
// Copyright (c) 2003-2010 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.4.0

package drakkar.oar.slice.action;

// <auto-generated>
//
// Generated from file `CollaborativeOperations.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>


public final class RCPOperationsHolder extends Ice.ObjectHolderBase<RCPOperations>
{
    public
    RCPOperationsHolder()
    {
    }

    public
    RCPOperationsHolder(RCPOperations value)
    {
        this.value = value;
    }

    public void
    patch(Ice.Object v)
    {
        try
        {
            value = (RCPOperations)v;
        }
        catch(ClassCastException ex)
        {
            IceInternal.Ex.throwUOE(type(), v.ice_id());
        }
    }

    public String
    type()
    {
        return _RCPOperationsDisp.ice_staticId();
    }
}