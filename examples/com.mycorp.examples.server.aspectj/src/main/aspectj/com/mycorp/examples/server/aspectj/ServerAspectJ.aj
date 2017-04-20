/**
 *
 */
package com.mycorp.examples.server.aspectj;

import com.mycorp.examples.server.test.java.Test;
import org.glassfish.jersey.server.model.Parameter;

/**
 * @author admin
 *
 */
public aspect ServerAspectJ {
	// Define a Pointcut is
	// collection of JoinPoint call sayHello of class HelloAspectJDemo.
	pointcut callSayHello(): call(* Test.sayHello(..) /*Parameter.create(..)*/);

	before() : callSayHello() {
		System.out.println("AspectJ: before callSayHello MY"); //$NON-NLS-1$
	}

	after() : callSayHello()  {
		System.out.println("AspectJ: after callSayHello MY"); //$NON-NLS-1$
	}
}
