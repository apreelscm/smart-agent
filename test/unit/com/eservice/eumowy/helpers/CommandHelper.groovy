package com.eservice.eumowy.helpers


class CommandHelper {
    public static void setProperties(def command, Map properties) {
        properties.each {
            command[it.key] = it.value
        }
    }
}
