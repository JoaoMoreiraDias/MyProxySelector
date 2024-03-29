package com.btr.proxy.selector.whitelist;

import java.net.URI;
import com.btr.proxy.util.UriFilter;

/*****************************************************************************
 * Tests if a host name of a given URI matches some criteria. 
 *
 * @author Bernd Rosstauscher (proxyvole@rosstauscher.de) Copyright 2009
 ****************************************************************************/

public class HostnameFilter implements UriFilter {

	public enum Mode {BEGINS_WITH, ENDS_WITH, REGEX}
	
	private String matchTo;
	private Mode mode;
	
	/*************************************************************************
	 * Constructor
	 * @param mode the filter mode.
	 * @param matchTo the match criteria.
	 ************************************************************************/
	
	public HostnameFilter(Mode mode, String matchTo) {
		super();
		this.mode = mode;
		this.matchTo = matchTo.toLowerCase();
	}
	
	/*************************************************************************
	 * accept
	 * @see com.btr.proxy.util.UriFilter#accept(java.net.URI)
	 ************************************************************************/
	
	public boolean accept(URI uri) {
		if (uri == null || uri.getAuthority() == null) {
			return false;
		}
		
		String host = uri.getAuthority();
		
		// Strip away port.
		int index = host.indexOf(':');
		if (index >= 0) {
			host = host.substring(0, index);
		}

		switch (this.mode) {
			case BEGINS_WITH :
				return host.toLowerCase().startsWith(this.matchTo);
			case ENDS_WITH :
				return host.toLowerCase().endsWith(this.matchTo);
			case REGEX :
				return host.toLowerCase().matches(this.matchTo);
		}
		return false;
	}
	
}
