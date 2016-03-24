# AndroidKeyboardWatcher
Software keyboard open/close watcher for Android.

Android SDK doesn't provide direct way to track open/close events from software keyboard. This small library does it for you.

![AndroidKeyboardWatcher](https://raw.githubusercontent.com/AzimoLabs/AndroidKeyboardWatcher/master/art/keyboard_shown.png)

## Usage

Make sure that you use `adjustResize` windowSoftInputMode in your Acitivty configuration in AndroidManifest.xml:

```xml
<activity
    android:name=".MainActivity"
    android:windowSoftInputMode="adjustResize" />
```

Bind KeyboardWatcher in your `Activity.onCreate()` method. To prevent memory leaks make sure to unbind it in `onDestroy()` method.

```java
public class MainActivity extends Activity implements KeyboardWatcher.OnKeyboardToggleListener {
    private KeyboardWatcher keyboardWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //...
        keyboardWatcher = KeyboardWatcher.initWith(this).bindKeyboardWatcher(this);
    }

    @Override
    protected void onDestroy() {
        keyboardWatcher.unbindKeyboardWatcher();
        super.onDestroy();
    }

    @Override
    public void onKeyboardShown(int keyboardSize) {
        
    }

    @Override
    public void onKeyboardClosed() {
        
    }
}
```

## Download

If you don't want to add another dependency to your project, just copy [KeyboardWatcher.java](https://github.com/AzimoLabs/AndroidKeyboardWatcher/blob/master/keyboardwatcher/src/main/java/com/azimolabs/keyboardwatcher/KeyboardWatcher.java) to your source directory.

```gradle
dependencies {
  compile 'com.azimolabs.keyboardwatcher:keyboardwatcher:0.1.2'
}
```

If you have issues with downloading it from jCenter repository please add temporary those lines to your root gradle file:

```gradle
allprojects {
    repositories {
        jcenter()
        maven{ //This will get KeyboardWatcher from local bintray Maven repository
            url 'https://dl.bintray.com/azimolabs/maven'
        }
    }
}
```


## License

    Copyright (C) 2016 Azimo

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


