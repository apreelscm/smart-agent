<div id="acceptorCorrespondenceAddressPanel">
    <fieldset>
        <div class="belka-glowna"><g:message code="panel.acceptor.correspondence.addres.title"/></div>
            <div style="text-align: center; padding-top: 20px;" class="centre">

                <div style="width: 700px; text-align: left; padding-bottom: 20px" class="centre">
                    <input type="checkbox" name="merchantAddress" value="1" /><g:message code="panel.as.merchant"/>
                </div>

                <div style="width: 700px" class="centre">
                    <div style="display: inline-block; text-align: left">
                           <div>
                               <div style="display:inline-block; width: 50px"><g:message code="panel.street"/></div>
                               <div style="display:inline-block; ">
                                   <select name="correspondenceAddressStreetTitle">
                                       <option value="ulica">ulica</option>
                                       <option value="osiedle">osiedle</option>
                                       <option value="aleja">aleja</option>
                                       <option value="plac">plac</option>
                                   </select>
                               </div>
                               <div style="display:inline-block; "><g:textField name="correspondenceAddressStreet" style="width: 200px"/></div>
                           </div>
                           <div style="text-align: left">
                               <div style="display:inline-block; width: 50px"><g:message code="panel.city"/></div>
                               <div style="display:inline-block; width: inherit"><g:textField name="correspondenceAddressCity" style="width: 280px"/></div>
                           </div>
                           <div style="text-align: left">
                               <div style="display:inline-block; width: 50px"><g:message code="panel.postal"/></div>
                               <div style="display:inline-block; width: inherit"><g:textField name="correspondenceAddressPostal" style="width: 280px"/></div>
                           </div>
                    </div>
                    <div style="display: inline-block; float: right">
                            <div>
                                <div style="display:inline-block; "><g:message code="panel.house.number"/></div>
                                <div style="display:inline-block; "><g:textField name="correspondenceAddressHomeNumber" style="width: 70px;"/></div>
                                <div style="display:inline-block; "><g:message code="panel.flat.number"/></div>
                                <div style="display:inline-block; "><g:textField name="correspondenceAddressFlatNumber" style="width: 70px"/></div>
                            </div>
                            <div style="text-align: right">
                                <div style="display:inline-block; "><g:message code="panel.postal.code"/></div>
                                <div style="display:inline-block; "><g:textField name="correspondenceAddressPostalCode" style="width: 100px"/></div>
                            </div>
                    </div>
                </div>

            </div>
    </fieldset>
</div>