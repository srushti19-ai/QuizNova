# Quiz Management System

A desktop quiz application built with Java Swing, JDBC and SQLite, aligned with the supplied project proposal.

## Features
- Student enters a name and attempts multiple-choice questions.
- Questions and correct answers are loaded from SQLite.
- Automatic scoring and immediate feedback.
- Results are stored with date/time and can be viewed in Result History.
- The SQLite database is created automatically on first run.

## Requirements
- JDK 17 or newer
- Eclipse IDE with Maven support
- Internet access on the first Maven build so Maven can download SQLite JDBC

## Run in Eclipse
1. Extract this ZIP to a folder.
2. Open Eclipse.
3. Choose **File > Import > Maven > Existing Maven Projects**.
4. Browse to the extracted `QuizManagementSystem` folder and select it.
5. Click **Finish** and wait for Maven to download dependencies.
6. Open `src/main/java/com/quiz/app/QuizManagementSystem.java`.
7. Right-click the file > **Run As > Java Application**.
8. Enter a student name, start the quiz, answer the questions, and submit.
9. The file `quiz_management.db` is created in the project's working directory.

## If Eclipse shows a red error
- Right-click the project > Maven > Update Project.
- Ensure Eclipse is using JDK 17+ (Window > Preferences > Java > Installed JREs).
- If Maven cannot download dependencies, check internet access and retry Maven > Update Project.

## Submission reminder
The portal screenshot requires a shareable Google Drive link to a PDF/PPT. The project video link is marked optional. Upload your final presentation PDF/PPT to Drive, set access to **Anyone with the link – Viewer**, then paste the share link in the portal.
