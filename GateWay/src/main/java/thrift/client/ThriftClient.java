package thrift.client;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import thrift.test.Bonk;

public class ThriftClient {

	public static void main(String[] args) throws TException {
		TTransport transport = new TSocket("localhost", 8080);
        transport.open();
        
        TProtocol protocol = new  TBinaryProtocol(transport);

        Bonk b = new Bonk();
        b.setType(100);
        b.setMessage("thrift client");
        
        b.write(protocol);
        
        transport.close();
	}

}
