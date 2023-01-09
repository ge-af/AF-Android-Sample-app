# AF-Android-Sample-app
Sample app to demonstrate the features in the AppsFlyer Android SDK.

This app is updated according to latest date of 8/1/2023.

Android SDK usage is 6.+ (Currently 6.9.4)

Sample app packageName is **com.appsflyer.sdk.support.demo**.


# Basic Integration instructions


![Screen Shot 2023-01-09 at 11 49 21](https://user-images.githubusercontent.com/87754256/211280710-44d57be6-55bf-49a5-9697-8ca4a421d0f6.png)

All configurations in the Edit texts fields and in the Checkboxes are stored in Shared Preferences and will be saved for later app launches.

When the application is first being launched, the SDK won't start yet as it is in **deferred mode** *by default*.
This can be changed by switch the **"Deferred Start"** toggle and re-launch the application.

The default state is to use deferred start and use UDL instead of GCD for Deferred Deep Linking (and eventually also Direct deep linking).


# In-App Events Testing

![Screen Shot 2023-01-08 at 17 47 53](https://user-images.githubusercontent.com/87754256/211205960-fcb40b52-c27b-4234-a5d5-cad1e3fcca00.png)

You can choose between 3 different scenarios:
1. **Regular** hard-coded In-App Event
2. **Purchase** hard-coded In App Event
3. **Custom** In-app event where the Event name and event values are dynamic *(Event values should be inputted as a valid JSON)*.
