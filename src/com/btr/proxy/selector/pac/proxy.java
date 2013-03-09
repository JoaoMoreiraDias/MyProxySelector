package com.btr.proxy.selector.pac;
import java.lang.reflect.Method;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class proxy {

	/**
	 * @param args
	 */
	public proxy() {
		// First we get a JavaScript engine
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("JavaScript");

		// Create a Java binding to be used from the JavaScript execution
		engine.put("MyJavaPacImpl", new PacScriptMethods());

		// Add the required JavaScript methods by bridging to the Java binding
		for(Method method : PacScriptMethods.class.getMethods())
		{
			String bridgeFunctionDef = defineBridgeFunction(
					method.getName(),
					method.getParameterTypes().length);
			engine.eval(bridgeFunctionDef);
		}

		// The engine is now ready to be used to evaluate the PAC script
		// (passed in as a string)
		engine.eval(pacScript);
		// Now let's use the FindProxyForURL function to get the proxy
		// for the URL we want to access
		Invocable invocableEngine = (Invocable) engine;
		Object resultObj = invocableEngine.invokeFunction(
				"FindProxyForURL",
				url.toString(),
				url.getHost());
		String proxyConfig = String.valueOf(resultObj);*/

	}

}
