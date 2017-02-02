package com.mycorp.examples.hello.model;

import com.mycorp.examples.hello.model.impl.HelloInterfaceMessageImpl;

public class MyFactory implements IFactory {

	@Override
	public Object create(Class<?> clazz) {
		return new HelloInterfaceMessageImpl();
	}
}
