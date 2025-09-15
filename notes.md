# Create DB user
CREATE USER 'jobito_user'@'%' IDENTIFIED BY 'XXXXX'; \
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE  ON jobito.* TO 'jobito_user'@'%'; \
(or everything GRANT ALL PRIVILEGES ON *.* TO 'jobito_user'@'localhost' WITH GRANT OPTION;)


# Build and deploy docker
### 1. Build 

*** Update index jobito-be:xxx ***
```bash
./gradlew clean shadowJar 
docker build .
docker build . -t jobito-be:latest
aws ecr get-login-password --region eu-central-1 | docker login --username AWS --password-stdin 427201564576.dkr.ecr.eu-central-1.amazonaws.com
docker tag jobito-be:latest 427201564576.dkr.ecr.eu-central-1.amazonaws.com/jobito-be:3 
docker push 427201564576.dkr.ecr.eu-central-1.amazonaws.com/jobito-be:3
```  

### 2-a Deploy to AWS docker
```bash
aws ecr get-login-password --region eu-central-1 | docker login --username AWS --password-stdin 427201564576.dkr.ecr.eu-central-1.amazonaws.com
docker tag jobito-be:latest 427201564576.dkr.ecr.eu-central-1.amazonaws.com/jobito-be:3 
docker push 427201564576.dkr.ecr.eu-central-1.amazonaws.com/jobito-be:3
```  

### 2-b Deploy to Local docker
```bash
docker run --env-file .env -p 2000:2000 jobito-be
```  
