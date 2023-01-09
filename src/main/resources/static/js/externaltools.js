/*-
 * #%L
 * external-tools-report
 * %%
 * Copyright (C) 2022 - 2023 Indiana University
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Indiana University nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
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
