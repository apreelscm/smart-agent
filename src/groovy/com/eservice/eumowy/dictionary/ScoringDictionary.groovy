package com.eservice.eumowy.dictionary

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 22.08.13
 * Time: 14:58
 * To change this template use File | Settings | File Templates.
 */
class ScoringDictionary {

    // przykladowy enum dla scoringu - temat narazie zawieszony

    enum Ownership{
        OWNERSHIP("1", "panel.ownership.ownership"),
        RENT("2", "panel.ownership.rent");

        final String key;
        final String placeholder;

        private Ownership(final String key, final String placeholder) {
            this.key = key;
            this.placeholder = placeholder;
        }

        public static final List<String> getKeys(){
            List<String> result = new ArrayList<String>();
            for (ScoringDictionary.Ownership o: ScoringDictionary.Ownership.values()){
                result.add(o.key);
            }
            return result;
        }

        public static final List<String> getPlaceholders(){
            List<String> result = new ArrayList<String>();
            for (ScoringDictionary.Ownership o: ScoringDictionary.Ownership.values()){
                result.add(o.placeholder);
            }
            return result;
        }
    }
}
