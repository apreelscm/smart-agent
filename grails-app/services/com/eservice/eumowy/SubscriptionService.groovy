package com.eservice.eumowy

class SubscriptionService {

    void save(Subscription signature) {
        //TODO
        signature.name = signature.name ?: "testName";
        signature.surname = signature.surname ?: "testSurname";
		signature.save()
	}
	
	Subscription getLast() {
		Subscription.last()
	}
	
	def getFileName(name, surname, subid) {
		return "sign-" + name + "-" + surname + "-" + subid + ".png"
	}
}
