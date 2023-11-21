import DailyDoseClient from '../api/dailyDoseClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
* Logic needed for the add notification page of the website.
*/

class AddNotification extends BindingClass {
    constructor() {
        super();
        this.bindingClassMethods(['mount', 'submit', 'showSuccessMessageAndRedirect'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.redirectToHealthChart);
        this.header = new Header(this.dataStore);
    }

    /**
    * Add the header to the page and load the dailyDoseClient.
    */
    async mount() {
        console.log('Mounting AddNotification');
        await this.header.addHeaderToPage();
        this.client = new DailyDoseClient();
        document.getElementById('add-notification-form').addEventListener('submit', this.submit);
        console.log('AddNotification mounted successfully');
    }

    /**
    * Method to run when the add medication form is submitted.
    * Calls the DailyDoseService to add the medication.
    */
    async submit(event) {
        event.preventDefault();

        const medName = document.getElementById('medName').value;
        const timeInput = document.getElementById('time');
        const time = formatTime(timeInput.value);

        // Convert time to 24-hour format
        const [hours, minutes] = time.split(':');
        const ampm = hours < 12 ? 'am' : 'pm';
        const convertedHours = (hours % 12) || 12; // Convert 0 to 12

        // Format the time with seconds set to 00
        const formattedTime = `${String(convertedHours).padStart(2, '0')}:${minutes}:00 ${ampm}`;

        try {
            const addNotification = await this.client.addNotification({
                medName: medName,
                time: formattedTime,
            });
            this.showSuccessMessageAndRedirect();
        } catch (error) {
            console.error('Error adding medication: ', error);
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
        messageText.innerText = "Notification has been added to your health chart successfully!";
        messageText.style.color = "#2c3e50";
        messageText.style.fontSize = "40px";
        messageText.style.margin = "20px 0";
        messageElement.appendChild(messageText);
        document.body.appendChild(messageElement);
        setTimeout(() => {
            const currentHostname = window.location.hostname;
            const isLocal = currentHostname === 'localhost' || currentHostname === '127.0.0.1';
            const baseUrl = isLocal ? 'http://localhost:8000/' : 'https://d3hqn9u6ae71hc.cloudfront.net/';
            window.location.href = `${baseUrl}healthChart.html`;
        }, 3000);  // redirect after 3 seconds
    }
}

/**
 * Format time to ensure it's in HH:mm:ss format.
 * @param {string} inputTime - The time input from the user.
 * @returns {string} - Formatted time in HH:mm:ss format.
 */
function formatTime(inputTime) {
    // Assuming the inputTime is in HH:mm format
    const currentTime = new Date();
    const [hours, minutes] = inputTime.split(':');
    currentTime.setHours(hours);
    currentTime.setMinutes(minutes);
    currentTime.setSeconds(0); // Set seconds to 0

    // Format the time as HH:mm:ss
    // Format the time as HH:mm:ss am/pm
    const formattedTime = currentTime.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit', hour12: true });

    return formattedTime;
}

/**
* Main method to run when the page contents have loaded.
*/
const main = async () => {
    console.log('Main method called');
    const addNotification = new AddNotification();
    addNotification.mount();
};
window.addEventListener('DOMContentLoaded', main);