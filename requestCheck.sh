start=$SECONDS

for i in {0..150}
    do
        #curl -X GET  https://polar-fjord-37505.herokuapp.com/action &
        curl -X GET http://localhost:8080/action
    done
wait

duration=$(( SECONDS - start ))

echo $duration