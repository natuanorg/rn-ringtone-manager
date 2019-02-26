
# react-native-ringtone-manager

## Getting started

`$ npm install react-native-ringtone-manager --save`

### Mostly automatic installation

`$ react-native link react-native-ringtone-manager`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNRingtoneManagerPackage;` to the imports at the top of the file
  - Add `new RNRingtoneManagerPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-ringtone-manager'
  	project(':react-native-ringtone-manager').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-ringtone-manager/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-ringtone-manager')
  	```


## Usage
```javascript
import RNRingtoneManager from 'react-native-ringtone-manager';

// TODO: What to do with the module?
RNRingtoneManager;
```
  