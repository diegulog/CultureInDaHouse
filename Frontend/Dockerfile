# Based on Node.js, to build and compile the frontend
FROM node:lts-alpine AS builder

WORKDIR /app

COPY . .

RUN npm install

ENV VUE_APP_ENV=production

RUN npm run build

# Nginx, to have only the compiled app, ready for production with Nginx
FROM nginx:alpine
# Set working directory to nginx asset directory
WORKDIR /usr/share/nginx/html
# Remove default nginx static assets
RUN rm -rf ./*
# Copy static assets from builder stage
COPY --from=builder /app/dist .
# Containers run nginx with global directives and daemon off
EXPOSE 80
ENTRYPOINT ["nginx", "-g", "daemon off;"]