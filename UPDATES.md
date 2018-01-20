# UPDATES

### First Patch
- Switched to a springboot app to avoid unnecessary boiler plate code mainly in the data layer.
- Created a Role enum class to avoid arbitrary strings in roles.
- Initial version of User model.
- Refactored test package names so that springboot can autowire dependencies.
- Complete unit testing suite. No UserService tests since no logic is housed there.
- UserRepository test for custom implementations and for validating unique constraint for emails is honored.
- Comment out UserIntegrationTest.java since no longer valid.
- Implement Validation on User model for new and existing when post and put.
- Refactor api endpoints to align with RESTful standards.
- Add mvnw to ease the maven dependency requirements for new engineers.
- Implement lombok annotations to avoid pojo boiler plate.
- Implement Builder pattern via lombok for User model.
- Add springfox swagger ui dependency for API discovery.
- Make user emails a unique constraint in the DB.
- Package by feature rather than layer.

### Second Patch
- Implement CompletableFuture for async requests and ETag header for concurrency.
  -  CompletableFuture allows for more concurrent requests by releasing request threads and creating Worker threads that return the result asynchronously.
  -  Utilizing ETag with a Version field on user objects implements optimistic locking to help ensure users are not working with stale data.
  -  Update tests for repository and controller.

### Third Patch
- Dockerize application
  - Update README with docker usage
- Eager load Roles on user
- Add docker dependency to pom for main class detection