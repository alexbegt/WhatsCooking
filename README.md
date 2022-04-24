# WhatsCooking
What's Cooking Repository 


This Repo Contains both the app code and the web code required to connect the app to our database. While this could've been done directly in android, I decided to use php to make it easier in case the database has to change, and for a firewall perspective.

The PHP will be hosted on azure along with our database.

In order to make a change to the codebase, you must switch to your own branch for the change. To do this, use 'git checkout -b [branch_name]'. After calling the git commit command, you would push this up to github using: 'git push -u origin [branch_name]'.

After doing this, please create a PR request that will be reviewed before being merged in. Please do not merge in your own PR requests.