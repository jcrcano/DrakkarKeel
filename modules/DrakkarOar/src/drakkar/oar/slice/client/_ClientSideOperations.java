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


public interface _ClientSideOperations extends drakkar.oar.slice.action.twoway._GetOperations
{
    void _notify(byte[] response, Ice.Current __current);

    void disconnect(Ice.Current __current);
}
