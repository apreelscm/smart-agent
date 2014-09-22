package com.eservice.eumowy.factory

import com.eservice.eumowy.Process

class ProcessCommandDefaultValuesFactory {
    public static ProcessCommandDefaultValuesSetter getDefaultValuesSetter(Process processInstance) {
        if(processInstance.activities.size() > 1) {
            return null
        }

        switch (processInstance.activities[0].code) {
            case "pakietStart":
            case "pakietStartPlus":
            case "pakietMobilny":
                return new PackageDefaultValuesSetter()
                break
            case "dodatkowyPunkt":
            case "dodatkowyPos":
                return new PointDefaultValuesSetter()
                break
        }
    }
}
