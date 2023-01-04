## Fake OAuth2 Server

This project was created with the objective of demonstrating a performance degradation when using native compilation with graalvm in a scenario where we have signatures with rsa

- GIthub issue https://github.com/oracle/graal/issues/5697

### Simple request example

```sh
curl --location --request GET 'localhost:8080/oauth/token'
```

### Build images

Jit Image

```sh
mvn clean package
docker build -t fake-oauth-jit -f dockerfile-jit .
```

Native image

```sh
mvn clean package -Dpackaging=native-image
docker build -t fake-oauth-native -f dockerfile-native .
```

### Tests

#### JIT Without limitation


Execution:

```sh
docker run -p 8080:8080 -e CERT_LOCATION=/cert/key1.pem fake-oauth-jit
k6 run --vus 100 --duration 60s performance-test/k6-get-token.js
```

Result:

```
Run 1 9221.79/s 
Run 2 9362.92/s
Run 3 9353.73/s
```

#### Native Without limitation

Execution:

```sh
docker run -p 8080:8080 -e CERT_LOCATION=/cert/key1.pem fake-oauth-native
k6 run --vus 100 --duration 60s performance-test/k6-get-token.js
```

Result:

```
Run 1 3590.95/s 
Run 2 3589.23/s
Run 3 3589.47/s
```

Production limit

####  JIT - Docker 300 milicore

Execution:

```sh
docker run -p 8080:8080 -e CERT_LOCATION=/cert/key1.pem --cpus=0.3 fake-oauth-jit
k6 run --vus 100 --duration 60s performance-test/k6-get-token.js
```

Result:

```
Run 1 188.36/s #ignored on average
Run 2 265.62/s
Run 3 314.46/s
```

#### Native - Docker 300 milicore

Execution:

```sh
docker run -p 8080:8080 -e CERT_LOCATION=/cert/key1.pem --cpus=0.3 fake-oauth-native
k6 run --vus 100 --duration 60s performance-test/k6-get-token.js
```

Result:

```
Run 1 53.66/s 
Run 2 54.69/s
Run 3 54.48/s
```

### PC Settings

- CPU: Ryzen 9 7950X
- Mem: 16GB 6200MHZ (x2)
