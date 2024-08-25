from flask import Flask, json

test_app = Flask(__name__)


@test_app.get('/')
def home():
    return '', 200


@test_app.get("/generate/json/5")
def test():
    with open('test_data.json') as test_data:
        return json.load(test_data), 200


if __name__ == '__main__':
    test_app.run(host='0.0.0.0', port=5000, debug=True)

