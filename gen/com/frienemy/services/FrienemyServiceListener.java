/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/RossChapman/Documents/workspace/Frienemy/aidl/com/frienemy/services/FrienemyServiceListener.aidl
 */
package com.frienemy.services;
public interface FrienemyServiceListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.frienemy.services.FrienemyServiceListener
{
private static final java.lang.String DESCRIPTOR = "com.frienemy.services.FrienemyServiceListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.frienemy.services.FrienemyServiceListener interface,
 * generating a proxy if needed.
 */
public static com.frienemy.services.FrienemyServiceListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.frienemy.services.FrienemyServiceListener))) {
return ((com.frienemy.services.FrienemyServiceListener)iin);
}
return new com.frienemy.services.FrienemyServiceListener.Stub.Proxy(obj);
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
case TRANSACTION_handleFriendsUpdated:
{
data.enforceInterface(DESCRIPTOR);
this.handleFriendsUpdated();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.frienemy.services.FrienemyServiceListener
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
public void handleFriendsUpdated() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_handleFriendsUpdated, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_handleFriendsUpdated = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void handleFriendsUpdated() throws android.os.RemoteException;
}
