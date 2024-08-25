from flask import Flask, request, jsonify

test_app = Flask(__name__)


@test_app.route('/')
def home():
    return '', 200


@test_app.get('/new/<int:size>')
def new(size: int):
    if size == 1001:
        return '', 200
    else:
        return '', 400


@test_app.get('/basic')
def basic():
    return 'basic heads', 200


@test_app.get('/select/<string:selected_heads>')
def selected(selected_heads: str):
    return selected_heads, 200


@test_app.post('/math')
def math():
    return ','.join(request.json.get('expressions')), 200


@test_app.get('/bridge')
def bridge():
    return jsonify({'cpu': {'cpu1': 1.1,
                            'cpu2': 1.2},
                    'memory': {'memory1': 1.1,
                               'memory2': 1.2},
                    'answerTime': {1: {'requestSize': 1,
                                       'responseTime': 1.1},
                                   2: {'requestSize': 2,
                                       'responseTime': 1.2}}}), 200


@test_app.get('/report')
def report():
    return jsonify({'cpu': {'cpu1': 2.1,
                            'cpu2': 2.2},
                    'memory': {'memory1': 2.1,
                               'memory2': 2.2},
                    'answerTime': {1: {'requestSize': 1,
                                       'responseTime': 2.1},
                                   2: {'requestSize': 2,
                                       'responseTime': 2.2}}}), 200


@test_app.errorhandler(404)
def not_found():
    return 'Not found', 404


if __name__ == '__main__':
    test_app.run(host='0.0.0.0', port=5000, debug=True)

