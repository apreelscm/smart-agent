package com.eservice.eumowy
import java.text.SimpleDateFormat

import org.springframework.beans.PropertyEditorRegistrar
import org.springframework.beans.PropertyEditorRegistry
import org.springframework.beans.propertyeditors.CustomDateEditor

class CustomDateEditorRegistrar implements PropertyEditorRegistrar {
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        String dateFormat = 'yyyy-MM-dd'
        registry.registerCustomEditor(Date, new CustomDateEditor(new SimpleDateFormat(dateFormat), true))
    }
}
