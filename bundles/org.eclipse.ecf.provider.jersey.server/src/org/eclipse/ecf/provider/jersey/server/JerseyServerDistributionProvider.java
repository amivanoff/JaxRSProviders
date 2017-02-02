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
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
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
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.server.model.ResourceMethod;
import org.glassfish.jersey.servlet.ServletContainer;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.http.HttpService;

@Component(service = IRemoteServiceDistributionProvider.class)
public class JerseyServerDistributionProvider extends JaxRSServerDistributionProvider {

	public static final String JERSEY_SERVER_CONFIG_NAME = "ecf.jaxrs.jersey.server";

	public static final String URI_PARAM = "uri";
	public static final String URI_DEFAULT = "http://localhost:8080/jersey";

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
				String uri = getParameterValue(parameters, URI_PARAM, URI_DEFAULT);
				try {
					return new JerseyServerContainer(new URI(uri), (ResourceConfig) configuration);
				} catch (URISyntaxException e) {
					throw new ContainerCreateException("Cannot create Jersey Server Container", e);
				}
			}
		});
		setDescription("Jersey Jax-RS Server Distribution Provider");
		setServer(true);
	}

	@Override
	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	public void bindHttpService(HttpService httpService) {
		super.bindHttpService(httpService);
	}

	@Override
	public void unbindHttpService(HttpService httpService) {
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
                throw new NullPointerException("Servlet cannot be null");
            }
			ResourceConfig config = new ResourceConfig(sc.getConfiguration());
			config.register(reg.getService());
			((ServletContainer) servlet).reload(config);
		}

		@Override
		protected Servlet createServlet(RSARemoteServiceRegistration registration) {
			ResourceConfig resourceConfig = (ResourceConfig) createConfigurable();
			if (resourceConfig != null)
            {
                resourceConfig.register(registration.getService());
            }

            Class<?> implClass = registration.getService().getClass();
            for (Class<?> clazz : implClass.getInterfaces())
            {
                if (clazz.getAnnotation(Path.class) == null)
                {
                    final Resource.Builder resourceBuilder = Resource.builder();
                    ResourceMethod.Builder methodBuilder;
                    Resource.Builder childResourceBuilder;
                    String serviceResourcePath;
                    String methodResourcePath;
                    String methodName;

                    //class
                    serviceResourcePath = "/" + clazz.getSimpleName().toLowerCase();
                    resourceBuilder.path(serviceResourcePath);
                    resourceBuilder.name(implClass.getName());

                    //methods
                    for (Method method : clazz.getMethods())
                    {
                        if (Modifier.isPublic(method.getModifiers()))
                        {
                            methodName = method.getName().toLowerCase();
                            methodResourcePath = "/" + methodName;
                            childResourceBuilder = resourceBuilder.addChildResource(methodResourcePath);

                            if (method.getAnnotation(Path.class) == null)
                            {
                                if (method.getParameterCount() == 0)
                                {
                                    methodBuilder = childResourceBuilder.addMethod("GET");
                                }
                                else
                                {
                                    if (methodName.contains("delete"))
                                    {
                                        methodBuilder = childResourceBuilder.addMethod("DELETE");

                                    }
                                    else
                                    {
                                        methodBuilder = childResourceBuilder.addMethod("POST");
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
                    resourceConfig.registerResources(resource);
                }
            }

			return new ServletContainer(resourceConfig);
		}

		@Override
		protected HttpService getHttpService() {
			List<HttpService> svcs = getHttpServices();
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

	}
}
