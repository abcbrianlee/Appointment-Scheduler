<h1>Appointment Scheduler</h1>

The Appointment Scheduler is a Java/FXML/Scenebuilder application designed to simplify your contact and appointment management.  This application allows users to securely log in using a MySQL database and seamlessly handle various tasks, such as adding, creating, updating and deleting contact users and appointments. It has the ability to assign appointments and generate insightful reports.   

# <span style="font-size: 16px;">Technologies Used</span>
* <b>Java</b>: The core programming language used for the application's logic.<br>
* <b>FXML</b>: A markup language for designing the application's user interface.<br>
* <b>Scenebuilder</b>: A visual layout tool for designing FXML-based interfaces.
* <b>MySQL</b>: A powerful database used to store information
## Project Description
The Appointment Scheduler leverages the strength of MySQL to store login credentials securely.  The application is able to track the user's location and automatically adjust the timezone, ensuring that the displayed timezone on the login page are accurate and convenient.  Furthermore, the application is able to switch between English and French based on the user's language preference.<br>
<br>The Main Menu offers an interface with two primary sections: Customers and Appointments.  Users can search through appointments by day, week, or month, enabling quick access to the desired information.  Adding or updating a customer enables changes to the interface as well as the database.  Deletion of customers is allowed, provided that no appointments are associated with them. If an appointment is linked to a customer upon deletion, the application will display and error message, preventing accidental removal of critical information and requiring the user to first delete the appointment.<br>
<br>Users are also able to manage appointments as well by either adding, updating or deleting appointments, all while adhering to business hours.  Appointments can only be scheduled between 8am and 7pm centrail time on weekdays.  The application enforces the non-overlapping of appointments, ensuring a smooth and organized schedule. Additionally, a helpful 15-minute reminder will prompt users before their next appointment.  When adding an appointment, users must associate it with a specific customer, further streamlining the process. Although appointment times are adjusted to display in the user's timezone, the are stored in the MySQL database inUTC time, ensuring accuracy and consistency across different time zones.<br>
<br>In-depth reports provide valuable insights into appointments and customer.  Users can generate reports using various queries, such as sorting by appointment type, contact or country(France, US, UK).  These reports serve as a powerful tool for generating future analysis and planning. Furthermore, the Appointment Scheduler records all login attempts, successful or unsuccessful, in a dedicated login_activity.txt file, capturing important information like usernames, passwords, and timestamps for security monitoring.

<h2>Instructions</h2>
To run the Appointment Scheduler on your local machine, follow these steps:<br>
<br>1. Clone this repository to your local machine<br>
2. Open the project in your preferred Java IDE<br>
3. Set Main.java located at src/main/java/Main as the entry point.<br>
4. Build and run the application<br>
5. Enter 'test' as username and password (case-sensitive)<br>
6. Start managing contacts and appointments with ease!<br>

<h2>Contributions</h2>
Contributions to the Appointment Scheduler are welcome! If you have any suggestions, bug reports, or feature requests, please feel free to submit an issue or a pull request.
