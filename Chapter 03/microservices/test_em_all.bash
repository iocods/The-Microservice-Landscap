#!/usr/bin/env bash

# Environment variable definition

: ${HOST=localhost}
: ${PORT=7000}
: ${PRODUCT_ID_OK=1}
: ${PRODUCT_ID_INVALID=-12}
: ${PRODUCT_ID_NOT_FOUND=13}
: ${PRODUCT_ID_NO_REV=224}
: ${PRODUCT_ID_NO_REC=114}


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