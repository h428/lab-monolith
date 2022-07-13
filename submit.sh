#!/bin/bash

if [ $# != 1 ] ; then 
   echo "wrong params, string contains blank should begin and end with quote"
   exit 1;  
fi

msg=$1

echo "commit and push master to remote with message : $msg"
git add .
git commit -m "$msg"

git push origin master
