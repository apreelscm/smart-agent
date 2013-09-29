package com.eservice.eumowy.command

import grails.validation.Validateable
import org.apache.commons.collections.FactoryUtils
import org.apache.commons.collections.ListUtils

@Validateable
class OnlyPointsCommand implements Serializable{
    List<PointCommand> points = ListUtils.lazyList([], FactoryUtils.instantiateFactory(PointCommand))

    static constraints = {
        points(nullable:true)
    }
}
