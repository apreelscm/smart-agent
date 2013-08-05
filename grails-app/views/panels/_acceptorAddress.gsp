<div id="acceptorAddressPanel">
    <fieldset>
        <div class="belka-glowna">Siedziba Akceptanta</div>
            <div style="text-align: center; padding-top: 20px;" class="centre">
                <div style="width: 700px" class="centre">
                    <div style="display: inline-block; text-align: left">
                           <div>
                               <div style="display:inline-block; width: 50px">Ulica</div>
                               <div style="display:inline-block; " name="addressStreetTitle">
                                   <select>
                                       <option value="ulica">ulica</option>
                                       <option value="osiedle">osiedle</option>
                                       <option value="aleja">aleja</option>
                                       <option value="plac">plac</option>
                                   </select>
                               </div>
                               <div style="display:inline-block; "><g:textField name="addressStreet" style="width: 200px"/></div>
                           </div>
                           <div style="text-align: left">
                               <div style="display:inline-block; width: 50px">Miasto</div>
                               <div style="display:inline-block; width: inherit"><g:textField name="addressCity" style="width: 280px"/></div>
                           </div>
                           <div style="text-align: left">
                               <div style="display:inline-block; width: 50px">Poczta</div>
                               <div style="display:inline-block; width: inherit"><g:textField name="addressPostal" style="width: 280px"/></div>
                           </div>
                    </div>
                    <div style="display: inline-block; float: right">
                            <div>
                                <div style="display:inline-block; ">Nr domu</div>
                                <div style="display:inline-block; "><g:textField name="addressHomeNumber" style="width: 70px;"/></div>
                                <div style="display:inline-block; ">Nr lokalu</div>
                                <div style="display:inline-block; "><g:textField name="addressFlatNumber" style="width: 70px"/></div>
                            </div>
                            <div style="text-align: right">
                                <div style="display:inline-block; ">Kod pocztowy</div>
                                <div style="display:inline-block; "><g:textField name="addressPostalCode" style="width: 100px"/></div>
                            </div>
                    </div>

                    <div style="display:inline-block; padding-top: 20px">Tel stacjonarny</div>
                    <div style="display:inline-block; "><g:textField name="addressHomeNumber" style="width: 100px;"/></div>
                    <div style="display:inline-block; padding-left: 30px">Fax</div>
                    <div style="display:inline-block; "><g:textField name="addressFlatNumber" style="width: 100px"/></div>
                    <div style="display:inline-block; padding-left: 30px">Tel komórkowy</div>
                    <div style="display:inline-block; "><g:textField name="addressFlatNumber" style="width: 100px"/></div>
                </div>
            </div>
    </fieldset>
</div>