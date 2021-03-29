# greetings-svc-a

curl "http://localhost:8080/greeting?name=sonal&sleepA=1000&sleepB=2000"

Load testing, invoke X requests per sec

url -L https://goo.gl/S1Dc3R | bash -s X  "localhost:8080/greeting"
