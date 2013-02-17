package testSandbox;

import java.io.*;
import java.lang.reflect.Method;
import java.rmi.*;
import java.rmi.server.*;

public final class Hello_Stub extends RemoteStub
    implements HelloInterface, Remote
{

    public Hello_Stub()
    {
    }

    public Hello_Stub(RemoteRef remoteref)
    {
        super(remoteref);
    }

    static Class _mthclass$(String s)
    {
        try
        {
            return Class.forName(s);
        }
        catch(ClassNotFoundException classnotfoundexception)
        {
            throw new NoClassDefFoundError(classnotfoundexception.getMessage());
        }
    }

    public String sayHello(String s)
        throws RemoteException
    {
        try
        {
            if(useNewInvoke)
            {
                Object obj = super.ref.invoke(this, $method_sayHello_0, new Object[] {
                    s
                }, 0x742a8a860d6b7ee4L);
                return (String)obj;
            }
            RemoteCall remotecall = super.ref.newCall(this, operations, 0, 0x38bc97be171621e6L);
            try
            {
                ObjectOutput objectoutput = remotecall.getOutputStream();
                objectoutput.writeObject(s);
            }
            catch(IOException ioexception)
            {
                throw new MarshalException("error marshalling arguments", ioexception);
            }
            super.ref.invoke(remotecall);
            String s1;
            try
            {
                ObjectInput objectinput = remotecall.getInputStream();
                s1 = (String)objectinput.readObject();
            }
            catch(IOException ioexception1)
            {
                throw new UnmarshalException("error unmarshalling return", ioexception1);
            }
            catch(ClassNotFoundException classnotfoundexception)
            {
                throw new UnmarshalException("error unmarshalling return", classnotfoundexception);
            }
            finally
            {
                super.ref.done(remotecall);
            }
            return s1;
        }
        catch(RuntimeException runtimeexception)
        {
            throw runtimeexception;
        }
        catch(RemoteException remoteexception)
        {
            throw remoteexception;
        }
        catch(Exception exception)
        {
            throw new UnexpectedException("undeclared checked exception", exception);
        }
    }

    private static final Operation operations[] = {
        new Operation("java.lang.String sayHello(java.lang.String)")
    };
    private static final long interfaceHash = 0x38bc97be171621e6L;
    private static final long serialVersionUID = 2L;
    private static boolean useNewInvoke;
    private static Method $method_sayHello_0;

    static 
    {
        try
        {
            (java.rmi.server.RemoteRef.class).getMethod("invoke", new Class[] {
                java.rmi.Remote.class, java.lang.reflect.Method.class, java.lang.Object[].class, Long.TYPE
            });
            useNewInvoke = true;
            $method_sayHello_0 = (HelloInterface.class).getMethod("sayHello", new Class[] {
                java.lang.String.class
            });
        }
        catch(NoSuchMethodException _ex)
        {
            useNewInvoke = false;
        }
    }
}