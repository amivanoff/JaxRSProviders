/**
 *
 */
package com.mycorp.examples.server.aspectj;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Encoded;

import org.glassfish.jersey.server.model.AnnotatedMethod;
import org.glassfish.jersey.server.model.Parameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.glassfish.jersey.server.model.annotations.helper.AnnotationsUtil;
import java.lang.reflect.Type;
import java.lang.annotation.Annotation;

/**
 * @author admin
 *
 */
public aspect ServerAspectJ {

	private static String TAG = ServerAspectJ.class.getCanonicalName();
	private static Logger log = Logger.getLogger(TAG);

	List<Parameter> around(Class<?> concreteClass, Class<?> declaringClass, Method javaMethod, boolean keepEncoded) :
		call(public static List<Parameter>  org.glassfish.jersey.server.model.Parameter.create(Class, Class, Method, boolean))
	 	 && args(concreteClass, declaringClass, javaMethod, keepEncoded)
	{
		log.info(concreteClass.toString() + " " + declaringClass.toString() + " " + javaMethod.toString()); //$NON-NLS-1$//$NON-NLS-2$

		AnnotatedMethod method = new AnnotatedMethod(javaMethod);

		if (AnnotationsUtil.isNeedToModify(method, javaMethod)) {
			return AnnotationsUtil.createWithoutAnnotations(javaMethod, (null != method.getAnnotation(Encoded.class)) || keepEncoded);
		}

		try {
			Class[] paramTypes = new Class[] { Class.class, Class.class, boolean.class, Class[].class, Type[].class, Annotation[][].class };
			Method methodCreate = Parameter.class.getDeclaredMethod("create", paramTypes); //$NON-NLS-1$
			methodCreate.setAccessible(true);
			return (List<Parameter>)methodCreate.invoke(Parameter.class, concreteClass, declaringClass, ((null != method.getAnnotation(Encoded.class)) || keepEncoded), method.getParameterTypes(), method.getGenericParameterTypes(), method.getParameterAnnotations());
		}
		catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
