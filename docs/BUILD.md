###Tech stack
 * Android min version supported : **2.3.3 (Api 10)**
 * Build tool : **maven**
 * Unit Testing : **robolectric**

###Machine Setup instructions
Note : These steps are done in Linux Mint 15 (should work fine in ubuntu without any change)

* Install git  
```sudo apt-get install git```
* Install maven  
```sudo apt-get install maven```
* You need to install the below lib for 64-bit OS   
```sudo apt-get install ia32-libs```

* Download and extract Oracle JDK to any location  
  For example : I download the jdk and extracted to the folder ```/opt/jdk1.7.0_45```
* Download and extract Android SDK to any location  
  For example : I download the sdk and extracted to the folder ```/opt/android-sdk-linux```

* Update your .bash_profile or .bashrc  with the below details  
```
export JAVA_HOME=/opt/jdk1.7.0_45  
export ANDROID_HOME=/opt/android-sdk-linux  
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
```
* Execute the below command to update your current shell  
    * ```cd ~```  
    * ```. ~/.bashrc```
* Make the downloaded java SDK as the default  
    * ```sudo update-alternatives --install "/usr/bin/java" "java" "/opt/jdk1.7.0_45/bin/java" 1```
    * ```sudo update-alternatives --install "/usr/bin/javac" "javac" "/opt/jdk1.7.0_45/bin/javac" 1```
    * ```sudo update-alternatives --config java``` and choose the oracle jdk
    * ```sudo update-alternatives --config javac```
    * Make sure it is updated properly by executing ```java -version```
* Open android sdk manager using the command  
    * ```cd /opt/android-sdk-linux```  
    * ```./tools/android```
    * Select and Install Android 2.3.3 Api
* Download and extract Android Studio to any location.
  For example : I extracted to ```/opt/android-studio```

* Clone panic button source code  
  ```git clone https://github.com/PanicButton/PanicButton.git```
* Run maven compile to download necessary libs and compile
    * ```cd ./PanicButton```
    * ```mvn compile```
  
Check the instructions to setup IDE [here](https://github.com/PanicButton/PanicButton/wiki/IDE-Setup-Instructions)

THAT'S ALL FOLKS :-)
