# Spotify Artist Search Sample App

<img width="200" alt="Captura de Pantalla" src="https://user-images.githubusercontent.com/10743855/100926235-dd3c9680-34c1-11eb-9f30-fda9d82097d1.png">

## About this project:

* Search app to find artists and their respectives albums in the spotify Api 
  https://developer.spotify.com/documentation/web-api/
* Background service syncs with google calendar to retrieve custom Events with the keyword "GOLDENTIFY" and notifies user with a notification.
  When the user click on that notification the app open with the query set on the calendar description.

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

* The project structure follows the Android Architecture guidelines and the principles of separation of concernes

<img width="146" alt="Project Structure" src="https://user-images.githubusercontent.com/10743855/100661688-92404900-3332-11eb-9337-04eb06ca3f97.png">

* Single Source of truth

It's common for different REST API endpoints to return the same data. For example, in the Spotify API if we geta list of artists, the same artist object could come from a different API endpoint, for example when you get albums
For this reason, our Repository implementation saves web service responses into the database. 
Changes to the database then trigger callbacks on active LiveData objects. Using this model, the database serves as the single source of truth, and other parts of the app access it using our Repository. 
So in our case the Data Base is the single source of truth and all the data shown in the UI came from the DB.

## Testing
One key benefit of the separation of concerns is testability. 
Also Dependency Injection makes the code easy to test, to reuse and maintain.

* ViewModel: Testing the SearchViewModel class using a JUnit test.

* ArtistRepository: 
  * The repository makes the correct web service calls.
  * The repository saves results into the database.
  
We have covered 100% of lines for the SearchViewModel and the ArtistRepository where most of the logic of the application happens.

<img width="337" alt="Captura de Pantalla 2020-11-30 a la(s) 12 09 13" src="https://user-images.githubusercontent.com/10743855/100671877-ef8ec700-333f-11eb-8f20-98d57aa7b0b9.png">

<img width="357" alt="Captura de Pantalla 2020-11-30 a la(s) 12 09 02" src="https://user-images.githubusercontent.com/10743855/100671884-f1588a80-333f-11eb-9179-807edf90f2e0.png">

## Calendar background work
In order to schedule tasks to sync with the Google Calendar events that match with "Goldentify" word that triggers a notification and a query in the app we use
* WorkManager: https://developer.android.com/topic/libraries/architecture/workmanager

WorkManager is an Android Jetpack library that runs deferrable, guaranteed background work when the work’s constraints are satisfied. It is the current best practice for this kind of work on Android.

WorkManager offers the following benefits:
  * Handles compatibility with different OS versions
  * Follows system health best practices
  * Supports asynchronous one-off and periodic tasks
  * Supports chained tasks with input/output
  * Lets you set constraints on when the task runs
  * Guarantees task execution, even if the app or device restarts
  
A Periodic Work Request is set every 15 minutes to check the calendar if a new event matching the requisites is created and generates an alarm with Alarm Manager that will trigger a notification that will open the app with query to be excuted.

## Spotify Autorization
In order to make authorized requests to the Spotify platform requires that you are granted permission to access data.
For that purpose we use the Android SDK Spotify https://developer.spotify.com/documentation/android/quick-start/

## TODO List
List of improvement to do:
* <s>Implement album tracks and filter for duration tracks</s>
* Create an infinite scroll list using Paging librarty: https://developer.android.com/topic/libraries/architecture/paging
* Details Screen for the artists
* UI Testing with Espresso library: https://developer.android.com/training/testing/espresso
* Improving UI code with Jetpack Compose library: https://developer.android.com/jetpack/compose

## Updates 12-02-2020
Album songs are displayed and a filter for searching albums
