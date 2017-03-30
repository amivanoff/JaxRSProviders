package org.eclipse.ecf.provider.jersey.server;

import java.util.List;

import javax.servlet.Filter;
import javax.servlet.ServletContextListener;

public interface IExtConfigService {

	public List<Object> getConfigObjects();

	public List<ServletContextListener> getListeners();


	public List<Filter> getFilters();
}
