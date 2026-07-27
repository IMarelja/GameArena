# Setup & Usage

## 1. Start SonarQube
```
  docker compose up -d
  # Wait ~60 seconds for it to start, then open http://localhost:9000 (login: admin / admin, you'll be prompted to change the password).
  ```
##  2. Generate a token
  - Login again
  - Go to My Account → Security → Generate Tokens
  - Give it a name and copy the token

 ## 3. Run the scan
```
  mv .env.example .env
  # add or edit the property "SONAR_TOKEN=" to add your token with your favorite text editor
  ./scan.sh
  ```
  Results appear at http://localhost:9000/projects for both GameArenaApi and GameArenaWebapp.
