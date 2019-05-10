printf "\n\nLooking up screenings...\n\n"
curl localhost:8080/screenings/after/2019-05-09T15:49:01.549
printf "\n\nGoing to one of the screenings...\n\n"
curl localhost:8080/screenings/11
printf "\n\nChecking out seats filled in...\n\n"
curl localhost:8080/screenings/11/seats
printf "\n\nBooking some seats...\n\n"
printf "Input: {\"name\":\"Johnny\", \"surname\":\"Appleseed\", \"seats\":[{\"row\":2, \"column\":2, \"ticketType\":\"ADULT\"}, {\"row\":2, \"column\":3, \"ticketType\":\"CHILD\"}]} \n\n"
curl -X POST localhost:8080/screenings/11 -H 'Content-type:application/json' -d '{"name":"Johnny", "surname":"Appleseed", "seats":[{"row":2, "column":2, "ticketType":"ADULT"}, {"row":2, "column":3, "ticketType":"CHILD"}]}'
printf "\n\n"