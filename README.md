
# Nuwe Challenge

## Overview

This project consists of implementing, correcting, and developing the different needs of Hospital AccWe regarding appointment management. Developers have recovered part of the previous project and have cleaned it to avoid potential errors, focusing especially on updating libraries and improving security.

## Main Objectives

-   Implement an efficient and secure appointment management system.
-   Develop new functionalities to enhance the user experience.
-   Correct any errors and vulnerabilities in the existing system.
-   Update libraries and technologies used to ensure system stability and security.

## Technologies Used

-   Java Spring Boot for backend development.
-   MySQL as the database to store medical appointment information.
-   Git and GitHub for version control and collaboration on the project development.

## Usage
To run this project locally, follow these steps:

1. Clone this repository: `git clone https://github.com/your-username/nuwe-desafio.git`
2. Navigate to the project directory: `cd nuwe-desafio`


### Database (MySQL) - Dockerfile.mysql

1.  Build the Docker image for the MySQL database:
```docker build -t nuwe-mysql -f Dockerfile.mysql ```
2.  Run the Docker container for the MySQL database:
```docker run -d -p 3306:3306 --name nuwe-mysql nuwe-mysql```

### Backend (Spring Boot) - Dockerfile.maven
1.  Build the Docker image for the backend:
```docker build -t nuwe-backend -f Dockerfile.maven .```
2.  Run the Docker container for the backend:
``docker run -d -p 8080:8080 --name nuwe-backend nuwe-backend``

### Entity Relation Diagram

![Entity Relation Diagram](https://github.com/nuwe-reports/65c8b55a777b7a83ba550517/assets/5830312/da9ea1c5-c96d-4503-90e8-9fb0b37dba64)

### Endpoints

Use the following endpoints to interact with the backend application:

### Appointment Controller Endpoints

#### Get All Appointments

-   **URL**: `/api/appointments`
-   **Method**: `GET`
-   **Description**: Retrieve all appointments.
-   **Response**:
    -   **Success**: `200 OK`
        -   Body: List of appointments
    -   **No Content**: `204 No Content` if no appointments are found

#### Get Appointment by ID

-   **URL**: `/api/appointments/{id}`
-   **Method**: `GET`
-   **Description**: Retrieve an appointment by ID.
-   **Parameters**:
    -   `id`: Appointment ID
-   **Response**:
    -   **Success**: `200 OK`
        -   Body: Appointment details
    -   **Not Found**: `404 Not Found` if the appointment with the specified ID is not found

#### Create Appointment

-   **URL**: `/api/appointment`
-   **Method**: `POST`
-   **Description**: Create a new appointment.
-   **Request Body**: Appointment details
-   **Response**:
    -   **Success**: `201 Created`
        -   Body: Created appointment details
    -   **Conflict**: `409 Conflict` if there is a scheduling conflict
    -   **Unprocessable Entity**: `422 Unprocessable Entity` if the request body is invalid

#### Delete Appointment by ID

-   **URL**: `/api/appointments/{id}`
-   **Method**: `DELETE`
-   **Description**: Delete an appointment by ID.
-   **Parameters**:
    -   `id`: Appointment ID
-   **Response**:
    -   **Success**: `200 OK` if the appointment is deleted successfully
    -   **Not Found**: `404 Not Found` if the appointment with the specified ID is not found

#### Delete All Appointments

-   **URL**: `/api/appointments`
-   **Method**: `DELETE`
-   **Description**: Delete all appointments.
-   **Response**:
    -   **Success**: `200 OK` if all appointments are deleted successfully

### Doctor Controller Endpoints

#### Get All Doctors

-   **URL**: `/api/doctors`
-   **Method**: `GET`
-   **Description**: Retrieve all doctors.
-   **Response**:
    -   **Success**: `200 OK`
        -   Body: List of doctors
    -   **No Content**: `204 No Content` if no doctors are found

#### Get Doctor by ID

-   **URL**: `/api/doctors/{id}`
-   **Method**: `GET`
-   **Description**: Retrieve a doctor by ID.
-   **Parameters**:
    -   `id`: Doctor ID
-   **Response**:
    -   **Success**: `200 OK`
        -   Body: Doctor details
    -   **Not Found**: `404 Not Found` if the doctor with the specified ID is not found

#### Create Doctor

-   **URL**: `/api/doctor`
-   **Method**: `POST`
-   **Description**: Create a new doctor.
-   **Request Body**: Doctor details
-   **Response**:
    -   **Success**: `201 Created`
        -   Body: Created doctor details
    -   **Unprocessable Entity**: `422 Unprocessable Entity` if the request body is invalid

#### Delete Doctor by ID

-   **URL**: `/api/doctors/{id}`
-   **Method**: `DELETE`
-   **Description**: Delete a doctor by ID.
-   **Parameters**:
    -   `id`: Doctor ID
-   **Response**:
    -   **Success**: `200 OK` if the doctor is deleted successfully
    -   **Not Found**: `404 Not Found` if the doctor with the specified ID is not found

#### Delete All Doctors

-   **URL**: `/api/doctors`
-   **Method**: `DELETE`
-   **Description**: Delete all doctors.
-   **Response**:
    -   **Success**: `200 OK` if all doctors are deleted successfully

### Patient Controller Endpoints

#### Get All Patients

-   **URL**: `/api/patients`
-   **Method**: `GET`
-   **Description**: Retrieve all patients.
-   **Response**:
    -   **Success**: `200 OK`
        -   Body: List of patients
    -   **No Content**: `204 No Content` if no patients are found

#### Get Patient by ID

-   **URL**: `/api/patients/{id}`
-   **Method**: `GET`
-   **Description**: Retrieve a patient by ID.
-   **Parameters**:
    -   `id`: Patient ID
-   **Response**:
    -   **Success**: `200 OK`
        -   Body: Patient details
    -   **Not Found**: `404 Not Found` if the patient with the specified ID is not found

#### Create Patient

-   **URL**: `/api/patient`
-   **Method**: `POST`
-   **Description**: Create a new patient.
-   **Request Body**: Patient details
-   **Response**:
    -   **Success**: `201 Created`
        -   Body: Created patient details
    -   **Unprocessable Entity**: `422 Unprocessable Entity` if the request body is invalid

#### Delete Patient by ID

-   **URL**: `/api/patients/{id}`
-   **Method**: `DELETE`
-   **Description**: Delete a patient by ID.
-   **Parameters**:
    -   `id`: Patient ID
-   **Response**:
    -   **Success**: `200 OK` if the patient is deleted successfully
    -   **Not Found**: `404 Not Found` if the patient with the specified ID is not found

#### Delete All Patients

-   **URL**: `/api/patients`
-   **Method**: `DELETE`
-   **Description**: Delete all patients.
-   **Response**:
    -   **Success**: `200 OK` if all patients are deleted successfully
### Room Controller Endpoints

#### Get All Rooms

-   **URL**: `/api/rooms`
-   **Method**: `GET`
-   **Description**: Retrieve all rooms.
-   **Response**:
    -   **Success**: `200 OK`
        -   Body: List of rooms
    -   **No Content**: `204 No Content` if no rooms are found

#### Get Room by Room Name

-   **URL**: `/api/rooms/{roomName}`
-   **Method**: `GET`
-   **Description**: Retrieve a room by room name.
-   **Parameters**:
    -   `roomName`: Room name
-   **Response**:
    -   **Success**: `200 OK`
        -   Body: Room details
    -   **Not Found**: `404 Not Found` if the room with the specified room name is not found

#### Create Room

-   **URL**: `/api/room`
-   **Method**: `POST`
-   **Description**: Create a new room.
-   **Request Body**: Room details
-   **Response**:
    -   **Success**: `201 Created`
        -   Body: Created room details
    -   **Unprocessable Entity**: `422 Unprocessable Entity` if the request body is invalid

#### Delete Room by Room Name

-   **URL**: `/api/rooms/{roomName}`
-   **Method**: `DELETE`
-   **Description**: Delete a room by room name.
-   **Parameters**:
    -   `roomName`: Room name
-   **Response**:
    -   **Success**: `200 OK` if the room is deleted successfully
    -   **Not Found**: `404 Not Found` if the room with the specified room name is not found

#### Delete All Rooms

-   **URL**: `/api/rooms`
-   **Method**: `DELETE`
-   **Description**: Delete all rooms.
-   **Response**:
    -   **Success**: `200 OK` if all rooms are deleted successfully

## License

This project is licensed under the [MIT License](LICENSE).

## Contact

If you have any questions or comments, please feel free to contact us at [salvatore356@gmail.com].

