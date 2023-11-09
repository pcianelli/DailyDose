import axios from "axios";
import BindingClass from "../util/bindingClass";
import Authenticator from "./authenticator";
/**
 * Client to call the DailyDoseService.
 */
export default class DailyDoseClient extends BindingClass {

    constructor(props = {}) {

        super();

        const methodsToBind = ['clientLoaded', 'getIdentity', 'login', 'logout', 'getMedications'];
        this.bindClassMethods(methodsToBind, this);

        this.authenticator = new Authenticator();
        this.props = props;

        axios.defaults.baseURL = process.env.API_BASE_URL;
        this.axiosClient = axios;
        this.clientLoaded();
    }

    /**
     * Run any functions that are supposed to be called once the client has loaded successfully.
     */
    clientLoaded() {
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady(this);
        }
    }

    /**
    * Get the identity of the current user
    * @param errorCallback (Optional) A function to execute if the call fails.
    * @returns The user information for the current user.
    */
    async getIdentity(errorCallback) {
        try {
            const isLoggedIn = await this.authenticator.isUserLoggedIn();

            if (!isLoggedIn) {
                return undefined;
            }

            return await this.authenticator.getCurrentUserInfo();
        } catch (error) {
            this.handleError(error, errorCallback)
        }
    }

    async login() {
        this.authenticator.login();
    }

    async logout() {
        this.authenticator.logout();
    }

    async getTokenOrThrow(unauthenticatedErrorMessage) {
        const isLoggedIn = await this.authenticator.isUserLoggedIn();
        if (!isLoggedIn) {
            throw new Error(unauthenticatedErrorMessage);
        }

        return await this.authenticator.getUserToken();
    }

    /**
    * Get medications.
    * @param errorCallback (Optional) A function to execute if the call fails.
    * @returns The list of medications.
    */
    async getMedications(parameter1, parameter2, errorCallback) {
        try {
            const queryParams = {customerId: parameter1, medName: parameter2}

            const response = await this.axiosClient.get('medications', {params: queryParams});

            console.log("Server Response:", response.data);

            const result = {
                medications: response.data.medicationList,
                currentCustomerId: parameter1,
                currentMedName: parameter2,
                nextCustomerId: response.data.id,
                nextMedName: response.data.name
            };
            return result;
        }
        catch (error) {
            this.handleError(error, errorCallback);
        }
    }

    /**
     * Helper method to log the error and run any error functions.
     * @param error The error received from the server.
     * @param errorCallback (Optional) A function to execute if the call fails.
     */
    handleError(error, errorCallback) {
        console.error(error);

        const errorFromApi = error?.response?.data?.error_message;
        if (errorFromApi) {
            console.error(errorFromApi)
            error.message = errorFromApi;
        }

        if (errorCallback) {
            errorCallback(error);
        }
    }
}