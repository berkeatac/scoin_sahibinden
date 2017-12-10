from flask import request, url_for
from flask_api import FlaskAPI, status, exceptions
import requests, json
from threading import Timer, Thread
from time import sleep


app = FlaskAPI(__name__)
lst = []

def call_at_interval(period, callback, args):
    while True:
        sleep(period)
        callback(*args)

def setInterval(period, callback, *args):
    Thread(target=call_at_interval, args=(period, callback, args)).start()

def hello(word):
    response = requests.get('https://devakademi.sahibinden.com/ticker')
    resp_dict = json.loads(response.content)
    lastVal = resp_dict['value']
    lst.append(lastVal)
    if len(lst)> 100:
        lst.pop(0)
    print lst

@app.route("/", methods=['GET'])
def notes_list():
    # request.method == 'GET'
    return [lst]

if __name__ == "__main__":
    setInterval(5, hello, 'world!')
    app.run(host='0.0.0.0')









