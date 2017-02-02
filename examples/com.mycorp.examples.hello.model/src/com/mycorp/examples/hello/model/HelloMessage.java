package com.mycorp.examples.hello.model;


public class HelloMessage {

	private String from;
	private String message;
	private int[] array = {1, 1, 2, 3, 5, 8, 13, 21};
	
	public HelloMessage() {
	}
	
	public HelloMessage(String from, String message) {
		this.from = from;
		this.message = message;
	}
	
	public String getFrom() {
		return from;
	}
	
	public String getMessage() {
		return message;
	}
	
	public int[] getArray() {
        return array;
    }

    public void setArray(int[] array) {
        this.array = array;
    }
}
