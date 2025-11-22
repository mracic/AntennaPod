# AntennaPod - Build and Installation Instructions

This document contains instructions for building and installing the modified AntennaPod application with the new features:

1. **Episode Favorite Functionality** (already existed, enhanced documentation)
2. **Auto-delete episodes not favorited within 7 days** ⭐ NEW
3. **Auto-download for subscribed podcasts** (already existed, enhanced documentation)
4. **Favorite toggle in Android Auto** ⭐ NEW

## Prerequisites

Before building, ensure you have the following installed:

- **Java Development Kit (JDK)**: Version 17 or higher
  - Download from: https://adoptium.net/ or https://www.oracle.com/java/technologies/downloads/
  - Verify installation: `java -version` and `javac -version`

- **Android SDK**: Install via Android Studio or command-line tools
  - Android Studio (recommended): https://developer.android.com/studio
  - Command-line tools only: https://developer.android.com/studio#command-tools
  - Minimum SDK version required: API 21 (Android 5.0)
  - Recommended target SDK: API 34 (Android 14)

- **Git**: For cloning the repository
  - Download from: https://git-scm.com/downloads
  - Verify installation: `git --version`

## Step 1: Clone the Repository

```bash
git clone https://github.com/mracic/AntennaPod.git
cd AntennaPod
git checkout claude/podcast-favorites-auto-delete-01Mhsb28q23dkcZtfZrTwpW3
```

## Step 2: Configure Android SDK Path

### Option A: Using Android Studio
1. Open the project in Android Studio
2. Android Studio will automatically configure the SDK path
3. Accept any SDK installation prompts

### Option B: Manual Configuration
Create a `local.properties` file in the project root directory:

```bash
# For Linux/macOS:
echo "sdk.dir=/path/to/your/Android/Sdk" > local.properties

# For Windows:
echo "sdk.dir=C:\\Users\\YourUsername\\AppData\\Local\\Android\\Sdk" > local.properties
```

Replace `/path/to/your/Android/Sdk` with your actual Android SDK path.

**Common SDK locations:**
- **Linux**: `~/Android/Sdk`
- **macOS**: `~/Library/Android/sdk`
- **Windows**: `C:\Users\YourUsername\AppData\Local\Android\Sdk`

## Step 3: Build the APK

### Option A: Using Android Studio (Recommended for beginners)

1. Open the project in Android Studio
2. Wait for Gradle sync to complete (may take several minutes on first run)
3. Go to **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
4. Wait for the build to complete
5. Click on the notification to locate the APK file

The APK will be located at:
```
app/build/outputs/apk/debug/app-debug.apk
```

### Option B: Using Command Line (Gradle)

```bash
# On Linux/macOS:
./gradlew assembleDebug

# On Windows:
gradlew.bat assembleDebug
```

**Build options:**
- **Debug APK** (for testing): `./gradlew assembleDebug`
- **Release APK** (optimized, requires signing): `./gradlew assembleRelease`

The debug APK will be located at:
```
app/build/outputs/apk/debug/app-debug.apk
```

**Build time:** First build may take 5-15 minutes depending on your system. Subsequent builds will be faster.

## Step 4: Install on Android Device

### Prerequisites for Installation

1. **Enable Developer Options** on your Android device:
   - Go to **Settings** → **About phone**
   - Tap **Build number** 7 times
   - You'll see a message "You are now a developer!"

2. **Enable USB Debugging**:
   - Go to **Settings** → **System** → **Developer options**
   - Enable **USB debugging**

3. **Enable Install from Unknown Sources** (for direct APK installation):
   - Go to **Settings** → **Security** or **Settings** → **Apps**
   - Enable **Install unknown apps** or **Unknown sources**
   - Select your file manager or browser

### Installation Method 1: Using ADB (Android Debug Bridge)

**Install ADB:**
- Included with Android SDK at `platform-tools/adb`
- Or download standalone: https://developer.android.com/tools/releases/platform-tools

**Install the APK:**
```bash
# Connect your device via USB
# Ensure USB debugging is enabled and authorize the computer on your device

# Install the APK
adb install app/build/outputs/apk/debug/app-debug.apk

# If you have multiple devices connected:
adb devices  # List devices
adb -s DEVICE_ID install app/build/outputs/apk/debug/app-debug.apk
```

### Installation Method 2: Direct APK Installation

1. **Transfer the APK to your device:**
   - Connect device via USB and copy `app-debug.apk` to Downloads folder
   - Or email the APK to yourself
   - Or use cloud storage (Google Drive, Dropbox, etc.)

2. **Install from device:**
   - Open a File Manager app on your device
   - Navigate to the APK location (usually Downloads)
   - Tap on `app-debug.apk`
   - Tap **Install**
   - Tap **Open** to launch the app

### Installation Method 3: Using Android Studio

1. Connect your Android device via USB
2. In Android Studio, select your device from the device dropdown
3. Click **Run** → **Run 'app'** (or press Shift+F10)
4. The app will be built and installed automatically

## Step 5: Using the New Features

### Feature 1: Favoriting Episodes

**How to favorite an episode:**
1. Open AntennaPod
2. Navigate to any podcast episode
3. **Long-press** on an episode or open episode details
4. Tap the **Star icon** or select **Mark as favorite** from the menu
5. The episode is now favorited and will be protected from auto-deletion

**Viewing favorites:**
- Tap the menu icon (≡) and select **Favorites** to see all favorited episodes

### Feature 2: Auto-Delete Episodes Not Favorited Within 7 Days

**How to enable 7-day auto-deletion:**

1. Open AntennaPod
2. Tap the menu icon (≡) → **Settings**
3. Select **Storage** → **Automatic download**
4. Enable **Auto delete** toggle
5. Tap **Delete before auto download**
6. Select **"After 7 days if not favorited"** from the list
7. Tap **OK** to save

**How it works:**
- Episodes downloaded more than 7 days ago will be automatically deleted
- **EXCEPTION**: Episodes marked as favorite are NEVER deleted
- Deletion happens automatically when new episodes are downloaded and space is needed
- Or during periodic cleanup (every 3 days)

**Important notes:**
- The 7-day countdown starts from the **download date**, not the publication date
- Favoriting an episode at any time will protect it from deletion permanently
- You can change this setting at any time

### Feature 3: Auto-Download for Subscribed Podcasts

**How to enable auto-download globally:**

1. Open AntennaPod
2. Tap menu (≡) → **Settings** → **Storage** → **Automatic download**
3. Enable **Enable Auto Download** toggle
4. Configure options:
   - **Auto Download on Battery**: Allow downloads when not charging
   - **Episode Cache**: Set maximum number of downloaded episodes (25, 50, 100, etc.)

**How to enable auto-download per podcast:**

1. Open AntennaPod
2. Navigate to a podcast
3. Tap the **Settings icon** (gear) for that podcast
4. Select **Auto Download**
5. Choose:
   - **Global** (use global setting)
   - **Enabled** (always auto-download this podcast)
   - **Disabled** (never auto-download this podcast)

**Advanced options:**
- **Episode Filter**: Set rules to only download episodes matching certain criteria (title contains, duration, etc.)
- **Auto Download Queue**: Enable to auto-download episodes added to the queue

### Feature 4: Favorite Toggle in Android Auto ⭐ NEW

**What is this feature:**
When using AntennaPod with Android Auto in your car, you can now favorite episodes directly from the Android Auto interface without touching your phone. The favorite button appears alongside other playback controls.

**How to use:**

1. **Connect to Android Auto:**
   - Connect your phone to your car via USB cable or wireless Android Auto
   - Your car's display should show the Android Auto interface

2. **Access AntennaPod:**
   - On the Android Auto home screen, select **AntennaPod**
   - Or tap the media icon and select AntennaPod from available apps

3. **Play an episode:**
   - Browse your podcasts and select an episode to play
   - The episode will start playing on your car's speakers

4. **Toggle Favorite:**
   - While an episode is playing, look for the custom action buttons around the play/pause button
   - You'll see a **star icon** (⭐ or ☆)
   - **Filled star** (⭐) = Episode is already favorited
   - **Outlined star** (☆) = Episode is not favorited
   - Tap the star icon to toggle the favorite status
   - The icon will update immediately to reflect the new state

**Where the button appears:**
- On Android Auto, custom actions appear around the play button
- The favorite toggle appears in the "additional actions" area or carousel
- Position: Usually near left, near right, far left, far right, or in additional actions panel

**Benefits:**
- Mark episodes as favorites while driving (hands-free, eyes on road)
- Protected episodes won't be auto-deleted even after 7 days
- No need to pull over to mark important episodes
- Syncs immediately with the main app

**Important notes:**
- The favorite button only appears when playing a podcast episode (not music or other audio)
- Some car displays may show the button differently depending on screen size
- The feature works on all Android Auto compatible vehicles
- Also works with Android Auto on phone (standalone mode)

**Testing without a car:**
You can test Android Auto features without a car:
1. Install **Android Auto** app from Google Play Store
2. Open the app on your phone (Developer mode may need to be enabled)
3. Or use Android Auto in Android Emulator

## Troubleshooting

### Build Issues

**Problem: "SDK location not found"**
- Solution: Create `local.properties` file with your SDK path (see Step 2)

**Problem: "Gradle sync failed"**
- Solution:
  - Check internet connection
  - Try running: `./gradlew clean`
  - Restart Android Studio

**Problem: "Java version incompatible"**
- Solution: Install JDK 17 or higher and set JAVA_HOME environment variable

### Installation Issues

**Problem: "App not installed"**
- Solution 1: Uninstall any existing AntennaPod version first
- Solution 2: Enable "Install from unknown sources" in settings
- Solution 3: Check available storage space (need at least 100MB free)

**Problem: "adb: device not found"**
- Solution:
  - Enable USB debugging on device
  - Reconnect USB cable
  - Try different USB port or cable
  - Install device drivers (Windows only)

**Problem: "Installation blocked"**
- Solution: Go to Settings → Security → Install unknown apps → Enable for your file manager

### Runtime Issues

**Problem: "Auto-delete not working"**
- Solution:
  - Ensure Auto Delete is enabled in Settings
  - Check that cleanup algorithm is set to "After 7 days if not favorited"
  - Wait for cleanup to trigger (happens during auto-download or every 3 days)

**Problem: "Favorites disappeared"**
- Solution: This shouldn't happen. Check Settings → Storage → Delete before auto download
- Ensure "Favorite Keeps Episode" is enabled

## Additional Resources

- **AntennaPod Documentation**: https://antennapod.org/documentation/
- **AntennaPod Forum**: https://forum.antennapod.org/
- **Issue Tracker**: https://github.com/AntennaPod/AntennaPod/issues
- **Android Developer Guide**: https://developer.android.com/guide

## Technical Details

### Modified Files

The following files were modified to implement the new features:

1. **SevenDayAutoDeleteCleanupAlgorithm.java** (NEW)
   - `net/download/service/src/main/java/de/danoeh/antennapod/net/download/service/episode/autodownload/SevenDayAutoDeleteCleanupAlgorithm.java`
   - Implements the 7-day auto-deletion logic

2. **UserPreferences.java**
   - `storage/preferences/src/main/java/de/danoeh/antennapod/storage/preferences/UserPreferences.java`
   - Added `EPISODE_CLEANUP_7DAY_AUTO_DELETE = -4` constant

3. **EpisodeCleanupAlgorithmFactory.java**
   - `net/download/service/src/main/java/de/danoeh/antennapod/net/download/service/episode/autodownload/EpisodeCleanupAlgorithmFactory.java`
   - Registered the new cleanup algorithm

4. **arrays.xml**
   - `ui/preferences/src/main/res/values/arrays.xml`
   - Added `-4` to `episode_cleanup_values` array

5. **strings.xml**
   - `ui/i18n/src/main/res/values/strings.xml`
   - Added display text: "After 7 days if not favorited"

6. **AutomaticDeletionPreferencesFragment.java**
   - `app/src/main/java/de/danoeh/antennapod/ui/screen/preferences/AutomaticDeletionPreferencesFragment.java`
   - Added UI handling for the new cleanup option

7. **PlaybackService.java** (NEW - Android Auto)
   - `playback/service/src/main/java/de/danoeh/antennapod/playback/service/PlaybackService.java`
   - Added `CUSTOM_ACTION_TOGGLE_FAVORITE` constant
   - Added favorite toggle button to media session for Android Auto
   - Added handler for favorite toggle action
   - Button dynamically shows filled/outlined star based on current favorite status

### How the Android Auto Favorite Toggle Works Internally

1. **Custom Action Registration** (in `updateMediaSession()` method):
   - Checks if current playable is a `FeedMedia` instance
   - Determines if episode is currently favorited using `FeedItem.TAG_FAVORITE`
   - Selects appropriate icon: `ic_star` (filled) or `ic_star_border` (outlined)
   - Adds custom action to `PlaybackStateCompat` which Android Auto displays

2. **Action Handler** (in `onCustomAction()` callback):
   - Receives action when user taps the favorite button in Android Auto
   - Calls `DBWriter.toggleFavoriteItem()` to update database
   - Calls `updateMediaSession()` to refresh the button state
   - Icon updates immediately to reflect new favorite status

3. **Integration Points:**
   - Uses existing favorite infrastructure (DBWriter, FeedItem tags)
   - Leverages MediaSessionCompat for Android Auto communication
   - WearMediaSession adds Wear OS compatibility extras
   - Works seamlessly with MediaBrowserServiceCompat

### How the 7-Day Auto-Delete Works Internally

1. **Download Tracking**: When an episode is downloaded, the `downloadDate` field in `FeedMedia` is set to the current timestamp

2. **Cleanup Trigger**: Cleanup runs in these scenarios:
   - Before auto-downloading new episodes (to make space)
   - Every 3 days via `DatabaseMaintenanceWorker`
   - When cache size limit is exceeded

3. **Candidate Selection** (in `SevenDayAutoDeleteCleanupAlgorithm.getCandidates()`):
   - Query all downloaded episodes
   - Filter out favorites (`!item.isTagged(FeedItem.TAG_FAVORITE)`)
   - Calculate cutoff date: current date minus 7 days
   - Include only episodes where `downloadDate < cutoffDate`

4. **Deletion**: Delete oldest episodes first until space requirement is met

## License

AntennaPod is licensed under the GNU General Public License v3.0.
See LICENSE file for details.
