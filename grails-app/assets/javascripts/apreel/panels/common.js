function clearFields(fieldsContainer) {
    fieldsContainer.find("input[type='checkbox']").removeAttr("checked");
    fieldsContainer.find("input[type='radio']").removeAttr("checked");
    fieldsContainer.find("input[type='text']").val('');
    fieldsContainer.find("select").val('');
}

function disableFields(fieldsContainer) {
    fieldsContainer.find("input, select").attr('disabled', 'disabled');
}

function enableFields(fieldsContainer) {
    fieldsContainer.find("input, select").removeAttr('disabled');
}