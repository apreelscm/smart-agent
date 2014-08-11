package com.eservice.eumowy.factory

import com.eservice.eumowy.Process

class ProcessCommandDefaultValuesFactory {
    public static ProcessCommandDefaultValuesSetter getDefaultValuesSetter(Process processInstance) {
        switch (processInstance.activities) {
            case ["pakietStart", "pakietStartPlus", "pakietMobilny"]:
                return new PackageDefaultValuesSetter()
                break
            default:
                return new DefaultValuesSetter()
        }
    }
}
