package com.eservice.eumowy.helpers

import com.eservice.eumowy.util.ProjectPathHelper


class PdfHelper {

    static URL url = new PdfHelper().getClass().getResource("PdfHelper.class");
    static String fileTemplatePath = "otherResources" + File.separator + "pdf_templates" + File.separator
    static String fileTemplateOutPath = "target" + File.separator + "pdf_out" + File.separator
    static String fontsPath = fileTemplatePath + File.separator + "fonts" + File.separator

    static String JULY_20TH_DAY_2018_DOCUMENTS_PATH = "20_07_2018/"
    static String JULY_1_DAY_2018_DOCUMENTS_PATH = "01_07_2018/"
    static String JULY_18TH_2020_DOCUMENTS_PATH = "18_07_2020/"
    static String DOCUMENTS_PATH_21_01_01 = "21-01-01/"

    static HashMap<String, String[]> insertSignatures(def signatures) {
        HashMap<String, String[]> result = new HashMap<String, String[]>();

        signatures.each {
            def person = it.get(0);
            def pageNo = it.get(1);
            def x = it.get(2);
            def y = it.get(3);
            def scaleX = it.get(4);
            def scaleY = it.get(5);

            result.put(person, [new File(fileTemplatePath + File.separator + "subscriptions" + File.separator + "signature1.jpg").toURI().toURL(), "", "signature", pageNo, x, y, scaleX, scaleY] as String[])
        }
        return result;
    }

    static String getTemplatePath() {
        return ProjectPathHelper.getProjectPath(url) + fileTemplatePath;
    }
}
