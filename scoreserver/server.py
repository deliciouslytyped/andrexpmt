#GPT >_>
import sqlite3
from http.server import HTTPServer, BaseHTTPRequestHandler
import json


def create_database():
    conn = sqlite3.connect('scores.db')
    c = conn.cursor()
    c.execute('''CREATE TABLE IF NOT EXISTS scores (name TEXT, score INTEGER)''')
    conn.commit()
    conn.close()


class ScoreHandler(BaseHTTPRequestHandler):
    def do_POST(self):
        if self.path == '/submit_score':
            content_length = int(self.headers['Content-Length'])
            post_data = self.rfile.read(content_length)
            data = json.loads(post_data)

            print(f"Adding score submission ${data['name']} ${data['score']}")
            # Insert data into SQLite database
            conn = sqlite3.connect('scores.db')
            c = conn.cursor()
            c.execute("INSERT INTO scores (name, score) VALUES (?, ?)", (data['name'], data['score']))
            conn.commit()
            conn.close()

            # Send response
            self.send_response(200)
            self.end_headers()
            self.wfile.write(b"Score submitted successfully")

    def do_GET(self):
        if self.path == '/scores':
            conn = sqlite3.connect('scores.db')
            c = conn.cursor()
            c.execute("SELECT name, score FROM scores ORDER BY score DESC")
            scores = c.fetchall()
            scores = [{"name": s[0], "score": s[1]} for s in scores]
            conn.close()

            print("Replying to score request")

            # Send response
            self.send_response(200)
            self.send_header('Content-type', 'application/json')
            self.end_headers()
            self.wfile.write(json.dumps(scores).encode())


def run(server_class=HTTPServer, handler_class=ScoreHandler):
    server_address = ('', 8000)
    httpd = server_class(server_address, handler_class)
    print('Starting http server...')
    httpd.serve_forever()


if __name__ == "__main__":
    create_database()
    run()
