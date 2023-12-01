# DailyDose


## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Documentation](#documentation)
- [Acknowledgments](#acknowledgments)


## Introduction
The DailyDose application is a revolutionary personalized medication management solution. 

This application empowers users to effortlessly schedule medication alarms, access crucial medication details, and comprehensively review their health chart encompassing all prescribed medications. 

## Features
- Add medications to your health Chart with an area to add medication information and notes you may want to keep.
- Users can set daily alarms for a medication which will send the user a daily notification in your homepage of the application at the time set to take the medication.
- Users can remove those alarms at anytime.
- Users can update the information on their medications, and remove a medication from their health chart when they are no longer taking the medication.
- Users can click on their medications and see information about that medication provided by a third party api.


## Getting Started
Create a user account with your email and a password. Amazons cognito services handles the management of the users account. Logout of the application when you are finished. 

## Usage
After the user has logged in, they are brought to the homepage where any set alarms will be displayed to notify the user when the alarm goes off.

The homepage provides a link to view their health chart. The header of every page provides a link to go back to the home screen at any point or login/logout. 

Once a user is viewing the health chart, the menu button provides a modal with links to add medications to your health chart, remove medications from their health chart, update the medication info, and add alarms for a medication. There is a "remove alarm" button on the users health chart to remove a given alarm from the medication. 

Lastly, within the users health chart, they can click on a medication and view information about that medication. 

## Documentation
view more details about the design at https://github.com/pcianelli/DailyDose/blob/main/resources/design-document.md

Third-Party api used for medication information: 

## Acknowledgments
I would like to acknowledge my mother who inspired me to develop the app due to a medical condition, she needs to take many medications and was looking for a place to help her organize her medications and alarms. 

I would like to acknowledge my teacher and senior engineer Jean Soderkvist, who helped me develop my app and gave much needed advice throughout my development. 

Lastly, I would like to thank all my classmates at the Nashville Software School for their support!