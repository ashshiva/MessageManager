#!/bin/bash
MMHOST="http://localhost:8080"
unset ACTION MESSAGE ID

print_help() {
    echo "usage: mmcli [OPTIONS]
   Options:
   -h          Prints help.
   -H          Message Manager host to connect to (default http://localhost:8080).       
   -a          Action to be taken:
                [create]   Creates a message with the given string (example: mmcli -a create -m 'hello world').
                [get]      Gets a message with a given id (example: mmcli -a get -i 1).
                [update]   Updates a message with a given id to the given string (example: mmcli -a update -i 1 -m 'hello ottawa').
                [delete]   Deletes a message with a given id (example: mmcli -a delete -i 1).
                [listall]  Lists all the messages (example: mmcli -a listall).
   -m          Message to be passed to MessageManager web app. Only needed for 'create' and 'update' actions.
   -i          Message id to be passed to MessageManager web app. Only needed for 'get', 'update' and 'delete' actions.
   "
}

preflight_check() {
    if [ -z "$(command -v curl)" ] ; then
        echo "error: Missing 'curl'. Please install 'curl' and try again."
        exit;
    fi
    if [ -z "$(command -v jq)" ] ; then
        echo "error: Missing 'jq'. Please install 'jq' and try again."
        exit;
    fi
}

listall() {
    echo "Host: $MMHOST - Listing all messages"
    response=$(curl -X GET -sw '%{http_code}' --header 'Accept: application/json' "$MMHOST/api/v1/messages")
    resp_code=${response:${#response}-3}
    if [ $resp_code == 200 ]; then
        echo "Retrieved all the messages successfully"
        msg=${response:0:${#response}-3}
        len=$(echo $msg | jq '. | length')
        if [ "$len" -gt 0 ]; then
            echo -e "ID\tIs palindrome\tCreated At\t\t\tUpdated At\t\t\tMessage Content"
            echo -e "---\t--------------\t-----------\t\t\t-------------\t\t\t-----------------"
            echo $msg | jq -r '.[] | "\(.id)\t\(.palindrome)\t\t\(.createdAt)\t\(.updatedAt)\t\(.message)"'
        else
            echo "There are no messages to display"
        fi
    else 
        echo "Unable to get all the messages. Please try again later."
    fi
}

get() {
    if [ -z "$ID" ]; then 
        echo "error: Please provide a message id to retrieve"
        exit 1 
    fi
    echo "Host: $MMHOST - Getting message with ID: $ID"
    response=$(curl -X GET -sw '%{http_code}' --header 'Accept: application/json' "$MMHOST/api/v1/messages/$ID")
    resp_code=${response:${#response}-3}
    if [ $resp_code == 200 ]; then
        echo "Retrieved Message with ID: $ID"
        msg=${response:0:${#response}-3}
        echo "Message content: $(echo $msg | jq -r '.message'), Is palindrome: $(echo $msg | jq -r '.palindrome')"
        echo "Created At: $(echo $msg | jq -r '.createdAt'), Updated At: $(echo $msg | jq -r '.updatedAt')"
    elif [ $resp_code == 404 ]; then
        echo "Message with the given id could not be found."
    elif [ $resp_code == 400 ]; then
        echo "Unable to retrieve the message. Please check the provided message id."
    else 
        echo "Unable to retrieve the message. Please try again later."
    fi
}

create() {
    if [ -z "$MESSAGE" ]; then 
        echo "error: Please provide a message string"
        exit 1 
    fi
    DATA='{
    "message": "'$MESSAGE'"
    }'
    echo "Host: $MMHOST - Creating message with content: '$MESSAGE'"
    response=$(curl -X POST -sw '%{http_code}' --header 'Content-Type: application/json' --header 'Accept: application/json' \
            -d "$DATA" "$MMHOST/api/v1/messages")
    resp_code=${response:${#response}-3}
    if [ $resp_code == 201 ]; then
        msg=${response:0:${#response}-3}
        echo "Successfully created message and it's id is: $(echo $msg | jq -r '.id')"
        echo "Message content: $(echo $msg | jq -r '.message'), Is palindrome: $(echo $msg | jq -r '.palindrome')"
        echo "Created At: $(echo $msg | jq -r '.createdAt')"
    elif [ $resp_code == 400 ]; then
        echo "Unable to create the message. Please check the provided message."
    else 
        echo "Unable to create the message. Please try again later."
    fi
}

update() {
    if [ -z "$ID" ]; then 
        echo "error: Please provide a message id to update"
        exit 1 
    fi
    if [ -z "$MESSAGE" ]; then 
        echo "error: Please provide a message string"
        exit 1 
    fi
    DATA='{ 
    "id": '$ID', 
    "message": "'$MESSAGE'"
    }'
    echo "Host: $MMHOST - Updating message with id: $ID to '$MESSAGE'"
    response=$(curl -X PUT -sw '%{http_code}' --header 'Content-Type: application/json' --header 'Accept: application/json' \
            -d "$DATA" "$MMHOST/api/v1/messages/$ID")
    resp_code=${response:${#response}-3}
    if [ $resp_code == 200 ]; then
        msg=${response:0:${#response}-3}
        echo "Successfully updated message with id: $ID"
        echo "Updated message content: $(echo $msg | jq -r '.message'), Is palindrome: $(echo $msg | jq -r '.palindrome')"
        echo "Updated At: $(echo $msg | jq -r '.updatedAt')"
    elif [ $resp_code == 400 ]; then
        echo "Unable to update the message. Please check the provided id and message."
    elif [ $resp_code == 404 ]; then
        echo "Message with the given id could not be found."
    else 
        echo "Unable to update the message. Please try again later."
    fi
}

delete() {
    if [ -z "$ID" ]; then 
        echo "error: Please provide a message id to delete"
        exit 1 
    fi
    echo "Host: $MMHOST - Deleting message with id: $ID"
    response=$(curl -X DELETE -sw '%{http_code}' --header 'Accept: */*' "$MMHOST/api/v1/messages/$ID")
    resp_code=${response:${#response}-3}
    if [ $resp_code == 200 ]; then
        echo "Message with id: $ID was successfully deleted"
    elif [ $resp_code == 400 ]; then
        echo "Unable to delete the message. Please check the provided message id."
    elif [ $resp_code == 404 ]; then
        echo "Message with the given id could not be found."
    else 
        echo "Unable to delete the message. Please try again later."
    fi
}

preflight_check

while getopts "hH:a:m:i:" opt; do
  case $opt in
    H)
      MMHOST=$OPTARG
      ;;
    a)
      ACTION=$OPTARG
      ;;
    m)
      MESSAGE=$OPTARG
      ;;
    i)
      ID=$OPTARG
      ;;
    h)
      print_help
      ;;
    \?)
      echo "Invalid option"
      exit 1
      ;;
  esac
done

case $ACTION in
    create|get|update|delete|listall)
      $ACTION
      ;;
    *)
      echo "error: Invalid action"
      exit 1
      ;;
esac

echo