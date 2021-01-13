
#host=http://localhost:8080
host=https://gentle-ravine-66957.herokuapp.com

curl --header "Content-Type: application/json" --request POST --data '{"context":["1.0", "0.0", "0.0", "1.0", "1.0"]}'  "$host/action"

for i in {0..50}
    do
    	context1=$(printf '"1.0", "1.0", "1.0", "0.0", "0.0"%.0s' {1..10})
    	context2=$(printf '"0.0", "0.0", "0.0", "1.0", "1.0"%.0s' {1..10})
    	curl --header "Content-Type: application/json" --request POST --data '{"modelId":"0", "context":[$context1], "reward":"1.0"}' "$host/update" &
    	curl --header "Content-Type: application/json" --request POST --data '{"modelId":"0", "context":[$context2], "reward":"0.0"}' "$host/update" &
    	curl --header "Content-Type: application/json" --request POST --data '{"modelId":"1", "context":[$context1], "reward":"0.0"}' "$host/update" &
    	curl --header "Content-Type: application/json" --request POST --data '{"modelId":"1", "context":[$context2], "reward":"1.0"}' "$host/update" &
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