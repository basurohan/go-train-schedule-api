# Go Train Schedule API

### Description
A spring boot microservice API that provides a simplified train timetable with weekday train leaving Union Station.

### To run service locally
1. Download the zip file locally from Github - (https://github.com/basurohan/go-train-schedule-api.git)
2. Unzip and navigate to the project folder
3. Run ./mvnw spring-boot:run
4. Alternatively - run from IDE

### Technical
1. Application runs locally on port 8080.
2. API documentation via swagger can be accessed - (http://localhost:8080/swagger-ui.html)
3. Test data is already preloaded into an in-memory H2 DB.
4. H2 console can be accessed at (http://localhost:8080/h2)
5. Connection details can be found in file - application.yml