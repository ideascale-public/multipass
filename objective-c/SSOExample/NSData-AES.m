//
//  NSData-AES.m
//  TestUserVoice
//
//  Created by xiangdong liu on 6/24/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "NSData-AES.h"
#import <CommonCrypto/CommonDigest.h>
#import <CommonCrypto/CommonCryptor.h>

@implementation NSData (NSData_AES)
- (NSData *)AESEncryptWithPassphrase:(NSString *)pass
{
    
    
    
    uint8_t keyPtr[kCCKeySizeAES128+1]; // room for terminator (unused)
    bzero( keyPtr, sizeof(keyPtr) ); // fill with zeroes (for padding)
    
    const char *cstr = [pass cStringUsingEncoding:NSUTF8StringEncoding];
    uint8_t digest[CC_SHA1_DIGEST_LENGTH];
    CC_SHA1(cstr, [pass length], digest);
    memcpy(keyPtr, digest, kCCKeySizeAES128);
 
  const char ivc[16] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
 
    NSUInteger dataLength = [self length];
    size_t bufferSize = dataLength + kCCBlockSizeAES128;
	void *buffer = malloc(bufferSize);
    

    const unsigned char *odata = [self bytes];

    
     size_t numBytesEncrypted = 0;
    CCCryptorStatus cryptStatus = CCCrypt( kCCEncrypt,  kCCAlgorithmAES128, kCCOptionPKCS7Padding,
                                     keyPtr, kCCKeySizeAES128,
                                     ivc /* initialization vector (optional) */,
                                     odata, dataLength, /* input */
                                     buffer, bufferSize, /* output */
                                     &numBytesEncrypted );
    
//    free(ibuffer);
    if(cryptStatus == kCCSuccess) {
        return [NSData dataWithBytesNoCopy:buffer length:numBytesEncrypted];
    }
    free(buffer);
  
    return nil;

}

@end
