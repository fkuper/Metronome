# Metronome

This project implements a native metronome application for Android using Jetpack Compose.

It was created as part of the [Mobile Platforms](https://www.oru.se/english/study/exchange-studies/courses-for-exchange-students/course/mobile-platforms-dt521a) course at the [Örebro Univerity](https://www.oru.se/english/).

## Building the app

Since the application makes use of the [Spotify Web API](https://developer.spotify.com/documentation/web-api), before you can build the app, you will need to specify your own API secret and client ID. 
To find out how you get this information please refer to the Spotify Web API documentation.

Once you have your API secret and client ID, you can specify it for the build by copying the template file in the root directory of the project and removing the `.template` file ending:
```bash
cp apikey.properties.template ./apikey.properties
```
In the file itself you simply fill in the missing information and you are good to go and build the application.
