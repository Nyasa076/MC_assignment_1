### README 
## MC Assignment-1

# PROGRESS TRACKING APP
Progress Tracking app is an Android app that helps users track their progress when traveling from one stop to other. It has a user-friendly design that allows you to track your total mileage traveled, distance left, and progress percentage.

The Android app allows users to view their stops in either scrollable or non-scrollable modes. Users may easily go through the whole list of stops in scrollable mode, however in non-scrollable mode, a set number of stops are presented at once, improving the user experience by offering a more concentrated perspective. Furthermore, the software supports both miles and kilometers for distance measurement, allowing users to choose between the two according to their desire. This adaptability appeals to a broader audience with diverse measuring units i.e kilometer and miles. Furthermore, to improve efficiency and ensure smooth scrolling, particularly when working with long lists of stops, the programme employs lazy list. When the number of stops is more than 10 than I have used lazy list which is scrollable while when the stops are 10 than normal list is used i.e the non scrollable list

# CODE STRUCTURE
The code initializes the app's theme and uses the setContent method to specify the content that will be shown in the MyApp.

The MyApp function apply the application's primary UI. It handles the application's state by utilizing remember and mutableStateOf to monitor different things such as stops, current stop index, and units (miles/kilometers)

Used 2 data classes stop and TotalDistance where stop tells the various stop with the distance from the previous stop while the TotalDistance calculates the total distance for the lazy list and and the normal list 

The calculateTotalDistance function is used to distance traveled and left for both the list 
The Progrress details implements the progress bar according to the total distance, distance traveled and distance covered in both the cases.

I have implemented the Lazylist and normal list , in lazy list lazy columns are used so that the app is still efficient with a large number of stops while the normal list (non-scrollable lis)t is implemented without it.

VIDEO LINK
[video of working of app](https://drive.google.com/file/d/1WLs1a1Avtrhzep3povMaPvxrT6nRi-n1/view?usp=drive_link)


