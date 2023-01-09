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
    var termSelect = document.getElementById("termToDelete");
    var termName = termSelect.options[termSelect.selectedIndex].text;
    var modalText = document.getElementById("modal-text");
    modalText.textContent = "All term data for " + termName + " will be deleted. Would you like to continue?";
    document.getElementById("submittedTermToDelete").value = termSelect.value;
}

function addLoadingIndicator(button) {
    var contents = document.getElementsByClassName("button-content");
    var loaders = document.getElementsByClassName("button-loader");

    for (var i=0; i < contents.length; i++) {
        contents[i].classList.add("rvt-button__content");
    }
    button.classList.add("rvt-button--loading");
    button.setAttribute("aria-busy", "true");

    for (var i=0; i < loaders.length; i++) {
        loaders[i].classList.remove("rvt-display-none");
    }

    var buttons = document.getElementsByClassName("modal-button");
    for (var i=0; i < buttons.length; i++) {
        buttons[i].setAttribute("disabled", "disabled");
    }

    if (button.dataset.action == "upload") {
        document.getElementById("uploadForm").submit();
    } else {
        document.getElementById("deleteForm").submit();
    }
}