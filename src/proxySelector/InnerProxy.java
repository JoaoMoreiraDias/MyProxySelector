
 // Inner class que reprrsenta um Proxy com mais alguns dados
package proxySelector;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

public class InnerProxy {
	Proxy proxy;
	SocketAddress addr;
	// Contador de erros na ligação ao Proxy.
	int failedCount = 0;

	public InnerProxy(Proxy string, SocketAddress i) {
		this.proxy = string;
		this.addr = i;
	}

	InnerProxy(InetSocketAddress a) {
		addr = a;
		proxy = new Proxy(Proxy.Type.HTTP, a);
	}

	SocketAddress address() {
		return addr;
	}

	Proxy toProxy() {
		return proxy;
	}

	int failed() {
		return ++failedCount;
	}
}