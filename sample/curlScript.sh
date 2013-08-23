#!/bin/sh

# Your CBS Sports URL:
url=http://[CBS_BRACKET_ID].cbssports.com/brackets/standings/2013

# Use this chrome plug-in to create your CURL string:
#  https://chrome.google.com/webstore/detail/kojiki/ccdgjjhapajgljlffamojdedkpkgneio
curl -o los.html --cookie "[YOUR_COOKIES]" $url

java -jar LeagueOfSteveRank.jar los.html rankings.xml false "[PERSON_TO_RICK_ROLL]"

# Use s3cmd to PUSH the file to S3; needs to be setup
s3cmd put rankings.xml s3://[YOUR_BUCKET]/los/rankings.xml
s3cmd setacl --acl-public s3://[YOUR_BUCKET]/los/rankings.xml

