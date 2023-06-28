function scaleImageMap() {
    var image = document.getElementById('image');
    var map = document.querySelector('map[name="image-map"]');
    var areas = map.getElementsByTagName('area');

    var widthRatio = image.clientWidth / image.naturalWidth;
    var heightRatio = image.clientHeight / image.naturalHeight;

    for (var i = 0; i < areas.length; i++) {
        var coords = areas[i].getAttribute('coords').split(',');
        var scaledCoords = [];

        for (var j = 0; j < coords.length; j++) {
            if (j % 2 === 0) {
                // X coordinate
                scaledCoords.push(Math.round(coords[j] * widthRatio));
            } else {
                // Y coordinate
                scaledCoords.push(Math.round(coords[j] * heightRatio));
            }
        }

        areas[i].setAttribute('coords', scaledCoords.join(','));
    }
}


function displayPopup() {
    var tooltip = document.createElement('div');
    tooltip.innerText = 'Klick mich zum Buchen';
    tooltip.classList.add('tooltip');

    document.body.appendChild(tooltip);

    document.addEventListener('mousemove', moveTooltip);

    function moveTooltip(event) {
        tooltip.style.left = event.clientX + 'px';
        tooltip.style.top = event.clientY + 'px';
    }

    function removeTooltip() {
        document.removeEventListener('mousemove', moveTooltip);
        document.body.removeChild(tooltip);
    }

    document.addEventListener('mouseout', removeTooltip);
}