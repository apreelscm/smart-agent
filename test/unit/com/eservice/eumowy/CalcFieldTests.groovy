package com.eservice.eumowy

import grails.test.mixin.TestFor
/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(CalcField)
class CalcFieldTests {

    void testSomething() {


        String csvFile = "B:/maping.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        try {

            Map<String, String> maps = new HashMap<String, String>();
            List<String> list = new ArrayList<String>();

            List<String> listValue = new ArrayList<String>();

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] country = line.split(cvsSplitBy);

                String entry = country[0] + "===" + country[1];
                if(!list.contains(entry)){
                    list.add(entry);
                }


                String entry1 = country[1];
                if(!listValue.contains(entry1)){
                    listValue.add(entry1);
                }

               /* if(!maps.containsKey(country[1])){
                    maps[country[1]] = new ArrayList();
                }

                if( !((ArrayList)maps[country[1]]).contains(country[2])){
                    ((ArrayList)maps[country[1]]).add(country[2])
                }*/
            }



            //loop map
        /*    for (String entr2y in list) {
                def arr = entr2y.split("===");
               println( """new CalcFieldSignature(signature:Signature.findByName("${arr[0]}"), calcField: CalcField.findByName(${arr[1]})).save();""")
            }*/

            for (String entr2y in list) {
                def arr = entr2y.split("===");
                println( """insert into CBD_UMOWY.PANEL (id, version, name, order_no) values (null, 0, '${arr[0]}','${arr[1]}')""")
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("Done");

    }
}
