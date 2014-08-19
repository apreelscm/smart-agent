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
        command.okresLojalnosciowy = "12"
        command.liczbaTerminali = "5"
        command.dccZakresUruchomienia = "obecne_i_nowe"
    }
}
