package com.eservice.eumowy.propEditors

import org.springframework.beans.PropertyEditorRegistrar
import org.springframework.beans.PropertyEditorRegistry

/**
 * User: Dominik Walczak
 * Date: 20.08.13 Time: 11:04
 *
 */
class CustomPropertyEditorRegistrar implements PropertyEditorRegistrar {

    @Override
    void registerCustomEditors(PropertyEditorRegistry registry) {
        // new custom editors can be registered here

        // registry.registerCustomEditor(,);
    }
}
