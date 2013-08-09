package eservice.docx.converter

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 25.07.13
 * Time: 09:42
 * To change this template use File | Settings | File Templates.
 */
class MyStringUtils {

    def static isEmpty(str){
        (str == null || str.length() == 0)? true: false;
    }

    def static isNotEmpty(str){
        !isEmpty(str);
    }
}
