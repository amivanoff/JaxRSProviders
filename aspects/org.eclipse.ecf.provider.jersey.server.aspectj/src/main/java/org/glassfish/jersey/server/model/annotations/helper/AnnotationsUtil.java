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
import java.util.logging.Logger;

import javax.ws.rs.HttpMethod;

import org.glassfish.jersey.server.model.AnnotatedMethod;
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

	private static final String URL_LITERAL = "url"; //$NON-NLS-1$
	private static final String HEADER_LITERAL = "header"; //$NON-NLS-1$
	private static final String QUERY_LITERAL = "query"; //$NON-NLS-1$
	private static final String COOKIE_LITERAL = "cookie"; //$NON-NLS-1$

	private static String TAG = AnnotationsUtil.class.getCanonicalName();
	private static Logger log = Logger.getLogger(TAG);

	public static List<Parameter> createWithoutAnnotations(Method javaMethod, boolean paramEncoded) {

		List<Parameter> parList = new ArrayList<>();

		Parameter parameter = null;

		log.info(coolectMethodParametersInfo(javaMethod));

		try {

			Class<?>[] paramTypes = new Class[] { Annotation[].class, Annotation.class, Parameter.Source.class, String.class, Class.class, Type.class, boolean.class, String.class };
			Constructor<Parameter> parameterConstructor = Parameter.class.getDeclaredConstructor(paramTypes);
			parameterConstructor.setAccessible(true);

			for (java.lang.reflect.Parameter p : javaMethod.getParameters()) {
				if (p.getName().startsWith(URL_LITERAL)) {
					PathParamInstance a = new PathParamInstance();
					a.setValue(p.getName());
					Annotation[] as = new PathParamInstance[1];
					as[0] = a;
					parameter = parameterConstructor.newInstance(as, a, Parameter.Source.PATH, p.getName(), p.getType(), p.getType(), paramEncoded, null);

					parList.add(parameter);
				}
				else if (p.getName().startsWith(HEADER_LITERAL)) {
					String header = createHeaderName(p.getName());

					HeaderParamInstance a = new HeaderParamInstance();
					a.setValue(header);
					Annotation[] as = new HeaderParamInstance[1];
					as[0] = a;

					parameter = parameterConstructor.newInstance(as, a, Parameter.Source.HEADER, header, p.getType(), p.getType(), paramEncoded, null);

					parList.add(parameter);
				}
				else if (p.getName().startsWith(QUERY_LITERAL)) {
					String query = createQueryName(p.getName());
					QueryParamInstance q = new QueryParamInstance();
					q.setValue(query);
					Annotation[] as = new QueryParamInstance[1];
					as[0] = q;

					parameter = parameterConstructor.newInstance(as, q, Parameter.Source.QUERY, query, p.getType(), p.getType(), paramEncoded, null);

					parList.add(parameter);
				}
				else if (p.getName().startsWith(COOKIE_LITERAL)) {
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
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return parList;
	}

	public static boolean isNeedToModify(AnnotatedMethod method, Method javaMethod) {

		boolean notParamAnnotations = true;

		for (int i = 0; i < method.getParameterAnnotations().length; i++) {
			if (method.getParameterAnnotations()[0].length != 0) {
				notParamAnnotations = false;
			}
		}

		if (method.getAnnotations().length == 0 && notParamAnnotations) {
			if (javaMethod.getName().startsWith(HttpMethod.GET.toLowerCase()) || javaMethod.getName().contains(HttpMethod.POST.toLowerCase()) || javaMethod.getName().contains(HttpMethod.DELETE.toLowerCase()) || javaMethod.getName().contains(HttpMethod.PUT.toLowerCase())) {
				return true;
			}
		}

		return false;

	}

	private static String createCookieName(String query) {
		query = query.substring(COOKIE_LITERAL.length());
		return query;
	}

	private static String createQueryName(String query) {
		query = query.substring(QUERY_LITERAL.length());
		query = Character.toLowerCase(query.charAt(0)) + query.substring(1);
		return query;
	}

	private static String createHeaderName(String header) {

		header = header.substring(HEADER_LITERAL.length());

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

	private static String coolectMethodParametersInfo(Method javaMethod) {
		StringBuilder methodParameters = new StringBuilder();
		methodParameters.append(javaMethod.toString()).append("{"); //$NON-NLS-1$
		for (java.lang.reflect.Parameter p : javaMethod.getParameters()) {
			methodParameters.append(" ").append(p.getName()); //$NON-NLS-1$
		}
		methodParameters.append(" }"); //$NON-NLS-1$

		return methodParameters.toString();
	}

}
