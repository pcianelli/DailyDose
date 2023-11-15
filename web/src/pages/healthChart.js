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
