package com.eservice.eumowy.factory

import com.eservice.eumowy.command.ProcessCommand


class PointDefaultValuesSetter implements ProcessCommandDefaultValuesSetter{
    @Override
    void setDefaultValues(ProcessCommand command) {
        command.liczbaTerminali = "10"
    }
}
