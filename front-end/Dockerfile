FROM node:latest

RUN mkdir -p /front-end
WORKDIR /front-end

ENV CI=true

COPY front-end /front-end
RUN npm ci

CMD [ "npm", "run", "start" ]
