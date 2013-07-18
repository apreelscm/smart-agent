package com.eservice.eumowy

class SignatureService {

    void save(Signature signature) {
		signature.save()
	}
	
	Signature getLast() {
		Signature.last()
	}
}
