import DailyDoseClient from '../api/dailyDoseClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
* Logic needed for the update medication info page of the website.
*/
class UpdateMedicationInfo extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'submit', 'populateMedicationDropdown', 'showSuccessMessageAndRedirect', 'showFailMessageRedirect'], this);
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

        await this.populateMedicationDropdown();

        document.getElementById('update-medication-info-form').addEventListener('submit', this.submit);
    }

    /**
     * Helper method to populate the medication dropdown.
     */
    async populateMedicationDropdown() {
        const medNameDropdown = document.getElementById('medName');
        try {
            const medicationsResponse = await this.client.getMedications();
            console.log('Medications:', medicationsResponse);

            const medicationsArray = Array.isArray(medicationsResponse.medications)
                ? medicationsResponse.medications
                : [];
            console.log('Medications:', medicationsArray);

            medicationsArray.forEach((medication) => {
                const option = document.createElement('option');
                option.value = medication.medName;
                option.text = medication.medName;
                medNameDropdown.add(option);
            });
        } catch (error) {
            console.error('Error fetching medications:', error);
        }
    }

    /**
    * Method to run when the add medication form is submitted.
    * Calls the DailyDoseService to add the medication.
    */
    async submit(event) {
        event.preventDefault();

        const medName = document.getElementById('medName').value;
        const medInfo = document.getElementById('medInfo').value;

        try {
            const updateMedicationInfo = await this.client.updateMedicationInfo(medName, medInfo);
            this.showSuccessMessageAndRedirect();
        } catch (error) {
            console.error("Error adding medication: ", error);
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
        messageText.innerText = "Medication info has been update in your health chart successfully!";
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

            // Create success message with card class
        const messageElement = document.createElement('div');
        messageElement.className = 'card';  // Add the card class
        const messageText = document.createElement('p');
        messageText.innerText = "Error occurred trying to update your Medication info! Try Again";
        messageText.style.textAlign = "center";
        messageText.style.color = "#FFFFFF";
        messageText.style.fontSize = "40px";
        messageText.style.margin = "20px 0";
        messageElement.appendChild(messageText);
        document.body.appendChild(messageElement);

        setTimeout(() => {
            window.location.href = `/updateMedicationInfo.html`;
        }, 4000);
    }
}

/**
* Main method to run when the page contents have loaded.
*/
const main = async () => {
    const updateMedicationInfo = new UpdateMedicationInfo();
    updateMedicationInfo.mount();
};
window.addEventListener('DOMContentLoaded', main);