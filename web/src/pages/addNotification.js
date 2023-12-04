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
        console.log("constructor called before binding class")
        this.bindClassMethods(['mount', 'submit', 'showSuccessMessageAndRedirect', 'showFailMessageRedirect'], this);
        console.log("after binding class")
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.redirectToHealthChart);
        this.header = new Header(this.dataStore);
        console.log("addNotification constructor");
    }

    /**
    * Add the header to the page and load the dailyDoseClient.
    */
    async mount() {
        await this.header.addHeaderToPage();
        this.client = new DailyDoseClient();
        document.getElementById('add-notification-form').addEventListener('submit', this.submit);
    }

    /**
    * Add the header to the page and load the dailyDoseClient.
    */
    async submit(event) {
        event.preventDefault();

        const medName = document.getElementById('medName').value;
        const timeInput = document.getElementById('time');
        // Find the 'period' element using querySelector
        const periodSelect = document.querySelector('#period');

        // Check if elements are found before accessing their values
        if (!medName || !timeInput || !periodSelect) {
            console.error('Required elements not found.');
            return;
        }

        // Get the selected option from the period select element
        const period = periodSelect.options[periodSelect.selectedIndex].value;

        const time = formatTime(timeInput.value, period);

        try {
            console.log('Before trying to add notification');
            const addNotification = await this.client.addNotification(medName, time);
            console.log('Notification added successfully');
            this.showSuccessMessageAndRedirect();
        } catch (error) {
            console.log('catch block called');
            console.error('Error adding notification: ', error.message);
            this.showFailMessageRedirect();

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
        messageText.innerText = "Alarm has been added to your health chart successfully!";
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

    showFailMessageRedirect() {
    // Hide everything except the header and body background
        const allChildren = document.body.children;

        for (let i = 0; i < allChildren.length; i++) {
            const element = allChildren[i];
            if (element.id !== 'header') {
                element.style.display = 'none';
            }
        }

        const messageElement = document.createElement('div');
        messageElement.className = 'card';  // Add the card class
        const messageText = document.createElement('p');
        messageText.innerText = "Error occurred trying to add Alarm! Try Again";
        messageText.style.textAlign = "center";
        messageText.style.color = "#FFFFFF";
        messageText.style.fontSize = "40px";
        messageText.style.margin = "20px 0";
        messageElement.appendChild(messageText);
        document.body.appendChild(messageElement);

        setTimeout(() => {
            window.location.href = `/addNotification.html`;
        }, 4000);  // redirect after 4 seconds
    }

}

/**
 * Format time to ensure it's in HH:mm:ss format.
 * @param {string} inputTime - The time input from the user.
 * @param {string} period - The period (am/pm) selected by the user.
 * @returns {string} - Formatted time in HH:mm:ss format.
 */
function formatTime(inputTime, period) {
    // Assuming the inputTime is in HH:mm format
    const [hours, minutes] = inputTime.split(':');

    // Convert hours to 24-hour format
    let convertedHours = parseInt(hours, 10);

    // Adjust for am/pm
    if (period.toLowerCase() === 'pm' && convertedHours < 12) {
        convertedHours += 12;
    } else if (period.toLowerCase() === 'am' && convertedHours === 12) {
        convertedHours = 0;
    }

    // Format the time as HH:mm:ss
    const formattedTime = `${String(convertedHours).padStart(2, '0')}:${minutes}:00`;

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