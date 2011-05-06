Imports System
Imports System.Collections.Generic
Imports System.Linq
Imports System.Web
Imports System.Web.UI
Imports System.Web.UI.WebControls
Imports System.Configuration
Imports System.IO
Imports System.Security.Cryptography
Imports System.Text
Imports System.Web.Script.Serialization

Partial Class _Default
    Inherits System.Web.UI.Page

    Protected Class UserData
        Public email As String
        Public name As String
        Public expires As String

    End Class

    Protected Sub Page_Load(ByVal sender As Object, ByVal e As System.EventArgs) Handles Me.Load
        Dim user_data As New UserData()
        user_data.expires = Date.UtcNow.AddMinutes(2).ToString("s")
        user_data.email = "testing@domain.com"
        user_data.name = "vb.net Example"
        Dim encrypted_data As String = encryptUserData(user_data)
        Dim url As String = String.Format("http://multipass.ideascale.com/a/panel/do?multipass={0}", encrypted_data)
        Response.Redirect(url)
    End Sub

    Protected Shared Function encryptUserData(ByVal user_data As UserData) As String
        Dim s As New JavaScriptSerializer()
        Dim json_data As String = s.Serialize(user_data)

        Dim site_key As String = "12849"
        Dim api_key As String = "0d2c15da-b36f-4a9c-8f44-45d2669c3013-05e1fb36-54aa-44fc-888e-93eb95811e2e"
        Dim bIV As Byte() = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}

        Dim encrypted As Byte()
        Dim data As Byte() = Encoding.ASCII.GetBytes(json_data)
 
        Dim pad As Integer = 16 - (data.Length Mod 16)
        Array.Resize(data, data.Length + pad)
        For i = 0 To pad - 1
            data(data.Length - pad + i) = CType(pad, Byte)
        Next

        Using aesAlg As AesManaged = New AesManaged()

            aesAlg.IV = bIV
            aesAlg.KeySize = 16 * 8

            Dim sha1 As SHA1 = sha1.Create()
            Dim saltedHash = sha1.ComputeHash(Encoding.ASCII.GetBytes(api_key + site_key), 0, (api_key + site_key).Length)
            Array.Resize(saltedHash, 16)
            aesAlg.Key = saltedHash

            Dim encryptor As ICryptoTransform = aesAlg.CreateEncryptor()
            Using msEncrypt As MemoryStream = New MemoryStream()
                Using csEncrypt As CryptoStream = New CryptoStream(msEncrypt, encryptor, CryptoStreamMode.Write)
                    csEncrypt.Write(data, 0, data.Length)
                    csEncrypt.FlushFinalBlock()
                End Using
                encrypted = msEncrypt.ToArray()
            End Using

        End Using

        Return Convert.ToBase64String(encrypted, Base64FormattingOptions.None).TrimEnd("=".ToCharArray()).Replace("+", "-").Replace("/", "_")

    End Function

End Class
