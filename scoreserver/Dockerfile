FROM python:3.12-slim

# Create app directory
WORKDIR /app

# Copy server script
COPY server.py .

# Create a non-root user for security
RUN useradd -m appuser && \
    chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose the port
EXPOSE 8000

# Run the server
CMD ["python", "server.py"]