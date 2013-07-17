package com.eservice.eumowy

class Signature {
	String signature

    static constraints = {
		signature(blank: false)
    }
	
	static mapping = {
		signature type: 'text'
	}
}
