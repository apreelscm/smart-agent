(function ($) {

    var signatureExceptions = ["zmianaWarunkowDcc"]

    $(function() {
        $('form').submit(function (e) {
            return validateArticle();
        });
    }); //end ready

    function validateArticle() {
        var isValid = true;
        $("article").each(function () {
            var article = this;
            var selects = $(article).find("select")

            if (selects.length > 0) {
                var select1 = selects[0];
                if (selects.length == 1) {
                    if (select1.value == "null") {
                        isValid = false;
                    }

                    select1.value == "null" ? makeInvalid(select1) : makeValid(select1);
                }
                else if (selects.length == 2) {
                    var select2 = selects[1];

                    if (signatureExceptions.indexOf(article.id) != -1){

                        if(select1.value == "null" && select2.value == "null"){
                            isValid = false;
                            makeArticleInvalid(article);
                        }
                        else{
                            makeArticleValid(article)
                        }
                    }
                    else{
                        if (select1.value == "null" || select2.value == "null"){
                            isValid = false;
                        }

                        select1.value == "null" ? makeInvalid(select1) : makeValid(select1);
                        select2.value == "null" ? makeInvalid(select2) : makeValid(select2);
                    }
                }
            }
        })
        return isValid;
    }

    function makeInvalid(obj) {
        $(obj).parent().addClass("error");
        $(obj).parent().find("img").removeClass("visibility-hidden");
    }

    function makeValid(obj) {
        $(obj).parent().removeClass("error");
        $(obj).parent().find("img").addClass("visibility-hidden");
    }

    function makeArticleInvalid(obj) {
        $(obj).addClass("article-error");
    }
    function makeArticleValid(obj) {
        $(obj).removeClass("article-error");
    }

}(jQuery));