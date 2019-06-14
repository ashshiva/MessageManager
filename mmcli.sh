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
}

listall() {
    echo "Host: $MMHOST - Listing all messages"
    curl -X GET --header 'Accept: application/json' "$MMHOST/api/v1/messages"
}

get() {
    if [ -z "$ID" ]; then 
        echo "error: Please provide a message id to retrieve"
        exit 1 
    fi
    echo "Host: $MMHOST - Getting message with ID: $ID"
    curl -X GET --header 'Accept: application/json' "$MMHOST/api/v1/messages/$ID"
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
    curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d "$DATA" "$MMHOST/api/v1/messages"
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
    curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' -d "$DATA" "$MMHOST/api/v1/messages/$ID"
}

delete() {
    if [ -z "$ID" ]; then 
        echo "error: Please provide a message id to delete"
        exit 1 
    fi
    echo "Host: $MMHOST - Deleting message with id: $ID"
    curl -X DELETE --header 'Accept: */*' "$MMHOST/api/v1/messages/$ID"
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