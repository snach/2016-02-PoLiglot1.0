import requests, json

r = requests.post(
    'http://localhost:8080/api/user/',
    data = json.dumps({
        "login": "test1",
        "password": "test",
        "email":"123@test"
        }),
    headers = {'content-type': 'application/json'}
    )
r.status_code