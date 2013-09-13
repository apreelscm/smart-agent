package com.eservice.eumowy

import org.apache.commons.lang.builder.HashCodeBuilder

class Subscription implements Serializable {

    String content
	String name
	String surname
	Date signDate
	//PersonType personType
	PersonRole personRole
	
	/*public enum PersonType {
		MR("Pan"),
		MRS("Pani")
		
		private final String text;
		
		private PersonType(final String text) {
			this.text = text
		}

		@Override
		public String toString() {
			return text
		}
	}*/
	
	public enum PersonRole {
		ACCEPTANT1("Akceptant 1"),
		ACCEPTANT2("Akceptant 2"),
		PH("PH")
		
		private final String text
		
		private PersonRole(final String text) {
			this.text = text
		}

		@Override
		public String toString() {
			return text
		}
	}

    static belongsTo = [process:Process]

    static constraints = {
        process(nullable:true)
		content(blank:false, maxSize: 100000)
        name(blank: false)
        surname(blank: false)
		//personType(nullable:true)
		personRole(nullable:true)
    }

    static mapping = {
        table name: "SUBSCRIPTION", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.SUBSCRIPTION_SEQ']
    }
	
	def getFileName() {
		return "sign-"+name+"-"+surname+"-"+id+".png"
	}
	
}
