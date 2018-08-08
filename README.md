# scoin_sahibinden
This project consists of two parts.

  1) Android Application that can be found in the Scoin folder.
  2) Python based API Server
  
The android project uses the API provided by both sahibinden.com and the python server. The API is pulled using the library "Volley".

<img src="https://github.com/berkeatac/scoin_sahibinden/blob/master/device_screenshots/device-2017-12-10-162204.png" width="400">   <img src="https://github.com/berkeatac/scoin_sahibinden/blob/master/device_screenshots/device-2017-12-10-162231.png" width="400">

It utilises the python server to create a real time ticking graph for past ~10 minutes of scoin price. The python server gets the value of scoin each 5 seconds and stores the last 100 values in an array. This way when the user opens the app, they instantly can analyze the change in value in the last 10 minutes.

App also provides the value of scoin in other currencies by using an api by "currencylayer".

The application creates the historical data by using the history API and creating a graph using the library GraphView. This is the same way the real time graph is created.


<img src="https://github.com/berkeatac/scoin_sahibinden/blob/master/device_screenshots/device-2017-12-10-162247.png" width="400">   <img src="https://github.com/berkeatac/scoin_sahibinden/blob/master/device_screenshots/device-2017-12-10-162306.png" width="400">

There is also a buy/sell activity where the user has an initial credit of 10000 dollars and can buy and sell coins using this credit they have. The credit of the coins and the dollars are stored in the app as long as the main activity is alive. The user can buy and sell.

## IMPORTANT

" The url in the MainActivity of the android application that gets data from python api server should be changed to the

IP of the server that is currently running the script in the network. "
