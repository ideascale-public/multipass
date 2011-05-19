<?php

/**
 * IdeaScale Single Sign-On: Multipass-Compatible Token-Based Authentication
 * PHP Implementation (requires PHP >= 5.2)
 * Help Article:
 * http://support.ideascale.com/kb/ideascale-setup/single-sign-on-multipass-token-based
 */

class UserData
{
    public $email;
    public $name;
    public $expires;
}

class Application 
{
    // Replace api_key and app_key with your Community's API Key and Site Key
    // The keys below are for this community: http://multipass.ideascale.com/
    // Do not share your keys - the keys below are for testing purposes only.
    // See: http://support.ideascale.com/kb/ideascale-setup/single-sign-on-multipass-token-based
    private $api_key = "0d2c15da-b36f-4a9c-8f44-45d2669c3013-05e1fb36-54aa-44fc-888e-93eb95811e2e";
    private $app_key = "12849";

    public function run()
    {
       $user = new UserData();
       $date = new DateTime(null, new DateTimeZone('UTC'));
       $date->modify('+1 minute');
       $user->expires = $date->format('c');
       $user->email = "testing@domain.com";
       $user->name = "PHP5 Example";
       $encrypted_data = $this->encryptUserData($user);
       // Example login URL
       printf("http://multipass.ideascale.com/a/panel.do?multipass=%s", $encrypted_data);
    }

    private function encryptUserData($user_data)
    {
        $app_key = $this->app_key;
        $api_key = $this->api_key;
        $json = json_encode($user_data);

        $salted = $api_key . $app_key;
        $saltedHash = substr(sha1($salted, true), 0, 16);

        $pad = 16 - (strlen($json) % 16);
        $data = $json . (str_repeat(chr($pad), $pad));

        if (!function_exists('mcrypt_encrypt'))
            throw new Exception('Mcrypt extension is not installed for PHP.');
        $aes = mcrypt_encrypt(MCRYPT_RIJNDAEL_128, $saltedHash, $data, MCRYPT_MODE_CBC, str_repeat("\0", 16));

        $b64token = base64_encode($aes);
        $b64token = rtrim(str_replace(array('+', '/'), array('-', '_'), $b64token), '=');
        
        return $b64token;
    }
}

$app = new Application();
$app->run();

?>