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

$(document).ready(function()
{
    $('#ventenc').prop('disabled', true);
    $('#ventnondeb').prop('disabled', true);
    $('#ventter').prop('disabled', true);
});