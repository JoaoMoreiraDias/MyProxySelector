package proxySelector;

import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;

public class MyProxySelector extends ProxySelector {
	// Referencia do valor por defeito anterior, defsel=default selector
	ProxySelector defsel = null;
	InnerProxy UPT = new InnerProxy(new InetSocketAddress("uportu.upt.pt", 3128));
	InnerProxy Original = new InnerProxy(new InetSocketAddress("webcache.mydomain.com", 8080));
	//System.setProperty("proxy.uportu.pt", "3128"); <- Supostament funciona so que não dá. Era uma alternativa.


	// Uma lista de Proxies, indexados por address.
	HashMap<SocketAddress, InnerProxy> proxies = new HashMap<SocketAddress, InnerProxy>();

	MyProxySelector(ProxySelector def) {
		// Save the previous default
		defsel = def;

		// Encher o HashMap (lista de proxies)
		InnerProxy i = UPT;
		proxies.put(i.address(), i);
		i = Original;
		proxies.put(i.address(), i);
	}

	/*
	 * This is the method that the handlers will call.
	 * Retorna a lista de proxies.
	 */
	public java.util.List<Proxy> select(URI uri) {
		if (uri == null) {
			throw new IllegalArgumentException("URI não pode ser nullo!");
		}

		String protocol = uri.getScheme();
		if ("http".equalsIgnoreCase(protocol) || "https".equalsIgnoreCase(protocol)) {
			ArrayList<Proxy> l = new ArrayList<Proxy>();
			for (InnerProxy p : proxies.values()) {
				l.add(p.toProxy());
			}
			return l;
		}
		if (defsel != null) {
			return defsel.select(uri);
		} else {
			ArrayList<Proxy> l = new ArrayList<Proxy>();
			l.add(Proxy.NO_PROXY);
			return l;
		}
	}

	// Metudo chamado se houver uma erro na ligação ao proxie.
	public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
		// Let's stick to the specs again.
		if (uri == null || sa == null || ioe == null) {
			throw new IllegalArgumentException("URI, SA e IOE não podem ser nullos!");
		}

		// Fazer lookup ao proxy
		InnerProxy p = proxies.get(sa); 
		if (p != null) {
			// Se o proxy é nosso, e falhou mais que 3 vezes, é removido da lista
			if (p.failed() >= 3)
				proxies.remove(sa);
		} else {
			/*
			 * Not one of ours, let's delegate to the default.
			 */
			if (defsel != null)
				defsel.connectFailed(uri, sa, ioe);
		}
	}
}