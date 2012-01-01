/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/rosschapman/Code/Android Projects/Frienemy/aidl/com/frienemy/services/FrienemyServiceAPI.aidl
 */
package com.frienemy.services;
public interface FrienemyServiceAPI extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.frienemy.services.FrienemyServiceAPI
{
private static final java.lang.String DESCRIPTOR = "com.frienemy.services.FrienemyServiceAPI";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.frienemy.services.FrienemyServiceAPI interface,
 * generating a proxy if needed.
 */
public static com.frienemy.services.FrienemyServiceAPI asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.frienemy.services.FrienemyServiceAPI))) {
return ((com.frienemy.services.FrienemyServiceAPI)iin);
}
return new com.frienemy.services.FrienemyServiceAPI.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_addListener:
{
data.enforceInterface(DESCRIPTOR);
com.frienemy.services.FrienemyServiceListener _arg0;
_arg0 = com.frienemy.services.FrienemyServiceListener.Stub.asInterface(data.readStrongBinder());
this.addListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_removeListener:
{
data.enforceInterface(DESCRIPTOR);
com.frienemy.services.FrienemyServiceListener _arg0;
_arg0 = com.frienemy.services.FrienemyServiceListener.Stub.asInterface(data.readStrongBinder());
this.removeListener(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.frienemy.services.FrienemyServiceAPI
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void addListener(com.frienemy.services.FrienemyServiceListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_addListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void removeListener(com.frienemy.services.FrienemyServiceListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_removeListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_addListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_removeListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
}
public void addListener(com.frienemy.services.FrienemyServiceListener listener) throws android.os.RemoteException;
public void removeListener(com.frienemy.services.FrienemyServiceListener listener) throws android.os.RemoteException;
}
