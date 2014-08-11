package com.eservice.eumowy.factory

import com.eservice.eumowy.command.ProcessCommand


class PackageDefaultValuesSetter implements ProcessCommandDefaultValuesSetter{
    @Override
    void setDefaultValues(ProcessCommand command) {
        command.wydrukGrafikiCena = "0"
        command.dzialaniaMatematyczneCena = "0"
        command.mudCena = "0"
        command.pierwszaSesjaCena = "-"
        command.oplataZaPlatnoscWInnejWalucie = "5"
        command.oplataVISAPr = "0"
        command.oplataVISA = "0"
        command.oplataMasterCardPr = "0"
        command.oplataMasterCard = "0"
        command.oplataMaestroPr = "0"
        command.oplataMasterCard = "0"
        command.okresLojalnosciowy = "12"
        command.liczbaTerminali = "10"
    }
}
