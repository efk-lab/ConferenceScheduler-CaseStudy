# ConferenceScheduler-CaseStudy

ConferenceScheduler is a rest api for an online conference scheduler.

# Tech Stack

Java11, Spring Boot(web, test, data-mongodb, security, log4j2, validation), REST, MongoDB, Spring Security Oauth2, Maven, Junit5, Mockito, Assertj, Kubernetes

# Rest API

- UserRegistryController : Restfull Service for user registry.
  - signUp : Method for user creation
    - Endpoint: http://127.0.0.1:8081/conference-scheduler/sign-up

- CustomerController : Restfull Service for conference operations.
  - scheduleConference : Method for scheduling a conference
    - Endpoint: http://127.0.0.1:8081/conference-scheduler/conference
  - getConference : Method for retrieving a conference data
    - Endpoint: http://127.0.0.1:8081/conference-scheduler/conference-details 

# Scheduling Details
Greedy algorithm is used to schedule conference. Greedy algorithms are a class of algorithms that make locally optimal choices at each step with the hope of finding a global optimum solution. 

# Deployment

- Install minikube and kubectl

  - https://minikube.sigs.k8s.io/docs/start/
  - https://kubernetes.io/docs/tasks/tools/

- For loadbanacer run command below

  > minikube tunnel

- Run the commands below to build Image

  >docker build -t eftmk/conferencescheduler:latest -f /ConferenceScheduler/src/main/resources/Dockerfile .
  >
  >eval $(minikube docker-env)

  - Note: If you want to use the pre-build image on Docker Hub, you should update imagePullPolicy of file Kubernetes/conference-scheduler.yaml as below
     >spec:
     >
     > containers:
     >
     > name: conference-scheduler
     >   
     >   image: eftmk/conferencescheduler:latest
     >   
     >   imagePullPolicy: Always
        
- Run the command below to deploy application

  >kubectl apply -f conference-scheduler.yaml

- Run the command below to undeploy application

  >kubectl delete -f conference-scheduler.yaml
  
- For Kubernetes Dashboard, run the command below

  >minikube dasboard

- Prometheus&Grafana : Run the commands below. 

  >kubectl create namespace monitoring
  >
  >helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
  >
  >helm install k8spromethuesstack --namespace monitoring prometheus-community/kube-prometheus-stack

- Prometheus Console
  - Run the command below:
    >kubectl port-forward service/k8spromethuesstack-kube-pr-prometheus  9090:9090 -n monitoring

  - Type 127.0.0.1:9090 to browser address line to open console page.
     
- Grafana Console
  - Run the command below:
    >kubectl port-forward service/k8spromethuesstack-grafana 8080:80 -n monitoring 

  - Type 127.0.0.1:8080 to browser address line to open console page. Username/password : admin/prom-operator
 
# Logging
- Log4j framework is used for logging.

- Logs are stored on a  Kubernetes Persistent Volume.

- To access the log file: 
  - List the pods with command below and copy the app pod name
    >kubectl get pods -n conference-scheduler-k8s
  - Then update the command below with PodName and run
    >kubectl exec <AppPodName> -n conference-scheduler-k8s -- tail -1000f /mnt/k8s/log/ConferenceScheduler.log

# DB Access
- To access DB:
    - List the pods with command below and copy the MongoDB pod name
      >kubectl get pods -n conference-scheduler-k8s
    - Then update the command below with PodName and run
      >kubectl exec -it <MongDBPodName>-n conference-scheduler-k8s -- mongosh -u user -p user
- Run the commands below to list the DB collections
    >db.conference.find();
    >
    >db.user.find();

# Rest Api Test
-  Find the test requests xml under /Postman directory and import it from Postman. (Security configurations are ready)

- Spring Security OAuth2 is used for Endpoint security. To make rest api calls, firstly call the UserRegistryServis via http://127.0.0.1:8081/conference-scheduler/sign-up and register an user with email and password.

- After that, all rest calls must be added an access token.
  
- The registered email and password is used to create access token:
  - Postman ->New request -> Authorization tab ->

        Type = Oauth2
    
        Grant type = password
    
        Access Token URL = http://localhost:8081/oauth/token
    
        Client ID = ConferenceSchedulerClient
    
        Client Secret = secret
    
        Username = user@conferencescheduler.com (Note:Registered in the previous step)
    
        Password = ••••••• (Note:Created in the previous step)
    
        Scope = write
    
        Client Authentication = Send as Basic Auth Header
    
  - Press "Get New Access Token". Now token is created and added to request.
  - Each access token validity period is 12 hours.

- AccessToken should be added endpoints below before calling them:
    - Endpoint: http://127.0.0.1:8081/conference-scheduler/conference
    - Endpoint: http://127.0.0.1:8081/conference-scheduler/conference-details 

# Unit Test
- Test Frameworks: Junit5, Mockito, Assertj 
- Coverage = %50
