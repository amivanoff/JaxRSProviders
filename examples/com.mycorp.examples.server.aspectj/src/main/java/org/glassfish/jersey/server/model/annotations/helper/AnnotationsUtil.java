/**
 *
 */
package org.glassfish.jersey.server.model.annotations.helper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.model.annotaions.instance.CookieParamInstance;
import org.glassfish.jersey.server.model.annotaions.instance.HeaderParamInstance;
import org.glassfish.jersey.server.model.annotaions.instance.PathParamInstance;
import org.glassfish.jersey.server.model.annotaions.instance.QueryParamInstance;

/**
 * @author zagrebaev
 *
 */
public class AnnotationsUtil {

	private static final String URL =  "url";
	private static final String HEADER =  "header";
	private static final String QUERY =  "query";
	private static final String COOKIE =  "cookie";

	public static List<Parameter> createWithoutAnnotations(Method method, boolean paramEncoded) {

		List<Parameter> parList = new ArrayList<>();

		Parameter parameter = null;

		try {

			Class<?>[] paramTypes = new Class[] { Annotation[].class, Annotation.class, Parameter.Source.class, String.class, Class.class, Type.class, boolean.class };
			Constructor<Parameter> parameterConstructor = Parameter.class.getConstructor(paramTypes);
			parameterConstructor.setAccessible(true);

			for (java.lang.reflect.Parameter p : method.getParameters()) {
				if (p.getName().startsWith("url")) //$NON-NLS-1$
				{
					PathParamInstance a = new PathParamInstance();
					a.setValue(p.getName());
					Annotation[] as = new PathParamInstance[1];
					as[0] = a;
					parameter = parameterConstructor.newInstance(as, a, Parameter.Source.PATH, p.getName(), p.getType(), p.getType(), paramEncoded, null);

					parList.add(parameter);
				}
				else if (p.getName().startsWith("header")) //$NON-NLS-1$
				{
					String header = createHeaderName(p.getName());

					HeaderParamInstance a = new HeaderParamInstance();
					a.setValue(header);
					Annotation[] as = new HeaderParamInstance[1];
					as[0] = a;

					parameter = parameterConstructor.newInstance(as, a, Parameter.Source.HEADER, header, p.getType(), p.getType(), paramEncoded, null);

					parList.add(parameter);
				}
				else if (p.getName().startsWith("query")) //$NON-NLS-1$
				{
					String query = createQueryName(p.getName());
					QueryParamInstance q = new QueryParamInstance();
					q.setValue(query);
					Annotation[] as = new QueryParamInstance[1];
					as[0] = q;

					parameter = parameterConstructor.newInstance(as, q, Parameter.Source.QUERY, query, p.getType(), p.getType(), paramEncoded, null);

					parList.add(parameter);
				}
				else if (p.getName().startsWith("cookie")) //$NON-NLS-1$
				{
					String cookie = createCookieName(p.getName());
					CookieParamInstance c = new CookieParamInstance();
					c.setValue(cookie);
					Annotation[] as = new CookieParamInstance[1];
					as[0] = c;

					parameter = parameterConstructor.newInstance(as, c, Parameter.Source.COOKIE, cookie, p.getType(), p.getType(), paramEncoded, null);

					parList.add(parameter);
				}
				else {
					parameter = parameterConstructor.newInstance(p.getAnnotations(), null, Parameter.Source.ENTITY, null, p.getType(), p.getType(), paramEncoded, null);

					parList.add(parameter);
				}
			}
		}
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException| SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return parList;
	}

	private static String createCookieName(String query) {
		query = query.substring("cookie".length()); //$NON-NLS-1$
		return query;
	}

	private static String createQueryName(String query) {
		query = query.substring("query".length()); //$NON-NLS-1$
		query = Character.toLowerCase(query.charAt(0)) + query.substring(1);
		return query;
	}

	private static String createHeaderName(String header) {

		header = header.substring("header".length()); //$NON-NLS-1$

		String[] partsOfHeader = header.split("(?=\\p{Lu})"); //$NON-NLS-1$

		header = ""; //$NON-NLS-1$

		for (int i = 0; i < partsOfHeader.length; i++) {

			header = header + partsOfHeader[i].toLowerCase();

			if (i + 1 != partsOfHeader.length) {
				header = header + "-"; //$NON-NLS-1$
			}
		}

		return header;
	}

}
