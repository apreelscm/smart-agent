package com.eservice.eumowy

class SubscriptionDefinition implements Serializable{

    Subscription.PersonRole role;

    Integer subscriptionX;
    Integer subscriptionY;
    Integer subscriptionPageNumber;

    Integer scaleX;
    Integer scaleY;

    String fileName;

    static belongsTo = [signature: Signature]

    static constraints = {
        role(nullable: true)
        subscriptionX(nullable: true)
        subscriptionY(nullable: true)
        subscriptionPageNumber(nullable: true)
        scaleX(nullable: true)
        scaleY(nullable: true)
        fileName(nullable: true)
    }

    static mapping = {
        table name: "SUBSCRIPTION_DEFINITION", schema: DomainConsts.SHEMA_NAME
        id generator:'sequence', params:[sequence:DomainConsts.SHEMA_NAME+'.SUBSCRIPTION_DEFINITION_SEQ']
    }
}
