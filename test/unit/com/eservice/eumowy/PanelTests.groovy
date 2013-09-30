package com.eservice.eumowy



import grails.test.mixin.*
import org.junit.*

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */

@TestFor(Panel)
class PanelTests {

    static PHONE_FAX_REGEXP = '\\(\\d{2}\\) \\d{3}-\\d{2}-\\d{2}'


    void testName(){

        String a = 'APUNTSS1.00312-01-16.pdf';

        def begin = a.substring(0, a.lastIndexOf('.'));
        println begin

        def end = a.substring(a.lastIndexOf('.'));
        println end

//        println a.substring(a.lastIndexOf('.'));



    }

    void testSomething() {
        def phoneNumber = '(34) 445-65-32'
        if (phoneNumber != null){
            def pattern = ~/\(\d{2}\) \d{3}-\d{2}-\d{2}/
            if (pattern.matcher(phoneNumber).matches()){
                final String[] split = phoneNumber.split("-");
                for (int i=0; i<split.length; i++){
                    println split[i]
                }
            } else {
                def key = 'test'
                println  '[PointDataDetails - ' + key+ '] nie spelnia warunku: (d{2}) d{3}-d{2}-d{2} value = ' + phoneNumber
            }
        }



//        if (phoneNumber != null &&  phoneNumber.matcher(PHONE_FAX_REGEXP).matches()){
//            def aaa = phoneNumber.substring(phoneNumber.lastIndexOf(' ')+1)
//            aaa.split('-').each{ i ->
//                println i
//            }
//
//            def ccc = phoneNumber.substring(phoneNumber.lastIndexOf('(') +1, phoneNumber.indexOf(')'));
//            println 'ccc=>' + ccc
//        }
    }
}
