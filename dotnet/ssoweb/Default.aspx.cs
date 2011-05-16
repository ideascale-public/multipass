using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Configuration;
using System.IO;
using System.Security.Cryptography;
using System.Text;
using System.Web.Script.Serialization;

// Tweeking .net code from an SSO Blog Post:
// http://sypher-news.blogspot.com/2011/03/assistly-multipass-sso.html

public partial class _Default : System.Web.UI.Page
{
    protected class UserData
    {
        public string email;
        public string name;
        public string expires;
    }
    protected void Page_Load(object sender, EventArgs e)
    {
        UserData user_data = new UserData();
        user_data.expires = DateTime.UtcNow.AddMinutes(2).ToString("s");
        user_data.email = "testing@domain.com";
        user_data.name = "dotNet Example";
        string encrypted_data = encryptUserData(user_data);
        string url = string.Format("http://multipass.ideascale.com/a/panel.do?multipass={0}", encrypted_data);
        Response.Redirect(url);
    }

    protected static string encryptUserData(UserData user_data)
    {
        // Encode the data into a JSON object
        JavaScriptSerializer s = new JavaScriptSerializer();
        string json_data = s.Serialize(user_data);

        // Example of web.config configuration/appSettings section:
        string site_key = "12849";
        string api_key = "0d2c15da-b36f-4a9c-8f44-45d2669c3013-05e1fb36-54aa-44fc-888e-93eb95811e2e";
        byte[] bIV = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        // Using byte arrays now instead of strings
        byte[] encrypted = null;
        byte[] data = Encoding.ASCII.GetBytes(json_data);

        // Pad using block size of 16 bytes
        int pad = 16 - (data.Length % 16);
        Array.Resize(ref data, data.Length + pad);
        for (var i = 0; i < pad; i++)
            data[data.Length - pad + i] = (byte)pad;

        // Use the AesManaged object to do the encryption
        using (AesManaged aesAlg = new AesManaged())
        {
            aesAlg.IV = bIV;
            aesAlg.KeySize = 16 * 8;

            // Create the 16-byte salted hash
            SHA1 sha1 = SHA1.Create();
            byte[] saltedHash = sha1.ComputeHash(Encoding.ASCII.GetBytes(api_key + site_key), 0, (api_key + site_key).Length);
            Array.Resize(ref saltedHash, 16);
            aesAlg.Key = saltedHash;

            // Encrypt using the AES Managed object
            ICryptoTransform encryptor = aesAlg.CreateEncryptor();
            using (MemoryStream msEncrypt = new MemoryStream())
            {
                using (CryptoStream csEncrypt = new CryptoStream(msEncrypt, encryptor, CryptoStreamMode.Write))
                {
                    csEncrypt.Write(data, 0, data.Length);
                    csEncrypt.FlushFinalBlock();
                }
                encrypted = msEncrypt.ToArray();
            }
        }

        // Return the Base64-encoded encrypted data
        return Convert.ToBase64String(encrypted, Base64FormattingOptions.None)
            .TrimEnd("=".ToCharArray())    // Remove trailing equal (=) characters
            .Replace("+", "-")            // Change any plus (+) characters to dashes (-)
            .Replace("/", "_");            // Change any slashes (/) characters to underscores (_)
    }
}