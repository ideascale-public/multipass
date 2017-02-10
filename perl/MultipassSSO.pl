#!/usr/bin/perl

use strict;
use warnings;

use Digest::SHA;
use Crypt::Mode::CBC;
use MIME::Base64;
use DateTime;
use DateTime::Duration;
use JSON;

sub Run {
    # needed keys
    my $APIKey  = '0d2c15da-b36f-4a9c-8f44-45d2669c3013-05e1fb36-54aa-44fc-888e-93eb95811e2e';
    my $SiteKey = '12849';

    my $ExpireDateTime = DateTime->now()->add(minutes => 1)->datetime();

    # userdata
    my %UserData;
    $UserData{email} = 'testing@domain.com';
    $UserData{name}  = 'Perl Programming Language Example';
    $UserData{expires} = $ExpireDateTime;

    # create json object and encode userdata
    my $JSONObject = JSON->new();
    $JSONObject->allow_nonref(1);
    my $UserDataJSONString = $JSONObject->encode( \%UserData );

    my $Salted    = $APIKey . $SiteKey;
    my $SHAObject = Digest::SHA->new('sha1');
    $SHAObject->add($Salted);
    my $SaltedHash = $SHAObject->digest();
    $SaltedHash = substr $SaltedHash, 0, 16;

    my $Length = length $UserDataJSONString;
    my $Pad    = 16 - ($Length % 16);
    my $Char   = chr $Pad;
    my $Data   = $UserDataJSONString . $Char x $Pad;

    # encrypt SaltedHash and encode with base64
    my $CryptCBCObject = Crypt::Mode::CBC->new('AES', 0);
    my $Binary = $CryptCBCObject->encrypt( $Data, $SaltedHash, "\0" x 16 );
    my $Binary64Token = MIME::Base64::encode_base64($Binary);

    # remove new lines and replace unwanted chars
    $Binary64Token =~ s{[\n\r\f]}{}xmsg;
    $Binary64Token =~ s{\+}{-}xmsg;
    $Binary64Token =~ s{/}{_}xmsg;
    $Binary64Token =~ s{=}{}xmsg;

    print "http://multipass.ideascale.com/a/panel.do?multipass=$Binary64Token" . "\n";
}

Run();

exit 0;
