# Paketto 
[![CI](https://github.com/cs481-ekh/f22-gneiss/actions/workflows/ci.yml/badge.svg)](https://github.com/cs481-ekh/f22-gneiss/actions/workflows/ci.yml)

A packet compiler made for Boise State's Mechanical and Biomedical Engineering Department

## Setup

To run Packetto locally, you have two options:
1. Run everything through the docker-compose.yaml
2. Run each piece individually

Option 1 is much quicker to set up for the first time, but Option 2 is more convenient for development.

### Running through Docker-Compose

First, you'll need Docker to be installed on your local machine. Run this command in any bash terminal.

```
./build.sh
```

This will create the necessary Docker image for the project. To create a container from that image, run:
```
docker-compose -f docker-compose.yaml --env-file production.env up
```

`docker-compose.yaml` consumes the `production.env` environment file. If you'd like to change which ports are exposed as what, feel free to edit it.

### Running individually

This should be the preferred method of running the project during active development.

#### Front-End

To run the front-end, you'll want to spin up a development server. You'll need NPM of at least version 8.19.1

The commands for starting up a development server are as follows:
```
cd front-end
npm ci
npm run start
```

The front-end is already configured to properly send requests to the back-end, if that's running locally as well. See this line from `package.json`:
```json
  "proxy": "http://localhost:8080",
```
If for whatever reason you'd like to change which port the back-end uses locally, make sure you change this as well.

#### Back-end

To run the back-end, you're going to need OpenJDK of at least version 19, and Maven of at least 3.8.6.

Create a jar file with Maven, and then run that Jar file.
```
cd back-end
mvn package
java -jar target/PacketCompiler-0.0.1-SNAPSHOT.jar    
```

If you'd like to connect your locally running back-end to a MySQL server, use `development.env` as your list of which environment variables need to be set. Add these variables to your `zshrc` or `bashrc` files if on Linux or MacOs, or set the environment variables manually if you're on Windows.

The easiest MySQL database to connect to is the one that's running with Docker-Compose! That's why `production.env` has the backend port set to `8081` instead of the default `8080`, so you can run both at the same time. The values in `development.env` are already configured to work with the Docker-Composed MySQL container.

