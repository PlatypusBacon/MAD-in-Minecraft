# Mutually Assured Destruction

Minecraft modding project for 5 guys, read the Tyler setup instructions below

## Tyler's Instructions
### Setup
first run (obviously):<br>
<code>git clone https://github.com/PlatypusBacon/MAD-in-Minecraft.git mutually-assured-destruction</code><br><br>
Then install JDK 25 if you dont have it, for windows this is here: https://adoptium.net/temurin/releases?version=25&os=windows&arch=any&mode=filter<br><br>
then setup using Intellij IDEA (update it as well).<br> 
Go to plugins in settings and install "Minecraft Development"<br>
Open this github folder as the project.<br>

### Where things are
We will be using different git branches so we dont read each others code all the time and can keep it to one really painful giant merge at the end, shouldnt be too bad as long as everyone keeps it to seperate folders.<br><br>
Code is almost all in the ```src/``` folder<br>
You will write either client or server mods, and these are written as Entrypoints and Mixins. Mixins inject your code into the source files, and Entrypoints are your code. 

### Where to put your code
Most code will be written and called from the main Entrypoints for client and server.
<br><br>
These are found at ```src\client\java\bunger\group\client\MutuallyAssuredDestructionClient.java```<br>
and at ```src\main\java\bunger\group\MutuallyAssuredDestruction.java```<br>
for client and server respectively.<br>
adding code is done through the main class in these files (the ones that implement ```ModInitializer``` and ```ClientModInitializer```)<br><br>
to add your code, make a new java file under ```src\(main|client)\java\bunger\group\(your_name)```, import it in the top of the file and then inside either ```onInitializeClient``` or ```onInitialize``` call your code.

### Updasing Json, not necessary usually
To update either Mixins or new main Entrypoints the config jsons need to be updated to map to these new files. IDK if this ever really needs to be done for Entrypoints, but for mixins this may need to be done for different target classes when the fabric source stuff isnt good enough.
<br><br>
When you add mixin files this needs to be updated in the mixins jsons. These are found in ```src\client\resources\mutually-assured-destruction.client.mixins.json``` for clients, and ```src\main\resources\mutually-assured-destruction.mixins.json``` for server.<br>
This is updated for the key:
```
"mixins": [
		"ExampleMixin"
	],
```
where the name of the mixin class is used in the value set, example is for class:
```
@Mixin(MinecraftServer.class)
public class ExampleMixin {
	@Inject(at = @At("HEAD"), method = "loadLevel")
	private void init(CallbackInfo info) {
		// This code is injected into the start of MinecraftServer.loadLevel()V
	}
}
```
<br><br>
When you add actual mod code (Entrypoints) (for some reason probably still dont do this)this needs to be updated in the json file: ```src\main\resources\fabric.mod.json``` under the key:
```
"entrypoints": {
	"main": [
		"bunger.group.MutuallyAssuredDestruction"
	],
	"client": [
		"bunger.group.client.MutuallyAssuredDestructionClient"
	]
},
```
where the values are the paths to the used ```.java``` files.

### Playing the Game

To test the mod and play the game, click next to run at the top and run either minecraft server or minecraft client, should run with your mods easy

### Changing the Version (bc Tyler was stupid)

First check master branch (or main both should work i think, accidentally made master when doing new version)
<br>
However if its easier to keep your code just take ```build.gradle```, ```gradle.properties```, and ```gradle/wrapper/gradle-wrapper.properties```
<br>
These will downgrade the versions, then change java version in project structure to Java version 17. Check your systeem path as well if this version is the highest.
<br><br>
Now ```./gradlew build clean```, and then try to run again. Some bits of the api may be different from existing code unfortunately, I know a few of them if you want to ask.