/* 
 ===============================================================
 jQuery plugin to expand/collapse a content element when a
 expander element is clicked. When expanding/collapsing the plug-in
 also toggles a class on the element.
 See https://github.com/redhotsly/simple-expand
 ===============================================================
 Copyright (C) 2012 Sylvain Hamel

 Permission is hereby granted, free of charge, to any person
 obtaining a copy of this software and associated documentation
 files (the "Software"), to deal in the Software without restriction,
 including without limitation the rights to use, copy, modify,
 merge, publish, distribute, sublicense, and/or sell copies of the
 Software, and to permit persons to whom the Software is furnished
 to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR
 ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 ===============================================================
 */
/*globals $:false, window:false*/
(function ($) {
    "use strict";

    // SimpleExpand 
    function SimpleExpand() {

        var that = this;

        var restrictionsMap = {}

        restrictionsMap["nowaUmowaCB"] = ["rozszerzenie", "pakiet", "zmianaWarunkow", "poprawDane", "odrzucDokumenty","uzupelnijPodpisy", "pakietSerwisowy", "wymianaUmowyZaplaty", "wymianaTerminalaCB"];

        restrictionsMap["dodatkowyPunktCB"] = ["nowaUmowa", "pakiet","poprawDane","odrzucDokumenty", "uzupelnijPodpisy", "wymianaTerminalaCB"];
        restrictionsMap["dodatkowyPosCB"] =  ["nowaUmowa","pakiet","poprawDane","odrzucDokumenty", "uzupelnijPodpisy", "wymianaTerminalaCB"];

        restrictionsMap["zmianaProwizjiCB"] =  ["nowaUmowa","pakiet","poprawDane","odrzucDokumenty", "uzupelnijPodpisy", "wymianaTerminalaCB"];
        restrictionsMap["zmianaWarunkowDccCB"] =  ["nowaUmowa","pakiet","poprawDane","odrzucDokumenty", "uzupelnijPodpisy", "wymianaTerminalaCB", "dodanieDccCB"];
        restrictionsMap["wymianaUmowyNajmuCB"] =  ["nowaUmowa","pakiet","poprawDane","odrzucDokumenty", "uzupelnijPodpisy", "wymianaTerminalaCB"];
        restrictionsMap["dodanieAneksuKosztyPlusCB"] = ["nowaUmowa","pakiet","poprawDane","odrzucDokumenty", "uzupelnijPodpisy", "wymianaTerminalaCB"];
        restrictionsMap["aneksCB"] =  ["nowaUmowa","poprawDane","pakiet","odrzucDokumenty", "uzupelnijPodpisy", "wymianaTerminalaCB"];
        restrictionsMap["wymianaUmowyZaplatyCB"] =  ["nowaUmowa","pakiet","poprawDane","odrzucDokumenty", "uzupelnijPodpisy", "wymianaTerminalaCB"];
        restrictionsMap["zmianaWarunkowPrepaidCB"] =  ["nowaUmowa","pakiet","poprawDane","odrzucDokumenty", "uzupelnijPodpisy", "wymianaTerminalaCB"];
        restrictionsMap["zmianaOkresuLojalnosciowegoCB"] =  ["nowaUmowa","pakiet","poprawDane","odrzucDokumenty", "uzupelnijPodpisy", "wymianaTerminalaCB"];
        restrictionsMap["zmianaWarunkowCashbackCB"] =  ["nowaUmowa","pakiet","poprawDane","odrzucDokumenty", "uzupelnijPodpisy", "wymianaTerminalaCB", "dodanieCashBackCB"];
        restrictionsMap["promocyjneObnizenieNajmuCB"] =  ["nowaUmowa","pakiet","poprawDane","odrzucDokumenty", "uzupelnijPodpisy", "wymianaTerminalaCB"];

        restrictionsMap["dodaniePrepaidCB"] =  ["poprawDane","pakiet","odrzucDokumenty", "uzupelnijPodpisy", "wymianaTerminalaCB"];
        restrictionsMap["dodanieDccCB"] =  ["poprawDane","pakiet","odrzucDokumenty", "uzupelnijPodpisy", "wymianaTerminalaCB", "zmianaWarunkowDccCB"];
        restrictionsMap["dodanieCashBackCB"] =  ["poprawDane","pakiet","odrzucDokumenty", "uzupelnijPodpisy", "wymianaTerminalaCB", "zmianaWarunkowCashbackCB"];
        restrictionsMap["logoKalkulatorSesjaCB"] =  ["poprawDane","pakiet","odrzucDokumenty", "uzupelnijPodpisy", "wymianaTerminalaCB"];

        restrictionsMap["ekonomicznyCB"] =  ["nowaUmowa", "pakiet","komfort", "prestiz", "uzupelnijPodpisy", "poprawDane","odrzucDokumenty", "wymianaTerminalaCB"];
        restrictionsMap["komfortCB"] =  ["nowaUmowa", "pakiet","ekonomiczny", "prestiz", "uzupelnijPodpisy", "poprawDane","odrzucDokumenty", "wymianaTerminalaCB"];
        restrictionsMap["prestizCB"] =  ["nowaUmowa", "pakiet","ekonomiczny", "komfort", "uzupelnijPodpisy", "poprawDane","odrzucDokumenty", "wymianaTerminalaCB"];

        restrictionsMap["poprawDaneCB"] = ["nowaUmowa", "pakiet","rozszerzenie", "zmianaWarunkow", "odrzucDokumenty","uzupelnijPodpisy", "pakietSerwisowy", "dodatkoweFuncjonalnosci", "wymianaTerminalaCB"];
        restrictionsMap["odrzucDokumentyCB"] = ["nowaUmowa","pakiet","rozszerzenie", "zmianaWarunkow", "poprawDane", "pakietSerwisowy","uzupelnijPodpisy", "dodatkoweFuncjonalnosci", "wymianaTerminalaCB"];
        restrictionsMap["uzupelnijPodpisyCB"] = ["nowaUmowa","pakiet","rozszerzenie", "zmianaWarunkow", "poprawDane","odrzucDokumenty", "pakietSerwisowy", "dodatkoweFuncjonalnosci", "wymianaTerminalaCB"];

        restrictionsMap["wymianaTerminalaCB"] = ["nowaUmowa","pakiet","rozszerzenie", "zmianaWarunkow", "dodatkoweFuncjonalnosci", "poprawDane","odrzucDokumenty", "uzupelnijPodpisy"];

        restrictionsMap["pakietStartCB"] = ["nowaUmowa", "rozszerzenie", "zmianaWarunkow", "dodatkoweFuncjonalnosci", "poprawDane", "odrzucDokumenty",
            "uzupelnijPodpisy", "wymianaTerminalaCB", "pakietStartPlusCB", "pakietMobilnyCB"];
        restrictionsMap["pakietStartPlusCB"] = ["nowaUmowa", "rozszerzenie", "zmianaWarunkow", "dodatkoweFuncjonalnosci", "poprawDane", "odrzucDokumenty",
            "uzupelnijPodpisy", "wymianaTerminalaCB", "pakietStartCB", "pakietMobilnyCB"];
        restrictionsMap["pakietMobilnyCB"] = ["nowaUmowa", "rozszerzenie", "zmianaWarunkow", "dodatkoweFuncjonalnosci", "poprawDane", "odrzucDokumenty",
            "uzupelnijPodpisy", "wymianaTerminalaCB", "pakietStartCB", "pakietStartPlusCB"];

        var restrictions = new Array();

        that.defaults = {

            // hideMode
            // -----------
            // Specifies method to hide the content element.
            //
            // Default: fadeToggle
            //
            // Values:
            // - fadeToggle: Use jquery.fadeToggle()
            // - basic: Use jquery.toggle()
            // - css: relies on user provided css to show/hide. you can define
            //   classes for "collapsed" and "expanded" classes.
            // - a function : custom toggle function. The function receives 3 arguments
            //                expander: the element that triggered the toggle
            //                targets: the items to toggle
            //                expanded: true if expanding; false if collapsing
            //
            // If un an unknown value is specified, the plug-in reverts to "css".
            'hideMode': 'basic',

            // searchMode
            // -----------
            // Specifies the defaut value for  data-expander-target-search
            // when none is specified on the expander element.
            //
            // Default: parent
            //
            // Values:
            // - parent: go up the expander's parents hierarchy searching 
            //           each parent's childens looking for a target
            //
            // - absolute : finds a target globally in the document (useful when 
            //              matching an id)
            //
            // - relative : finds a target nested inside the expander
            //
            // If un an unknown value is specified, no targets will be found.
            'defaultSearchMode': 'parent',

            // defaultTarget
            // -----------
            // Specifies the defaut value for data-expander-target when
            // none is specified on the expander element.
            //
            // Default: .content
            'defaultTarget': '.content',

            // throwOnMissingTarget
            // -----------
            // Specifies whether the plug-in throws an exception if it
            // cannot find a target for the expander 
            //
            // Default: true
            'throwOnMissingTarget': true,

            // keepStateInCookie
            // -----------
            // Specifies whether the plug-in keeps the expended/collapsed state 
            // in a cookie for the next time.
            //
            // Default: false
            //
            // Notes:
            // - This only works for expanders with an Id attribute.
            // - Make sure you load the jQuery cookie plug-in (https://github.com/carhartl/jquery-cookie/)
            //   before simple-expand is loaded.
            //     
            'keepStateInCookie': false,
            'cookieName': 'simple-expand'
        };

        that.settings = {};
        $.extend(that.settings, that.defaults);

        // Search in the children of the 'parent' element for an element that matches 'filterSelector'
        // but don't search deeper if a 'stopAtSelector' element is met.
        //     See this question to better understand what this does.
        //     http://stackoverflow.com/questions/10902077/how-to-select-children-elements-but-only-one-level-deep-with-jquery
        that.findLevelOneDeep = function (parent, filterSelector, stopAtSelector) {
            return parent.find(filterSelector).filter(function () {
                return !$(this).parentsUntil(parent, stopAtSelector).length;
            });
        };

        // Hides targets
        that.setInitialState = function (expander, targets) {
            var isExpanded = that.readState(expander);

            if (isExpanded) {
                expander.removeClass("collapsed").addClass("expanded");
                that.show(targets);
            } else {
                expander.removeClass("expanded").addClass("collapsed");
                that.hide(targets);
            }
        };

        that.hide = function (targets) {
            if (that.settings.hideMode === "fadeToggle") {
                targets.hide();
            } else if (that.settings.hideMode === "basic") {
                targets.hide();
            }
        };

        that.show = function (targets) {
            if (that.settings.hideMode === "fadeToggle") {
                targets.show();
            } else if (that.settings.hideMode === "basic") {
                targets.show();
            }
        };

        // assert that $.cookie if 'keepStateInCookie' option is enabled
        that.checkKeepStateInCookiePreconditions = function () {
            if (that.settings.keepStateInCookie && $.cookie === undefined){
                throw new Error("simple-expand: keepStateInCookie option requires $.cookie to be defined.");
            }
        };

        // returns the cookie
        that.readCookie = function () {
            var jsonString = $.cookie(that.settings.cookieName);
            if ( jsonString === null  || jsonString === '' ){
                return {};
            }
            else{
                return JSON.parse(jsonString);
            }
        };

        // gets state for the expander from cookies
        that.readState = function (expander) {
            if (!that.settings.keepStateInCookie){
                return false;
            }

            var id = expander.attr('Id');
            if (id === undefined){
                return;
            }

            var cookie = that.readCookie();
            var isExpanded = cookie[id] === true || false;
            return isExpanded;
        };

        // save states of the item in the cookies
        that.saveState = function (expander, isExpanded) {
            if (!that.settings.keepStateInCookie){
                return;
            }

            var id = expander.attr('Id');
            if (id === undefined){
                return;
            }

            var cookie = that.readCookie();
            cookie[id] = isExpanded;
            $.cookie(that.settings.cookieName, JSON.stringify(cookie), { raw: true, path:window.location.pathname });
        };

        // Toggles the targets and sets the 'collapsed' or 'expanded'
        // class on the expander
        that.toggle = function (expander, targets) {

            var isExpanded = that.toggleCss(expander);

            if (that.settings.hideMode === "fadeToggle") {
                targets.fadeToggle(150);
            } else if (that.settings.hideMode === "basic") {
                targets.toggle();
            } else if ($.isFunction(that.settings.hideMode)) {
                that.settings.hideMode(expander, targets, isExpanded);
            }

            that.saveState(expander, isExpanded);

            // prevent default to stop browser from scrolling to: href="#"
            return false;
        };

        // Toggles using css
        that.toggleCss = function (expander) {
            if (expander.hasClass("expanded")) {
                expander.toggleClass("collapsed expanded");
                return false;
            }
            else {
                expander.toggleClass("expanded collapsed");
                return true;
            }
        };

        // returns the targets for the given expander
        that.findTargets = function (expander, searchMode, targetSelector) {
            // find the targets using the specified searchMode
            var targets = [];
            if (searchMode === "absolute") {
                targets = $(targetSelector);
            }
            else if (searchMode === "relative") {
                targets = that.findLevelOneDeep(expander, targetSelector, targetSelector);
            }
            else if (searchMode === "parent") {

                // Search the expander's parents recursively until targets are found.
                var parent = expander.parent();
                do {
                    targets = that.findLevelOneDeep(parent, targetSelector, targetSelector);

                    // No targets found, prepare for next iteration...
                    if (targets.length === 0) {
                        parent = parent.parent();
                    }
                } while (targets.length === 0 && parent.length !== 0);
            }
            return targets;
        };

        that.activate = function (jquery, options) {
            $.extend(that.settings, options);

            that.checkKeepStateInCookiePreconditions();


            // Plug-in entry point
            //
            // For each expander:
            //    search targets
            //    hide targets
            //    register to targets' click event to toggle them on click
            jquery.each(function () {
                var expander = $(this);

                var targetSelector = expander.attr("data-expander-target") || that.settings.defaultTarget;
                var searchMode = expander.attr("data-expander-target-search") || that.settings.defaultSearchMode;

                var targets = that.findTargets(expander, searchMode, targetSelector);

                // no elements match the target selector
                // there is nothing we can do
                if (targets.length === 0) {
                    if (that.settings.throwOnMissingTarget) {
                        throw "simple-expand: Targets not found";
                    }
                    return this;
                }

                that.setInitialState(expander, targets);

                // hook the click on the expander
                expander.click(function (event) {
                    return that.toggle(expander, targets);
                });


                var checkboxes = targets.find("input:checkbox");

                $.each(checkboxes,function(index, value){
                    value.addEventListener("change", changeActivityEvent, false);
                })

            });

            $('#nowaUmowaCB').change(changeActivityEvent);
            $('#poprawDane').change(changeActivityEvent);
            $('#odrzucDokumenty').change(changeActivityEvent);
            $('#uzupelnijPodpisyCB').change(changeActivityEvent);
            $('#wymianaTerminalaCB').change(changeActivityEvent);

            $('input:checkbox[data-selected="true"]').each(function( index ) {
                $(this).attr('checked', true);
                changeActivity(this);
                var expander = $(this).closest( ".expendable").find(".expander")
                if(!$(expander).hasClass("expanded") && $(expander).prop('data-disabled') != true){
                    toggleExpander(expander)
                }
             })

        };

        function toggleExpander(expanderTmp){
            var targetSelector1 = expanderTmp.attr("data-expander-target") || that.settings.defaultTarget;
            var searchMode1 = expanderTmp.attr("data-expander-target-search") || that.settings.defaultSearchMode;
            var targets1 = that.findTargets(expanderTmp, searchMode1, targetSelector1);
            that.toggle(expanderTmp, targets1)
        }


        function changeActivityEvent (e) {
            changeActivity( e.target);
        }

        function changeActivity (target) {
            var elements = restrictionsMap[target.id]
            console.log(elements);

            if(target.checked){
                $.each(elements,function(index, value){
                    if(restrictions.indexOf(value) == -1){
                        disableItem(value)
                    }
                    restrictions.push(value);
                })

            }
            else{
                $.each(elements,function(index, value){
                    var itemIndex = restrictions.indexOf(value);
                    if(itemIndex >= 0){
                        restrictions.splice(itemIndex,1);

                        if(restrictions.indexOf(value) == -1){
                            enableItem(value)
                        }
                    }
                })
            }
        }

        function disableItem (itemId) {
            var item = $('#'+itemId);
            if(item.is("input")){
                item = item.parents(".checkBoxBlock");
            }
            item.addClass("disabled-activity");
            item.prop("data-disabled", true);
            item.attr('disabled', 'disabled');
            item.find("input").prop("disabled", true);

            var disabledExpander = item.children("a[class*='expander']:first");
            if(disabledExpander){
                var targetSelector = disabledExpander.attr("data-expander-target") || that.settings.defaultTarget;
                var searchMode = disabledExpander.attr("data-expander-target-search") || that.settings.defaultSearchMode;
                var targets = that.findTargets(disabledExpander, searchMode, targetSelector);

                if(disabledExpander.hasClass("expanded")){
                    that.toggle(disabledExpander, targets);
                }

                var checkboxes = item.find("input:checkbox");//.removeAttr("checked","");

                $.each(checkboxes,function(index, value){

                    if($(value).attr('checked') !== undefined){

                        var elements = restrictionsMap[value.id]

                        $.each(elements,function(index, value){
                            var itemIndex = restrictions.indexOf(value);
                            if(itemIndex >= 0){
                                restrictions.splice(itemIndex,1);
                                if(restrictions.indexOf(value) == -1){
                                    enableItem(value)
                                }
                            }
                        })

                        $(value).removeAttr("checked");
                    }

                    value.addEventListener("change", changeActivityEvent, false);
                })


            }
        }

        function enableItem (itemId) {
            var item = $('#'+itemId);
            if(item.is("input")){
                item = item.parents(".checkBoxBlock");
            }
            item.removeClass("disabled-activity");
            item.removeAttr('disabled');
            item.prop("data-disabled", false);
            item.find("input").prop("disabled", false);
        }
    }



// export SimpleExpand
    window.SimpleExpand = SimpleExpand;

// expose SimpleExpand as a jQuery plugin
    $.fn.simpleexpand = function (options) {
        var instance = new SimpleExpand();
        instance.activate(this, options);
        return this;
    };


    $(document).ready(function () {
        $('.expander').simpleexpand();
    });

}(jQuery));
