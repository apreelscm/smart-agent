package com.eservice.eumowy.documents;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DocumentDefinitionsConfiguration {
    public List<DocumentDefinition> definitions;

    public DocumentDefinitionsConfiguration() {
        definitions = load();
    }

    // Class loaders don't work well in Grails so we can't use reflection to map YAML onto class
    private List<DocumentDefinition> load() {
        InputStream inputStream = DocumentDefinitionsConfiguration.class
                .getClassLoader()
                .getResourceAsStream("document-definitions.yaml");

        List<DocumentDefinition> documentTemplates = new ArrayList<DocumentDefinition>();

        Yaml yaml = new Yaml();
        Map<String, Object> data = (Map<String, Object>) yaml.load(inputStream);
        if (data.get("documents") != null) {
            for (Map<String, Object> definition : (List<Map<String, Object>>) data.get("documents")) {
                DocumentDefinition docTemplate = new DocumentDefinition();
                docTemplate.name = (String) definition.get("name");
                docTemplate.active = (Boolean) definition.get("active");
                docTemplate.order = (Integer) definition.get("order");
                docTemplate.description = (String) definition.get("description");
                docTemplate.templateFileName = definition.get("templateFileName").toString();
                docTemplate.fileName = definition.get("fileName").toString();
                docTemplate.isForPoint = (Boolean) definition.get("isForPoint");
                docTemplate.isForPos = (Boolean) definition.get("isForPos");
                docTemplate.sendToClient = (Boolean) definition.get("sendToClient");
                docTemplate.showOnPreview = (Boolean) definition.get("showOnPreview");
                docTemplate.showOnZrd = (Boolean) definition.get("showOnZrd");
                docTemplate.merge = (Boolean) definition.get("merge");
                documentTemplates.add(docTemplate);
            }
        }
        return documentTemplates;
    }
}
