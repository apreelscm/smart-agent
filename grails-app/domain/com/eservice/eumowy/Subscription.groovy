package com.eservice.eumowy

import org.apache.commons.logging.LogFactory

class Subscription implements Serializable {

    private static final auditLogger = LogFactory.getLog("audit")

    String content
	String name
	String surname
	Date signDate
	PersonRole personRole
    String uniqueKey
	
	public enum PersonRole {
		ACCEPTANT1("ACCEPTANT1"),
		ACCEPTANT2("ACCEPTANT2"),
		ACCEPTANT3("ACCEPTANT3"),
		ACCEPTANT4("ACCEPTANT4"),
		ACCEPTANT1_1("ACCEPTANT1_1"),
		ACCEPTANT2_1("ACCEPTANT2_1"),
		ACCEPTANT3_1("ACCEPTANT3_1"),
		ACCEPTANT4_1("ACCEPTANT4_1"),
		ACCEPTANT1_APUW_ZAL4("ACCEPTANT1_APUW_ZAL4"),
		ACCEPTANT2_APUW_ZAL4("ACCEPTANT2_APUW_ZAL4"),
		ACCEPTANT3_APUW_ZAL4("ACCEPTANT3_APUW_ZAL4"),
		ACCEPTANT4_APUW_ZAL4("ACCEPTANT4_APUW_ZAL4"),
		PH("PH"),
		PH_1("PH_1"),
        ZARZAD1(""),
        ZARZAD2("")
		
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
        uniqueKey(nullable:true)
    }

    static mapping = {
        table name: "SUBSCRIPTION", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.SUBSCRIPTION_SEQ']
        uniqueKey column: "unique_key"
    }
	
	def getFileName() {
		return "sign-"+name+"-"+surname+"-"+id+".png"
	}

    def afterInsert() {
        auditLogger.info("Zapisano podpis [id:${id}, role: ${personRole.text}, signDate: ${signDate}, uniqueKey: ${uniqueKey}, name: ${name}, lastName: ${surname}]")
    }

    def afterUpdate() {
        auditLogger.info("Aktualizacja podpisu [id:${id}, role: ${personRole.text}, signDate: ${signDate}, uniqueKey: ${uniqueKey}, name: ${name}, lastName: ${surname}]")
    }
	
}
