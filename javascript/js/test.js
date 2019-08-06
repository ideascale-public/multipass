define(['node_modules/moment/moment', 'node_modules/crypto-js/sha1',
	'node_modules/crypto-js/aes', 'node_modules/crypto-js/enc-hex',
	'node_modules/crypto-js/enc-base64'], function( moment, SHA1, AES, HEX, Base64 ) {

	var conf ={
		name: '[Username]',
		sso_id: '[Login/email]',
		email: '[email]',
		api_token: '[admin api token]',
		app_key: '[your app key]',
		iv: [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
		base_url: 'https://ideascale.com/',
		base_path: 'a/rest/v1/'
	};

	var url = conf.base_url + conf.base_path + 'campaigns/active';

	function createSSOToken() {
		console.log('getSSOToken, called');
		var sso_json = {ssoId: conf.sso_id, email: conf.email, name: conf.name, expires: moment().add(5, 'm').format()};
		var salted_key = SHA1(conf.api_token + conf.app_key);
		var hash_key = salted_key.toString(Base64);
		var iv = new Int8Array(16);
		iv = conf.iv;

		console.log('getSSOToken, sso_json  : ' + JSON.stringify(sso_json));
		console.log('getSSOToken, hash_key  : ' + hash_key);
		console.log('getSSOToken, iv        : ' + iv);

		var sso_aes = AES.encrypt(JSON.stringify(sso_json), hash_key.substring(0, 16), {iv: iv});
		console.log('getSSOToken, sso_aes   : ' + sso_aes);
		var sso_token = sso_aes.toString();
		console.log('getSSOToken, sso_token1: ' + sso_token);

		sso_token = sso_token.replace(/\\n/g, '');
		sso_token = sso_token.replace(/(=+)$/g ,'');
		sso_token = sso_token.replace(/\+/g,'-');
		sso_token = sso_token.replace(/\\/g,'_');

		console.log('getSSOToken, sso_token2: ' + sso_token);
		return sso_token;
	};

	function getOptions() {
		console.log('getOptions, called');
		var sso_token = createSSOToken();

		return {
			method: 'GET',
			headers: {
				'Access-Control-Allow-Origin': conf.base_url,
				'Content-Type':	'application/json',
				'api_token': conf.api_token,
				'multipass': sso_token
			}
		};
	};

	window.fetch(url, getOptions())
		.then(res => {
			if (res.status >= 400) {
				throw new Error("Bad response from server");
			}
			return res.json();
		})
		.then(res => {
			console.log(res);
		})
		.catch(err => {
			console.error(err);
		});
});
