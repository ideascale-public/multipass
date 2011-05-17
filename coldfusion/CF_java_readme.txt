
Install the following files to your Coldfusion Server:

1) ideascale-multipass-sso.jar,
2) joda-time-1.6.jar,
3) commons-codec-1.4.jar,
4) json-dep.jar

You can install them in either of the following methods:

1)	Install the files to the Coldfusion Classpath directory.  You will find this directory on your coldfusion server at {cf_installation}/lib.  Typically that directory will be c:\Colfusion[version]\lib.

Once you have copied the files listed above to that directory.  Restart the Coldfusion Application Service.  You can find it listed in your services in the Administration Tools folder from the Start Menu or by “right clicking” on “My Computer” and “Manage.”


2)	Install the files in the directory of your choice.

Once the files have been copied to the server, you must add the Coldfusion Class Path to the Coldfusion Administrator.  Log into the Coldfusoin Administrator and select “Java and JVM” from the menu on the left hand side of the screen.

You will need to place the entire path of each file in the Coldfusion Class Path text area.  Each file must be separated by a comma (,).  It may look like this: 

c:\inetpub\wwwroot\website\multipass\ideascale-multipass-sso.jar,c:\inetpub\wwwroot\website\multipass\joda-time-1.6.jar,c:\inetpub\wwwroot\website\multipass\commons-codec-1.4.jar,c:\inetpub\wwwroot\website\multipass\json-dep.jar

Once you have installed the files and updated the Coldfusion Administrator, you must restart the Coldfusion Service so the changes will take affect.
