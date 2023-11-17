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
        this.bindClassMethods(['clientNotificationLoad', 'displayNotifications', 'mount'], this);
        this.client = new dailyDoseClient();
        this.dataStore = new DataStore();
        this.authenticator = new Authenticator();
        this.header = new Header(this.dataStore);
        console.log("index constructor");
    }

    /**
    * Once the client is loaded, get the notifications.
    */

    async clientNotificationLoad() {
    }

     // Display Notifications method to loop through each notification in the notificationList and display the vendor
    displayNotifications() {
        const notifications = this.dataStore.get('notifications');
        const arrayNotifications = Array.from(notifications);
        const displayDiv = document.getElementById('notification-time-display');
        displayDiv.innerText = notifications.length > 0 ? "" : "No Alarms.";

        for(let i = 0; i < arrayNotifications.length; i++) {

        }
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
 }

 /**
  * Main method to run when the page contents have loaded.
  */
  const main = async () => {
      const index = new Index();
      index.mount();
  };

  window.addEventListener('DOMContentLoaded', main);