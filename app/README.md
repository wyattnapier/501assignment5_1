# 501 Assignment 5-1

### Description

A basic multi-page recipe app with proper navigation and a ViewModel for storing recipes.

### AI Usage

I used AI to create my add recipe page because I wasn't sure how to add new inputs.
I had already created the functions within the ViewModel,
so it was just a matter of creating a space for inputs.
It also built in navigation on form submission back to the home page which is very clean. 
I also used AI to try to help me add the navigation to view recipe details when it is clicked
on from the home screen, but it struggled here. For one, it made it so that you could never return
to the home page via the bottom bar after you opened the recipe details -- you could only go back to
the home page using the back button. I managed to fix that by adding more dynamic routing checks.
Another issue that I experienced with Gemini was the wrong NavController in certain places which
then broke the following dependencies and things like my Icon imports.