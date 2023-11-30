import dailyDoseClient from '../api/dailyDoseClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';
import Authenticator from '../api/authenticator';
/**
 * Logic needed for the view notification banner and button to view health chart of the website index page.
 */

 class Index extends BindingClass {
    //Constructor
    constructor() {
        super();
        this.bindClassMethods(['clientNotificationLoad', 'displayNotifications', 'mount', 'showLoading', 'hideLoading', 'getCurrentTime'], this);
        this.client = new dailyDoseClient();
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.displayNotifications);
        this.authenticator = new Authenticator();
        this.header = new Header(this.dataStore);
        console.log("index constructor");
    }

    async mount() {

         const isLoggedIn = await this.authenticator.isUserLoggedIn();
             if (!isLoggedIn) {
                         // Redirect to login or create account
                         this.authenticator.login();
                         return;
                     }
         this.header.addHeaderToPage();
         this.clientNotificationLoad();
     }

    showLoading() {
        document.getElementById('notifications-loading').innerText = "(Loading notifications...)";
    }

    hideLoading() {
        document.getElementById('notifications-loading').style.display = 'none';
    }

    getCurrentTime() {
        const now = new Date();
        const hours = now.getHours().toString().padStart(2, '0');
        const minutes = now.getMinutes().toString().padStart(2, '0');
        const seconds = now.getSeconds().toString().padStart(2, '0');
        return `${hours}:${minutes}:${seconds}`;
        }


    /**
    * Once the client is loaded, get the notifications.
    */
    async clientNotificationLoad() {
        console.log("client Loaded called...");
        this.showLoading();

        const time = this.getCurrentTime();
        console.log("time :", time);

        const result = await this.client.getNotifications(time);
        this.hideLoading();
        console.log("Result in clientLoaded: ", result);

        this.dataStore.set('notification', result.notification)

    }


    // Display Notifications method to loop through each notification in the notificationList and display the vendor
    displayNotifications() {
        console.log("Displaying Notifications...");
        const notifications = this.dataStore.get('notification');
        console.log('Notifications:', notifications);

        const displayDiv = document.getElementById('notification-time-display');
        displayDiv.innerText = notifications.length > 0 ? "" : "No Medication To Take At This Time";

        for(let i = 0; i < notifications.length; i++) {
            const not = notifications[i];

            // Create HTML elements for each notification
            const notificationElement = document.createElement('div');
            notificationElement.classList.add('notification-card');

            const medNameElement = document.createElement('p');
            medNameElement.textContent = `MEDINE: ${not.medName}`;

            const timeElement = document.createElement('p');

            // Parse the notification time
            const parsedTime = new Date(`2000-01-01T${not.time}`);

            // Get hours and minutes
            const hours = parsedTime.getHours();
            const minutes = parsedTime.getMinutes();

            // Convert to AM/PM format
            const amPm = hours >= 12 ? 'PM' : 'AM';
            const displayHours = hours % 12 || 12; // Convert 0 to 12 for noon/midnight

            // Format the time as HH:mm AM/PM
            const formattedTime = `${displayHours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')} ${amPm}`;

            timeElement.textContent = `TIME: ${formattedTime}`;

            // Append elements to the notification card
            notificationElement.appendChild(medNameElement);
            notificationElement.appendChild(timeElement);

            // Append the notification card to the display div
            displayDiv.appendChild(notificationElement);
        }
    }
}

 /**
  * Main method to run when the page contents have loaded.
  */
  const main = async () => {
      const index = new Index();
      index.mount();
  };

  window.addEventListener('DOMContentLoaded', main);