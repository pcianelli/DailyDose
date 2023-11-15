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
        this.bindClassMethods(['mount', 'submit', 'redirectToHealthChart'], this);
        this.dataStore = new DataStore();
        this.dataStore.addChangeListener(this.redirectToHealthChart);
        this.header = new Header(this.dataStore);
    }

    /**
    * Add the header to the page and load the dailyDoseClient.
    */
    mount() {
        document.getElementById('add-medication-form').addEventListener('click', this.submit);
        this.header.addHeaderToPage();
        this.client = new DailyDoseClient()
    }
 }