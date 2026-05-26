window.addEventListener("DOMContentLoaded", function () {
    flatpickr("#expectedRentalOn", {
        dateFormat: "Y/m/d",
        allowInput: true
    });

    flatpickr("#expectedReturnOn", {
        dateFormat: "Y/m/d",
        allowInput: true
    });
});