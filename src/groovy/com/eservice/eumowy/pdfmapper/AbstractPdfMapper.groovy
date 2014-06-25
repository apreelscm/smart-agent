package com.eservice.eumowy.pdfmapper

import com.eservice.eumowy.util.DateUtils
import org.apache.log4j.Logger

public abstract class AbstractPdfMapper {

    static Logger LOG = Logger.getLogger(AbstractPdfMapper.class)

    def String getAddress(String streetType, String street, String houseNumber, String flatNumber, String postalCode, String city){
        def sb = new StringBuilder();

        sb.append(streetType).append(" ").append(street).append(" ").append(houseNumber);
        if (flatNumber != null && !"".equals(flatNumber)) {
            sb.append("/").append(flatNumber);
        }
        sb.append(", ").append(postalCode).append(" ").append(city);
        return sb.toString();
    }

    def getPropertyFromObject(def object, def property){
        if (object?.hasProperty(property)){
            return object."${property}" ?: "";
        } else {
            return ""
        }
    }

    def addCheckbox(def data, def fieldName) {
        addCheckbox(data, fieldName, true, true)
    }

    def addCheckbox(def data, def pdfName, def fieldValue, def value){
        data.put(pdfName, [fieldValue.equals(value), "", "checkbox"] as String[])
    }


    def addDateField(def data, def key, def value){
        data.put(key, [value?.trim()?DateUtils.getFormattedDate(DateUtils.parseWithTimezone(value), DateUtils.YYYY_MM_DD):""] as String[])
    }

    void addSeparatedDateFields(Map data, String formattedDate, String prefix) {
        if (formattedDate != null && !"".equals(formattedDate)) {
            def pattern = ~/\d{4}-\d{2}-\d{2}/

            if (pattern.matcher(formattedDate).matches()) {
                final String[] split = formattedDate.split("-")
                data.put(prefix + "1", [split[2]] as String[]);
                data.put(prefix + "2", [split[1]] as String[]);
                data.put(prefix + "3", [split[0]] as String[]);
            }
        }
    }

    def addCheckboxes(def data, def pdfKeyValue, def value){
        pdfKeyValue.each{ k, v ->  data.put(k, [v.equals(value), "", "checkbox"] as String[])}
    }

    def mapWithPattern(def data, def value, def pattern, def delimenter, def fieldName){
        if (value != null){
            if (pattern.matcher(value).matches()){
                final String[] split = value.split(delimenter);
                for (int i=0; i<split.length; i++){
                    data.put(fieldName+(i+1), [split[i]] as String[])
                }
            } else {
                LOG.error('[Wartosc - ' + value + '] nie spelnia zalozonego warunku ('+pattern.pattern()+'). value = ' + value )
            }
        }
    }

    def mapFaxOrPhone(def key, def data, def phoneNumber, def kierName, def otherName){
        //(11) 222-33-44
        def pattern = ~/\(\d{2}\) \d{3}-\d{2}-\d{2}/

        if (phoneNumber != null){
            if (pattern.matcher(phoneNumber).matches()){
                data.put(kierName, [phoneNumber.substring(phoneNumber.lastIndexOf('(') +1, phoneNumber.indexOf(')'))] as String[]);

                def parts = phoneNumber.substring(phoneNumber.lastIndexOf(' ')+1).split('-');
                for (int i=0; i<parts.length; i++){
                    data.put(otherName+(i+1), [parts[i]] as String[])
                }
            } else {
                LOG.error('[key ' + key + '] nie spelnia warunku: (d{2}) d{3}-d{2}-d{2} value = ' + phoneNumber )
            }
        }
    }
}
