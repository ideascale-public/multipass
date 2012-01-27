<cfparam name="url.debug" default="0">	<!--- For testing purposes. When ready to go live, just change default to 0 --->

<cfset api_key = "0d2c15da-b36f-4a9c-8f44-45d2669c3013-05e1fb36-54aa-44fc-888e-93eb95811e2e">
<cfset app_key = "12849">

<cfset name = "Name"> <!--- String --->
<cfset email = "email@domain.com"> <!--- Valid Email Address --->
<cfset expires = "2099-12-31 23:59:59"> <!--- Valid Date Time --->
<cfset office = "OFFICE">

<cfobject type="java" class="com.ideascale.multipass.IdeaScaleTokenizer" name="tokenizerClass">
<cfset tokenizer = tokenizerClass.init(app_key,api_key)>

<cfscript>
 tokenizer.reset();
 tokenizer.setEmail(email);
 tokenizer.setName(name);
 tokenizer.setExpiration(expires);
 tokenizer.addAttrbiute('office',office);
 token = tokenizer.token();
</cfscript>

<cfif url.debug is 0>

	<!---
	// Example Login URL (below). Use a similar URL in your website.
	// Your URL: http://multipass.ideascale.com/a/panel.do?multipass=#{token}
	--->
	
	<!--- Redirect to ideascale --->
	<cflocation url="http://multipass.ideascale.com/a/panel.do?multipass=#trim(token)#" addtoken="no">
		
<cfelse>

	<cfdump var="#token#">

</cfif>
