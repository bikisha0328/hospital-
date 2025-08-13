### Hospital Management System - Database Setup

- Requires MySQL 8+ running locally.
- Java app connects to: `jdbc:mysql://localhost:3306/hospital_management_system` with user `root` and password `root` (see `hospital.management.system.conn`).

#### 1) Create schema and seed data

```bash
mysql -u root -proot < /workspace/hms_schema.sql
```

If your MySQL root password differs, update the command accordingly or edit `conn.java`.

#### 2) Notes on schema choices
- Table/column names and cases match the Java code exactly: `Patient_Info`, `room`, `department`, `EMP_INFO`, `Ambulance`, `login`.
- `room.room_no` and `Patient_Info.Room_Number` are `INT` to support numeric comparisons already coded.
- `Patient_Info.Time` is `VARCHAR(100)` because the UI stores `new Date().toString()`.
- `Patient_Info.Deposite` is `INT` to safely compute pending amounts.
- `room.Availability` uses only `Available`/`Occupied` values expected by the UI.

#### 3) Optional improvements (future)
- Use prepared statements in Java to avoid SQL injection.
- Normalize employees, departments, and patients with proper IDs.
- Store timestamps with `DATETIME` instead of raw strings; add audit tables for discharges/bills.