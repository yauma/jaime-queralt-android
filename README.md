# Spotify Artist Search Sample App

## About this project:

* Search app to find artists and their respectives albums in the spotify Api 
  https://developer.spotify.com/documentation/web-api/
* Background service syncs with google calendar to retrieve custom Events with the keyword "GOLDENTIFY" and notifies user with a notification.
When the user click on that notification the app open with the query set on the calendar description.
* TODO implement album tracks and filter for duration tracks

## Libraries used in this application
* Kotlin Coroutines: for concurrency https://developer.android.com/kotlin/coroutines
* Android JetPack Components https://developer.android.com/jetpack
  * WorkManager for background scheduling needs.
  * Room for data storage persistence.
  * Dependency Injection using Hilt Improve app scalability and testing with simplified dependency injection

* Retrofit: for HTTP client https://square.github.io/retrofit/
* Glide: for image download https://github.com/bumptech/glide
* Mockito: for Unit Testing https://site.mockito.org

## Architecture
This project implements the Android recommended architecture using Architecture Components 
https://developer.android.com/topic/libraries/architecture
* The following diagram shows how all the modules should interact with one another after designing the app:

<img width="500" alt="Architecture Diagram" src="https://user-images.githubusercontent.com/10743855/100661226-e7c82600-3331-11eb-8b4f-801b8c449528.png">

* The project structure follows the Android Architecture guidelines

<img width="146" alt="Project Structure" src="https://user-images.githubusercontent.com/10743855/100661688-92404900-3332-11eb-9337-04eb06ca3f97.png">
