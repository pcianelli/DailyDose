import DailyDoseClient from '../api/dailyDoseClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
* Logic needed for the add medication page of the website.
*/
class AddMedication extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'submit', 'showSuccessMessageAndRedirect'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.redirectToHealthChart);
        this.header = new Header(this.dataStore);
    }

/**
    * Add the header to the page and load the dailyDoseClient.
    */
    async mount() {
        await this.header.addHeaderToPage();
        this.client = new DailyDoseClient();
        document.getElementById('add-medication-form').addEventListener('submit', this.submit);
    }

    /**
    * Method to run when the add medication form is submitted.
    * Calls the DailyDoseService to add the medication.
    */
    async submit(event) {
        event.preventDefault();

        const medName = document.getElementById('medName').value;
        const medInfo = document.getElementById('medInfo').value;

        const medicationDetails = {
            medName: medName,
            medInfo: medInfo
        };

        try {
            const addMedication = await this.client.addMedication(medicationDetails);
            this.showSuccessMessageAndRedirect();
        } catch (error) {
            console.error("Error adding medication: ", error);
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
        messageText.innerText = "Medication has been added to your health chart successfully!";
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
* Main method to run when the page contents have loaded.
*/
const main = async () => {
    const addMedication = new AddMedication();
    addMedication.mount();
};
window.addEventListener('DOMContentLoaded', main);