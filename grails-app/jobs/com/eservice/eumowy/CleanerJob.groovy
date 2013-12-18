package com.eservice.eumowy

class CleanerJob {

    def appParametersService

    static triggers = {
        simple name: 'cleanerTrigger', startDelay: 10000, repeatInterval: 3*1000*60*60, repeatCount: -1
    }

    def execute() {
        def dd = new Date(new Date().getTime()-appParametersService.getAgeToRemove()*1000*60*60)

        log.info 'Removing files older than: ' + dd.format('EEE MMM dd hh:mm:ss a yyyy')

        new File(appParametersService.getPdfPreviewPath()).eachFile{
            if (new Date(it.lastModified()).before(dd)){
                log.info 'Removing file: ' + it.name + ' - ' + it.delete()
            }
        }
    }
}
