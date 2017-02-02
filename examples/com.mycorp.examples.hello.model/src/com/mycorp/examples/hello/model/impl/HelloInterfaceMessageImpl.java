package com.mycorp.examples.hello.model.impl;

import com.mycorp.examples.hello.model.IHelloInterfaceMessage;

public class HelloInterfaceMessageImpl implements IHelloInterfaceMessage {

	private String from;
	private String message;
	private int[] array = {1, 1, 2, 3, 5, 8, 13, 21};
	
	public HelloInterfaceMessageImpl() {
	}
	
	public HelloInterfaceMessageImpl(String from, String message) {
		this.from = from;
		this.message = message;
	}
	
	/* (non-Javadoc)
	 * @see com.mycorp.examples.hello.model.IHelloInterfaceMessage#getFrom()
	 */
	@Override
	public String getFrom() {
		return from;
	}
	
	/* (non-Javadoc)
	 * @see com.mycorp.examples.hello.model.IHelloInterfaceMessage#getMessage()
	 */
	@Override
	public String getMessage() {
		return message;
	}
	
	/* (non-Javadoc)
	 * @see com.mycorp.examples.hello.model.IHelloInterfaceMessage#getArray()
	 */
	@Override
	public int[] getArray() {
        return array;
    }

    /* (non-Javadoc)
	 * @see com.mycorp.examples.hello.model.IHelloInterfaceMessage#setArray(int[])
	 */
    @Override
	public void setArray(int[] array) {
        this.array = array;
    }
}
