import DailyDoseClient from '../api/dailyDoseClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
* Logic needed for the remove medication page of the website.
*/
class RemoveMedication extends BindingClass {
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

         document.getElementById('remove-medication-form').addEventListener('submit', (event) => this.submit(event));
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

        const userConfirmed = window.confirm('Are you sure you want to remove this Medication?');

        if (!userConfirmed) {
            // User canceled the operation, do nothing
            return;
        }

        const medName = document.getElementById('medName').value;

        try {
            const removeMedication = await this.client.removeMedication(medName);
            this.showSuccessMessageAndRedirect();
        }
        catch (error) {
            console.error("Error removing medication: ", error);
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
        messageText.innerText = "Medication has been removed from your health chart successfully!";
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
        messageText.innerText = "Error occurred trying to remove Medication! Try Again";
        messageText.style.textAlign = "center";
        messageText.style.color = "#FFFFFF";
        messageText.style.fontSize = "40px";
        messageText.style.margin = "20px 0";
        messageElement.appendChild(messageText);
        document.body.appendChild(messageElement);

        setTimeout(() => {
            window.location.href = `/removeMedication.html`;
        }, 4000);
    }
}
/**
* Main method to run when the page contents have loaded.
*/
const main = async () => {
    const removeMedication = new RemoveMedication();
    removeMedication.mount();
};
window.addEventListener('DOMContentLoaded', main);