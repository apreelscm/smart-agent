package com.eservice.eumowy

class Subscription implements Serializable {

    String content;
	
	String name;
	String surname;
	
	PersonType personType;
	PersonRole personRole;
	
	public enum PersonType {
		MR("Pan"),
		MRS("Pani")
		
		private final String text;
		
		private PersonType(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}
	}
	
	public enum PersonRole {
		ACCEPTANT1("Akceptant 1"),
		ACCEPTANT2("Akceptant 2"),
		PH("PH")
		
		private final String text;
		
		private PersonRole(final String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}
	}

    static belongsTo = [process:Process]

    static constraints = {
        content(maxSize: 10000)
    }

    static mapping = {
        table name: "SUBSCRIPTION", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.SUBSCRIPTION_SEQ']
    }
}
