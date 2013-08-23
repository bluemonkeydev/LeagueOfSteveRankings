LeagueOfSteveRankings
=====================

NCAA Basketball Bracket Rankings Parser / RickRoll

Usage
=====
Edit  the provided "sample/curlScript.sh" and:
1. Add your CBS Bracket ID in the URL
2. Create your CURL cookies parameter, I suggest you use a Chrome plugin
2b. Log into CBS's site, go to the rankings page and extract the CURL cookies
2c. Add those as parameter
3. Put the user you want to RickRoll in as a parameter to the JAR call
4. Adjust the S3 bucket name to match YOUR S3 bucket.

What You Need
=============
1. AWS S3 Account & s3cmd tool
2. CURL (I recommend a Linux server)
3. Ability to setup a CRON job to automate the XML update
4. CBS NCAA Basketball bracket account
