//
//  SSOHelper.m
//  SSOExample
//
//  Created by xiangdong liu on 8/16/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "SSOHelper.h"
#import "NSData-AES.h"
#import "NSData-Base64.h"
#import "JSONKit.h"

@implementation SSOHelper

+ (NSString*) getSSOWithEmail:(NSString*)email DisplayName:(NSString*) displayName {
  
  
  NSString *expires = @"2019-08-14T16:00:00.000-07:00";
  
  NSArray *keys = [NSArray arrayWithObjects:@"email",@"name",@"expires",nil];
  
  NSArray *values = [NSArray arrayWithObjects:email,displayName,expires,nil];
  
  NSDictionary *dict = [NSDictionary dictionaryWithObjects:values forKeys:keys];
  
  NSString *api_key = @"0d2c15da-b36f-4a9c-8f44-45d2669c3013-05e1fb36-54aa-44fc-888e-93eb95811e2e";
  NSString *app_key = @"12849";
  NSString *passPhrase = [api_key stringByAppendingString:app_key] ;
  
  
  
  NSString *jsonDict = [dict JSONString];
  
  NSData *dataDict = [jsonDict dataUsingEncoding:NSUTF8StringEncoding];
  
  NSData * aesData = [dataDict AESEncryptWithPassphrase:passPhrase];
  
  NSString* b64Str = [NSData encodeKLBase64:aesData];
  
  
  NSString *result = [[[b64Str stringByReplacingOccurrencesOfString:@"=" withString:@""] 
                       stringByReplacingOccurrencesOfString:@"+" withString:@"-"] 
                      stringByReplacingOccurrencesOfString:@"/" withString:@"_"];
  return result;
}


@end
