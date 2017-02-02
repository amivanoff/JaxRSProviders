/*******************************************************************************
* Copyright (c) 2015 Composent, Inc. and others. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*   Composent, Inc. - initial API and implementation
******************************************************************************/
package com.mycorp.examples.hello.client;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicy;

import com.mycorp.examples.hello.IHello;

@Component(name = "com.mycorp.examples.student.client")
public class HelloServiceClient {

	public HelloServiceClient() {
		System.out.println("HelloServiceClient created"); //$NON-NLS-1$
	}

	@Activate
	void activate() {
		System.out.println("HelloServiceClient started"); //$NON-NLS-1$
	}

	@Reference(policy = ReferencePolicy.DYNAMIC)
	void bindHello(IHello service) {
		System.out.println("Discovered IHello service=" + service); //$NON-NLS-1$
		// Get students
		String hello = service.hello();
		// Print list
		System.out.println("hello=" + hello); //$NON-NLS-1$
	}

	void unbindHello(IHello service) {
	}

}
