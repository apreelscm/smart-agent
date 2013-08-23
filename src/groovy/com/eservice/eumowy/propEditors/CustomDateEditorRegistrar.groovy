package com.eservice.eumowy.propEditors

import org.springframework.beans.PropertyEditorRegistrar
import org.springframework.beans.PropertyEditorRegistry
import org.springframework.beans.propertyeditors.CustomDateEditor
import java.text.SimpleDateFormat


//TODO - czy to jest potrzebne????

public class CustomDateEditorRegistrar implements PropertyEditorRegistrar {

    public static final String DATE_FORMAT = 'yyyy-MM-dd'

    public void registerCustomEditors(PropertyEditorRegistry registry) {
        registry.registerCustomEditor(Date, new CustomDateEditor(new SimpleDateFormat(DATE_FORMAT), true))
    }
}