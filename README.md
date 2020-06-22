# GitDroid
![](https://raw.githubusercontent.com/marcc992/GitDroid/master/app/src/main/res/drawable/ic_main_logo.png)

GitDroid is an Android application for show all the Public GitHub repositories.

**The main goal of this application is not its behavior or appearance but its architecture**

## Features

- It shows the list of the public GitHub repositories by pages.
- When the user reaches the bottom of the list, a new page of repositories if loaded.
- Also the user can find repositories by name in this list screen
- Clicking on any repository, the user can see the details of it.
- On each repository detail appears the owner in the top of the screen.



## Technology
- MVP Design pattern
- Dependency injection with Dagger 2

```java
implementation "com.google.dagger:dagger:$daggerVersion"
annotationProcessor "com.google.dagger:dagger-compiler:$daggerVersion"
```
- Reactive programing with RxJava / RxAndroid
```java
implementation "io.reactivex.rxjava2:rxjava:2.2.0"
implementation 'io.reactivex.rxjava2:rxandroid:2.1.0'
implementation "com.squareup.retrofit2:adapter-rxjava2:$retrofitVersion"
```
- Unit Test with JUnit4 and Mockito 3
```java
testImplementation 'junit:junit:4.12'
testImplementation 'org.mockito:mockito-core:3.3.3'
```
