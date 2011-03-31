<cfif isdefined("firstname")>
  <cfset fname = "#form.firstname#">
  <cfset lname = "#form.lastname#">
  <cfset expdate="#form.expirydate#">
  <cfset info_string=#fname# & #lname# & #expdate# >
  <cfset algorithm="AES">
  <cfset encoding = "Base64">
  <!---<cfset apikey="0d2c15da-b36f-4a9c-8f44-45d2669c3013-05e1fb36-54aa-44fc-888e-93eb95811e2e">--->
  <cfset apikey="http://support.ideascale.com/kb/ideascale-setup/single-sign-on-multipass-token-based
">
  <cfset salt=#apikey#>
  <cfset key=generateSecretKey(algorithm)>
  <!---<cfset key="12345">--->
  
  <!---Using hash()--->
  <cfset hashdata=hash(info_string)>
  <cfoutput>Hashed String is:#hashdata#</cfoutput>
  <!---using encrypt() and pass the hashdata with AES encryption standard--->
  <cfset encrypted_string = encrypt(hashdata,key,algorithm,encoding,salt)>
  <!--- In case of passing the encrypted string to the another page using session
  <cfapplication sessionmanagement="true">
  <cfset Session.encdata = "#encrypted_string#">
  <cflocation url="encryptddatapage.cfm">--->
  <cfset decrypted_string=decrypt(encrypted_string,key,algorithm,encoding,salt)>
   <cfoutput> 
   <cflocation url="http://multipass.ideascale.com/a/panel.do?multipass=#encrypted_string#">
 
  
 
    <div>Encrypted String is:#encrypted_string#</div>
    <br>
    <!---Not able to get source code back because hash() is used to encrypt the data and hash() is one time used function for encryption--->
    Decrypted String is:#decrypted_string#
	 
  </cfoutput>
</cfif>
<html>
<head>
<title>Encryption Example</title>
</head>
<body>
<cfform name="encform" method="post">
  <table border="1">
    <tr>
      <th align="center">Enter Details </th>
    </tr>
    <tr>
      <td>Firstname</td>
      <td><cfinput type="text" name="firstname" required="yes"></td>
    </tr>
    <tr>
      <td>Lastname</td>
      <td><cfinput type="text" name="lastname" required="yes"></td>
    </tr>
    <tr>
      <td>Expiry Date</td>
      <td><cfinput type="datefield" name="expirydate" required="yes" validate="date"></td>
    </tr>
    <tr>
      <td><cfinput type="submit" name="encbtn" value="Submit"></td>
    </tr>
  </table>
</cfform>
</body>
</html>
<encryption form.