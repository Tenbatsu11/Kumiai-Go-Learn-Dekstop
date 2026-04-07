App developped for Kumiai Go Learn in java

----------------------------------------

This app is the desktop version of the Kumiai Go Learn website and adds some new integration only available through this version of the learning interface.

To use the application, you need to have a database connected with the spring boot api for kumiai go rpg and the DB set up. 
Else, you would not be able to access the entirety of the app and the Kumiai Go RPG. 

As of now, the app includes multiples categories found on the Kumiai Go Learn website such as : 
 - Kanji
 - Vocabulaire
 - Profile

But the app includes a new tabs only for this version of Kumiai Go Learn, which are the Kumiai Go RPG prototype and subscription tab.
More informations on the Kumiai Go RPG repository.

----------------------------------------

To set up the desktop app in local dev environment you need to modify the api URL found in src/main/java/fr/kumiaigorpg/desktop/api/ApiClient.java , with your own api URL. 
For more informations on how to set up the Kumiai Go RPG api, please follow the instructions on the dedicated repository.
