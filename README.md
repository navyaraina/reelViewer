# ReelView App

ReelView is a video-sharing Android application where users can record, upload, view, and interact with short videos. Built with Kotlin, Firebase Storage, and ExoPlayer, this app enables endless scrolling and allows for liking, sharing, and downloading videos.

## Table of Contents
- [Features](#features)
- [Installation](#installation)
  - [For Users](#for-users)
  - [For Developers](#for-developers)
- [Usage](#usage)
  - [User Guide](#user-guide)
  - [Developer Guide](#developer-guide)
- [Dependencies](#dependencies)

## Features
- Record and upload videos from the camera.
- Store videos in Firebase Storage.
- Endless video scrolling with auto-pause on scroll.
- Like, share, and download videos.
- Dynamic video playback with ExoPlayer.

## Installation

### For Users
1. Download the latest APK-currently in debug mode.
2. Install the APK on your Android device.
3. Open the app and allow camera and storage permissions for recording and viewing videos.

### For Developers
To run this app locally or contribute to its development:

1. **Clone the Repository**
    ```bash
    git clone https://github.com/yourusername/reelview.git
    cd reelview
    ```

2. **Set Up Firebase**
    - Create a Firebase project at [Firebase Console](https://console.firebase.google.com/).
    - Add an Android app with your package name, and download the `google-services.json` file.
    - Place `google-services.json` in the `app/` directory.
    - Enable Firebase Storage in your Firebase console.

3. **Open in Android Studio**
    - Open Android Studio and select “Open an existing project.”
    - Choose the `reelview` directory.

4. **Run the Project**
    - Connect an Android device or use an emulator.
    - Run the app from Android Studio.

## Usage

### User Guide

#### Recording a Video
- Tap on the upload button to open the camera.
- Record a short video.
- After recording, the video will automatically upload to Firebase Storage.

#### Viewing Videos
- The app will display a continuous scroll of videos.
- Tap on any video to play, and swipe up to see the next one.

#### Interacting with Videos
- **Like:** Tap the heart icon to like a video.
- **Share:** Tap the share icon to share the video link with others.
- **Download:** Tap the download icon to save the video locally.

### Developer Guide

#### Adding New Features
- The app currently uses a `VideoFragment.kt` file to handle video interactions.
- ExoPlayer manages playback; refer to the `playNextVideo` and `uploadVideo` functions to add new playback behaviors.

#### Modifying UI
- The main layout for video viewing is in `videoview.xml`.
- Customize or add UI components here and modify `VideoFragment.kt` to handle new interactions.

#### Firebase Storage Structure
- Videos are stored in Firebase Storage under the `videos/` directory.
- The app generates random filenames for video uploads. Ensure these filenames are unique to avoid overwriting files.

#### Debugging Tips
- **Playback issues:** Check that the `videoUris` list is populated correctly and verify Firebase Storage rules.
- **Uploading issues:** Ensure Firebase Storage permissions are set to allow authenticated reads/writes.

## Dependencies
- **ExoPlayer** - for video playback.
- **Firebase Storage** - for cloud video storage.
- **AndroidX libraries** - for Android app components.