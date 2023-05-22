//alert("JavaScript-Code wird ausgeführt!");

// Adjust dropdown width when expanded
let dropdowns = document.querySelectorAll(".dropdown");
dropdowns.forEach((dropdown) => {
    let content = dropdown.querySelector(".dropdown-content");
    let links = content.querySelectorAll("a");
    let width = 0;
    links.forEach((link) => {
        width = Math.max(width, link.offsetWidth);
    });
    content.style.width = width + "px";
});

// Toggle dropdown content on click
let dropdownToggles = document.querySelectorAll(".dropdown > a");
dropdownToggles.forEach((toggle) => {
    let content = toggle.nextElementSibling;
    toggle.addEventListener("click", (event) => {
        event.preventDefault();
        content.classList.toggle("open");
    });
});