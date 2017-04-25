/**
 *
 */
package org.eclipse.ecf.provider.aspectj;

import java.lang.reflect.Method;
import java.util.List;
import org.glassfish.jersey.server.model.Parameter;

/**
 * @author zagrebaev
 *
 */
public aspect ParameterEditor {

	// Using target: define object on which the method is called
	// Using args: define args on the method is called
	// Using within: to restrict JoinPoint within ClassTest02
	pointcut callAnnotatedMethod()
                       : call(public static List<Parameter> org.glassfish.jersey.server.model.Parameter.create(
                           Class ,
                           Class ,
                           Method ,
                           boolean ))
                                && within(org.glassfish.jersey.server.model.Parameter)  ;


	after() :  callAnnotatedMethod() {
		System.err.println("Before call move( )"); //$NON-NLS-1$
//        System.out.println(point.toString());
	}

//	pointcut callAnnotatedMethod()
//    : call(public String hello() )
//               ;
//
//	before() :  callAnnotatedMethod() {
//		System.err.println("Before call move(" + ")"); //$NON-NLS-1$//$NON-NLS-2$
////System.out.println(point.toString());
//	}

}
