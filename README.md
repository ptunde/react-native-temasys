# react-native-temasys

## Android install steps:

1. npm install react-native-temasys@https://github.com/ptunde/react-native-temasys.git --save

2. Open up `android/app/src/main/java/[...]/MainActivity.java

- Add import com.temasysreact.*; to the imports at the top of the file
- Add new TemasysReactPackage() to the list returned by the getPackages() method

3. Append the following lines to android/settings.gradle:

  include ':TemasysReact'
  project(':TemasysReact').projectDir = new File(rootProject.projectDir, '../node_modules/TemasysReact/android')

4. Insert the following lines inside the dependencies block in android/app/build.gradle:

    compile project(':TemasysReact')
