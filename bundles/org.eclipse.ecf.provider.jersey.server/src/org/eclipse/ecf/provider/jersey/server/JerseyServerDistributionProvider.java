/*******************************************************************************
* Copyright (c) 2016 Composent, Inc. and Erdal Karaca. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Composent, Inc. - initial API and implementation
*   Erdal Karaca - initial API and implementation
******************************************************************************/
package org.eclipse.ecf.provider.jersey.server;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Configurable;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.WriterInterceptor;

import org.eclipse.ecf.core.ContainerCreateException;
import org.eclipse.ecf.core.ContainerTypeDescription;
import org.eclipse.ecf.core.IContainer;
import org.eclipse.ecf.provider.jaxrs.JaxRSContainerInstantiator;
import org.eclipse.ecf.provider.jaxrs.server.JaxRSServerContainer;
import org.eclipse.ecf.provider.jaxrs.server.JaxRSServerDistributionProvider;
import org.eclipse.ecf.remoteservice.RSARemoteServiceContainerAdapter.RSARemoteServiceRegistration;
import org.eclipse.ecf.remoteservice.provider.IRemoteServiceDistributionProvider;
import org.eclipse.equinox.http.servlet.ExtendedHttpService;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceMethod;
import org.glassfish.jersey.servlet.ServletContainer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(service = IRemoteServiceDistributionProvider.class)
public class JerseyServerDistributionProvider extends JaxRSServerDistributionProvider {

	public static final String JERSEY_SERVER_CONFIG_NAME = "ecf.jaxrs.jersey.server"; //$NON-NLS-1$

	public static final String URI_PARAM = "uri"; //$NON-NLS-1$
	public static final String URI_DEFAULT = "http://localhost:8080/jersey"; //$NON-NLS-1$

	public static final String SERVICE_ALIAS_PARAM = "ecf.jaxrs.jersey.server.service.alias"; //$NON-NLS-1$
    public static final String SERVER_ALIAS_PARAM = "ecf.jaxrs.jersey.server.server.alias"; //$NON-NLS-1$
    public static final String EXPORTED_INTERFACES = "ecf.jaxrs.jersey.server.exported.interfaces"; //$NON-NLS-1$

    private static final Map<String, Set<Resource>> aliasToSetResources = new HashMap<>();

    private IExtConfigService extConfigService;

	public JerseyServerDistributionProvider() {
		super();
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Configurable createConfigurable() {
		return new ResourceConfig();
	}

	@Activate
	public void activate() throws Exception {
		setName(JERSEY_SERVER_CONFIG_NAME);
		setInstantiator(new JaxRSContainerInstantiator(JERSEY_SERVER_CONFIG_NAME) {
			@Override
			public IContainer createInstance(ContainerTypeDescription description, Map<String, ?> parameters,
					Configuration configuration) throws ContainerCreateException {
                String uri =
                    getParameterValue(parameters, URI_PARAM, URI_DEFAULT);
                String alias =
                    getParameterValue(parameters, SERVER_ALIAS_PARAM.replaceFirst(JERSEY_SERVER_CONFIG_NAME + ".", "")); //$NON-NLS-1$ //$NON-NLS-2$
                if (alias != null)
                {
                    uri += alias;
                }
				try {
					return new JerseyServerContainer(new URI(uri), (ResourceConfig) configuration);
				} catch (URISyntaxException e) {
					throw new ContainerCreateException("Cannot create Jersey Server Container", e); //$NON-NLS-1$
				}
			}
		});
		setDescription("Jersey Jax-RS Server Distribution Provider"); //$NON-NLS-1$
		setServer(true);
	}

	@Override
	@Reference(cardinality = ReferenceCardinality.MANDATORY)
    public void bindHttpService(ExtendedHttpService httpService)
    {
		super.bindHttpService(httpService);
	}

	@Override
    public void unbindHttpService(ExtendedHttpService httpService)
    {
		super.unbindHttpService(httpService);
	}

	@Override
    @SuppressWarnings("rawtypes")
	@Reference(cardinality = ReferenceCardinality.MULTIPLE)
	protected void bindMessageBodyWriter(MessageBodyWriter instance, Map serviceProps) {
		super.bindMessageBodyWriter(instance, serviceProps);
	}

	@Override
    @SuppressWarnings("rawtypes")
	protected void unbindMessageBodyWriter(MessageBodyWriter instance) {
		super.unbindMessageBodyWriter(instance);
	}

	@Override
    @SuppressWarnings("rawtypes")
	@Reference(cardinality = ReferenceCardinality.MULTIPLE)
	protected void bindMessageBodyReader(MessageBodyReader instance, Map serviceProps) {
		super.bindMessageBodyReader(instance, serviceProps);
	}

	@Override
    @SuppressWarnings("rawtypes")
	protected void unbindMessageBodyReader(MessageBodyReader instance) {
		super.unbindMessageBodyReader(instance);
	}

	@Override
    @SuppressWarnings("rawtypes")
	@Reference(cardinality = ReferenceCardinality.MULTIPLE)
	protected void bindContextResolver(ContextResolver instance, Map serviceProps) {
		super.bindContextResolver(instance, serviceProps);
	}

	@Override
    @SuppressWarnings("rawtypes")
	protected void unbindContextResolver(ContextResolver instance) {
		super.unbindContextResolver(instance);
	}

	@Override
    @SuppressWarnings("rawtypes")
	@Reference(cardinality = ReferenceCardinality.MULTIPLE)
	protected void bindExceptionMapper(ExceptionMapper instance, Map serviceProps) {
		super.bindExceptionMapper(instance, serviceProps);
	}

	@Override
    @SuppressWarnings("rawtypes")
	protected void unbindExceptionMapper(ExceptionMapper instance) {
		super.unbindExceptionMapper(instance);
	}

	@Override
    @SuppressWarnings("rawtypes")
	@Reference(cardinality = ReferenceCardinality.MULTIPLE)
	protected void bindFeature(Feature instance, Map serviceProps) {
		super.bindFeature(instance, serviceProps);
	}

	@Override
    protected void unbindFeature(Feature instance) {
		super.unbindFeature(instance);
	}

	@Override
    @SuppressWarnings("rawtypes")
	@Reference(cardinality = ReferenceCardinality.MULTIPLE)
	protected void bindReaderInterceptor(ReaderInterceptor instance, Map serviceProps) {
		super.bindReaderInterceptor(instance, serviceProps);
	}

	@Override
    protected void unbindReaderInterceptor(ReaderInterceptor instance) {
		super.unbindReaderInterceptor(instance);
	}

	@Override
    @SuppressWarnings("rawtypes")
	@Reference(cardinality = ReferenceCardinality.MULTIPLE)
	protected void bindWriterInterceptor(WriterInterceptor instance, Map serviceProps) {
		super.bindWriterInterceptor(instance, serviceProps);
	}

	@Override
    protected void unbindWriterInterceptor(WriterInterceptor instance) {
		super.unbindWriterInterceptor(instance);
	}

	@Override
    @SuppressWarnings("rawtypes")
	@Reference(cardinality = ReferenceCardinality.MULTIPLE)
	protected void bindContainerRequestFilter(ContainerRequestFilter instance, Map serviceProps) {
		super.bindContainerRequestFilter(instance, serviceProps);
	}

	@Override
    protected void unbindContainerRequestFilter(ContainerRequestFilter instance) {
		super.unbindContainerRequestFilter(instance);
	}

	@Override
    @SuppressWarnings("rawtypes")
	@Reference(cardinality = ReferenceCardinality.MULTIPLE)
	protected void bindContainerResponseFilter(ContainerResponseFilter instance, Map serviceProps) {
		super.bindContainerResponseFilter(instance, serviceProps);
	}

	@Override
    protected void unbindContainerResponseFilter(ContainerResponseFilter instance) {
		super.unbindContainerResponseFilter(instance);
	}

	public void bindIExtConfigService(IExtConfigService extConfigService)
    {
        this.extConfigService = extConfigService;
    }

    public void unbindIExtConfigService(IExtConfigService extConfigService)
    {
    }

	public class JerseyServerContainer extends JaxRSServerContainer {

		private ResourceConfig originalConfiguration;

		public JerseyServerContainer(URI uri, ResourceConfig configuration) {
			super(uri);
			this.originalConfiguration = configuration;
		}

		@SuppressWarnings("rawtypes")
		@Override
		protected Configurable createConfigurable() {
			return (this.originalConfiguration == null) ? new ResourceConfig()
					: new ResourceConfig(this.originalConfiguration);
		}

		@Override
		protected void exportRegistration(RSARemoteServiceRegistration reg) {
			ServletContainer sc = (ServletContainer) this.servlet;
			if (sc == null)
            {
                throw new NullPointerException("Servlet cannot be null"); //$NON-NLS-1$
            }
			ResourceConfig config = new ResourceConfig(sc.getConfiguration());
			config.register(reg.getService());

            if (extConfigService != null)
            {
                for (ServletContextListener o : extConfigService.getListeners())
                {
                    ((ServletContainer)servlet).getServletContext().addListener(o);
                }
                for (Filter o : extConfigService.getFilters())
                {
                    ((ServletContainer)servlet).getServletContext().addFilter(o.getClass().getSimpleName(), o);
                }
            }

            ((ServletContainer)servlet).reload(config);
		}

		@Override
		protected Servlet createServlet(RSARemoteServiceRegistration registration) {
		    ResourceConfig resourceConfig = (ResourceConfig) createConfigurable();
			if (resourceConfig != null)
            {
                resourceConfig.register(registration.getService());

                if (extConfigService != null)
                {
                    for (Object o : extConfigService.getConfigObjects())
                    {
                        resourceConfig.register(o);
                    }
                    /*
                     * for (ServletContextListener o :
                     * extConfigService.getListeners()) resourceConfig.register(o);
                     * for (Filter o : extConfigService.getFilters())
                     * resourceConfig.register(o);
                     */
                }
            }

            Class<?> implClass = registration.getService().getClass();
			String serverAlias = (String)registration.getProperty(SERVER_ALIAS_PARAM);

            for (Class<?> clazz : implClass.getInterfaces())
            {
			  System.out.println(clazz.getCanonicalName());
              if (((String)registration.getProperty(EXPORTED_INTERFACES)).contains(clazz.getCanonicalName()))
              {
                if (clazz.getAnnotation(Path.class) == null)
                {
                    final Resource.Builder resourceBuilder = Resource.builder();
                    ResourceMethod.Builder methodBuilder;
                    Resource.Builder childResourceBuilder;
                    String serviceResourcePath;
                    String methodResourcePath;
                    String methodName;
					String pathParam;
                    String serviceAliace =
                        (String)registration.getProperty(JerseyServerDistributionProvider.SERVICE_ALIAS_PARAM);

                    //class
                    if (serviceAliace != null && !serviceAliace.equals("")) { //$NON-NLS-1$
                        serviceResourcePath = serviceAliace;
                    } else {
                        serviceResourcePath = buildServicePath(clazz.getSimpleName());
                    }
                    resourceBuilder.path(serviceResourcePath);
                    resourceBuilder.name(implClass.getName());

                    //methods
                    for (Method method : clazz.getMethods())
                    {
                        if (Modifier.isPublic(method.getModifiers()))
                        {
							pathParam = pathParam(method);
                            methodName = method.getName().toLowerCase();


                            methodResourcePath = buildMethodPath(methodName);
                            if (pathParam != null)
                            {
                                methodResourcePath =
                                    methodResourcePath.equals("/") ? pathParam : methodResourcePath + pathParam; //$NON-NLS-1$
                            }

                            childResourceBuilder = resourceBuilder.addChildResource(methodResourcePath);

                            if (method.getAnnotation(Path.class) == null)
                            {
                                if (methodName.contains("get")) //$NON-NLS-1$
                                {
                                    methodBuilder = childResourceBuilder.addMethod("GET"); //$NON-NLS-1$
                                }
                                else
                                {
                                    if (methodName.contains("delete")) //$NON-NLS-1$
                                    {
                                        methodBuilder = childResourceBuilder.addMethod("DELETE"); //$NON-NLS-1$

                                    }
									else if (methodName.contains("post")) //$NON-NLS-1$
                                    {
                                        methodBuilder = childResourceBuilder.addMethod("POST"); //$NON-NLS-1$
                                    }
                                    else
                                    {
                                        methodBuilder = childResourceBuilder.addMethod("PUT"); //$NON-NLS-1$
                                    }
                                    methodBuilder.consumes(MediaType.APPLICATION_JSON);//APPLICATION_JSON)TEXT_PLAIN_TYPE
                                }
                                methodBuilder.produces(MediaType.APPLICATION_JSON)//APPLICATION_JSON)
                                    //.handledBy(implClass, method)
                                    .handledBy(registration.getService(), method).handlingMethod(method).extended(
                                        false);
                            }
                        }
                    }
                    final Resource resource = resourceBuilder.build();
					saveResourceByAlias(serverAlias, resource);
                }
                }
            }
			Resource[] resources = getResourcesByServerAilas(serverAlias);
            resourceConfig.registerResources(resources);
			ServletContainer sc = new ServletContainer(resourceConfig)
            {
                @Override
                public void init(ServletConfig config) throws ServletException
                {
                    super.init(config);
                }
            };
            /*
             * for (ServletContextListener o : extConfigService.getListeners())
             * sc.getServletContext().addListener(o); for (Filter o :
             * extConfigService.getFilters())
             * sc.getServletContext().addFilter(o.getClass().getName(), o);
             */
            // TODO Add Shiro Listener
            return sc;
		}

		@Override
        protected ExtendedHttpService getHttpService()
        {
            List<ExtendedHttpService> svcs = getHttpServices();
			return (svcs == null || svcs.size() == 0) ? null : svcs.get(0);
		}

		@Override
		protected void unexportRegistration(RSARemoteServiceRegistration registration) {
			@SuppressWarnings("rawtypes")
			Configurable c = createConfigurable(getExportedRegistrations());
			if (c != null)
            {
                ((ServletContainer) servlet).reload((ResourceConfig) c);
            }
		}

        @Override
        protected List<Filter> getFilters()
        {
            if (extConfigService != null)
            {
                return extConfigService.getFilters();
            }
            else
            {
                return new ArrayList<>();
            }
        }

        @Override
        protected List<ServletContextListener> getListeners()
        {
            if (extConfigService != null)
            {
                return extConfigService.getListeners();
            }
            else
            {
                return new ArrayList<>();
            }
        }

	}

	protected Resource[] getResourcesByServerAilas(String serverAlias) {
        Resource[] resources = new Resource[aliasToSetResources.get(serverAlias).size()];
        int i = 0;
        if (resources.length != 0)
        {
            Iterator<Resource> iter = aliasToSetResources.get(serverAlias).iterator();
            while (iter.hasNext())
            {
                resources[i++] = iter.next();
            }
        }
        return resources;
    }

    protected void saveResourceByAlias(String alias, Resource resource) {
        if (!aliasToSetResources.containsKey(alias))
        {
            Set<Resource> resources = new HashSet<>();
            resources.add(resource);
            aliasToSetResources.put(alias, resources);
        }
        else
        {
            Set<Resource> resources = aliasToSetResources.get(alias);
            resources.add(resource);
        }
    }

    protected String pathParam(Method method) {
        StringBuilder strBuilder = new StringBuilder();
        for (java.lang.reflect.Parameter p : method.getParameters())
        {
            if (p.getName().startsWith("url")) //$NON-NLS-1$
            {
                strBuilder.append("/{").append(p.getName()).append("}"); //$NON-NLS-1$//$NON-NLS-2$
            }
        }
        return strBuilder.toString().length() == 0 ? null : strBuilder.toString();
    }

    protected String buildServicePath(String simpleClassName) {
        StringBuilder servicePath = new StringBuilder();
        servicePath.append("/"); //$NON-NLS-1$
        String[] partsOfPath;
        if (!simpleClassName.startsWith("I")) //$NON-NLS-1$
        {
            return simpleClassName.toLowerCase();
        }
        simpleClassName = simpleClassName.substring(1);

        if (simpleClassName.endsWith("Service")) //$NON-NLS-1$
        {
            simpleClassName = simpleClassName.substring(0, simpleClassName.length() - "Service".length()); //$NON-NLS-1$
        }

        partsOfPath = simpleClassName.split("(?=\\p{Lu})"); //$NON-NLS-1$
        for (String parts : partsOfPath)
        {
            servicePath.append(parts.toLowerCase() + "/"); //$NON-NLS-1$
        }
        servicePath.deleteCharAt(servicePath.length() - 1); // remove last '/'
        return servicePath.toString();
    }

    protected String buildMethodPath(String methodName) {
        if (methodName.startsWith(HttpMethod.GET.toLowerCase()))
        {
            methodName = methodName.substring(HttpMethod.GET.length());
        }
        else if (methodName.startsWith(HttpMethod.POST.toLowerCase()))
        {
            methodName = methodName.substring(HttpMethod.POST.length());
        }
        else if (methodName.startsWith(HttpMethod.DELETE.toLowerCase()))
        {
            methodName = methodName.substring(HttpMethod.DELETE.length());
        }
        else if (methodName.startsWith(HttpMethod.PUT.toLowerCase()))
        {
            methodName = methodName.substring(HttpMethod.PUT.length());
        }
        return methodName == "" ? "/" : "/" + methodName.toLowerCase(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
}
