//
//  NSData-AES.h
//  TestUserVoice
//
//  Created by xiangdong liu on 6/24/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import <Foundation/Foundation.h>


@interface NSData (NSData_AES)
- (NSData *)AESEncryptWithPassphrase:(NSString *)pass;
@end
