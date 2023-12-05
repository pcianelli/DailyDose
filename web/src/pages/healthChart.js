import DailyDoseClient from '../api/dailyDoseClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

class HealthChart extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'clientLoaded', 'displayMedications', 'showLoading', 'hideLoading', 'removeNotificationClicked', 'showSuccessMessageAndRedirect', 'handleModalButtonClick', 'handleCloseModalClick', 'handleMenuListClick'], this);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
        this.client = new DailyDoseClient();
        this.dataStore.addChangeListener(this.displayMedications);
        console.log("viewMedications constructor");
    }

    mount() {
        this.header.addHeaderToPage();
        this.clientLoaded();
    }

    showLoading() {
        document.getElementById('medications-loading').innerText = "(Loading medications...)";
    }

    hideLoading() {
        document.getElementById('medications-loading').style.display = 'none';
    }

    /**
    * Once the client is loaded, get the list of medications.
    */
    async clientLoaded() {
    console.log("client Loaded called...");
        this.showLoading();
        const result = await this.client.getMedications();
        this.hideLoading();
        console.log("Result in clientLoaded:", result);
        const medications = result.medications;

        this.dataStore.set('medications', medications);
    }

    removeNotificationClicked(medName, time) {
        // Handle the "Remove Notification" button click
        console.log(`Remove Notification Clicked for ${medName} at ${time}`);
        // Add your logic to call the removeNotification method in your DailyDoseClient
        try {
            // Call the removeNotification method in your DailyDoseClient
            this.client.removeNotification(medName, time);

            // Show success message and redirect
            this.showSuccessMessageAndRedirect();
        } catch (error) {
            console.error('Error removing notification:', error);
            // Handle error
        }
    }

    showSuccessMessageAndRedirect() {
        // Hide everything except the header and body background
        const allChildren = document.body.children;

        for (let i = 0; i < allChildren.length; i++) {
            const element = allChildren[i];
            if (element.id !== 'header') {
                element.style.display = 'none';
            }
        }

        // Create success message with card class
        const messageElement = document.createElement('div');
        messageElement.className = 'card';  // Add the card class
        const messageText = document.createElement('p');
        messageText.innerText = "Notification has been removed from your health chart successfully!";
        messageText.style.textAlign = "center";
        messageText.style.color = "#FFFFFF";
        messageText.style.fontSize = "40px";
        messageText.style.margin = "20px 0";
        messageElement.appendChild(messageText);
        document.body.appendChild(messageElement);
        setTimeout(() => {
            window.location.href = `/healthChart.html`;
        }, 3000);  // redirect after 3 seconds
    }

    displayMedications() {
        console.log("Displaying Medications...");
        const medications = this.dataStore.get('medications');
        console.log('Medications:', medications);
        const displayDiv = document.getElementById('medication-list-display');

        const uniqueMedicationNames = new Set(medications.map(medication => medication.medName));
        console.log('Unique Medication Names:', Array.from(uniqueMedicationNames));

        if (!medications || medications.length === 0) {
                displayDiv.innerText = "No available medications.";
                return;
            }

        medications.forEach(medication => {
             console.log('Medication:', medication);
             console.log('Medication Name:', medication.medName);
             console.log('Medication Info:', medication.medInfo);
             console.log('Notification Times:', medication.notificationTimes);

            const medicationCard = document.createElement('section');
            medicationCard.className = 'medication-card';

            const medicationName = document.createElement('h2');
            medicationName.className = 'medication-name';
            medicationName.innerHTML = medication.medName;

            const medicationInfo = document.createElement('h3');
            medicationInfo.className = 'medication-info';
            if (medication.medInfo !== null) {
                medicationInfo.innerHTML = "Info: " + medication.medInfo;
            } else {
                medicationInfo.innerHTML = medication.medInfo || ""; // Use an empty string if medInfo is null
            }

            medicationCard.appendChild(medicationName);
            medicationCard.appendChild(medicationInfo);

            // convert Set<notificationModel> to array so I can use for loop
            const notifications = Array.from(medication.notificationTimes || []);
            console.log('Notifications:', notifications);


            notifications.forEach(notification => {
                const time = notification.time;
                const parsedTime = new Date(`2000-01-01T${time}`);

                // Get hours and minutes
                const hours = parsedTime.getHours();
                const minutes = parsedTime.getMinutes();

                // Convert to AM/PM format
                const amPm = hours >= 12 ? 'PM' : 'AM';
                const displayHours = hours % 12 || 12; // Convert 0 to 12 for noon/midnight

                // Format the time string
                const formattedTime = `${displayHours}:${minutes.toLocaleString('en-US', {minimumIntegerDigits: 2})} ${amPm}`;

                const timeElement = document.createElement('h4');
                timeElement.className = 'alarm-time';
                timeElement.innerHTML = `Alarm Time: ${formattedTime}`;

                // Create "Remove Notification" button
                const removeButton = document.createElement('button');
                removeButton.innerText = 'Remove Alarm';
                removeButton.className = 'remove-notification-button';

                // Attach a click event listener to the button
                removeButton.addEventListener('click', () => {
                    this.removeNotificationClicked(medication.medName, time); // Add your logic to handle the button click
                });
                const removeButtonContainer = document.createElement('div');
                removeButtonContainer.className = 'remove-button-container';
                removeButtonContainer.appendChild(removeButton);

                const notificationContainer = document.createElement('div');
                notificationContainer.className = 'notification-container';
                notificationContainer.appendChild(timeElement);
                notificationContainer.appendChild(removeButtonContainer);

                medicationCard.appendChild(notificationContainer);
            });

            displayDiv.appendChild(medicationCard);
        });
    }

    handleModalButtonClick() {
        const modal = document.getElementById('myModal');
        const button = document.querySelector('.button2');

        // Toggle the modal visibility
        modal.style.display = 'block';

        // Add the class to hide the button
        button.classList.add('button2--hidden');
    }

    handleCloseModalClick() {
        const modal = document.getElementById('myModal');
        const button = document.querySelector('.button2');

        // Toggle the modal visibility
        modal.style.display = 'none';

        // Remove the class to show the button
        button.classList.remove('button2--hidden');
    }

    handleMenuListClick(event) {
        event.stopPropagation();
    }
}

const main = async () => {
    const healthCart = new HealthChart();
    healthCart.mount();

    // Open modal
    const openModalButton = document.getElementById('openModalButton');
    openModalButton.addEventListener('click', () => {
        healthCart.handleModalButtonClick();
    });

    // Close modal
    const closeModalButton = document.getElementById('closeModal');
    closeModalButton.addEventListener('click', () => {
        healthCart.handleCloseModalClick();
    });

    // Close modal when clicking outside the modal
    window.onclick = function (event) {
        console.log('Window clicked');
        const modal = document.getElementById('myModal');
        if (event.target === modal) {
            modal.style.display = 'none';
            healthCart.handleCloseModalClick();
        }
    };
};

window.addEventListener('DOMContentLoaded', main);