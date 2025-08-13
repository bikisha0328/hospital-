-- Hospital Management System - MySQL schema and seed data
-- Safe to run multiple times (drops and recreates tables)

-- Ensure UTF8MB4 and predictable behavior
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE DATABASE IF NOT EXISTS hospital_management_system
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
USE hospital_management_system;

-- Drop in dependency order
DROP TABLE IF EXISTS Patient_Info;
DROP TABLE IF EXISTS room;
DROP TABLE IF EXISTS department;
DROP TABLE IF EXISTS EMP_INFO;
DROP TABLE IF EXISTS Ambulance;
DROP TABLE IF EXISTS login;

-- login table (used by Login.java)
CREATE TABLE login (
  ID VARCHAR(30) NOT NULL PRIMARY KEY,
  PW VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- room table (used by Room.java, SearchRoom.java, NEW_PATIENT.java, patient_discharge.java)
-- NOTE: room_no is INT so string-less comparisons in code work (q1 in NEW_PATIENT.java)
CREATE TABLE room (
  room_no INT NOT NULL PRIMARY KEY,
  Availability ENUM('Available','Occupied') NOT NULL DEFAULT 'Available',
  Price INT NOT NULL,
  Room_Type VARCHAR(50) NOT NULL,
  INDEX idx_room_availability (Availability)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Patient_Info table (case and column names exactly as Java expects)
-- Using VARCHAR for Time to match the Java Date.toString() format stored by NEW_PATIENT.java
CREATE TABLE Patient_Info (
  ID VARCHAR(20) NOT NULL,
  Number VARCHAR(40) NOT NULL,
  Name VARCHAR(30) NOT NULL,
  Gender VARCHAR(30) NOT NULL,
  Patient_Disease VARCHAR(30) NOT NULL,
  Room_Number INT NOT NULL,
  Time VARCHAR(100) NOT NULL,
  Deposite INT NOT NULL,
  PRIMARY KEY (Number),
  INDEX idx_patient_name (Name),
  INDEX idx_patient_room (Room_Number),
  CONSTRAINT fk_patient_room FOREIGN KEY (Room_Number)
    REFERENCES room(room_no)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- department table (used by Department.java)
CREATE TABLE department (
  Department VARCHAR(100) NOT NULL PRIMARY KEY,
  Phone_no VARCHAR(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- EMP_INFO table (used by Employee_info.java)
-- Keep exact columns (no extra id) to preserve UI alignment
CREATE TABLE EMP_INFO (
  Name VARCHAR(20) NOT NULL,
  Age VARCHAR(20),
  Phone_Number VARCHAR(20),
  salary VARCHAR(20),
  Gmail VARCHAR(30),
  Citizenship_Number VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Ambulance table (used by Ambulance.java)
CREATE TABLE Ambulance (
  Name VARCHAR(30) NOT NULL,
  Gender VARCHAR(10),
  Car_name VARCHAR(50),
  Available ENUM('Available','Not Available') DEFAULT 'Available',
  Location VARCHAR(30)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;

-- Seed data ---------------------------------------------------------------

INSERT INTO login (ID, PW) VALUES
  ('akshyta','123456789')
ON DUPLICATE KEY UPDATE PW = VALUES(PW);

-- Rooms: availability fixed to 'Available' (not misspelled), INT room_no, INT Price
INSERT INTO room (room_no, Availability, Price, Room_Type) VALUES
  (100,'Available',500,'G Bed 1'),
  (101,'Available',500,'G Bed 2'),
  (102,'Available',500,'G Bed 3'),
  (103,'Available',500,'G Bed 4'),
  (200,'Available',1500,'Private Room'),
  (201,'Available',1500,'Private Room'),
  (202,'Available',1500,'Private Room'),
  (203,'Available',1500,'Private Room'),
  (300,'Available',3500,'ICU Bed 1'),
  (301,'Available',3500,'ICU Bed 2'),
  (302,'Available',3500,'ICU Bed 3'),
  (303,'Available',3500,'ICU Bed 4'),
  (304,'Available',3500,'ICU Bed 5'),
  (305,'Available',3500,'ICU Bed 6')
ON DUPLICATE KEY UPDATE
  Availability = VALUES(Availability),
  Price = VALUES(Price),
  Room_Type = VALUES(Room_Type);

-- Departments
INSERT INTO department (Department, Phone_no) VALUES
  ('Surgical department','123456789'),
  ('Nursing department','123456789'),
  ('Operation theater complex(OD)','123456789'),
  ('Paramedical department','123456789')
ON DUPLICATE KEY UPDATE Phone_no = VALUES(Phone_no);

-- Employees
INSERT INTO EMP_INFO (Name, Age, Phone_Number, salary, Gmail, Citizenship_Number) VALUES
  ('Doctors=1','30','123456789','5000','gr@gmail.com','1239876543'),
  ('Doctors=2','25','123456789','5000','gr@gmail.com','1239876543');

-- Ambulance
INSERT INTO Ambulance (Name, Gender, Car_name, Available, Location) VALUES
  ('av','Male','ZEN','Available','kathmandu');

-- Compat: create a case-insensitive view alias for `Room` (some code uses `Room`)
DROP VIEW IF EXISTS Room;
CREATE VIEW Room AS SELECT * FROM room;

-- Enhancement: discharge audit
CREATE TABLE IF NOT EXISTS discharge_audit (
  audit_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  patient_number VARCHAR(40) NOT NULL,
  name VARCHAR(30) NOT NULL,
  room_number INT NOT NULL,
  in_time VARCHAR(100) NOT NULL,
  out_time DATETIME NOT NULL,
  deposite INT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TRIGGER IF EXISTS trg_patient_discharge_log;
DELIMITER $$
CREATE TRIGGER trg_patient_discharge_log
BEFORE DELETE ON Patient_Info
FOR EACH ROW
BEGIN
  INSERT INTO discharge_audit (patient_number, name, room_number, in_time, out_time, deposite)
  VALUES (OLD.Number, OLD.Name, OLD.Room_Number, OLD.Time, NOW(), OLD.Deposite);
END$$
DELIMITER ;