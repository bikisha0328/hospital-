-- Schema for Hospital Management System
CREATE DATABASE IF NOT EXISTS hospital_management_system;
USE hospital_management_system;

-- Basic login table
CREATE TABLE IF NOT EXISTS login (
  ID VARCHAR(50) PRIMARY KEY,
  PW VARCHAR(100) NOT NULL
);

-- Room table
CREATE TABLE IF NOT EXISTS room (
  room_no VARCHAR(20) PRIMARY KEY,
  Availability ENUM('Available','Occupied') NOT NULL DEFAULT 'Available',
  Price INT NOT NULL DEFAULT 0,
  Bed_Type VARCHAR(50) NOT NULL DEFAULT 'Single'
);

-- Patient Info
CREATE TABLE IF NOT EXISTS Patient_Info (
  ID VARCHAR(50) NOT NULL,                -- ID type (Citizenship, Voter Id, etc.)
  number VARCHAR(50) NOT NULL,            -- Document number (treated as unique for demo)
  Name VARCHAR(100) NOT NULL,
  Gender VARCHAR(10) NOT NULL,
  Disease VARCHAR(100) NOT NULL,
  Room_Number VARCHAR(20) NOT NULL,
  Time VARCHAR(100) NOT NULL,
  Deposite INT NOT NULL DEFAULT 0,
  PRIMARY KEY (number),
  CONSTRAINT fk_patient_room FOREIGN KEY (Room_Number) REFERENCES room(room_no)
);

-- Department
CREATE TABLE IF NOT EXISTS department (
  Department VARCHAR(100) PRIMARY KEY,
  Phone_Number VARCHAR(30) NOT NULL
);

-- Employees
CREATE TABLE IF NOT EXISTS EMP_INFO (
  Name VARCHAR(100) PRIMARY KEY,
  Age INT NOT NULL,
  Phone_Number VARCHAR(30) NOT NULL,
  Salary INT NOT NULL,
  Gmail VARCHAR(100) NOT NULL,
  Citizenship_Number VARCHAR(60) NOT NULL
);

-- Ambulance
CREATE TABLE IF NOT EXISTS Ambulance (
  Name VARCHAR(100) PRIMARY KEY,
  Gender VARCHAR(10) NOT NULL,
  Car_name VARCHAR(100) NOT NULL,
  Available ENUM('Yes','No') NOT NULL DEFAULT 'Yes',
  Location VARCHAR(100) NOT NULL
);

-- Seed admin user
INSERT INTO login (ID, PW) VALUES ('admin', 'admin') ON DUPLICATE KEY UPDATE PW=VALUES(PW);

-- Seed sample rooms
INSERT INTO room (room_no, Availability, Price, Bed_Type) VALUES
 ('101','Available',1000,'Single'),
 ('102','Available',1200,'Double')
ON DUPLICATE KEY UPDATE Availability=VALUES(Availability), Price=VALUES(Price), Bed_Type=VALUES(Bed_Type);