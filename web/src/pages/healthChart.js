import VendorEventClient from '../api/dailyDoseClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

class HealthChart extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'clientLoaded', 'next', 'previous', 'displayMedications', 'showLoading', 'hideLoading']);
        this.dataStore = new DataStore();
        this.header = new Header(this.dataStore);
        this.client = new DailyDoseClient();
        this.dataStore.addChangeListener(this.displayMedications);
        this.previousKeys = [];
        console.log("viewAllVendors constructor");
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
        this.showLoading();
        const result = await this.client.getMedications();
        this.hideLoading();
        console.log("Result:", result);
        const medications = result.medications;

        this.previousKeys.push({customerId: result.currentCustomerId, medName: result.currentMedName});

        this.dataStore.set('medications', medications);
        this.dataStore.set('previousId', result.currentCustomerId);
        this.dataStore.set('previousName', result.currentMedName);
        this.dataStore.set('nextCustomerId', result.nextCustomerId);
        this.dataStore.set('nextMedName', result.nextMedName);
        this.displayMedications();
    }

    /**
    * Add the header to the page and load the VendorEventClient.
    */
    mount() {
        this.header.addHeaderToPage();
        this.clientLoaded();
        document.getElementById('nextButton').addEventListener('click', this.next);
        document.getElementById('prevButton').addEventListener('click', this.previous);
    }

    async next() {
        this.showLoading();
        // To make the it stop at the end rather than looping around
        if (this.dataStore.get('medications') == 0) {
            this.displayMedications();
        }
        else {
        const nextId = this.dataStore.get('nextCustomerId');
        const nextName = this.dataStore.get('nextMedName');

        const result = await this.client.getMedications(nextCustomerId, nextMedName);
        console.log("Result:", result);
        const medications = result.medications;
        console.log("Received medications:", medications);

        this.previousKeys.push({customerId: this.dataStore.get('previousId'), medName: this.dataStore.get('previousName')});

        this.dataStore.set('medications', medications);
        this.dataStore.set('previousId', result.currentCustomerId);
        this.dataStore.set('previousName', result.currentMedName);
        this.dataStore.set('nextCustomerId', result.nextCustomerId);
        this.dataStore.set('nextMedName', result.nextMedName);
        this.hideLoading();
        }
    }

    async previous() {
        this.showLoading();

        let result;
        if (this.previousKeys.length > 0) {
            const previousRequest = this.previousKeys.pop();
            result = await this.client.getMedications(previousRequest.customerId, previousRequest.medName);
        }
        else {
            result = await this.client.getMedications(this.dataStore.get('previousId'), this.dataStore.get('previousName'));
        }

        console.log("Result:", result);
        const medications = result.medications;
        console.log("Received medications:", medications);

        this.dataStore.set('medications', medications);
        this.dataStore.set('previousId', result.currentCustomerId);
        this.dataStore.set('previousName', result.currentMedName);
        this.dataStore.set('nextCustomerId', result.nextCustomerId);
        this.dataStore.set('nextMedName', result.nextMedName);
        this.hideLoading();
    }

    displayMedications() {
        const medications = this.dataStore.get('medications');
        const displayDiv = document.getElementById('medication-list-display');
        displayDiv.innerText = medications.length > 0 ? "" : "No more Medications available.";

        medications.forEach(medication => {
            const medicationCard = document.createElement('section');
            medicationCard.className = 'medicationCard';

            const customerId = encodeURIComponent(medication.customerId);
            const medName = encodeURIComponent(medication.medName);
            const medInfo = encodeURIComponent(medication.medInfo);
            const notifications = encodeURIComponent(medication.notifications);

            const currentHostname = window.location.hostname;

//            const isLocal = currentHostname === 'localhost' || currentHostname === '127.0.0.1';
//            const baseUrl = isLocal ? 'http://localhost:8000/' : 'https://d3hqn9u6ae71hc.cloudfront.net/';

//            const vendorPageUrl = `${baseUrl}viewVendor.html?id=${customerId}&name=${encodeURIComponent(medName)}`;

            const medicationName = document.createElement('h2');
            medicationName.innerText = medication.medName;

            const medicationInfo = document.createElement('h3');
            medicationInfo.innerText = medication.medInfo;

            const medNotifications = document.createElement('h4');
            medNotifications.innerText = medication.notifications;

            vendorCard.appendChild(medicationName);
            vendorCard.appendChild(medicationInfo);
            vendorCard.appendChild(medNotifications);

            displayDiv.appendChild(medicationCard);
        });
    }
}

const main = async () => {
    const healthCart = new HealthChart();
    healthCart.mount();
};

window.addEventListener('DOMContentLoaded', main);
