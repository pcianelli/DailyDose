import DailyDoseClient from '../api/dailyDoseClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

class HealthChart extends BindingClass {
    constructor() {
        super();
        this.bindClassMethods(['mount', 'clientLoaded', 'displayMedications', 'showLoading', 'hideLoading'], this);
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
            medicationCard.className = 'medicationCard';

            const medicationName = document.createElement('h2');
            medicationName.innerHTML = "Medication Name: " + medication.medName;

            const medicationInfo = document.createElement('h3');
            medicationInfo.innerHTML = "Medication Info: " + medication.medInfo;

            medicationCard.appendChild(medicationName);
            medicationCard.appendChild(medicationInfo);

            // convert Set<notificationModel> to array so I can use for loop
            const notifications = Array.from(medication.notificationTimes || []);
            console.log('Notifications:', notifications);


            notifications.forEach(notification => {
            // Extract only the 'time' field
                const time = notification.time;
                const timeElement = document.createElement('h4');
                timeElement.innerHTML = `Alarm Time: ${time}`;
                medicationCard.appendChild(timeElement);
            });

            displayDiv.appendChild(medicationCard);
        });
    }
}

const main = async () => {
    const healthCart = new HealthChart();
    healthCart.mount();
};

window.addEventListener('DOMContentLoaded', main);
