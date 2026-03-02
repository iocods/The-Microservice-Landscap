#!/usr/bin/env bash

# Environment variable definition

: ${HOST=localhost}
: ${PORT=8080}
: ${PRODUCT_ID_OK=1}
: ${PRODUCT_ID_INVALID=-12}
: ${PRODUCT_ID_NOT_FOUND=13}
: ${PRODUCT_ID_NO_REV=224}
: ${PRODUCT_ID_NO_REC=114}


if [[ $@ == *"start"* ]]
then
  echo 'Beginning Tests for the services'
  echo 'Restarting all microservices in test environment'
  echo '$ docker-compose down --remove-orphans'
  docker-compose down --remove-orphans
  echo '$ docker compose up -d'
  docker-compose up -d
fi

function testUrl() {
  url=$@
  if curl $url -ks -f -o /dev/null
  then return 0
  else return 1
  fi
}

function waitForService() {
  url=$@
  n=0;
  echo -n "waiting for curl $url "
  until testUrl $url
  do
    n=$(($n+1))
    if [[ $n == 100 ]]
    then
      echo 'Aborting test retried failed'
    else
      sleep 3
      echo -n "Retry #$n "
    fi
  done
  echo "Done, Service is active proceeding with test cases..."
}

function assertCurl () {
  local expectedResponseStatusCode=$1;
  local curlCommand="$2 -w \"%{http_code}\""
  local result=$(eval "$curlCommand")
  local httpCode="${result:(-3)}"
  RESPONSE="" && (( ${#result} > 3)) && RESPONSE="${result%???}"


  if [ "$httpCode" = "$expectedResponseStatusCode" ]
  then
    if [ "$httpCode" = "200" ]
    then
      echo "Test OK (HTTP CODE: $httpCode)"
    else
      echo "Test OK (HTTP CODE: $httpCode, $RESPONSE)"
    fi
  else
    echo "Test FAILED, EXPECTED HTTP Code: $expectedResponseStatusCode. GOT $httpCode. WILL ABORT!"
    echo "- Failing command: $curlCommand"
    echo "- Response Body: $RESPONSE"
    exit 1
  fi
}

function assertEqual() {
  local expectedValue=$1
  local actualValue=$2

  if [ "$expectedValue" = "$actualValue" ]
  then
    echo "Test OK (actual value: $actualValue)"
  else
    echo "The Expected Value is: $expectedValue, but the actual value was: $actualValue"
    echo "Test Failed exiting from the process"
    exit 1
  fi
}

set -e

echo "HOST=${HOST}"
echo "PORT=${PORT}"

waitForService "http://${HOST}:${PORT}/product-composite/12"
assertCurl 200 "curl http://${HOST}:${PORT}/product-composite/${PRODUCT_ID_OK} -s"

assertEqual "$PRODUCT_ID_OK" $(echo "$RESPONSE" | jq .productId)
assertEqual 3 $(echo "$RESPONSE" | jq ".recommendations | length")
assertEqual 3 $(echo "$RESPONSE" | jq ".reviews | length")


assertCurl 422 "curl http://${HOST}:${PORT}/product-composite/${PRODUCT_ID_INVALID} -s"

assertEqual "Invalid Product ID detected: $PRODUCT_ID_INVALID" "$(echo $RESPONSE | jq -r .message)"


assertCurl 404 "curl http://${HOST}:${PORT}/product-composite/${PRODUCT_ID_NOT_FOUND} -s"

assertEqual "No Product found for product ID: $PRODUCT_ID_NOT_FOUND"  "$(echo "$RESPONSE" | jq -r .message)"

assertCurl 200 "curl http://${HOST}:${PORT}/product-composite/${PRODUCT_ID_NO_REV} -s"

assertEqual "$PRODUCT_ID_NO_REV" $(echo "$RESPONSE" | jq .productId)
assertEqual 3 $(echo "$RESPONSE" | jq ".recommendations | length")
assertEqual 0 $(echo "$RESPONSE" | jq ".reviews | length")

assertCurl 200 "curl http://${HOST}:${PORT}/product-composite/${PRODUCT_ID_NO_REC} -s"

assertEqual "$PRODUCT_ID_NO_REC" $(echo "$RESPONSE" | jq .productId)
assertEqual 0 $(echo "$RESPONSE" | jq ".recommendations | length")
assertEqual 3 $(echo "$RESPONSE" | jq ".reviews | length")


if [[ $@ == *"stop"* ]]
then
  echo 'Ending Tests for the services'
  echo 'stoping all microservices running in test environment'
  echo '$ docker-compose down --remove-orphans'
  docker-compose down --remove-orphans
fi

today=$(date +"%a %b %d %H:%M:%S %Z %Y")
echo "End all Tests OK : $today"