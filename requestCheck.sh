#!/bin/bash

#host=https://localhost:8080
host=https://shrouded-crag-78457.herokuapp.com/

context0=$(printf '"1.0", "0.0", "0.0", "1.0", "1.0", %.0s' {1..100})
context0='"1.0", "0.0"  '
curl --header "Content-Type: application/json" --request POST --data "{\"context\":[${context0::-2}]}" "$host/action"

# Define a timestamp function
timestamp() {
  date + # current time
}


for j in {0..600}
    do

    for i in {0..10}
        do
            context1=$(printf '"1.0", "1.0", "1.0", "0.0", "0.0", %.0s' {1..100})
            context2=$(printf '"0.0", "0.0", "0.0", "1.0", "1.0", %.0s' {1..100})
            context1='"1.0", "0.0"  '
            context2='"0.0", "1.0"  '
            curl --header "Content-Type: application/json" --request POST --data "{\"modelId\":\"0\", \"context\":[${context1::-2}], \"reward\":\"1.0\"}" "$host/update" &
            curl --header "Content-Type: application/json" --request POST --data "{\"modelId\":\"0\", \"context\":[${context2::-2}], \"reward\":\"0.0\"}" "$host/update" &
            curl --header "Content-Type: application/json" --request POST --data "{\"modelId\":\"1\", \"context\":[${context1::-2}], \"reward\":\"0.0\"}" "$host/update" &
            curl --header "Content-Type: application/json" --request POST --data "{\"modelId\":\"1\", \"context\":[${context2::-2}], \"reward\":\"1.0\"}" "$host/update" &
        done
    wait

    start=$SECONDS

    for i in {0..10}
        do
            curl --header "Content-Type: application/json" --request POST --data "{\"context\":[${context0::-2}]}" "$host/action" &
        done
    wait

    duration=$(( SECONDS - start ))

    echo $duration

    printf "j: "
    printf $j
    printf "   "
    dt=$(date '+%d/%m/%Y %H:%M:%S');
    echo "$dt"
    sleep 120
    done