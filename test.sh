#!/bin/bash
echo -e 'GET /system/open/check/email-exist/lyh@lab.com:'
curl localhost:8080/system/open/check/email-exist/lyh@lab.com

echo -e '\n'
echo -e 'POST /system/open/login with body {"email": "lyh@lab.com","password": "lyh"}:'
curl localhost:8080/system/open/login \
  -X POST \
  -H "Content-Type: application/json" \
  -d '{"email": "lyh@lab.com","password": "lyh"}'


