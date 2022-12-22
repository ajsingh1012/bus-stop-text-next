# BusStopTextNext

If you:
- Live in a city with a transit system that lets you text a phone number with a bus stop number to see when the next bus will come; and
- Have a phone with the ability to text

then this is the application for you!

## Why I made this application

I made this application because I didn't want to use cellular data when I could use an already existing and fairly reliable texting service for bus times in my area.

## App Flow

To use this application, a user first needs to either log in, or sign up with their name, the phone number _of their local bus stop query system_, and an email/password combination. The email/password combination should be noted for reference; these are the login credentials for the user.

The user is brought to a main screen, where there are three tabs:
- The 'Home' tab: A user can select a bus stop from their saved list of bus stops, and text it with the bus stop number associated with that bus stop with the button at the bottom of the screen
- The 'Bus Stops' tab: The user can add a bus stop to their saved list with a description and the bus stop number (this bus stop number will be what is texted). The user can also delete a bus stop, by swiping right on the bus stop in the list.
- The 'Notifications' tab: The user can change the phone number to text for bus recommendations.

The user can log out at any time.

Note:
- The texts sent from this application are sent from the user's device/SIM/phone number. Spamming/ misuse is therefore the fault of the user. I do not take any responsibility for misuse of this application, or any consequences of user actions.
- Seeing the times returned by the bus service is through the phone's default messaging app (this application only sends the texts, but does not handle replies).

## Resources Used
- Android Studio
- Firebase Authentication, Firestore
- Android.Telephony