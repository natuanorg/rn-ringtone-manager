
# rn-ringtone-manager

## Getting started

`$ npm install rn-ringtone-manager --save`

### Mostly automatic installation

`$ react-native link rn-ringtone-manager`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.natuan.RNRingtoneManagerPackage;` to the imports at the top of the file
  - Add `new RNRingtoneManagerPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':rn-ringtone-manager'
  	project(':rn-ringtone-manager').projectDir = new File(rootProject.projectDir, 	'../node_modules/rn-ringtone-manager/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':rn-ringtone-manager')
  	```


## Usage
```javascript
import RNRingtoneManager from 'react-native-ringtone-manager';
```
  