import dailyDoseClient from '../api/dailyDoseClient';
import Header from '../components/header';
import BindingClass from '../util/bindingClass';
import DataStore from '../util/DataStore';

/**
 * Logic needed login or create account, and returns to index page
 */

 class LoginOrCreateAccount extends BindingClass {
    //Constructor
    constructor {
        super();
        this.bindClassMethods(['mount'], this);
        this.client = new dailyDoseClient();
        this.header = new Header(this.dataStore);
        console.log("Login or Create Account constructor");
    }

    mount() {
        this.header.addHeaderToPage();
        }
 /**
   * Main method to run when the page contents have loaded.
   */
   const main = async () => {
       const loginOrCreateAccount = new LoginOrCreateAccount();
       loginOrCreateAccount.mount();
   };

   window.addEventListener('DOMContentLoaded', main);
 }