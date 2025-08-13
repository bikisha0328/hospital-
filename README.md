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
- `rs2xml` (DbUtils for JTable) or Maven dependencies below

## Database
- Create a database named `hospital_management_system`
- Import `schema.sql` provided in the project

You can override DB credentials using environment variables:
- `DB_URL` (default: `jdbc:mysql://localhost:3306/hospital_management_system?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true`)
- `DB_USER` (default: `root`)
- `DB_PASSWORD` (default: `root`)

## Build/Run (javac)
This repo is plain Java sources. You can:

1) Compile with `javac` and include required jars on the classpath (recommended for quick try):

```bash
# Example classpath; adjust paths to your jars
JARS="/path/to/mysql-connector-j-8.x.x.jar:/path/to/rs2xml.jar"
javac -cp "$JARS" *.java
java -cp ".:$JARS" hospital.management.system.Main
```

## Build/Run (Maven)
A minimal `pom.xml` is provided. For a quick run without restructuring sources, use the exec plugin with an ad-hoc config:

```bash
mvn -q -DskipTests dependency:resolve
# Compile sources in-place
mvn -q -DskipTests org.apache.maven.plugins:maven-compiler-plugin:3.11.0:compile -DcompileSourceRoots=. -Dincludes=**/*.java
# Run Main class with classpath from dependencies and current dir
mvn -q -Dexec.classpathScope=compile -Dexec.cleanupDaemonThreads=false \
  org.codehaus.mojo:exec-maven-plugin:3.1.0:java \
  -Dexec.mainClass=hospital.management.system.Main \
  -Dexec.classpathScope=compile \
  -Dexec.additionalClasspathElements=.
```

If you prefer a standard layout, move sources to `src/main/java/` and run `mvn package` then `mvn exec:java -Dexec.mainClass=hospital.management.system.Main`.

## Libraries
- MySQL Connector/J
- `rs2xml` (DbUtils): often distributed as `rs2xml.jar` (groupId `net.proteanit`, artifactId `rs2xml`)

## Notes
- Place image assets under classpath at `icon/`:
  - `icon/login.png`, `icon/dr.png`, `icon/amb.png`, `icon/roomm.png`, `icon/patient.png`, `icon/updated.png`
- Windows are centered and dispose on close (except `Login` which exits the app).
- All database operations use PreparedStatements to prevent SQL injection and ensure resources are closed.