# touk-cinema

## What is it

It's a ticket booking server app prepared for purposes of recruitment to an internship at TouK.

# How to build and run it

Easiest option: import the repository to an IDE and run it from there.

Another option: run mvnw file provided in the repository.

# How to test it

Run test.sh file provided in the repository.

# Assumptions made

* The user wants to see all films they can reserve - so no upper time limit.
* The state of how the room looks like (how filled it is) is shown by displaying seat reservations rather than empty seats).
* Reservations end 15 minutes before the screening starts.
* There will be no need to update anything - if the user/the cinema makes a mistake, it'll notice it fast enough to delete created object and create a corrected version before anyone takes notice.
* No security *per se* is provided, although the structure allows for easy authentication enabling (e.g. reservations are under a single class, so their views can be easily blocked).
