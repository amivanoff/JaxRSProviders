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
	pointcut callSayHello(Class concreteClass, Class declaringClass, Method javaMethod, boolean keepEncoded):
		call(public static List<Parameter>  org.glassfish.jersey.server.model.Parameter.create(Class, Class, Method, boolean))
	 	 && args(concreteClass, declaringClass, javaMethod, keepEncoded)	;

	before(Class concreteClass, Class declaringClass, Method javaMethod, boolean keepEncoded) :
		callSayHello(concreteClass, declaringClass, javaMethod, keepEncoded) {
		System.out.println(concreteClass.toString() + " " + declaringClass.toString() + " " + javaMethod.toString()); //$NON-NLS-1$
	}

	after(Class concreteClass, Class declaringClass, Method javaMethod, boolean keepEncoded) :
		callSayHello(concreteClass, declaringClass, javaMethod, keepEncoded)  {
//			System.out.println("AspectJ: after callSayHello MY"); //$NON-NLS-1$
	}
}
