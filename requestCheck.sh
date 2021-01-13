
#host=http://localhost:8080
host=https://salty-river-28824.herokuapp.com

curl --header "Content-Type: application/json" --request POST --data '{"context":["1.0", "0.0", "0.0", "1.0", "1.0"]}'  "$host/action"

for i in {0..50}
    do
    	curl --header "Content-Type: application/json" --request POST --data '{"modelId":"0", "context":["1.0", "1.0", "1.0", "0.0", "0.0"], "reward":"1.0"}' "$host/update" &
    	curl --header "Content-Type: application/json" --request POST --data '{"modelId":"0", "context":["0.0", "0.0", "0.0", "1.0", "1.0"], "reward":"0.0"}' "$host/update" &
    	curl --header "Content-Type: application/json" --request POST --data '{"modelId":"1", "context":["1.0", "1.0", "1.0", "0.0", "0.0"], "reward":"0.0"}' "$host/update" &
    	curl --header "Content-Type: application/json" --request POST --data '{"modelId":"1", "context":["0.0", "0.0", "0.0", "1.0", "1.0"], "reward":"1.0"}' "$host/update" &
    done
wait

start=$SECONDS

for i in {0..100}
    do
		curl --header "Content-Type: application/json" --request POST --data '{"context":["1.0", "0.0", "0.0", "1.0", "1.0"]}'  "$host/action" &
    done
wait

duration=$(( SECONDS - start ))

echo $duration