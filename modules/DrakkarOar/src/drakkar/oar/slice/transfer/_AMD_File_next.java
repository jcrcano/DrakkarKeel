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


final class _AMD_File_next extends IceInternal.IncomingAsync implements AMD_File_next
{
    public
    _AMD_File_next(IceInternal.Incoming in)
    {
        super(in);
    }

    public void
    ice_response(byte[] __ret)
    {
        if(__validateResponse(true))
        {
            try
            {
                IceInternal.BasicStream __os = this.__os();
                Ice.ByteSeqHelper.write(__os, __ret);
            }
            catch(Ice.LocalException __ex)
            {
                ice_exception(__ex);
            }
            __response(true);
        }
    }
}