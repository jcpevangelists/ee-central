# The tomitribe.io website resources compiler

Start TomEE

    mvn clean install tomee:run

Dependency on nodejs *0.12.7*. On Debian, install it with:

    curl -sL https://deb.nodesource.com/setup_0.12 | sudo bash -
    
Once nodejs is installed, you can install the node dependencies locally.

    npm install

Now start the static assembly process by executing:
    
    gulp
    
Every change under *./static* will trigger a rebuild. It is not necessary to restart TomEE in order to reflect static 
resources updates.

Link to the application: http://localhost:8080/tomitribe-io