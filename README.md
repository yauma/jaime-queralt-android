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
  * WorkManager background scheduling needs.
  * Room for data storage persistence.
  * Dependency Injection using Hilt Improve app scalability and testing with simplified dependency injection

* Retrofit: for HTTP client https://square.github.io/retrofit/
* Glide: for image download https://github.com/bumptech/glide
* Mockito: for Unit Testing https://site.mockito.org
