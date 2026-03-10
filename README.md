# Summary
GovMobi is a mobile application developed as part of ANU's Software Construction course by a group of 5 students for streamlined vehicle and license management, built with Java and Firebase using Android Studio. It offers role-based dashboards for vehicle owners and administrators, enabling secure vehicle registration, multi-stage license applications, and real-time request processing. The app features a smart grammar-based search and an AI-powered chatbot integrated via the Gemini API to assist users. Under the hood, it utilises custom data structures like an AVL tree for efficient data handling and incorporates design patterns such as Factory, Iterator, and Facade. 
# Report - GovMobi Application


## Table of Contents

1. [Team Members and Roles](#team-members-and-roles)
2. [Summary of Individual Contributions](#summary-of-individual-contributions)
3. [Application Description](#application-description)
4. [Application Use Cases](#application-use-cases)
4. [Application UML](#application-uml)
5. [Tools and Platform Details](#tools-and-platform-details)
5. [Application Design and Decisions](#code-design-and-decisions)
6. [Implemented Features](#implemented-features)
7. [Testing Summary](#testing-summary)
6. [Summary of Known Errors and Bugs](#summary-of-known-errors-and-bugs)
9. [Team Meetings](#team-management)
10. [Conflict Resolution Protocol](#conflict-resolution-protocol)
    <br>
<hr>

## Administrative
- Minimum SDK: 30
- Target SDK: 35
- Firebase Repository Link: https://console.firebase.google.com/project/gp25s1app  
   - Confirm: [x] I have already added comp21006442@gmail.com as a Editor to the Firebase project prior to due date.
- Two user accounts for markers' access are usable on the app's APK:
   - Username: comp2100@anu.edu.au	Password: comp2100 [x] 
   - Username: comp6442@anu.edu.au	Password: comp6442 [x]
<br> 
<hr>

## Team Members and Roles
The key area(s) of responsibilities for each member

| UID      | Name       | Role                                                                                                                   |
|----------|------------|------------------------------------------------------------------------------------------------------------------------|
| U7991805 | Janvi      | UI/UX, Profile Integration, Firebase Authentication, Factory Design Pattern, Report, GPS Integration, Code Integration |
| u7763790 | Vrushabh   | Data Structures, Vehicle Processing                                                                                    |
| u8007015 | Arjun      | Admin , DataStream, Transaction                                                                                        |
| u8030355 | Shane      | Tokenizer, Search, Firebase Integration, Iterator Design Pattern Engine , LLM implementation                                              |
| u7884012 | Feng       | Firebase Structure, Data Isolation, Testing (JUnit + Espresso + JaCoCo), Search & Filter Reconstruction |
<br> 
<hr>

## Summary of Individual Contributions

1. **u7991805, Janvi Rajendra Nandre**  I have 20% contribution, as follows: <br>
- **Code Contribution in the final App**

    - **Profile Management (View/Edit, Avatar, Firebase Storage) (100% contribution)**
        - Class  [`ProfileActivity.java`](app/src/main/java/com/example/myapplication/activity/ProfileActivity.java)
            - Methods: [`loadUserProfile`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/23820b64b4b35d533d7c4aa23b983d4b99af97b0/app/src/main/java/com/example/myapplication/activity/ProfileActivity.java#L78-L113), [`selectRandomProfileImage()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/23820b64b4b35d533d7c4aa23b983d4b99af97b0/app/src/main/java/com/example/myapplication/activity/ProfileActivity.java#L135-L164), [`saveProfileToFirebase()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/23820b64b4b35d533d7c4aa23b983d4b99af97b0/app/src/main/java/com/example/myapplication/activity/ProfileActivity.java#L171-L206)
            - Features:
                - Retrieves/stores profile data from `temp_userdata.json` and [`Userdata/{uid}/profile`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2FUserdata)
                - Random avatar selection (1–8) with index stored in Firebase

    - **Login & Signup Functionality (Firebase Authentication) (100% contribution)**
        - Class [`LoginActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/LoginActivity.java), [`SignUpActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/SignUpActivity.java)
            - Methods: [`signInWithEmailAndPassword()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/23820b64b4b35d533d7c4aa23b983d4b99af97b0/app/src/main/java/com/example/myapplication/activity/LoginActivity.java#L77-L117), [`createUserWithEmailAndPassword()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/7c35e88104895a86f6d023604e998651bd77c388/app/src/main/java/com/example/myapplication/auth/SignUpHandler.java#L51-L81)
            - Features:
                - Admin/user routing based on email
                - Profile info persisted in Firebase [`Userdata/{uid}`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2FUserdata)

    - **Factory Pattern – Modular Auth Flow(100% contribution)**
        - Class  [`AuthHandlerFactory()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/auth/AuthHandlerFactory.java), [`LoginHandler()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/auth/LoginHandler.java), [`SignUpHandler()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/auth/SignUpHandler.java), Interface [`AuthHandler.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/auth/AuthHandler.java)
            - Used to decouple login and signup logic
            - Each handler encapsulates Firebase logic via [`HashMap<String, Object>`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/7c35e88104895a86f6d023604e998651bd77c388/app/src/main/java/com/example/myapplication/auth/SignUpHandler.java#L66)

    - **GPS Tracking(100% contribution)**
        - Class [`AddVehicleActivity`](app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java): [`getCurrentLocation(), saveVehicle()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java#L72-132)
            - Features:
                - Uses [`FusedLocationProviderClient`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5aae7afed8a9acd0fc5605c037107e638228e938/app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java#L64) for coordinates
                - Converts lat/lng to address using [`Geocoder`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5aae7afed8a9acd0fc5605c037107e638228e938/app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java#L117-L131)
                - Saves in Firebase under [`Userdata/{uid}/vehicles`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2FUserdata~2FAxcSlfEnZhUth7hbMMezirAseq93~2Fvehicles)

    - **Dashboard Interface(80% contribution)**
        - Class [`DashboardActivity`](app/src/main/java/com/example/myapplication/activity/DashboardActivity.java)
            - Methods: [`buildCards()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/2ebb6bb86aede8451de963e10763ef4d83c7da76/app/src/main/java/com/example/myapplication/activity/DashboardActivity.java#L118-L191), [`handleCardClick()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/2ebb6bb86aede8451de963e10763ef4d83c7da76/app/src/main/java/com/example/myapplication/activity/DashboardActivity.java#L196-L225)
            - Features:
                - Dashboard grid built with [`String[][]` ](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/2ebb6bb86aede8451de963e10763ef4d83c7da76/app/src/main/java/com/example/myapplication/activity/DashboardActivity.java#L146-L151)card structure
                - Parses JSON via [`Map<String, Object>`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/2ebb6bb86aede8451de963e10763ef4d83c7da76/app/src/main/java/com/example/myapplication/activity/DashboardActivity.java#L136-L144) to display license and vehicle info

- **Code and App Design**
    - **Design Patterns Used**
        - Factory Design Pattern in `auth` module for scalable, maintainable login/signup handling
    - **Data Structures Used**
        - `HashMap<String, Object>` for Firebase updates
        - `Map<String, Object>` for reading/parsing JSON
        - `String[][]` for UI card generation on dashboard
        - `String` for storing GPS coordinates and address
    - **UI Design**
        - Created XML layouts for all core screens using Android Studio and Material Design
        - Layouts designed:[`activity_profile.xml`](app/src/main/res/layout/activity_profile.xml), [`activity_login.xml`](app/src/main/res/layout/activity_login.xml), [`activity_signup.xml`](app/src/main/res/layout/activity_signup.xml), [`activity_dashboard.xml`](app/src/main/res/layout/activity_dashboard.xml)

- **Others**
        - Helped write the final report (60% contribution), meeting minutes, initlal [`Project Plan`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/items/plan.md?ref_type=heads), integration of everyones's features, merge conflict resolution,UML,use case diagrams 
<br><br>

2. **u7763790, Vrushabh Vijay Dhoke** : I have contributed 20% to the project, as detailed below:

- **Code Contribution in the final App**

    **AVL Tree Implementation**
    - **Purpose:** Efficient vehicle history search using a self-balancing binary search tree.
    - **Files:** [`VehicleStatusActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/VehicleStatusActivity.java), [`AVLTree.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/structures/AVLTree.java), [`AVLTreeIterator.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/structures/AVLTreeIterator.java)
    
    - **Details:**
        - Authored AVLTree class for storing/searching vehicles using VIN as a key.
        - Covers recursive insertion (Lines 80–108), VIN-based search (Lines 118–126), license plate search (Lines 129–137), and deletion (Lines 140–179).
        - Iterator for sorted VIN traversal (Lines 16–50), co-authored with Shane George Shibu (u8030355).
        - Designed for scalability, maintaining balance using AVL rotation logic (Lines 25–78).
        - Integrated with [`LicenseDetailActivity`](app/src/main/java/com/example/myapplication/activity/LicenseDetailActivity.java) and local cache for efficient display.
    
    **Add Vehicle Feature**
    - **File:** [`AddVehicleActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java)
    - **Description:**
        - Developed complete logic and UI for adding vehicles.
        - Captures name, license plate, VIN, and visibility (Public/Private).
        - Stores data in Firebase ([`Userdata/vehicles`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2FUserdata), [`public_vehicle`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2Fpublic_vehicle)), validates inputs, and syncs cache.
        - Includes input error handling, logging, and toast messages for feedback.
        - Integrates with AVLTree for vehicle search.
    
    **UI Layout Design**
    - **Files:** [`apply_license.xml`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/res/layout/apply_license.xml), [`activity_vehicle_status.xml`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/res/layout/activity_vehicle_status.xml), [`activity_add_vehicle.xml`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/res/layout/activity_add_vehicle.xml)
    
    - **Features:**
        - Used responsive `LinearLayout`, conditional visibility for multistep flows.
        - Designed intuitive forms for adding vehicles and license applications.
        - Applied visual consistency with clean layouts and Material Design principles.


- **Code and App Design** 
    **AVL Tree Integration**: Proposed and implemented core AVLTree class for vehicle data handling.
    - Ensures O(log n) performance for insert/search/delete across large datasets.
    - Supports VIN and case-insensitive license plate search.
    - Integrated with `AddVehicleActivity` for data input and `LicenseDetailActivity` for viewing.
    - Iterator designed for sorted report generation.
    
  **License Application UI (MyLicensesActivity)**: Form inputs for license details.
    - Designed multistep input flow with conditional layouts.
    - Integrated confirmation dialog on submission.
    
  **Add Vehicle UI Enhancements**: Designed clean form layout.
    - Added checkbox for Public/Private selection.
    - Ensured accessibility and mobile responsiveness.
    
  **Tools Used**
    - Android Studio Layout Editor
    - XML for UI structure
    - Material Design components for consistency

3. **u8007015, Arjun Satish**  I have 20% contribution, as follows: <br>

    -  **Admin Dashboard & License Request Management** : Built an admin dashboard to manage pending license requests with real-time Firebase updates. Added a simulator (LicenseRequestSimulator.java) to auto-generate mock requests for to represent Datastream.
        * [`AdminActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/1dc762a3ccd745d379ff08e29f2df1f0ec04fce2/app/src/main/java/com/example/myapplication/activity/AdminActivity.java)
        * [`LicenseRequestSimulator.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/1dc762a3ccd745d379ff08e29f2df1f0ec04fce2/app/src/main/java/com/example/myapplication/util/LicenseRequestSimulator.java)
        * [`LicenseAdapter.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/1dc762a3ccd745d379ff08e29f2df1f0ec04fce2/app/src/main/java/com/example/myapplication/adapter/LicenseAdapter.java)

    - **Admin Profile & Authentication** :  Admin profile viewer which fetches and displays admin details from Firebase (AdminData node).  Updated Firebase with status changes and admin comments 
        * [`AdminProfileActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/1dc762a3ccd745d379ff08e29f2df1f0ec04fce2/app/src/main/java/com/example/myapplication/activity/AdminProfileActivity.java)

    - **License Application System**: Implemented user-facing form to apply for a driving license. The license requests then gets submitted to Firebase Realtime database and sent to the Admin for review.
        * [`ApplyLicenseActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/1dc762a3ccd745d379ff08e29f2df1f0ec04fce2/app/src/main/java/com/example/myapplication/activity/ApplyLicenseActivity.java)

    - **License Request Review**: Admin interface to review/process individual license requests. Each request displays applicant details and admin  has the following actions defined to proccess license request: Approve, Reject, or Request Evidence. Admin can also add remarks to be sent back to the user.
        * [`LRequestReviewActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/1dc762a3ccd745d379ff08e29f2df1f0ec04fce2/app/src/main/java/com/example/myapplication/activity/LRequestReviewActivity.java)
    - **Singleton Design Pattern**: Used singleton design pattern for using database connection.
        * [`DatabaseSingleton`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/1dc762a3ccd745d379ff08e29f2df1f0ec04fce2/app/src/main/java/com/example/myapplication/util/DatabaseSingleton.java)
    - **Feature Video**: Recorded and edited video of app features for features.mp4
        * [`features.mp4`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/1dc762a3ccd745d379ff08e29f2df1f0ec04fce2/items/features.mp4)
          <br><br>

4. **u8030355, Shane George Shibu** I have 20% contribution, as follows: <br>
    - **Design Pattern**:I implemented the Iterator Design Pattern for our custom AVLTree class to decouple the tree’s structural logic from its traversal mechanism. Instead of embedding traversal directly within the tree class, I created a dedicated [`AVLTreeIterator.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/structures/AVLTreeIterator.java?ref_type=heads), which provides a clean and reusable way to iterate over the tree’s elements in-order.
This approach allows client classes (like VehicleStatusActivity or admin tools) to traverse the AVL tree without needing to understand or duplicate the traversal logic. It also adheres to the Single Responsibility Principle, making the AVL tree solely responsible for maintaining balance and structure, while traversal is handled independently.

    - **Search Feature**: Developed search feature, including logic for Tokenizer and Parser. The Parser also handles plate pattern detection using regular expressions (e.g., [A-Z]{3}[0-9]{3}), and distinguishes between help requests and data queries. It returns one or more structured Query objects that the app then evaluates through the Evaluator class to fetch relevant results from local data or Firebase.

    - **User Centralisation**: I implemented user centralisation in the Licenses section to ensure that only license data associated with the signed-in user is retrieved from Firebase Realtime Database. This was achieved by leveraging Firebase Authentication to securely identify the current user via their UID. [`MyLicensesActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/MyLicensesActivity.java?ref_type=heads), I modified the database reference to dynamically point to the authenticated user’s license data. This ensures that when the user opens the Licenses screen, the app retrieves only their licenses (e.g., licenses/U7vBZWAat5dR7iM7GtvwuKQ4gTi2/license1, etc.). No global or unfiltered data is accessed.

    - **LLM Implementation**: I was responsible for designing and implementing the Surprise Feature, which involved integrating a Large Language Model (LLM) into our Android application. Instead of using Hugging Face, I integrated the Gemini API by Google, ensuring that the feature complied with all outlined requirements.
To support modularity, I implemented an LLMFacade class using the Facade design pattern, allowing the LLM component to be easily swapped or updated without impacting the rest of the app. I also developed the [`QAActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/QAActivity.java?ref_type=heads), which acts as a dedicated Help & Support chatbot interface. This screen allows users to ask questions like "How do I apply for a license?" or "What does vehicle history mean?", and receive context-specific answers from Gemini in real time.
To make the chatbot relevant to our app, I used prompt engineering to embed key information about the app’s features into every request sent to the model. This ensured the chatbot gave meaningful and accurate responses about the functionality of our system. I also avoided hardcoded responses to ensure full compliance with the project brief. 

    - **Code and App Design**:[Iterator Design Pattern for AVL traversal logic: [`AVLTree.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/structures/AVLTree.java?ref_type=heads), [`Evaluator.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/parser/Evaluator.java?ref_type=heads)], 
    [Search and parsing logic: [`Tokenizer.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/parser/Tokenizer.java?ref_type=heads), [`Parser.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/parser/Parser.java?ref_type=heads), [`Query.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/parser/Query.java?ref_type=heads)][`LLMfacade.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/util/LLMFacade.java?ref_type=heads)[`Chatadapter`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/adapter/ChatAdapter.java?ref_type=heads)[`ChatMessage.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/model/ChatMessage.java?ref_type=heads)    

    - **Others**
       made videos and edited them[`features.mp4`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/items/features.mp4?ref_type=heads)]     


5. **u7884012, Ruoheng Feng**  I have 20% contribution, as follows: <br>


- **Code Contribution in the final App**

    - **Firebase Structure & User Data Isolation**  
    Designed and implemented the Firebase `Userdata/{uid}` schema to store all user-related data (licenses, vehicles, profile) under a unified and isolated structure.  
    Implemented core read/write logic for synchronizing Firebase data with local temporary storage (`temp_userdata.json`).  
    Established a consistent access pattern to abstract Firebase structure and reduce coupling in Activity layers.  
        Files:  
        - [`FirebaseUserUtil.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/util/FirebaseUserUtil.java)  
        - [`LocalDataUtil.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/util/LocalDataUtil.java)

        Key Methods:
        - `FirebaseUserUtil.fetchAndSaveUserData(Context, FirebaseUser)`
        - `FirebaseUserUtil.uploadUserData(Context, FirebaseUser)`
        - `LocalDataUtil.saveUserMapToTempJson(Context, Map<String, Object>)`
        - `LocalDataUtil.loadUserMapFromTempJson(Context)`

    - **Testing Infrastructure & Bug Fixing: JUnit, Espresso, JaCoCo**  
      Developed and integrated a complete testing infrastructure covering both logic and UI layers using JUnit and Espresso.  
      Configured JaCoCo for unified code coverage reporting, combining unit tests and instrumentation tests, and fully integrated it into the Gradle build system.  
      Registered a custom `jacocoTestReport` Gradle task to merge coverage data from `testDebugUnitTest` and `connectedDebugAndroidTest`, producing unified HTML and XML reports.  
      Developed Firebase test reset utilities and backup helpers to ensure test consistency, data isolation, and repeatability.  
      Beyond infrastructure setup, took full responsibility for identifying and fixing all known bugs revealed by test coverage—ranging from UI inconsistencies and crash scenarios to edge case logic errors in both frontend and backend logic.

      Files:
        - [`app/src/test/`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/tree/main/app/src/test/)
        - [`app/src/androidTest/`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/tree/main/app/src/androidTest/)
        - [`build.gradle.kts`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/build.gradle.kts)
        - [`FirebaseUserBackupUtil.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/util/FirebaseUserBackupUtil.java)
        - [`TestFirebaseAuthUtil.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/util/TestFirebaseAuthUtil.java)
        - [`ResetTestAccount.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/util/ResetTestAccount.java)

      Key Methods and Build Elements:
        - `TestFirebaseAuthUtil.loginIfNeeded()`
        - `FirebaseUserBackupUtil.backupFirebaseUser(Context)`
        - `FirebaseUserBackupUtil.restoreFirebaseUser(Context)`
        - `ResetTestAccount.runReset()`

      Gradle:
        - `tasks.register<JacocoReport>("jacocoTestReport") { ... }`
      
    - **License & Vehicle Detail Views**  
      Independently developed two activity classes for displaying user-specific license and vehicle information.  
      Each activity retrieves relevant data from Firebase under the `Userdata/{uid}/licenses` and `Userdata/{uid}/vehicles` nodes, respectively.  
      UI includes TextView fields dynamically populated from the database, with null-checking and fallback handling to prevent crashes on incomplete records.  
      Implemented intent-based navigation and deserialization of selected object data passed from parent activities (e.g., list or dashboard views).  
      Additionally implemented deletion functionalities to remove licenses and vehicles from both local storage and Firebase, with proper UI refresh and confirmation dialogs.
    
      Files:
        - [`LicenseDetailActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/LicenseDetailActivity.java)
        - [`VehicleDetailActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/VehicleDetailActivity.java)
    
      Key Methods:
        - `LicenseDetailActivity.onCreate(Bundle)`
        - `LicenseDetailActivity.formatValue(String)`
        - `VehicleDetailActivity.onCreate(Bundle)`
        - `VehicleDetailActivity.formatKeyName(String)`
        - `VehicleDetailActivity.formatValue(String)`
        - `LicenseDetailActivity.delete_license()`
        - `VehicleDetailActivity.delete_vehicle()`
      
    - **Smart Search System (Co-authored with u8030355 – Shane George Shibu)**  
      Focused on implementing result filtering and keyword parsing logic in the smart search activity.  
      Developed logic to extract query tokens, detect search categories (e.g., name, license number, plate), and dynamically filter results using fuzzy matching and partial field mapping.  
      Integrated the search logic with the UI to respond to user input through a centralized search button, and updated result views accordingly.  
      Explicitly not responsible for `InstructionActivity` or any UI related to instructions/help.
    
      File:
        - [`SmartSearchActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/SmartSearchActivity.java)
    
      Key Methods:
        - `onCreate(Bundle)`
        - `updateFilterButtonStates()`
        - `searchInstructionKeywords(String)`
        - `loadLocalData(Context)`
        - `handleResultClick(Context, String)`

    - **Dashboard Integration – Realtime Data Update (Co-authored with u7991805 – Janvi Rajendra Nandre)**  
        Implemented the real-time dashboard update logic that displays the current number of user licenses and registered vehicles.  
        Integrated Firebase listeners (`ValueEventListener`) on `Userdata/{uid}/licenses` and `Userdata/{uid}/vehicles` nodes, and dynamically updated UI cards based on snapshot changes.  
        Ensured thread-safe UI updates and lifecycle-aware listener management to avoid memory leaks or null references.  

        File:  
        - [`DashboardActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/DashboardActivity.java)  
        
        Key Methods:  
        - `buildCards()` — builds and updates the GridLayout UI using local data refreshed from Firebase  
        - `autoRefresh` — `Handler` + `Runnable` loop for periodic dashboard update  
        - `onCreate()` — handles user info display, data sync, and listener registration

- **Code and App Design**

    - **Modular Firebase Schema Design**  
      Designed a unified Firebase schema centered around `Userdata/{uid}` to ensure clear data isolation across users. Enabled scalable extension of license, vehicle, and profile data as modular components.

    - **Reusable Data Layer Utilities**  
      Encapsulated all Firebase access logic in utility classes ([`FirebaseUserUtil`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/util/FirebaseUserUtil.java), [`LocalDataUtil`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/util/LocalDataUtil.java)) to minimize coupling with activity code and enable consistent local caching, backup, and testing.

    - **Testability-Centric Structure**  
      Structured classes to support test automation: separated logic from UI wherever possible, enabled Firebase mocking, and used consistent interfaces for user data access.

    - **Coverage-Oriented Build Integration**  
      Integrated test coverage generation directly into the Gradle build process via a dedicated `jacocoTestReport` task, enabling unified analysis of JUnit and Espresso test results.

    - **Collaborative Search Logic Abstraction**  
      Contributed to the redesign of the smart search system by isolating keyword parsing, token matching, and result filtering into dedicated methods within [`SmartSearchActivity`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/SmartSearchActivity.java).

    - **Activity Communication Pattern**  
      Applied a consistent intent-based data-passing mechanism between list and detail activities (e.g., [`LicenseDetailActivity`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/LicenseDetailActivity.java), [`VehicleDetailActivity`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/VehicleDetailActivity.java)) to ensure data flow is predictable and maintainable.
<br> 
<hr>

## Application Description : GovMobi

**GovMobi** is a mobile application designed to streamline vehicle registration and license management for vehicle owners, administrators, and the general public. Built with Firebase for secure authentication and data storage, it offers a user-friendly platform to manage vehicle-related tasks efficiently.

For **vehicle owners**, GovMobi enables adding, viewing, and deleting vehicles, applying for or renewing licenses, and tracking registration expiry.**Administrators** benefit from a real-time interface to review, approve, or reject license applications, with simulated requests for testing. The **general public** can access public vehicle details, such as registration status or stolen reports, via a smart search feature powered by natural language processing.

### Key features include:
- **Secure Access**: Firebase Authentication for login and sign-up, with role-based dashboards.
- **Vehicle Management**: Add, edit, or remove vehicles with details like license plate and VIN.
- **License Applications**: A simple process to apply for licenses, tracked in real time.
- **Admin Dashboard**: Manage license requests with approval or rejection options.
- **Smart Search**: Query vehicle details (e.g., rego status) using natural language.

GovMobi leverages an AVL tree for efficient vehicle data storage and retrieval, ensuring fast searches by VIN or license plate. Certified administrators are distinguished by their email domain (@govmobi.com), ensuring trusted oversight. The app’s intuitive design and robust backend make it a centralised solution for vehicle and license management.

## Problem Statement

Modern vehicle registration and license systems are often fragmented, inefficient, and lacking in user-centric features. Vehicle owners typically have to navigate multiple platforms or physical offices to check registration status, renew licenses, report stolen vehicles, or update personal information. Additionally, administrative workflows for processing license applications are manual and error-prone, causing delays in approvals and limited traceability.

There is also a growing need for transparency and public access to vehicle data—such as checking if a vehicle is stolen or verifying its registration status—while ensuring user privacy. However, few existing systems provide public-facing interfaces that balance accessibility with user control.

Furthermore, users often struggle with static interfaces that lack intelligent assistance or contextual help, especially when navigating government processes.

**This app addresses these problems by providing:**

- A **centralised mobile platform** where users can manage vehicle registrations, apply for or renew licenses, and view their license and profile details.
- A **real-time Firebase-backed system** that allows administrators to process license applications efficiently and transparently.
- A **smart search interface** powered by a grammar-based parser and AVL Tree for fast, intuitive queries.
- A **privacy-aware vehicle visibility model**, allowing users to control who can view their vehicle data.
- A **chatbot assistant** powered by the Gemini API to help users understand the app’s features, vehicle processes, and licensing workflows.

By integrating user-friendly design, secure data management, intelligent search, and AI-powered assistance, the app offers a scalable and intuitive solution for both users and administrators in vehicle and license management.
<br> 
<hr>

## Application Use Cases

**Use Case 1 (Admin – James): Processing License Applications**

James is an admin for GovMobi and needs to process a driving license application. Here's the flow:

1. **James logs into the app** with his admin credentials.
2. He is redirected to the **Admin Dashboard**.
3. James views a list of **pending license applications**.
4. He clicks on an application from Sarah and sees that it is missing some documents.
5. James selects "Request Evidence" and updates the application’s status to “Awaiting Evidence.”
6. The system immediately reflects this change in real-time, and Sarah can resubmit further information.

**Outcome:** The license application process continues, and the status of the request is updated promptly in the app.
<p align="center">
  <img src="media/app_snaps/AdminDashboard.jpeg" width="20%" />
  <img src="media/app_snaps/AdminRequestAction.jpeg" width="20%" />
  <img src="media/app_snaps/ReapplyLicense.jpeg" width="20%" />
</p>

**Use Case 2 (Public User – Emily): Searching for a Stolen Vehicle**

Emily is concerned that a vehicle she noticed on the road might be stolen, so she uses the GovMobi app to check the status. Here’s her process:

1. **Emily opens the app** and navigates to the **Vehicle History** section.
2. She types in the **license plate number** of her(added in My vehicles) or public(in the 2500+ vehicle.json dataset)vehicle into the search box.
3. The app performs a search on the **AVL Tree** and checks if the vehicle is registered and public.
4. If the vehicle is marked as stolen, the app displays that the vehicle is stolen and provides relevant details.
5. If no information is found, the app returns "Vehicle not found or private."

**Outcome:** Emily is able to get the information she needs about the vehicle’s status.

<p align="center">
<img src="media/app_snaps/DashboardPage.jpeg" width="20%" />
  <img src="media/app_snaps/VehicleStatusPublic.jpeg" width="20%" />
  <img src="media/app_snaps/VehicleStatusPrivate.jpeg" width="20%" />
  <img src="media/app_snaps/VehicleStatusALl.jpeg" width="20%" />
</p>

**Use Case 3 (Public User – Alex): Asking About Registration Expiry**

Alex needs to find out when his vehicle's registration data. Here’s his process:

1. **Alex opens the app** and taps on the **Smart Search** function.
2. He types a natural language question: "rego XYZ-123?"
3. The app uses the **Tokenizer** and **Parser** to match the question with the correct data.
4. The app returns the data for his vehicle,.

**Outcome:** Alex gets the information he needs quickly and easily without having to navigate through menus.
<p align="center">
  <img src="media/app_snaps/SmartSearchQuery.jpeg" width="20%" />
  <img src="media/app_snaps/SmartSearchFIlter.jpeg" width="20%" />
  <img src="media/app_snaps/VehicleInstruction.jpeg" width="20%" />
</p>

**Use Case 4 (Public User – Sam): Using Chat Support**

Sam has a question about how to add vehicles he received and wants to use the chat support feature. Here’s how it works for him:

![LLMusecase](media/_examples/LLMusecase.png)
<br><hr>
**Outcome:** Sam gets instant support without needing to call customer service.

**Use Case 5 (Vehicle Owner – Sarah): Applying for a Driving License**

Sarah is a vehicle owner who wants to apply for a driving license. Here’s the process:

![Applylicense](media/_examples/applylicense.png) <br><hr>
**Outcome:** Sarah's license application is successfully submitted, and her apply license page shows that she's waiting for update.

**Use Case 6 (Vehicle Owner – Jack): Adding a New Vehicle**

![AddVehicleUseCase](media/_examples/addvehicleusecase.png) <br><hr>

Jack, a vehicle owner, wants to add his new car to the app. Here's how he does it:

**Outcome:** Jack’s new vehicle is successfully added to the app.

**Use Case 7 (Vehicle Owner – Lucy): Managing Vehicle Profile**

Lucy wants to update her profile and change her profile avatar in the app. Here's how she does it:

1. **Lucy opens the app** and navigates to the **Profile** section.
2. She taps on "Edit Profile" to make changes to her details.
3. She updates her phone number and email address.
4. Lucy taps "Change Photo" and selects a new avatar from the app’s predefined list.
5. She saves her updated profile details.

**Outcome:** Lucy’s profile is updated with new information and a new avatar.
<p align="center"> <img src="media/app_snaps/Profile.png" width="20%" /></p>

### Target Users

#### Admins
- **Goal:** Admins use the application to review, approve, or reject license applications and manage system functionality.
- **Example Users:** Officials of licensing authority

#### Public Users
- **Goal:**  Users can search for vehicle information, apply for driving licenses, or interact with support.
- **Example Users:**
    - Security enforcers - Police (Searching for stolen vehicles)
    - Vehicle owners (Checking registration expiry)
    - General public (Getting support via chat)

#### Vehicle Owners
- **Goal:** Vehicle owners can register their vehicles, manage their profiles, and apply for services like driving licenses.
- **Example Users:**
    - Vehicle ownerss
    - People applying license for the first time
    

<br> 
<hr>

## Application UML

![UML](media/_examples/UML.png) 

<br> 
<hr>

## Tools and Platform Details

#### Tools Used

- **IDE:** Android Studio 
- **Programming Language:** Java
- **UI Design:** XML (Android Layout Editor)
- **Version Control:** Git (hosted on GitLab)
- **Testing Frameworks:**
    - JUnit 4 for unit testing junit:junit:4.13.2
    - Espresso for UI testing
    - JaCoCo for code coverage reporting
- **Database:** [Firebase Realtime Database](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data)
- **AdminData :** [`Admin Database`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2FAdminData)
- **Authentication:** Firebase Authentication (firebase-auth:22.1.1 - [`Auth data`](https://console.firebase.google.com/project/gp25s1app/authentication/users) )
- **Local Storage:** JSON files (stored in [`assets`](app/src/main/assets) and [`Firebase`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data))
- **Design Libraries:**
    - AndroidX Components
    - Material Design Components
    - Glide (for image loading)
- **Data Visualization:** RecyclerView, CardView
- **Language Model Integration:** Gemini API (Google AI)

#### Admin Login Credentials for testing Admin Features from [Firebase](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2FAdminData)

| Admin ID | Email               | Password  |
|----------|---------------------|-----------|
| ADM001   | admin@govmobi.com   | admin1    |

#### Platform Configuration

- **Minimum SDK Version:** API Level 30 
- **Target SDK Version:** API Level 35 ("VanillaIceCream" Android 15.0)
- **Compile SDK Version:** API Level 35
<br><hr>

## Code Design and Decisions
<hr>

### Parser and Tokenizer Implementation

1. **Grammar Design**:
    - Formal grammar for search queries:
      ```
      <Query> ::= <Command> <Target>
      <Command> ::= "status" | "expiry" | "fines"
      <Target> ::= [A-Z]{3}-?[0-9]{3}  // License plate pattern
      ```
    - **Justification**:
        - Converts natural language input into structured Query objects using keyword and pattern recognition..
        - Matches known vehicle plates using regex: [a-zA-Z]{3}-?[0-9]{3}.
        - Classifies queries by intent types such as rego_status, expiry_date, fines, and license_info.
        - Implements fallback logic when plates are missing, based on vehicle and license keyword categories.
        - Detects help/instruction prompts using the isIntroRequest() method.
2. **Tokenizer/Parser**:
    - Code: [`Tokenizer.java`](src/com/example/parser/Tokenizer.java), [`Parser.java`](src/com/example/parser/Parser.java)
    - **Key Features**:
        - Tokenizer splits user input into lowercase tokens using whitespace-based regex.
        -Parser.suggestQueries() analyzes these tokens to generate intent-based Query objects.
        - Produces structured `Query` objects for further processing.
        - Supports fallback parsing for [Search-Invalid].
        - Integrated into `[SmartSearchActivity.java](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/23820b64b4b35d533d7c4aa23b983d4b99af97b0/app/src/main/java/com/example/myapplication/activity/SmartSearchActivity.java)` for dynamic results.
        - Stateless, lightweight design suitable for real-time mobile usage.
<hr>

### Data Structure Choices

1. **AVL Tree**
    - Used in: [`VehicleStatusActivity.java`](src/com/example/activity/VehicleStatusActivity.java)
    - **Justification**:
        - Ensures O(log n) search performance for large vehicle datasets.
        - Automatically balances tree on insert/delete.
        - Optimized for range-based search operations.
        - Improves performance over linear search or lists.
        - Stores vehicle registration objects keyed by license plate.

2. **HashMap**
    - Used in: [`SmartSearchActivity.java`](src/com/example/activity/SmartSearchActivity.java)
    - **Justification**:
        - Provides O(1) lookup for exact license plate matches.
        - Lightweight memory usage for in-memory search results.
        - Easy to populate from JSON and Firebase.
        - Suitable for flat key-value vehicle data.
        - Simplifies implementation of [Search-Filter].

3. **Firebase Realtime Database**
    - Used across:
        - [`FirebaseUserUtil.java`](app/src/main/java/com/example/myapplication/util/FirebaseUserUtil.java)
        - [`LocalDataUtil.java`](app/src/main/java/com/example/myapplication/util/LocalDataUtil.java)
        - [`ProfileActivity.java`](app/src/main/java/com/example/myapplication/activity/ProfileActivity.java)
        - [`AddVehicleActivity.java`](app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java)
        - [`ApplyLicenseActivity.java`](app/src/main/java/com/example/myapplication/activity/ApplyLicenseActivity.java)
        - [`AdminActivity.java`](app/src/main/java/com/example/myapplication/activity/AdminActivity.java)
        - [`MyVehiclesActivity.java`](app/src/main/java/com/example/myapplication/activity/MyVehiclesActivity.java)
        - [`MyLicensesActivity.java`](app/src/main/java/com/example/myapplication/activity/MyLicensesActivity.java)
        - [`VehicleStatusActivity.java`](app/src/main/java/com/example/myapplication/activity/VehicleStatusActivity.java)
    - **Justification**:
        - Serves as the persistent, cloud-hosted backend for all user and vehicle-related data.
        - Structured with a clear and consistent hierarchy:
          ```
          userdata/
            {uid}/
              profile/            // personal user details
              vehicles/           // user’s own vehicle records
              licenses/           // user’s personal license list (view only)

          public_vehicle/
            {vehicle_id}/         // public vehicles accessible by all users

          LicenseRequests/
            {request_id}/         // submitted license applications (centralized for admin review)
          ```
        - Design decisions:
            - `userdata/` provides user-specific storage and isolation
            - `public_vehicle/` and `LicenseRequests/` enable global access for search and admin workflows
            - Supports role-based access control for users vs. admins
        - Real-time synchronization is enabled via Firebase listeners, while local caching is handled via `LocalDataUtil`.
        - This structure underpins profile updates, vehicle tracking, license application workflows, and admin decision processes.
<hr>

### Design Patterns

1. **Singleton**
   * *Objective:* Manage a single instance of Firebase throughout the app to avoid memory leaks and duplicated initialization logic.
   * *Code Location:* [`DatabaseSingleton.java`](app/src/main/java/com/example/myapplication/util/DatabaseSingleton.java)
   * *Reasons:*
       * **Centralized Configuration:** Handles all Firebase interactions from one class.
       * **Memory Management:** Prevents memory leaks by reusing the same instance.
       * **Simplified Lifecycle:** Ensures consistent data access across activities.
       * **Testable:** Easier to mock Firebase access during unit testing.
       * **Consistency:** Reduces the risk of multiple Firebase client instances across the app.

2. **Factory Pattern**
    * *Objective: Used for deciding between Login and Sign-Up actions in the authentication process without repeating code, ensuring flexibility and easy maintenance.*
    * *Code Locations: Defined in [`AuthHandlerFactory.java`](app/src/main/java/com/example/myapplication/auth/AuthHandlerFactory.java), [`AuthHandler.java`](app/src/main/java/com/example/myapplication/auth/AuthHandler.java), [`LoginHandler.java`](app/src/main/java/com/example/myapplication/auth/LoginHandler.java), [`SignUpHandler.java`](app/src/main/java/com/example/myapplication/auth/SignUpHandler.java); Processed in [`LoginActivity.java`](app/src/main/java/com/example/myapplication/activity/LoginActivity.java) and [`SignUpActivity.java`](app/src/main/java/com/example/myapplication/activity/SignUpActivity.java).*
    * *Reasons:*
        * **Cleaner code:** Separates logic for Login and Sign-Up, keeping code maintainable.
        * **Flexibility:** New authentication types (like social logins) can be easily added by creating new handler classes.
        * **Separation of concerns:** Keeps the login and sign-up logic isolated, making it easier to manage and update in the future.
      
3. **Iterator Pattern**
   * *Objective:* Provide a standard way to traverse the AVL Tree without exposing its internal structure.
   * *Code Location:*  [`AVLTreeIterator.java`](app/src/main/java/com/example/myapplication/structures/AVLTreeIterator.java)
   * *Reasons:*
       * **Encapsulation:** Hides AVL Tree traversal details from the activity layer.
       * **Reusable Logic:** Allows consistent in-order traversal for any AVLTree instance.
       * **Separation:** Keeps traversal behavior separate from storage logic.
       * **Maintainability:** Easier to debug and update traversal without modifying core tree structure.
       * **Readability:** Activities and views interact with a simple `hasNext()` and `next()` interface.<br>
<hr>

### <u>Grammar(s)</u>

Production Rules:
```
<Query> ::= <Command> <Target>
<Command> ::= "status" | "expiry" | "fines"
<Target> ::= [A-Z]{3}-?[0-9]{3}
```

- Used to interpret structured queries from users in search functionality.
- Allows partial parsing and graceful degradation on invalid inputs.
<hr>

### Architectural Decisions

1. **Firebase + Local Dataset**:
    - Code: [`LocalDataUtil.java`](src/com/example/util/LocalDataUtil.java)
    - **Justification**:
        - Enables offline access via local where all relevant data associated with the user is fetched from the Firebase Realtime Database under the /Userdata/{uid} path. This data is then serialized into a local JSON file (temp_userdata.json).
        - Reduces read operations to Firebase, lowering latency.
        - Local fallback improves resilience and UX.

2. **Activity Result API**:
    - Usage: Between [`AddVehicleActivity`](app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java) and [`MyVehiclesActivity`](app/src/main/java/com/example/myapplication/activity/MyVehiclesActivity.java)
    - **Justification**:
        - Modern and type-safe replacement for `startActivityForResult`.
        - Reduces risk of intent mismatches.
        - Easier lifecycle management.

3. **View Binding**:
    - Used across all major activities.
    - **Justification**:
        - Eliminates boilerplate code.
        - Avoids `NullPointerException` by ensuring safe access.
        - Improves code readability and refactorability.
<hr>

### Performance Considerations

1. **Glide for Image Loading**:
    - Used in: [`ProfileActivity.java`](app/src/main/java/com/example/myapplication/activity/ProfileActivity.java#L95-100)
    - **Justification**:
        - Efficient image caching and scaling.
        - Reduces lag in profile image rendering.
        - Prevents memory leaks via automatic recycling.

2. **AVL Tree Search in Background Thread**:
    - Ensures smooth UI by offloading search logic.
    - Supports future integration with loading indicators.
      <br>
<hr>

## Implemented Features

### Basic Features

1. **[LogIn]** Firebase-based user authentication (easy )
    * **Code:** [`AuthHandlerFactory.java`](app/src/main/java/com/example/myapplication/auth/AuthHandlerFactory.java), [`AuthHandler.java`](app/src/main/java/com/example/myapplication/auth/AuthHandler.java), [`LoginHandler.java`](app/src/main/java/com/example/myapplication/auth/LoginHandler.java), [`SignUpHandler.java`](app/src/main/java/com/example/myapplication/auth/SignUpHandler.java); Processed in [`LoginActivity.java`](app/src/main/java/com/example/myapplication/activity/LoginActivity.java) and [`SignUpActivity.java`](app/src/main/java/com/example/myapplication/activity/SignUpActivity.java).
   * **Layout:** [`activity_login.xml`](app/src/main/res/layout/activity_login.xml), [`activity_signup.xml`](app/src/main/res/layout/activity_signup.xml)

    * **Description of feature:**  
      Implements secure login and sign-up functionality using Firebase Authentication, allowing users to access the app with role-based redirection (Admin to [`AdminActivity`](app/src/main/java/com/example/myapplication/activity/AdminActivity.java), User to[ `DashboardActivity`](app/src/main/java/com/example/myapplication/activity/DashboardActivity.java)). Includes support for creating new accounts with basic profile details.

    * **Description of your implementation:**
        - In [`LoginActivity`](app/src/main/java/com/example/myapplication/activity/LoginActivity.java), the feature uses [`FirebaseAuth.signInWithEmailAndPassword`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/23820b64b4b35d533d7c4aa23b983d4b99af97b0/app/src/main/java/com/example/myapplication/activity/LoginActivity.java#L77-L118) to authenticate users. Input validation checks for empty fields, and errors are logged with [`TAG`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/23820b64b4b35d533d7c4aa23b983d4b99af97b0/app/src/main/java/com/example/myapplication/activity/LoginActivity.java#L25) and displayed via [`Toast`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/23820b64b4b35d533d7c4aa23b983d4b99af97b0/app/src/main/java/com/example/myapplication/activity/LoginActivity.java#L63).  
          Users with “@govmobi.com” emails are routed to [`AdminActivity`](app/src/main/java/com/example/myapplication/activity/AdminActivity.java), others to [`DashboardActivity`](app/src/main/java/com/example/myapplication/activity/DashboardActivity.java). Two test accounts (“test@gmail.com”, “comp6442@anu.edu.au”) trigger `ResetTestAccount` utilities.  
          [`FirebaseUserUtil.fetchAndSaveUserData`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/23820b64b4b35d533d7c4aa23b983d4b99af97b0/app/src/main/java/com/example/myapplication/activity/LoginActivity.java#L106) fetches and saves user data post-login.
      
        - In [`SignUpActivity`](app/src/main/java/com/example/myapplication/activity/SignUpActivity.java), [`SignUpHandler`](app/src/main/java/com/example/myapplication/auth/SignUpHandler.java) handles account creation with [`FirebaseAuth.createUserWithEmailAndPassword`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/7c35e88104895a86f6d023604e998651bd77c388/app/src/main/java/com/example/myapplication/auth/SignUpHandler.java#L51-L82), storing name, email, phone, and default collections (vehicles, licenses) in Firebase Database under [`Userdata/{uid}`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2FUserdata).

        - The UI layouts ([`activity_login.xml`](app/src/main/res/layout/activity_login.xml), [`activity_signup.xml`](app/src/main/res/layout/activity_signup.xml)) provide a clean interface with input fields and buttons, styled with a purple theme (`#7B1FA2`) and shadow effects.

        - **Factory Design Pattern Usage:**  
          The [`AuthHandlerFactory`](app/src/main/java/com/example/myapplication/auth/AuthHandler.java) class implements the Factory design pattern to dynamically create appropriate authentication handlers ([`LoginHandler`](app/src/main/java/com/example/myapplication/auth/LoginHandler.java) or [`SignUpHandler`](app/src/main/java/com/example/myapplication/auth/SignUpHandler.java)) based on the authentication type (“login” or “signup”).  
          This decouples the client code (e.g., [`LoginActivity`](app/src/main/java/com/example/myapplication/activity/LoginActivity.java), [`SignUpActivity`](app/src/main/java/com/example/myapplication/activity/SignUpActivity.java)) from specific handler implementations, enhancing extensibility.  
          The factory method [`getHandler`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/7c35e88104895a86f6d023604e998651bd77c388/app/src/main/java/com/example/myapplication/auth/AuthHandlerFactory.java#L21-L28) takes a type parameter and optional extra arguments (e.g., name, phone for signup), returning the correct [`AuthHandler`](app/src/main/java/com/example/myapplication/auth/AuthHandler.java) instance.  
          This approach allows easy addition of new handler types without altering existing code, adhering to the open-closed principle.

    * **Link to the Firebase repo:**  
      [`Firebase Project Repo`](https://console.firebase.google.com/project/gp25s1app/authentication/users)
    <p align="center">
      <img src="media/app_snaps/LoginPage.jpeg" width="20%" />
      <img src="media/app_snaps/SignUpPage.jpeg" width="20%" />
    </p>
    <br><br>

2. **[DataFiles]** 2500+ vehicle records in JSON file (easy)
    * **Code:** [`AddVehicleActivity.java`](app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java), [`VehicleStatusActivity.java`](app/src/main/java/com/example/myapplication/activity/VehicleStatusActivity.java)

    * **Description of feature:**  
      The application uses a dataset with over 2,500 vehicle records to store and manage vehicle information, supporting features like adding vehicles and searching public records.

    * **Description of your implementation:**
        - In [`AddVehicleActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5aae7afed8a9acd0fc5605c037107e638228e938/app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java#L163-L232), we save vehicle data (e.g., name, license plate, VIN, location) to a [`vehicles.json`](app/src/main/assets/vehicles.json) file in Firebase under the user’s node ([`Userdata/{uid}/vehicles`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2FUserdata)) and optionally in [`public_vehicle`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2Fpublic_vehicle) if marked "Public". The data is structured as a JSON object with fields like `vehicleName`, `licensePlate`, and `visibility`, updated via `FirebaseDatabase.setValue()`. This ensures new vehicle entries are added to the dataset.

        - In [`VehicleStatusActivity.java`](app/src/main/java/com/example/myapplication/activity/VehicleStatusActivity.java), we load the dataset from Firebase’s [`public_vehicle`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2Fpublic_vehicle)) node into an [`AVLTree`](app/src/main/java/com/example/myapplication/structures/AVLTree.java) using [`loadVehiclesFromFirebase()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5a550234d98f5d162e4a3d4091e79241a7f95e5b/app/src/main/java/com/example/myapplication/activity/VehicleStatusActivity.java#L121-L148). The method iterates over Firebase snapshots, filters for "Public" vehicles, and inserts them into the tree. This allows searching and browsing of over 2,500 records by license plate.

        - The initial [`vehicles.json`](app/src/main/assets/vehicles.json) file with 2,500+ entries is stored in the app’s assets folder and copied to internal storage during setup. Updates from [`AddVehicleActivity`](app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java) sync with Firebase, keeping the dataset current. [`VehicleStatusActivity`](app/src/main/java/com/example/myapplication/activity/VehicleStatusActivity.java) uses this data for real-time searches and displays.

        - We used this dataset to provide a robust foundation for vehicle management and public search features, ensuring scalability and quick access to vehicle records. The JSON format aligns with course teachings for structured data, and Firebase integration allows real-time updates across users.

    * **Link to the Firebase repo:**   [`Firebase Database`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2Fpublic_vehicle) 

    * **Code to the Data File:** [`vehicles.json`](app/src/main/assets/vehicles.json)
      <br><br>

3. **[LoadShowData]** Vehicles and Licenses loaded and displayed from Firebase and JSON (easy)

    * **Code:** [`VehicleStatusActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5a550234d98f5d162e4a3d4091e79241a7f95e5b/app/src/main/java/com/example/myapplication/activity/VehicleStatusActivity.java#L121-L148), [`MyVehiclesActivity.java`](https://gitlab.cecs.anu.edu.au/your-repo-path/MyVehiclesActivity.java#L50-90), [`LicenseDetailActivity.java`](app/src/main/java/com/example/myapplication/activity/LicenseDetailActivity.java), [`AdminActivity.java`](app/src/main/java/com/example/myapplication/activity/AdminActivity.java)

    * **Description of feature:**  
      The app loads vehicle and license data from Firebase and JSON files and shows it to users in a simple way, like lists, cards, or tables.

    * **Description of your implementation:**
        - In [`VehicleStatusActivity.java`](app/src/main/java/com/example/myapplication/activity/VehicleStatusActivity.java), public vehicle data is loaded from Firebase at [`public_vehicle`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5a550234d98f5d162e4a3d4091e79241a7f95e5b/app/src/main/java/com/example/myapplication/activity/VehicleStatusActivity.java#L121-L148).
        - In [`MyVehiclesActivity.java`](app/src/main/java/com/example/myapplication/activity/MyLicensesActivity.java), user vehicles are loaded from local [`vehicles.json`](app/src/main/assets/vehicles.json) (synced with Firebase under[ `Userdata/{uid}/vehicles`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2FUserdata~2Fx92KUj6HxTZny0QTs7NoV72MJpr1~2Fvehicles)).
        - In [`LicenseDetailActivity.java`](app/src/main/java/com/example/myapplication/activity/LicenseDetailActivity.java), license details are loaded from local [`users_interaction.json`]() and synced with Firebase under[ `Userdata/{uid}/licenses`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2FUserdata~2Fycx4pfzo5XQAGarDpu6FpyXK9x22~2Flicenses).
        - In [`AdminActivity.java`](app/src/main/java/com/example/myapplication/activity/AdminActivity.java), pending license requests are loaded from Firebase at [`LicenseRequests`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2FLicenseRequests).
        - In [`VehicleStatusActivity`](app/src/main/java/com/example/myapplication/activity/VehicleStatusActivity.java), [`loadVehiclesFromFirebase()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5a550234d98f5d162e4a3d4091e79241a7f95e5b/app/src/main/java/com/example/myapplication/activity/VehicleStatusActivity.java#L121-L148) uses [`FirebaseDatabase.getReference("public_vehicle")`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5a550234d98f5d162e4a3d4091e79241a7f95e5b/app/src/main/java/com/example/myapplication/activity/VehicleStatusActivity.java#L122) to fetch data and loads it into an [`AVLTree`](app/src/main/java/com/example/myapplication/structures/AVLTree.java). [`populateVehicleDetails()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5a550234d98f5d162e4a3d4091e79241a7f95e5b/app/src/main/java/com/example/myapplication/activity/VehicleStatusActivity.java#L169-L209) displays details (e.g., name, license plate, status) in text views with colored icons.
        - In [`MyVehiclesActivity`](app/src/main/java/com/example/myapplication/activity/MyVehiclesActivity.java), [`loadVehicles`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/a740fa61139e2129756eb771217718c27aa61111/app/src/main/java/com/example/myapplication/activity/MyVehiclesActivity.java#L65-L68) uses [`LocalDataUtil.loadVehiclesFromTempJson()`](app/src/main/java/com/example/myapplication/util/LocalDataUtil.java) to load [`vehicles.json`](app/src/main/assets/vehicles.json) and [`populateVehicleCards()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/a740fa61139e2129756eb771217718c27aa61111/app/src/main/java/com/example/myapplication/activity/MyVehiclesActivity.java#L76-L122) shows each vehicle as a card with name, plate, and visibility.
        - In [`LicenseDetailActivity`](app/src/main/java/com/example/myapplication/activity/LicenseDetailActivity.java), the app takes a JSON string from the intent, converts it to a map using `Gson`, and [`populateLicenseCards()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/1e16e85e78ad09256f266837799124865ac8ecfe/app/src/main/java/com/example/myapplication/activity/MyLicensesActivity.java#L115-L154) displays license details in a table layout. [`LocalDataUtil.loadUserMapFromTempJson()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/23820b64b4b35d533d7c4aa23b983d4b99af97b0/app/src/main/java/com/example/myapplication/util/LocalDataUtil.java#L120-L150) loads [`users_interaction.json`]() for local data.
        - In [`AdminActivity`](app/src/main/java/com/example/myapplication/activity/AdminActivity.java), [`fetchRequests()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5a550234d98f5d162e4a3d4091e79241a7f95e5b/app/src/main/java/com/example/myapplication/activity/AdminActivity.java#L167-L263) uses [`DatabaseReference`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5a550234d98f5d162e4a3d4091e79241a7f95e5b/app/src/main/java/com/example/myapplication/activity/AdminActivity.java#L58) to load [`LicenseRequests`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2FLicenseRequests) data from Firebase, filters for "Pending" status, and displays it in a [`RecyclerView`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5a550234d98f5d162e4a3d4091e79241a7f95e5b/app/src/main/java/com/example/myapplication/activity/AdminActivity.java#L102-L116) with [`LicenseAdapter`](app/src/main/java/com/example/myapplication/adapter/LicenseAdapter.java).

    - We load data from Firebase to keep it up-to-date for public searches, user vehicles, license details, and admin reviews. Using JSON files ([`vehicles.json`](app/src/main/assets/vehicles.json), [`users_interaction.json`]()) locally ensures offline access and quick loading. Displaying data this way makes it easy for users to see vehicle history, their own vehicles, license info, and admin tasks, improving the app’s usability.

   * **Link to the Firebase repo:**   [`Firebase Project Repo`](https://console.firebase.google.com/project/your-project-id) 

     * **Code to the Data File:**  [`vehicles.json`](app/src/main/assets/vehicles.json)
     <p align="center">
     <img src="media/app_snaps/VehicleStatusALl.jpeg" width="15%" />
     <img src="media/app_snaps/MyVehiclesPage.jpeg" width="15%" />
     <img src="media/app_snaps/VehicleStatusPublic.jpeg" width="15%" />
     <img src="media/app_snaps/AdminDashboard.jpeg" width="15%" />
     <img src="media/app_snaps/VehicleDataPage.jpeg" width="15%" />
     <img src="media/app_snaps/LicenseDetailPage.jpeg" width="15%" />
    </p>
     <br><br>

4. **[DataStream]** Automatic update of data using Firebase listeners (medium) (**Admin Email: admin@govmobi.com , pwd : admin1 )**

    * **Code:** [`AdminActivity.java`](app/src/main/java/com/example/myapplication/activity/AdminActivity.java), [`LicenseRequestSimulator.java`](app/src/main/java/com/example/myapplication/util/LicenseRequestSimulator.java), [`activity_admin.xml`](app/src/main/res/layout/activity_admin.xml)
    * **Description of feature:**  
      The app simulates user interactions with a data stream, automatically updating pending license requests with artificially generated license requests every 10 seconds for admins.

    * **Description of your implementation:**
        - In[`AdminActivity.java`](app/src/main/java/com/example/myapplication/activity/AdminActivity.java), data is loaded and updated in the admin dashboard. [`LicenseRequestSimulator.java`](app/src/main/java/com/example/myapplication/util/LicenseRequestSimulator.java) generates new license requests, and [`activity_admin.xml`](app/src/main/res/layout/activity_admin.xml)displays the updated list.
        - [`AdminActivity`](app/src/main/java/com/example/myapplication/activity/AdminActivity.java) uses [`ValueEventListener`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5a550234d98f5d162e4a3d4091e79241a7f95e5b/app/src/main/java/com/example/myapplication/activity/AdminActivity.java#L169-L262) on[ `dbRef`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5a550234d98f5d162e4a3d4091e79241a7f95e5b/app/src/main/java/com/example/myapplication/activity/AdminActivity.java#L58) (Firebase [`LicenseRequests`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2FLicenseRequests)) to listen for real-time changes. When new data arrives, [`fetchRequests()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5a550234d98f5d162e4a3d4091e79241a7f95e5b/app/src/main/java/com/example/myapplication/activity/AdminActivity.java#L167-L263) filters "Pending" requests, sorts them by [`dateCreated`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5a550234d98f5d162e4a3d4091e79241a7f95e5b/app/src/main/java/com/example/myapplication/activity/AdminActivity.java#L215-L233) using [`Collections.sort()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5a550234d98f5d162e4a3d4091e79241a7f95e5b/app/src/main/java/com/example/myapplication/activity/AdminActivity.java#L215-L233), and updates a [`RecyclerView`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5a550234d98f5d162e4a3d4091e79241a7f95e5b/app/src/main/java/com/example/myapplication/activity/AdminActivity.java#L167-L263) with [`LicenseAdapter`](app/src/main/java/com/example/myapplication/adapter/LicenseAdapter.java). A custom [`Toast`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5a550234d98f5d162e4a3d4091e79241a7f95e5b/app/src/main/java/com/example/myapplication/activity/AdminActivity.java#L244-L246) notifies of new requests.
        - [`LicenseRequestSimulator`](app/src/main/java/com/example/myapplication/util/LicenseRequestSimulator.java) runs a [`Handler`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/d777b56d975c70db4d81071da0a9dac6be451faa/app/src/main/java/com/example/myapplication/util/LicenseRequestSimulator.java#L35-L46) to add random license requests every 10 seconds via [`scheduleNextRequest()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/d777b56d975c70db4d81071da0a9dac6be451faa/app/src/main/java/com/example/myapplication/util/LicenseRequestSimulator.java#L77-L84) and [`addLicenseRequest()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/d777b56d975c70db4d81071da0a9dac6be451faa/app/src/main/java/com/example/myapplication/util/LicenseRequestSimulator.java#L89-L125), using a [`HashMap`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/d777b56d975c70db4d81071da0a9dac6be451faa/app/src/main/java/com/example/myapplication/util/LicenseRequestSimulator.java#L103) to store data (e.g., name, licenseType) and pushing it to Firebase.
        - The UI in `activity_admin.xml` shows the request count and list, updating automatically.
        - This simulates real user activity to test the app, ensuring admins see live updates. Firebase listeners provide real-time data flow, and the [`Handler`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/d777b56d975c70db4d81071da0a9dac6be451faa/app/src/main/java/com/example/myapplication/util/LicenseRequestSimulator.java#L35-L46) with Runnable implements a timed data stream, aligning with software construction concepts like event-driven programming and thread management.

    * **Link to the Firebase repo:** [`Firebase License Requests`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2FLicenseRequests)  <br>
    <p align="center">
            <img src="media/app_snaps/LicenseRequestDataStream.jpeg" width="20%" />
   </p>
    <br><br>

5. **[Search]** Smart grammar-based search (medium)
    * **Code:** [`SmartSearchActivity.java`](app/src/main/java/com/example/myapplication/activity/SmartSearchActivity.java), [`Tokenizer.java`](app/src/main/java/com/example/myapplication/parser/Tokenizer.java), [`Parser.java`](app/src/main/java/com/example/myapplication/parser/Parser.java), [`Evaluator.java`](app/src/main/java/com/example/myapplication/parser/Evaluator.java), [`Query.java`](https://gitlab.cecs.anu.edu.au/your-repo-path/Query.jaapp/src/main/java/com/example/myapplication/parser/Query.java), [`activity_smart_search.xml`](app/src/main/res/layout/activity_smart_search.xml), [`activity_instruction.xml`](app/src/main/res/layout/activity_instruction.xml)

    * **Description of feature:**  
      The app lets users search for vehicle, license, and instruction info using natural language queries, processed with a grammar-based system.

    * **Description of your implementation:**
        - In [`SmartSearchActivity.java`](app/src/main/java/com/example/myapplication/activity/SmartSearchActivity.java), users enter queries, and results are shown in a [`ListView`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/23820b64b4b35d533d7c4aa23b983d4b99af97b0/app/src/main/java/com/example/myapplication/activity/SmartSearchActivity.java#L157-L170). The grammar-based search is handled by [`Tokenizer.java`](app/src/main/java/com/example/myapplication/parser/Tokenizer.java), [`Parser.java`](app/src/main/java/com/example/myapplication/parser/Parser.java), [`Evaluator.java`](app/src/main/java/com/example/myapplication/parser/Evaluator.java), and [`Query.java`](app/src/main/java/com/example/myapplication/parser/Query.java). UI layouts are in [`activity_smart_search.xml`](app/src/main/res/layout/activity_smart_search.xml) and [`activity_instruction.xml`](app/src/main/res/layout/activity_instruction.xml).
        -[`Tokenizer.java` ](app/src/main/java/com/example/myapplication/parser/Tokenizer.java)splits user input into tokens (e.g., "check expiry ABC-123" → ["check", "expiry", "abc-123"]).
        - [`Parser.java`](app/src/main/java/com/example/myapplication/parser/Parser.java) uses a custom grammar to interpret tokens, recognizing keywords (e.g., "status", "expiry") and patterns like license plates (e.g., ABC123). It creates [`Query`](app/src/main/java/com/example/myapplication/parser/Query.java) objects (e.g., `Query("expiry_date", "COM-210")`) using [`suggestQueries()`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/23820b64b4b35d533d7c4aa23b983d4b99af97b0/app/src/main/java/com/example/myapplication/parser/Parser.java#L39-L93). The grammar includes predefined lists like [`VEHICLE_KEYWORDS`]() and [`LICENSE_KEYWORDS`]() to match intents.
        - [`Evaluator.java`](app/src/main/java/com/example/myapplication/parser/Evaluator.java) processes [`Query`](app/src/main/java/com/example/myapplication/parser/Query.java) objects against vehicle data in [`localVehicleMap`](), returning formatted results (e.g., "Expiry: 12-12-2025").
        - [`SmartSearchActivity` ](app/src/main/java/com/example/myapplication/activity/SmartSearchActivity.java)loads data from [`temp_userdata.json`]() (vehicles/licenses) and [`instructions.json`](app/src/main/assets/instructions.json) (FAQs). It displays results in a [`ListView`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/23820b64b4b35d533d7c4aa23b983d4b99af97b0/app/src/main/java/com/example/myapplication/activity/SmartSearchActivity.java#L157-L170), with filter buttons for Vehicles, Licenses, and Instructions. Clicking a result navigates to detailed views (e.g., [`VehicleDetailActivity`](app/src/main/java/com/example/myapplication/activity/VehicleDetailActivity.java)).
        - We used grammar-based search to make queries flexible and natural (e.g., "check expiry for COM-210"), aligning with the app’s theme of vehicle/license management. The tokenizer and parser ensure accurate interpretation, while [`Evaluator`](app/src/main/java/com/example/myapplication/parser/Evaluator.java) provides relevant responses. This approach improves user experience by handling varied inputs and showing precise results.

    * **Code to the Data File:**, [`instructions.json`](app/src/main/assets/instructions.json)

    * **Link to the Firebase repo:**  
      [Firebase Project Repo](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2Fpublic_vehicle)
      
    <p align="center">
    <img src="media/app_snaps/SmartSearchQuery.jpeg" width="20%" />
    <img src="media/app_snaps/SmartSearchFIlter.jpeg" width="20%" />
    <img src="media/app_snaps/SmartSearchWoFilter.jpeg" width="20%" />
    <img src="media/app_snaps/VehicleInstruction.jpeg" width="20%" />
    </p>
      <br><br>

6. **[UXUI]** Consistent design, responsive across orientations (easy)

    * **Code**:
        - [`DashboardActivity.java`](app/src/main/java/com/example/myapplication/activity/DashboardActivity.java), [`activity_signup.xml`](app/src/main/res/layout/activity_signup.xml), [`activity_dashboard.xml`](app/src/main/res/layout/activity_dashboard.xml), [`activity_profile.xml`](app/src/main/res/layout/activity_profile.xml), [`activity_add_vehicle.xml`](app/src/main/res/layout/activity_add_vehicle.xml), [`apply_license.xml`](app/src/main/res/layout/apply_license.xml)

    * **Description of feature**:  
      The app maintains a consistent design with matching colors, fonts, and styles, and adjusts seamlessly when the phone rotates.

    * **Description of your implementation**:
        - Applied across all key activities like [`DashboardActivity`](app/src/main/java/com/example/myapplication/activity/DashboardActivity.java), [`SignUp`](app/src/main/java/com/example/myapplication/activity/SignUpActivity.java), [`Profile`](app/src/main/java/com/example/myapplication/activity/ProfileActivity.java), [`AddVehicle`](app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java), and [`ApplyLicense`](app/src/main/java/com/example/myapplication/activity/ApplyLicenseActivity.java), using layouts in `res/layout/*.xml`.
        - Used `ScrollView` and `LinearLayout` to ensure flexible, scrollable layouts.
        - Consistent colors (e.g., #FAF5FF, #7E57C2) and fonts (bold for headers, `16sp` for text) throughout.
        - Dynamic sizing (`wrap_content`, `match_parent`) for adaptability in portrait and landscape.
        - Uniform icons (e.g., [`ic_profile`](app/src/main/res/drawable/ic_profile.png)) and backgrounds (e.g., [`dashboard_card_bg`](app/src/main/res/drawable/dashboard_card_bg.xml)).
        - To ensure a clean, user-friendly interface that remains consistent across the app. Responsive design allows the app to function seamlessly across different orientations and devices.

    * **Code to the Data File**: [`XML Files`](app/src/main/res/layout/)

    * **Link to the Firebase repo**: [`Firebase Project Repo`](https://console.firebase.google.com/project/gp25s1app)
      <br><br>

7. **[UIFeedback]** Feedback for all major actions (easy)
    * **Code**: - [`LoginActivity.java`](app/src/main/java/com/example/myapplication/activity/LoginActivity.java) - [`AddVehicleActivity.java`](app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java) - [`AdminActivity.java`](app/src/main/java/com/example/myapplication/activity/AdminActivity.java) - [`AdminProfileActivity.java`](app/src/main/java/com/example/myapplication/activity/AdminProfileActivity.java) - [`DashboardActivity.java`](app/src/main/java/com/example/myapplication/activity/DashboardActivity.java) - [`LicenseDetailActivity.java`](app/src/main/java/com/example/myapplication/activity/LicenseDetailActivity.java) - [`LRequestReviewActivity.java`](app/src/main/java/com/example/myapplication/activity/LRequestReviewActivity.java)
    * **Description of feature**: The app shows feedback messages for major actions like saving, errors, and form validation.
    * **Description of your implementation**:
      -  Feedback is implemented across all major activities using `Toast.makeText()` for actions like login ([`LoginActivity`](app/src/main/java/com/example/myapplication/activity/LoginActivity.java)), adding vehicles ([`AddVehicleActivity`](app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java)), admin actions ([`AdminActivity`](app/src/main/java/com/example/myapplication/activity/AdminActivity.java), [`LRequestReviewActivity`](app/src/main/java/com/example/myapplication/activity/LRequestReviewActivity.java)), and profile updates ([`AdminProfileActivity`](app/src/main/java/com/example/myapplication/activity/AdminProfileActivity.java)).
      - We used `Toast.makeText()` to display short messages for success (e.g., "Vehicle added successfully" in [`AddVehicleActivity`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5aae7afed8a9acd0fc5605c037107e638228e938/app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java#L209)), errors (e.g., "Login Failed" in [`LoginActivity`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/23820b64b4b35d533d7c4aa23b983d4b99af97b0/app/src/main/java/com/example/myapplication/activity/LoginActivity.java#L114)), and validation issues (e.g., "Please fill all fields" in [`LoginActivity`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/23820b64b4b35d533d7c4aa23b983d4b99af97b0/app/src/main/java/com/example/myapplication/activity/LoginActivity.java#L63)). In [`AdminActivity`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5a550234d98f5d162e4a3d4091e79241a7f95e5b/app/src/main/java/com/example/myapplication/activity/AdminActivity.java#L245), a custom toast with styling (purple background, bold text) is used for new license requests. Toasts are shown for `LENGTH_SHORT` or `LENGTH_LONG` based on context.
      - Feedback ensures users know the result of their actions, improving usability. Toasts are simple, non-intrusive, and effective for real-time updates, making the app more interactive and user-friendly.
    * **Link to the Firebase repo**: [`Firebase Project Repo`](https://console.firebase.google.com/project/gp25s1app)   
      <br><br>
<hr>

### Custom Features

#### Feature Category: Search-related features <br>
1. [Search-Filter] Sort and filter functionality in vehicle and license lists. (easy)

* **Code:** [`SmartSearchActivity.java`](app/src/main/java/com/example/myapplication/activity/SmartSearchActivity.java), [`Tokenizer.java`](app/src/main/java/com/example/myapplication/parser/Tokenizer.java), [`Parser.java`](app/src/main/java/com/example/myapplication/parser/Parser.java), [`Evaluator.java`](app/src/main/java/com/example/myapplication/parser/Evaluator.java), [`Query.java`](app/src/main/java/com/example/myapplication/parser/Query.java), [`activity_smart_search.xml`](app/src/main/res/layout/activity_smart_search.xml), [`SmartSearchActivityTest.java`](app/src/androidTest/java/com/example/myapplication/search/SmartSearchActivityTest.java)

* **Description of feature and implementation:**
  The app implements a smart search bar that supports grammar-based queries (e.g., "check expiry COM-210") and dynamic filtering by Vehicles, Licenses, and Instructions. The feature is implemented in `SmartSearchActivity`, where user input is parsed into tokens using `Tokenizer`. These tokens are interpreted into `Query` objects via `Parser`, which are then processed by `Evaluator` to retrieve matches from local data structures (such as `Map<String, SimpleVehicle>` and `List<License>`). Users can filter results by toggling between Vehicles, Licenses, and Instructions. The UI dynamically updates based on the selected filter, and “No results found” is displayed if no matches exist.


  The feature is tested with `JUnit 4` and `Espresso`, where test cases in [`SmartSearchActivityTest.java`](app/src/main/java/com/example/myapplication/activity/SmartSearchActivity.java) cover:
    * Vehicle and license filtering
    * Instruction matches and dialogs
    * Edge cases like empty or unknown queries
    * UI visibility and state resets

  **Exception Handling**: The app uses `try-catch` blocks to handle parsing errors and file read exceptions, displaying user-friendly fallback messages such as "Vehicle not found" when necessary.

* **Code to the Data File**: [`temp_userdata.json`](https://gitlab.cecs.anu.edu.au/your-repo-path/temp_userdata.json), [`instructions.json`](app/src/main/assets/instructions.json)

* **Link to Firebase repo**: [`Firebase Project Repo`](https://console.firebase.google.com/project/gp25s1app)   
    <p align="center">
      <img src="media/app_snaps/SmartSearchQuery.jpeg" width="20%" />
      <img src="media/app_snaps/SmartSearchFIlter.jpeg" width="20%" />
    <img src="media/app_snaps/SmartSearchWoFilter.jpeg" width="20%" />
    </p><br>

#### Feature Category: UI Design and Testing <br>
2. [UI-Test] UI Testing Using Espresso (hard)
**Feature Category:** UI Design and Testing

- **Code:** [`LoginActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/LoginActivityTest.java), [`SignUpActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/SignUpActivityTest.java), [`DashboardActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/DashboardActivityTest.java), [`ProfileActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/ProfileActivityTest.java), [`AddVehicleActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/AddVehicleActivityTest.java), [`ApplyLicenseActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/ApplyLicenseActivityTest.java), [`SmartSearchActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/SmartSearchActivityTest.java), [`QAActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/QAActivityTest.java), [`VehicleStatusActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/VehicleStatusActivityTest.java), [`MyDetailActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/MyDetailActivityTest.java), [`AdminActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/AdminActivityTest.java)

- **Description of Your Implementation:**

  * We implemented **11 Espresso UI tests** to validate critical user workflows including login, sign-up, dashboard navigation, vehicle addition, license application, profile editing, search, and chatbot interaction. These tests use `ViewMatchers`, `ViewActions`, and `ActivityScenarioRule` to simulate realistic user interaction.

  * Firebase login logic was tested using seeded accounts with `FirebaseAuth`, and asynchronous flows were controlled via `CountDownLatch` to ensure stability.

  * We integrated both `JUnit4` (unit tests in `app/src/test/`) and `Espresso` (UI tests in `app/src/androidTest/`) into a unified coverage report using **JaCoCo**.  
  Coverage reports are generated with:
  * We integrated both `JUnit4` (unit tests in `app/src/test/`) and `Espresso` (UI tests in `app/src/androidTest/`) into a unified coverage report using `JaCoCo`. Coverage reports are generated with:  
  `./gradlew jacocoTestReportDebug`  
  **Output location:** `app/build/reports/jacoco/testDebugUnitTestCoverage/`
    <br><br>

#### Feature Category: Greater Data Usage, Handling and Sophistication <br>
3. [Data-Profile] Profile page with random avatar assignment (easy)

    * **Code:** [`ProfileActivity.java`](app/src/main/java/com/example/myapplication/activity/ProfileActivity.java), [`activity_profile.xml`](app/src/main/res/layout/activity_profile.xml), [`ProfileActivityTest.java`](app/src/androidTest/java/com/example/myapplication/activity/ProfileActivityTest.java)

    * **Description of feature:**  Allows users to view and edit their profile (name, phone, DOB, etc.). A random avatar is assigned and changed on image refresh. Data is saved to Firebase and displayed from local cache.

    * **Description of your implementation:**

        * On load, profile data is fetched from local JSON and Firebase ([`Userdata/{uid}/profile`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2FUserdata)).
        * Clicking "Change Photo" cycles through avatars ([`profileimg1`](app/src/main/res/drawable/profileimg1.png) to [`profileimg8`](app/src/main/res/drawable/profileimg8.png)) using [`getResources().getIdentifier()`]().
        * Profile fields are disabled by default; clicking "Edit" enables all except email.
        * Save button writes updated data to Firebase, including avatar index. Glide is used for image display.
        * **Data structures used:** [`Map<String, Object>`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/23820b64b4b35d533d7c4aa23b983d4b99af97b0/app/src/main/java/com/example/myapplication/activity/ProfileActivity.java#L85) for profile data, and [`Hashmap`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/23820b64b4b35d533d7c4aa23b983d4b99af97b0/app/src/main/java/com/example/myapplication/activity/ProfileActivity.java#L180-L205) to get and set profile.
        * **Design patterns implemented:**
            * **Singleton:** FirebaseAuth instance
            * **Strategy:** Random avatar switching logic
            * **Facade:** [`LocalDataUtil`](app/src/main/java/com/example/myapplication/util/LocalDataUtil.java) for JSON parsing
        * **Testing:** Full UI tested via JUnit 4 + Espresso ([`ProfileActivityTest.java`](app/src/androidTest/java/com/example/myapplication/activity/ProfileActivityTest.java)), including field editing, avatar cycling, save functionality, and Firebase sync.

    * **Link to the Firebase repo:** [`Firebase User Database`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2FUserdata)  <br>
    <p align="center">
      <img src="media/app_snaps/ProfileJane.jpeg" width="20%" />
    <img src="media/app_snaps/Profile2.jpeg" width="20%" />
    <img src="media/app_snaps/Profile3.jpeg" width="20%" />
    </p>
      <br>

#### Feature Category: Process <br>
4. [Process] License application. The admin can login using the credentials, **Admin Email**: admin@govmobi.com and  **password** : admin1

    * **Code:** [`ApplyLicense.java`](https://gitlab.cecs.anu.edu.au/your-repo-path/ApplyLicense.java) [AdminActivity.java](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/AdminActivity.java?ref_type=heads) [LRequestReviewActivity.java](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/LRequestReviewActivity.java?ref_type=heads)
    * **Description of your implementation:** The license application process is designed as a multi-stage user workflow.  

     * **Form Submission:** Users enter required information e.g., name, age, contact details, payment reference in "Apply License" feature from the main dashboard. Once the details are entered, it gets sent to Firebase - [`ApplyLicenseActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/1dc762a3ccd745d379ff08e29f2df1f0ec04fce2/app/src/main/java/com/example/myapplication/activity/ApplyLicenseActivity.java)

  
     * **Admin Action:** Submitted requests are retrieved in  "Admin Page" via Firebase and displayed in a RecyclerView. Admins has following actions defined: Approve/Reject/Request for further information. [AdminActivity.java](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/1dc762a3ccd745d379ff08e29f2df1f0ec04fce2/app/src/main/java/com/example/myapplication/activity/AdminActivity.java). <br>
     <p align="center">
        <img src="media/app_snaps/ApplyLicenseForm.jpeg" width="15%" />
        <img src="media/app_snaps/ApplyLicensePayment.jpeg" width="15%" />
        <img src="media/app_snaps/ApplyLicenseStatus.jpeg" width="15%" />
        <img src="media/app_snaps/AdminRequestAction.jpeg" width="15%" />
        <img src="media/app_snaps/ReapplyLicense.jpeg" width="15%" />
    </p><br>

    * **User Action**: Once admins reviews and selects an appropriate action, its gets updated to the user in the "Apply License" page. The user can resubmit information if the admin action is Reject/ Awaiting further evidence. Then it goes to admin again after form submission for review and the multi stage process continues.
     

#### Feature Category: Privacy <br>
5. [Privacy-Visibility] Vehicle Visibility and Status Control (easy)

- **Code:** [`AddVehicleActivity.java`](app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java), [`VehicleStatusActivity.java`](app/src/main/java/com/example/myapplication/activity/VehicleStatusActivity.java)

- **Description of Feature:** Supports Public and Private visibility modes for vehicles. Users can only view their own Private vehicles and Public vehicles from all users.

- **Description of Your Implementation:**

    - In [`AddVehicleActivity`](app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java), users select visibility (`Public` or `Private`) using a [`RadioGroup`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5aae7afed8a9acd0fc5605c037107e638228e938/app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java#L200-L202). Data is saved to [`Userdata/{uid}/vehicles`](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2FUserdata), and if Public, also to [`public_vehicle` ](https://console.firebase.google.com/project/gp25s1app/database/gp25s1app-default-rtdb/data/~2Fpublic_vehicle)in Firebase.
    - In [`VehicleStatusActivity`](app/src/main/java/com/example/myapplication/activity/VehicleStatusActivity.java), only Public vehicles are fetched using [`FirebaseDatabase.getReference("public_vehicle")`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5aae7afed8a9acd0fc5605c037107e638228e938/app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java#L211-L218) and stored in an [`AVLTree`](app/src/main/java/com/example/myapplication/structures/AVLTree.java) for efficient access.

    - **Data Structures Used:**
        - [`Map<String, Object>`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5aae7afed8a9acd0fc5605c037107e638228e938/app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java#L189) for vehicle data
        - [`AVLTree`](app/src/main/java/com/example/myapplication/structures/AVLTree.java) for storing searchable public vehicles

    - **Design Patterns Implemented:**
        - **Iterator:** [`AVLTreeIterator`](app/src/main/java/com/example/myapplication/structures/AVLTreeIterator.java) for ordered traversal

    - To enforce privacy logic, only public vehicles are globally visible. Each vehicle entry has a [`visibility`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/blob/5aae7afed8a9acd0fc5605c037107e638228e938/app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java#L201) flag, checked before display. This supports secure data handling based on access level.

- **Code to the Data File:**
    - [`vehicles.json`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/assets/vehicles.json)

- **Link to the Firebase Repo:**
    - [`Firebase Project Repo`](https://console.firebase.google.com/u/1/project/gp25s1app/database/gp25s1app-default-rtdb/data)  
<br>
  <p align="center">
  <img src="media/app_snaps/MyVehiclesPage.jpeg" width="15%" />
  <img src="media/app_snaps/AddVehicleLocation.jpeg" width="15%" />
    <img src="media/app_snaps/AddVehicle.jpeg" width="15%" />
    <img src="media/app_snaps/VehicleStatusPrivate.jpeg" width="15%" />
    <img src="media/app_snaps/VehicleStatusPublic.jpeg" width="15%" />
</p>
<hr>

### Surprise Feature: AI-Powered Chat Support Using Gemini API

[Smart-Chat] AI-Powered Chat Support Using Gemini API

- **Code:** [`QAActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/QAActivity.java), [`LLMFacade.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/LLMFacade.java), [`ChatAdapter.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/adapter/ChatAdapter.java)

- **Description of Feature:**  
  An in-app chatbot allows users to ask questions about vehicles, licenses, and profiles. The assistant is powered by Google Gemini and accessed via REST APIs.

- **Description of Your Implementation:**

    - In [`QAActivity`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/QAActivity.java), a chat interface is built using `RecyclerView`, allowing users to send and view messages.
    - In [`LLMFacade`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/LLMFacade.java), the app constructs a prompt (with system context + user query) and makes an HTTP POST request to the Gemini API. JSON response is parsed and returned to the UI.`LLMFacade` abstracts all Gemini API interaction.
    - Messages are styled and displayed using [`ChatAdapter`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/adapter/ChatAdapter.java). `ChatAdapter` displays bot/user messages in a unified interface
  
    - **System Prompt Design:**
        - Includes a static instruction that frames the assistant as part of a government vehicle management system, followed by the user's query.

    - **UI Layout Files:**
        - [`activity_qaactivity.xml`](app/src/main/res/layout/activity_qaactivity.xml)
        - [`item_chat_user.xml`](app/src/main/res/layout/item_chat_user.xml)
        - [`item_chat_bot.xml`](app/src/main/res/layout/item_chat_bot.xml)

     <p align="center">
  <img src="media/app_snaps/Chatbot.jpeg" width="20%" />
</p>

#### (i) Design Pattern Justification

We implemented the **Facade design pattern** for the LLM integration to decouple the Gemini API logic from the rest of the application. This is encapsulated in the `LLMFacade` class, which acts as a single interface between the UI and the underlying LLM interaction logic.

This ensures that if we choose to replace Gemini with another LLM provider (e.g., Hugging Face or OpenAI), only the internal implementation of `LLMFacade` needs to change — not the UI or activity code.

This design promotes:
- Modularity
- Testability
- Clean separation of concerns

It also avoids tight coupling between the LLM and UI layers, making the module easy to maintain and replace without affecting the rest of the app.

#### (ii) Software License Justification

We chose the **MIT License** for our project because it is simple, permissive, and widely adopted in the open-source community.
  - The MIT License allows for reuse, modification, and distribution of our code with minimal restrictions, which is appropriate for our educational Android app.
  - It encourages collaboration and transparency while ensuring that our project remains freely available.

**Use of Gemini API:**
  - We integrated the Gemini API (Google's Large Language Model) as a third-party service.
  - We do **not** distribute or embed the model's code directly.
  - Access is done through authenticated HTTP requests using the official API.
  - This usage complies with Gemini’s Terms of Service and does not violate any license or redistribution terms.

**Data Handling:**
  - Our app also works with structured public data (e.g., vehicle registration data) stored in Firebase Realtime Database.
  - This data is accessed securely and scoped to the currently authenticated user.
One major ethical concern in our app is the potential for misinformation generated by the Gemini-powered chatbot. Since users may ask questions like “How do I pay a fine?” or “Is my license valid?”, an incorrect response could mislead them about important government processes. We mitigated this by restricting prompts to app-specific functionality only and avoiding critical advice.

The MIT License supports this usage model because:
  - It imposes no restrictions on how data is accessed or processed.
  - It only requires that the software license is clearly stated.

**Conclusion:** The MIT License ensures our code is usable and shareable while respecting ethical use of third-party services and protecting contributors from liability.

#### (iii) Ethical Concern Discussion

One major ethical concern in integrating an LLM into our app is the **potential for misinformation**.

- The Gemini model is a general-purpose LLM.
- It may generate incorrect, outdated, or misleading answers to user queries.
- In a government-focused application, such errors could erode public trust or cause confusion, especially if the LLM gives wrong advice about legal, administrative, or safety-related topics.

**Mitigation Measures:**
- The chatbot interface is designed to guide users with app-specific functionality.
- Prompt engineering is used to narrow the LLM’s scope.
- The chatbot is **not** used for critical or high-risk decisions.
- It only complements — not replaces — factual app logic.

This approach aligns with the **IEEE Code of Ethics**, which urges developers to:
- "Avoid injuring others, their property, reputation, or employment by false or malicious action."
- Ensure high-quality service delivery while being transparent about the tool’s limitations.
<br> 
<hr>

## Other functionalities
1. **Log Out**: A normal user can log out of their account using the log out button,located below the "Help & Support" feature in the main dashboard page.

2. **Admin Profile**: The admin profile can be viewed by clicking the top right profile icon in the admin dashboard page which is loaded when the admin credentials are entered. The profile details are loaded from the 'AdminData' node in the root of the firbase realtime database.

3. **Refresh**: Located to the left of the search icon in main dashboard, this feature helps the data refreh in case the user updates any information for eg.,user adds a new car.



## Testing Summary

1. **Tests for Tokenization**
    - Code: [`TokenizerTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/test/java/com/example/myapplication/parser/TokenizerTest.java) for [`Tokenizer.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/parser/Tokenizer.java)
    - Number of test cases: 6
    - Code coverage: 70%
    - Description: Validates the robustness of the tokenizer against mixed casing, irregular spacing, newlines, and empty inputs. Confirms that all tokens are correctly lowercased and split, even when input consists solely of whitespace or tabs.

2. **Tests for Search Query Parsing**
    - Code: [`ParserTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/test/java/com/example/myapplication/parser/ParserTest.java) for [`Parser.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/parser/Parser.java)
    - Number of test cases: 7
    - Code coverage: 91%
    - Description: Covers structured query extraction from tokenized inputs including status, expiry, fines, and fallback scenarios. Tests intro/help detection and default query generation for unmatched plates or unrecognized commands.

3. **Tests for Query Evaluation Logic**
    - Code: [`EvaluatorTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/test/java/com/example/myapplication/parser/EvaluatorTest.java) for [`Evaluator.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/parser/Evaluator.java)
    - Number of test cases: 7
    - Code coverage: 85%
    - Description: Ensures that structured queries return correct responses for known vehicles. Covers valid and expired vehicle states, mock fine calculations, unknown vehicles, unrecognized query types, and null query handling.

4. **Tests for AVL Tree Data Structure**
    - Code: [`AVLTreeTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/test/java/com/example/myapplication/structures/AVLTreeTest.java) for [`AVLTree.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/structures/AVLTree.java)
    - Number of test cases: 10
    - Code coverage: 89%
    - Description: Covers AVL tree balancing, insertions (including duplicates), deletions (leaf/two-child), and search operations by both VIN and license plate. Also validates in-order traversal correctness via AVLTreeIterator. Ensures that tree remains structurally consistent across all operations.

5. **Tests for License Data Model**
    - Code: [`LicenseTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/test/java/com/example/myapplication/model/LicenseTest.java) for [`License.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/model/License.java)
    - Number of test cases: 2
    - Code coverage: 100%
    - Description: Verifies constructor and all getter/setter methods of the License model. Confirms default behavior and field values for active, suspended, and learner-type licenses. Ensures Firebase-compatible model integrity.

6. **Tests for Vehicle Data Model**
    - Code: [`VehicleTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/test/java/com/example/myapplication/model/VehicleTest.java) for [`Vehicle.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/model/Vehicle.java)
    - Number of test cases: 5
    - Code coverage: ~99%
    - Description: Validates all constructors, getters, setters, and `toMap()` conversion. Includes testing of Firebase deserialization from maps and checks default visibility handling. Ensures the model accurately captures vehicle registration, maintenance, and security data.

7. **Tests for Simplified Vehicle View**
    - Code: [`SimpleVehicleTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/test/java/com/example/myapplication/model/SimpleVehicleTest.java) for [`SimpleVehicle.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/model/SimpleVehicle.java)
    - Number of test cases: 1
    - Code coverage: 100%
    - Description: Confirms constructor correctness and validates all getter methods. Ensures minimal vehicle data is correctly stored and retrieved in summary objects, supporting search results and previews.

8. **Tests for ID Generation Utility**
    - Code: [`IdGeneratorTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/test/java/com/example/myapplication/util/IdGeneratorTest.java) for [`IdGenerator.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/util/IdGenerator.java)
    - Number of test cases: 1
    - Code coverage: 100%
    - Description: Verifies that each generated ID is unique and non-null across 1000 consecutive calls. Ensures robustness of random ID generation for license or request identifiers.

9. **Tests for Database Singleton (Local + Firebase)**
    - Code: [`DatabaseSingletonTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/test/java/com/example/myapplication/util/DatabaseSingletonTest.java) for [`DatabaseSingleton.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/util/DatabaseSingleton.java)
    - Number of test cases: 4
    - Code coverage: 91%
    - Description: Validates loading and saving vehicles to local JSON file and Firebase. Confirms empty fallback behavior when the file is missing, and ensures async Firebase operations complete successfully using CountDownLatch.

10. **Tests for Firebase User Utility**
    - Code: [`FirebaseUserUtilTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/test/java/com/example/myapplication/util/FirebaseUserUtilTest.java) for [`FirebaseUserUtil.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/util/FirebaseUserUtil.java)
    - Number of test cases: 1
    - Code coverage: Partial (~60%)
    - Description: Verifies that the `fetchAndSaveUserData()` method safely handles null users without throwing exceptions. Although the actual Firebase call path is not mocked here, this test confirms crash safety for edge cases.

11. **Tests for Local Data Utility**
    - Code: [`LocalDataUtilTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/test/java/com/example/myapplication/util/LocalDataUtilTest.java) for [`LocalDataUtil.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/util/LocalDataUtil.java)
    - Number of test cases: 4
    - Code coverage: High (~90%+)
    - Description: Confirms correct parsing of `temp_userdata.json` for vehicles, licenses, and user maps. Edge cases tested include empty input, null fields, and unknown data structures. Ensures fallback to default values when necessary.

12. **Tests for LicenseAdapter Logic**
    - Code: [`LicenseAdapterLogicTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/test/java/com/example/myapplication/adapter/LicenseAdapterLogicTest.java) for [`LicenseAdapter.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/adapter/LicenseAdapter.java)
    - Number of test cases: 1
    - Code coverage: ~100% for UI binding logic
    - Description: Unit tests the static `bindToFakeViews()` method, ensuring correct data formatting and display in mock TextViews. Supports logic separation for view binding in RecyclerView and improves testability of UI-related

13. **Tests for Chat Message Model**
    - Code: [`ChatMessageTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/test/java/com/example/myapplication/model/ChatMessageTest.java) for [`ChatMessage.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/model/ChatMessage.java)
    - Number of test cases: 3
    - Code coverage: 100%
    - Description: Covers both sender types (USER and BOT) and ensures correctness of all getter methods. Confirms model behavior for real-time QA conversations.

14. **UI Tests for Login Flow**
    - Code: [`LoginActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/LoginActivityTest.java) for [`LoginActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/LoginActivity.java)
    - Number of test cases: 4
    - Code coverage: 94%
    - Description: Validates login button behavior, input validation, Firebase failure handling, and navigation to sign-up screen. Uses Espresso and logcat checks to ensure logs are triggered correctly for each scenario.

15. **UI Tests for Sign-Up Flow**
    - Code: [`SignUpActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/SignUpActivityTest.java) for [`SignUpActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/SignUpActivity.java)
    - Number of test cases: 3
    - Code coverage: 65%
    - Description: Tests include field validation and navigation to login screen. Registration success is assumed via Firebase logs. Improves test automation for edge

16. **UI Tests for Profile Editing**
    - Code: [`ProfileActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/ProfileActivityTest.java) for [`ProfileActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/ProfileActivity.java)
    - Number of test cases: 4
    - Code coverage: 93%
    - Description: Verifies that only editable fields are enabled in edit mode, and that save operation correctly updates data in Firebase and resets the UI. Also confirms functionality of the profile photo change button.

17. **UI Tests for Dashboard Interactions**
    - Code: [`DashboardActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/DashboardActivityTest.java) for [`DashboardActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/DashboardActivity.java)
    - Number of test cases: 7
    - Code coverage: 79%
    - Description: Tests all main dashboard actions including search, logout, and navigation card clicks. Uses logcat-based assertions to validate internal behavior and user flow between modules.

18. **UI Tests for Add Vehicle Workflow**
    - Code: [`AddVehicleActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/AddVehicleActivityTest.java) for [`AddVehicleActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/AddVehicleActivity.java)
    - Number of test cases: 5
    - Code coverage: 72%
    - Description: Covers input validation, GPS fallback behavior, and both private and public vehicle submission flows. Includes permission automation logic and GPS-dependent UI behavior simulation.

19. **UI Tests for Vehicle Status Activity**
    - Code: [`VehicleStatusActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/VehicleStatusActivityTest.java) for [`VehicleStatusActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/VehicleStatusActivity.java)
    - Number of test cases: 7
    - Code coverage: 82%
    - Description: Covers search field validation, UI result clearing, and public vehicle lookup. Includes log-based checks for button events and simulates report sharing via intercepted intents. Validates detail rendering for known and unknown license plates.

20. **UI Tests for Deleting Vehicles and Licenses**
    - Code: [`MyDetailActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/MyDetailActivityTest.java)  
      for [`MyVehiclesActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/MyVehiclesActivity.java)  
      and [`MyLicensesActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/MyLicensesActivity.java)
    - Number of test cases: 2
    - Code coverage: 92%
    - Description: Simulates deletion of the first listed vehicle and license by navigating through MyVehicles and MyLicenses screens. Ensures proper rendering of list views, card clicks, and delete confirmation flow with local and Firebase sync.

21. **UI Tests for Smart Search Activity**
    - Code: [`SmartSearchActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/search/SmartSearchActivityTest.java) for [`SmartSearchActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/SmartSearchActivity.java)
    - Number of test cases: 11
    - Code coverage: 92%
    - Description: Validates multi-source query parsing and UI filtering by vehicle, license, and instruction. Includes input clearing, instruction dialog display, and intent-based navigation to detail views. Covers both keyword match and fuzzy instruction search logic.

22. **UI Tests for License Application Flow**
    - Code: [`ApplyLicenseActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/ApplyLicenseActivityTest.java) for [`ApplyLicenseActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/ApplyLicenseActivity.java)
    - Number of test cases: 1
    - Code coverage: 78%
    - Description: Simulates a complete user flow for license application and payment. Tests include clicking Apply/Reapply, filling form fields, uploading transaction reference, and confirming Firebase data write and cleanup. Tear-down removes test data after submission.

23. **UI Tests for Admin Panel**
    - Code: [`AdminActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/AdminActivityTest.java) for [`AdminActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/AdminActivity.java)
    - Number of test cases: 1
    - Code coverage: 93%
    - Description: Validates login, profile icon access, and logout functionality for admin users. Simulates complete login and session handling using Espresso on a pre-configured admin account, and ensures UI element visibility and correctness.

24. **UI Tests for Chatbot QA System**
    - Code: [`QAActivityTest.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/QAActivityTest.java) for [`QAActivity.java`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/QAActivity.java)
    - Number of test cases: 3
    - Code coverage: 100%
    - Description: Tests include validating chatbot welcome message, sending user input, and confirming the bot's response appears. Espresso checks ensure toolbar rendering, input clearing, and interaction with Gemini-based LLMFacade.

### Test Coverage Summary Table

| Test Type | Tools    | Files                               | Test Cases | Coverage |
|-----------|----------|--------------------------------------|------------|----------|
| Unit      | JUnit4   | `TokenizerTest.java`                | 6          | 70%      |
| Unit      | JUnit4   | `ParserTest.java`                   | 7          | 91%      |
| Unit      | JUnit4   | `EvaluatorTest.java`                | 7          | 85%      |
| Unit      | JUnit4   | `AVLTreeTest.java`                  | 10         | 89%      |
| Unit      | JUnit4   | `IdGeneratorTest.java`              | 1          | 100%     |
| Unit      | JUnit4   | `LicenseTest.java`                  | 2          | 100%     |
| Unit      | JUnit4   | `SimpleVehicleTest.java`            | 1          | 100%     |
| Unit      | JUnit4   | `VehicleTest.java`                  | 5          | 99%      |
| Unit      | JUnit4   | `DatabaseSingletonTest.java`        | 4          | 91%      |
| Unit      | JUnit4   | `FirebaseUserUtilTest.java`         | 1          | 68%      |
| Unit      | JUnit4   | `LocalDataUtilTest.java`            | 4          | 85%      |
| Unit      | JUnit4   | `LicenseAdapterLogicTest.java`      | 1          | 94%      |
| Unit      | JUnit4   | `ChatMessageTest.java`              | 3          | 100%     |
| UI        | Espresso | `LoginActivityTest.java`            | 4          | 94%      |
| UI        | Espresso | `SignUpActivityTest.java`           | 3          | 65%      |
| UI        | Espresso | `ProfileActivityTest.java`          | 4          | 93%      |
| UI        | Espresso | `DashboardActivityTest.java`        | 7          | 79%      |
| UI        | Espresso | `AddVehicleActivityTest.java`       | 5          | 72%      |
| UI        | Espresso | `VehicleStatusActivityTest.java`    | 7          | 82%      |
| UI        | Espresso | `MyDetailActivityTest.java`         | 2          | 92%      |
| UI        | Espresso | `SmartSearchActivityTest.java`      | 11         | 92%      |
| UI        | Espresso | `ApplyLicenseActivityTest.java`     | 1          | 78%      |
| UI        | Espresso | `AdminActivityTest.java`            | 1          | 93%      |
| UI        | Espresso | `QAActivityTest.java`               | 3          | 100%     |
<br> 
<hr>


### Additional Coverage Summary and Limitations

#### JaCoCo Coverage Overview

The JaCoCo report shows that our core modules such as `parser`, `adapter`, and `model` have high line coverage. As seen below, overall code quality and branch logic are sufficiently validated across modules.

![JaCoCo Coverage Summary](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/raw/main/items/media/_testcoverage/jacoco_summary.png)

The full JaCoCo test coverage report is available at:

**Path:** `coverage-report/jacocoTestReport/html/index.html`

#### Detailed Activity-Level Coverage Breakdown

The breakdown reveals that some activity classes (especially Firebase-heavy ones) have lower coverage, which is explained in the next section.

![Activity-Level Coverage Breakdown](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/raw/main/items/media/_testcoverage/activity_coverage_breakdown.png)
#### JUnit Test Coverage

The following image shows class-level and method-level coverage for logic-intensive packages. All core data structures (`AVLTree`, `AVLTreeIterator`), parsers (`Tokenizer`, `Query`), and data models (`Vehicle`, `License`) have achieved **100% method and line coverage** via unit tests.

![JUnit Coverage Breakdown](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/raw/main/items/media/_testcoverage/junit_coverage.png)


---
### Explanation of Incomplete Coverage

The following classes were not individually tested, due to specific constraints:

#### 1. [`LRequestReviewActivity`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/activity/LRequestReviewActivity.java)
- This activity handles admin-side license approval, rejection, and evidence requests, using multiple `ValueEventListener`s and dynamic Firebase interactions.
- Full testing would require extensive mocking of real-time listeners and user-triggered state transitions, which was out of scope.

#### 2. [`LicenseRequestSimulator`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/util/LicenseRequestSimulator.java)
- This utility class generates simulated requests via `Handler.postDelayed()` and pushes them to Firebase.
- Its effects are validated through the UI (e.g., in `AdminActivity`), and isolated testing is not meaningful without mocking Firebase timing behavior.

#### 3. [`AuthHandler`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/auth/AuthHandler.java), [`AuthHandlerFactory`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/auth/AuthHandlerFactory.java), [`LoginHandler`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/auth/LoginHandler.java), [`SignUpHandler`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/main/java/com/example/myapplication/auth/SignUpHandler.java)
- These classes are part of our Factory Pattern for handling login and sign-up flows with Firebase.
- Instead of writing separate unit tests for each, we tested them indirectly through [`LoginActivityTest`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/LoginActivityTest.java) and [`SignUpActivityTest`](https://gitlab.cecs.anu.edu.au/u8030355/gp-25s1/-/blob/main/app/src/androidTest/java/com/example/myapplication/activity/SignUpActivityTest.java), which cover full usage of these handlers in production logic.

---

We intentionally focused our efforts on core app flows, parser and search logic, and major user activities. The above files represent infrastructure or simulation tools that were either indirectly covered or impractical to test under current project constraints.

## Summary of Known Errors and Bugs

1. **Card Layout Landscape Fault in Vehicle History Feature**
    - When rotating the device to landscape mode, vehicle status cards in `VehicleStatusActivity` do not wrap or resize properly. Cards overflow off-screen, requiring horizontal scrolling. This is due to missing responsiveness in `activity_vehicle_status.xml`, especially for `ConstraintLayout` or `RecyclerView` container width settings.

2. **License Approval Syncing**
    - When a user applies for a license and the admin approves it via 'LRequestReviewActivity.java', the updated license status is reflected in the admin view, but it does not automatically update or appear in the user's My Licenses page (MyLicensesActivity.java).
    - As part of future scope, you could implement logic where, upon admin approval:
The approved license is copied or moved to licenses/{uid}/licenseId. The user's 'MyLicensesActivity' will then reflect the updated license in real time.

3. **Lack of Input Validation for Structured Fields**
    - The app currently does not enforce strict validation rules on critical input fields such as license number, vehicle registration number, VIN, expiry dates, or payment IDs. This means that users can enter arbitrary or malformed values — e.g., “abc123!”, empty strings, or even emoji — without any restrictions or error feedback.
    - **Impact: **
        -Leads to inconsistent or unusable data being saved in Firebase.
        -Makes it harder for features like Smart Search or admin review to interpret or process the data correctly.
        -Compromises data integrity and could result in failed approvals, search mismatches, or confusing UI output.

4. **No Handling of Duplicate Entries**
    - Currently, the application does not check for duplicate entries when users apply for a new license or register a vehicle. This means a user can:
       - Submit multiple license applications with the same data.
       - Re-register the same vehicle multiple times under different IDs.
       - Have overlapping or redundant records in Firebase (e.g., same licenseNumber or plateNumber).

    - **Impact :**
       -Pollutes the Firebase database with redundant or conflicting data.
       -Makes it harder for the admin to verify which application is valid.
       -Breaks expectations in the “My Licenses” or “Public Vehicles” screens where uniqueness is assumed.
       -Could lead to accidental overwriting or duplication in logic where only one record should exist per user/license.
<br> 
<hr>

## Team Management

### Meeting Minutes

- [Meeting 1 – Planning & Setup](items/meeting1.md)
- [Meeting 2 – Core Feature Division](items/meeting2.md)
- [Meeting 3 – Mid-Stage Integration](items/meeting3.md)
- [Meeting 4 – Profile, Search, AVL](items/meeting4.md)
- [Meeting 5 – UI Tests, Privacy, Admin](items/meeting5.md)
- [Meeting 6 – Gemini AI Chat & QA](items/meeting6.md)
- [Meeting 7 – Polish, Freeze, Submission Checklist](items/meeting7.md)
- [Meeting 8 – Final Submission](items/meeting8.md)
  <br>
<hr>

### Conflict Resolution Protocol

During the course of development for our Android vehicle and license management app, our team established a clear and proactive **conflict resolution protocol** to ensure smooth collaboration, accountability, and timely delivery. This protocol addresses both technical and interpersonal issues, and was agreed upon by all members.

#### 1. Development & Technical Conflicts

**A. Merge Conflicts in Git**

- Each team member develops their assigned feature in a **separate Git branch** (e.g., `janvi-ui`).
- Only after completing and testing the feature locally, the branch is **merged into `main` or `pre-production`** via a pull request.
- In case of merge conflicts:
    - The member responsible for the feature resolves the conflict by rebasing or merging the latest changes from `main`.
    - If the conflict is unclear or complex, a short Google Meet call is scheduled within 24 hours to resolve it together.

**B. Overlapping Code Changes**

- Avoid parallel editing of the same files unless explicitly coordinated.
- **Update on WhatsApp** before working on or modifying shared code or features (e.g., `AddVehicleActivity.java`, `SmartSearchActivity.java`).
- If working on an existing feature, **mention on WhatsApp** to avoid duplicate work and to inform the team of ongoing changes.
- Major refactors must be proposed in the group first and approved through team consensus.

**C. Design Pattern Disagreements**

- Bring up the design concern in the group chat with reasoning, pros, and cons.
- Discuss and resolve via vote if needed.
- In case of a tie, choose the simpler and more readable option.

#### 2. Task Management & Role Conflicts

**A. Duplicate Work**

- **Notify on WhatsApp** when working on a task, especially if it's an existing feature or if there is any ambiguity regarding responsibilities.

**B. Missed Deadlines**

- If a deadline cannot be met within 48 hours:
    - Notify the team immediately.
    - The task is temporarily reassigned to maintain project flow.
- All such decisions are discussed openly in WhatsApp or during Google Meet calls.

**C. Ambiguous Responsibilities**

- Follow the original **role allocation** defined in the project plan.
- If roles overlap, break the task into subtasks and divide based on expertise.

#### 3. Team Dynamics & Communication Conflicts

**A. Inactive Member (No Update in 48 hours)**

- Step 1: Ping the member via WhatsApp.
- Step 2: Wait 24 hours.
- Step 3: If still inactive, redistribute their tasks among active members and log the issue.

**B. Disagreements or Miscommunication**

- Set up a quick 15-minute Google Meet call to clarify the issue.
- Maintain a respectful tone and share all relevant context.
- If unresolved, the team lead mediates and proposes a compromise.

**C. Repeated Non-Cooperation**

- Document the issue with examples (screenshots, logs).
- If the behavior continues:
    - Inform the tutor via edstem or email with attached documentation.
    - Proceed without relying on the non-cooperative member.

#### 4. Mitigation for Unforeseen Incidents

- **Health or Personal Emergency:**
    - Notify the group as early as possible.
    - Remaining members adjust priorities and handle critical parts temporarily.
- **Final Week Illness or Absence:**
    - Stop introducing new features.
    - Focus only on bug fixes, UI adjustments, and final testing.
    - Assign backup owners for any incomplete modules.

#### Tools Supporting This Protocol

- **Communication**: WhatsApp (daily coordination), Google Meet (meetings), Email (formal reporting)
- **Planning & Tracking**:  GitLab Issues
- **Documentation**: Shared Google Docs for logs
- **Version Control**: GitLab with branching strategy
  <br>
<hr>
