# ConferenceScheduler-CaseStudy

ConferenceScheduler is a rest api for an online conference scheduler.

# Tech Stack

Java11, Spring Boot(web, test, data-mongodb, security, log4j2, validation), REST, MongoDB, Spring Security Oauth2, Maven, Junit5, Mockito, Assertj, Kubernetes

# Rest API

- UserRegistryController : Restfull Service for user registry.
  - signUp : Method for user creation

- CustomerController : Restfull Service for conference operations.
  - scheduleConference : Method for scheduling a conference
  - getConference : Method for retrieving a conference data

# Deployment

- Install minikube and kubectl

  - https://minikube.sigs.k8s.io/docs/start/
  - https://kubernetes.io/docs/tasks/tools/

- For loadbanacer run command below

  > minikube tunnel

- Build Image

  >docker build -t bookworm:latest -f /eclipse-workspace/BookWorm/src/main/resources/Dockerfile .
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
        
- Deploy Applications

  >kubectl apply -f conference-scheduler.yaml

- Undeploy Applications

  >kubectl delete -f conference-scheduler.yaml
  
- Kubernetes Dashboard

  >minikube dasboard

- Prometheus&Grafana 

  >kubectl create namespace monitoring
  >
  >helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
  >
  >helm install k8spromethuesstack --namespace monitoring prometheus-community/kube-prometheus-stack

- Login to Prometheus console
   >kubectl port-forward service/k8spromethuesstack-kube-pr-prometheus  9090:9090 -n monitoring

  - Type 127.0.0.1:9090 to browser address line to open console page.
     
- Login to Grafana Console
  >kubectl port-forward service/k8spromethuesstack-grafana 8080:80 -n monitoring 

  - Type 127.0.0.1:8080 to browser address line to open console page. Username/password : admin/prom-operator
