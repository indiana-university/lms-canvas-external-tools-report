function termSelect(select) {
    // disable the delete button until a term is selected
    var selectedTerm = select.value;
    var deleteButton = document.getElementById("deleteButton");
    if (selectedTerm != "") {
         deleteButton.removeAttribute("disabled");
    } else {
        deleteButton.setAttribute("disabled", "disabled");
    }
}

function deleteConfirmation(button) {
    var termName = document.getElementById("termToDelete").value;
    var modalText = document.getElementById("modal-text");
    modalText.textContent = "All term data for " + termName + " will be deleted. Would you like to continue?";
}

function addLoadingIndicator(button) {
    button.classList.add("rvt-button--loading");
    button.setAttribute("aria-busy", "true");
    
    if (button.dataset.action != null) {
        document.getElementById("submitAction").value = button.dataset.action;
    }
    
    var buttons = document.getElementsByClassName("modal-button");
    for (var i=0; i < buttons.length; i++) {
        buttons[i].setAttribute("disabled", "disabled");
    }
    
    // FF doesn't need this, but Chrome and Edge do
    button.form.submit();
}