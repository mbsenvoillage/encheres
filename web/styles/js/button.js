function disableVentes() {
    document.getElementById("ventenc").disabled = true;
    document.getElementById("ventnondeb").disabled = true;
    document.getElementById("ventter").disabled = true;
    document.getElementById("enchouv").disabled = false;
    document.getElementById("enchenc").disabled = false;
    document.getElementById("enchremp").disabled = false;
}

function disableAchats() {
    document.getElementById("enchouv").disabled = true;
    document.getElementById("enchenc").disabled = true;
    document.getElementById("enchremp").disabled = true;
    document.getElementById("ventenc").disabled = false;
    document.getElementById("ventnondeb").disabled = false;
    document.getElementById("ventter").disabled = false;
}

$(document).ready(function ()
{
    $('#enchouv').click(function (){
        if($(this).prop("checked") === true) {
            $('#enchenc').prop("disabled", true);
            $('#enchremp').prop("disabled", true);
        } else if ($(this).prop("checked") === false) {
            $('#enchenc').prop("disabled", false);
            $('#enchremp').prop("disabled", false);
        }
    })

    $('#enchenc').click(function (){
        if($(this).prop("checked") === true) {
            $('#enchouv').prop("disabled", true);
            $('#enchremp').prop("disabled", true);
        } else if ($(this).prop("checked") === false) {
            $('#enchouv').prop("disabled", false);
            $('#enchremp').prop("disabled", false);
        }
    })

    $('#enchremp').click(function (){
        if($(this).prop("checked") === true) {
            $('#enchouv').prop("disabled", true);
            $('#enchenc').prop("disabled", true);
        } else if ($(this).prop("checked") === false) {
            $('#enchouv').prop("disabled", false);
            $('#enchenc').prop("disabled", false);
        }
    })

    $('#ventenc').click(function (){
        if($(this).prop("checked") === true) {
            $('#ventnondeb').prop("disabled", true);
            $('#ventter').prop("disabled", true);
        } else if ($(this).prop("checked") === false) {
            $('#ventnondeb').prop("disabled", false);
            $('#ventter').prop("disabled", false);
        }
    })

    $('#ventnondeb').click(function (){
        if($(this).prop("checked") === true) {
            $('#ventenc').prop("disabled", true);
            $('#ventter').prop("disabled", true);
        } else if ($(this).prop("checked") === false) {
            $('#ventenc').prop("disabled", false);
            $('#ventter').prop("disabled", false);
        }
    })

    $('#ventter').click(function (){
        if($(this).prop("checked") === true) {
            $('#ventenc').prop("disabled", true);
            $('#ventnondeb').prop("disabled", true);
        } else if ($(this).prop("checked") === false) {
            $('#ventenc').prop("disabled", false);
            $('#ventnondeb').prop("disabled", false);
        }
    })
});

/* function disableAchatsCBoxECET() {

    if (!document.getElementById("enchouv").disabled) {
        document.getElementById("enchenc").disabled = true;
        document.getElementById("enchremp").disabled = true;
    } else {
        document.getElementById("enchenc").disabled = false;
        document.getElementById("enchremp").disabled = false;
    }
}*/

$(document).ready(function()
{
    $('#ventenc').prop('disabled', true);
    $('#ventnondeb').prop('disabled', true);
    $('#ventter').prop('disabled', true);
});