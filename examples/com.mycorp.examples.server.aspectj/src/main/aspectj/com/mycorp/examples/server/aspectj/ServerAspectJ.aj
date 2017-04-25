/**
 *
 */
package com.mycorp.examples.server.aspectj;

import java.util.List;

import org.glassfish.jersey.server.model.Parameter;
import java.lang.reflect.Method;

/**
 * @author admin
 *
 */
public aspect ServerAspectJ {
	// Define a Pointcut is
	// collection of JoinPoint call sayHello of class HelloAspectJDemo.
	pointcut callSayHello(): call(public static List<Parameter>  org.glassfish.jersey.server.model.Parameter.create(Class, Class, Method, boolean))
	 && within(org.glassfish.jersey.server.model.Parameter)	;

	before() : callSayHello() {
		System.out.println("AspectJ: before callSayHello MY"); //$NON-NLS-1$
	}

	after() : callSayHello()  {
		System.out.println("AspectJ: after callSayHello MY"); //$NON-NLS-1$
	}
}
