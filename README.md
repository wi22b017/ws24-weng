# Web Engineering Project

Welcome to the **WENG Project**! This guide will walk you through setting up the project on your local machine, including starting the frontend, backend, and populating the database with sample data.

---

## Project Startup

### Frontend Setup
1. **Open the Project:**
    - Open the project in an IDE of your choice (recommended: IntelliJ IDEA).
    - Navigate to the `fweng` subdirectory in the console:
      ```bash
      cd fweng
      ```
2. **Install Dependencies:**
    - Run the following command to install all required dependencies:
      ```bash
      npm install
      ```
3. **Start the Frontend:**
    - Run the following command to start the development server:
      ```bash
      npm run serve
      ```
    - The frontend should now be running on your machine at [http://localhost:8080](http://localhost:8080).

---

### Backend Setup

#### 1. Prerequisites
- Ensure you have **Docker** or **Docker Desktop** installed on your system.

#### 2. Start Required Containers
- Navigate to the `bweng` folder and locate the `docker-compose.yml` file.
- Open the file in IntelliJ IDEA. You will see small **play buttons** next to each service definition.
- Start the necessary containers (`db` and `minio`) by clicking the play buttons.

#### 3. Start the Backend
- Open the file `src/main/java/at.fhtw.bweng/BwengApplication.java`.
- Locate the **main method** and press the **play button** next to it.
- The backend should now be running on your local machine.

---

## Populating the Database

### Configure the Data Source
1. Open the **Data Sources and Drivers** dialog:
    - In IntelliJ IDEA, navigate to the **Database tool window**:  
      `View | Tool Windows | Database`.
    - Click the **Data Source Properties** button or use the shortcut `⌘Cmd0;` to open the dialog.

2. Add a New Data Source:
    - Click the **plus button** and select **MariaDB** as the data source type.
    - Enter the following connection details:
        - **Name:** `MariaDB`
        - **Host:** `localhost`
        - **Port:** `3306`
        - **Username:** `springuser`
        - **Password:** `springpw`
    - Click **Test Connection**. If the connection succeeds, click **Apply** and **OK**.

### Run the SQL Script
1. Navigate to the `src/main/resources` directory and locate the `data.sql` file.
2. Open the file in IntelliJ IDEA.
3. At the top of the file you will find on the right side two drop-downs:
    - Ensure the **data source** is set to the one you configured earlier (e.g., `MariaDB`).
    - Select the `spring` schema.
4. Execute the script:
    - Highlight all lines of the file.
    - Click the **play button** on the top left or right-click the selection and choose **Run 'data.sql'**.

This will populate the database with the required data.

---

## Using the Application

Once the setup is complete, you can access the application via the frontend in your browser at:  
[http://localhost:8080](http://localhost:8080)

### Predefined Users
You can log in using the following predefined user credentials:

| **Email Address**         | **Username** | **Password**  | **Role**   |
|---------------------------|--------------|---------------|------------|
| john.doe@example.com      | johndoe      | password1     | USER       |
| jane.smith@example.com    | janesmith    | password2     | USER       |
| emily.johnson@example.com | emily        | password3     | USER      |
| admin@example.com         | admin        | adminpassword | ADMIN      |


---

Enjoy using the WENG Project!
