# MessagesBoard API

Provides a basic API for sumbmitting and reading messages.
This project uses java, maven, jax-rs, jersey and grizzly2.

## Running Tests

The tests use junit. To run the tests use the command

`mvn test`

## Starting the server

To start the server use the command

`maven exec:java`

Or run the main class `com.example.Main`.
The API will the the be hosted at `http://localhost:8080/messageboard/`

## Using the service

Get the messages

`curl --location --request GET 'http://localhost:8080/messageboard/messages'`

Post a new message

`
curl --location --request POST 'http://localhost:8080/myapp/messages' \
 --header 'userid: 123' \
 --header 'Content-Type: application/json' \
 --data-raw '{
     "message": "my test message"
 }'
 `

Edit one of your messages

`
curl --location --request PUT 'http://localhost:8080/messageboard/messages/{messageId}' \
--header 'userid: 123' \
--header 'Content-Type: application/json' \
--data-raw '{
    "message": "edited message"
}'
`

Delete one of your messages

`curl --location --request DELETE 'http://localhost:8080/messageboard/messages/{messageid}' \
 --header 'userid: 123'`
