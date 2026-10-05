#!/bin/bash

# Load environment variables from .env (required)
if [ ! -f .env ]; then
  echo "Error: .env file not found. Copy .env.template to .env and fill in values."
  exit 1
fi
set -a
. ./.env
set +a

# Start backend
cd pokemon
./mvnw spring-boot:run &
BACKEND_PID=$!

# Start frontend
cd ../frontend
npm install
npm run dev &
FRONTEND_PID=$!

# Print URLs
echo "Backend: http://localhost:8080"
echo "Frontend: http://localhost:5173"
echo "H2 Console: http://localhost:8080/h2-console"

# Cleanup on exit
trap "kill $BACKEND_PID $FRONTEND_PID" EXIT

# Wait
wait $BACKEND_PID $FRONTEND_PID