document.querySelectorAll('.desk').forEach(function(desk) {
    desk.addEventListener('click', function() {
        var deskId = this.getAttribute('data-desk-id'); // Assuming each desk element has a data-desk-id attribute with the desk's id.
        bookDesk(deskId);
    });
});

    function bookDesk(deskId) {
        fetch('/DeskBooking/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                deskId: deskId,
            }),
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // If the booking was successful, change the color of the desk.
                    document.querySelector(`[data-desk-id="${deskId}"]`).style.backgroundColor = 'green';
                } else {
                    // If the booking was not successful, display an error message to the user.
                    alert(data.errorMessage);
                }
            })

            .catch((error) => {
                console.error('Error:', error);
                alert('An error occurred while booking the desk. Please try again.');
            });
    }