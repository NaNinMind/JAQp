FROM node:20-alpine
WORKDIR /react-docker-example/
COPY public/ /react-docker-example/public
COPY src/ /react-docker-example/src
COPY package.json /react-docker-example/
COPY package-lock.json /react-docker-example/
RUN npm install --force
EXPOSE 3000
CMD ["npm", "start"]
