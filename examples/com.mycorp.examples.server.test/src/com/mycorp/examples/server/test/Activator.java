package com.mycorp.examples.server.test;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.mycorp.examples.server.test.java.Test;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		Test.sayHello();
		System.out.println("--------"); //$NON-NLS-1$
		Test.sayHello();
		System.out.println("--------"); //$NON-NLS-1$
		Test.greeting();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
	}
}
