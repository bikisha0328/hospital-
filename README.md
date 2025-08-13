# Hospital Management System (Java Swing)

A simple desktop application built with Java Swing and MySQL.

## Features
- Login and Reception dashboard
- Add new patient, update details, discharge
- View rooms, search rooms by availability
- View employees, departments, ambulance info

## Requirements
- Java 8+
- MySQL 5.7/8+
- JDBC MySQL connector (`mysql-connector-j`)
- `rs2xml` (DbUtils for JTable) or the bundled minimal replacement

## Database
If you already have a schema like below, the app is aligned with it:

- Tables: `login`, `patient_info`, `Room`, `department`, `EMP_INFO`, `Ambulance`
- Column names used by the app:
  - `login(ID, PW)`
  - `patient_info(ID, Number, Name, Gender, Patient_Disease, Room_Number, Time, Deposit)`
  - `Room(room_no, Availability, Price, Room_Type)`
  - `department(Department, Phone_no)`
  - `EMP_INFO(Name, Age, Phone_Number, salary, Gmail, Citizenship_Number)`
  - `Ambulance(Name, Gender, Car_name, Available, Location)`

The application loosely matches room availability (e.g., values beginning with "Avail" are treated as available) to support values like `Availabil` from your inserts.

If starting fresh, you can still import `schema.sql` as a baseline, or adjust it to the above structure.

You can override DB credentials using environment variables:
- `DB_URL` (default: `jdbc:mysql://localhost:3306/hospital_management_system?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true`)
- `DB_USER` (default: `root`)
- `DB_PASSWORD` (default: `root`)

## Build/Run (javac)
```bash
# Example classpath; adjust paths to your jars if you prefer external libraries
javac $(find . -name '*.java')
java hospital.management.system.Main
```

## Build/Run (Maven)
A minimal `pom.xml` is provided if you migrate to a standard layout. The project already includes a small replacement for `DbUtils` to avoid extra jars.

## Notes
- Place image assets under classpath at `icon/`:
  - `icon/login.png`, `icon/dr.png`, `icon/amb.png`, `icon/roomm.png`, `icon/patient.png`, `icon/updated.png`
- Windows are centered and dispose on close (except `Login` which exits the app).
- DB operations use PreparedStatements and transactions where needed.