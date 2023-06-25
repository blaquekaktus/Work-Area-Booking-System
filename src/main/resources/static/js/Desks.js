function createDesk(desk) {
    const room = document.querySelector('.room');
    const deskEl = document.createElement('div');
    deskEl.classList.add('desk');
    deskEl.id = `desk${desk.id}`;

    // Add the desk number as text in the desk element
    deskEl.textContent = desk.deskNr;

    // Set the color based on the booking status
    switch (desk.bookingStatus) {
        case 'AM':
            deskEl.style.backgroundColor = 'yellow';
            break;
        case 'PM':
            deskEl.style.backgroundColor = 'orange';
            break;
        case 'FULL':
            deskEl.style.backgroundColor = 'red';
            break;
        default:
            deskEl.style.backgroundColor = 'green';
            break;
    }
    // Attach a click event listener
    deskEl.addEventListener('click', function() {
        bookDesk(desk.id);
    });

    // Add the desk element to the floor
        room.appendChild(deskEl);
}

// Function to fetch desk data from the server and update the room
function updateDesks() {
    fetch('/api/desks')
        .then(response => response.json())
        .then(desks => {
            // Remove all existing desks from the room
            document.querySelector('.room').innerHTML = '';

            // Create new desk elements
            desks.forEach(desk => createDesk(desk));
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

// Update the desks when the page loads, and every 5 seconds afterwards.
updateDesks();
setInterval(updateDesks, 5000);

function fetchDeskStatus() {
    fetch('/api/desks') // replace with your actual API endpoint
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(desks => {
            // 'desks' is a JavaScript object that contains the data from the server
            // Now call the function to create the grid with this data
            createDesk(desks);
        })
        .catch(e => {
            console.log('There was a problem with the fetch operation: ' + e.message);
        });
}

// Call the function when the page loads
fetchDeskStatus();

/*
// Create the desks when the page loads
window.onload = () => {
    createDesks(desks);
}; */

let desks = Array.from(document.querySelectorAll('.desk-data')).map(el => ({
    id: parseInt(el.id.replace('desk', '')),
    bookingStatus: el.dataset.status
}));

// Call createDesk() for each desk
desks.forEach(desk => createDesk(desk));