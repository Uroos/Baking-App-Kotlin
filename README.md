# Baking-App
This is a Baking App that allows Udacity’s resident baker-in-chief, Miriam, to share her recipes with the world. Its allows a user to 
select a recipe and see video-guided steps for how to complete it.

This app was taken from a functional state to a production-ready state. It involved finding and handling error cases, adding accessibility features, allowing for localization, adding a widget, and adding a library.

Through this app I learned to create and implement apps where I am responsible for designing and planning the steps I need to take to create a production-ready app. It was up to me to figure things out. I also learned how to handle unexpected input in the data.

This app uses:
-MediaPlayer/Exoplayer to display videos.
-Handles error cases in Android.
-A widget to enhance your app experience.
-Broadcast receiver to check for wifi connectivity and then automatically load data
-Intent Service that updates the widget when user selects a favorite recipe
-Leverages third-party libraries like butterknife, gson, timber, picasso
-Use Fragments to create a responsive design that works on phones and tablets.

The recipe listing is gotten from a JSON file containing the recipes' instructions, ingredients, videos and images. However it is not a properly filled file and some recipes have a video, an image, or no visual media at all.


<img src="https://github.com/Uroos/Baking-App/blob/master/Screenshot_20181222-221419.png" width="200" height="300" />


<img src="https://github.com/Uroos/Baking-App/blob/master/Screenshot_20181222-221429.png" width="200" height="300" />


<img src="https://github.com/Uroos/Baking-App/blob/master/Screenshot_20181222-221509.png" width="200" height="300" />

