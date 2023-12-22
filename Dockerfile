# Use the official Python image as the base image
FROM python:3.11-slim

# Set the working directory inside the container
ENV PYTHONUNBUFFERED True

ENV APP_HOME /app

ENV PORT 8080

WORKDIR $APP_HOME

COPY . ./

RUN pip install --no-cache-dir -r requirements.txt

# Expose the port that Flask will run on
EXPOSE $PORT

# Run the Flask application without Gunicorn
CMD ["python", "app.py"]
