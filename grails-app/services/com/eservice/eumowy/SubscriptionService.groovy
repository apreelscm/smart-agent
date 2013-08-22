package com.eservice.eumowy

class SubscriptionService {

    void save(Subscription signature) {
		signature.save()
	}
	
	Subscription getLast() {
		Subscription.last()
	}
	
	def getFileName(name, surname, subid) {
		return "sign-" + name + "-" + surname + "-" + subid + ".png"
	}
}
