# RSS reader - the client part

Author: Krzysztof Danielak 

## Prerequisities
You need to have compiled both Client and [Server](https://github.com/krzydan/RSS_Server). 
You also need to generate a MS SQL database from database.sql script. You need at least SQL Server in version 14.00.1

## Usage

You need to run the Server first. Then you can run the Client. If you don't have an account yet, you can create a new one. New user has 3 default subscribed channels:
* www.tvn24.pl
* www.interia.pl
* www.cnn.com

When you log in, you will see posts from RSS channells you subscribed. When you click on "Czytaj więcej..." then the post will be opened in your main browser. If you want to add or change your channels, you need to click on "Kanały" and choose "Zarządzaj kanałami" from the menu or use ctrl + M shortcut. In the new window on the left you can see the list of the channels which you're currently subscriber. 
## Changing your channels
You can add new channel by clicking on "Nowy kanał" button. There you need to paste the URL of the desired RSS feed and confirm it with clicking on "Zapisz". If you want delete channel, click on "Usuń kanał". Don't click on "Kategorie kanału" as setting custom categories is not implemented fully.
When you're fine with changes you made, click on "Potwierdź zmiany" to save changes.
